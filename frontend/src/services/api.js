// src/services/api.js
import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    headers: { 'Content-Type': 'application/json' }
});

// So'rov yuborishdan oldin token'ni avtomatik qo'shish (bu sizda bor)
apiClient.interceptors.request.use(config => {
    const token = localStorage.getItem('accessToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// JAVOBLARNI USHLAB QOLUVCHI INTERCEPTOR (YANGI QISM)
apiClient.interceptors.response.use(
    (response) => response, // Agar javob muvaffaqiyatli bo'lsa, hech narsa qilmaymiz
    async (error) => {
        const originalRequest = error.config;

        // Agar xato 401 (Unauthorized) bo'lsa va bu qayta urinish bo'lmasa
        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true; // Qayta urinishni belgilab qo'yamiz

            try {
                const refreshToken = localStorage.getItem('refreshToken');
                const response = await apiClient.post('/auth/refresh', { refreshToken });

                const { accessToken } = response.data;
                localStorage.setItem('accessToken', accessToken);

                // Asl so'rovning sarlavhasini yangi token bilan o'zgartiramiz
                apiClient.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
                originalRequest.headers['Authorization'] = `Bearer ${accessToken}`;

                // Asl so'rovni qayta yuboramiz
                return apiClient(originalRequest);

            } catch (refreshError) {
                // Agar refresh ham xato bersa, demak foydalanuvchi sessiyasi tugagan
                // Bu yerda foydalanuvchini tizimdan chiqarib, login sahifasiga yo'naltirish kerak
                console.error("Session expired. Please log in again.");
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);


// Barcha API chaqiruvlari (bu qism o'zgarmaydi)
export const authService = {
    register: (data) => apiClient.post('/auth/register', data),
    login: (data) => {
        return apiClient.post('/auth/login', data).then(response => {
            // Login muvaffaqiyatli bo'lganda ikkala tokenni ham saqlaymiz
            if (response.data.accessToken && response.data.refreshToken) {
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);
            }
            return response;
        });
    },
};

export const userService = {
    getProfile: () => apiClient.get('/user/profile'),
};

// Logout funksiyasini ham shu yerga qo'shish qulay
export const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    delete apiClient.defaults.headers.common['Authorization'];
};