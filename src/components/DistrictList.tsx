
import React from 'react';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { District } from './DistrictForm';

type DistrictListProps = {
  districts: District[];
  allocatedResources: Record<number, number>;
};

const getRiskLevel = (score: number): string => {
  if (score <= 10) return 'Low';
  if (score <= 15) return 'Medium';
  if (score <= 20) return 'High';
  return 'Critical';
};

const getRiskClass = (score: number): string => {
  if (score <= 10) return 'risk-low';
  if (score <= 15) return 'risk-medium';
  if (score <= 20) return 'risk-high';
  return 'risk-critical';
};

const DistrictList: React.FC<DistrictListProps> = ({ districts, allocatedResources }) => {
  return (
    <div className="space-y-6 mt-8 animate-fade-in">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-medium">District Overview</h2>
        <Badge variant="outline" className="px-3 py-1">
          {districts.length} {districts.length === 1 ? 'District' : 'Districts'}
        </Badge>
      </div>
      
      {districts.length === 0 ? (
        <Card className="border-dashed hover-scale glass-panel rounded-2xl overflow-hidden">
          <CardContent className="py-8 flex flex-col items-center justify-center text-center">
            <p className="text-muted-foreground">No districts added yet</p>
            <p className="text-sm text-muted-foreground mt-1">Add a district using the form above</p>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {districts.map((district, index) => (
            <Card 
              key={district.id} 
              className={`hover-scale glass-panel rounded-2xl overflow-hidden animate-scale-in`} 
              style={{ animationDelay: `${index * 50}ms` }}
            >
              <CardHeader className="pb-2">
                <div className="flex justify-between items-start">
                  <CardTitle className="text-xl">{district.name}</CardTitle>
                  {district.riskScore && (
                    <Badge className={`${getRiskClass(district.riskScore)}`}>
                      {getRiskLevel(district.riskScore)}
                    </Badge>
                  )}
                </div>
                <CardDescription>Population: {district.population.toLocaleString()}</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div className="grid grid-cols-2 gap-2 text-sm">
                    <div className="flex flex-col">
                      <span className="text-muted-foreground">Land Type</span>
                      <span>{district.landType}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-muted-foreground">Urbanization</span>
                      <span>{district.urbanization}</span>
                    </div>
                  </div>
                  
                  <div className="pt-2 border-t">
                    <div className="flex justify-between items-center">
                      <div className="text-sm">
                        <span className="text-muted-foreground">Resource Demand:</span>
                        <span className="ml-1 font-medium">{district.resourceDemand}</span>
                      </div>
                      {district.riskScore && (
                        <div className="text-sm">
                          <span className="text-muted-foreground">Risk Score:</span>
                          <span className="ml-1 font-medium">{district.riskScore}</span>
                        </div>
                      )}
                    </div>
                  </div>
                  
                  {allocatedResources[district.id] !== undefined && (
                    <div className="pt-2 border-t">
                      <div className="flex justify-between items-center">
                        <div className="text-sm">
                          <span className="text-muted-foreground">Allocated:</span>
                          <span className="ml-1 font-medium">{allocatedResources[district.id]}</span>
                        </div>
                        <Badge variant={allocatedResources[district.id] < district.resourceDemand ? "secondary" : "outline"}>
                          {allocatedResources[district.id] < district.resourceDemand ? "Partial" : "Full"}
                        </Badge>
                      </div>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default DistrictList;
