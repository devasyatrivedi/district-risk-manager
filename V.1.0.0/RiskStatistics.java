/**
 * Utility class for calculating various risk statistics
 * for disaster response resource allocation.
 */
public class RiskStatistics {
    /**
     * Calculates the risk score based on population size
     * 
     * @param population The population of the district
     * @return Risk score from 1-4
     */
    public static int calculatePopulationRisk(int population) {
        if (population < 10000) {
            return 1;
        } 
        else if (population >= 10000 && population <= 50000) {
            return 2; 
        } 
        else if(population > 50000 && population <= 100000) {
            return 3; 
        }
        else {
            return 4;
        }
    }
                                                                             
    /**
     * Calculates the risk score based on land type
     * 
     * @param landType The type of land (Forest, Coastal, Desert, Urban)
     * @return Risk score from 1-4
     */
    public static int calculateLandTypeRisk(String landType) {
        switch (landType) {
            case "Forest":
                return 1;
            case "Coastal":
                return 2;
            case "Desert":
                return 3;
            case "Urban":
                return 4;
            default:
                return 0;
        }
    }

    /**
     * Calculates the risk score based on urbanization level
     * 
     * @param urbanization The level of urbanization (Rural, Suburban, Urban)
     * @return Risk score from 1-3
     */
    public static int calculateUrbanizationRisk(String urbanization) {
        switch (urbanization) {
            case "Rural":
                return 1;
            case "Suburban":
                return 2;
            case "Urban":
                return 3;
            default:
                return 0;
        }
    }

    /**
     * Calculates the total weighted risk score
     * 
     * @param populationRisk Risk score from population
     * @param landTypeRisk Risk score from land type
     * @param urbanizationRisk Risk score from urbanization
     * @return Total weighted risk score
     */
    public static int calculateTotalRisk(int populationRisk, int landTypeRisk, int urbanizationRisk) {
        int weightPopulation = 3;
        int weightLandType = 2;
        int weightUrbanization = 1;

        return (populationRisk * weightPopulation) + (landTypeRisk * weightLandType) + (urbanizationRisk * weightUrbanization);
    }
}