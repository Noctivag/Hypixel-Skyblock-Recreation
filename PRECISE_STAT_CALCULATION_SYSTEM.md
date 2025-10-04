# Precise Stat Calculation System - Hypixel Skyblock Recreation

## Overview

This document describes the implementation of a precise stat calculation system that maintains exact numerical accuracy for the Hypixel Skyblock recreation. The system follows the strict order of operations and uses precise formulas to ensure game balance and meta-game integrity.

## Core Principles

### 1. Numerical Precision
- **CRITICAL**: All calculations must use exact numerical precision
- Deviations from original formulas destroy the meta-game and game balance
- All stat values must match Hypixel Skyblock exactly

### 2. Order of Operations
The calculation order is **CRITICAL** and must be followed exactly:

1. **Load player base stats**
2. **Add base stats from weapon and armor**
3. **Apply reforge stats (based on rarity)**
4. **Apply enchantment stats/effects**
5. **Apply global buffs (talismans, magical power, potions)**
6. **Calculate final damage with aggregated values**

## System Architecture

### Core Components

#### 1. StatCalculationService
- **Location**: `src/main/java/de/noctivag/skyblock/engine/StatCalculationService.java`
- **Purpose**: Main stat calculation engine
- **Features**:
  - Asynchronous stat calculations
  - Precise formula implementation
  - Performance optimization with caching
  - Integration with all stat sources

#### 2. ReforgeMatrixManager
- **Location**: `src/main/java/de/noctivag/skyblock/engine/ReforgeMatrixManager.java`
- **Purpose**: Manages reforge stat matrices
- **Features**:
  - External JSON configuration (`reforge_matrix.json`)
  - Rarity-based stat calculations
  - Item type compatibility
  - Easy balance adjustments

#### 3. AccessoryManager
- **Location**: `src/main/java/de/noctivag/skyblock/engine/AccessoryManager.java`
- **Purpose**: Manages Magical Power and Accessories system
- **Features**:
  - Unique accessory tracking
  - MP calculation with exact formulas
  - Power Stone selection and application
  - Complex MP to stat conversion

#### 4. PreciseDamageCalculator
- **Location**: `src/main/java/de/noctivag/skyblock/engine/PreciseDamageCalculator.java`
- **Purpose**: Implements exact damage formulas
- **Features**:
  - Melee damage: `Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)`
  - Ranged damage calculations
  - Magic damage calculations
  - Ferocity and critical hit handling

#### 5. StatAggregationPipeline
- **Location**: `src/main/java/de/noctivag/skyblock/engine/StatAggregationPipeline.java`
- **Purpose**: Ensures correct order of operations
- **Features**:
  - Step-by-step stat aggregation
  - Precise order enforcement
  - Derived stat calculations
  - Performance optimization

#### 6. StatEngineIntegration
- **Location**: `src/main/java/de/noctivag/skyblock/engine/StatEngineIntegration.java`
- **Purpose**: Integrates all components
- **Features**:
  - Unified API for stat calculations
  - Event handling
  - Performance optimization
  - System integration

## Core Damage Formula

### Melee Damage
```
Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)
```

### Additional Multipliers
- **Ferocity**: Chance for multiple attacks
- **Enchantments**: Various damage bonuses
- **Armor Reduction**: Target defense calculation

## Reforge System

### Configuration
- **File**: `src/main/resources/reforge_matrix.json`
- **Structure**: Item type → Reforge type → Rarity → Stats
- **Benefits**: Easy balance adjustments without code changes

### Supported Reforges
- **Weapons**: Sharp, Heavy, Light, Wise, Pure, Fierce
- **Armor**: Pure, Heavy, Wise
- **Accessories**: Bizarre, Forceful

### Rarity Values
- **Common**: 1 MP
- **Uncommon**: 2 MP
- **Rare**: 3 MP
- **Epic**: 5 MP
- **Legendary**: 8 MP
- **Mythic**: 12 MP
- **Divine**: 16 MP

## Magical Power System

### Power Stones
- **Bloody**: Strength + Health
- **Silky**: Critical Damage + Magic Find
- **Shaded**: Critical Damage + Intelligence
- **Bizarre**: Intelligence + Ability Damage
- **Forceful**: Strength + Defense
- **Hurtful**: Critical Damage + Strength
- **Itchy**: Attack Speed + Critical Chance
- **Jerry**: Intelligence + Speed
- **Light**: Speed + Critical Chance
- **Magnetic**: Mining Speed + Mining Fortune
- **Auspicious**: Mining Fortune + Mining Wisdom
- **Fortunate**: Mining Fortune + Magic Find
- **Heated**: Mining Speed + Mining Wisdom
- **Sweet**: Farming Fortune + Farming Wisdom
- **Submerged**: Sea Creature Chance + Fishing Wisdom
- **Wither**: True Defense + Ability Damage

