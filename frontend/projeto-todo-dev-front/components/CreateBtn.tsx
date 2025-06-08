import "@styles/NewProjectBtn.css";

type CreateBtnProps = {
  text: string;
  openModal: () => void;
};

export default function CreateBtn({ text, openModal }: CreateBtnProps) {
  return (
    <button className="new-project-btn" onClick={openModal}>
      {text}
    </button>
  );
}
