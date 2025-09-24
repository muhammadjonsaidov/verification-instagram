import React from 'react';
import { useAuth } from '../context/AuthContext';

const ProfilePage = () => {
    const { user } = useAuth();

    if (!user) {
        return <p>Yuklanmoqda...</p>;
    }

    return (
        <div className="profile-container">
            <h2>Xush kelibsiz, {user.fullName || user.instagramUsername}!</h2>
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Instagram:</strong> @{user.instagramUsername}</p>
            <p><strong>Status:</strong> Tasdiqlangan</p>
        </div>
    );
};

export default ProfilePage;