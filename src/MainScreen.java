package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main screen of the Email Defender game.
 * Shows the email list and game status.
 */
public class MainScreen extends JPanel {
    private GameManager gameManager;
    
    // UI Components
    private JList<Email> emailList;
    private DefaultListModel<Email> emailListModel;
    private JLabel scoreLabel;
    private JLabel coinsLabel;
    private JLabel levelLabel;
    private JLabel inboxCapacityLabel;
    private JProgressBar inboxCapacityBar;
    private JButton upgradeShopButton;
    private JButton pauseResumeButton;
    
    /**
     * Constructor for the main screen
     */
    public MainScreen(GameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.setMainScreen(this);
        
        // Set layout
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create UI components
        createHeader();
        createEmailList();
        createFooter();
        
        // Update UI with initial values
        updateUI();
    }
    
    /**
     * Create the header with status indicators
     */
    private void createHeader() {
        JPanel headerPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        scoreLabel = new JLabel("Score: 0");
        coinsLabel = new JLabel("Coins: 0");
        levelLabel = new JLabel("Level: 1");
        
        JPanel inboxPanel = new JPanel(new BorderLayout(5, 0));
        inboxCapacityLabel = new JLabel("Inbox: 0/20");
        inboxCapacityBar = new JProgressBar(0, 20);
        inboxCapacityBar.setStringPainted(true);
        inboxPanel.add(inboxCapacityLabel, BorderLayout.NORTH);
        inboxPanel.add(inboxCapacityBar, BorderLayout.CENTER);
        
        headerPanel.add(scoreLabel);
        headerPanel.add(coinsLabel);
        headerPanel.add(levelLabel);
        headerPanel.add(inboxPanel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create the email list area
     */
    private void createEmailList() {
        emailListModel = new DefaultListModel<>();
        emailList = new JList<>(emailListModel);
        
        // Custom cell renderer for emails
        emailList.setCellRenderer(new EmailListCellRenderer());
        
        // Add double-click handler
        emailList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Email selectedEmail = emailList.getSelectedValue();
                    if (selectedEmail != null) {
                        openEmailViewDialog(selectedEmail);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(emailList);
        scrollPane.setBorder(new TitledBorder("Inbox"));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the footer with buttons
     */
    private void createFooter() {
        JPanel footerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        upgradeShopButton = new JButton("Upgrade Shop");
        upgradeShopButton.addActionListener(e -> openUpgradeShopDialog());
        
        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.addActionListener(e -> togglePauseResume());
        
        footerPanel.add(upgradeShopButton);
        footerPanel.add(pauseResumeButton);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Toggle between pause and resume
     */
    private void togglePauseResume() {
        if (gameManager.isGamePaused()) {
            gameManager.resumeGame();
            pauseResumeButton.setText("Pause");
        } else {
            gameManager.pauseGame();
            pauseResumeButton.setText("Resume");
        }
    }
    
    /**
     * Open the email view dialog for a selected email
     */
    private void openEmailViewDialog(Email email) {
        EmailViewDialog dialog = new EmailViewDialog(SwingUtilities.getWindowAncestor(this), email, gameManager);
        dialog.setVisible(true);
    }
    
    /**
     * Open the upgrade shop dialog
     */
    private void openUpgradeShopDialog() {
        UpgradeShopDialog dialog = new UpgradeShopDialog(SwingUtilities.getWindowAncestor(this), gameManager);
        dialog.setVisible(true);
        
        // Update UI after shop dialog closes
        updateUI();
    }
    
    /**
     * Update the UI with current game state
     */
    public void updateUI() {
        // Update status indicators
        Player player = gameManager.getPlayer();
        EmailSystem emailSystem = gameManager.getEmailSystem();
        
        scoreLabel.setText("Score: " + player.getScore());
        coinsLabel.setText("Coins: " + player.getCoins());
        levelLabel.setText("Level: " + player.getLevel());
        
        // Update inbox capacity
        int currentSize = emailSystem.getCurrentInboxSize();
        int maxCapacity = emailSystem.getInboxCapacity();
        inboxCapacityLabel.setText("Inbox: " + currentSize + "/" + maxCapacity);
        inboxCapacityBar.setMaximum(maxCapacity);
        inboxCapacityBar.setValue(currentSize);
        
        // Set color based on capacity
        double capacityPercentage = (double) currentSize / maxCapacity;
        if (capacityPercentage < 0.7) {
            inboxCapacityBar.setForeground(new Color(0, 150, 0)); // Green
        } else if (capacityPercentage < 0.9) {
            inboxCapacityBar.setForeground(new Color(200, 150, 0)); // Yellow/Orange
        } else {
            inboxCapacityBar.setForeground(new Color(200, 0, 0)); // Red
        }
        
        // Update email list
        updateEmailList();
        
        // Disable buttons if game is not running
        boolean gameRunning = gameManager.isGameRunning();
        upgradeShopButton.setEnabled(gameRunning);
        pauseResumeButton.setEnabled(gameRunning);
        
        // Call superclass method to repaint
        super.updateUI();
    }
    
    /**
     * Update the email list with current emails in inbox
     */
    private void updateEmailList() {
        List<Email> inbox = gameManager.getEmailSystem().getInbox();
        
        // Remember selection
        Email selectedEmail = emailList.getSelectedValue();
        
        // Update list model
        emailListModel.clear();
        for (Email email : inbox) {
            emailListModel.addElement(email);
        }
        
        // Restore selection if possible
        if (selectedEmail != null && inbox.contains(selectedEmail)) {
            emailList.setSelectedValue(selectedEmail, true);
        }
    }
    
    /**
     * Custom cell renderer for the email list
     */
    private class EmailListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Email) {
                Email email = (Email) value;
                setText(email.getDisplayString());
                
                // Apply icon based on email type
                if (email.isUrgent()) {
                    setIcon(new ColorIcon(Color.RED, 16, 16));
                } else if (email.isSpam()) {
                    setIcon(new ColorIcon(Color.ORANGE, 16, 16));
                } else {
                    setIcon(new ColorIcon(Color.BLUE, 16, 16));
                }
                
                // Set tooltip with email details
                setToolTipText("From: " + email.getSender() + 
                              " | Size: " + email.getSize() + 
                              " | Time: " + email.getTimeReceived().toLocalTime());
            }
            
            return this;
        }
    }
    
    /**
     * Simple colored icon for the email list
     */
    private class ColorIcon implements Icon {
        private Color color;
        private int width;
        private int height;
        
        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x, y, width - 4, height - 4);
        }
        
        @Override
        public int getIconWidth() {
            return width;
        }
        
        @Override
        public int getIconHeight() {
            return height;
        }
    }
}
