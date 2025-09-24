// src/App.js
import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import {useAuth} from './context/AuthContext';
import Navbar from './components/Navbar';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import ProfilePage from './components/ProfilePage';
import VerificationPendingPage from './components/VerificationPendingPage';

// Himoyalangan sahifalar uchun komponent
const ProtectedRoute = ({children}) => {
    const {isAuthenticated} = useAuth();
    return isAuthenticated ? children : <Navigate to="/login"/>;
};

function App() {
    return (
        <Router>
            <Navbar/>
            <div className="container">
                <Routes>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                    <Route path="/verification-pending" element={<VerificationPendingPage/>}/>
                    <Route path="/profile" element={
                        <ProtectedRoute>
                            <ProfilePage/>
                        </ProtectedRoute>
                    }/>
                    {/* Bosh sahifa uchun yo'naltirish */}
                    <Route path="/" element={<Navigate to="/profile"/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;