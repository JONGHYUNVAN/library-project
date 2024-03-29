import { Providers } from "@/redux/provider";
import './globals.css'
import React from 'react';
export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
        <head>
            <link rel="preconnect" href="https://fonts.gstatic.com" />
            <link href="https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap" rel="stylesheet" />
            <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet" />
            <link href="https://fonts.googleapis.com/css2?family=Josefin+Slab:ital,wght@1,400;1,500;1,700&display=swap" rel="stylesheet"/>
            <link
                rel="stylesheet"
                href="https://fonts.googleapis.com/css2?family=Long+Cang&display=swap"
            />
        </head>
        <body>
        <Providers>{children}</Providers>
        </body>
        </html>
    );
}