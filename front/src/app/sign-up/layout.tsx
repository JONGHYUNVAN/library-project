'use client'
import { Providers } from "@/redux/provider";
import {logOut} from "@/redux/features/authSlice";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import '../globals.css'
import React from 'react';
import Link from 'next/link';
import axios from "axios";

export default function Layout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    const loggedIn = useAppSelector((state) => state.authReducer.value);
    const dispatch = useAppDispatch();
    const handleLogout = async () => {

        try {
            const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/logout`, {}, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                },
                withCredentials: true
            });

            if (response.status === 200) {
                alert('로그아웃이 성공적으로 이루어졌습니다.');
                dispatch(logOut());
                localStorage.removeItem('accessToken');
                window.location.href = '/';
            }
        }
        catch (error ) {
            const err = error as Error;
            alert(`로그아웃에 실패했습니다. ${err.name}`);
        }

    };

    return (


        <html lang="en">
        <body>
        <div className="navbar" >

            <div style={{ marginTop: '1vh' }}>
                <div className="navbar-button-holder">
                    <Link href="/" className="navbar-left-side-button">
                        home
                    </Link>
                </div>
                <div className="navbar-button-holder">
                    <Link href={{ pathname: '/ranking' }}
                          className="navbar-left-side-button">
                        rank
                    </Link><span></span>
                </div>
                <div className="navbar-button-holder">
                    <Link href={{ pathname: '/search' }}
                          className="navbar-left-side-button">
                        search
                    </Link><span></span>
                </div>
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end', background:'transparent', maxHeight: '50px', marginTop: '0.5vh' }}>
                {loggedIn === "In" ? (
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

            <link
                rel="stylesheet"
                href="https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap"
            />
            <link
                rel="stylesheet"
                href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
            />

        </div>
        <Providers>{children}</Providers>
        </body>
        </html>

    );
}