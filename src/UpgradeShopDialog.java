package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for purchasing upgrades in the game.
 */
public class UpgradeShopDialog extends JDialog {
    private GameManager gameManager;
    
    // UI Components
    private JLabel coinsLabel;
    private JPanel upgradesPanel;
    
    /**
     * Constructor for the upgrade shop dialog
     */
    public UpgradeShopDialog(Window owner, GameManager gameManager) {
        super(owner, "Upgrade Shop", ModalityType.APPLICATION_MODAL);
        this.gameManager = gameManager;
        
        // Set dialog properties
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create UI components
        createHeaderPanel();
        createUpgradesPanel();
        createFooterPanel();
        
        // Initial update
        updateCoinsLabel();
    }
    
    /**
     * Create the header panel with player coins
     */
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Upgrade Shop");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        
        coinsLabel = new JLabel();
        coinsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(coinsLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create the panel with all available upgrades
     */
    private void createUpgradesPanel() {
        upgradesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        
        // Get all upgrade types
        UpgradeType[] upgradeTypes = gameManager.getUpgradeSystem().getAllUpgradeTypes();
        
        // Create a panel for each upgrade
        for (UpgradeType type : upgradeTypes) {
            JPanel upgradePanel = createUpgradePanel(type);
            upgradesPanel.add(upgradePanel);
        }
        
        JScrollPane scrollPane = new JScrollPane(upgradesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create a panel for an individual upgrade
     */
    private JPanel createUpgradePanel(UpgradeType type) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new EtchedBorder(),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Left side - upgrade info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        
        JLabel nameLabel = new JLabel(type.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
        
        JLabel descriptionLabel = new JLabel(type.getDescription());
        
        Player player = gameManager.getPlayer();
        int currentLevel = player.getUpgradeLevel(type);
        int cost = gameManager.getUpgradeSystem().getUpgradeCost(type, currentLevel);
        String effectDesc = gameManager.getUpgradeSystem().getUpgradeEffectDescription(type, currentLevel);
        
        JLabel statusLabel = new JLabel("Level: " + currentLevel + " | Effect: " + effectDesc);
        
        infoPanel.add(nameLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.add(statusLabel);
        
        // Right side - purchase button
        JButton purchaseButton = new JButton("Upgrade (" + cost + " coins)");
        purchaseButton.setPreferredSize(new Dimension(150, 30));
        
        // Disable button if max level or not enough coins
        boolean canPurchase = gameManager.getUpgradeSystem().canPurchaseUpgrade(player, type);
        purchaseButton.setEnabled(canPurchase);
        
        purchaseButton.addActionListener(e -> {
            boolean success = gameManager.purchaseUpgrade(type);
            if (success) {
                // Update UI after purchase
                updateUpgradePanel(panel, type);
                updateCoinsLabel();
            }
        });
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(purchaseButton, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Update an upgrade panel with new information
     */
    private void updateUpgradePanel(JPanel panel, UpgradeType type) {
        // Remove all components
        panel.removeAll();
        
        // Create updated panel
        JPanel updatedPanel = createUpgradePanel(type);
        
        // Copy components to original panel
        panel.setLayout(updatedPanel.getLayout());
        for (Component component : updatedPanel.getComponents()) {
            panel.add(component, ((BorderLayout) updatedPanel.getLayout()).getConstraints(component));
        }
        
        // Repaint
        panel.revalidate();
        panel.repaint();
    }
    
    /**
     * Create the footer panel with close button
     */
    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        footerPanel.add(closeButton);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Update the coins label with current player coins
     */
    private void updateCoinsLabel() {
        int coins = gameManager.getPlayer().getCoins();
        coinsLabel.setText("Coins: " + coins);
    }
}