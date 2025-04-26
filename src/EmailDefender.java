package src;

import javax.swing.*;
import java.awt.*;

/**
 * Main class for the Email Defender game.
 * Initializes the game and starts the application.
 */
public class EmailDefender {
    private JFrame mainFrame;
    private GameManager gameManager;
    
    public EmailDefender() {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        // Create the main application frame
        mainFrame = new JFrame("Email Defender");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null); // Center on screen
        
        // Initialize game components
        Player player = new Player();
        EmailSystem emailSystem = new EmailSystem();
        UpgradeSystem upgradeSystem = new UpgradeSystem();
        gameManager = new GameManager(player, emailSystem, upgradeSystem);
        
        // Create the main screen
        MainScreen mainScreen = new MainScreen(gameManager);
        
        // Set the main screen reference in game manager AFTER MainScreen is fully initialized
        gameManager.setMainScreen(mainScreen);
        
        // Add main screen to frame
        mainFrame.add(mainScreen);
        
        // Show the tutorial on first run
        showTutorial();
        
        // Display the frame
        mainFrame.setVisible(true);
        
        // Start the game loop
        gameManager.startGameLoop();
    }
    
    private void showTutorial() {
        JOptionPane.showMessageDialog(mainFrame,
            "Welcome to Email Defender!\n\n" +
            "- Respond to legitimate emails to earn points\n" +
            "- Delete spam emails to keep your inbox clean\n" +
            "- Manage your inbox space carefully\n" +
            "- Earn coins to purchase upgrades\n\n" +
            "Good luck defending your inbox!",
            "Tutorial",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Create and run the application on the EDT
        SwingUtilities.invokeLater(() -> {
            new EmailDefender();
        });
    }
}