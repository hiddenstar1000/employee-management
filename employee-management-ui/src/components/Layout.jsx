import React from 'react';
import { Users } from 'lucide-react';

export default function Layout({ children }) {
  return (
    <div className="min-h-screen bg-surface-50 font-sans">
      {/* Top Navigation */}
      <header className="bg-white border-b border-surface-200 sticky top-0 z-30">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="flex items-center gap-2">
              <div className="bg-brand-100 p-2 rounded-lg text-brand-600">
                <Users size={24} />
              </div>
              <h1 className="text-xl font-semibold text-surface-900 tracking-tight">
                Team Directory
              </h1>
            </div>
            <div className="flex items-center gap-4">
              <div className="h-8 w-8 rounded-full bg-surface-200 border border-surface-300 overflow-hidden flex items-center justify-center">
                <span className="text-sm font-medium text-surface-600">A</span>
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
    </div>
  );
}
