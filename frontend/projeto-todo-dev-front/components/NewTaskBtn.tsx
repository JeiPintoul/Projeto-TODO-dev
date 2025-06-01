"use client";
import "@styles/NewTaskBtn.css";

import { MouseEvent } from "react";

export default function NewTaskBtn() {
  const openNewTaskModal = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    e.stopPropagation();
    const projectModal = document.getElementById("projectModal creating");
    if (projectModal) projectModal.style.display = "flex";
  };

  return (
    <button className="new-task-btn" onClick={openNewTaskModal}>
      + New Task
    </button>
  );
}
