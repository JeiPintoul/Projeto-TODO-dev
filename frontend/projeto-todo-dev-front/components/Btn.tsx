import "@styles/Btn.css";

type BtnProps = {
  text: string;
  whenClick: () => void;
};

export default function Btn({ text, whenClick }: BtnProps) {
  return (
    <button className="Btn" onClick={whenClick}>
      {text}
    </button>
  );
}
