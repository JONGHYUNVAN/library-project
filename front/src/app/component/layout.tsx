'use client'
import { Providers } from "@/redux/provider";
import {logIn,logOut} from "@/redux/features/authSlice";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import '../globals.css'
import React, {useEffect} from 'react';
import Link from 'next/link';
import Head from "next/head"
import { logout } from './logout';


export default function SharedLayout({
                                   children,
                               }: {
    children: React.ReactNode;
}) {
    const loggedIn = useAppSelector((state) => state.authReducer.value);
    const count = useAppSelector((state) => state.counterReducer.value);
    const dispatch = useAppDispatch();
    useEffect(() => {
        if (localStorage.getItem('accessToken')) {
            dispatch(logIn());
        } else {
            dispatch(logOut());
        }
    }, [dispatch]);

    const handleLogout = () => {
        logout(dispatch);
    };

    return (

        <html lang="en">
        <head>
            <title>Library Project by JHVan</title>
        </head>
        <body>
        <div className="navbar" >

            <div style={{marginTop: '1vh'}}>
                <div className="navbar-button-holder">
                    <Link href="/" className="navbar-left-side-button">
                        home
                    </Link>
                </div>
                <div className="navbar-button-holder">
                    <Link href={{pathname: '/ranking'}}
                          className="navbar-left-side-button">
                        ranking
                    </Link><span></span>
                </div>
                <div className="navbar-button-holder">
                    <Link href={{pathname: '/search'}}
                          className="navbar-left-side-button">
                        search
                    </Link><span></span>
                </div>
                <div className="navbar-button-holder">
                    <Link href={{pathname: '/discussion'}}
                          className="navbar-left-side-button">
                        discussion
                    </Link><span></span>
                </div>
            </div>

            <div style={{
                display: 'flex',
                justifyContent: 'flex-end',
                background: 'transparent',
                maxHeight: '50px',
                marginTop: '0.5vh'
            }}>
                {loggedIn === "In" ? (
                    <>
                        <div className="navbar-button-holder">
                            <Link href={{ pathname: '/my' }}
                                  className="navbar-right-side-button"
                                  style={{fontFamily:'Pacifico, cursive'}}>
                                My
                            </Link>
                        </div>
                        <div className="navbar-button-holder">
                            <button onClick={() => {
                                handleLogout();
                                dispatch(logOut());
                            }}
                                    className="navbar-right-side-button"
                                    style={{fontFamily:'Pacifico, cursive', border: 'none'}}>
                                Logout
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <div className="navbar-button-holder">
                            <Link href={{ pathname: '/sign-up' }}
                                  className="navbar-right-side-button"
                                  style={{fontFamily:'Pacifico, cursive'}}>
                                sign up
                            </Link>
                        </div>
                        <div className="navbar-button-holder">
                            <Link href={{ pathname: '/log-in' }}
                                  className="navbar-right-side-button"
                                  style={{fontFamily:'Pacifico, cursive'}}>
                                Log in
                            </Link>
                        </div>
                    </>
                )}
            </div>



        </div>
        <Providers>{children}</Providers>
        </body>
        </html>

    );
}