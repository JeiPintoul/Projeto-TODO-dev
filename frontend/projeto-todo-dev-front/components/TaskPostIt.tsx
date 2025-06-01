"use client";
import "@styles/TaskPostIt.css";

import TaskData from "@myTypes/tasks";

export default function TaskPostIt({ taskInfo }: { taskInfo: TaskData }) {

  const handleView = (e: React.MouseEvent) => {
    // TODO: What should happen when clicking task?
  };

  const handleEdit = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    //editTask(taskInfo);
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    deleteProject(taskInfo);
  };

  // const editTask = (task: TaskData) => {
  //   const elements = {
  //     modal: document.getElementById(
  //       "projectModal editing"
  //     ) as HTMLElement | null,
  //     title: document.getElementById("modalTitle editing") as HTMLElement | null,
  //     name: document.getElementById("projectName editing") as HTMLInputElement | null,
  //     description: document.getElementById(
  //       "projectDescription editing"
  //     ) as HTMLTextAreaElement | null,
  //     date: document.getElementById("projectDate editing") as HTMLInputElement | null,
  //     color: document.getElementById(
  //       "projectColor editing"
  //     ) as HTMLSelectElement | null,
  //   };

  //   if (Object.values(elements).some((el) => el === null)) return;

  //   elements.name!.value = project.name;
  //   elements.description!.value = project.description;
  //   elements.date!.value = project.date;
  //   elements.color!.value = project.color;
  //   elements.modal!.style.display = "flex";
  // };

  const deleteProject = (taskInfo: TaskData) => {
    if (confirm("Do you really want to exclude this task?")) {
      //TODO: Delete it from API
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