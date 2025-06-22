"use client";
import "@styles/LoginRegisterForm.css";

import { useRouter } from "next/navigation";
import { useState } from "react";
import LoginData from "@myTypes/login";

export default function LoginForm() {
  const router = useRouter();

  const handleInternalLink = (link: string) => {
    router.push(`/${link}`);
  };

  const handleSignUp = (e: React.MouseEvent) => {
    e.preventDefault();
    router.push("/register");
  };

  const [formData, setFormData] = useState<LoginData>({
    email: "",
    password: "",
  });

  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent): Promise<void> => {
    e.preventDefault();

    const loginData = {
      email: formData.email,
      senha: formData.password,
    };

    try {

      const response = await fetch(
        `http://localhost:8080/api/usuarios/login-simples?email=${encodeURIComponent(loginData.email)}&senha=${encodeURIComponent(loginData.senha)}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          }
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      console.log('User logged sucessfully', data);

      localStorage.setItem("user", JSON.stringify(data));

      handleInternalLink("");

    } catch (error) {
      console.error('Error in login', error);
    }

  };

  return (
    <div className="container">
      <div className="top"></div>
      <div className="bottom"></div>
      <form className="center"  onSubmit={handleSubmit}>
        <h2>Please Sign In</h2>
        <input
          type="email"
          placeholder="Email"
          required
          onChange={handleChange}
          name="email"
        />
        <input
          type="password"
          placeholder="Password"
          required
          onChange={handleChange}
          name="password"
          minLength={10}
        />
        <p>
          Don't have an account? <a onClick={handleSignUp}>Sign up now</a>
        </p>
        <button type="submit" className="login-button">
          Sign In
        </button>
      </form>
    </div>
  );
}
