type ProjectColor = "yellow" | "pink" | "blue" | "green" | "purple" | "orange";
type ProjectStatus = "todo" | "progress" | "done";

export default interface ProjectData {
  id: string;
  name: string;
  description: string;
  color: ProjectColor;
  status: ProjectStatus;
  creationDate: string;
  dueDate: string;

  companyId: string;
  managerId: string;
  artefacts: string;
  // tasks
  // workers
}
