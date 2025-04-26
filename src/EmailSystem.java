package src;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages email generation and delivery.
 * Controls inbox capacity and tracks email status.
 */
public class EmailSystem {
    private List<Email> inbox;
    private int inboxCapacity;
    private Random random;
    
    // Email templates for generation
    private String[] workSenders = {"Boss", "Manager", "HR Dept", "IT Support", "Colleague"};
    private String[] workSubjects = {"Project Update", "Meeting Request", "Report Due", "System Update", "Task Assignment"};
    
    private String[] personalSenders = {"Friend", "Family", "Spouse", "School", "Doctor"};
    private String[] personalSubjects = {"Hello!", "Weekend Plans", "Important News", "Check-in", "Invitation"};
    
    private String[] spamSenders = {"Prize Dept", "Security Alert", "Account Service", "Lottery Win", "Unknown"};
    private String[] spamSubjects = {"You Won!", "Account Alert", "Urgent Action Required", "Special Offer", "Security Warning"};
    
    /**
     * Constructor for the email system
     */
    public EmailSystem() {
        inbox = new CopyOnWriteArrayList<>(); // Thread-safe list for concurrent modifications
        inboxCapacity = 3; // Default capacity from the spec
        random = new Random();
    }
    
    /**
     * Generate a new email based on the current game level
     */
    public Email generateEmail(int currentLevel) {
        // Determine email type based on level
        // Higher levels have more complex and spam emails
        Email.EmailType emailType = determineEmailType(currentLevel);
        
        // Generate appropriate content based on type
        String sender = "";
        String subject = "";
        String content = "";
        int size = 1;
        boolean urgent = random.nextDouble() < 0.1; // 10% chance of being urgent
        
        switch(emailType) {
            case LEGITIMATE_WORK:
                sender = workSenders[random.nextInt(workSenders.length)];
                subject = workSubjects[random.nextInt(workSubjects.length)];
                content = "This is a work-related email requiring your attention.";
                //size = random.nextInt(3) + 1; // Size 1-3
                size = 1;
                break;
                
            case LEGITIMATE_PERSONAL:
                sender = personalSenders[random.nextInt(personalSenders.length)];
                subject = personalSubjects[random.nextInt(personalSubjects.length)];
                content = "This is a personal email from " + sender + ".";
                //size = random.nextInt(2) + 1; // Size 1-2
                size = 1;
                break;
                
            case LEGITIMATE_SUBSCRIPTION:
                sender = "Newsletter";
                subject = "Your Weekly Update";
                content = "Thank you for subscribing to our newsletter.";
                size = 1;
                break;
                
            case LEGITIMATE_ACCOUNT:
                sender = "Account Services";
                subject = "Account Notification";
                content = "This is a notification about your account.";
                size = 1;
                break;
                
            case SPAM_PHISHING:
                sender = spamSenders[random.nextInt(spamSenders.length)];
                subject = spamSubjects[random.nextInt(spamSubjects.length)];
                content = "Please click this link to claim your prize!";
                //size = random.nextInt(2) + 1; // Size 1-2
                size = 1;
                break;
                
            case SPAM_PROMOTIONAL:
                sender = "Marketing";
                subject = "Special Offer Inside!";
                content = "Limited time offer! Buy now!";
                size = 1;
                break;
                
            case SPAM_SCAM:
                sender = "Prince";
                subject = "Business Proposal";
                content = "I need your help to transfer $10,000,000...";
                size = 1;
                break;
                
            case SPAM_MALWARE:
                sender = "System Admin";
                subject = "Security Update Required";
                content = "Please download this attachment immediately.";
                size = 1; // Malware tends to be larger
                break;
                
            case URGENT:
                sender = workSenders[random.nextInt(workSenders.length)];
                subject = "URGENT: " + workSubjects[random.nextInt(workSubjects.length)];
                content = "This requires your immediate attention!";
                //size = random.nextInt(3) + 1;
                size = 1;
                urgent = true; // Always urgent
                break;
                
            case CHAIN:
                sender = "Multiple Recipients";
                subject = "Re: Re: Re: Important Discussion";
                content = "This is part of an ongoing discussion thread.";
                //size = random.nextInt(4) + 2; // Size 2-5 (larger due to chain)
                size = 1;
                break;
                
            case LARGE_ATTACHMENT:
                sender = workSenders[random.nextInt(workSenders.length)];
                subject = "Files Attached";
                content = "I've attached the requested files.";
                //size = random.nextInt(5) + 5; // Size 5-9 (large attachment)
                size = 1;
                break;
        }
        
        return new Email(sender, subject, content, size, emailType, urgent);
    }
    
