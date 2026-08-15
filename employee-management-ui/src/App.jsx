import React, { useState, useEffect } from 'react';
import { Plus, Loader2 } from 'lucide-react';
import Layout from './components/Layout';
import EmployeeList from './components/EmployeeList';
import EmployeeFormModal from './components/EmployeeFormModal';
import Login from './components/Login';
import { getEmployees, createEmployee, updateEmployee, deleteEmployee } from './services/api';

function App() {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [currentUser, setCurrentUser] = useState(() => {
    const savedUser = localStorage.getItem('currentUser');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);

  const fetchEmployees = async () => {
    if (!token) return;
    try {
      setLoading(true);
      const response = await getEmployees();
      setEmployees(Array.isArray(response.data) ? response.data : []);
      setError(null);
    } catch (err) {
      console.error('Error fetching employees:', err);
      setError('Failed to load employees. Please ensure the backend is running and you are authenticated.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const handleUnauthorized = () => {
      setToken(null);
      setCurrentUser(null);
    };

    window.addEventListener('auth-unauthorized', handleUnauthorized);
    return () => window.removeEventListener('auth-unauthorized', handleUnauthorized);
  }, []);

  useEffect(() => {
    if (token) {
      fetchEmployees();
    }
  }, [token]);

  const handleLoginSuccess = ({ token: newToken, employee }) => {
    setToken(newToken);
    setCurrentUser(employee);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    setToken(null);
    setCurrentUser(null);
  };

  const handleOpenModal = (employee = null) => {
    setEditingEmployee(employee);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingEmployee(null);
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingEmployee) {
        await updateEmployee(editingEmployee.id, formData);
      } else {
        await createEmployee(formData);
      }
      handleCloseModal();
      fetchEmployees();
    } catch (err) {
      console.error('Error saving employee:', err);
      alert('Failed to save employee. Please try again.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this employee? This action cannot be undone.')) {
      try {
        await deleteEmployee(id);
        fetchEmployees();
      } catch (err) {
        console.error('Error deleting employee:', err);
        alert('Failed to delete employee.');
      }
    }
  };

  if (!token) {
    return <Login onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <Layout currentUser={currentUser} onLogout={handleLogout}>
      <div className="sm:flex sm:items-center sm:justify-between mb-8">
        <div>
          <h2 className="text-2xl font-bold text-surface-900">Team Members</h2>
          <p className="mt-1 text-sm text-surface-500">
            A list of all the employees in your organization including their name, title, email and role.
          </p>
        </div>
        <div className="mt-4 sm:mt-0">
          <button
            onClick={() => handleOpenModal()}
            className="btn-primary gap-2"
          >
            <Plus size={18} />
            Add Member
          </button>
        </div>
      </div>

      {error && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm">
          {error}
        </div>
      )}

      {loading ? (
        <div className="flex flex-col items-center justify-center py-24">
          <Loader2 size={32} className="text-brand-500 animate-spin mb-4" />
          <p className="text-surface-500 font-medium">Loading team directory...</p>
        </div>
      ) : (
        <EmployeeList 
          employees={employees} 
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
        />
      )}

      <EmployeeFormModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSubmit={handleSubmit}
        employee={editingEmployee}
      />
    </Layout>
  );
}

export default App;

