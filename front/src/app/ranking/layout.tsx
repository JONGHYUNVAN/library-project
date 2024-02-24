'use client'
import React, { ReactNode } from 'react';
import SharedLayout from "../component/layout"

const Layout = ({ children }: { children: ReactNode }) => {
    return (
        <SharedLayout>
            {children}
        </SharedLayout>
    );
};

export default Layout;