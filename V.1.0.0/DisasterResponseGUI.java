import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Main GUI class for the Disaster Response Resource Allocation application.
 * Provides interface for entering district information and viewing results.
 */
public class DisasterResponseGUI extends JFrame {
    // Model
    private List<District> districts = new ArrayList<>();
    private int totalResources = 0;
    
    // Components for the main panel
    private JTextField totalResourcesField;
    private JButton calculateButton;
    private JButton addDistrictButton;
    private JPanel districtListPanel;
    private JPanel resultsPanel;
    
    // Components for district form
    private JTextField districtNameField;
    private JTextField populationField;
    private JComboBox<String> landTypeCombo;
    private JComboBox<String> urbanizationCombo;
    private JTextField resourceDemandField;
    
    // District counter for labeling
    private int districtCounter = 1;
    
    // Constants for layout
    private static final int PADDING = 15;
    private static final int FIELD_WIDTH = 180;
    private static final String[] LAND_TYPES = {"Forest", "Coastal", "Desert", "Urban"};
    private static final String[] URBANIZATION_TYPES = {"Rural", "Suburban", "Urban"};
    
    // Modern UI Colors
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210); // Material blue
    private static final Color SECONDARY_COLOR = new Color(66, 165, 245); // Lighter blue
    private static final Color ACCENT_COLOR = new Color(255, 193, 7); // Amber
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color CARD_COLOR = new Color(255, 255, 255); // White
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    
    /**
     * Constructor - sets up the main UI components
     */
    public DisasterResponseGUI() {
        // Setup main frame
        setTitle("Disaster Response Resource Allocation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        try {
            // Set modern cross-platform look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Customize UI colors
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("TextField.background", CARD_COLOR);
            UIManager.put("TextField.foreground", TEXT_COLOR);
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("TabbedPane.selected", SECONDARY_COLOR);
            
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main content panel with padding
        JPanel contentPanel = new JPanel(new BorderLayout(PADDING, PADDING));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Add header with modern styling
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel headerLabel = new JLabel("Disaster Response Resource Allocation System", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create input panel (left side)
        JPanel inputPanel = createInputPanel();
        
        // Create results panel (right side)
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(CARD_COLOR);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Create a split pane to divide input and results
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, 
                inputPanel, 
                resultsPanel);
        splitPane.setDividerLocation(550);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        // Add to frame
        add(contentPanel);
    }
    
    /**
     * Initialize the form with default values
     */
    public void initializeForm() {
        districtNameField.setText("District " + districtCounter);
    }
    
    /**
     * Creates the input panel with district form and controls
     */
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(PADDING, PADDING));
        inputPanel.setBackground(BACKGROUND_COLOR);
        
        // Resources panel at top
        JPanel resourcesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        resourcesPanel.setBackground(CARD_COLOR);
        resourcesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Total Available Resources");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel totalResourcesLabel = new JLabel("Enter total resources:");
        totalResourcesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        totalResourcesField = new JTextField(10);
        totalResourcesField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        calculateButton = new JButton("Calculate Allocation");
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calculateButton.setBackground(PRIMARY_COLOR);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(e -> calculateAllocation());
        
        JPanel resourcesContentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resourcesContentPanel.setBackground(CARD_COLOR);
        resourcesContentPanel.add(totalResourcesLabel);
        resourcesContentPanel.add(totalResourcesField);
        resourcesContentPanel.add(calculateButton);
        
        resourcesPanel.setLayout(new BorderLayout());
        resourcesPanel.add(titleLabel, BorderLayout.NORTH);
        resourcesPanel.add(resourcesContentPanel, BorderLayout.CENTER);
        
        // District form panel
        JPanel districtFormPanel = new JPanel(new BorderLayout());
        districtFormPanel.setBackground(CARD_COLOR);
        districtFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel formTitleLabel = new JLabel("Add New District");
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        formFieldsPanel.setBackground(CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // District name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("District Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        districtNameField = new JTextField(15);
        districtNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(districtNameField, gbc);
        
        // Population
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel popLabel = new JLabel("Population:");
        popLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(popLabel, gbc);
        
        gbc.gridx = 1;
        populationField = new JTextField(15);
        populationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(populationField, gbc);
        
        // Land Type
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel landLabel = new JLabel("Land Type:");
        landLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(landLabel, gbc);
        
        gbc.gridx = 1;
        landTypeCombo = new JComboBox<>(LAND_TYPES);
        landTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        landTypeCombo.setBackground(Color.WHITE);
        formFieldsPanel.add(landTypeCombo, gbc);
        
        // Urbanization
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel urbanLabel = new JLabel("Urbanization:");
        urbanLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(urbanLabel, gbc);
        
        gbc.gridx = 1;
        urbanizationCombo = new JComboBox<>(URBANIZATION_TYPES);
        urbanizationCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        urbanizationCombo.setBackground(Color.WHITE);
        formFieldsPanel.add(urbanizationCombo, gbc);
        
        // Resource demand
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel demandLabel = new JLabel("Resource Demand:");
        demandLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(demandLabel, gbc);
        
        gbc.gridx = 1;
        resourceDemandField = new JTextField(15);
        resourceDemandField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formFieldsPanel.add(resourceDemandField, gbc);
        
        // Add district button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 8, 8, 8);  // Extra padding on top
        
        addDistrictButton = new JButton("Add District");
        addDistrictButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addDistrictButton.setBackground(PRIMARY_COLOR);
        addDistrictButton.setForeground(Color.WHITE);
        addDistrictButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addDistrictButton.setFocusPainted(false);
        addDistrictButton.addActionListener(e -> addDistrict());
        
        formFieldsPanel.add(addDistrictButton, gbc);
        
        // Add title and form fields to the district form panel
        JPanel formContentPanel = new JPanel(new BorderLayout(0, 15));
        formContentPanel.setBackground(CARD_COLOR);
        formContentPanel.add(formTitleLabel, BorderLayout.NORTH);
        formContentPanel.add(formFieldsPanel, BorderLayout.CENTER);
        
        districtFormPanel.add(formContentPanel, BorderLayout.NORTH);
        
        // District list panel (scrollable)
        districtListPanel = new JPanel();
        districtListPanel.setBackground(BACKGROUND_COLOR);
        districtListPanel.setLayout(new BoxLayout(districtListPanel, BoxLayout.Y_AXIS));
        
        JScrollPane districtScrollPane = new JScrollPane(districtListPanel);
        districtScrollPane.setBackground(CARD_COLOR);
        districtScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel listTitleLabel = new JLabel("Added Districts");
        listTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel listPanel = new JPanel(new BorderLayout(0, 10));
        listPanel.setBackground(CARD_COLOR);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listPanel.add(listTitleLabel, BorderLayout.NORTH);
        listPanel.add(districtScrollPane, BorderLayout.CENTER);
        
        // Set preferred size for the scrollable list
        districtScrollPane.setPreferredSize(new Dimension(200, 200));
        
        // Combine all panels
        JPanel combinedPanel = new JPanel(new BorderLayout(PADDING, PADDING));
        combinedPanel.setBackground(BACKGROUND_COLOR);
        combinedPanel.add(resourcesPanel, BorderLayout.NORTH);
        combinedPanel.add(districtFormPanel, BorderLayout.CENTER);
        combinedPanel.add(listPanel, BorderLayout.SOUTH);
        
        inputPanel.add(combinedPanel, BorderLayout.NORTH);
        
        return inputPanel;
    }
    
    /**
     * Validates and adds a new district based on form input
     */
    private void addDistrict() {
        // Validate inputs
        String name = districtNameField.getText().trim();
        if (name.isEmpty()) {
            showError("District name cannot be empty");
            return;
        }
        
        int population;
        try {
            population = Integer.parseInt(populationField.getText().trim());
            if (population <= 0) {
                showError("Population must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Population must be a valid number");
            return;
        }
        
        String landType = (String) landTypeCombo.getSelectedItem();
        String urbanization = (String) urbanizationCombo.getSelectedItem();
        
        int resourceDemand;
        try {
            resourceDemand = Integer.parseInt(resourceDemandField.getText().trim());
            if (resourceDemand <= 0) {
                showError("Resource demand must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Resource demand must be a valid number");
            return;
        }
        
        // Create district and add to list
        District district = new District(name, population, landType, urbanization, resourceDemand);
        districts.add(district);
        
        // Add district to display panel
        JPanel districtPanel = createDistrictListItem(district, districts.size() - 1);
        districtListPanel.add(districtPanel);
        districtListPanel.revalidate();
        districtListPanel.repaint();
        
        // Clear form for next entry
        clearDistrictForm();
        
        // Increment counter for next district
        districtCounter++;
    }
    
    /**
     * Creates a panel representing a district in the list
     */
    private JPanel createDistrictListItem(District district, int index) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SECONDARY_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                )
        ));
        
        // Create a more modern and colorful indicator for risk level
        JPanel riskIndicator = new JPanel();
        riskIndicator.setPreferredSize(new Dimension(8, 0));
        
        // Color based on risk level
        int risk = district.getRiskScore();
        if (risk > 80) {
            riskIndicator.setBackground(new Color(220, 53, 69)); // Red for high risk
        } else if (risk > 50) {
            riskIndicator.setBackground(new Color(255, 193, 7)); // Yellow for medium risk
        } else {
            riskIndicator.setBackground(new Color(40, 167, 69)); // Green for low risk
        }
        
        // Format the district details with better HTML styling
        String details = String.format(
                "<html>" +
                "<div style='font-family: Segoe UI; margin-bottom: 3px;'>" +
                "<span style='font-size: 14px; font-weight: bold;'>%s</span>" +
                "</div>" +
                "<div style='font-family: Segoe UI; font-size: 12px; color: #555;'>" +
                "Population: <b>%d</b> • Type: <b>%s</b> • Zone: <b>%s</b>" +
                "</div>" +
                "<div style='font-family: Segoe UI; font-size: 12px; margin-top: 3px;'>" +
                "Resource Demand: <b>%d</b> • Risk Score: <b><span style='color: %s;'>%d</span></b>" +
                "</div>" +
                "</html>", 
                district.getName(), 
                district.getPopulation(), 
                district.getLandType(), 
                district.getUrbanization(),
                district.getResourceDemand(), 
                risk > 80 ? "#dc3545" : risk > 50 ? "#ffc107" : "#28a745",
                risk);
        
        JLabel label = new JLabel(details);
        
        // Create a styled remove button
        JButton removeButton = new JButton("×");
        removeButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        removeButton.setForeground(new Color(220, 53, 69));
        removeButton.setBackground(Color.WHITE);
        removeButton.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        removeButton.setFocusPainted(false);
        removeButton.setToolTipText("Remove this district");
        removeButton.addActionListener(e -> removeDistrict(panel, index));
        
        // Add components to panel
        panel.add(riskIndicator, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Removes a district from the list
     */
    private void removeDistrict(JPanel panel, int index) {
        districts.remove(index);
        districtListPanel.remove(panel);
        districtListPanel.revalidate();
        districtListPanel.repaint();
        
        // Rebuild the district list to update indexes
        rebuildDistrictList();
    }
    
    /**
     * Rebuilds the district list panel after removal of an item
     */
    private void rebuildDistrictList() {
        districtListPanel.removeAll();
        for (int i = 0; i < districts.size(); i++) {
            JPanel districtPanel = createDistrictListItem(districts.get(i), i);
            districtListPanel.add(districtPanel);
        }
        districtListPanel.revalidate();
        districtListPanel.repaint();
    }
    
    /**
     * Clears the district form fields
     */
    private void clearDistrictForm() {
        districtNameField.setText("District " + districtCounter);
        populationField.setText("");
        landTypeCombo.setSelectedIndex(0);
        urbanizationCombo.setSelectedIndex(0);
        resourceDemandField.setText("");
    }
    
    /**
     * Performs the allocation calculation and updates the results panel
     */
    private void calculateAllocation() {
        if (districts.isEmpty()) {
            showError("Please add at least one district");
            return;
        }
        
        try {
            totalResources = Integer.parseInt(totalResourcesField.getText().trim());
            if (totalResources <= 0) {
                showError("Total resources must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Total resources must be a valid number");
            return;
        }
        
        // Sort districts by risk-to-resource ratio (descending)
        List<District> sortedDistricts = new ArrayList<>(districts);
        Collections.sort(sortedDistricts, (d1, d2) -> {
            double ratio1 = d1.getRiskResourceRatio();
            double ratio2 = d2.getRiskResourceRatio();
            return Double.compare(ratio2, ratio1);
        });
        
        // Calculate resource allocation
        int remainingResources = totalResources;
        List<AllocationResult> allocationResults = new ArrayList<>();
        
        for (District district : sortedDistricts) {
            int allocated;
            boolean isPartial = false;
            
            if (remainingResources >= district.getResourceDemand()) {
                allocated = district.getResourceDemand();
                remainingResources -= allocated;
            } else {
                allocated = remainingResources;
                remainingResources = 0;
                isPartial = true;
            }
            
            allocationResults.add(new AllocationResult(district, allocated, isPartial));
            
            if (remainingResources == 0) {
                break;
            }
        }
        
        // Update results panel with allocation information
        updateResultsPanel(sortedDistricts, allocationResults, remainingResources);
    }
    
    /**
     * Updates the results panel with allocation information
     */
    private void updateResultsPanel(List<District> sortedDistricts, 
            List<AllocationResult> allocationResults, int remainingResources) {
        
        resultsPanel.removeAll();
        
        // Create modern tabbed pane for results
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        
        // Risk scores tab
        JPanel riskScorePanel = new JPanel(new BorderLayout());
        riskScorePanel.setBackground(CARD_COLOR);
        riskScorePanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        String[] riskColumns = {"District", "Population", "Land Type", "Urbanization", "Risk Score", "Risk/Resource Ratio"};
        
        Object[][] riskData = new Object[sortedDistricts.size()][riskColumns.length];
        for (int i = 0; i < sortedDistricts.size(); i++) {
            District d = sortedDistricts.get(i);
            DecimalFormat df = new DecimalFormat("#.##");
            riskData[i] = new Object[]{
                d.getName(), 
                d.getPopulation(), 
                d.getLandType(), 
                d.getUrbanization(), 
                d.getRiskScore(),
                df.format(d.getRiskResourceRatio())
            };
        }
        
        // Create and style the risk table
        JTable riskTable = new JTable(new DefaultTableModel(riskData, riskColumns));
        styleTable(riskTable);
        
        JScrollPane riskScrollPane = new JScrollPane(riskTable);
        riskScrollPane.setBorder(BorderFactory.createEmptyBorder());
        riskScrollPane.getViewport().setBackground(CARD_COLOR);
        
        // Add a title to the risk panel
        JLabel riskTitleLabel = new JLabel("Risk Assessment by District");
        riskTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        riskTitleLabel.setForeground(PRIMARY_COLOR);
        riskTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel riskHeaderPanel = new JPanel(new BorderLayout());
        riskHeaderPanel.setBackground(CARD_COLOR);
        riskHeaderPanel.add(riskTitleLabel, BorderLayout.WEST);
        
        riskScorePanel.add(riskHeaderPanel, BorderLayout.NORTH);
        riskScorePanel.add(riskScrollPane, BorderLayout.CENTER);
        
        // Allocation tab with modern styling
        JPanel allocationPanel = new JPanel(new BorderLayout());
        allocationPanel.setBackground(CARD_COLOR);
        allocationPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        String[] allocationColumns = {"District", "Risk Score", "Resource Demand", "Allocated Resources", "Status"};
        
        Object[][] allocationData = new Object[allocationResults.size()][allocationColumns.length];
        for (int i = 0; i < allocationResults.size(); i++) {
            AllocationResult result = allocationResults.get(i);
            District d = result.district;
            allocationData[i] = new Object[]{
                d.getName(),
                d.getRiskScore(),
                d.getResourceDemand(),
                result.allocatedResources,
                result.isPartial ? "Partial" : "Full"
            };
        }
        
        // Create and style the allocation table
        JTable allocationTable = new JTable(new DefaultTableModel(allocationData, allocationColumns)) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                
                // Color the status cell based on allocation status
                if (column == 4) {
                    String value = getValueAt(row, column).toString();
                    if (value.equals("Full")) {
                        comp.setForeground(new Color(40, 167, 69)); // Green for full allocation
                    } else {
                        comp.setForeground(new Color(255, 193, 7)); // Yellow for partial allocation
                    }
                    comp.setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    comp.setForeground(TEXT_COLOR);
                }
                
                return comp;
            }
        };
        
        styleTable(allocationTable);
        
        JScrollPane allocationScrollPane = new JScrollPane(allocationTable);
        allocationScrollPane.setBorder(BorderFactory.createEmptyBorder());
        allocationScrollPane.getViewport().setBackground(CARD_COLOR);
        
        // Add a title to the allocation panel
        JLabel allocationTitleLabel = new JLabel("Resource Allocation Results");
        allocationTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        allocationTitleLabel.setForeground(PRIMARY_COLOR);
        allocationTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel allocationHeaderPanel = new JPanel(new BorderLayout());
        allocationHeaderPanel.setBackground(CARD_COLOR);
        allocationHeaderPanel.add(allocationTitleLabel, BorderLayout.WEST);
        
        allocationPanel.add(allocationHeaderPanel, BorderLayout.NORTH);
        allocationPanel.add(allocationScrollPane, BorderLayout.CENTER);
        
        // Modern summary panel with stats and progress bars
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        summaryPanel.setBackground(CARD_COLOR);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        ));
        
        int totalAllocated = totalResources - remainingResources;
        double allocatedPercentage = (double) totalAllocated / totalResources * 100;
        
        // Create styled summary labels
        JLabel totalLabel = new JLabel("Total Resources: " + totalResources);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(PRIMARY_COLOR);
        
        // Allocated resources with progress bar
        JPanel allocatedPanel = new JPanel(new BorderLayout(10, 0));
        allocatedPanel.setBackground(CARD_COLOR);
        
        JLabel allocatedLabel = new JLabel("Allocated: " + totalAllocated + " (" + String.format("%.1f", allocatedPercentage) + "%)");
        allocatedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JProgressBar allocatedProgress = new JProgressBar(0, 100);
        allocatedProgress.setValue((int) allocatedPercentage);
        allocatedProgress.setStringPainted(false);
        allocatedProgress.setForeground(PRIMARY_COLOR);
        allocatedProgress.setBackground(new Color(230, 230, 230));
        
        allocatedPanel.add(allocatedLabel, BorderLayout.WEST);
        allocatedPanel.add(allocatedProgress, BorderLayout.CENTER);
        
        // Remaining resources with colored indicator
        JPanel remainingPanel = new JPanel(new BorderLayout(10, 0));
        remainingPanel.setBackground(CARD_COLOR);
        
        JLabel remainingLabel = new JLabel("Remaining: " + remainingResources);
        remainingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // If no resources remain, use red text to indicate
        if (remainingResources == 0) {
            remainingLabel.setForeground(new Color(220, 53, 69));
            remainingLabel.setText("Remaining: 0 (All resources allocated)");
        }
        
        remainingPanel.add(remainingLabel, BorderLayout.WEST);
        
        summaryPanel.add(totalLabel);
        summaryPanel.add(allocatedPanel);
        summaryPanel.add(remainingPanel);
        
        // Add visualization panel
        JPanel visualizationPanel = createVisualizationPanel(allocationResults, remainingResources);
        
        // Add all panels to tabbed pane
        tabbedPane.addTab("Risk Scores", riskScorePanel);
        tabbedPane.addTab("Allocation Results", allocationPanel);
        tabbedPane.addTab("Visualization", visualizationPanel);
        
        // Add components to results panel
        resultsPanel.add(tabbedPane, BorderLayout.CENTER);
        resultsPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    /**
     * Creates a visualization panel showing allocation as a bar chart
     */
    private JPanel createVisualizationPanel(List<AllocationResult> results, int remainingResources) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        // Add a title to the visualization panel
        JLabel visualizationTitleLabel = new JLabel("Resource Allocation Visualization");
        visualizationTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        visualizationTitleLabel.setForeground(PRIMARY_COLOR);
        visualizationTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int barWidth = Math.min(60, width / (results.size() + 2));
                int maxBarHeight = height - 100;
                int baseline = height - 50;
                int barSpacing = 20;
                int chartStartX = 70;
                
                // Set background
                g2d.setColor(CARD_COLOR);
                g2d.fillRect(0, 0, width, height);
                
                // Find maximum resource demand for scaling
                int maxDemand = 1; // Avoid division by zero
                for (AllocationResult result : results) {
                    maxDemand = Math.max(maxDemand, result.district.getResourceDemand());
                }
                
                // Draw axes with more subtle coloring
                g2d.setColor(new Color(180, 180, 180));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(chartStartX, baseline, width - 50, baseline); // X-axis
                g2d.drawLine(chartStartX, baseline, chartStartX, 50); // Y-axis
                
                // Draw y-axis labels (demand scale)
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                for (int i = 0; i <= 5; i++) {
                    int value = maxDemand * i / 5;
                    int y = baseline - (maxBarHeight * i / 5);
                    g2d.drawLine(chartStartX - 3, y, chartStartX, y); // Tick mark
                    g2d.drawString(String.valueOf(value), chartStartX - 30, y + 4);
                    
                    // Draw light horizontal grid lines
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.drawLine(chartStartX + 1, y, width - 50, y);
                    g2d.setColor(new Color(180, 180, 180));
                }
                
                // Draw bars with rounded corners and gradients
                int x = chartStartX + 30;
                for (AllocationResult result : results) {
                    District district = result.district;
                    
                    // Calculate bar heights
                    int demandHeight = (int)((double)district.getResourceDemand() / maxDemand * maxBarHeight);
                    int allocatedHeight = (int)((double)result.allocatedResources / maxDemand * maxBarHeight);
                    
                    // Draw allocation percentage
                    double percentage = (double)result.allocatedResources / district.getResourceDemand() * 100;
                    String percentText = String.format("%.0f%%", percentage);
                    
                    // Draw demand bar (outline with light fill)
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.fillRoundRect(x, baseline - demandHeight, barWidth, demandHeight, 8, 8);
                    g2d.setColor(new Color(200, 200, 200));
                    g2d.drawRoundRect(x, baseline - demandHeight, barWidth, demandHeight, 8, 8);
                    
                    // Draw allocated bar (filled with gradient)
                    Color barColor;
                    if (result.isPartial) {
                        // Partial allocation - orange
                        barColor = new Color(255, 193, 7);
                    } else {
                        // Full allocation - green
                        barColor = new Color(40, 167, 69);
                    }
                    
                    // Create gradient for allocated bar
                    GradientPaint gradient = new GradientPaint(
                            x, baseline, barColor,
                            x + barWidth, baseline, barColor.brighter());
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(x, baseline - allocatedHeight, barWidth, allocatedHeight, 8, 8);
                    
                    // Add allocation percentage on top of bar
                    g2d.setColor(TEXT_COLOR);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(percentText);
                    g2d.drawString(percentText, x + (barWidth - textWidth) / 2, baseline - allocatedHeight - 5);
                    
                    // Draw district name
                    g2d.setColor(TEXT_COLOR);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    textWidth = fm.stringWidth(district.getName());
                    
                    // Rotate district name if it's too long
                    if (textWidth > barWidth + barSpacing) {
                        AffineTransform originalTransform = g2d.getTransform();
                        g2d.rotate(-Math.PI/4, x + barWidth/2, baseline + 10);
                        g2d.drawString(district.getName(), x + barWidth/2 - textWidth/2, baseline + 20);
                        g2d.setTransform(originalTransform);
                    } else {
                        g2d.drawString(district.getName(), x + barWidth/2 - textWidth/2, baseline + 20);
                    }
                    
                    x += barWidth + barSpacing;
                }
                
                // Draw modern legend
                int legendX = width - 180;
                int legendY = 30;
                int legendSize = 15;
                int textOffset = 25;
                int rowHeight = 25;
                
                // Draw legend box
                g2d.setColor(new Color(250, 250, 250));
                g2d.fillRoundRect(legendX - 10, legendY - 20, 170, 90, 10, 10);
                g2d.setColor(new Color(230, 230, 230));
                g2d.drawRoundRect(legendX - 10, legendY - 20, 170, 90, 10, 10);
                
                // Full allocation
                g2d.setColor(new Color(40, 167, 69));
                g2d.fillRoundRect(legendX, legendY, legendSize, legendSize, 4, 4);
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2d.drawString("Full Allocation", legendX + textOffset, legendY + 12);
                
                // Partial allocation
                g2d.setColor(new Color(255, 193, 7));
                g2d.fillRoundRect(legendX, legendY + rowHeight, legendSize, legendSize, 4, 4);
                g2d.setColor(TEXT_COLOR);
                g2d.drawString("Partial Allocation", legendX + textOffset, legendY + rowHeight + 12);
                
                // Resource demand
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRoundRect(legendX, legendY + rowHeight*2, legendSize, legendSize, 4, 4);
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRoundRect(legendX, legendY + rowHeight*2, legendSize, legendSize, 4, 4);
                g2d.setColor(TEXT_COLOR);
                g2d.drawString("Resource Demand", legendX + textOffset, legendY + rowHeight*2 + 12);
                
                // Draw resource stats at bottom
                int statsY = height - 15;
                g2d.setColor(PRIMARY_COLOR);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2d.drawString("Total Resources: " + totalResources, 70, statsY);
                
                // Format text for remaining resources
                String remainingText = remainingResources > 0 ? 
                        "Unallocated: " + remainingResources : 
                        "All resources allocated";
                g2d.setColor(remainingResources == 0 ? new Color(40, 167, 69) : TEXT_COLOR);
                g2d.drawString(remainingText, width - 200, statsY);
            }
        };
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.add(visualizationTitleLabel, BorderLayout.WEST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Applies consistent modern styling to tables
     */
    private void styleTable(JTable table) {
        // Set basic table properties
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Style the header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Add row highlighting on hover/selection
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_COLOR);
        
        // Add zebra striping (alternate row colors)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                
                ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return comp;
            }
        });
    }

    /**
     * Display an error message dialog
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Inner class to store allocation results
     */
    private static class AllocationResult {
        District district;
        int allocatedResources;
        boolean isPartial;
        
        public AllocationResult(District district, int allocatedResources, boolean isPartial) {
            this.district = district;
            this.allocatedResources = allocatedResources;
            this.isPartial = isPartial;
        }
    }
}