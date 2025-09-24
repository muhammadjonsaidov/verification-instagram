import React from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';

const Navbar = () => {
    const {isAuthenticated, logout} = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <Link to="/" className="brand">Instagram Auth</Link>
            <div className="nav-links">
                {isAuthenticated ? (
                    <>
                        <Link to="/profile">Profil</Link>
                        <button onClick={handleLogout}>Chiqish</button>
                    </>
                ) : (
                    <>
                        <Link to="/login">Kirish</Link>
                        <Link to="/register">Ro'yxatdan o'tish</Link>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Navbar;