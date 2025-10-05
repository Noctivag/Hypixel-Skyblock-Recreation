package de.noctivag.skyblock.web;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WebConfigServer {
    private final SkyblockPlugin SkyblockPlugin;
    private HttpServer server;
    private final int port;
    private final String password;

    public WebConfigServer(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        FileConfiguration config = SkyblockPlugin.getConfig();
        this.port = config.getInt("web-config.port", 8080);
        this.password = config.getString("web-config.password", "changeme");
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new MainHandler());
            server.createContext("/api/config", new ConfigHandler());
            server.createContext("/api/features", new FeaturesHandler());
            server.setExecutor(null);
            server.start();
            SkyblockPlugin.getLogger().info("Web-Konfiguration läuft auf Port " + port);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Fehler beim Starten des Web-Servers: " + e.getMessage());
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class MainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                return;
            }

            String response = loadHtmlTemplate("web/index.html");
            sendResponse(exchange, response);
        }
    }

    private class ConfigHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!authenticateRequest(exchange)) {
                exchange.sendResponseHeaders(401, 0);
                exchange.close();
                return;
            }

            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleConfigGet(exchange);
                    break;
                case "POST":
                    handleConfigPost(exchange);
                    break;
                default:
                    exchange.sendResponseHeaders(405, 0);
                    exchange.close();
            }
        }

        private void handleConfigGet(HttpExchange exchange) throws IOException {
            Map<String, Object> config = new HashMap<>();
            // Lade Konfigurationswerte
            config.put("maxHomes", SkyblockPlugin.getConfig().getInt("max-homes", 3));
            config.put("webPort", port);
            config.put("features", readFeatureStates());

            sendJsonResponse(exchange, config);
        }

        private void handleConfigPost(HttpExchange exchange) throws IOException {
            // Read request body using UTF-8 and handle empty bodies safely
            String body;
            try (Scanner scanner = new Scanner(exchange.getRequestBody(), StandardCharsets.UTF_8.name())) {
                scanner.useDelimiter("\\A");
                body = scanner.hasNext() ? scanner.next() : "";
            }

            try {
                Object parsed = new JSONParser().parse(body);
                if (!(parsed instanceof Map)) {
                    sendJsonResponse(exchange, Map.of("error", "Invalid JSON payload"));
                    return;
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> json = (Map<String, Object>) parsed;

                // Aktualisiere Konfiguration safely (check presence and types)
                Object maxHomesObj = json.get("maxHomes");
                if (maxHomesObj instanceof Number) {
                    SkyblockPlugin.getConfig().set("max-homes", ((Number) maxHomesObj).intValue());
                } else if (maxHomesObj instanceof String) {
                    try {
                        SkyblockPlugin.getConfig().set("max-homes", Integer.parseInt((String) maxHomesObj));
                    } catch (NumberFormatException ignored) {
                        // ignore invalid number
                    }
                }

                Object webPortObj = json.get("webPort");
                if (webPortObj instanceof Number) {
                    SkyblockPlugin.getConfig().set("web-config.port", ((Number) webPortObj).intValue());
                } else if (webPortObj instanceof String) {
                    try {
                        SkyblockPlugin.getConfig().set("web-config.port", Integer.parseInt((String) webPortObj));
                    } catch (NumberFormatException ignored) {
                        // ignore invalid number
                    }
                }

                SkyblockPlugin.getConfigManager().saveConfig("config");

                sendJsonResponse(exchange, Map.of("success", true));
            } catch (Exception e) {
                sendJsonResponse(exchange, Map.of("error", e.getMessage()));
            }
        }
    }

    private class FeaturesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!authenticateRequest(exchange)) {
                exchange.sendResponseHeaders(401, 0);
                exchange.close();
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, Object> features = new HashMap<>();
                features.put("features", readFeatureStates());
                features.put("descriptions", getFeatureDescriptions());
                sendJsonResponse(exchange, features);
            } else {
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
            }
        }
    }

    private boolean authenticateRequest(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        return auth != null && auth.equals("Bearer " + password);
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendJsonResponse(HttpExchange exchange, Object body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        String jsonString;
        if (body instanceof String) {
            jsonString = (String) body;
        } else if (body instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) body;
            jsonString = JSONObject.toJSONString(map);
        } else if (body instanceof JSONObject) {
            jsonString = ((JSONObject) body).toJSONString();
        } else {
            // Fallback to toString()
            jsonString = body == null ? "null" : body.toString();
        }

        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String loadHtmlTemplate(String path) {
        try (InputStream is = SkyblockPlugin.getResource(path)) {
            if (is == null) return "Error: Template not found";
            try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private Map<String, Boolean> readFeatureStates() {
        Map<String, Boolean> states = new HashMap<>();
        var cfg = SkyblockPlugin.getConfig();
        var section = cfg.getConfigurationSection("features");
        if (section == null) return states;
        for (String key : section.getKeys(false)) {
            boolean enabled = cfg.getBoolean("features." + key + ".enabled", false);
            states.put(key, enabled);
        }
        return states;
    }

    private Map<String, String> getFeatureDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("kits", "Kit-System mit verschiedenen Ausrüstungs-Paketen");
        descriptions.put("homes", "Home-System zum Setzen persönlicher Teleport-Punkte");
        descriptions.put("warps", "Warp-System für öffentliche Teleport-Punkte");
        descriptions.put("scoreboard", "Anpassbares Scoreboard mit Spieler-Informationen");
        descriptions.put("achievements", "Achievement-System mit Belohnungen");
        descriptions.put("daily-rewards", "Tägliche Belohnungen mit Streak-System");
        descriptions.put("nametags", "Angepasste Namensschilder über Spielern");
        descriptions.put("status", "Status-System für Chat und Tab-Liste");
        return descriptions;
    }
}
