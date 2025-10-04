package de.noctivag.plugin.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Network Communication - Server-zu-Server Kommunikation
 * 
 * Verantwortlich für:
 * - Echtzeit-Kommunikation zwischen Servern
 * - Message Queue System
 * - Event Broadcasting
 * - Request-Response Pattern
 * - Data Synchronization
 */
public class NetworkCommunication {
    
    private final Plugin plugin;
    private final JedisPool jedisPool;
    private final String serverId;
    
    private final Map<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, MessageHandler> messageHandlers = new ConcurrentHashMap<>();
    private final List<NetworkEventListener> eventListeners = new ArrayList<>();
    
    private boolean isListening = false;
    private JedisPubSub pubSub;
    
    public NetworkCommunication(Plugin plugin, JedisPool jedisPool, String serverId) {
        this.plugin = plugin;
        this.jedisPool = jedisPool;
        this.serverId = serverId;
        
        startListening();
    }
    
    private void startListening() {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                pubSub = new NetworkMessageSubscriber();
                isListening = true;
                
                // Subscribe to server-specific channel
                jedis.subscribe(pubSub, "server:" + serverId);
                
                // Subscribe to global channels
                jedis.subscribe(pubSub, "global", "events", "requests");
                
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to start network communication: " + e.getMessage());
                isListening = false;
            }
        }).start();
    }
    
    public void stopListening() {
        if (pubSub != null && isListening) {
            pubSub.unsubscribe();
            isListening = false;
        }
    }
    
    // Send message to specific server
    public boolean sendMessage(String targetServerId, String messageType, Map<String, String> data) {
        try (Jedis jedis = jedisPool.getResource()) {
            String messageId = generateMessageId();
            String messageKey = "message:" + messageId;
            
            // Store message data
            jedis.hset(messageKey, "id", messageId);
            jedis.hset(messageKey, "from", serverId);
            jedis.hset(messageKey, "to", targetServerId);
            jedis.hset(messageKey, "type", messageType);
            jedis.hset(messageKey, "timestamp", String.valueOf(System.currentTimeMillis()));
            
            // Add custom data
            for (Map.Entry<String, String> entry : data.entrySet()) {
                jedis.hset(messageKey, entry.getKey(), entry.getValue());
            }
            
            // Set expiration
            jedis.expire(messageKey, 300); // 5 minutes
            
            // Send message
            jedis.publish("server:" + targetServerId, messageId);
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send message: " + e.getMessage());
            return false;
        }
    }
    
    // Send request and wait for response
    public CompletableFuture<String> sendRequest(String targetServerId, String requestType, 
                                               Map<String, String> data, long timeoutSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            String requestId = generateMessageId();
            String requestKey = "request:" + requestId;
            
            // Store request data
            jedis.hset(requestKey, "id", requestId);
            jedis.hset(requestKey, "from", serverId);
            jedis.hset(requestKey, "to", targetServerId);
            jedis.hset(requestKey, "type", requestType);
            jedis.hset(requestKey, "timestamp", String.valueOf(System.currentTimeMillis()));
            
            // Add custom data
            for (Map.Entry<String, String> entry : data.entrySet()) {
                jedis.hset(requestKey, entry.getKey(), entry.getValue());
            }
            
            // Set expiration
            jedis.expire(requestKey, timeoutSeconds);
            
            // Create future for response
            CompletableFuture<String> future = new CompletableFuture<>();
            pendingRequests.put(requestId, future);
            
            // Send request
            jedis.publish("requests", requestId);
            
            // Set timeout
            CompletableFuture.delayedExecutor(timeoutSeconds, TimeUnit.SECONDS)
                    .execute(() -> {
                        if (pendingRequests.containsKey(requestId)) {
                            pendingRequests.remove(requestId);
                            future.completeExceptionally(new RuntimeException("Request timeout"));
                        }
                    });
            
            return future;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send request: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
    
    // Send response to request
    public boolean sendResponse(String requestId, String responseData) {
        try (Jedis jedis = jedisPool.getResource()) {
            String responseKey = "response:" + requestId;
            
            jedis.hset(responseKey, "requestId", requestId);
            jedis.hset(responseKey, "from", serverId);
            jedis.hset(responseKey, "data", responseData);
            jedis.hset(responseKey, "timestamp", String.valueOf(System.currentTimeMillis()));
            
            jedis.expire(responseKey, 300); // 5 minutes
            
            // Send response
            jedis.publish("responses", requestId);
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send response: " + e.getMessage());
            return false;
        }
    }
    
    // Broadcast event to all servers
    public boolean broadcastEvent(String eventType, Map<String, String> data) {
        try (Jedis jedis = jedisPool.getResource()) {
            String eventId = generateMessageId();
            String eventKey = "event:" + eventId;
            
            // Store event data
            jedis.hset(eventKey, "id", eventId);
            jedis.hset(eventKey, "from", serverId);
            jedis.hset(eventKey, "type", eventType);
            jedis.hset(eventKey, "timestamp", String.valueOf(System.currentTimeMillis()));
            
            // Add custom data
            for (Map.Entry<String, String> entry : data.entrySet()) {
                jedis.hset(eventKey, entry.getKey(), entry.getValue());
            }
            
            jedis.expire(eventKey, 300); // 5 minutes
            
            // Broadcast event
            jedis.publish("events", eventId);
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to broadcast event: " + e.getMessage());
            return false;
        }
    }
    
    // Register message handler
    public void registerMessageHandler(String messageType, MessageHandler handler) {
        messageHandlers.put(messageType, handler);
    }
    
    // Register event listener
    public void registerEventListener(NetworkEventListener listener) {
        eventListeners.add(listener);
    }
    
    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
    
    // Network Message Subscriber
    private class NetworkMessageSubscriber extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            try {
                if (channel.startsWith("server:")) {
                    handleServerMessage(message);
                } else if (channel.equals("requests")) {
                    handleRequest(message);
                } else if (channel.equals("responses")) {
                    handleResponse(message);
                } else if (channel.equals("events")) {
                    handleEvent(message);
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to handle message: " + e.getMessage());
            }
        }
        
        private void handleServerMessage(String messageId) {
            try (Jedis jedis = jedisPool.getResource()) {
                String messageKey = "message:" + messageId;
                Map<String, String> messageData = jedis.hgetAll(messageKey);
                
                if (!messageData.isEmpty()) {
                    String messageType = messageData.get("type");
                    MessageHandler handler = messageHandlers.get(messageType);
                    
                    if (handler != null) {
                        handler.handleMessage(messageData);
                    }
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to handle server message: " + e.getMessage());
            }
        }
        
        private void handleRequest(String requestId) {
            try (Jedis jedis = jedisPool.getResource()) {
                String requestKey = "request:" + requestId;
                Map<String, String> requestData = jedis.hgetAll(requestKey);
                
                if (!requestData.isEmpty()) {
                    String targetServer = requestData.get("to");
                    if (serverId.equals(targetServer)) {
                        String requestType = requestData.get("type");
                        MessageHandler handler = messageHandlers.get(requestType);
                        
                        if (handler != null) {
                            String response = handler.handleRequest(requestData);
                            sendResponse(requestId, response);
                        }
                    }
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to handle request: " + e.getMessage());
            }
        }
        
        private void handleResponse(String requestId) {
            try (Jedis jedis = jedisPool.getResource()) {
                String responseKey = "response:" + requestId;
                Map<String, String> responseData = jedis.hgetAll(responseKey);
                
                if (!responseData.isEmpty()) {
                    CompletableFuture<String> future = pendingRequests.remove(requestId);
                    if (future != null) {
                        String response = responseData.get("data");
                        future.complete(response);
                    }
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to handle response: " + e.getMessage());
            }
        }
        
        private void handleEvent(String eventId) {
            try (Jedis jedis = jedisPool.getResource()) {
                String eventKey = "event:" + eventId;
                Map<String, String> eventData = jedis.hgetAll(eventKey);
                
                if (!eventData.isEmpty()) {
                    String eventType = eventData.get("type");
                    
                    // Notify all event listeners
                    for (NetworkEventListener listener : eventListeners) {
                        try {
                            listener.onEvent(eventType, eventData);
                        } catch (Exception e) {
                            plugin.getLogger().severe("Event listener error: " + e.getMessage());
                        }
                    }
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to handle event: " + e.getMessage());
            }
        }
    }
    
    // Message Handler Interface
    public interface MessageHandler {
        void handleMessage(Map<String, String> messageData);
        String handleRequest(Map<String, String> requestData);
    }
    
    // Network Event Listener Interface
    public interface NetworkEventListener {
        void onEvent(String eventType, Map<String, String> eventData);
    }
    
    // Utility methods for common operations
    public boolean syncPlayerData(UUID playerId, Map<String, String> playerData) {
        Map<String, String> data = new HashMap<>(playerData);
        data.put("playerId", playerId.toString());
        return broadcastEvent("player_data_sync", data);
    }
    
    public boolean syncIslandData(String islandId, Map<String, String> islandData) {
        Map<String, String> data = new HashMap<>(islandData);
        data.put("islandId", islandId);
        return broadcastEvent("island_data_sync", data);
    }
    
    public boolean notifyPlayerJoin(UUID playerId, String serverId) {
        Map<String, String> data = new HashMap<>();
        data.put("playerId", playerId.toString());
        data.put("serverId", serverId);
        return broadcastEvent("player_join", data);
    }
    
    public boolean notifyPlayerLeave(UUID playerId, String serverId) {
        Map<String, String> data = new HashMap<>();
        data.put("playerId", playerId.toString());
        data.put("serverId", serverId);
        return broadcastEvent("player_leave", data);
    }
    
    public boolean notifyServerShutdown(String serverId) {
        Map<String, String> data = new HashMap<>();
        data.put("serverId", serverId);
        return broadcastEvent("server_shutdown", data);
    }
    
    public boolean notifyServerStartup(String serverId) {
        Map<String, String> data = new HashMap<>();
        data.put("serverId", serverId);
        return broadcastEvent("server_startup", data);
    }
    
    public boolean isListening() {
        return isListening;
    }
    
    public String getServerId() {
        return serverId;
    }
}
