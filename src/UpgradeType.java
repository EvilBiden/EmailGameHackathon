package src;

/**
 * Enum for different types of upgrades available in the game
 */
enum UpgradeType {
    INBOX_CAPACITY("Inbox Capacity", "Increase maximum storage", "+10 emails per level", 100),
    RESPONSE_SPEED("Response Speed", "Decrease time to process emails", "-5% per level", 150),
    SPAM_FILTER("Spam Filter", "Auto-delete obvious spam", "5% chance per level", 200),
    QUICK_REPLY("Quick Reply", "Reduce reply time with templates", "-10% time per level", 125),  // Modified description to match new functionality
    SEARCH_FUNCTION("Search Function", "Find specific emails faster", "+10% search speed", 175),
    ATTACHMENT_COMPRESSOR("Attachment Compressor", "Reduce space used by attachments", "-5% space per level", 150);
    
    private final String name;
    private final String description;
    private final String effect;
    private final int baseCost;
    
    UpgradeType(String name, String description, String effect, int baseCost) {
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.baseCost = baseCost;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getEffect() {
        return effect;
    }
    
    public int getBaseCost() {
        return baseCost;
    }
}