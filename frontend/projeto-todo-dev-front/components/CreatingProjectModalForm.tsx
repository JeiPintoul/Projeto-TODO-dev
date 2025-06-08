"use client";

import { useState } from "react";
import ProjectData from "@myTypes/projects";
import { ProjectColor } from "@myTypes/projects";
import Modal from "./Modal";

type CreatingProjectModalFormProps = {
  isOpen: boolean;
  setIsOpen: (param: boolean) => void;
};

export default function CreatingModalForm({
  isOpen,
  setIsOpen,
}: CreatingProjectModalFormProps) {
  const [formData, setFormData] = useState<ProjectData>({
    id: "",
    name: "",
    description: "",
    date: "",
    color: "yellow",
  });

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

    setIsOpen(false);
    // TODO post json to API
    console.log("Posting it to API... ", JSON.stringify(projectData, null, 2));
  };

  return (
    <>
      <Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
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
      </Modal>
    </>
  );
}
