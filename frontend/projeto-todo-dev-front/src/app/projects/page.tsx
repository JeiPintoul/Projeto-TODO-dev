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
      startDate: "2024-02-01",
      dueDate: "2024-10-01",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "1",
          name: "Tech Solutions Inc.",
          description: "Technology consulting and software development"
        }
      ],
      managers: [
        {
          id: "4",
          name: "John Smith",
          cpf: "123.456.789-00",
          phone: "+55 11 98765-4321",
          email: "john.smith@tech.com",
          password: "securepassword123",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "101",
          projectId: "1",
          name: "UI Design",
          description: "Create dashboard interface mockups",
          status: "done",
          priority: "medium",
          color: "green",
          startDate: "2024-02-01",
          dueDate: "2024-02-15",
          artefacts: "Figma link: www.figma.com/smarthome"
        }
      ],
      workers: [
        {
          id: "5",
          name: "Alice Johnson",
          cpf: "987.654.321-00",
          phone: "+55 11 91234-5678",
          email: "alice.j@tech.com",
          password: "alicepass123",
          isManager: false
        }
      ]
    },
    {
      id: "2",
      name: "Health Tracker App",
      description: "Mobile app to log meals, workouts, and daily health metrics.",
      color: "green",
      status: "done",
      creationDate: "2023-09-01",
      startDate: "2023-09-15",
      dueDate: "2023-12-15",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "1",
          name: "Tech Solutions Inc.",
          description: "Technology consulting and software development"
        }
      ],
      managers: [
        {
          id: "3",
          name: "Maria Garcia",
          cpf: "456.789.123-00",
          phone: "+55 11 94567-8901",
          email: "maria.g@tech.com",
          password: "mariasecure123",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "201",
          projectId: "2",
          name: "Backend Development",
          description: "Implement API for health data",
          status: "done",
          priority: "high",
          color: "blue",
          startDate: "2023-09-15",
          dueDate: "2023-10-30",
          artefacts: "Swagger docs: api.healthtracker.com/docs"
        }
      ],
      workers: [
        {
          id: "6",
          name: "Bob Wilson",
          cpf: "789.123.456-00",
          phone: "+55 11 92345-6789",
          email: "bob.w@tech.com",
          password: "bobpass123",
          isManager: false
        }
      ]
    },
    {
      id: "3",
      name: "AI Chatbot",
      description: "Customer support chatbot using natural language processing.",
      color: "purple",
      status: "progress",
      creationDate: "2024-03-10",
      startDate: "2024-03-20",
      dueDate: "2024-05-20",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "1",
          name: "Tech Solutions Inc.",
          description: "Technology consulting and software development"
        }
      ],
      managers: [
        {
          id: "3",
          name: "Maria Garcia",
          cpf: "456.789.123-00",
          phone: "+55 11 94567-8901",
          email: "maria.g@tech.com",
          password: "mariasecure123",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "301",
          projectId: "3",
          name: "NLP Model Training",
          description: "Train the AI model with customer service data",
          status: "progress",
          priority: "high",
          color: "purple",
          startDate: "2024-03-20",
          dueDate: "2024-04-15",
          artefacts: "Jupyter notebook: github.com/tech/chatbot-nlp"
        }
      ],
      workers: [
        {
          id: "7",
          name: "David Lee",
          cpf: "321.654.987-00",
          phone: "+55 11 93456-7890",
          email: "david.l@tech.com",
          password: "davidpass123",
          isManager: false
        }
      ]
    },
    {
      id: "4",
      name: "Portfolio Website",
      description: "A modern portfolio site to showcase personal projects and experience.",
      color: "orange",
      status: "done",
      creationDate: "2023-06-20",
      startDate: "2023-07-01",
      dueDate: "2023-08-10",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "2",
          name: "Creative Designs Ltd.",
          description: "Web and graphic design agency"
        }
      ],
      managers: [
        {
          id: "2",
          name: "Carlos Oliveira",
          cpf: "234.567.890-11",
          phone: "+55 11 95678-9012",
          email: "carlos.o@creative.com",
          password: "carlos123secure",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "401",
          projectId: "4",
          name: "Responsive Layout",
          description: "Implement mobile-friendly design",
          status: "done",
          priority: "medium",
          color: "orange",
          startDate: "2023-07-01",
          dueDate: "2023-07-20",
          artefacts: "CSS styles: github.com/creative/portfolio/css"
        }
      ],
      workers: [
        {
          id: "8",
          name: "Emma Davis",
          cpf: "654.321.987-00",
          phone: "+55 11 94567-1234",
          email: "emma.d@creative.com",
          password: "emmapass123",
          isManager: false
        }
      ]
    },
    {
      id: "5",
      name: "Task Manager API",
      description: "A RESTful API for managing daily tasks and projects.",
      color: "green",
      status: "todo",
      creationDate: "2024-01-05",
      startDate: "2024-01-20",
      dueDate: "2024-02-18",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "2",
          name: "Creative Designs Ltd.",
          description: "Web and graphic design agency"
        }
      ],
      managers: [
        {
          id: "2",
          name: "Carlos Oliveira",
          cpf: "234.567.890-11",
          phone: "+55 11 95678-9012",
          email: "carlos.o@creative.com",
          password: "carlos123secure",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "501",
          projectId: "5",
          name: "Database Schema",
          description: "Design the database structure",
          status: "todo",
          priority: "low",
          color: "yellow",
          startDate: "2024-01-20",
          dueDate: "2024-01-25",
          artefacts: "ER Diagram: drive.google.com/db-schema"
        }
      ],
      workers: [
        {
          id: "9",
          name: "Frank Miller",
          cpf: "987.123.456-00",
          phone: "+55 11 96789-0123",
          email: "frank.m@creative.com",
          password: "frankpass123",
          isManager: false
        }
      ]
    },
    {
      id: "6",
      name: "E-commerce Platform",
      description: "A full-stack online store with cart, payment, and admin panel.",
      color: "purple",
      status: "progress",
      creationDate: "2024-04-01",
      startDate: "2024-04-10",
      dueDate: "2024-06-30",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "3",
          name: "Digital Commerce SA",
          description: "E-commerce solutions provider"
        }
      ],
      managers: [
        {
          id: "1",
          name: "Ana Silva",
          cpf: "345.678.901-22",
          phone: "+55 11 96789-0123",
          email: "ana.s@digital.com",
          password: "anasecure123",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "601",
          projectId: "6",
          name: "Payment Gateway",
          description: "Integrate with Stripe API",
          status: "progress",
          priority: "high",
          color: "pink",
          startDate: "2024-04-10",
          dueDate: "2024-05-01",
          artefacts: "API docs: stripe.com/docs/integration"
        }
      ],
      workers: [
        {
          id: "10",
          name: "Grace Taylor",
          cpf: "123.987.654-00",
          phone: "+55 11 97890-1234",
          email: "grace.t@digital.com",
          password: "gracepass123",
          isManager: false
        }
      ]
    },
    {
      id: "7",
      name: "Weather Visualizer",
      description: "Interactive data visualization app for historical and current weather data.",
      color: "pink",
      status: "todo",
      creationDate: "2024-02-15",
      startDate: "2024-02-25",
      dueDate: "2024-03-12",
      artefacts: "PDF Link in company server: www.serverPFD001.com\nAPI key LinK: www.companyKey002.com",
      companies: [
        {
          id: "3",
          name: "Digital Commerce SA",
          description: "E-commerce solutions provider"
        }
      ],
      managers: [
        {
          id: "1",
          name: "Ana Silva",
          cpf: "345.678.901-22",
          phone: "+55 11 96789-0123",
          email: "ana.s@digital.com",
          password: "anasecure123",
          isManager: true
        }
      ],
      tasks: [
        {
          id: "701",
          projectId: "7",
          name: "Data API Research",
          description: "Find suitable weather data APIs",
          status: "todo",
          priority: "medium",
          color: "blue",
          startDate: "2024-02-25",
          dueDate: "2024-03-01",
          artefacts: "API comparison: docs.google.com/weather-apis"
        }
      ],
      workers: [
        {
          id: "11",
          name: "Henry Brown",
          cpf: "456.123.789-00",
          phone: "+55 11 98901-2345",
          email: "henry.b@digital.com",
          password: "henrypass123",
          isManager: false
        }
      ]
    }
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
        <div>
          <Btn
            text="Home"
            whenClick={() => handleInternalLink("/")}
          />
          <Btn
            text="My Companies"
            whenClick={() => handleInternalLink("companies")}
        />
        </div>

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
