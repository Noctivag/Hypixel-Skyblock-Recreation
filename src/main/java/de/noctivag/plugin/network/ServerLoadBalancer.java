package de.noctivag.plugin.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Server Load Balancer - Intelligente Lastverteilung zwischen Servern
 * 
 * Verantwortlich für:
 * - Dynamische Lastverteilung
 * - Server-Health-Monitoring
 * - Intelligente Server-Auswahl
 * - Auto-Scaling-Unterstützung
 * - Performance-Optimierung
 * - Failover-Management
 */
public class ServerLoadBalancer {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final ServerCommunicationManager communicationManager;
    
    private final Map<NetworkArchitecture.ServerType, List<ServerInfo>> serverPools = new ConcurrentHashMap<>();
    private final Map<String, ServerMetrics> serverMetrics = new ConcurrentHashMap<>();
    private final Map<String, LoadBalancingStrategy> strategies = new ConcurrentHashMap<>();
    
    // Load Balancing Konfiguration
    private final int maxServersPerType = 10;
    private final double healthThreshold = 0.7;
    private final double loadThreshold = 0.8;
    private final long metricsUpdateInterval = 5000; // 5 Sekunden
    
    public ServerLoadBalancer(Plugin plugin, MultiServerDatabaseManager databaseManager,
                             ServerCommunicationManager communicationManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.communicationManager = communicationManager;
        
        initializeStrategies();
    }
    
    /**
     * Initialisiert Load Balancing-Strategien
     */
    private void initializeStrategies() {
        strategies.put("round_robin", new RoundRobinStrategy());
        strategies.put("least_connections", new LeastConnectionsStrategy());
        strategies.put("weighted_round_robin", new WeightedRoundRobinStrategy());
        strategies.put("least_response_time", new LeastResponseTimeStrategy());
        strategies.put("resource_based", new ResourceBasedStrategy());
    }
    
    /**
     * Findet den besten Server für einen Request
     */
    public CompletableFuture<ServerInfo> findBestServer(NetworkArchitecture.ServerType serverType,
                                                       Player player, LoadBalancingRequest request) {
        CompletableFuture<ServerInfo> future = new CompletableFuture<>();
        
        try {
            // Aktualisiere Server-Metriken
            updateServerMetrics();
            
            // Filtere verfügbare Server
            List<ServerInfo> availableServers = getAvailableServers(serverType);
            
            if (availableServers.isEmpty()) {
                future.complete(null);
                return future;
            }
            
            // Wähle Strategie basierend auf Server-Typ und Request
            LoadBalancingStrategy strategy = selectStrategy(serverType, request);
            
            // Finde besten Server
            ServerInfo bestServer = strategy.selectServer(availableServers, player, request);
            
            if (bestServer != null) {
                // Aktualisiere Server-Metriken
                updateServerLoad(bestServer.getServerId(), 1);
                
                // Logge Auswahl
                plugin.getLogger().info("Selected server " + bestServer.getServerId() + 
                    " for " + serverType.name() + " using " + strategy.getName() + " strategy");
            }
            
            future.complete(bestServer);
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error finding best server", e);
            future.complete(null);
        }
        
        return future;
    }
    
    /**
     * Aktualisiert Server-Metriken
     */
    private void updateServerMetrics() {
        for (ServerCommunicationManager.ServerInfo serverInfo : communicationManager.getConnectedServers().values()) {
            String serverId = serverInfo.getServerId();
            
            ServerMetrics metrics = serverMetrics.computeIfAbsent(serverId, k -> new ServerMetrics());
            
            metrics.updateMetrics(
                serverInfo.getPlayerCount(),
                serverInfo.getTps(),
                serverInfo.getMemoryUsage(),
                serverInfo.getCpuUsage(),
                serverInfo.getLoad(),
                serverInfo.getHealth()
            );
        }
    }
    
