import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {authService} from '../services/api';

const RegisterPage = () => {
    const [formData, setFormData] = useState({instagramUsername: '', email: '', password: '', fullName: ''});
    const [error, setError] = useState(null);
    const [validationErrors, setValidationErrors] = useState({});
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setValidationErrors({});
        try {
            await authService.register(formData);
            navigate('/verification-pending');
        } catch (err) {
            if (err.response && err.response.status === 400) {
                setValidationErrors(err.response.data.errors);
            } else if (err.response && err.response.data.error) {
                setError(err.response.data.error);
            } else {
                setError('Ro\'yxatdan o\'tishda kutilmagan xatolik.');
            }
        }
    };

    return (
        <div className="form-container">
            <h2>Ro'yxatdan o'tish</h2>
            <form onSubmit={handleSubmit}>
                <input name="instagramUsername" placeholder="Instagram Username" onChange={handleChange} required/>
                {validationErrors.instagramUsername && <p className="error">{validationErrors.instagramUsername}</p>}

                <input name="email" type="email" placeholder="Email" onChange={handleChange} required/>
                {validationErrors.email && <p className="error">{validationErrors.email}</p>}

                <input name="password" type="password" placeholder="Parol" onChange={handleChange} required/>
                {validationErrors.password && <p className="error">{validationErrors.password}</p>}

                <input name="fullName" placeholder="To'liq ism (ixtiyoriy)" onChange={handleChange}/>
                <button type="submit">Ro'yxatdan o'tish</button>
                {error && <p className="error">{error}</p>}
            </form>
        </div>
    );
};

export default RegisterPage;