import javax.swing.UIManager;

/**
 * Main application class that starts the Disaster Response Resource Allocation application.
 */
public class ResourceAllocationApp {
    /**
     * Main method - entry point for the application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use the Event Dispatch Thread for Swing applications
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create and show the GUI
            DisasterResponseGUI gui = new DisasterResponseGUI();
            gui.setVisible(true);
            
            // Initialize with first district name
            gui.initializeForm();
        });
    }
}