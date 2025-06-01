type TaskColor = "yellow" | "pink" | "blue" | "green" | "purple" | "orange";

export default interface TaskData {
    id: string;
    projectId: string;
    name: string;
    description: string;
    status: "todo" | "in-progress" | "done";
    priority: "low" | "medium" | "high";
    dueDate: string;
    color: TaskColor;
  }