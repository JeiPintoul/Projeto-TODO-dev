"use client";
import "@styles/Home.css";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Home() {
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);

  const handleInternalLink = (link: string) => {
    router.push(`/${link}`);
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
  };

  return (
    <div className="container-home">
      <div className="main-home">
        <h1>TODO-Dev Project</h1>

        <p>
          Efficiently organize your tasks with our enterprise-grade todo system
          designed specifically for developers.
        </p>

        <div className="link-container">
          {isAuthenticated ? (
            <>
              <div onClick={handleLogout} className="link">
                Log out
              </div>
              <div onClick={() => handleInternalLink("projects")} className="link">
                View Projects
              </div>
            </>
          ) : (
            <>
              <div onClick={() => handleInternalLink("login")} className="link">
                Login
              </div>
              <div onClick={() => handleInternalLink("register")} className="link">
                Register
              </div>
            </>
          )}
        </div>

        <footer>
          <p>
          © {new Date().getFullYear()} TODO-Dev Project. Built with 💻 by devs,
          for devs.
          </p>
          <a
            href="https://github.com/JeiPintoul/Projeto-TODO-dev"
            target="_blank"
            rel="noopener noreferrer"
            style={{ color: "#4361ee", cursor: "pointer", textDecoration: 'none' }}
          >
            Github Repository
          </a>
        </footer>
      </div>
    </div>
  );
}