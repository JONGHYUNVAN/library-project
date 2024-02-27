'user client'
import {Suspense} from "react";
import axios from "axios";
import {logIn,logOut} from "@/redux/features/authSlice";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { AppDispatch } from "../redux/store";

export async function logout(dispatch: AppDispatch) {
    try {
        const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/auth/logout`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
            },
            withCredentials: true
        });

        if (response.status === 200) {
            alert('Logged out successfully');
            dispatch(logOut());
            localStorage.removeItem('accessToken');
            window.location.href = '/';
        }
    } catch (error) {
        const err = error as Error;
        alert(`로그아웃에 실패했습니다. ${err.name}`);
    }
}
