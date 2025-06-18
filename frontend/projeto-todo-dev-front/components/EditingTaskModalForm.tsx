"use client";
import "@styles/TaskModalForm.css";
import { useState, useEffect } from "react";
import TaskData from "@myTypes/tasks";
import { TaskColor, TaskPriority, TaskStatus } from "@myTypes/tasks";
import Modal from "./Modal";

type EditingModalFormProps = {
  isOpen: boolean;
  setIsOpen: (param: boolean) => void;
  taskData: TaskData | null;
};

export default function EditingTaskModalForm({
  isOpen,
  setIsOpen,
  taskData,
}: EditingModalFormProps) {
  const [formData, setFormData] = useState<TaskData>({
    id: "",
    projectId: "",
    name: "",
    description: "",
    status: "todo",
    priority: "low",
    color: "yellow",
    startDate: "",
    dueDate: "",
    artefacts: ""
  });

  useEffect(() => {
    if (taskData) {
      setFormData(taskData);
    }
  }, [taskData]);

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

    const updatedTaskData: TaskData = {
      ...formData,
      color: formData.color as TaskColor,
      status: formData.status as TaskStatus,
      priority: formData.priority as TaskPriority
    };

    setIsOpen(false);
    // TODO put json to API
    console.log("Putting it to API... ", JSON.stringify(updatedTaskData, null, 2));
  };

  return (
    <Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
      <form id="taskForm" className="modal-form" onSubmit={handleSubmit}>
        <h2 className="modal-title">Edit Task</h2>
        
        <div className="form-group full-width">
          <input
            type="text"
            className="form-input"
            name="name"
            placeholder="Task Name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group full-width">
          <textarea
            className="form-textarea"
            name="description"
            placeholder="Task Description"
            value={formData.description}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group half-width">
            <label>Start Date</label>
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
            <label>Due Date</label>
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

        <div className="form-row">
          <div className="form-group third-width">
            <label>Priority</label>
            <select
              className="form-select"
              name="priority"
              value={formData.priority}
              onChange={handleChange}
            >
              <option value="low">Low</option>
              <option value="medium">Medium</option>
              <option value="high">High</option>
            </select>
          </div>

          <div className="form-group third-width">
            <label>Status</label>
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

          <div className="form-group third-width">
            <label>Color</label>
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

        <div className="form-group full-width">
          <label>Artefacts</label>
          <textarea
            className="form-textarea"
            name="artefacts"
            placeholder="Links or references (one per line)"
            value={formData.artefacts}
            onChange={handleChange}
          />
        </div>

        <button type="submit" className="form-button">Save Changes</button>
      </form>
    </Modal>
  );
}