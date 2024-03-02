'use client'
import React, { useState } from 'react';
import axios from 'axios';
import {logIn} from "@/redux/features/authSlice";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import Image from 'next/image'


export default function Search() {
    const loggedIn = useAppSelector((state) => state.authReducer.value);
    const dispatch = useAppDispatch();
    const [passwordLength, setPasswordLength] = useState(0);
    const [coords, setCoords] = useState({ x: 0, y: 0 });
    const [isClicked, setIsClicked] = useState(false);
    const [email,setEmail] = useState('');
    const [password,setPassword] = useState('');



    const handleMouseMove = (event: React.MouseEvent<HTMLDivElement>, isInputBox: String) => {
        let x = event.nativeEvent.offsetX;
        let y = event.nativeEvent.offsetY;
        if(isInputBox == "email"){
            x+=310;
            y+=120;
        }
        else if(isInputBox == "password"){
            x+=310;
            y+=240;
        }
        setCoords({
            x: x,
            y: y
        });

    };
    const handleMouseDown = () => {
        setIsClicked(true);
    };

    const handleMouseUp = () => {
        setIsClicked(false);
    };
    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPasswordLength(e.target.value.length);
        setPassword(e.target.value);
    };
    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value);
    };
    const handleLogin = async (email:string, password:string): Promise<void> => {
        try {
            const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/auth/login`, {
                email,
                password,
            },
            );

            if (response.status === 200) {
                const accessToken = response.headers['authorization'];
                localStorage.setItem('accessToken', accessToken);
                alert(`logged in successfully.`);
                dispatch(logIn());
                window.history.back();

            }
        } catch (error ) {
            const err = error as Error;
            alert(`Failed to log in. ${err.message}`);
        }
    };
    const handleKakaoClick = () => {
        const newWindow = window.open(`https://kauth.kakao.com/oauth/authorize?client_id=${process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID}&redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URL}&response_type=code`, 'newWindow', 'width=600,height=400');

        const checkInterval = setInterval(() => {
            if (newWindow && newWindow.closed) {
                if (localStorage.getItem('accessToken')) {
                    dispatch(logIn());
                }
                clearInterval(checkInterval);
            }
        }, 1000);
    };
    const handleGoogleClick = () => {
        const newWindow = window.open(`https://accounts.google.com/o/oauth2/v2/auth?client_id=${process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID}&redirect_uri=${process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URL}&response_type=code&scope=email%20profile%20openid&access_type=offline`, 'newWindow', 'width=600,height=400');

        const checkInterval = setInterval(() => {
            if (newWindow && newWindow.closed) {
                if (localStorage.getItem('accessToken')) {
                    dispatch(logIn());
                }
                clearInterval(checkInterval);
            }
        }, 1000);
    };

    return (
        <div>
            <video className="background-video" autoPlay muted loop>
                <source src="/backgroundLogIn.mp4" type="video/mp4" />
            </video>
            <div style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '80vh',
                position: 'relative'
            }}>
                <div
                    className="box-form" onMouseMove={(event) => handleMouseMove(event, "")}
                    style={{ background: `radial-gradient(circle 900px at ${coords.x}px ${coords.y}px, rgba(255,255,255,0.06), rgba(255,255,255,0))`,
                    }} />
                <div>
                    <Image className="login-form" src="/loginForm.png" alt="image"  width={1100} height={700} />
                </div>
            </div>

            <Image src="/key.png" alt="image"
                 onClick={() => handleLogin(email, password)}
                 onMouseDown={handleMouseDown}
                 onMouseUp={handleMouseUp}
                 className={isClicked ? 'login-button clicked' : 'login-button'}
                 width={80}
                   height={80}
            />
            <input
                type="text"
                name="email"
                onChange={handleEmailChange}
                placeholder={email}
                className="emailInputBox"
                onMouseMove={(event) => handleMouseMove(event, "email")}

            />
            <input
                type="password"
                onChange={handlePasswordChange}
                className="passwordInputBox"
                placeholder={password}
                onMouseMove={(event) => handleMouseMove(event, "password")}
            />
            <h2 className="passwordOutputBox">{'$'.repeat(passwordLength)}</h2>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                marginTop: '-250px'
            }}>
                <button onClick={handleKakaoClick} style={{ border: 'none', background: 'transparent',zIndex: 1 }}>
                    <Image src="/kakaoLogin.png" alt="Kakao Login"  width={300}  height={45}style={{zIndex: 10}} />
                </button>
                <button onClick={handleGoogleClick} style={{ border: 'none', background: 'transparent',zIndex: 1 }}>
                    <Image src="/googleLogin.png" alt="image"  width={300}  height={45}style={{zIndex: 10}}/>
                </button>
            </div>
        </div>



)
}