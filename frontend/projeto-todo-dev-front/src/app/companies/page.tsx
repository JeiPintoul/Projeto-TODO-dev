"use client";
import "@styles/Companies.css";
import Btn from "@components/Btn";
import CompaniesPostIt from "@components/CompaniesPostIt";
import { useRouter } from "next/navigation";
import CompanyData from "@myTypes/company";

export default function CompaniesPage() {
  // TODO: Fetch companies from API
  const companies: CompanyData[] = [
    {
      id: "1",
      name: "Buriti Tech Solutions",
      description:
        "A software development company delivering custom cloud-based applications for startups and growing businesses.",
    },
    {
      id: "2",
      name: "Aurum Robotics",
      description:
        "Innovative robotics startup focused on educational kits and smart automation for small-scale industries.",
    },
    {
      id: "3",
      name: "NeoByte Studios",
      description:
        "A creative game development studio building immersive AR and VR experiences for entertainment and education.",
    },
    {
      id: "4",
      name: "GreenLeaf Data",
      description:
        "Data analytics firm specializing in precision agriculture and IoT-based monitoring systems for sustainable farming.",
    },
  ];

  const router = useRouter();

  const handleInternalLink = (link: string) => {
    router.push(`/${link}`);
  };

  return (
    <>
      <header>
        <Btn text="Home" whenClick={() => handleInternalLink("/")} />
        <h1 className="title">My Companies</h1>
        <Btn
          text="Projects"
          whenClick={() => handleInternalLink("projects/")}
        />
      </header>

      <div className="tasks-container">
        {companies.map((company) => (
          <CompaniesPostIt key={company.id} companyInfo={company} />
        ))}
      </div>
    </>
  );
}