    /**
     * Gibt verfügbare Server zurück
     */
    private List<ServerInfo> getAvailableServers(NetworkArchitecture.ServerType serverType) {
        List<ServerInfo> availableServers = new ArrayList<ServerInfo>();
        
        for (ServerCommunicationManager.ServerInfo serverInfo : communicationManager.getConnectedServers().values()) {
            if (serverInfo.getServerType() == serverType && 
                serverInfo.isOnline() && 
                serverInfo.getHealth() >= healthThreshold &&
                serverInfo.getLoad() <= loadThreshold) {
                
                availableServers.add(new ServerInfo(serverInfo));
            }
        }
        
        return availableServers;
    }
    
    /**
     * Wählt die beste Strategie aus
     */
    private LoadBalancingStrategy selectStrategy(NetworkArchitecture.ServerType serverType, LoadBalancingRequest request) {
        // Wähle Strategie basierend auf Server-Typ
        switch (serverType) {
            case HUB:
                return strategies.get("round_robin");
            case ISLAND:
                return strategies.get("resource_based");
            case DUNGEON:
                return strategies.get("least_connections");
            case EVENT:
                return strategies.get("weighted_round_robin");
            case PVP:
                return strategies.get("least_response_time");
            default:
                return strategies.get("round_robin");
        }
    }
    
    /**
     * Aktualisiert Server-Last
     */
    private void updateServerLoad(String serverId, int loadChange) {
        ServerMetrics metrics = serverMetrics.get(serverId);
        if (metrics != null) {
            metrics.updateLoad(loadChange);
        }
    }
    
