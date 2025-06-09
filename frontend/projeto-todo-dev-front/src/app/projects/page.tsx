"use client";
import "@styles/ProjectsTasks.css";

import { useState } from "react";

import ProjectPostIt from "@components/ProjectPostIt";
import ProjectData from "@myTypes/projects";
import Btn from "@components/Btn";
import CreatingModalForm from "@components/CreatingProjectModalForm";
import EditingModalForm from "@components/EditingProjectModalForm";
import { useRouter } from "next/navigation";

export default function ProjectsPage() {
  // TODO: Fetch projects from API
  const projects: ProjectData[] = [
    {
      id: "1",
      name: "SmartHome Dashboard",
      description: "A web-based dashboard for monitoring and controlling smart home devices.",
      color: "blue",
      status: "progress",
      creationDate: "2024-01-15",
      dueDate: "2024-10-01",
      companyId: "1",
      managerId: "4",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    },
    {
      id: "2",
      name: "Health Tracker App",
      description: "Mobile app to log meals, workouts, and daily health metrics.",
      color: "green",
      status: "done",
      creationDate: "2023-09-01",
      dueDate: "2023-12-15",
      companyId: "1",
      managerId: "3",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    },
    {
      id: "3",
      name: "AI Chatbot",
      description: "Customer support chatbot using natural language processing.",
      color: "purple",
      status: "progress",
      creationDate: "2024-03-10",
      dueDate: "2024-05-20",
      companyId: "1",
      managerId: "3",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    },
    {
      id: "4",
      name: "Portfolio Website",
      description: "A modern portfolio site to showcase personal projects and experience.",
      color: "orange",
      status: "done",
      creationDate: "2023-06-20",
      dueDate: "2023-08-10",
      companyId: "2",
      managerId: "2",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    },
    {
      id: "5",
      name: "Task Manager API",
      description: "A RESTful API for managing daily tasks and projects.",
      color: "green",
      status: "todo",
      creationDate: "2024-01-05",
      dueDate: "2024-02-18",
      companyId: "2",
      managerId: "2",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    },
    {
      id: "6",
      name: "E-commerce Platform",
      description: "A full-stack online store with cart, payment, and admin panel.",
      color: "purple",
      status: "progress",
      creationDate: "2024-04-01",
      dueDate: "2024-06-30",
      companyId: "3",
      managerId: "1",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    },
    {
      id: "7",
      name: "Weather Visualizer",
      description: "Interactive data visualization app for historical and current weather data.",
      color: "pink",
      status: "todo",
      creationDate: "2024-02-15",
      dueDate: "2024-03-12",
      companyId: "3",
      managerId: "1",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
    }
  ];

  // TODO: Fetch Project workers name from API
  const projectWorkers = [
    { projectId: "1", workers: ["Lucas Silva", "Ana Costa", "Carlos Oliveira"] },
    { projectId: "2", workers: ["Mariana Santos", "Pedro Alves"] },
    { projectId: "3", workers: ["Tiago Pereira", "Beatriz Lima", "Rafael Souza"] },
    { projectId: "4", workers: ["Juliana Martins"] },
    { projectId: "5", workers: ["Fernando Gomes", "Patrícia Rocha"] },
    { projectId: "6", workers: ["Gustavo Henrique", "Amanda Ferreira", "Diego Castro"] },
    { projectId: "7", workers: ["Camila Neves", "Roberto Andrade"] }
  ];

  // TODO: Fetch Company Name from API
  const companies = [
    { id: "1", name: "Tech Innovations Inc." },
    { id: "2", name: "Creative Solutions Ltd." },
    { id: "3", name: "Digital Futures Corp." }
  ];

  // TODO: Fetch Manager name ID API
  const managers = [
    { id: "1", managers: ["Alex Johnson", "Michael Douglas"] },
    { id: "2", managers: ["Sarah Williams"] },
    { id: "3", managers: ["Michael Brown"] },
    { id: "4", managers: ["Emily Davis", "Carl Johnson"] }
  ];

  const [isCreateModalOpen, setIsCreateModalOpen] = useState<boolean>(false);

  const [isEditModalOpen, setIsEditModalOpen] = useState<boolean>(false);
  const [projectData, setProjectData] = useState<ProjectData | null>(null);

  const router = useRouter();

  const handleInternalLink = (link: string) => {
    router.push(`/${link}`);
  };

  return (
    <>
      <header>
        <Btn
          text="Home"
          whenClick={() => handleInternalLink("/")}
        />
      <h1 className="title">My Projects</h1>
        <Btn
          text="+ New Project"
          whenClick={() => setIsCreateModalOpen(true)}
        />
      </header>

      <div className="projects-container">
        {projects.map((project) => (
          <ProjectPostIt
            key={project.id}
            projectInfo={project}
            setIsOpen={setIsEditModalOpen}
            setProjectData={setProjectData}
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
        projectData={projectData}
      />
    </>
  );
}
