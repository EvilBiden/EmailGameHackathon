package src;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controls game flow and level progression.
 * Manages difficulty scaling and win/lose conditions.
 */
public class GameManager {
    private Player player;
    private EmailSystem emailSystem;
    private UpgradeSystem upgradeSystem;
    
    private boolean gameRunning;
    private boolean gamePaused;
    private int incorrectDeletesCount;
    private int missedCriticalEmailsCount;
    private Random random;
    
    // Game balance parameters
    private static final int MAX_INCORRECT_DELETES = 5;
    private static final int MAX_MISSED_CRITICAL = 3;
    
    // Scheduler for email generation
    private ScheduledExecutorService scheduler;
    
    // UI reference
    private MainScreen mainScreen;
    
    /**
     * Constructor for the game manager
     */
    public GameManager(Player player, EmailSystem emailSystem, UpgradeSystem upgradeSystem) {
        this.player = player;
        this.emailSystem = emailSystem;
        this.upgradeSystem = upgradeSystem;
        
        gameRunning = false;
        gamePaused = false;
        incorrectDeletesCount = 0;
        missedCriticalEmailsCount = 0;
        random = new Random();
        
        // Create scheduler for email generation
        scheduler = Executors.newScheduledThreadPool(1);
    }
    
    /**
     * Set the main screen reference for UI updates
     */
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        
        // Now that everything is initialized, we can update the UI
        if (mainScreen != null) {
            SwingUtilities.invokeLater(() -> {
                mainScreen.updateUI();
            });
        }
    }
    
    /**
     * Start the game loop
     */
    public void startGameLoop() {
        if (gameRunning) {
            return;
        }
        
        gameRunning = true;
        gamePaused = false;
        
        // Schedule email generation
        // Rate depends on current level
        scheduleEmailGeneration();
    }
    
    /**
     * Schedule email generation at intervals appropriate for the current level
     */
    private void scheduleEmailGeneration() {
        // Calculate delay based on level
        // Higher levels have faster email arrival
        int baseDelaySeconds = Math.max(10 - player.getLevel(), 2);
        int randomVariation = random.nextInt(3) - 1; // -1 to +1
        int delaySeconds = Math.max(1, baseDelaySeconds + randomVariation);
        
        scheduler.scheduleAtFixedRate(() -> {
            if (!gamePaused && gameRunning) {
                generateNewEmail();
                checkInboxCapacity();
                
                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    if (mainScreen != null) {
                        mainScreen.updateUI();
                    }
                });
                
                // Check level progression
                if (player.checkLevelUp()) {
                    advanceLevel();
                }
            }
        }, 0, delaySeconds, TimeUnit.SECONDS);
    }
    
    /**
     * Generate a new email and add it to the inbox
     */
    private void generateNewEmail() {
        Email newEmail = emailSystem.generateEmail(player.getLevel());
        
        // Check if spam filter catches this email
        if (newEmail.isSpam() && random.nextDouble() < player.getSpamFilterChance()) {
            // Auto-delete spam and award points
            player.awardPoints(5);
            return; // Don't add to inbox
        }
        
        // Add to inbox if there's space
        if (!emailSystem.addToInbox(newEmail)) {
            // Inbox is full - game over
            if (mainScreen != null) {
                SwingUtilities.invokeLater(() -> {
                    gameOver("Your inbox is full! Game Over.");
                });
            }
        }
    }
    
    /**
     * Check if inbox has reached capacity
     */
    private void checkInboxCapacity() {
        if (emailSystem.isInboxFull()) {
            if (mainScreen != null) {
                SwingUtilities.invokeLater(() -> {
                    gameOver("Your inbox is full! Game Over.");
                });
            }
        }
    }
    
    /**
     * Process player action on an email
     */
    public void processEmailAction(Email email, String action) {
        if (!gameRunning || gamePaused) {
            return;
        }
        
        if (action.equals("respond") && email.isLegitimate()) {
            // Correct response to legitimate email
            player.awardPoints(10);
            emailSystem.removeFromInbox(email);
        } else if (action.equals("delete") && email.isSpam()) {
            // Correct deletion of spam
            player.awardPoints(5);
            emailSystem.removeFromInbox(email);
        } else if (action.equals("mark_spam") && email.isSpam()) {
            // Correctly identified spam
            player.awardPoints(5);
            emailSystem.removeFromInbox(email);
        } else if (action.equals("mark_spam") && !email.isSpam()) {
            // Incorrectly marked legitimate email as spam
            player.deductPoints(15);
            incorrectDeletesCount++;
            emailSystem.removeFromInbox(email);
            
            // Check if too many legitimate emails incorrectly handled
            if (incorrectDeletesCount >= MAX_INCORRECT_DELETES) {
                if (mainScreen != null) {
                    SwingUtilities.invokeLater(() -> {
                        gameOver("You've marked too many legitimate emails as spam! Game Over.");
                    });
                }
            }
        } else {
            // Incorrect action
            player.deductPoints(15);
            
            if (action.equals("delete") && email.isLegitimate()) {
                // Incorrectly deleted legitimate email
                incorrectDeletesCount++;
                
                if (incorrectDeletesCount >= MAX_INCORRECT_DELETES) {
                    if (mainScreen != null) {
                        SwingUtilities.invokeLater(() -> {
                            gameOver("You've deleted too many legitimate emails! Game Over.");
                        });
                    }
                }
            } else if (action.equals("ignore") && email.isUrgent()) {
                // Missed critical email
                missedCriticalEmailsCount++;
                
                if (missedCriticalEmailsCount >= MAX_MISSED_CRITICAL) {
                    if (mainScreen != null) {
                        SwingUtilities.invokeLater(() -> {
                            gameOver("You've missed too many critical emails! Game Over.");
                        });
                    }
                }
            }
            
            // For any other incorrect actions, just remove the email
            if (!action.equals("ignore")) {
                emailSystem.removeFromInbox(email);
            }
        }
        
        // Update UI after action
        if (mainScreen != null) {
            SwingUtilities.invokeLater(() -> {
                mainScreen.updateUI();
            });
        }
    }
    
    /**
     * Advance to the next level
     */
    private void advanceLevel() {
        player.levelUp();
        
        // Award bonus coins for level completion
        player.awardCoins(player.getLevel() * 50);
        
        // Recalibrate email generation rate
        scheduler.shutdown();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduleEmailGeneration();
        
        // Update inbox capacity based on upgrade
        int inboxCapacityUpgradeLevel = player.getUpgradeLevel(UpgradeType.INBOX_CAPACITY);
        emailSystem.setInboxCapacity(20 + (inboxCapacityUpgradeLevel * 10));
        
        // Notify player of level up
        if (mainScreen != null) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(mainScreen,
                    "Congratulations! You've reached Level " + player.getLevel() + "!\n" +
                    "You've been awarded " + (player.getLevel() * 50) + " bonus coins.",
                    "Level Up!",
                    JOptionPane.INFORMATION_MESSAGE);
                
                mainScreen.updateUI();
            });
        }
    }
    
    /**
     * Purchase an upgrade for the player
     */
    public boolean purchaseUpgrade(UpgradeType type) {
        boolean success = upgradeSystem.purchaseUpgrade(player, type);
        
        if (success && type == UpgradeType.INBOX_CAPACITY) {
            // Update inbox capacity immediately
            int inboxCapacityUpgradeLevel = player.getUpgradeLevel(UpgradeType.INBOX_CAPACITY);
            emailSystem.setInboxCapacity(20 + (inboxCapacityUpgradeLevel * 10));
        }
        
        return success;
    }
    
    /**
     * Pause the game
     */
    public void pauseGame() {
        gamePaused = true;
    }
    
    /**
     * Resume the game
     */
    public void resumeGame() {
        gamePaused = false;
    }
    
    /**
     * End the game
     */
    public void gameOver(String message) {
        gameRunning = false;
        scheduler.shutdownNow();
        
        JOptionPane.showMessageDialog(mainScreen,
            message + "\n\n" +
            "Final Score: " + player.getScore() + "\n" +
            "Final Level: " + player.getLevel(),
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Get the player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get the email system
     */
    public EmailSystem getEmailSystem() {
        return emailSystem;
    }
    
    /**
     * Get the upgrade system
     */
    public UpgradeSystem getUpgradeSystem() {
        return upgradeSystem;
    }
    
    /**
     * Check if the game is running
     */
    public boolean isGameRunning() {
        return gameRunning;
    }
    
    /**
     * Check if the game is paused
     */
    public boolean isGamePaused() {
        return gamePaused;
    }
}