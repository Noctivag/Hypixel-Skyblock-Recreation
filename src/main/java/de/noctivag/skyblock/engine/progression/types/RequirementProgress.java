package de.noctivag.skyblock.engine.progression.types;

import java.util.List;

/**
 * Requirement Progress
 * 
 * Contains detailed progress information for all requirements
 * for a specific content type and content ID.
 */
public class RequirementProgress {
    
    private final ContentType contentType;
    private final String contentId;
    private final List<RequirementProgressItem> progressItems;
    
    public RequirementProgress(ContentType contentType, String contentId, 
                              List<RequirementProgressItem> progressItems) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.progressItems = progressItems;
    }
    
    public ContentType getContentType() {
        return contentType;
    }
    
    public String getContentId() {
        return contentId;
    }
    
    public List<RequirementProgressItem> getProgressItems() {
        return progressItems;
    }
    
    public boolean isAllRequirementsMet() {
        return progressItems.stream().allMatch(RequirementProgressItem::isMet);
    }
    
    public int getCompletedRequirements() {
        return (int) progressItems.stream().filter(RequirementProgressItem::isMet).count();
    }
    
    public int getTotalRequirements() {
        return progressItems.size();
    }
    
    public double getProgressPercentage() {
        if (getTotalRequirements() == 0) return 100.0;
        return (double) getCompletedRequirements() / getTotalRequirements() * 100.0;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s): %d/%d requirements met (%.1f%%)", 
            contentType.getDisplayName(), contentId, 
            getCompletedRequirements(), getTotalRequirements(), 
            getProgressPercentage());
    }
}
