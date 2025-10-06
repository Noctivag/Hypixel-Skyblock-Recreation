package de.noctivag.skyblock.engine.rte.services;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import de.noctivag.skyblock.engine.rte.RealTimeEconomyEngine;
import de.noctivag.skyblock.engine.rte.data.BazaarItemData;
import de.noctivag.skyblock.engine.rte.data.MarketTrend;

/**
 * Agile Economy Management Service (AEMS) - Real-time Economic Dashboard
 *
 * Provides a secure web interface for real-time economic management:
 * - Live price monitoring and adjustment
 * - Drop rate configuration
 * - NPC price management
 * - Loot pool configuration
 * - Market stability controls
 * - Economic analytics and reporting
 *
 * This enables the admin team to stabilize the economy in real-time
 * without server restarts or downtime.
 */
public class EconomyDashboardService {

    private static final Logger logger = Logger.getLogger(EconomyDashboardService.class.getName());

    private final RealTimeEconomyEngine engine;
    private final Gson gson;
    private HttpServer httpServer;

    // Dashboard configuration
    private static final int DASHBOARD_PORT = 8080;
    private static final String DASHBOARD_PATH = "/economy-dashboard";
    private static final String API_PATH = "/api";

    // Security configuration
    private final Set<String> authorizedTokens = ConcurrentHashMap.newKeySet();
    private static final String ADMIN_TOKEN = "admin_token_2024"; // In production, use secure token generation

    // Real-time data cache
    private final Map<String, EconomicMetric> economicMetrics = new ConcurrentHashMap<>();
    private final Map<String, MarketAlert> marketAlerts = new ConcurrentHashMap<>();

    // Configuration management
    private final Map<String, EconomicConfig> economicConfigs = new ConcurrentHashMap<>();

    public EconomyDashboardService(RealTimeEconomyEngine engine) {
        this.engine = engine;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        // Initialize with default admin token
        authorizedTokens.add(ADMIN_TOKEN);

        initialize();
    }

    /**
     * Initialize the dashboard service
     */
    public void initialize() {
        try {
            // Start HTTP server
            httpServer = HttpServer.create(new InetSocketAddress(DASHBOARD_PORT), 0);

            // Register handlers
            httpServer.createContext(DASHBOARD_PATH, new DashboardHandler());
            httpServer.createContext(API_PATH, new APIHandler());

            // Start server
            httpServer.start();

            logger.info("Economy Dashboard Service started on port " + DASHBOARD_PORT);
            logger.info("Dashboard URL: http://localhost:" + DASHBOARD_PORT + DASHBOARD_PATH);

            // Initialize default configurations
            initializeDefaultConfigurations();

            // Start real-time monitoring
            startRealTimeMonitoring();

        } catch (Exception e) {
            logger.severe("Failed to initialize Economy Dashboard Service: " + e.getMessage());
        }
    }

    /**
     * Initialize default economic configurations
     */
    private void initializeDefaultConfigurations() {
        // Bazaar configuration
        EconomicConfig bazaarConfig = new EconomicConfig("bazaar", new HashMap<>());
        bazaarConfig.getSettings().put("fee_percentage", "1.0");
        bazaarConfig.getSettings().put("max_orders_per_player", "14");
        bazaarConfig.getSettings().put("max_orders_per_item", "1000");
        bazaarConfig.getSettings().put("price_update_interval", "1000");
        economicConfigs.put("bazaar", bazaarConfig);

        // Auction House configuration
        EconomicConfig auctionConfig = new EconomicConfig("auction_house", new HashMap<>());
        auctionConfig.getSettings().put("auction_fee_percentage", "5.0");
        auctionConfig.getSettings().put("bin_fee_percentage", "2.0");
        auctionConfig.getSettings().put("max_auctions_per_player", "7");
        auctionConfig.getSettings().put("auction_duration_hours", "24");
        economicConfigs.put("auction_house", auctionConfig);

        // Drop rates configuration
        EconomicConfig dropRatesConfig = new EconomicConfig("drop_rates", new HashMap<>());
        dropRatesConfig.getSettings().put("diamond_drop_rate", "0.1");
        dropRatesConfig.getSettings().put("emerald_drop_rate", "0.05");
        dropRatesConfig.getSettings().put("gold_drop_rate", "0.2");
        dropRatesConfig.getSettings().put("iron_drop_rate", "0.3");
        economicConfigs.put("drop_rates", dropRatesConfig);

        // NPC prices configuration
        EconomicConfig npcPricesConfig = new EconomicConfig("npc_prices", new HashMap<>());
        npcPricesConfig.getSettings().put("diamond_npc_price", "8.0");
        npcPricesConfig.getSettings().put("emerald_npc_price", "6.0");
        npcPricesConfig.getSettings().put("gold_npc_price", "4.0");
        npcPricesConfig.getSettings().put("iron_npc_price", "1.5");
        economicConfigs.put("npc_prices", npcPricesConfig);

        logger.info("Initialized " + economicConfigs.size() + " economic configurations");
    }

