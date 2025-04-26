package src;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

/**
 * Main class for the Email Defender game.
 * Initializes the game and starts the application with Windows 95 style.
 */
public class EmailDefender {
    private JFrame mainFrame;
    private GameManager gameManager;
    
    public EmailDefender() {
        // Apply Windows 95 theme
        Windows95Theme.apply();
        
        // Create the main application frame with Windows 95 styling
        mainFrame = new JFrame("Email Defender");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null); // Center on screen
        mainFrame.setBackground(Windows95Theme.WINDOW_BG);
        
        // Create custom title bar to mimic Windows 95
        JPanel titleBar = createTitleBar("Email Defender");
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Windows95Theme.WINDOW_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Initialize game components
        Player player = new Player();
        EmailSystem emailSystem = new EmailSystem();
        UpgradeSystem upgradeSystem = new UpgradeSystem();
        gameManager = new GameManager(player, emailSystem, upgradeSystem);
        
        // Create the main screen
        MainScreen mainScreen = new MainScreen(gameManager);
        
        // Add Windows 95 style to the main screen
        applyWindows95Style(mainScreen);
        
        // Set the main screen reference in game manager
        gameManager.setMainScreen(mainScreen);
        
        // Add components to content panel
        contentPanel.add(mainScreen, BorderLayout.CENTER);
        
        // Add components to frame
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(titleBar, BorderLayout.NORTH);
        mainFrame.add(contentPanel, BorderLayout.CENTER);
        
        // Create Windows 95 style menu bar
        JMenuBar menuBar = createMenuBar();
        mainFrame.setJMenuBar(menuBar);
        
        // Show the tutorial on first run
        showTutorial();
        
        // Display the frame
        mainFrame.setVisible(true);
        
        // Start the game loop
        gameManager.startGameLoop();
    }
    
    /**
     * Create a Windows 95 style title bar
     */
    private JPanel createTitleBar(String title) {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Windows95Theme.TITLE_BAR_COLOR);
        titleBar.setPreferredSize(new Dimension(800, 22));
        
        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setFont(Windows95Theme.WINDOW_TITLE_FONT);
        titleLabel.setForeground(Windows95Theme.TITLE_TEXT_COLOR);
        
        JPanel controlBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlBox.setBackground(Windows95Theme.TITLE_BAR_COLOR);
        
        JButton minimizeButton = createTitleBarButton("_");
        JButton maximizeButton = createTitleBarButton("□");
        JButton closeButton = createTitleBarButton("X");
        
        // Add action listener for close button
        closeButton.addActionListener(e -> System.exit(0));
        
        controlBox.add(minimizeButton);
        controlBox.add(maximizeButton);
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
     * Create a Windows 95 style menu bar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Windows95Theme.WINDOW_BG);
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(Windows95Theme.SYSTEM_FONT);
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(Windows95Theme.SYSTEM_FONT);
        
        JMenuItem tutorialItem = new JMenuItem("Tutorial");
        JMenuItem aboutItem = new JMenuItem("About");
        
        tutorialItem.addActionListener(e -> showTutorial());
        
        helpMenu.add(tutorialItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * Apply Windows 95 style to components in the MainScreen
     */
    private void applyWindows95Style(JPanel panel) {
        panel.setBackground(Windows95Theme.WINDOW_BG);
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Apply styles to all components recursively
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBackground(Windows95Theme.WINDOW_BG);
                if (comp.getClass().getSimpleName().equals("JPanel")) {
                    ((JPanel) comp).setBorder(BorderFactory.createRaisedBevelBorder());
                }
                applyWindows95Style((JPanel) comp);
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(Windows95Theme.BUTTON_FACE);
                button.setFont(Windows95Theme.SYSTEM_FONT);
                button.setBorder(Windows95Theme.createBevelBorder());
                button.setFocusPainted(false);
            } else if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(Windows95Theme.SYSTEM_FONT);
            } else if (comp instanceof JScrollPane) {
                ((JScrollPane) comp).setBorder(BorderFactory.createLoweredBevelBorder());
                if (((JScrollPane) comp).getViewport().getView() instanceof JList) {
                    JList<?> list = (JList<?>) ((JScrollPane) comp).getViewport().getView();
                    list.setBackground(Color.WHITE);
                    list.setForeground(Windows95Theme.TEXT_COLOR);
                    list.setFont(Windows95Theme.SYSTEM_FONT);
                }
            }
        }
    }
    
    /**
     * Show the tutorial dialog with Windows 95 style
     */
    private void showTutorial() {
        // Create a Windows 95 style option pane
        UIManager.put("OptionPane.background", Windows95Theme.WINDOW_BG);
        UIManager.put("Panel.background", Windows95Theme.WINDOW_BG);
        
        // Custom panel for message
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(Windows95Theme.WINDOW_BG);
        
        // Tutorial icon (folder icon)
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        iconLabel.setVerticalAlignment(JLabel.TOP);
        
        // Tutorial text with Windows 95 font
        JTextArea textArea = new JTextArea(
            "Welcome to Email Defender!\n\n" +
            "- Respond to legitimate emails to earn points\n" +
            "- Delete spam emails to keep your inbox clean\n" +
            "- Manage your inbox space carefully\n" +
            "- Earn coins to purchase upgrades\n\n" +
            "Good luck defending your inbox!"
        );
        textArea.setFont(Windows95Theme.SYSTEM_FONT);
        textArea.setBackground(Windows95Theme.WINDOW_BG);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(textArea, BorderLayout.CENTER);
        
        // Show dialog
        JOptionPane.showMessageDialog(
            mainFrame,
            panel,
            "Tutorial",
            JOptionPane.PLAIN_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        // Create and run the application on the EDT
        SwingUtilities.invokeLater(() -> {
            new EmailDefender();
        });
    }
}