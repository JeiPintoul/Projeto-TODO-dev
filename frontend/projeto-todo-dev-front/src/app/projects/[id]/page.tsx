"use client"
import "@styles/ProjectsTasks.css";
import TaskData from "@myTypes/tasks";
import ProjectData from "@myTypes/projects";
import Btn from "@components/Btn";
import TaskPostIt from "@components/TaskPostIt";
import { useState } from "react";
import CreatingModalForm from "@components/CreatingTaskModalForm";
import EditingModalForm from "@components/EditingTaskModalForm";
import { useRouter } from "next/navigation";
import ViewTaskModal from "@components/ViewTaskModal";

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
    color: "blue",
    status: "progress",
    creationDate: "2024-02-15",
    startDate: "2024-02-20",
    dueDate: "2024-03-12",
    artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    companies: [],
    managers: [],
    tasks: [],
    workers: []
  };

  // TODO: Fetch tasks from API
  const tasks: TaskData[] = [
    {
      id: "1",
      projectId: params.id,
      name: "UI Design",
      description: "Create wireframes and mockups for the dashboard",
      status: "todo",
      priority: "medium",
      color: "green",
      startDate: "2024-02-20",
      dueDate: "2024-02-28",
      artefacts: "Figma link: www.figma.com/example"
    },
    {
      id: "2",
      projectId: params.id,
      name: "API Development",
      description: "Implement backend endpoints for user management",
      status: "progress",
      priority: "high",
      color: "blue",
      startDate: "2024-02-25",
      dueDate: "2024-03-05",
      artefacts: "Swagger docs: api.example.com/docs"
    },
    {
      id: "3",
      projectId: params.id,
      name: "Database Schema",
      description: "Design and implement database structure",
      status: "done",
      priority: "high",
      color: "purple",
      startDate: "2024-02-15",
      dueDate: "2024-02-18",
      artefacts: "ER Diagram: drive.google.com/example"
    }
  ];

  const [isCreateModalOpen, setIsCreateModalOpen] = useState<boolean>(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState<boolean>(false);
  const [taskData, setTaskData] = useState<TaskData | null>(null);
  const [isViewModalOpen, setIsViewModalOpen] = useState<boolean>(false);

  const router = useRouter();

  const handleInternalLink = (link: string) => {
    router.push(`/${link}`);
  };

  return (
    <>
      <header>
        <Btn
            text="Projects"
            whenClick={() => handleInternalLink("projects/")}
          />
        <h1 className="title">{project.name}</h1>
        <Btn
          text="+ New Task"
          whenClick={() => setIsCreateModalOpen(true)}
        />
      </header>

      <div className="tasks-container">
        {tasks.map((task) => (
          <TaskPostIt
            key={task.id}
            taskInfo={task}
            setIsOpen={setIsEditModalOpen}
            setIsViewOpen={setIsViewModalOpen}
            setTaskData={setTaskData}
          />
        ))}
      </div>

      <CreatingModalForm
        isOpen={isCreateModalOpen}
        setIsOpen={setIsCreateModalOpen}
        projectId={params.id}
      />
      <EditingModalForm
        isOpen={isEditModalOpen}
        setIsOpen={setIsEditModalOpen}
        taskData={taskData}
      />
      <ViewTaskModal
        isOpen={isViewModalOpen}
        setIsOpen={setIsViewModalOpen}
        taskData={taskData}
      />
    </>
  );
}