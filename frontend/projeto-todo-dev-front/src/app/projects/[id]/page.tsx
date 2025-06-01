import "@styles/Home.css";
import ProjectData from "@myTypes/projects";
import TaskData from "@myTypes/tasks";
import NewTaskBtn from "@components/NewTaskBtn";
import TaskPostIt from "@components/TaskPostIt";

interface Props {
  params: {
    id: string;
  };
}

export default async function ProjectTasksPage({ params }: Props) {
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
      status: "in-progress",
      priority: "high",
      dueDate: "2024-02-15",
      color: "green",
    },
  ];

  return (
    <>
      <NewTaskBtn />

      <h1>{project.name}</h1>

      <div className="tasks-container">
        {tasks.map((task) => (
          <TaskPostIt key={task.id} taskInfo={task} />
        ))}
      </div>

      {/* <CreatingModalForm projectId={params.id} />
      <EditingModalForm /> */}
    </>
  );
}
