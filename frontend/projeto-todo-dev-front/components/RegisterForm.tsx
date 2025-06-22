"use client";
import "@styles/LoginRegisterForm.css";
import { useState } from "react";
import UserData from "@myTypes/user";
import { useRouter } from "next/navigation";

export default function RegisterForm() {
  
  const router = useRouter();

  const handleInternalLink = (link: string) => {
    router.push(`/${link}`);
  };

  const [formData, setFormData] = useState<UserData>({
    id: "",
    name: "",
    cpf: "",
    phone: "",
    email: "",
    password: "",
    isManager: false,
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

  const handleSubmit = async (e: React.FormEvent): Promise<void> => {
    e.preventDefault();

    const UserData = {
      nome: formData.name,
      email: formData.email,
      senha: formData.password,
      cpf: formData.cpf,
      phone: formData.phone,
    };

    try {
      const response = await fetch('http://localhost:8080/api/usuarios', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(UserData),
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
  
      const data = await response.json();
      console.log('User created sucessfully', data);

      localStorage.setItem("user", JSON.stringify(data));

      handleInternalLink("")

    } catch (error) {
      console.error('Error creating user', error);
    }
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