### MP Calculation
- Only unique accessories count (no duplicates)
- MP values based on rarity
- Complex scaling formulas for stat conversion

## Stat Types

### Primary Stats
- **Health**: Maximum health points
- **Defense**: Damage reduction
- **Strength**: Melee damage multiplier
- **Intelligence**: Mana capacity and magic damage
- **Speed**: Movement speed
- **Critical Chance**: Chance for critical hits
- **Critical Damage**: Critical hit damage multiplier
- **Attack Speed**: Attack frequency
- **Ferocity**: Multiple attack chance

### Secondary Stats
- **Mining Stats**: Mining Speed, Mining Fortune, Mining Wisdom
- **Farming Stats**: Farming Fortune, Farming Wisdom
- **Fishing Stats**: Sea Creature Chance, Fishing Wisdom
- **Combat Stats**: Magic Find, True Defense, Ability Damage, True Damage, Combat Wisdom
- **Pet Stats**: Pet Luck, Taming Wisdom
- **Skill Stats**: Various wisdom stats for different skills

## Performance Optimization

### Caching System
- **Duration**: 1 second cache for stat calculations
- **Strategy**: Cache frequently accessed calculations
- **Invalidation**: Automatic cache expiration

### Asynchronous Processing
- **Update Interval**: 5 ticks (0.25 seconds)
- **Strategy**: Non-blocking stat calculations
- **Benefits**: Improved server performance

### Memory Management
- **Cleanup**: Automatic cleanup on player disconnect
- **Strategy**: Efficient memory usage
- **Monitoring**: Built-in performance monitoring

## Integration Points

### Event Handling
- **Player Join**: Initialize stat profiles
- **Player Quit**: Clean up player data
- **Damage Events**: Apply precise damage calculations

### API Access
- **Stat Queries**: Get current stat values
- **Damage Calculation**: Calculate precise damage
- **Profile Management**: Manage player stat profiles

## Usage Examples

### Get Player Stats
```java
StatEngineIntegration statEngine = plugin.getStatEngine();
CompletableFuture<Double> strength = statEngine.getPlayerStat(player, PrimaryStat.STRENGTH);
```

### Calculate Damage
```java
CompletableFuture<DamageResult> damage = statEngine.calculateDamage(attacker, target);
```

### Refresh Stats
```java
statEngine.refreshPlayerStats(player);
```

## Configuration

### Reforge Matrix
Edit `src/main/resources/reforge_matrix.json` to adjust reforge values:

```json
{
  "weapon": {
    "sharp": {
      "displayName": "Sharp",
      "color": "§c",
      "description": "Increases damage and critical chance",
      "stats": {
        "common": {
          "strength": 15,
          "critChance": 0
        }
      }
    }
  }
}
```

### Power Stones
Power stone configurations are defined in `AccessoryManager.java` and can be easily modified.

## Testing and Validation

### Stat Validation
- Compare calculated values with Hypixel Skyblock
- Verify formula accuracy
- Test edge cases and boundary conditions

### Performance Testing
- Monitor calculation times
- Test with multiple players
- Validate memory usage

### Integration Testing
- Test with existing systems
- Verify event handling
- Test error conditions

## Future Enhancements

### Planned Features
- **Pet Integration**: Pet stat bonuses
- **Skill Integration**: Skill-based stat bonuses
- **Potion Integration**: Potion effect stat bonuses
- **Enchantment Integration**: Custom enchantment stat bonuses

### Performance Improvements
- **Database Caching**: Persistent stat caching
- **Batch Processing**: Multiple player updates
- **Optimized Algorithms**: Faster calculation methods

## Troubleshooting

### Common Issues
1. **Incorrect Stat Values**: Check reforge matrix configuration
2. **Performance Issues**: Monitor cache hit rates
3. **Memory Leaks**: Verify cleanup on player disconnect

### Debug Tools
- **Stat Profiles**: Access via `getPlayerStatProfiles()`
- **Aggregations**: Access via `getPlayerAggregations()`
- **Detailed Logging**: Enable debug mode for detailed output

## Conclusion

The Precise Stat Calculation System provides a robust, accurate, and performant implementation of Hypixel Skyblock's stat mechanics. By following the exact order of operations and using precise formulas, it maintains game balance and meta-game integrity while providing excellent performance and maintainability.

The system is designed to be easily extensible and configurable, allowing for future enhancements and balance adjustments without code changes. The external configuration system ensures that stat values can be adjusted quickly and easily to match any changes in the original game.
