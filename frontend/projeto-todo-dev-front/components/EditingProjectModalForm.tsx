"use client";
import "@styles/ModalForm.css";
import { useState, useEffect } from "react";
import ProjectData from "@myTypes/projects";
import Modal from "./Modal";

type EditingProjectModalFormProps = {
  isOpen: boolean;
  setIsOpen: (param: boolean) => void;
  projectData: ProjectData | null;
};

export default function EditingProjectModalForm({
  isOpen,
  setIsOpen,
  projectData,
}: EditingProjectModalFormProps) {
  const [formData, setFormData] = useState<ProjectData>(
    projectData || {
      id: "",
      name: "",
      description: "",
      color: "yellow",
      status: "todo",
      creationDate: "",
      startDate: "",
      dueDate: "",
      artefacts: "",
      companies: [],
      managers: [],
      tasks: [],
      workers: []
    }
  );

  const [selectedCompanies, setSelectedCompanies] = useState<string[]>([]);
  const [selectedManagers, setSelectedManagers] = useState<string[]>([]);
  const [selectedWorkers, setSelectedWorkers] = useState<string[]>([]);

  useEffect(() => {
    if (projectData) {
      setFormData(projectData);
      setSelectedCompanies(projectData.companies.map(c => c.id));
      setSelectedManagers(projectData.managers.map(m => m.id));
      setSelectedWorkers(projectData.workers.map(w => w.id));
    }
  }, [projectData]);

  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleMultiSelectChange = (e: React.ChangeEvent<HTMLSelectElement>, type: 'companies' | 'managers' | 'workers') => {
    const selectedOptions = Array.from(e.target.selectedOptions, option => option.value);
    
    if (type === 'companies') {
      setSelectedCompanies(selectedOptions);
    } else if (type === 'managers') {
      setSelectedManagers(selectedOptions);
    } else {
      setSelectedWorkers(selectedOptions);
    }
  };

  const handleSubmit = (e: React.FormEvent): void => {
    e.preventDefault();

    const updatedProjectData: ProjectData = {
      ...formData,
      companies: allCompanies.filter(company => selectedCompanies.includes(company.id)),
      managers: allManagers.filter(manager => selectedManagers.includes(manager.id)),
      workers: allWorkers.filter(worker => selectedWorkers.includes(worker.id))
    };

    setIsOpen(false);
    // TODO put json to API
    console.log("Putting it to API... ", JSON.stringify(updatedProjectData, null, 2));
  };

  // TODO: Fetch Project workers from API
  const allWorkers = [
    { id: "5", name: "Alice Johnson", cpf: "987.654.321-00", phone: "+55 11 91234-5678", email: "alice.j@tech.com", password: "alicepass123", isManager: false },
    { id: "6", name: "Bob Wilson", cpf: "789.123.456-00", phone: "+55 11 92345-6789", email: "bob.w@tech.com", password: "bobpass123", isManager: false },
    { id: "7", name: "David Lee", cpf: "321.654.987-00", phone: "+55 11 93456-7890", email: "david.l@tech.com", password: "davidpass123", isManager: false },
    { id: "8", name: "Emma Davis", cpf: "654.321.987-00", phone: "+55 11 94567-1234", email: "emma.d@creative.com", password: "emmapass123", isManager: false },
    { id: "9", name: "Frank Miller", cpf: "987.123.456-00", phone: "+55 11 96789-0123", email: "frank.m@creative.com", password: "frankpass123", isManager: false },
    { id: "10", name: "Grace Taylor", cpf: "123.987.654-00", phone: "+55 11 97890-1234", email: "grace.t@digital.com", password: "gracepass123", isManager: false },
    { id: "11", name: "Henry Brown", cpf: "456.123.789-00", phone: "+55 11 98901-2345", email: "henry.b@digital.com", password: "henrypass123", isManager: false }
  ];

  // TODO: Fetch Company from API
  const allCompanies = [
    { id: "1", name: "Tech Solutions Inc.", description: "Technology consulting and software development" },
    { id: "2", name: "Creative Designs Ltd.", description: "Web and graphic design agency" },
    { id: "3", name: "Digital Commerce SA", description: "E-commerce solutions provider" }
  ];

  // TODO: Fetch Managers from API
  const allManagers = [
    { id: "1", name: "Ana Silva", cpf: "345.678.901-22", phone: "+55 11 96789-0123", email: "ana.s@digital.com", password: "anasecure123", isManager: true },
    { id: "2", name: "Carlos Oliveira", cpf: "234.567.890-11", phone: "+55 11 95678-9012", email: "carlos.o@creative.com", password: "carlos123secure", isManager: true },
    { id: "3", name: "Maria Garcia", cpf: "456.789.123-00", phone: "+55 11 94567-8901", email: "maria.g@tech.com", password: "mariasecure123", isManager: true },
    { id: "4", name: "John Smith", cpf: "123.456.789-00", phone: "+55 11 98765-4321", email: "john.smith@tech.com", password: "securepassword123", isManager: true }
  ];

  return (
    <Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
      <form id="projectForm" className="modal-form" onSubmit={handleSubmit}>
        <h2 className="modal-title">Edit Project</h2>
        
        <div className="form-group full-width">
          <input
            type="text"
            className="form-input"
            name="name"
            placeholder="Project Name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group full-width">
          <textarea
            className="form-textarea"
            name="description"
            placeholder="Project Description"
            value={formData.description}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group half-width">
            <select
              className="form-select"
              name="status"
              value={formData.status}
              onChange={handleChange}
            >
              <option value="todo">To Do</option>
              <option value="progress">In Progress</option>
              <option value="done">Done</option>
            </select>
          </div>

          <div className="form-group half-width">
            <select
              className="form-select"
              name="color"
              value={formData.color}
              onChange={handleChange}
            >
              <option value="yellow">Yellow</option>
              <option value="pink">Pink</option>
              <option value="blue">Blue</option>
              <option value="green">Green</option>
              <option value="purple">Purple</option>
              <option value="orange">Orange</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div className="form-group half-width">
            <input
              type="date"
              className="form-input"
              name="startDate"
              value={formData.startDate}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group half-width">
            <input
              type="date"
              className="form-input"
              name="dueDate"
              value={formData.dueDate}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="form-group full-width">
          <textarea
            className="form-textarea"
            name="artefacts"
            placeholder="Artefacts"
            value={formData.artefacts}
            onChange={handleChange}
          />
        </div>

        <p className="p-info">Companies</p>
        <div className="form-group full-width">
          <select 
            className="form-select multiple"
            name="companies"
            onChange={(e) => handleMultiSelectChange(e, 'companies')}
            multiple
            required
            value={selectedCompanies}
          >
            {allCompanies.map((company) => (
              <option key={company.id} value={company.id}>{company.name}</option>
            ))}
          </select>
        </div>
        <p className="p-info">Managers</p>
        <div className="form-group full-width">
          <select 
            className="form-select multiple"
            name="managers"
            onChange={(e) => handleMultiSelectChange(e, 'managers')}
            multiple
            required
            value={selectedManagers}
          >
            {allManagers.map((manager) => (
              <option key={manager.id} value={manager.id}>{manager.name}</option>
            ))}
          </select>
        </div>
        <p className="p-info">Workers</p>
        <div className="form-group full-width">
          <select 
            className="form-select multiple"
            name="workers"
            onChange={(e) => handleMultiSelectChange(e, 'workers')}
            multiple
            value={selectedWorkers}
          >
            {allWorkers.map((worker) => (
              <option key={worker.id} value={worker.id}>{worker.name}</option>
            ))}
          </select>
        </div>

        <button type="submit" className="form-button">Save Changes</button>
      </form>
    </Modal>
  );
}