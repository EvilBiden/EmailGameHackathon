package src;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import java.awt.*;

/**
 * A utility class to apply Windows 95 styling to the application
 */
public class Windows95Theme {
    // Windows 95 color scheme
    public static final Color BACKGROUND_COLOR = new Color(192, 192, 192); // Light gray
    public static final Color WINDOW_BG = new Color(212, 208, 200);        // Window background
    public static final Color TEXT_COLOR = new Color(0, 0, 0);             // Black text
    public static final Color TITLE_BAR_COLOR = new Color(0, 0, 128);      // Dark blue title bar
    public static final Color TITLE_TEXT_COLOR = new Color(255, 255, 255); // White title text
    public static final Color BUTTON_FACE = new Color(192, 192, 192);      // Button gray
    public static final Color SHADOW_COLOR = new Color(128, 128, 128);     // Shadow gray
    public static final Color HIGHLIGHT_COLOR = new Color(255, 255, 255);  // Highlight white
    
    // Font for Windows 95 look
    public static final Font SYSTEM_FONT = new Font("Dialog", Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font("Dialog", Font.BOLD, 12);
    public static final Font WINDOW_TITLE_FONT = new Font("Dialog", Font.BOLD, 11);
    
    /**
     * Apply Windows 95 look and feel to the application
     */
    public static void apply() {
        try {
            // Set system properties to tweak the UI
            System.setProperty("swing.boldMetal", "false");
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            
            // Set the base look and feel to Windows
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Override with Windows 95 style customizations
            customizeUIDefaults();
        } catch (Exception e) {
            System.err.println("Failed to apply Windows 95 look and feel: " + e.getMessage());
        }
    }
    
    /**
     * Apply custom Windows 95 style UI defaults
     */
    private static void customizeUIDefaults() {
        UIDefaults defaults = UIManager.getDefaults();
        
        // Colors
        defaults.put("Panel.background", WINDOW_BG);
        defaults.put("Button.background", BUTTON_FACE);
        defaults.put("Button.foreground", TEXT_COLOR);
        defaults.put("OptionPane.background", WINDOW_BG);
        defaults.put("TextField.background", Color.WHITE);
        defaults.put("TextArea.background", Color.WHITE);
        defaults.put("Label.foreground", TEXT_COLOR);
        defaults.put("List.background", Color.WHITE);
        defaults.put("Table.background", Color.WHITE);
        defaults.put("TabbedPane.background", WINDOW_BG);
        defaults.put("ScrollPane.background", WINDOW_BG);
        defaults.put("ProgressBar.foreground", TITLE_BAR_COLOR);
        defaults.put("ProgressBar.background", Color.WHITE);
        
        // Fonts
        defaults.put("Button.font", SYSTEM_FONT);
        defaults.put("Label.font", SYSTEM_FONT);
        defaults.put("TextField.font", SYSTEM_FONT);
        defaults.put("TextArea.font", SYSTEM_FONT);
        defaults.put("TabbedPane.font", SYSTEM_FONT);
        defaults.put("Table.font", SYSTEM_FONT);
        defaults.put("TableHeader.font", SYSTEM_FONT);
        defaults.put("OptionPane.messageFont", SYSTEM_FONT);
        defaults.put("OptionPane.buttonFont", SYSTEM_FONT);
        defaults.put("Menu.font", SYSTEM_FONT);
        defaults.put("MenuItem.font", SYSTEM_FONT);
        
        // Borders
        defaults.put("Button.border", createBevelBorder());
        defaults.put("TextField.border", createTextFieldBorder());
        defaults.put("TextArea.border", createTextFieldBorder());
        
        // Title pane
        defaults.put("InternalFrame.activeTitleBackground", TITLE_BAR_COLOR);
        defaults.put("InternalFrame.activeTitleForeground", TITLE_TEXT_COLOR);
        defaults.put("InternalFrame.titleFont", WINDOW_TITLE_FONT);
    }
    
    /**
     * Create a Windows 95 style beveled border for buttons
     */
    public static Border createBevelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
            ),
            BorderFactory.createEmptyBorder(1, 1, 1, 1)
        );
    }
    
    /**
     * Create a Windows 95 style sunken border for text fields
     */
    public static Border createTextFieldBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        );
    }
    
    /**
     * Create a Windows 95 style titled border for panels
     */
    public static Border createTitledBorder(String title) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                title
            ),
            BorderFactory.createEmptyBorder(2, 5, 5, 5)
        );
    }
    
    /**
     * Create a Windows 95 style panel with 3D border
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(WINDOW_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return panel;
    }
    
    /**
     * Create a Windows 95 style button
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(SYSTEM_FONT);
        button.setBackground(BUTTON_FACE);
        button.setForeground(TEXT_COLOR);
        button.setBorder(createBevelBorder());
        button.setFocusPainted(false);
        return button;
    }
}