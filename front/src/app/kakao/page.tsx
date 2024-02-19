'use client'
import { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation'
import axios from 'axios';
import {logIn} from "@/redux/features/authSlice";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";

export default function KakaoLogin() {
    const searchParams = useSearchParams()
    const code = searchParams.get('code')
    const loggedIn = useAppSelector((state) => state.authReducer.value);
    const dispatch = useAppDispatch();
    const [kakaoToken, setKakaoToken] = useState('');
    const [accessToken, setAccessToken] = useState('');
    useEffect(() => {
        fetch(`/api/kakao?code=${code}`, {
            method: 'POST',
        })
            .then(response => response.json())

            .then(data => {
                setKakaoToken(data.access_token);
                axios
                    .get(`${process.env.NEXT_PUBLIC_API_URL}/auth/oauth2/kakao/token`, {
                        headers: { "Authorization": data.access_token },
                    })
                    .then((response) => {
                        setAccessToken(response.headers.authorization);
                        localStorage.setItem('accessToken', response.headers.authorization);
                        alert(`logged in successfully.`);
                        dispatch(logIn());
                        window.close();
                    })
                    .catch((error) => {
                        console.error('백엔드 서버에서 토큰 요청 실패', error);
                    })
            })
            .catch((error) => {
                console.error('토큰 요청 실패', error);
            });
    }, []);
   // return <>code: {code} token: {JSON.stringify(kakaoToken)} accessToken:{accessToken}</>
}