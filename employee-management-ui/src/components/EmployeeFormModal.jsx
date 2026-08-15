import React, { useState, useEffect } from 'react';
import { X, Lock, Eye, EyeOff, ShieldCheck } from 'lucide-react';

export default function EmployeeFormModal({ isOpen, onClose, onSubmit, employee }) {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    emailId: '',
    department: '',
    loginEnabled: false,
    password: '',
  });
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    if (employee) {
      setFormData({
        firstName: employee.firstName || '',
        lastName: employee.lastName || '',
        emailId: employee.emailId || '',
        department: employee.department || '',
        loginEnabled: employee.loginEnabled || false,
        password: '',
      });
    } else {
      setFormData({
        firstName: '',
        lastName: '',
        emailId: '',
        department: '',
        loginEnabled: false,
        password: '',
      });
    }
    setShowPassword(false);
  }, [employee, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (type === 'checkbox') {
      setFormData((prev) => ({
        ...prev,
        [name]: checked,
        password: checked ? prev.password : '',
      }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = { ...formData };
    if (!payload.loginEnabled) {
      delete payload.password;
    }
    onSubmit(payload);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-surface-900/40 backdrop-blur-sm">
      <div 
        className="bg-white rounded-2xl shadow-xl w-full max-w-md max-h-[90vh] overflow-y-auto animate-in fade-in zoom-in-95 duration-200"
        role="dialog"
        aria-modal="true"
      >

        <div className="flex items-center justify-between px-6 py-4 border-b border-surface-200">
          <h2 className="text-xl font-semibold text-surface-900">
            {employee ? 'Edit Team Member' : 'Add Team Member'}
          </h2>
          <button 
            onClick={onClose}
            className="p-2 text-surface-400 hover:text-surface-600 hover:bg-surface-100 rounded-full transition-colors"
          >
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-1.5">
              <label htmlFor="firstName" className="block text-sm font-medium text-surface-700">First Name</label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                required
                value={formData.firstName}
                onChange={handleChange}
                className="input-field"
                placeholder="Jane"
              />
            </div>
            <div className="space-y-1.5">
              <label htmlFor="lastName" className="block text-sm font-medium text-surface-700">Last Name</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                required
                value={formData.lastName}
                onChange={handleChange}
                className="input-field"
                placeholder="Doe"
              />
            </div>
          </div>

          <div className="space-y-1.5">
            <label htmlFor="emailId" className="block text-sm font-medium text-surface-700">Email Address</label>
            <input
              type="email"
              id="emailId"
              name="emailId"
              required
              value={formData.emailId}
              onChange={handleChange}
              className="input-field"
              placeholder="jane.doe@example.com"
            />
          </div>

          <div className="space-y-1.5">
            <label htmlFor="department" className="block text-sm font-medium text-surface-700">Department</label>
            <select
              id="department"
              name="department"
              required
              value={formData.department}
              onChange={handleChange}
              className="input-field bg-white"
            >
              <option value="" disabled>Select a department</option>
              <option value="Engineering">Engineering</option>
              <option value="Design">Design</option>
              <option value="Product">Product</option>
              <option value="Marketing">Marketing</option>
              <option value="Sales">Sales</option>
              <option value="HR">HR</option>
            </select>
          </div>

          <div className="pt-2 border-t border-surface-200 space-y-3">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Lock size={16} className={formData.loginEnabled ? "text-brand-600" : "text-surface-400"} />
                <label htmlFor="loginEnabled" className="text-sm font-medium text-surface-800 cursor-pointer select-none">
                  Enable System Login
                </label>
              </div>
              <input
                type="checkbox"
                id="loginEnabled"
                name="loginEnabled"
                checked={formData.loginEnabled}
                onChange={handleChange}
                className="h-4 w-4 text-brand-600 focus:ring-brand-500 border-surface-300 rounded cursor-pointer"
              />
            </div>

            {formData.loginEnabled ? (
              <div className="space-y-1.5 animate-in fade-in duration-200">
                <div className="flex items-center justify-between">
                  <label htmlFor="password" className="block text-sm font-medium text-surface-700">
                    Password <span className="text-red-500">*</span>
                  </label>
                  <span className="text-xs text-brand-600 flex items-center gap-1 font-medium">
                    <ShieldCheck size={12} /> AES-256-GCM
                  </span>
                </div>
                <div className="relative">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    id="password"
                    name="password"
                    required={formData.loginEnabled}
                    value={formData.password}
                    onChange={handleChange}
                    className="input-field pr-10"
                    placeholder={employee ? "Enter new password to update" : "Set login password"}
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-surface-400 hover:text-surface-600"
                    tabIndex={-1}
                  >
                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
                <p className="text-xs text-surface-500">
                  Password will be encrypted using strong AES-256-GCM encryption before saving.
                </p>
              </div>
            ) : (
              <div className="p-3 bg-surface-50 border border-surface-200 rounded-lg text-xs text-surface-500">
                System login is currently disabled for this employee.
              </div>
            )}
          </div>

          <div className="pt-4 flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="btn-secondary"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn-primary"
            >
              {employee ? 'Save Changes' : 'Add Member'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

