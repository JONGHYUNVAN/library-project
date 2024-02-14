import React, {createContext, useState, useEffect, SetStateAction, Dispatch} from 'react';

export const AuthContext = createContext({
    isLoggedIn: false,
    setIsLoggedIn: (() => {}) as Dispatch<SetStateAction<boolean>>,
});
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setIsLoggedIn(true);
        } else {
            setIsLoggedIn(false);
        }
    }, [isLoggedIn]);

    return (
        <AuthContext.Provider value={{ isLoggedIn, setIsLoggedIn }}>
    {children}
    </AuthContext.Provider>
);
};
