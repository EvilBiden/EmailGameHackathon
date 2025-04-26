package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for viewing and interacting with individual emails.
 * Now includes a loading bar for email responses.
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
        
        // Set dialog properties
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create UI components
        createHeaderPanel();
        createContentPanel();
        createButtonPanel();
    }
    
    /**
     * Create the header panel with sender and subject
     */
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        senderLabel = new JLabel("From: " + email.getSender());
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD));
        
        subjectLabel = new JLabel("Subject: " + email.getSubject());
        
        headerPanel.add(senderLabel);
        headerPanel.add(subjectLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create the content panel with email body
     */
    private void createContentPanel() {
        contentArea = new JTextArea(email.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(new TitledBorder("Content"));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the button panel with actions
     */
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(0, 10));
        
        // Progress bar for reply
        replyProgressBar = new JProgressBar(0, 100);
        replyProgressBar.setStringPainted(true);
        replyProgressBar.setString("Reply Progress");
        replyProgressBar.setValue(0);
        replyProgressBar.setVisible(false);
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        progressPanel.add(replyProgressBar, BorderLayout.CENTER);
        
        buttonPanel.add(progressPanel, BorderLayout.NORTH);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        
        replyButton = new JButton("Reply");
        replyButton.addActionListener(e -> startReplyProcess());
        
        markSpamButton = new JButton("Mark as Spam");
        markSpamButton.addActionListener(e -> {
            gameManager.processEmailAction(email, "mark_spam");
            dispose();
        });
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            gameManager.processEmailAction(email, "delete");
            dispose();
        });
        
        cancelButton = new JButton("Close");
        cancelButton.addActionListener(e -> dispose());
        
        actionPanel.add(replyButton);
        actionPanel.add(markSpamButton);
        actionPanel.add(deleteButton);
        actionPanel.add(cancelButton);
        
        buttonPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Start the reply process with a progress bar
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