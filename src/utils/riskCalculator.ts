
export const calculatePopulationRisk = (population: number): number => {
  if (population < 10000) {
    return 1;
  } else if (population >= 10000 && population <= 50000) {
    return 2;
  } else if (population > 50000 && population <= 100000) {
    return 3;
  } else {
    return 4;
  }
};

export const calculateLandTypeRisk = (landType: string): number => {
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
};

export const calculateUrbanizationRisk = (urbanization: string): number => {
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
};

export const calculateTotalRisk = (
  populationRisk: number,
  landTypeRisk: number,
  urbanizationRisk: number
): number => {
  const weightPopulation = 3;
  const weightLandType = 2;
  const weightUrbanization = 1;

  return (
    populationRisk * weightPopulation +
    landTypeRisk * weightLandType +
    urbanizationRisk * weightUrbanization
  );
};

export const calculateDistrictRisk = (
  population: number,
  landType: string,
  urbanization: string
): number => {
  const populationRisk = calculatePopulationRisk(population);
  const landTypeRisk = calculateLandTypeRisk(landType);
  const urbanizationRisk = calculateUrbanizationRisk(urbanization);

  return calculateTotalRisk(populationRisk, landTypeRisk, urbanizationRisk);
};
