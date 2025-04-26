package src;

import java.time.LocalDateTime;

/**
 * Represents an individual email in the game.
 * Contains all email properties and methods to handle email actions.
 */
public class Email {
    // Email types
    public enum EmailType {
        LEGITIMATE_WORK,
        LEGITIMATE_PERSONAL,
        LEGITIMATE_SUBSCRIPTION,
        LEGITIMATE_ACCOUNT,
        SPAM_PHISHING,
        SPAM_PROMOTIONAL,
        SPAM_SCAM,
        SPAM_MALWARE,
        URGENT,
        CHAIN,
        LARGE_ATTACHMENT
    }
    
    // Email properties
    private String sender;
    private String subject;
    private String content;
    private int size;
    private EmailType type;
    private boolean urgent;
    private LocalDateTime timeReceived;
    
    /**
     * Constructor for creating a new email
     */
    public Email(String sender, String subject, String content, int size, EmailType type, boolean urgent) {
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.size = size;
        this.type = type;
        this.urgent = urgent;
        this.timeReceived = LocalDateTime.now();
    }
    
    /**
     * Check if this email is spam based on its type
     */
    public boolean isSpam() {
        return type == EmailType.SPAM_PHISHING || 
               type == EmailType.SPAM_PROMOTIONAL || 
               type == EmailType.SPAM_SCAM || 
               type == EmailType.SPAM_MALWARE;
    }
    
    /**
     * Check if this email is legitimate based on its type
     */
    public boolean isLegitimate() {
        return !isSpam();
    }
    
    /**
     * Return a display string for the email in the inbox list
     */
    public String getDisplayString() {
        String urgentPrefix = urgent ? "[URGENT] " : "";
        return urgentPrefix + sender + " - " + subject;
    }
    
    // Getters and setters
    public String getSender() {
        return sender;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getContent() {
        return content;
    }
    
    public int getSize() {
        return size;
    }
    
    public EmailType getType() {
        return type;
    }
    
    public boolean isUrgent() {
        return urgent;
    }
    
    public LocalDateTime getTimeReceived() {
        return timeReceived;
    }
    
    /**
     * Get appropriate response options based on email type
     */
    public String[] getResponseOptions() {
        switch(type) {
            case LEGITIMATE_WORK:
                return new String[]{"Complete Task", "Schedule Meeting", "Request More Info"};
            case LEGITIMATE_PERSONAL:
                return new String[]{"Reply", "Thank", "Schedule"};
            case LEGITIMATE_SUBSCRIPTION:
                return new String[]{"Confirm", "Change Preferences"};
            case LEGITIMATE_ACCOUNT:
                return new String[]{"Acknowledge", "Update Details"};
            case URGENT:
                return new String[]{"Attend Immediately", "Delegate", "Schedule"};
            case CHAIN:
                return new String[]{"Forward", "Reply All", "Reply"};
            default:
                return new String[]{"Reply"};
        }
    }
}
