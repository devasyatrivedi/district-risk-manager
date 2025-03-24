
import React from 'react';
import { Shield } from 'lucide-react';

const Header: React.FC = () => {
  return (
    <header className="w-full py-6 animate-fade-in">
      <div className="container mx-auto px-4 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <Shield className="h-8 w-8 text-primary animate-pulse-gentle" />
          <h1 className="text-2xl font-medium">District Risk Manager</h1>
        </div>
      </div>
    </header>
  );
};

export default Header;
