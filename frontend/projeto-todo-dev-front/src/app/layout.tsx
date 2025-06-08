import ThemeToggleBtn from "@components/ThemeToggleBtn";
import "@styles/Global.css"


import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Projeto TODO Dev",
  description:
    "Trabalho da matéria de Engenharia de Software ministrada pelo professor Rodrigo Elias",
};

export default function HomeLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${geistSans.variable} ${geistMono.variable}`}>
        <ThemeToggleBtn />
        {children}
      </body>
    </html>
  );
}