// src/context/AuthContext.js
import React, {createContext, useContext, useEffect, useState} from 'react';
import {authService, userService} from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            userService.getProfile()
                .then(response => {
                    setUser(response.data);
                })
                .catch(() => {
                    // Token yaroqsiz bo'lsa
                    localStorage.removeItem('accessToken');
                    setUser(null);
                })
                .finally(() => setLoading(false));
        } else {
            setLoading(false);
        }
    }, []);

    const login = async (email, password) => {
        const response = await authService.login({email, password});
        localStorage.setItem('accessToken', response.data.accessToken);
        setUser(response.data.user);
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
        setUser(null);
    };

    const value = {user, login, logout, isAuthenticated: !!user};

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};