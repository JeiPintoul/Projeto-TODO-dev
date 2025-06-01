type ProjectColor = "yellow" | "pink" | "blue" | "green" | "purple" | "orange";

export default interface ProjectData {
  id: string;
  name: string;
  description: string;
  date: string;
  color: ProjectColor;
}
