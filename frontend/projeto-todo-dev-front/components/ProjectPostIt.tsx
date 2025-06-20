"use client";
import "@styles/ProjectPostIt.css";

import ProjectData from "@myTypes/projects";
import { useRouter } from "next/navigation";

export default function ProjectPostIt({ projectInfo, setIsOpen, setProjectData }: { projectInfo: ProjectData, setIsOpen: (param: boolean) => void, setProjectData: (data: ProjectData | null) => void }) {
  
  const router = useRouter();

  const handleView = (e: React.MouseEvent) => {
    e.preventDefault();
    router.push(`/projects/${projectInfo.id}`);
  };

  const handleEdit = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setProjectData(projectInfo);
    setIsOpen(true);
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (confirm("Do you really want to exclude this project?")) {
      setIsOpen(false);
      //TODO delete it from API
      console.log(`Deleting ${projectInfo.id} in API...`);
    }
  };

  if(!setProjectData) return null;

  return (
    <div className={`post-it ${projectInfo.color}`} onClick={handleView}>
      <div onClick={handleView} className="post-it-link">
        <h3>{projectInfo.name}</h3>
        <p>{projectInfo.description}</p>
        <div className="date">
          {new Date(projectInfo.dueDate).toLocaleDateString("en")}
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