
import React, { useState } from 'react';
import { 
  Card, 
  CardContent, 
  CardDescription, 
  CardFooter, 
  CardHeader, 
  CardTitle 
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { 
  Select, 
  SelectContent, 
  SelectItem, 
  SelectTrigger, 
  SelectValue 
} from "@/components/ui/select";
import { toast } from "sonner";
import { useToast } from "@/hooks/use-toast";

export type District = {
  id: number;
  name: string;
  population: number;
  landType: string;
  urbanization: string;
  resourceDemand: number;
  riskScore?: number;
};

type DistrictFormProps = {
  onAddDistrict: (district: District) => void;
  districtCount: number;
  totalResources: number;
  setTotalResources: (value: number) => void;
};

const DistrictForm: React.FC<DistrictFormProps> = ({ 
  onAddDistrict, 
  districtCount,
  totalResources,
  setTotalResources
}) => {
  const [name, setName] = useState('');
  const [population, setPopulation] = useState('');
  const [landType, setLandType] = useState('');
  const [urbanization, setUrbanization] = useState('');
  const [resourceDemand, setResourceDemand] = useState('');
  const [resources, setResources] = useState('');
  const { toast } = useToast();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!name || !population || !landType || !urbanization || !resourceDemand) {
      toast({
        title: "Missing fields",
        description: "Please fill out all fields",
        variant: "destructive",
      });
      return;
    }

    const populationNum = parseInt(population);
    const resourceDemandNum = parseInt(resourceDemand);

    if (isNaN(populationNum) || populationNum <= 0) {
      toast({
        title: "Invalid population",
        description: "Population must be a positive number",
        variant: "destructive",
      });
      return;
    }

    if (isNaN(resourceDemandNum) || resourceDemandNum <= 0) {
      toast({
        title: "Invalid resource demand",
        description: "Resource demand must be a positive number",
        variant: "destructive",
      });
      return;
    }

    const district: District = {
      id: Date.now(),
      name,
      population: populationNum,
      landType,
      urbanization,
      resourceDemand: resourceDemandNum,
    };

    onAddDistrict(district);
    
    // Reset form fields
    setName('');
    setPopulation('');
    setLandType('');
    setUrbanization('');
    setResourceDemand('');
    
    toast({
      title: "District added",
      description: `${name} has been added to the list`,
    });
  };

  const handleSetResources = () => {
    const resourcesNum = parseInt(resources);
    
    if (isNaN(resourcesNum) || resourcesNum < 0) {
      toast({
        title: "Invalid resources",
        description: "Total resources must be a non-negative number",
        variant: "destructive",
      });
      return;
    }
    
    setTotalResources(resourcesNum);
    setResources('');
    
    toast({
      title: "Resources updated",
      description: `Total resources set to ${resourcesNum}`,
    });
  };

  return (
    <div className="grid grid-cols-1 gap-6 md:grid-cols-2 animate-fade-in">
      <Card className="hover-scale glass-panel rounded-2xl overflow-hidden">
        <CardHeader>
          <CardTitle>Add District</CardTitle>
          <CardDescription>Enter the details for a new district</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="name">District Name</Label>
              <Input
                id="name"
                placeholder="Enter district name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="smooth-transition"
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="population">Population</Label>
              <Input
                id="population"
                type="number"
                placeholder="Enter population"
                value={population}
                onChange={(e) => setPopulation(e.target.value)}
                className="smooth-transition"
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="landType">Land Type</Label>
              <Select value={landType} onValueChange={setLandType}>
                <SelectTrigger id="landType" className="smooth-transition">
                  <SelectValue placeholder="Select land type" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Forest">Forest</SelectItem>
                  <SelectItem value="Coastal">Coastal</SelectItem>
                  <SelectItem value="Desert">Desert</SelectItem>
                  <SelectItem value="Urban">Urban</SelectItem>
                </SelectContent>
              </Select>
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="urbanization">Urbanization</Label>
              <Select value={urbanization} onValueChange={setUrbanization}>
                <SelectTrigger id="urbanization" className="smooth-transition">
                  <SelectValue placeholder="Select urbanization" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Rural">Rural</SelectItem>
                  <SelectItem value="Suburban">Suburban</SelectItem>
                  <SelectItem value="Urban">Urban</SelectItem>
                </SelectContent>
              </Select>
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="resourceDemand">Resource Demand</Label>
              <Input
                id="resourceDemand"
                type="number"
                placeholder="Enter resource demand"
                value={resourceDemand}
                onChange={(e) => setResourceDemand(e.target.value)}
                className="smooth-transition"
              />
            </div>
            
            <Button type="submit" className="w-full smooth-transition">
              Add District
            </Button>
          </form>
        </CardContent>
      </Card>
      
      <Card className="hover-scale glass-panel rounded-2xl overflow-hidden">
        <CardHeader>
          <CardTitle>Resource Management</CardTitle>
          <CardDescription>Set total available resources and view current status</CardDescription>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="totalResources">Total Available Resources</Label>
            <div className="flex space-x-2">
              <Input
                id="totalResources"
                type="number"
                placeholder="Enter total resources"
                value={resources}
                onChange={(e) => setResources(e.target.value)}
                className="smooth-transition"
              />
              <Button onClick={handleSetResources} className="smooth-transition">Set</Button>
            </div>
          </div>
          
          <div className="space-y-4">
            <h3 className="text-lg font-medium">Current Status</h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="p-4 rounded-lg bg-secondary">
                <div className="text-sm text-muted-foreground">Total Resources</div>
                <div className="text-2xl font-medium">{totalResources}</div>
              </div>
              <div className="p-4 rounded-lg bg-secondary">
                <div className="text-sm text-muted-foreground">Districts</div>
                <div className="text-2xl font-medium">{districtCount}</div>
              </div>
            </div>
          </div>
        </CardContent>
        <CardFooter className="flex justify-between border-t px-6 py-4">
          <p className="text-sm text-muted-foreground">
            Risk scores are calculated automatically based on district data
          </p>
        </CardFooter>
      </Card>
    </div>
  );
};

export default DistrictForm;
