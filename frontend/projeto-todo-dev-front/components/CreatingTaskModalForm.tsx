"use client";

import { useState } from "react";
import TaskData from "@myTypes/tasks";
import { TaskColor, TaskPriority, TaskStatus } from "@myTypes/tasks";
import Modal from "./Modal";

type CreatingTaskModalFormProps = {
  isOpen: boolean;
  setIsOpen: (param: boolean) => void;
};

export default function CreatingTaskForm({
  isOpen,
  setIsOpen,
}: CreatingTaskModalFormProps) {
  const [formData, setFormData] = useState<TaskData>({
    id: "",
    projectId: "",
    name: "",
    description: "",
    dueDate: "",
    color: "yellow",
    status: "todo",
    priority:"low",
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

    const taskData: TaskData = {
      id: formData.id,
      projectId: formData.projectId,
      name: formData.name,
      description: formData.description,
      dueDate: formData.dueDate,
      color: formData.color as TaskColor,
      status: formData.status as TaskStatus,
      priority: formData.priority as TaskPriority,
    };

    setIsOpen(false);
    // TODO post json to API
    console.log("Posting it to API... ", JSON.stringify(taskData, null, 2));
  };

  return (
    <>
      <Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
        <form id="taskForm creating" onSubmit={handleSubmit}>
          <h2 id="modalTitle creating">New Task</h2>
          <input
            type="text"
            id="taskName creating"
            name="name"
            placeholder="Task Name"
            value={formData.name}
            onChange={handleChange}
            required
          />
          <textarea
            id="taskDescription creating"
            name="description"
            placeholder="Task Description"
            value={formData.description}
            onChange={handleChange}
            required
          />
          <input
            type="date"
            id="taskDate creating"
            name="date"
            value={formData.dueDate}
            onChange={handleChange}
            required
          />

          <select
            id="taskPriority creating"
            name="priority"
            value={formData.priority}
            onChange={handleChange}
          >
            <option value="low">Low</option>
            <option value="medium">Medium</option>
            <option value="high">High</option>
          </select>

          <select
            id="taskStatus creating"
            name="status"
            value={formData.status}
            onChange={handleChange}
          >
            <option value="todo">TODO</option>
            <option value="progress">Progress</option>
            <option value="done">Done</option>
          </select>

          <select
            id="taskColor creating"
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
