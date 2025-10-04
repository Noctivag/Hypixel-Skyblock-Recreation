# Agent V: Real-Time Economy and Temporal Management Integration Guide

## Overview

This guide provides comprehensive instructions for integrating the new Real-Time Economy (RTE) and Temporal Management systems into the existing Hypixel Skyblock Recreation plugin. These systems implement Agent V's mandate for real-time economy synchronization and global time management.

## System Architecture

### Real-Time Economy (RTE) System

The RTE system provides:
- **Redis-based Bazaar/Auction House backend** with real-time synchronization
- **Atomic transaction protocols** for distributed trading
- **Hypixel API correction service** for data inconsistencies
- **Administrative dashboard** for agile economy management
- **Market protection** against manipulation and arbitrage

### Temporal Management System

The Temporal Management system provides:
- **Global Time Management Service** for game calendar and events
- **Event scheduling** for Mayor elections, Jacob's events, and Cult of the Fallen Star
- **GIM integration** for proactive server scaling
- **Time synchronization** across all server instances

## File Structure

```
src/main/java/de/noctivag/skyblock/engine/
├── rte/
│   ├── RealTimeEconomyEngine.java          # Core RTE engine
│   ├── data/
│   │   ├── BazaarItemData.java            # Bazaar item data structure
│   │   ├── BazaarOrder.java               # Bazaar order data structure
│   │   ├── BazaarOrderResult.java         # Order result data structure
│   │   ├── AuctionItemData.java           # Auction item data structure
│   │   ├── AuctionBid.java                # Auction bid data structure
│   │   ├── PriceData.java                 # Price data structure
│   │   ├── MarketTrend.java               # Market trend analysis
│   │   ├── PriceUpdateMessage.java        # Real-time price updates
│   │   ├── OrderUpdateMessage.java        # Real-time order updates
│   │   ├── TransactionMessage.java        # Transaction broadcasts
│   │   └── MarketProtectionMessage.java   # Market protection alerts
│   ├── protocols/
│   │   └── TransactionProtocol.java       # Atomic transaction protocols
│   └── services/
│       ├── HypixelAPICorrectionService.java # API correction service
│       └── EconomyDashboardService.java   # Administrative dashboard
└── temporal/
    ├── GlobalTimeManagementService.java   # Core temporal service
    ├── data/
    │   ├── GlobalEvent.java               # Base event data structure
    │   ├── MayorElection.java             # Mayor election data
    │   ├── JacobsEvent.java               # Jacob's event data
    │   ├── CultEvent.java                 # Cult event data
    │   ├── ScalingRequest.java            # GIM scaling requests
    │   ├── ScalingResponse.java           # GIM scaling responses
    │   ├── EventTriggerMessage.java       # Event trigger messages
    │   └── TimeSyncMessage.java           # Time synchronization
    └── services/
        └── GIMIntegrationService.java     # GIM integration service
```

## Integration Steps

### 1. Update Existing Economy System

The new RTE system should replace the existing `BazaarSystem` in `EconomySystem.java`:

```java
// In EconomySystem.java constructor
public EconomySystem(SkyblockPlugin plugin, CorePlatform corePlatform) {
    this.plugin = plugin;
    this.corePlatform = corePlatform;
    
    // Replace existing BazaarSystem with RTE
    this.realTimeEconomyEngine = new RealTimeEconomyEngine(plugin, serverManager);
    this.realTimeEconomyEngine.initialize();
    
    // Keep other systems
    this.auctionHouse = new AuctionHouse(this, plugin.getMultiServerDatabaseManager());
    this.npcShopSystem = new NPCShopSystem(this);
    this.tradingSystem = new TradingSystem(this);
    this.marketAnalyzer = new MarketAnalyzer(this);
    
    initializeEconomy();
}
```

### 2. Update Calendar System Integration

The existing `CalendarSystem` should be integrated with the new `GlobalTimeManagementService`:

```java
// In CalendarSystem.java
public class CalendarSystem implements Listener {
    private final GlobalTimeManagementService globalTimeService;
    
    public CalendarSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.globalTimeService = new GlobalTimeManagementService(plugin, serverManager);
        this.globalTimeService.initialize();
        
        // Initialize existing events
        initializeEvents();
        startCalendarUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
```

### 3. Redis Configuration

Ensure Redis is properly configured in your `config.yml`:

```yaml
redis:
  host: localhost
  port: 6379
  password: ""
  database: 0
  timeout: 2000
  max-total: 20
  max-idle: 10
  min-idle: 5
```

### 4. Server Manager Integration

The existing `ServerManager` class already provides Redis connectivity. The RTE system uses this existing infrastructure:

```java
// RTE system uses existing ServerManager
public RealTimeEconomyEngine(Plugin plugin, ServerManager serverManager) {
    this.plugin = plugin;
    this.serverManager = serverManager;
    this.jedisPool = serverManager.getJedisPool(); // Reuse existing connection
}
```

## Key Features

### Real-Time Economy Features

1. **Instant Order Matching**: Orders are matched in real-time across all server instances
2. **Price Synchronization**: Prices are synchronized instantly via Redis Pub/Sub
3. **Atomic Transactions**: All trades are atomic to prevent inconsistencies
4. **Market Protection**: Automatic detection and prevention of market manipulation
5. **API Correction**: Corrects Hypixel API inconsistencies (sellPrice/buyPrice swap)

