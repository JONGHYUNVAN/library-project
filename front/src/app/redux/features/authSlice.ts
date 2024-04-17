import { createSlice } from "@reduxjs/toolkit";
type authState = {
    value: String;
};
const initialState = {
    value: "Initial",
} as authState;
export const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        logIn: (state) => {
            state.value = "In";
        },
        logOut: (state) => {
            state.value= "Out";
        },
    },
});

export const { logIn, logOut } = authSlice.actions;

export default authSlice.reducer;
