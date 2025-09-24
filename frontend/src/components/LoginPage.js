import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';

const LoginPage = () => {
    const [formData, setFormData] = useState({email: '', password: ''});
    const [error, setError] = useState('');
    const {login} = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await login(formData.email, formData.password);
            navigate('/profile');
        } catch (err) {
            setError('Email yoki parol noto\'g\'ri.');
        }
    };

    return (
        <div className="form-container">
            <h2>Tizimga kirish</h2>
            <form onSubmit={handleSubmit}>
                <input name="email" type="email" placeholder="Email" onChange={handleChange} required/>
                <input name="password" type="password" placeholder="Parol" onChange={handleChange} required/>
                <button type="submit">Kirish</button>
                {error && <p className="error">{error}</p>}
            </form>
        </div>
    );
};

export default LoginPage;