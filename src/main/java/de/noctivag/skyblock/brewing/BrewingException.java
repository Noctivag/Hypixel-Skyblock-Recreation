package de.noctivag.skyblock.brewing;

/**
 * Exception for brewing system
 */
public class BrewingException extends Exception {
    
    public BrewingException(String message) {
        super(message);
    }
    
    public BrewingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BrewingException(Throwable cause) {
        super(cause);
    }
}
