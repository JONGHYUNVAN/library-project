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


          <div style={{ maxHeight: '10px', marginTop: '4vh' }}>
              <Link href="/" className="navbar-left-side-button">
                  home
              </Link>
              <Link href={{ pathname: '/ranking' }} className="navbar-left-side-button">
                  rank
              </Link>
              <Link href={{ pathname: '/search' }} className="navbar-left-side-button">
                  search
              </Link>
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', background:'transparent', maxHeight: '100px', marginTop: '-1vh' }}>
              <Link href={{ pathname: '/sign-in' }} className="navbar-right-side-button">
                  sign in
              </Link>
              <Link href={{ pathname: '/log-in' }} className="navbar-right-side-button">
                  Log in
              </Link>
          </div>

          <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap"
        />
        <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
        />

      </div>
      {children}
      </body>
      </html>
  )
}