### Temporal Management Features

1. **Global Calendar**: Manages all game events across server instances
2. **Event Scheduling**: Precise timing for Mayor elections, Jacob's events, etc.
3. **Proactive Scaling**: Requests additional server capacity before events
4. **Time Synchronization**: Ensures consistent time across all servers

## API Usage Examples

### Creating a Bazaar Order

```java
// Get the RTE engine
RealTimeEconomyEngine rte = plugin.getEconomySystem().getRealTimeEconomyEngine();

// Create a buy order
CompletableFuture<BazaarOrderResult> result = rte.createBazaarOrder(
    player, 
    "ENCHANTED_DIAMOND", 
    64, 
    100.0, 
    BazaarOrder.OrderType.BUY
);

result.thenAccept(orderResult -> {
    if (orderResult.isSuccess()) {
        player.sendMessage("Order created successfully!");
    } else {
        player.sendMessage("Order failed: " + orderResult.getMessage());
    }
});
```

### Scheduling an Event

```java
// Get the temporal service
GlobalTimeManagementService temporal = plugin.getCalendarSystem().getGlobalTimeService();

// Schedule a Mayor election
MayorElection election = new MayorElection(
    "election_2024_01",
    "January 2024 Mayor Election",
    "Vote for your preferred mayor",
    LocalDateTime.now().plusDays(7),
    LocalDateTime.now().plusDays(14),
    Arrays.asList("Diana", "Jerry", "Paul")
);

temporal.scheduleEvent(election);
```

### Market Intervention

```java
// Get the dashboard service
EconomyDashboardService dashboard = rte.getDashboardService();

// Apply market intervention
dashboard.applyMarketIntervention(
    "ENCHANTED_DIAMOND",
    InterventionType.ADJUST_DROP_RATE,
    0.1, // 10% increase
    "Market manipulation detected"
);
```

## Configuration

### Economy Configuration

```yaml
economy:
  rte:
    enabled: true
    price-update-interval: 5 # seconds
    order-processing-interval: 1 # seconds
    market-analysis-interval: 30 # seconds
    bazaar-fee: 0.01 # 1%
    max-orders-per-player: 14
    max-orders-per-item: 1000
  dashboard:
    enabled: true
    alert-thresholds:
      price-volatility: 50.0
      price-change: 25.0
      volume-spike: 500.0
      manipulation: 100.0
```

### Temporal Configuration

```yaml
temporal:
  enabled: true
  time-sync-interval: 60 # seconds
  event-check-interval: 30 # seconds
  gim-integration:
    enabled: true
    scaling-threshold: 0.8 # 80% capacity
    pre-event-scaling: 300 # 5 minutes before event
```

## Monitoring and Maintenance

### Economy Monitoring

The `EconomyDashboardService` provides real-time monitoring:

- **Market Alerts**: Automatic detection of anomalies
- **Performance Metrics**: Order processing times, success rates
- **Intervention History**: Track all market interventions
- **API Status**: Monitor Hypixel API connectivity

### Temporal Monitoring

The `GlobalTimeManagementService` provides:

- **Event Status**: Current and upcoming events
- **Scaling Requests**: GIM integration status
- **Time Sync Status**: Server time synchronization
- **Event History**: Past event performance

## Troubleshooting

### Common Issues

1. **Redis Connection Issues**
   - Check Redis server status
   - Verify connection configuration
   - Check network connectivity

2. **Price Synchronization Issues**
   - Verify Redis Pub/Sub channels
   - Check server network connectivity
   - Monitor Redis memory usage

3. **Event Scheduling Issues**
   - Verify time synchronization
   - Check GIM service connectivity
   - Monitor event trigger messages

### Debug Commands

```java
// Check RTE status
/rte status

// Check temporal service status
/temporal status

// Force price update
/rte update-prices

// Force event check
/temporal check-events
```

## Performance Considerations

### Redis Optimization

- Use Redis clustering for high availability
- Configure appropriate memory limits
- Monitor Redis performance metrics
- Use Redis persistence for critical data

### Network Optimization

- Use dedicated Redis network
- Implement connection pooling
- Monitor network latency
- Use compression for large messages

## Security Considerations

### Market Protection

- Implement rate limiting for orders
- Monitor for suspicious trading patterns
- Use transaction logging for audit trails
- Implement automatic intervention triggers

### Access Control

- Restrict administrative dashboard access
- Implement role-based permissions
- Log all administrative actions
- Use secure Redis authentication

## Future Enhancements

### Planned Features

1. **Advanced Analytics**: Machine learning for market prediction
2. **Cross-Server Trading**: Direct player-to-player trading
3. **Dynamic Events**: Procedurally generated events
4. **Market Simulation**: Test market changes before implementation

### Integration Opportunities

1. **Discord Integration**: Market alerts and event notifications
2. **Web Dashboard**: Browser-based administrative interface
3. **Mobile App**: Player market access via mobile
4. **API Gateway**: External service integration

## Conclusion

The Agent V RTE and Temporal Management systems provide a robust foundation for real-time economy synchronization and global time management. These systems integrate seamlessly with the existing plugin architecture while providing significant improvements in performance, reliability, and administrative capabilities.

For additional support or questions, refer to the individual class documentation or contact the development team.
