import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

export default function EmployeeFormModal({ isOpen, onClose, onSubmit, employee }) {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    emailId: '',
    department: '',
  });

  useEffect(() => {
    if (employee) {
      setFormData({
        firstName: employee.firstName || '',
        lastName: employee.lastName || '',
        emailId: employee.emailId || '',
        department: employee.department || '',
      });
    } else {
      setFormData({
        firstName: '',
        lastName: '',
        emailId: '',
        department: '',
      });
    }
  }, [employee, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-surface-900/40 backdrop-blur-sm">
      <div 
        className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden animate-in fade-in zoom-in-95 duration-200"
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
