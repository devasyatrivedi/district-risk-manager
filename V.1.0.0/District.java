/**
 * Represents a district with properties relevant for disaster response
 * resource allocation.
 */
public class District {
    private String name;
    private int population;
    private String landType;
    private String urbanization;
    private int riskScore;
    private int resourceDemand; 

    /**
     * Constructor for District
     * 
     * @param name District name
     * @param population District population
     * @param landType Type of land (Forest, Coastal, Desert, Urban)
     * @param urbanization Level of urbanization (Rural, Suburban, Urban)
     * @param resourceDemand Resources needed for this district
     */
    public District(String name, int population, String landType, String urbanization, int resourceDemand) {
        this.name = name;
        this.population = population;
        this.landType = landType;
        this.urbanization = urbanization;
        this.resourceDemand = resourceDemand;
        
        // Calculate risk score automatically upon creation
        calculateAndSetRiskScore();
    }

    /**
     * Calculate and set the risk score for this district
     */
    private void calculateAndSetRiskScore() {
        int populationRisk = RiskStatistics.calculatePopulationRisk(population);
        int landTypeRisk = RiskStatistics.calculateLandTypeRisk(landType);
        int urbanizationRisk = RiskStatistics.calculateUrbanizationRisk(urbanization);
        
        this.riskScore = RiskStatistics.calculateTotalRisk(
                populationRisk, landTypeRisk, urbanizationRisk);
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public int getPopulation() {
        return population;
    }
    
    public String getLandType() {
        return landType;
    }
    
    public String getUrbanization() {
        return urbanization;
    }
    
    public int getRiskScore() {
        return riskScore;
    }
    
    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }
    
    public int getResourceDemand() {
        return resourceDemand;
    }
    
    /**
     * Calculate risk to resource ratio used for prioritization
     * 
     * @return The ratio of risk score to resource demand
     */
    public double getRiskResourceRatio() {
        return (double) riskScore / resourceDemand;
    }
    
    @Override
    public String toString() {
        return name;
    }
}