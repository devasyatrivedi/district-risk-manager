
import React from 'react';
import { Shield, User } from 'lucide-react';

const Header: React.FC = () => {
  return (
    <header className="w-full py-6 animate-fade-in bg-background">
      <div className="container mx-auto px-4 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <Shield className="h-8 w-8 text-primary" />
          <h1 className="text-3xl font-semibold">Districts</h1>
        </div>
        <div className="h-10 w-10 rounded-full bg-muted flex items-center justify-center">
          <User className="h-6 w-6 text-muted-foreground" />
        </div>
      </div>
    </header>
  );
};

export default Header;
