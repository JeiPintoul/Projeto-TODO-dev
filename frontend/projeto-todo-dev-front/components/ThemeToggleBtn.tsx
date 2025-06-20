"use client";
import "@styles/ThemeToggleBtn.css";

import { useEffect, useState } from "react";

export default function ThemeToggleBtn() {
  const [isDarkMode, setIsDarkMode] = useState(false);

  useEffect(() => {
    const savedTheme = localStorage.getItem("theme");
    const prefersDark = window.matchMedia(
      "(prefers-color-scheme: dark)"
    ).matches;

    // localStorage > prefers-color-scheme > light
    const initialDarkMode = savedTheme ? savedTheme === "dark" : prefersDark;

    setIsDarkMode(initialDarkMode);
    applyTheme(initialDarkMode);
  }, []);

  const applyTheme = (dark: boolean) => {
    if (dark) {
      document.body.setAttribute("data-theme", "dark");
    } else {
      document.body.removeAttribute("data-theme");
    }
  };

  const toggleTheme = () => {
    const newDarkMode = !isDarkMode;
    setIsDarkMode(newDarkMode);
    applyTheme(newDarkMode);
    localStorage.setItem("theme", newDarkMode ? "dark" : "light");
  };

  return (
    <button
      className="theme-toggle"
      onClick={toggleTheme}
      aria-label="Alternar tema"
    >
      {isDarkMode ? (
        <span className="sun-icon">☀️</span>
      ) : (
        <span className="moon-icon">🌙</span>
      )}
    </button>
  );
}
