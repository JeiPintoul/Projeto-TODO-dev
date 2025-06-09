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
    creationDate: "2024-02-15",
    dueDate: "2024-03-12",
    companyId: "3",
    managerId: "1",
    color: "blue",
    status: "progress",
    artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
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
              whenClick={() =>handleInternalLink("projects/")}
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
  