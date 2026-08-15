import React from 'react';
import { Edit2, Trash2, Mail, Building, KeyRound, Lock } from 'lucide-react';

export default function EmployeeList({ employees, onEdit, onDelete }) {
  if (!employees || employees.length === 0) {
    return (
      <div className="bg-white rounded-2xl border border-surface-200 p-12 text-center shadow-xs">
        <div className="inline-flex items-center justify-center w-12 h-12 rounded-full bg-surface-100 text-surface-500 mb-4">
          <Building size={24} />
        </div>
        <h3 className="text-lg font-semibold text-surface-900">No employees found</h3>
        <p className="mt-1 text-surface-500 max-w-sm mx-auto text-sm">
          Get started by adding a new team member to your directory.
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Mobile Card Grid (Visible on small mobile screens < 768px) */}
      <div className="grid grid-cols-1 gap-4 md:hidden">
        {employees.map((employee) => (
          <div key={employee.id} className="bg-white border border-surface-200 rounded-2xl p-5 shadow-xs flex flex-col gap-4">
            <div className="flex items-start justify-between">
              <div className="flex items-center gap-3">
                <div className="h-11 w-11 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-bold text-sm shadow-xs">
                  {employee.firstName?.charAt(0)}{employee.lastName?.charAt(0)}
                </div>
                <div>
                  <h4 className="text-base font-semibold text-surface-900">
                    {employee.firstName} {employee.lastName}
                  </h4>
                  <span className="inline-flex items-center mt-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-surface-100 text-surface-700 border border-surface-200">
                    {employee.department || 'N/A'}
                  </span>
                </div>
              </div>

              <div className="flex items-center gap-1">
                <button
                  onClick={() => onEdit(employee)}
                  className="p-2 text-surface-500 hover:text-brand-600 hover:bg-brand-50 rounded-lg transition-colors"
                  title="Edit"
                >
                  <Edit2 size={18} />
                </button>
                <button
                  onClick={() => onDelete(employee.id)}
                  className="p-2 text-surface-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                  title="Delete"
                >
                  <Trash2 size={18} />
                </button>
              </div>
            </div>

            <div className="pt-3 border-t border-surface-100 flex flex-col sm:flex-row sm:items-center justify-between gap-2 text-xs">
              <div className="flex items-center gap-2 text-surface-600 truncate">
                <Mail size={15} className="text-surface-400 shrink-0" />
                <span className="truncate">{employee.emailId}</span>
              </div>

              <div>
                {employee.loginEnabled ? (
                  <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
                    <KeyRound size={12} /> Login Enabled
                  </span>
                ) : (
                  <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium bg-surface-100 text-surface-500 border border-surface-200">
                    <Lock size={12} /> Login Disabled
                  </span>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Desktop Data Table (Visible on screens >= 768px) */}
      <div className="hidden md:block bg-white border border-surface-200 rounded-2xl shadow-xs overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-surface-50 border-b border-surface-200">
                <th className="px-6 py-4 text-xs font-semibold text-surface-500 uppercase tracking-wider">Name</th>
                <th className="px-6 py-4 text-xs font-semibold text-surface-500 uppercase tracking-wider">Contact</th>
                <th className="px-6 py-4 text-xs font-semibold text-surface-500 uppercase tracking-wider">Department</th>
                <th className="px-6 py-4 text-xs font-semibold text-surface-500 uppercase tracking-wider">Login Access</th>
                <th className="px-6 py-4 text-xs font-semibold text-surface-500 uppercase tracking-wider text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-surface-200">
              {employees.map((employee) => (
                <tr key={employee.id} className="hover:bg-surface-50/80 transition-colors duration-150 group">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center gap-3">
                      <div className="h-10 w-10 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-semibold text-sm shadow-xs">
                        {employee.firstName?.charAt(0)}{employee.lastName?.charAt(0)}
                      </div>
                      <div>
                        <div className="text-sm font-semibold text-surface-900">
                          {employee.firstName} {employee.lastName}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center gap-2 text-sm text-surface-600">
                      <Mail size={16} className="text-surface-400" />
                      {employee.emailId}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-surface-100 text-surface-700 border border-surface-200">
                      {employee.department || 'N/A'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {employee.loginEnabled ? (
                      <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
                        <KeyRound size={12} /> Enabled
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium bg-surface-100 text-surface-500 border border-surface-200">
                        <Lock size={12} /> Disabled
                      </span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <div className="flex items-center justify-end gap-1 opacity-90 group-hover:opacity-100 transition-opacity">
                      <button
                        onClick={() => onEdit(employee)}
                        className="p-2 text-surface-400 hover:text-brand-600 hover:bg-brand-50 rounded-lg transition-colors"
                        title="Edit"
                      >
                        <Edit2 size={18} />
                      </button>
                      <button
                        onClick={() => onDelete(employee.id)}
                        className="p-2 text-surface-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Delete"
                      >
                        <Trash2 size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}


