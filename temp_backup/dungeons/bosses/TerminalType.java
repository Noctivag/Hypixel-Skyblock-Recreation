package de.noctivag.skyblock.dungeons.bosses;

/**
 * Represents the type of terminal in dungeon boss fights
 */
public enum TerminalType {
    COLOR("Color Terminal"),
    ORDER("Order Terminal"),
    SYMBOL("Symbol Terminal"),
    SEQUENCE("Sequence Terminal"),
    RHYTHM("Rhythm Terminal"),
    MEMORY("Memory Terminal"),
    LOGIC("Logic Terminal"),
    PATTERN("Pattern Terminal");

    private final String displayName;

    TerminalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
