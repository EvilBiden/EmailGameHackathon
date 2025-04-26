package src;

/**
 * Enum for different types of upgrades available in the game
 */
enum UpgradeType {
    INBOX_CAPACITY("Inbox Capacity", "Increase maximum storage", "+10 emails per level", 100),
    RESPONSE_SPEED("Response Speed", "Decrease time to process emails", "-5% per level", 150),
    SPAM_FILTER("Spam Filter", "Auto-delete obvious spam", "5% chance per level", 200),
    QUICK_REPLY("Quick Reply", "Prepare template responses", "+1 template per level", 125),
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

/**
 * Manages available upgrades, purchase transactions, and applies upgrade effects.
 */
public class UpgradeSystem {
    private static final int MAX_UPGRADE_LEVEL = 10;
    
    /**
     * Constructor for the upgrade system
     */
    public UpgradeSystem() {
        // Initialize any necessary data
    }
    
    /**
     * Calculate the cost of the next upgrade level
     */
    public int getUpgradeCost(UpgradeType type, int currentLevel) {
        // Each level increases the cost by 50%
        return (int)(type.getBaseCost() * (1 + (currentLevel * 0.5)));
    }
    
    /**
     * Check if the upgrade can be purchased
     */
    public boolean canPurchaseUpgrade(Player player, UpgradeType type) {
        int currentLevel = player.getUpgradeLevel(type);
        
        // Check if already at max level
        if (currentLevel >= MAX_UPGRADE_LEVEL) {
            return false;
        }
        
        // Check if player has enough coins
        int cost = getUpgradeCost(type, currentLevel);
        return player.getCoins() >= cost;
    }
    
    /**
     * Purchase an upgrade for the player
     */
    public boolean purchaseUpgrade(Player player, UpgradeType type) {
        if (!canPurchaseUpgrade(player, type)) {
            return false;
        }
        
        int currentLevel = player.getUpgradeLevel(type);
        int cost = getUpgradeCost(type, currentLevel);
        
        if (player.deductCoins(cost)) {
            player.increaseUpgradeLevel(type);
            applyUpgradeEffect(player, type);
            return true;
        }
        
        return false;
    }
    
    /**
     * Apply the effect of an upgrade to the game
     */
    private void applyUpgradeEffect(Player player, UpgradeType type) {
        // Some upgrades need to be applied immediately
        // Others are applied dynamically when needed
        
        switch (type) {
            case INBOX_CAPACITY:
                // This will be applied in GameManager
                break;
                
            case RESPONSE_SPEED:
                // Applied through getResponseSpeedModifier() in Player
                break;
                
            case SPAM_FILTER:
                // Applied through getSpamFilterChance() in Player
                break;
                
            case QUICK_REPLY:
                // Applied through getQuickReplyTemplateCount() in Player
                break;
                
            case SEARCH_FUNCTION:
                // Applied through getSearchSpeedModifier() in Player
                break;
                
            case ATTACHMENT_COMPRESSOR:
                // Applied through getAttachmentSpaceReduction() in Player
                break;
        }
    }
    
    /**
     * Get a description of the upgrade effect at a specific level
     */
    public String getUpgradeEffectDescription(UpgradeType type, int level) {
        switch (type) {
            case INBOX_CAPACITY:
                return "+" + (10 * level) + " capacity";
                
            case RESPONSE_SPEED:
                return "-" + (5 * level) + "% time";
                
            case SPAM_FILTER:
                return (5 * level) + "% auto-delete";
                
            case QUICK_REPLY:
                return level + " templates";
                
            case SEARCH_FUNCTION:
                return "+" + (10 * level) + "% speed";
                
            case ATTACHMENT_COMPRESSOR:
                return "-" + (5 * level) + "% space";
                
            default:
                return "";
        }
    }
    
    /**
     * Get all available upgrade types
     */
    public UpgradeType[] getAllUpgradeTypes() {
        return UpgradeType.values();
    }
}
