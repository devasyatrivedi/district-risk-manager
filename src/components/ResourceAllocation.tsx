
import React from 'react';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { District } from './DistrictForm';
import { Progress } from "@/components/ui/progress";
import { ArrowDownToLine, Zap } from 'lucide-react';
import { toast } from "sonner";

type ResourceAllocationProps = {
  districts: District[];
  totalResources: number;
  onAllocate: (allocations: Record<number, number>) => void;
};

const ResourceAllocation: React.FC<ResourceAllocationProps> = ({ 
  districts,
  totalResources,
  onAllocate
}) => {
  const allocateResources = () => {
    if (districts.length === 0) {
      toast("No districts to allocate resources to");
      return;
    }
    
    if (totalResources <= 0) {
      toast("No resources available for allocation");
      return;
    }
    
    // Sort districts by risk score / resource demand ratio (descending)
    const sortedDistricts = [...districts].sort((a, b) => {
      if (!a.riskScore || !b.riskScore) return 0;
      
      const ratioA = a.riskScore / a.resourceDemand;
      const ratioB = b.riskScore / b.resourceDemand;
      
      return ratioB - ratioA;
    });
    
    let remainingResources = totalResources;
    const allocations: Record<number, number> = {};
    
    for (const district of sortedDistricts) {
      if (remainingResources >= district.resourceDemand) {
        allocations[district.id] = district.resourceDemand;
        remainingResources -= district.resourceDemand;
      } else {
        allocations[district.id] = remainingResources;
        remainingResources = 0;
        break;
      }
    }
    
    onAllocate(allocations);
    
    toast.success(
      remainingResources > 0
        ? `Resources allocated with ${remainingResources} remaining`
        : "All resources have been allocated"
    );
  };
  
  // Calculate total resource demand
  const totalDemand = districts.reduce((sum, district) => sum + district.resourceDemand, 0);
  
  // Calculate percentage of resources that can be fulfilled
  const fulfillmentPercentage = totalDemand > 0 
    ? Math.min(100, (totalResources / totalDemand) * 100) 
    : 0;
  
  return (
    <div className="mt-8 animate-fade-in">
      <Card className="hover-scale glass-panel rounded-2xl overflow-hidden">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Zap className="h-5 w-5 text-primary" />
            Resource Allocation
          </CardTitle>
          <CardDescription>
            Allocate available resources based on risk priority
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-6">
            <div className="space-y-2">
              <div className="flex justify-between">
                <span className="text-sm text-muted-foreground">Total Resources</span>
                <span className="text-sm font-medium">{totalResources}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-muted-foreground">Total Demand</span>
                <span className="text-sm font-medium">{totalDemand}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-muted-foreground">Resource Fulfillment</span>
                <span className="text-sm font-medium">{fulfillmentPercentage.toFixed(1)}%</span>
              </div>
              <Progress value={fulfillmentPercentage} className="h-2 mt-2" />
            </div>
            
            {districts.length > 0 && totalResources > 0 ? (
              <Button 
                onClick={allocateResources} 
                className="w-full smooth-transition flex items-center gap-2"
              >
                <ArrowDownToLine className="h-4 w-4" />
                Allocate Resources
              </Button>
            ) : (
              <Button 
                disabled 
                className="w-full"
              >
                {districts.length === 0 
                  ? "Add districts to allocate resources" 
                  : "Set available resources"}
              </Button>
            )}
            
            <div className="text-sm text-muted-foreground">
              <p>Resources will be allocated based on risk score priority.</p>
              <p className="mt-1">Districts with higher risk-to-demand ratios receive resources first.</p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default ResourceAllocation;
