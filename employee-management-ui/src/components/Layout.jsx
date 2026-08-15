import React from 'react';
import { Users, LogOut, User } from 'lucide-react';

export default function Layout({ children, currentUser, onLogout }) {
  const initial = currentUser?.firstName ? currentUser.firstName.charAt(0).toUpperCase() : 'U';

  return (
    <div className="min-h-screen bg-surface-50 font-sans">
      {/* Top Navigation */}
      <header className="bg-white border-b border-surface-200 sticky top-0 z-30 shadow-xs">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="flex items-center gap-3">
              <div className="bg-brand-100 p-2 rounded-xl text-brand-600 shadow-xs">
                <Users size={24} />
              </div>
              <div>
                <h1 className="text-xl font-bold text-surface-900 tracking-tight">
                  Team Directory
                </h1>
                <p className="text-xs text-surface-500">Employee Management System</p>
              </div>
            </div>

            <div className="flex items-center gap-4">
              {currentUser && (
                <div className="flex items-center gap-3 bg-surface-100 border border-surface-200 px-3 py-1.5 rounded-full">
                  <div className="h-7 w-7 rounded-full bg-brand-600 text-white flex items-center justify-center font-semibold text-xs shadow-xs">
                    {initial}
                  </div>
                  <div className="text-xs">
                    <div className="font-semibold text-surface-800">
                      {currentUser.firstName} {currentUser.lastName}
                    </div>
                    <div className="text-surface-500">{currentUser.emailId}</div>
                  </div>
                </div>
              )}

              {onLogout && (
                <button
                  onClick={onLogout}
                  className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium text-surface-600 hover:text-red-600 hover:bg-red-50 border border-surface-200 hover:border-red-200 rounded-lg transition-colors"
                  title="Sign out of system"
                >
                  <LogOut size={16} />
                  <span>Logout</span>
                </button>
              )}
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

