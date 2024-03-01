'use client'
import {useRouter} from "next/navigation";
import { useDispatch } from 'react-redux';
import { logIn, logOut } from './redux/features/authSlice';
import React, { useEffect } from 'react';



export default function Home() {
  let name = 'J.H.VAN';
  const router = useRouter();
  const dispatch = useDispatch();

    useEffect(() => {
        if (localStorage.getItem('accessToken')) {
            dispatch(logIn());
        } else {
            dispatch(logOut());
        }
    }, [dispatch]);

  return (
      <>
          <head>
              <title> Welcome! </title>
          </head>
          <div className="main-page"
               onClick={() => router.push('/ranking')}>
            <div className="title" style={{ position: 'relative', backgroundSize: 'cover', backgroundPosition: 'center' }}>
              <h4>library project</h4>
              <span className="title-sub">by {name}</span>
            </div>
            <video className="background-video" autoPlay muted loop>
              <source src="/background.mp4" type="video/mp4" />
            </video>
          </div>
      </>
  );
}

``