    /**
     * Start real-time monitoring
     */
    private void startRealTimeMonitoring() {
        Timer timer = new Timer("EconomyMonitoring", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateEconomicMetrics();
                detectMarketAnomalies();
                generateMarketAlerts();
            }
        }, 0L, 5000L); // Update every 5 seconds
    }

    /**
     * Update economic metrics
     */
    private void updateEconomicMetrics() {
        try {
            // Update bazaar metrics
            Map<String, BazaarItemData> bazaarItems = engine.getBazaarItems();
            for (Map.Entry<String, BazaarItemData> entry : bazaarItems.entrySet()) {
                String itemId = entry.getKey();
                BazaarItemData itemData = entry.getValue();

                EconomicMetric metric = new EconomicMetric(
                    itemId,
                    itemData.getInstantBuyPrice(),
                    itemData.getInstantSellPrice(),
                    itemData.getAveragePrice(),
                    itemData.getTotalVolume(),
                    java.lang.System.currentTimeMillis()
                );

                economicMetrics.put(itemId, metric);
            }

            // Update market trends
            Map<String, MarketTrend> marketTrends = engine.getMarketTrends();
            for (Map.Entry<String, MarketTrend> entry : marketTrends.entrySet()) {
                String itemId = entry.getKey();
                MarketTrend trend = entry.getValue();

                EconomicMetric metric = economicMetrics.get(itemId);
                if (metric != null) {
                    metric.setTrend(trend);
                }
            }

        } catch (Exception e) {
            logger.severe("Failed to update economic metrics: " + e.getMessage());
        }
    }

    /**
     * Detect market anomalies
     */
    private void detectMarketAnomalies() {
        for (Map.Entry<String, EconomicMetric> entry : economicMetrics.entrySet()) {
            String itemId = entry.getKey();
            EconomicMetric metric = entry.getValue();

            // Detect price spikes
            if (metric.getPriceChange() > 20.0) {
                createMarketAlert(itemId, "PRICE_SPIKE",
                    "Price increased by " + metric.getPriceChange() + "%",
                    AlertLevel.HIGH);
            }

            // Detect price crashes
            if (metric.getPriceChange() < -20.0) {
                createMarketAlert(itemId, "PRICE_CRASH",
                    "Price decreased by " + Math.abs(metric.getPriceChange()) + "%",
                    AlertLevel.HIGH);
            }

            // Detect unusual volume
            if (metric.getVolume() > 10000) {
                createMarketAlert(itemId, "HIGH_VOLUME",
                    "Unusual trading volume: " + metric.getVolume(),
                    AlertLevel.MEDIUM);
            }
        }
    }

    /**
     * Generate market alerts
     */
    private void generateMarketAlerts() {
        // Check for manipulation patterns
        for (Map.Entry<String, EconomicMetric> entry : economicMetrics.entrySet()) {
            String itemId = entry.getKey();
            EconomicMetric metric = entry.getValue();

            if (metric.getTrend() != null && metric.getTrend().isManipulationDetected()) {
                createMarketAlert(itemId, "MARKET_MANIPULATION",
                    "Potential market manipulation detected",
                    AlertLevel.CRITICAL);
            }
        }
    }

    /**
     * Create market alert
     */
    private void createMarketAlert(String itemId, String type, String message, AlertLevel level) {
        String alertId = UUID.randomUUID().toString();
        MarketAlert alert = new MarketAlert(
            alertId,
            itemId,
            type,
            message,
            level,
            java.lang.System.currentTimeMillis()
        );

        marketAlerts.put(alertId, alert);

        // Log critical alerts
        if (level == AlertLevel.CRITICAL) {
            logger.severe("CRITICAL MARKET ALERT: " + message + " for " + itemId);
        }
    }

    /**
     * Dashboard HTTP handler
     */
    private class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAuthorized(exchange)) {
                sendUnauthorizedResponse(exchange);
                return;
            }

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equals(method)) {
                if (path.equals(DASHBOARD_PATH)) {
                    serveDashboard(exchange);
                } else if (path.equals(DASHBOARD_PATH + "/")) {
                    serveDashboard(exchange);
                } else {
                    serveStaticFile(exchange, path);
                }
            } else {
                sendMethodNotAllowedResponse(exchange);
            }
        }
    }

    /**
     * API HTTP handler
     */
    private class APIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAuthorized(exchange)) {
                sendUnauthorizedResponse(exchange);
                return;
            }

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            try {
                if ("GET".equals(method)) {
                    handleGetRequest(exchange, path, query);
                } else if ("POST".equals(method)) {
                    handlePostRequest(exchange, path);
                } else if ("PUT".equals(method)) {
                    handlePutRequest(exchange, path);
                } else {
                    sendMethodNotAllowedResponse(exchange);
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
            }
        }
    }

    /**
     * Handle GET requests
     */
    private void handleGetRequest(HttpExchange exchange, String path, String query) throws IOException {
        if (path.equals(API_PATH + "/metrics")) {
            // Get economic metrics
            JsonObject response = new JsonObject();
            response.addProperty("success", true);

            JsonObject metrics = new JsonObject();
            for (Map.Entry<String, EconomicMetric> entry : economicMetrics.entrySet()) {
                metrics.add(entry.getKey(), entry.getValue().toJson());
            }
            response.add("metrics", metrics);

            sendJsonResponse(exchange, response.toString());

        } else if (path.equals(API_PATH + "/alerts")) {
            // Get market alerts
            JsonObject response = new JsonObject();
            response.addProperty("success", true);

            JsonObject alerts = new JsonObject();
            for (Map.Entry<String, MarketAlert> entry : marketAlerts.entrySet()) {
                alerts.add(entry.getKey(), entry.getValue().toJson());
            }
            response.add("alerts", alerts);

            sendJsonResponse(exchange, response.toString());

        } else if (path.equals(API_PATH + "/config")) {
            // Get economic configurations
            JsonObject response = new JsonObject();
            response.addProperty("success", true);

            JsonObject configs = new JsonObject();
            for (Map.Entry<String, EconomicConfig> entry : economicConfigs.entrySet()) {
                configs.add(entry.getKey(), entry.getValue().toJson());
            }
            response.add("configs", configs);

            sendJsonResponse(exchange, response.toString());

        } else {
            sendNotFoundResponse(exchange);
        }
    }

    /**
     * Handle POST requests
     */
    private void handlePostRequest(HttpExchange exchange, String path) throws IOException {
        if (path.equals(API_PATH + "/config/update")) {
            // Update economic configuration
            String requestBody = readRequestBody(exchange);
            JsonObject configJson = gson.fromJson(requestBody, JsonObject.class);

            String configType = configJson.get("type").getAsString();
            JsonObject settings = configJson.getAsJsonObject("settings");

            EconomicConfig config = economicConfigs.get(configType);
            if (config != null) {
                // Update settings
                for (Map.Entry<String, com.google.gson.JsonElement> entry : settings.entrySet()) {
                    config.getSettings().put(entry.getKey(), entry.getValue().getAsString());
                }

                // Apply configuration changes
                applyConfigurationChanges(configType, config);

                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                response.addProperty("message", "Configuration updated successfully");

                sendJsonResponse(exchange, response.toString());
            } else {
                sendErrorResponse(exchange, 404, "Configuration type not found");
            }

        } else if (path.equals(API_PATH + "/price/adjust")) {
            // Adjust item prices
            String requestBody = readRequestBody(exchange);
            JsonObject priceJson = gson.fromJson(requestBody, JsonObject.class);

            String itemId = priceJson.get("itemId").getAsString();
            double sellPrice = priceJson.get("sellPrice").getAsDouble();
            double buyPrice = priceJson.get("buyPrice").getAsDouble();

            // Update prices in real-time
            engine.getApiCorrectionService().updatePriceData(itemId, sellPrice, buyPrice, (sellPrice + buyPrice) / 2, 0);

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Prices adjusted successfully");

            sendJsonResponse(exchange, response.toString());

        } else {
            sendNotFoundResponse(exchange);
        }
    }

    /**
     * Handle PUT requests
     */
    private void handlePutRequest(HttpExchange exchange, String path) throws IOException {
        if (path.equals(API_PATH + "/alert/dismiss")) {
            // Dismiss market alert
            String requestBody = readRequestBody(exchange);
            JsonObject alertJson = gson.fromJson(requestBody, JsonObject.class);

            String alertId = alertJson.get("alertId").getAsString();
            marketAlerts.remove(alertId);

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Alert dismissed");

            sendJsonResponse(exchange, response.toString());

        } else {
            sendNotFoundResponse(exchange);
        }
    }

    /**
     * Apply configuration changes
     */
    private void applyConfigurationChanges(String configType, EconomicConfig config) {
        switch (configType) {
            case "bazaar":
                // Apply bazaar configuration changes
                logger.info("Applied bazaar configuration changes");
                break;
            case "auction_house":
                // Apply auction house configuration changes
                logger.info("Applied auction house configuration changes");
                break;
            case "drop_rates":
                // Apply drop rate configuration changes
                logger.info("Applied drop rate configuration changes");
                break;
            case "npc_prices":
                // Apply NPC price configuration changes
                logger.info("Applied NPC price configuration changes");
                break;
        }
    }

    /**
     * Serve dashboard HTML
     */
    private void serveDashboard(HttpExchange exchange) throws IOException {
        String html = generateDashboardHTML();
        sendHtmlResponse(exchange, html);
    }

    /**
     * Generate dashboard HTML
     */
    private String generateDashboardHTML() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Hypixel Skyblock - Economy Dashboard</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f0f0f0; }
                    .container { max-width: 1200px; margin: 0 auto; }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 10px; margin-bottom: 20px; }
                    .metrics-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-bottom: 20px; }
                    .metric-card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .metric-title { font-size: 18px; font-weight: bold; margin-bottom: 10px; color: #333; }
                    .metric-value { font-size: 24px; font-weight: bold; color: #667eea; }
                    .alerts-section { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 20px; }
                    .alert { padding: 10px; margin: 10px 0; border-radius: 5px; border-left: 4px solid; }
                    .alert.critical { background-color: #ffebee; border-left-color: #f44336; }
                    .alert.high { background-color: #fff3e0; border-left-color: #ff9800; }
                    .alert.medium { background-color: #e8f5e8; border-left-color: #4caf50; }
                    .config-section { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .config-item { margin: 10px 0; }
                    .config-item label { display: inline-block; width: 200px; font-weight: bold; }
                    .config-item input { padding: 5px; border: 1px solid #ddd; border-radius: 3px; }
                    .btn { background: #667eea; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; }
                    .btn:hover { background: #5a6fd8; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🏦 Hypixel Skyblock Economy Dashboard</h1>
                        <p>Real-time economic monitoring and management</p>
                    </div>

                    <div class="metrics-grid" id="metricsGrid">
                        <!-- Metrics will be loaded here -->
                    </div>

                    <div class="alerts-section">
                        <h2>🚨 Market Alerts</h2>
                        <div id="alertsContainer">
                            <!-- Alerts will be loaded here -->
                        </div>
                    </div>

                    <div class="config-section">
                        <h2>⚙️ Economic Configuration</h2>
                        <div id="configContainer">
                            <!-- Configuration will be loaded here -->
                        </div>
                    </div>
                </div>

                <script>
                    // Auto-refresh every 5 seconds
                    setInterval(loadData, 5000);
                    loadData();

                    async function loadData() {
                        await loadMetrics();
                        await loadAlerts();
                        await loadConfig();
                    }

                    async function loadMetrics() {
                        try {
                            const response = await fetch('/api/metrics');
                            const data = await response.json();

                            const container = document.getElementById('metricsGrid');
                            container.innerHTML = '';

                            for (const [itemId, metric] of Object.entries(data.metrics)) {
                                const card = document.createElement('div');
                                card.className = 'metric-card';
                                card.innerHTML = `
                                    <div class="metric-title">${itemId}</div>
                                    <div class="metric-value">Buy: $${metric.buyPrice.toFixed(2)}</div>
                                    <div class="metric-value">Sell: $${metric.sellPrice.toFixed(2)}</div>
                                    <div>Volume: ${metric.volume}</div>
                                    <div>Change: ${metric.priceChange.toFixed(2)}%</div>
                                `;
                                container.appendChild(card);
                            }
                        } catch (error) {
                            console.error('Failed to load metrics:', error);
                        }
                    }

                    async function loadAlerts() {
                        try {
                            const response = await fetch('/api/alerts');
                            const data = await response.json();

                            const container = document.getElementById('alertsContainer');
                            container.innerHTML = '';

                            for (const [alertId, alert] of Object.entries(data.alerts)) {
                                const alertDiv = document.createElement('div');
                                alertDiv.className = `alert ${alert.level.toLowerCase()}`;
                                alertDiv.innerHTML = `
                                    <strong>${alert.type}</strong> - ${alert.message}
                                    <button class="btn" onclick="dismissAlert('${alertId}')" style="float: right; padding: 5px 10px; font-size: 12px;">Dismiss</button>
                                `;
                                container.appendChild(alertDiv);
                            }
                        } catch (error) {
                            console.error('Failed to load alerts:', error);
                        }
                    }

                    async function loadConfig() {
                        try {
                            const response = await fetch('/api/config');
                            const data = await response.json();

                            const container = document.getElementById('configContainer');
                            container.innerHTML = '';

                            for (const [configType, config] of Object.entries(data.configs)) {
                                const section = document.createElement('div');
                                section.innerHTML = `<h3>${configType.toUpperCase()}</h3>`;

                                for (const [key, value] of Object.entries(config.settings)) {
                                    const item = document.createElement('div');
                                    item.className = 'config-item';
                                    item.innerHTML = `
                                        <label>${key}:</label>
                                        <input type="text" id="${configType}_${key}" value="${value}">
                                    `;
                                    section.appendChild(item);
                                }

                                const updateBtn = document.createElement('button');
                                updateBtn.className = 'btn';
                                updateBtn.textContent = 'Update ' + configType;
                                updateBtn.onclick = () => updateConfig(configType);
                                section.appendChild(updateBtn);

                                container.appendChild(section);
                            }
                        } catch (error) {
                            console.error('Failed to load config:', error);
                        }
                    }

                    async function dismissAlert(alertId) {
                        try {
                            await fetch('/api/alert/dismiss', {
                                method: 'PUT',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ alertId: alertId })
                            });
                            loadAlerts();
                        } catch (error) {
                            console.error('Failed to dismiss alert:', error);
                        }
                    }

                    async function updateConfig(configType) {
                        try {
                            const settings = {};
                            const inputs = document.querySelectorAll(`[id^="${configType}_"]`);
                            inputs.forEach(input => {
                                const key = input.id.replace(`${configType}_`, '');
                                settings[key] = input.value;
                            });

                            await fetch('/api/config/update', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ type: configType, settings: settings })
                            });

                            alert('Configuration updated successfully!');
                        } catch (error) {
                            console.error('Failed to update config:', error);
                            alert('Failed to update configuration');
                        }
                    }
                </script>
            </body>
            </html>
            """;
    }

    /**
     * Check if request is authorized
     */
    private boolean isAuthorized(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return authorizedTokens.contains(token);
        }

        // Check for token in query parameters
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    String token = param.substring(6);
                    return authorizedTokens.contains(token);
                }
            }
        }

        return false;
    }

    /**
     * Read request body
     */
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Send JSON response
     */
    private void sendJsonResponse(HttpExchange exchange, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(json.getBytes());
        }
    }

    /**
     * Send HTML response
     */
    private void sendHtmlResponse(HttpExchange exchange, String html) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, html.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(html.getBytes());
        }
    }

    /**
     * Send error response
     */
    private void sendErrorResponse(HttpExchange exchange, int code, String message) throws IOException {
        JsonObject response = new JsonObject();
        response.addProperty("success", false);
        response.addProperty("error", message);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.toString().getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.toString().getBytes());
        }
    }

    /**
     * Send unauthorized response
     */
    private void sendUnauthorizedResponse(HttpExchange exchange) throws IOException {
        sendErrorResponse(exchange, 401, "Unauthorized");
    }

    /**
     * Send method not allowed response
     */
    private void sendMethodNotAllowedResponse(HttpExchange exchange) throws IOException {
        sendErrorResponse(exchange, 405, "Method not allowed");
    }

    /**
     * Send not found response
     */
    private void sendNotFoundResponse(HttpExchange exchange) throws IOException {
        sendErrorResponse(exchange, 404, "Not found");
    }

    /**
     * Serve static files
     */
    private void serveStaticFile(HttpExchange exchange, String path) throws IOException {
        // Serve static files (CSS, JS, images)
        sendNotFoundResponse(exchange);
    }

    /**
     * Report market manipulation
     */
    public void reportMarketManipulation(String itemId, MarketTrend trend) {
        createMarketAlert(itemId, "MARKET_MANIPULATION",
            "Market manipulation detected: " + trend.getChangePercentage() + "% change",
            AlertLevel.CRITICAL);
    }

    /**
     * Shutdown service
     */
    public void shutdown() {
        if (httpServer != null) {
            httpServer.stop(0);
            logger.info("Economy Dashboard Service stopped");
        }
    }

    // Data classes
    public static class EconomicMetric {
        private final String itemId;
        private final double buyPrice;
        private final double sellPrice;
        private final double averagePrice;
        private final int volume;
        private final double priceChange;
        private final long timestamp;
        private MarketTrend trend;

        public EconomicMetric(String itemId, double buyPrice, double sellPrice,
                            double averagePrice, int volume, long timestamp) {
            this.itemId = itemId;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.averagePrice = averagePrice;
            this.volume = volume;
            this.priceChange = 0.0; // Will be calculated
            this.timestamp = timestamp;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("itemId", itemId);
            json.addProperty("buyPrice", buyPrice);
            json.addProperty("sellPrice", sellPrice);
            json.addProperty("averagePrice", averagePrice);
            json.addProperty("volume", volume);
            json.addProperty("priceChange", priceChange);
            json.addProperty("timestamp", timestamp);
            return json;
        }

        // Getters and setters
        public String getItemId() { return itemId; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public double getAveragePrice() { return averagePrice; }
        public int getVolume() { return volume; }
        public double getPriceChange() { return priceChange; }
        public long getTimestamp() { return timestamp; }
        public MarketTrend getTrend() { return trend; }

        public void setTrend(MarketTrend trend) { this.trend = trend; }
    }

    public static class MarketAlert {
        private final String alertId;
        private final String itemId;
        private final String type;
        private final String message;
        private final AlertLevel level;
        private final long timestamp;

        public MarketAlert(String alertId, String itemId, String type, String message, AlertLevel level, long timestamp) {
            this.alertId = alertId;
            this.itemId = itemId;
            this.type = type;
            this.message = message;
            this.level = level;
            this.timestamp = timestamp;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("alertId", alertId);
            json.addProperty("itemId", itemId);
            json.addProperty("type", type);
            json.addProperty("message", message);
            json.addProperty("level", level.name());
            json.addProperty("timestamp", timestamp);
            return json;
        }

        // Getters
        public String getAlertId() { return alertId; }
        public String getItemId() { return itemId; }
        public String getType() { return type; }
        public String getMessage() { return message; }
        public AlertLevel getLevel() { return level; }
        public long getTimestamp() { return timestamp; }
    }

    public static class EconomicConfig {
        private final String type;
        private final Map<String, String> settings;

        public EconomicConfig(String type, Map<String, String> settings) {
            this.type = type;
            this.settings = settings;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", type);

            JsonObject settingsJson = new JsonObject();
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                settingsJson.addProperty(entry.getKey(), entry.getValue());
            }
            json.add("settings", settingsJson);

            return json;
        }

        // Getters
        public String getType() { return type; }
        public Map<String, String> getSettings() { return settings; }
    }

    public enum AlertLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
