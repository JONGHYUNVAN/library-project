import { configureStore } from "@reduxjs/toolkit";
import authReducer from './features/authSlice'
import counterReducer from "./features/counterSlice";

export const store = configureStore({
    reducer: {
        authReducer,
        counterReducer,
    },
    devTools: process.env.NODE_ENV !== "production",
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