    /**
     * Determine which type of email to generate based on the current level
     */
    private Email.EmailType determineEmailType(int currentLevel) {
        double rand = random.nextDouble();
        
        // Adjust probabilities based on level
        // Higher levels have more spam and special events
        if (currentLevel <= 3) {
            // Early levels - mostly legitimate emails with little spam
            if (rand < 0.8) {
                // 80% chance of legitimate email
                double legitType = random.nextDouble();
                if (legitType < 0.4) return Email.EmailType.LEGITIMATE_WORK;
                else if (legitType < 0.7) return Email.EmailType.LEGITIMATE_PERSONAL;
                else if (legitType < 0.9) return Email.EmailType.LEGITIMATE_SUBSCRIPTION;
                else return Email.EmailType.LEGITIMATE_ACCOUNT;
            } else {
                // 20% chance of spam
                double spamType = random.nextDouble();
                if (spamType < 0.4) return Email.EmailType.SPAM_PROMOTIONAL;
                else if (spamType < 0.7) return Email.EmailType.SPAM_PHISHING;
                else if (spamType < 0.9) return Email.EmailType.SPAM_SCAM;
                else return Email.EmailType.SPAM_MALWARE;
            }
        } else if (currentLevel <= 6) {
            // Mid levels - more spam and some special events
            if (rand < 0.1) return Email.EmailType.URGENT;
            else if (rand < 0.2) return Email.EmailType.LARGE_ATTACHMENT;
            else if (rand < 0.5) {
                // 30% chance of spam
                double spamType = random.nextDouble();
                if (spamType < 0.3) return Email.EmailType.SPAM_PROMOTIONAL;
                else if (spamType < 0.6) return Email.EmailType.SPAM_PHISHING;
                else if (spamType < 0.8) return Email.EmailType.SPAM_SCAM;
                else return Email.EmailType.SPAM_MALWARE;
            } else {
                // 50% chance of legitimate email
                double legitType = random.nextDouble();
                if (legitType < 0.3) return Email.EmailType.LEGITIMATE_WORK;
                else if (legitType < 0.6) return Email.EmailType.LEGITIMATE_PERSONAL;
                else if (legitType < 0.8) return Email.EmailType.LEGITIMATE_SUBSCRIPTION;
                else return Email.EmailType.LEGITIMATE_ACCOUNT;
            }
        } else {
            // Higher levels - more complex distribution
            if (rand < 0.15) return Email.EmailType.URGENT;
            else if (rand < 0.25) return Email.EmailType.CHAIN;
            else if (rand < 0.35) return Email.EmailType.LARGE_ATTACHMENT;
            else if (rand < 0.6) {
                // 25% chance of spam
                double spamType = random.nextDouble();
                if (spamType < 0.25) return Email.EmailType.SPAM_PROMOTIONAL;
                else if (spamType < 0.5) return Email.EmailType.SPAM_PHISHING;
                else if (spamType < 0.75) return Email.EmailType.SPAM_SCAM;
                else return Email.EmailType.SPAM_MALWARE;
            } else {
                // 40% chance of legitimate email
                double legitType = random.nextDouble();
                if (legitType < 0.25) return Email.EmailType.LEGITIMATE_WORK;
                else if (legitType < 0.5) return Email.EmailType.LEGITIMATE_PERSONAL;
                else if (legitType < 0.75) return Email.EmailType.LEGITIMATE_SUBSCRIPTION;
                else return Email.EmailType.LEGITIMATE_ACCOUNT;
            }
        }
    }
    
    /**
     * Add an email to the inbox
     */
    public boolean addToInbox(Email email) {
        if (getCurrentInboxSize() + email.getSize() <= inboxCapacity) {
            inbox.add(email);
            return true;
        }
        return false; // Inbox full
    }
    
    /**
     * Remove an email from the inbox
     */
    public void removeFromInbox(Email email) {
        inbox.remove(email);
    }
    
    /**
     * Get the current size of all emails in the inbox
     */
    public int getCurrentInboxSize() {
        int totalSize = 0;
        for (Email email : inbox) {
            totalSize += email.getSize();
        }
        return totalSize;
    }
    
    /**
     * Check if inbox is at capacity
     */
    public boolean isInboxFull() {
        return getCurrentInboxSize() >= inboxCapacity;
    }
    
    /**
     * Get all emails in the inbox
     */
    public List<Email> getInbox() {
        return inbox;
    }
    
    /**
     * Get inbox capacity
     */
    public int getInboxCapacity() {
        return inboxCapacity;
    }
    
    /**
     * Set inbox capacity (for upgrades)
     */
    public void setInboxCapacity(int inboxCapacity) {
        this.inboxCapacity = inboxCapacity;
    }
    
    /**
     * Increase inbox capacity by the specified amount
     */
    public void increaseInboxCapacity(int amount) {
        inboxCapacity += amount;
    }
}