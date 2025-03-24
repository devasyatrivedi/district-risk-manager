
import React, { useState } from 'react';
import Header from '@/components/Header';
import DistrictForm, { District } from '@/components/DistrictForm';
import DistrictList from '@/components/DistrictList';
import ResourceAllocation from '@/components/ResourceAllocation';
import { calculateDistrictRisk } from '@/utils/riskCalculator';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { ChevronRight, PlusCircle, BarChart3 } from 'lucide-react';
import { Toaster } from 'sonner';

const Index = () => {
  const [districts, setDistricts] = useState<District[]>([]);
  const [totalResources, setTotalResources] = useState<number>(0);
  const [allocatedResources, setAllocatedResources] = useState<Record<number, number>>({});
  const [showForm, setShowForm] = useState<boolean>(false);

  const handleAddDistrict = (district: District) => {
    const riskScore = calculateDistrictRisk(
      district.population,
      district.landType,
      district.urbanization
    );
    
    const districtWithRisk = {
      ...district,
      riskScore
    };
    
    setDistricts([...districts, districtWithRisk]);
    setShowForm(false);
  };

  const handleAllocateResources = (allocations: Record<number, number>) => {
    setAllocatedResources(allocations);
  };

  // Calculate total risk across all districts
  const totalRisk = districts.reduce((sum, district) => sum + district.riskScore, 0);

  return (
    <div className="min-h-screen pb-16 bg-background">
      <Header />
      <main className="container mx-auto px-4 py-6 space-y-4">
        <Card className="w-full animate-fade-in">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-xl font-medium">Resources Balance</CardTitle>
            <Button variant="ghost" size="sm" className="text-muted-foreground">
              Configure <ChevronRight className="ml-1 h-4 w-4" />
            </Button>
          </CardHeader>
          <CardContent>
            <div className="text-4xl font-bold py-4">{totalResources}</div>
            <div className="grid grid-cols-2 gap-2">
              <Button 
                variant="outline" 
                onClick={() => setShowForm(true)}
                className="flex items-center justify-center gap-2 bg-muted/50 hover:bg-muted"
              >
                <PlusCircle className="h-5 w-5" />
                Add District
              </Button>
              <Button 
                variant="outline"
                onClick={() => {
                  const input = window.prompt("Enter total resources:");
                  if (input) {
                    const resources = parseInt(input);
                    if (!isNaN(resources) && resources > 0) {
                      setTotalResources(resources);
                    }
                  }
                }}
                className="flex items-center justify-center gap-2 bg-muted/50 hover:bg-muted"
              >
                <BarChart3 className="h-5 w-5" />
                Set Resources
              </Button>
            </div>
          </CardContent>
        </Card>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Card className="w-full animate-fade-in">
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-xl font-medium">Risk Metrics</CardTitle>
              <Button variant="ghost" size="sm" className="text-muted-foreground">
                Details <ChevronRight className="ml-1 h-4 w-4" />
              </Button>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-bold py-4">{totalRisk || 0}</div>
              <p className="text-muted-foreground text-sm">Total risk score across all districts</p>
            </CardContent>
          </Card>
          
          <Card className="w-full animate-fade-in">
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-xl font-medium">Districts</CardTitle>
              <Button variant="ghost" size="sm" className="text-muted-foreground">
                View All <ChevronRight className="ml-1 h-4 w-4" />
              </Button>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-bold py-4">{districts.length}</div>
              <p className="text-muted-foreground text-sm">Total districts under management</p>
            </CardContent>
          </Card>
        </div>
        
        {showForm && (
          <Card className="w-full animate-fade-in">
            <CardHeader>
              <CardTitle>Add New District</CardTitle>
            </CardHeader>
            <CardContent>
              <DistrictForm 
                onAddDistrict={handleAddDistrict}
                districtCount={districts.length}
                totalResources={totalResources}
                setTotalResources={setTotalResources}
              />
            </CardContent>
          </Card>
        )}
        
        {districts.length > 0 && (
          <>
            <Card className="w-full animate-fade-in">
              <CardHeader>
                <CardTitle>Resource Allocation</CardTitle>
              </CardHeader>
              <CardContent>
                <ResourceAllocation
                  districts={districts}
                  totalResources={totalResources}
                  onAllocate={handleAllocateResources}
                />
              </CardContent>
            </Card>
            
            <Card className="w-full animate-fade-in">
              <CardHeader>
                <CardTitle>District Overview</CardTitle>
              </CardHeader>
              <CardContent>
                <DistrictList 
                  districts={districts}
                  allocatedResources={allocatedResources}
                />
              </CardContent>
            </Card>
          </>
        )}
      </main>
      <Toaster position="top-right" closeButton />
    </div>
  );
};

export default Index;
