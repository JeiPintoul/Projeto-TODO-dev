"use client"
import "@styles/ProjectsTasks.css";
import TaskData from "@myTypes/tasks";
import ProjectData from "@myTypes/projects";
import NewTaskBtn from "@components/CreateBtn";
import TaskPostIt from "@components/TaskPostIt";
import { useState } from "react";
import CreatingModalForm from "@components/CreatingTaskModalForm";
import EditingModalForm from "@components/EditingTaskModalForm";

interface Props {
  params: {
    id: string;
  };
}

export default function TasksPage({ params }: Props) {
  // TODO: Fetch project from API
  const project: ProjectData = {
    id: params.id,
    name: "Example Project",
    description: "Project Description",
    date: "2024-01-01",
    color: "blue",
  };

  // TODO: Fetch tasks from API
  const tasks: TaskData[] = [
    {
      id: "1",
      projectId: params.id,
      name: "Task 1",
      description: "First task",
      status: "todo",
      priority: "medium",
      dueDate: "2024-02-01",
      color: "green",
    },
    {
      id: "2",
      projectId: params.id,
      name: "Task 2",
      description: "Second task",
      status: "progress",
      priority: "high",
      dueDate: "2024-02-15",
      color: "green",
    },
  ];


    const [isCreateModalOpen, setIsCreateModalOpen] = useState<boolean>(false);
  
    const [isEditModalOpen, setIsEditModalOpen] = useState<boolean>(false);
    const [taskData, setTaskData] = useState<ProjectData | null>(null);
  
    return (
      <>
        <NewTaskBtn
          text="+ New Task"
          openModal={() => setIsCreateModalOpen(true)}
        />
  
        <h1>{project.name}</h1>
  
        <div className="tasks-container">
          {tasks.map((task) => (
            <TaskPostIt
              key={task.id}
              taskInfo={task}
              setIsOpen={setIsEditModalOpen}
              setTaskData={setTaskData}
            />
          ))}
        </div>
  
        <CreatingModalForm
          isOpen={isCreateModalOpen}
          setIsOpen={setIsCreateModalOpen}
        />
        <EditingModalForm
          isOpen={isEditModalOpen}
          setIsOpen={setIsEditModalOpen}
          taskData={taskData}
        />
      </>
    );
  }
  