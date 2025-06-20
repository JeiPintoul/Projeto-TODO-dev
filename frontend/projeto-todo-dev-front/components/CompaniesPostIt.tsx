"use client";
import "@styles/CompaniesPostIt.css";

import CompanyData from "@myTypes/company";

export default function CompaniesPostIt({ companyInfo }: { companyInfo: CompanyData}) {

  if (!companyInfo) return null;

  return (
    <div className={`post-it green`}>
      <div className="post-it-link">
        <h3>{companyInfo.name}</h3>
        <p>{companyInfo.description}</p>
      </div>
    </div>
  );
}