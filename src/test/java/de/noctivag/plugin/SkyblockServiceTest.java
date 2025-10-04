package de.noctivag.plugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.skyblock.SkyblockService;
import de.noctivag.plugin.features.skyblock.impl.SkyblockServiceImpl;
import de.noctivag.plugin.infrastructure.config.ConfigService;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for SkyblockService
 */
@ExtendWith(MockitoExtension.class)
class SkyblockServiceTest {
    
    @Mock
    private JavaPlugin plugin;
    
    @Mock
    private ConfigService configService;
    
    @Mock
    private Logger logger;
    
    private SkyblockService skyblockService;
    
    @BeforeEach
    void setUp() {
        // Mock the logger
        when(plugin.getLogger()).thenReturn(logger);
        
        skyblockService = new SkyblockServiceImpl(plugin, configService);
        // Enable the service for testing
        skyblockService.enable().join();
    }
    
    @Test
    void shouldInitializeSuccessfully() {
        // When
        CompletableFuture<Void> initFuture = skyblockService.initialize();
        
        // Then
        assertThat(initFuture).isNotNull();
        assertThat(skyblockService.isInitialized()).isTrue();
    }
    
    @Test
    void shouldCreateIslandForPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        
        // When
        CompletableFuture<Void> createFuture = skyblockService.createIsland(playerId);
        createFuture.join(); // Wait for completion
        
        // Then
        assertThat(createFuture).isNotNull();
        assertThat(skyblockService.hasIsland(playerId)).isTrue();
    }
    
    @Test
    void shouldReturnCorrectCoinsForPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        double expectedCoins = 1000.0;
        
        // When
        skyblockService.createIsland(playerId).join(); // Wait for completion
        double actualCoins = skyblockService.getCoins(playerId);
        
        // Then
        assertThat(actualCoins).isEqualTo(expectedCoins);
    }
    
    @Test
    void shouldAddCoinsToPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        double initialCoins = 1000.0;
        double coinsToAdd = 500.0;
        double expectedCoins = initialCoins + coinsToAdd;
        
        // When
        skyblockService.createIsland(playerId).join(); // Wait for completion
        skyblockService.addCoins(playerId, coinsToAdd);
        double actualCoins = skyblockService.getCoins(playerId);
        
        // Then
        assertThat(actualCoins).isEqualTo(expectedCoins);
    }
    
    @Test
    void shouldRemoveCoinsFromPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        double initialCoins = 1000.0;
        double coinsToRemove = 300.0;
        double expectedCoins = initialCoins - coinsToRemove;
        
        // When
        skyblockService.createIsland(playerId).join(); // Wait for completion
        boolean success = skyblockService.removeCoins(playerId, coinsToRemove);
        double actualCoins = skyblockService.getCoins(playerId);
        
        // Then
        assertThat(success).isTrue();
        assertThat(actualCoins).isEqualTo(expectedCoins);
    }
    
    @Test
    void shouldNotRemoveCoinsIfInsufficientFunds() {
        // Given
        UUID playerId = UUID.randomUUID();
        double initialCoins = 1000.0;
        double coinsToRemove = 1500.0;
        
        // When
        skyblockService.createIsland(playerId).join(); // Wait for completion
        boolean success = skyblockService.removeCoins(playerId, coinsToRemove);
        double actualCoins = skyblockService.getCoins(playerId);
        
        // Then
        assertThat(success).isFalse();
        assertThat(actualCoins).isEqualTo(initialCoins);
    }
}
