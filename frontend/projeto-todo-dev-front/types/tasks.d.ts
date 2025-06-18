type TaskColor = "yellow" | "pink" | "blue" | "green" | "purple" | "orange";
type TaskPriority = "low" | "medium" | "high";
type TaskStatus = "todo" | "progress" | "done";

export default interface TaskData {
    id: string;
    projectId: string;
    name: string;
    description: string;
    status: TaskStatus;
    priority: TaskPriority;
    color: TaskColor;
    startDate: string;
    dueDate: string;

    artefacts: string;
  }