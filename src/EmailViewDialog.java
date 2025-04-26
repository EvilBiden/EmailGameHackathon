package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for viewing and interacting with individual emails.
 * Now styled to look like Windows 95 with a loading bar for email responses.
 */
public class EmailViewDialog extends JDialog {
    private Email email;
    private GameManager gameManager;
    
    // UI Components
    private JLabel senderLabel;
    private JLabel subjectLabel;
    private JTextArea contentArea;
    private JButton replyButton;
    private JButton markSpamButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JProgressBar replyProgressBar;
    
    // Timer for the reply process
    private Timer replyTimer;
    private int replyProgress = 0;
    private int replyDuration = 0; // Will be set based on player's response speed
    
    /**
     * Constructor for the email view dialog
     */
    public EmailViewDialog(Window owner, Email email, GameManager gameManager) {
        super(owner, "Email", ModalityType.APPLICATION_MODAL);
        this.email = email;
        this.gameManager = gameManager;
        
        // Set dialog properties with Windows 95 styling
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setBackground(Windows95Theme.WINDOW_BG);
        
        // Create custom Windows 95 title bar
        JPanel titleBar = createTitleBar("Email: " + email.getSubject());
        
        // Create content panel with Windows 95 styling
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Windows95Theme.WINDOW_BG);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create UI components
        JPanel headerPanel = createHeaderPanel();
        JPanel centerPanel = createContentPanel();
        JPanel buttonPanel = createButtonPanel();
        
        // Add components to content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add components to dialog
        add(titleBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create a Windows 95 style title bar
     */
    private JPanel createTitleBar(String title) {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Windows95Theme.TITLE_BAR_COLOR);
        titleBar.setPreferredSize(new Dimension(500, 22));
        
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
     * Create the header panel with sender and subject in Windows 95 style
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(Windows95Theme.WINDOW_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        senderLabel = new JLabel("From: " + email.getSender());
        senderLabel.setFont(Windows95Theme.BOLD_FONT);
        
        subjectLabel = new JLabel("Subject: " + email.getSubject());
        subjectLabel.setFont(Windows95Theme.SYSTEM_FONT);
        
        headerPanel.add(senderLabel);
        headerPanel.add(subjectLabel);
        
        return headerPanel;
    }
    
    /**
     * Create the content panel with email body in Windows 95 style
     */
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        contentArea = new JTextArea(email.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setFont(Windows95Theme.SYSTEM_FONT);
        contentArea.setBorder(Windows95Theme.createTextFieldBorder());
        contentArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(Windows95Theme.createTitledBorder("Content"));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Create the button panel with actions in Windows 95 style
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(0, 10));
        buttonPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        // Progress bar for reply in Windows 95 style
        replyProgressBar = new JProgressBar(0, 100);
        replyProgressBar.setStringPainted(true);
        replyProgressBar.setString("Reply Progress");
        replyProgressBar.setValue(0);
        replyProgressBar.setVisible(false);
        replyProgressBar.setBackground(Color.WHITE);
        replyProgressBar.setForeground(Windows95Theme.TITLE_BAR_COLOR);
        replyProgressBar.setBorder(Windows95Theme.createTextFieldBorder());
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Windows95Theme.WINDOW_BG);
        progressPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        progressPanel.add(replyProgressBar, BorderLayout.CENTER);
        
        buttonPanel.add(progressPanel, BorderLayout.NORTH);
        
        // Action buttons in Windows 95 style
        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        actionPanel.setBackground(Windows95Theme.WINDOW_BG);
        
        replyButton = Windows95Theme.createButton("Reply");
        replyButton.addActionListener(e -> startReplyProcess());
        
        markSpamButton = Windows95Theme.createButton("Mark as Spam");
        markSpamButton.addActionListener(e -> {
            gameManager.processEmailAction(email, "mark_spam");
            dispose();
        });
        
        deleteButton = Windows95Theme.createButton("Delete");
        deleteButton.addActionListener(e -> {
            gameManager.processEmailAction(email, "delete");
            dispose();
        });
        
        cancelButton = Windows95Theme.createButton("Close");
        cancelButton.addActionListener(e -> dispose());
        
        actionPanel.add(replyButton);
        actionPanel.add(markSpamButton);
        actionPanel.add(deleteButton);
        actionPanel.add(cancelButton);
        
        buttonPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return buttonPanel;
    }
    
    /**
     * Start the reply process with a progress bar in Windows 95 style
     */
    private void startReplyProcess() {
        // Calculate reply duration based on player's response speed
        double responseSpeedModifier = gameManager.getPlayer().getResponseSpeedModifier();
        replyDuration = (int)(100 * responseSpeedModifier); // Base time (in timer ticks) * modifier
        
        // Set up the progress bar
        replyProgressBar.setValue(0);
        replyProgressBar.setVisible(true);
        replyProgress = 0;
        
        // Disable buttons during reply
        replyButton.setEnabled(false);
        markSpamButton.setEnabled(false);
        deleteButton.setEnabled(false);
        cancelButton.setEnabled(false);
        
        // Create and start the timer
        replyTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replyProgress++;
                replyProgressBar.setValue((int)(replyProgress * 100.0 / replyDuration));
                
                if (replyProgress >= replyDuration) {
                    // Reply completed
                    replyTimer.stop();
                    
                    // Show Windows 95 style completion message
                    JOptionPane.showMessageDialog(
                        EmailViewDialog.this,
                        "Email reply sent successfully.",
                        "Reply Complete",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    gameManager.processEmailAction(email, "respond");
                    dispose();
                }
            }
        });
        replyTimer.start();
    }
    
    /**
     * Override dispose to ensure timer is stopped when dialog closes
     */
    @Override
    public void dispose() {
        if (replyTimer != null && replyTimer.isRunning()) {
            replyTimer.stop();
        }
        super.dispose();
    }
}