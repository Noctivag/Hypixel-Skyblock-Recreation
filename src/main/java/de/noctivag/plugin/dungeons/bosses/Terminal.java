package de.noctivag.plugin.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Terminal - Represents a terminal puzzle for Necron Phase 3
 */
public class Terminal {
    
    private final String id;
    private final Location location;
    private final TerminalType type;
    private TerminalState state;
    private String currentPuzzle;
    private String correctAnswer;
    private long puzzleStartTime;
    private long puzzleTimeLimit = 30000L; // 30 seconds
    
    public Terminal(String id, Location location, TerminalType type) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.state = TerminalState.IDLE;
        
        // Set up terminal block
        location.getBlock().setType(Material.ENCHANTING_TABLE);
        
        generateNewPuzzle();
    }
    
    /**
     * Update terminal state
     */
    public void update() {
        switch (state) {
            case IDLE -> updateIdle();
            case ACTIVE -> updateActive();
            case SOLVED -> updateSolved();
            case FAILED -> updateFailed();
        }
    }
    
    /**
     * Update idle state
     */
    private void updateIdle() {
        // Terminal is waiting for player interaction
        // Show visual indicator
        if (Math.random() < 0.1) { // 10% chance per update
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, 
                location.clone().add(0.5, 1, 0.5), 5, 0.3, 0.3, 0.3, 0.1);
        }
    }
    
    /**
     * Update active state
     */
    private void updateActive() {
        long currentTime = System.currentTimeMillis();
        
        // Check if puzzle time limit exceeded
        if (currentTime - puzzleStartTime >= puzzleTimeLimit) {
            failPuzzle();
            return;
        }
        
        // Show puzzle progress
        long remainingTime = puzzleTimeLimit - (currentTime - puzzleStartTime);
        double progress = 1.0 - (double) remainingTime / puzzleTimeLimit;
        
        // Visual progress indicator
        if (Math.random() < 0.2) { // 20% chance per update
            location.getWorld().spawnParticle(Particle.REDSTONE, 
                location.clone().add(0.5, 1 + progress, 0.5), 3, 0.1, 0.1, 0.1, 0.1);
        }
    }
    
    /**
     * Update solved state
     */
    private void updateSolved() {
        // Show success effects
        if (Math.random() < 0.3) { // 30% chance per update
            location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, 
                location.clone().add(0.5, 1, 0.5), 3, 0.3, 0.3, 0.3, 0.1);
        }
    }
    
    /**
     * Update failed state
     */
    private void updateFailed() {
        // Show failure effects
        if (Math.random() < 0.2) { // 20% chance per update
            location.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, 
                location.clone().add(0.5, 1, 0.5), 3, 0.3, 0.3, 0.3, 0.1);
        }
        
        // Reset to idle after 5 seconds
        if (System.currentTimeMillis() - puzzleStartTime >= 5000) {
            state = TerminalState.IDLE;
            generateNewPuzzle();
        }
    }
    
    /**
     * Activate terminal (start puzzle)
     */
    public boolean activate(Player player) {
        if (state != TerminalState.IDLE) {
            return false; // Terminal is not available
        }
        
        state = TerminalState.ACTIVE;
        puzzleStartTime = System.currentTimeMillis();
        
        // Send puzzle to player
        sendPuzzleToPlayer(player);
        
        // Visual and sound effects
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, 
            location.clone().add(0.5, 1, 0.5), 20, 0.5, 0.5, 0.5, 0.1);
        location.getWorld().playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Submit answer to terminal
     */
    public boolean submitAnswer(Player player, String answer) {
        if (state != TerminalState.ACTIVE) {
            return false; // No active puzzle
        }
        
        if (answer.equalsIgnoreCase(correctAnswer)) {
            solvePuzzle(player);
            return true;
        } else {
            failPuzzle();
            return false;
        }
    }
    
    /**
     * Solve the puzzle
     */
    private void solvePuzzle(Player player) {
        state = TerminalState.SOLVED;
        
        // Success effects
        location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, 
            location.clone().add(0.5, 1, 0.5), 30, 0.5, 0.5, 0.5, 0.1);
        location.getWorld().playSound(location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        
        // Change terminal appearance
        location.getBlock().setType(Material.EMERALD_BLOCK);
        
        // Announce success
        player.sendMessage("§a§lTerminal solved! Necron is weakened!");
        
        // TODO: Apply Necron weakening effect
    }
    
    /**
     * Fail the puzzle
     */
    private void failPuzzle() {
        state = TerminalState.FAILED;
        
        // Failure effects
        location.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, 
            location.clone().add(0.5, 1, 0.5), 20, 0.5, 0.5, 0.5, 0.1);
        location.getWorld().playSound(location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        
        // Change terminal appearance
        location.getBlock().setType(Material.REDSTONE_BLOCK);
    }
    
    /**
     * Generate new puzzle
     */
    private void generateNewPuzzle() {
        switch (type) {
            case NUMBER_SEQUENCE -> generateNumberSequencePuzzle();
            case COLOR_PATTERN -> generateColorPatternPuzzle();
            case SYMBOL_MATCH -> generateSymbolMatchPuzzle();
            case DIRECTION_ARROW -> generateDirectionArrowPuzzle();
        }
    }
    
    /**
     * Generate number sequence puzzle
     */
    private void generateNumberSequencePuzzle() {
        // Generate a simple arithmetic sequence
        int start = (int) (Math.random() * 10) + 1;
        int difference = (int) (Math.random() * 5) + 1;
        
        StringBuilder sequence = new StringBuilder();
        StringBuilder answer = new StringBuilder();
        
        for (int i = 0; i < 4; i++) {
            int number = start + (i * difference);
            sequence.append(number);
            if (i < 3) sequence.append(", ");
            
            if (i == 3) {
                answer.append(number + difference); // Next number in sequence
            }
        }
        
        currentPuzzle = "Complete the sequence: " + sequence + ", ?";
        correctAnswer = answer.toString();
    }
    
    /**
     * Generate color pattern puzzle
     */
    private void generateColorPatternPuzzle() {
        String[] colors = {"Red", "Blue", "Green", "Yellow", "Purple"};
        StringBuilder pattern = new StringBuilder();
        StringBuilder answer = new StringBuilder();
        
        // Generate 4-color pattern
        for (int i = 0; i < 4; i++) {
            String color = colors[(int) (Math.random() * colors.length)];
            pattern.append(color);
            if (i < 3) pattern.append(" - ");
            
            if (i == 3) {
                // Find next color in pattern (simple repetition)
                String nextColor = pattern.toString().split(" - ")[0];
                answer.append(nextColor);
            }
        }
        
        currentPuzzle = "Complete the pattern: " + pattern + " - ?";
        correctAnswer = answer.toString();
    }
    
    /**
     * Generate symbol match puzzle
     */
    private void generateSymbolMatchPuzzle() {
        String[] symbols = {"★", "◆", "●", "▲", "■"};
        String symbol1 = symbols[(int) (Math.random() * symbols.length)];
        String symbol2 = symbols[(int) (Math.random() * symbols.length)];
        
        currentPuzzle = "Match the symbol: " + symbol1 + " = ?";
        correctAnswer = symbol2;
    }
    
    /**
     * Generate direction arrow puzzle
     */
    private void generateDirectionArrowPuzzle() {
        String[] directions = {"North", "South", "East", "West"};
        String direction = directions[(int) (Math.random() * directions.length)];
        
        currentPuzzle = "Which direction is opposite to " + direction + "?";
        
        // Find opposite direction
        switch (direction) {
            case "North" -> correctAnswer = "South";
            case "South" -> correctAnswer = "North";
            case "East" -> correctAnswer = "West";
            case "West" -> correctAnswer = "East";
        }
    }
    
    /**
     * Send puzzle to player
     */
    private void sendPuzzleToPlayer(Player player) {
        player.sendMessage("§6§l=== TERMINAL PUZZLE ===");
        player.sendMessage("§7Type: " + type.name().replace("_", " "));
        player.sendMessage("§f" + currentPuzzle);
        player.sendMessage("§7Time limit: 30 seconds");
        player.sendMessage("§7Use §e/terminal answer <answer> §7to submit your answer!");
    }
    
    /**
     * Get terminal state
     */
    public TerminalState getState() {
        return state;
    }
    
    /**
     * Get terminal location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Get terminal ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get terminal type
     */
    public TerminalType getType() {
        return type;
    }
    
    /**
     * Check if terminal is solved
     */
    public boolean isSolved() {
        return state == TerminalState.SOLVED;
    }
    
    /**
     * Reset terminal to idle state
     */
    public void reset() {
        state = TerminalState.IDLE;
        location.getBlock().setType(Material.ENCHANTING_TABLE);
        generateNewPuzzle();
    }
    
    /**
     * Terminal State enum
     */
    public enum TerminalState {
        IDLE,      // Waiting for player interaction
        ACTIVE,    // Puzzle is active
        SOLVED,    // Puzzle has been solved
        FAILED     // Puzzle failed
    }
}
