import { Inter } from 'next/font/google'
import './globals.css'
import React from 'react';
import Link from 'next/link';


const inter = Inter({ subsets: ['latin'] })


export default function RootLayout({children }: { children: React.ReactNode})
{
  return (
      <html lang="en">
      <body className={inter.className}>
      <div className="navbar" >


        <Link href="/" style={{  float:'left',
            display: 'flex',
            justifyContent: 'left',
            marginTop:'4vw',
            marginLeft:'1vw',
            marginRight :'1vw',
            background: 'linear-gradient(to bottom right, palegoldenrod ,gold)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent'}}>home</Link>
        <Link href={{ pathname: '/ranking' }} style={{  float:'left',
            display: 'flex',
            justifyContent: 'left',
            marginTop:'4vw',
            marginLeft:'1vw',
            marginRight :'1vw',
            background: 'linear-gradient(to bottom right, palegoldenrod ,gold)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent'}}>rank</Link>
        <Link href={{ pathname: '/search' }} style={{  float:'left',
            display: 'flex',
            justifyContent: 'left',
            marginTop:'4vw',
            marginLeft:'1vw',
            marginRight :'1vw',
            background: 'linear-gradient(to bottom right, palegoldenrod ,gold)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent'}}>search</Link>
        <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap"
        />
      </div>
      {children}
      </body>
      </html>
  )
}
