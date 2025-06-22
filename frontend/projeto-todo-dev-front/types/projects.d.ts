import CompanyData from "./company";
import TaskData from "./tasks";
import UserData from "./user";

type ProjectColor = "yellow" | "pink" | "blue" | "green" | "purple" | "orange";
type ProjectStatus = "todo" | "progress" | "done";

export default interface ProjectData {
  id: string;
  name: string;
  description: string;
  color: ProjectColor;
  status: ProjectStatus;
  creationDate: string;
  startDate: string;
  dueDate: string;

  artefacts: string;
  companies: CompanyData[];
  managers: UserData[];
  tasks?: TaskData[];
  workers: UserData[];
}
