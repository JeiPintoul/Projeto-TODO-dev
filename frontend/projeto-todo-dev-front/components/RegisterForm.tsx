"use client";
import "@styles/LoginRegisterForm.css";
import { useState } from "react";
import UserData from "@myTypes/user";

export default function RegisterForm() {
  const [formData, setFormData] = useState<UserData>({
    name: "",
    cpf: "",
    phone: "",
    email: "",
    password: "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e: React.FormEvent): void => {
    e.preventDefault();

    const UserData: UserData = {
      name: formData.name,
      cpf: formData.cpf,
      phone: formData.phone,
      email: formData.email,
      password: formData.password,
    };

    console.log("Putting it to API... ", JSON.stringify(UserData, null, 2));
  };

  return (
    <div className="container">
      <div className="top"></div>
      <div className="bottom"></div>
      <form className="center" onSubmit={handleSubmit}>
        <h2>Please Sign Up</h2>
        <input
          type="text"
          placeholder="Name"
          required
          onChange={handleChange}
          name="name"
          value={formData.name}
        />
        <input
          type="email"
          placeholder="E-mail"
          required
          onChange={handleChange}
          name="email"
          value={formData.email}
        />
        <input
          type="tel"
          placeholder="Phone"
          required
          onChange={handleChange}
          name="phone"
          value={formData.phone}
          pattern="[0-9]{10,11}"
        />
        <input
          type="text"
          placeholder="CPF"
          required
          onChange={handleChange}
          name="cpf"
          value={formData.cpf}
          inputMode="numeric"
          pattern="\d{3}\.\d{3}\.\d{3}-\d{2}"
        />
        <input
          type="password"
          placeholder="Password"
          required
          onChange={handleChange}
          name="password"
          value={formData.password}
          minLength={10}
        />
        <button type="submit" className="login-button">
          Sign Up
        </button>
      </form>
    </div>
  );
}