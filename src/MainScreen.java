package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.image.BufferedImage;

/**
 * Main screen of the Email Defender game with Windows 95 styling.
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
        
        // Set layout with Windows 95 style
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Windows95Theme.WINDOW_BG);
        
        // Create UI components
        createHeader();
        createEmailList();
        createFooter();
        
        // NOTE: We delay the updateUI() call to avoid the NullPointerException
        // It will be called later by the GameManager after everything is initialized
    }
    
    /**
     * Create the header with status indicators in Windows 95 style
     */
    private void createHeader() {
        JPanel headerPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        headerPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        // Create status labels with Windows 95 styling
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        coinsLabel = new JLabel("Coins: 0");
        coinsLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        levelLabel = new JLabel("Level: 1");
        levelLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        // Create inbox capacity panel with Windows 95 styling
        JPanel inboxPanel = new JPanel(new BorderLayout(5, 0));
        inboxPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        inboxCapacityLabel = new JLabel("Inbox: 0/20");
        inboxCapacityLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        inboxCapacityBar = new JProgressBar(0, 20);
        inboxCapacityBar.setStringPainted(true);
        inboxCapacityBar.setBackground(Color.WHITE);
        inboxCapacityBar.setForeground(new Color(0, 0, 128)); // Windows 95 blue
        
        inboxPanel.add(inboxCapacityLabel, BorderLayout.NORTH);
        inboxPanel.add(inboxCapacityBar, BorderLayout.CENTER);
        
        // Add components to header panel
        headerPanel.add(createStatusPanel("Score", scoreLabel));
        headerPanel.add(createStatusPanel("Coins", coinsLabel));
        headerPanel.add(createStatusPanel("Level", levelLabel));
        headerPanel.add(inboxPanel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create a Windows 95 style status panel for a label
     */
    private JPanel createStatusPanel(String title, JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Windows95Theme.WINDOW_BG);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            Windows95Theme.SYSTEM_FONT
        ));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Create the email list area with Windows 95 styling
     */
    private void createEmailList() {
        emailListModel = new DefaultListModel<>();
        emailList = new JList<>(emailListModel);
        
        // Apply Windows 95 styling to list
        emailList.setBackground(Color.WHITE);
        emailList.setFont(Windows95Theme.SYSTEM_FONT);
        emailList.setBorder(Windows95Theme.createTextFieldBorder());
        
        // Custom cell renderer for emails with Windows 95 icons
        emailList.setCellRenderer(new Win95EmailListCellRenderer());
        
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
        
        // Create scroll pane with Windows 95 styling
        JScrollPane scrollPane = new JScrollPane(emailList);
        scrollPane.setBorder(Windows95Theme.createTitledBorder("Inbox"));
        scrollPane.setBackground(Windows95Theme.WINDOW_BG);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the footer with buttons in Windows 95 style
     */
    private void createFooter() {
        JPanel footerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        footerPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        // Create Windows 95 style buttons
        upgradeShopButton = Windows95Theme.createButton("Upgrade Shop");
        upgradeShopButton.addActionListener(e -> openUpgradeShopDialog());
        
        pauseResumeButton = Windows95Theme.createButton("Pause");
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
        // Get the parent window without trying to cast it to JDialog
        Window parent = SwingUtilities.getWindowAncestor(this);
        
        // Create the dialog with the correct parent window
        EmailViewDialog dialog = new EmailViewDialog(parent, email, gameManager);
        dialog.setVisible(true);
    }
    
    /**
     * Open the upgrade shop dialog
     */
    private void openUpgradeShopDialog() {
        // Get the parent window without trying to cast it to JDialog
        Window parent = SwingUtilities.getWindowAncestor(this);
        
        // Create the dialog with the correct parent window
        UpgradeShopDialog dialog = new UpgradeShopDialog(parent, gameManager);
        dialog.setVisible(true);
        
        // Update UI after shop dialog closes
        updateUI();
    }
    
    /**
     * Update the UI with current game state
     */
    @Override
    public void updateUI() {
        super.updateUI(); // Call the superclass method first
        
        if (gameManager == null) {
            return; // Skip the update if gameManager is not set yet
        }
        
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
        
        // Set color based on capacity - use Windows 95 color scheme
        double capacityPercentage = (double) currentSize / maxCapacity;
        if (capacityPercentage < 0.7) {
            inboxCapacityBar.setForeground(new Color(0, 0, 128)); // Windows 95 blue
        } else if (capacityPercentage < 0.9) {
            inboxCapacityBar.setForeground(new Color(128, 128, 0)); // Dark yellow
        } else {
            inboxCapacityBar.setForeground(new Color(128, 0, 0)); // Dark red
        }
        
        // Update email list
        updateEmailList();
        
        // Disable buttons if game is not running
        boolean gameRunning = gameManager.isGameRunning();
        upgradeShopButton.setEnabled(gameRunning);
        pauseResumeButton.setEnabled(gameRunning);
    }
    
    /**
     * Update the email list with current emails in inbox
     */
    private void updateEmailList() {
        if (gameManager == null) {
            return; // Skip the update if gameManager is not set yet
        }
        
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
     * Custom cell renderer for the email list with Windows 95 styling
     */
    private class Win95EmailListCellRenderer extends DefaultListCellRenderer {
        private ImageIcon mailIcon;
        private ImageIcon spamIcon;
        private ImageIcon urgentIcon;
        
        public Win95EmailListCellRenderer() {
            // Create icons that look like Windows 95 mail icons
            mailIcon = createMailIcon(Color.BLUE);
            spamIcon = createMailIcon(Color.ORANGE);
            urgentIcon = createMailIcon(Color.RED);
        }
        
        private ImageIcon createMailIcon(Color color) {
            // Create a simple mail icon that resembles Windows 95 style
            int width = 16;
            int height = 16;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Draw envelope
            g2d.setColor(Windows95Theme.BUTTON_FACE);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(color);
            g2d.drawRect(0, 0, width-1, height-1);
            g2d.drawLine(0, 0, width/2, height/2);
            g2d.drawLine(width-1, 0, width/2, height/2);
            
            g2d.dispose();
            return new ImageIcon(image);
        }
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Email) {
                Email email = (Email) value;
                setText(email.getDisplayString());
                setFont(Windows95Theme.SYSTEM_FONT);
                
                // Apply icon based on email type
                if (email.isUrgent()) {
                    setIcon(urgentIcon);
                } else if (email.isSpam()) {
                    setIcon(spamIcon);
                } else {
                    setIcon(mailIcon);
                }
                
                // Set Windows 95 style selection colors
                if (isSelected) {
                    setBackground(new Color(0, 0, 128)); // Windows 95 selection blue
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                
                // Set tooltip with email details
                setToolTipText("From: " + email.getSender() + 
                              " | Size: " + email.getSize() + 
                              " | Time: " + email.getTimeReceived().toLocalTime());
            }
            
            return this;
        }
    }
}