"use client";
import "@styles/ProjectPostIt.css";

import ProjectData from "@myTypes/projects";
import { useRouter } from "next/navigation";

export default function ProjectPostIt({ projectInfo }: { projectInfo: ProjectData }) {
  const router = useRouter();

  const handleView = (e: React.MouseEvent) => {
    e.preventDefault();
    router.push(`/projects/${projectInfo.id}`);
  };

  const handleEdit = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    editProject(projectInfo);
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    deleteProject(projectInfo);
  };

  const editProject = (project: ProjectData) => {
    const elements = {
      modal: document.getElementById(
        "projectModal editing"
      ) as HTMLElement | null,
      title: document.getElementById("modalTitle editing") as HTMLElement | null,
      name: document.getElementById("projectName editing") as HTMLInputElement | null,
      description: document.getElementById(
        "projectDescription editing"
      ) as HTMLTextAreaElement | null,
      date: document.getElementById("projectDate editing") as HTMLInputElement | null,
      color: document.getElementById(
        "projectColor editing"
      ) as HTMLSelectElement | null,
    };

    if (Object.values(elements).some((el) => el === null)) return;

    elements.name!.value = project.name;
    elements.description!.value = project.description;
    elements.date!.value = project.date;
    elements.color!.value = project.color;
    elements.modal!.style.display = "flex";
  };

  const deleteProject = (projectInfo: ProjectData) => {
    if (confirm("Do you really want to exclude this project?")) {
      //TODO delete it from API
      console.log(`Deleting ${projectInfo.id} in API...`);
    }
  };

  return (
    <div className={`post-it ${projectInfo.color}`} onClick={handleView}>
      <div onClick={handleView} className="post-it-link">
        <h3>{projectInfo.name}</h3>
        <p>{projectInfo.description}</p>
        <div className="date">
          {new Date(projectInfo.date).toLocaleDateString("en")}
        </div>
      </div>
      <div className="actions">
        <button className="edit-btn" onClick={handleEdit} title="Edit">
          ✏️
        </button>
        <button className="delete-btn" onClick={handleDelete} title="Delete">
          🗑️
        </button>
      </div>
    </div>
  );
}