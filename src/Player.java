package src;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages player-related data like score, coins, and upgrades.
 * Handles user input and decision validation.
 */
public class Player {
    private int score;
    private int coins;
    private int level;
    private int consecutiveCorrectActions;
    private double comboMultiplier;
    private Map<UpgradeType, Integer> upgradeLevel;
    
    /**
     * Constructor for the player
     */
    public Player() {
        score = 0;
        coins = 0;
        level = 1;
        consecutiveCorrectActions = 0;
        comboMultiplier = 1.0;
        upgradeLevel = new HashMap<>();
        
        // Initialize all upgrades to level 0
        for (UpgradeType type : UpgradeType.values()) {
            upgradeLevel.put(type, 0);
        }
    }
    
    /**
     * Award points for correct actions
     */
    public void awardPoints(int basePoints) {
        int calculatedPoints = (int) (basePoints * comboMultiplier);
        score += calculatedPoints;
        
        // Update combo multiplier
        consecutiveCorrectActions++;
        updateComboMultiplier();
        
        // Award coins (1 coin per 10 points)
        awardCoins(calculatedPoints / 10);
        if (calculatedPoints % 10 == 0 && calculatedPoints > 0) {
            awardCoins(1);
        }
    }
    
    /**
     * Deduct points for incorrect actions
     */
    public void deductPoints(int points) {
        score = Math.max(0, score - points); // Never go below 0
        
        // Reset combo on incorrect action
        consecutiveCorrectActions = 0;
        updateComboMultiplier();
    }
    
    /**
     * Award coins to the player
     */
    public void awardCoins(int amount) {
        coins += amount;
    }
    
    /**
     * Deduct coins for purchases
     */
    public boolean deductCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false; // Not enough coins
    }
    
    /**
     * Update the combo multiplier based on consecutive correct actions
     */
    private void updateComboMultiplier() {
        // Cap combo at 10 consecutive actions for a max 2.0x multiplier
        comboMultiplier = 1.0 + (Math.min(consecutiveCorrectActions, 10) * 0.1);
    }
    
    /**
     * Get the current level of a specific upgrade
     */
    public int getUpgradeLevel(UpgradeType type) {
        return upgradeLevel.getOrDefault(type, 0);
    }
    
    /**
     * Increase the level of a specific upgrade
     */
    public void increaseUpgradeLevel(UpgradeType type) {
        int currentLevel = upgradeLevel.getOrDefault(type, 0);
        upgradeLevel.put(type, currentLevel + 1);
    }
    
    /**
     * Check if player has enough points for next level
     */
    public boolean checkLevelUp() {
        // Level up every 500 * current level points
        int requiredPoints = 500 * level;
        return score >= requiredPoints;
    }
    
    /**
     * Level up the player
     */
    public void levelUp() {
        level++;
    }
    
    // Getters for player attributes
    
    public int getScore() {
        return score;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getComboMultiplier() {
        return comboMultiplier;
    }
    
    /**
     * Get the response speed modifier based on the Response Speed upgrade
     * Lower values mean faster response times
     */
    public double getResponseSpeedModifier() {
        // Each level reduces response time by 5%
        int upgradeLevel = getUpgradeLevel(UpgradeType.RESPONSE_SPEED);
        return 1.0 - (upgradeLevel * 0.05);
    }
    
    /**
     * Get the base response time in milliseconds (before modifier)
     */
    public int getBaseResponseTime() {
        // Base time of 3 seconds (3000ms)
        return 3000;
    }
    
    /**
     * Get the actual response time in milliseconds after applying upgrade modifier
     */
    public int getActualResponseTime() {
        return (int) (getBaseResponseTime() * getResponseSpeedModifier());
    }
    
    /**
     * Get the spam filter effectiveness based on the Spam Filter upgrade
     */
    public double getSpamFilterChance() {
        // Each level gives 5% chance to auto-delete spam
        return getUpgradeLevel(UpgradeType.SPAM_FILTER) * 0.05;
    }
    
    /**
     * Get the number of quick reply templates based on the Quick Reply upgrade
     */
    public int getQuickReplyTemplateCount() {
        // Each level provides 1 template
        return getUpgradeLevel(UpgradeType.QUICK_REPLY);
    }
    
    /**
     * Get the search speed modifier based on the Search Function upgrade
     */
    public double getSearchSpeedModifier() {
        // Each level increases search speed by 10%
        return 1.0 + (getUpgradeLevel(UpgradeType.SEARCH_FUNCTION) * 0.1);
    }
    
    /**
     * Get the attachment space reduction based on the Attachment Compressor upgrade
     */
    public double getAttachmentSpaceReduction() {
        // Each level reduces space by 5%
        return getUpgradeLevel(UpgradeType.ATTACHMENT_COMPRESSOR) * 0.05;
    }
}