"use client";
import "@styles/TaskPostIt.css";

import TaskData from "@myTypes/tasks";
import { useRouter } from "next/navigation";

export default function TaskPostIt({ taskInfo, setIsOpen, setTaskData }: { taskInfo: TaskData, setIsOpen: (param: boolean) => void, setTaskData: (data: TaskData | null) => void }) {
  
  const router = useRouter();

  const handleView = (e: React.MouseEvent) => {
    e.preventDefault();
    router.push(`/projects/${taskInfo.id}`);
    // TODO arrumar quando clicar
  };

  const handleEdit = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setTaskData(taskInfo);
    setIsOpen(true);
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (confirm("Do you really want to exclude this task?")) {
      setIsOpen(false);
      //TODO delete it from API
      console.log(`Deleting ${taskInfo.id} in API...`);
    }
  };

  return (
    <div className={`post-it ${taskInfo.color}`} onClick={handleView}>
      <div onClick={handleView} className="post-it-link">
        <h3>{taskInfo.name}</h3>
        <p>{taskInfo.description}</p>
        <div className="date">
          {new Date(taskInfo.dueDate).toLocaleDateString("en")}
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