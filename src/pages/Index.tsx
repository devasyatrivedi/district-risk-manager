
import React, { useState } from 'react';
import Header from '@/components/Header';
import DistrictForm, { District } from '@/components/DistrictForm';
import DistrictList from '@/components/DistrictList';
import ResourceAllocation from '@/components/ResourceAllocation';
import { calculateDistrictRisk } from '@/utils/riskCalculator';
import { Separator } from '@/components/ui/separator';
import { Toaster } from 'sonner';

const Index = () => {
  const [districts, setDistricts] = useState<District[]>([]);
  const [totalResources, setTotalResources] = useState<number>(0);
  const [allocatedResources, setAllocatedResources] = useState<Record<number, number>>({});

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
  };

  const handleAllocateResources = (allocations: Record<number, number>) => {
    setAllocatedResources(allocations);
  };

  return (
    <div className="min-h-screen pb-16 bg-background">
      <Header />
      <main className="container mx-auto px-4 py-8 space-y-8">
        <div className="text-center max-w-3xl mx-auto space-y-3 animate-fade-in">
          <h1 className="text-4xl md:text-5xl font-semibold">District Risk Management</h1>
          <p className="text-lg text-muted-foreground">
            Intelligently manage and allocate resources based on district risk factors
          </p>
        </div>

        <Separator className="my-8" />
        
        <DistrictForm 
          onAddDistrict={handleAddDistrict}
          districtCount={districts.length}
          totalResources={totalResources}
          setTotalResources={setTotalResources}
        />
        
        <ResourceAllocation
          districts={districts}
          totalResources={totalResources}
          onAllocate={handleAllocateResources}
        />
        
        <DistrictList 
          districts={districts}
          allocatedResources={allocatedResources}
        />
      </main>
      <Toaster position="top-right" closeButton />
    </div>
  );
};

export default Index;