    /**
     * Führt Auto-Scaling durch
     */
    public void performAutoScaling() {
        for (NetworkArchitecture.ServerType serverType : NetworkArchitecture.ServerType.values()) {
            List<ServerInfo> servers = getAvailableServers(serverType);
            
            if (servers.isEmpty()) {
                // Keine Server verfügbar - starte neuen Server
                requestNewServer(serverType);
            } else {
                // Prüfe ob Server überlastet sind
                for (ServerInfo server : servers) {
                    if (server.getLoad() > loadThreshold) {
                        // Server ist überlastet - starte zusätzlichen Server
                        requestNewServer(serverType);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Fordert einen neuen Server an
     */
    private void requestNewServer(NetworkArchitecture.ServerType serverType) {
        plugin.getLogger().info("Requesting new server for type: " + serverType.name());
        
        // Hier würde man einen neuen Server starten
        // In einer echten Implementierung würde dies über eine Cloud-API oder Container-Orchestrierung erfolgen
    }
    
    /**
     * Gibt Load Balancing-Statistiken zurück
     */
    public LoadBalancingStatistics getStatistics() {
        Map<NetworkArchitecture.ServerType, Integer> serversByType = new HashMap<>();
        Map<NetworkArchitecture.ServerType, Double> averageLoadByType = new HashMap<>();
        Map<NetworkArchitecture.ServerType, Double> averageHealthByType = new HashMap<>();
        
        for (NetworkArchitecture.ServerType serverType : NetworkArchitecture.ServerType.values()) {
            List<ServerInfo> servers = getAvailableServers(serverType);
            serversByType.put(serverType, servers.size());
            
            if (!servers.isEmpty()) {
                double avgLoad = servers.stream().mapToDouble(ServerInfo::getLoad).average().orElse(0.0);
                double avgHealth = servers.stream().mapToDouble(ServerInfo::getHealth).average().orElse(0.0);
                
                averageLoadByType.put(serverType, avgLoad);
                averageHealthByType.put(serverType, avgHealth);
            }
        }
        
        return new LoadBalancingStatistics(serversByType, averageLoadByType, averageHealthByType);
    }
    
    /**
     * Server Info Klasse
     */
    public static class ServerInfo {
        private final String serverId;
        private final String serverName;
        private final NetworkArchitecture.ServerType serverType;
        private final String address;
        private final int port;
        private int playerCount;
        private double tps;
        private double memoryUsage;
        private double cpuUsage;
        private double load;
        private double health;
        private boolean isOnline;
        private long lastUpdate;
        
        public ServerInfo(ServerCommunicationManager.ServerInfo serverInfo) {
            this.serverId = serverInfo.getServerId();
            this.serverName = serverInfo.getServerName();
            this.serverType = serverInfo.getServerType();
            this.address = serverInfo.getAddress();
            this.port = serverInfo.getPort();
            this.playerCount = serverInfo.getPlayerCount();
            this.tps = serverInfo.getTps();
            this.memoryUsage = serverInfo.getMemoryUsage();
            this.cpuUsage = serverInfo.getCpuUsage();
            this.load = serverInfo.getLoad();
            this.health = serverInfo.getHealth();
            this.isOnline = serverInfo.isOnline();
            this.lastUpdate = serverInfo.getLastUpdate();
        }
        
        // Getters
        public String getServerId() { return serverId; }
        public String getServerName() { return serverName; }
        public NetworkArchitecture.ServerType getServerType() { return serverType; }
        public String getAddress() { return address; }
        public int getPort() { return port; }
        public int getPlayerCount() { return playerCount; }
        public double getTps() { return tps; }
        public double getMemoryUsage() { return memoryUsage; }
        public double getCpuUsage() { return cpuUsage; }
        public double getLoad() { return load; }
        public double getHealth() { return health; }
        public boolean isOnline() { return isOnline; }
        public long getLastUpdate() { return lastUpdate; }
    }
    
    /**
     * Server Metrics Klasse
     */
    public static class ServerMetrics {
        private int playerCount;
        private double tps;
        private double memoryUsage;
        private double cpuUsage;
        private double load;
        private double health;
        private long lastUpdate;
        private int currentLoad;
        
        public void updateMetrics(int playerCount, double tps, double memoryUsage, 
                                 double cpuUsage, double load, double health) {
            this.playerCount = playerCount;
            this.tps = tps;
            this.memoryUsage = memoryUsage;
            this.cpuUsage = cpuUsage;
            this.load = load;
            this.health = health;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void updateLoad(int loadChange) {
            this.currentLoad += loadChange;
        }
        
        // Getters
        public int getPlayerCount() { return playerCount; }
        public double getTps() { return tps; }
        public double getMemoryUsage() { return memoryUsage; }
        public double getCpuUsage() { return cpuUsage; }
        public double getLoad() { return load; }
        public double getHealth() { return health; }
        public long getLastUpdate() { return lastUpdate; }
        public int getCurrentLoad() { return currentLoad; }
    }
    
    /**
     * Load Balancing Request Klasse
     */
    public static class LoadBalancingRequest {
        private final String requestId;
        private final NetworkArchitecture.TransferReason reason;
        private final Map<String, Object> parameters;
        private final long timestamp;
        
        public LoadBalancingRequest(NetworkArchitecture.TransferReason reason, Map<String, Object> parameters) {
            this.requestId = UUID.randomUUID().toString();
            this.reason = reason;
            this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public String getRequestId() { return requestId; }
        public NetworkArchitecture.TransferReason getReason() { return reason; }
        public Map<String, Object> getParameters() { return new HashMap<>(parameters); }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Load Balancing Statistics Klasse
     */
    public static class LoadBalancingStatistics {
        private final Map<NetworkArchitecture.ServerType, Integer> serversByType;
        private final Map<NetworkArchitecture.ServerType, Double> averageLoadByType;
        private final Map<NetworkArchitecture.ServerType, Double> averageHealthByType;
        
        public LoadBalancingStatistics(Map<NetworkArchitecture.ServerType, Integer> serversByType,
                                      Map<NetworkArchitecture.ServerType, Double> averageLoadByType,
                                      Map<NetworkArchitecture.ServerType, Double> averageHealthByType) {
            this.serversByType = new HashMap<>(serversByType);
            this.averageLoadByType = new HashMap<>(averageLoadByType);
            this.averageHealthByType = new HashMap<>(averageHealthByType);
        }
        
        // Getters
        public Map<NetworkArchitecture.ServerType, Integer> getServersByType() { return new HashMap<>(serversByType); }
        public Map<NetworkArchitecture.ServerType, Double> getAverageLoadByType() { return new HashMap<>(averageLoadByType); }
        public Map<NetworkArchitecture.ServerType, Double> getAverageHealthByType() { return new HashMap<>(averageHealthByType); }
    }
    
    /**
     * Load Balancing Strategy Interface
     */
    public interface LoadBalancingStrategy {
        ServerInfo selectServer(List<ServerInfo> servers, Player player, LoadBalancingRequest request);
        String getName();
    }
    
    /**
     * Round Robin Strategy
     */
    public static class RoundRobinStrategy implements LoadBalancingStrategy {
        private int currentIndex = 0;
        
        @Override
        public ServerInfo selectServer(List<ServerInfo> servers, Player player, LoadBalancingRequest request) {
            if (servers.isEmpty()) return null;
            
            ServerInfo selected = servers.get(currentIndex % servers.size());
            currentIndex++;
            return selected;
        }
        
        @Override
        public String getName() { return "Round Robin"; }
    }
    
    /**
     * Least Connections Strategy
     */
    public static class LeastConnectionsStrategy implements LoadBalancingStrategy {
        @Override
        public ServerInfo selectServer(List<ServerInfo> servers, Player player, LoadBalancingRequest request) {
            return servers.stream()
                .min(Comparator.comparingInt(ServerInfo::getPlayerCount))
                .orElse(null);
        }
        
        @Override
        public String getName() { return "Least Connections"; }
    }
    
    /**
     * Weighted Round Robin Strategy
     */
    public static class WeightedRoundRobinStrategy implements LoadBalancingStrategy {
        private int currentIndex = 0;
        
        @Override
        public ServerInfo selectServer(List<ServerInfo> servers, Player player, LoadBalancingRequest request) {
            if (servers.isEmpty()) return null;
            
            // Sortiere Server nach Health (höhere Health = höhere Gewichtung)
            servers.sort(Comparator.comparingDouble(ServerInfo::getHealth).reversed());
            
            ServerInfo selected = servers.get(currentIndex % servers.size());
            currentIndex++;
            return selected;
        }
        
        @Override
        public String getName() { return "Weighted Round Robin"; }
    }
    
    /**
     * Least Response Time Strategy
     */
    public static class LeastResponseTimeStrategy implements LoadBalancingStrategy {
        @Override
        public ServerInfo selectServer(List<ServerInfo> servers, Player player, LoadBalancingRequest request) {
            return servers.stream()
                .min(Comparator.comparingDouble(ServerInfo::getTps).reversed()) // Höhere TPS = bessere Performance
                .orElse(null);
        }
        
        @Override
        public String getName() { return "Least Response Time"; }
    }
    
    /**
     * Resource Based Strategy
     */
    public static class ResourceBasedStrategy implements LoadBalancingStrategy {
        @Override
        public ServerInfo selectServer(List<ServerInfo> servers, Player player, LoadBalancingRequest request) {
            return servers.stream()
                .min(Comparator.comparingDouble(server -> {
                    // Kombiniere verschiedene Faktoren
                    double loadFactor = server.getLoad();
                    double healthFactor = 1.0 - server.getHealth();
                    double memoryFactor = server.getMemoryUsage() / 100.0;
                    double cpuFactor = server.getCpuUsage() / 100.0;
                    
                    return loadFactor + healthFactor + memoryFactor + cpuFactor;
                }))
                .orElse(null);
        }
        
        @Override
        public String getName() { return "Resource Based"; }
    }
}
