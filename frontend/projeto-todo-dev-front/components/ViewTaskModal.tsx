import Modal from "./Modal";
import TaskData from "@myTypes/tasks";
import "@styles/TaskView.css";

type ViewTaskModalProps = {
  isOpen: boolean;
  setIsOpen: (param: boolean) => void;
  taskData: TaskData | null;
};

export default function ViewTaskModal({
  isOpen,
  setIsOpen,
  taskData,
}: ViewTaskModalProps) {
  if (!taskData) return null;

  return (
<Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
      <h2 className="modal-title">Task Details</h2>
      
      <div className="data">
        <h3>Task Name</h3>   
        <p>{taskData.name}</p>       
      </div>

      <div className="data">
        <h3>Description</h3>   
        <p>{taskData.description}</p>            
      </div>

      <div className="data">
        <h3>Start Date</h3>   
        <p>{new Date(taskData.startData).toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        })}</p>            
      </div>

      <div className="data">
        <h3>Due Date</h3>   
        <p>{new Date(taskData.dueDate).toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        })}</p>            
      </div>

      <div className="data">
        <h3>Priority</h3>   
        <p data-priority={taskData.priority}>
          {taskData.priority.charAt(0).toUpperCase() + taskData.priority.slice(1)}
        </p>            
      </div>

      <div className="data">
        <h3>Status</h3>   
        <p data-status={taskData.status}>
          {taskData.status === 'progress' ? 'In Progress' : 
           taskData.status.charAt(0).toUpperCase() + taskData.status.slice(1)}
        </p>            
      </div>

      <div className="data">
        <h3>Artefacts</h3>   
        <p>{taskData.artefacts}</p>       
      </div>
    </Modal>
  );
}