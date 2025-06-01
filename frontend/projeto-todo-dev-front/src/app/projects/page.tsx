import "@styles/Home.css";

import CreatingModalForm from "@components/CreatingModalForm";
import EditingModalForm from "@components/EditingModalForm";
import NewProjectBtn from "@components/NewProjectBtn";
import ProjectPostIt from "@components/ProjectPostIt";
import ProjectData from "@myTypes/projects";

export default function ProjectsPage() {

  // TODO: Fetch projects from API
  const projects: ProjectData[] = [
    {
      id: "1",
      name: "SmartHome Dashboard",
      description:
        "A web-based dashboard for monitoring and controlling smart home devices.",
      date: "2024-10-01",
      color: "blue",
    },
    {
      id: "2",
      name: "Health Tracker App",
      description:
        "Mobile app to log meals, workouts, and daily health metrics.",
      date: "2023-12-15",
      color: "green",
    },
    {
      id: "3",
      name: "AI Chatbot",
      description:
        "Customer support chatbot using natural language processing.",
      date: "2024-05-20",
      color: "purple",
    },
    {
      id: "4",
      name: "Portfolio Website",
      description:
        "A modern portfolio site to showcase personal projects and experience.",
      date: "2023-08-10",
      color: "orange",
    },
    {
      id: "5",
      name: "Task Manager API",
      description: "A RESTful API for managing daily tasks and projects.",
      date: "2024-02-18",
      color: "green",
    },
    {
      id: "6",
      name: "E-commerce Platform",
      description:
        "A full-stack online store with cart, payment, and admin panel.",
      date: "2024-06-30",
      color: "purple",
    },
    {
      id: "7",
      name: "Weather Visualizer",
      description:
        "Interactive data visualization app for historical and current weather data.",
      date: "2024-03-12",
      color: "pink",
    },
  ];

  return (
    <>
      <NewProjectBtn />

      <h1>My Projects</h1>

      <div className="projects-container">
        {projects.map((project) => (
          <ProjectPostIt key={project.id} projectInfo={project} />
        ))}
      </div>
      
      <CreatingModalForm />
      <EditingModalForm />
    </>
  );
}
