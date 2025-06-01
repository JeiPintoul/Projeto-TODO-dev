"use client";
import "@styles/NewProjectBtn.css";

import { MouseEvent } from "react";

export default function NewProjectBtn() {
  const openNewProjectModal = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    e.stopPropagation();
    const projectModal = document.getElementById("projectModal creating");
    if (projectModal) projectModal.style.display = "flex";
  };

  return (
    <button className="new-project-btn" onClick={openNewProjectModal}>
      + New Project
    </button>
  );
}
