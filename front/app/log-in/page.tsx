'use client'
import React, { useState } from 'react';
import axios from 'axios';


export default function Search() {
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
            const response = await axios.post('http://localhost:8080/auth/login', {
                email,
                password,
            });

            if (response.status === 200) {
                const { accessToken } = response.data;
                localStorage.setItem('accessToken', accessToken);
                alert('로그인에 성공했습니다.');
            }
        } catch (error ) {
            const err = error as Error;
                alert(`로그인에 실패했습니다. ${err.message}`);
        }
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
                    <img className="login-form" src="/loginForm.png" alt="image" />
                </div>
            </div>

            <img src="/key.png" alt="image"
                 onClick={() => handleLogin(email, password)}
                 onMouseDown={handleMouseDown}
                 onMouseUp={handleMouseUp}
                 className={isClicked ? 'login-button clicked' : 'login-button'}
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
            <link href="https://fonts.googleapis.com/css2?family=Josefin+Slab:ital,wght@1,400;1,500;1,700&display=swap" rel="stylesheet"/>
        </div>

    )
}