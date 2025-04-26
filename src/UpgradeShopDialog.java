package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for purchasing upgrades in the game.
 * Styled to look like a Windows 95 system component.
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
        
        // Set dialog properties with Windows 95 styling
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setBackground(Windows95Theme.WINDOW_BG);
        
        // Create custom Windows 95 title bar
        JPanel titleBar = createTitleBar("Upgrade Shop");
        
        // Create content panel with Windows 95 styling
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Windows95Theme.WINDOW_BG);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create UI components with Windows 95 styling
        JPanel headerPanel = createHeaderPanel();
        JPanel upgradesScrollPane = createUpgradesPanel();
        JPanel footerPanel = createFooterPanel();
        
        // Add components to content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(upgradesScrollPane, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Add components to dialog
        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Initial update
        updateCoinsLabel();
    }
    
    /**
     * Create a Windows 95 style title bar
     */
    private JPanel createTitleBar(String title) {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Windows95Theme.TITLE_BAR_COLOR);
        titleBar.setPreferredSize(new Dimension(600, 22));
        
        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setFont(Windows95Theme.WINDOW_TITLE_FONT);
        titleLabel.setForeground(Windows95Theme.TITLE_TEXT_COLOR);
        
        JPanel controlBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlBox.setBackground(Windows95Theme.TITLE_BAR_COLOR);
        
        JButton closeButton = createTitleBarButton("X");
        closeButton.addActionListener(e -> dispose());
        
        controlBox.add(closeButton);
        
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(controlBox, BorderLayout.EAST);
        
        return titleBar;
    }
    
    /**
     * Create a Windows 95 style title bar button
     */
    private JButton createTitleBarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Windows95Theme.BOLD_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(Windows95Theme.BUTTON_FACE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(22, 20));
        return button;
    }
    
    /**
     * Create the header panel with player coins in Windows 95 style
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Windows95Theme.WINDOW_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Create icon for shop (folder icon)
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        
        JLabel titleLabel = new JLabel("  System Upgrades");
        titleLabel.setFont(Windows95Theme.BOLD_FONT);
        
        coinsLabel = new JLabel();
        coinsLabel.setFont(Windows95Theme.SYSTEM_FONT);
        coinsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Windows95Theme.WINDOW_BG);
        titlePanel.add(iconLabel, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(coinsLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create the panel with all available upgrades in Windows 95 style
     */
    private JPanel createUpgradesPanel() {
        upgradesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        upgradesPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        // Get all upgrade types
        UpgradeType[] upgradeTypes = gameManager.getUpgradeSystem().getAllUpgradeTypes();
        
        // Create a panel for each upgrade
        for (UpgradeType type : upgradeTypes) {
            JPanel upgradePanel = createUpgradePanel(type);
            upgradesPanel.add(upgradePanel);
        }
        
        JScrollPane scrollPane = new JScrollPane(upgradesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Windows95Theme.WINDOW_BG);
        
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setBackground(Windows95Theme.WINDOW_BG);
        scrollWrapper.setBorder(Windows95Theme.createTitledBorder("Available Upgrades"));
        scrollWrapper.add(scrollPane, BorderLayout.CENTER);
        
        return scrollWrapper;
    }
    
    /**
     * Create a panel for an individual upgrade in Windows 95 style
     */
    private JPanel createUpgradePanel(UpgradeType type) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Windows95Theme.WINDOW_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Left side - upgrade info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        JLabel nameLabel = new JLabel(type.getName());
        nameLabel.setFont(Windows95Theme.BOLD_FONT);
        
        JLabel descriptionLabel = new JLabel(type.getDescription());
        descriptionLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        Player player = gameManager.getPlayer();
        int currentLevel = player.getUpgradeLevel(type);
        int cost = gameManager.getUpgradeSystem().getUpgradeCost(type, currentLevel);
        String effectDesc = gameManager.getUpgradeSystem().getUpgradeEffectDescription(type, currentLevel);
        
        JLabel statusLabel = new JLabel("Level: " + currentLevel + " | Effect: " + effectDesc);
        statusLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.add(statusLabel);
        
        // Right side - purchase button with Windows 95 styling
        JButton purchaseButton = Windows95Theme.createButton("Upgrade (" + cost + " coins)");
        
        // Disable button if max level or not enough coins
        boolean canPurchase = gameManager.getUpgradeSystem().canPurchaseUpgrade(player, type);
        purchaseButton.setEnabled(canPurchase);
        
        purchaseButton.addActionListener(e -> {
            boolean success = gameManager.purchaseUpgrade(type);
            if (success) {
                // Update UI after purchase
                updateUpgradePanel(panel, type);
                updateCoinsLabel();
                
                // Play Windows 95 style "ding" sound
                Toolkit.getDefaultToolkit().beep();
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
     * Create the footer panel with close button in Windows 95 style
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        JButton closeButton = Windows95Theme.createButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        footerPanel.add(closeButton);
        
        return footerPanel;
    }
    
    /**
     * Update the coins label with current player coins
     */
    private void updateCoinsLabel() {
        int coins = gameManager.getPlayer().getCoins();
        coinsLabel.setText("Available Coins: " + coins + "  ");
    }
}