package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for viewing and interacting with individual emails.
 */
public class EmailViewDialog extends JDialog {
    private Email email;
    private GameManager gameManager;
    
    // UI Components
    private JLabel senderLabel;
    private JLabel subjectLabel;
    private JTextArea contentArea;
    private JPanel responsePanel;
    
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
        
        // Response options
        responsePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        responsePanel.setBorder(new TitledBorder("Response Options"));
        
        if (email.isLegitimate()) {
            // Add response options for legitimate emails
            String[] options = email.getResponseOptions();
            for (String option : options) {
                JButton responseButton = new JButton(option);
                responseButton.addActionListener(e -> {
                    gameManager.processEmailAction(email, "respond");
                    dispose();
                });
                responsePanel.add(responseButton);
            }
        }
        
        buttonPanel.add(responsePanel, BorderLayout.CENTER);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            gameManager.processEmailAction(email, "delete");
            dispose();
        });
        
        JButton markSpamButton = new JButton("Mark as Spam");
        markSpamButton.addActionListener(e -> {
            if (email.isSpam()) {
                gameManager.processEmailAction(email, "delete");
            } else {
                gameManager.processEmailAction(email, "mark_spam");
            }
            dispose();
        });
        
        JButton cancelButton = new JButton("Close");
        cancelButton.addActionListener(e -> dispose());
        
        actionPanel.add(deleteButton);
        actionPanel.add(markSpamButton);
        actionPanel.add(cancelButton);
        
        buttonPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
}