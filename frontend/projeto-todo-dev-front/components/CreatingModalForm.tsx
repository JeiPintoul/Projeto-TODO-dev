"use client";
import "@styles/ModalForm.css";

import { useRef, useState } from "react";
import ProjectData from "@myTypes/projects";
import { ProjectColor } from "@myTypes/projects";

export default function CreatingModalForm() {
  const modalRef = useRef<HTMLDivElement>(null);
  const [currentProjectId, setCurrentProjectId] = useState<string | null>(null);
  const [formData, setFormData] = useState<ProjectData>({
    id: "",
    name: "",
    description: "",
    date: "",
    color: "yellow",
  });

  const closeModal = (): void => {
    if (modalRef.current) {
      modalRef.current.style.display = "none";
    }
    setCurrentProjectId(null);
  };

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

  const handleSubmit = (e: React.FormEvent): void => {
    e.preventDefault();

    const projectData: ProjectData = {
      id: formData.id,
      name: formData.name,
      description: formData.description,
      date: formData.date,
      color: formData.color as ProjectColor,
    };

    if (modalRef.current) {
      modalRef.current.style.display = "none";
    }

    // TODO post json to API
    console.log("Posting it to API... ", JSON.stringify(projectData, null, 2));
  };

  return (
    <div id="projectModal creating" className="modal" ref={modalRef}>
      <div className="modal-content">
        <span className="close-btn" onClick={closeModal}>
          &times;
        </span>
        <form id="projectForm creating" onSubmit={handleSubmit}>
          <h2 id="modalTitle creating">New Project</h2>
          <input
            type="text"
            id="projectName creating"
            name="name"
            placeholder="Project Name"
            value={formData.name}
            onChange={handleChange}
            required
          />
          <textarea
            id="projectDescription creating"
            name="description"
            placeholder="Project Description"
            value={formData.description}
            onChange={handleChange}
            required
          />
          <input
            type="date"
            id="projectDate creating"
            name="date"
            value={formData.date}
            onChange={handleChange}
            required
          />
          <select
            id="projectColor creating"
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
          <button type="submit" onSubmit={handleSubmit}>
            Save
          </button>
        </form>
      </div>
    </div>
  );
}
