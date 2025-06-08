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
    dueDate: string;
    color: TaskColor;
  }