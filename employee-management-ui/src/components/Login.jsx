import React, { useState } from 'react';
import { Mail, Lock, Eye, EyeOff, LogIn, AlertCircle, Building2, ShieldCheck } from 'lucide-react';
import { login } from '../services/api';

export default function Login({ onLoginSuccess }) {
  const [emailId, setEmailId] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await login({ emailId: emailId.trim(), password });
      const { token, employee } = response.data;

      localStorage.setItem('token', token);
      localStorage.setItem('currentUser', JSON.stringify(employee));

      if (onLoginSuccess) {
        onLoginSuccess({ token, employee });
      }
    } catch (err) {
      console.error('Login error:', err);
      const message = err.response?.data?.message || 'Failed to authenticate. Please check your credentials.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen bg-gradient-to-br from-surface-900 via-surface-800 to-brand-950 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-brand-600/20 border border-brand-500/30 text-brand-400 mb-4 shadow-xl backdrop-blur-md">
            <Building2 size={32} />
          </div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Employee Hub</h1>
          <p className="text-surface-400 text-sm mt-1">Enterprise Management Portal</p>
        </div>

        <div className="bg-white/10 backdrop-blur-xl border border-white/20 rounded-3xl p-8 shadow-2xl animate-in fade-in zoom-in-95 duration-200">
          <div className="flex items-center justify-between mb-6 border-b border-white/10 pb-4">
            <div>
              <h2 className="text-xl font-semibold text-white">Sign In</h2>
              <p className="text-xs text-surface-300">Enter your credentials to access the system</p>
            </div>
            <div className="px-2.5 py-1 rounded-full bg-emerald-500/20 border border-emerald-500/30 text-emerald-300 text-xs font-medium flex items-center gap-1">
              <ShieldCheck size={14} /> Spring Security
            </div>
          </div>

          {error && (
            <div className="mb-6 p-4 rounded-xl bg-red-500/10 border border-red-500/30 flex items-start gap-3 text-red-200 text-sm animate-in fade-in duration-150">
              <AlertCircle size={18} className="text-red-400 shrink-0 mt-0.5" />
              <div>{error}</div>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-1.5">
              <label htmlFor="loginEmail" className="block text-xs font-medium uppercase tracking-wider text-surface-300">
                Email Address
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-surface-400">
                  <Mail size={18} />
                </div>
                <input
                  type="email"
                  id="loginEmail"
                  required
                  value={emailId}
                  onChange={(e) => setEmailId(e.target.value)}
                  placeholder="jane.doe@example.com"
                  className="w-full pl-10 pr-4 py-2.5 bg-white/10 border border-white/20 rounded-xl text-white placeholder-surface-400 focus:outline-none focus:ring-2 focus:ring-brand-500 focus:border-transparent transition-all text-sm"
                />
              </div>
            </div>

            <div className="space-y-1.5">
              <label htmlFor="loginPassword" className="block text-xs font-medium uppercase tracking-wider text-surface-300">
                Password
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-surface-400">
                  <Lock size={18} />
                </div>
                <input
                  type={showPassword ? 'text' : 'password'}
                  id="loginPassword"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full pl-10 pr-10 py-2.5 bg-white/10 border border-white/20 rounded-xl text-white placeholder-surface-400 focus:outline-none focus:ring-2 focus:ring-brand-500 focus:border-transparent transition-all text-sm"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-surface-400 hover:text-white transition-colors"
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full mt-2 py-3 px-4 bg-brand-600 hover:bg-brand-500 text-white font-semibold rounded-xl shadow-lg shadow-brand-600/30 flex items-center justify-center gap-2 transition-all duration-200 active:scale-[0.99] disabled:opacity-50 disabled:pointer-events-none text-base sm:text-sm"
            >
              {loading ? (
                <div className="h-5 w-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : (
                <>
                  <LogIn size={18} />
                  <span>Login</span>
                </>
              )}
            </button>
          </form>
        </div>
      </div>

      <div className="absolute bottom-4 right-4 text-xs text-surface-400 font-mono select-none">
        version: 1.0.0
      </div>
    </div>
  );
}

