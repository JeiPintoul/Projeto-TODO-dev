"use client";
import "@styles/LoginRegisterForm.css";

import { useRouter } from "next/navigation";
import { useState } from "react";
import LoginData from "@myTypes/login";

export default function LoginForm() {
  const router = useRouter();

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

  const handleSubmit = (e: React.FormEvent): void => {
    e.preventDefault();

    const loginData: LoginData = {
      email: formData.email,
      password: formData.password,
    };

    // TODO: Add json to API
    console.log("Add it to API... ", JSON.stringify(loginData, null, 2));
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
