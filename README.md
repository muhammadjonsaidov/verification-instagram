# Instagram DM orqali OTP Tasdiqlash Tizimi

Ushbu loyiha — foydalanuvchilarni ro'yxatdan o'tkazishda ularning email manzili va Instagram akkauntini bir vaqtning o'zida tasdiqlaydigan to'liq (full-stack) veb-ilova. Tasdiqlash jarayoni emailga yuborilgan bir martalik kodni (OTP) foydalanuvchining o'z Instagram akkauntidan belgilangan biznes sahifaga direct message (DM) orqali yuborishi orqali amalga oshiriladi.

## Asosiy funksionallik (Features)

-   **Xavfsiz Ro'yxatdan o'tish:** Foydalanuvchi ma'lumotlari faqat email va Instagram akkaunti tasdiqlangandan keyingina bazaga saqlanadi.
-   **Ikki bosqichli tasdiqlash:** Emailga yuborilgan kod Instagram DM orqali tekshiriladi.
-   **JWT Asosida Avtorizatsiya:** Tizimga kirish uchun `Access` va `Refresh` tokenlaridan foydalaniladi.
-   **API Hujjatlari:** Barcha API endpoint'lar Swagger (OpenAPI 3) yordamida to'liq hujjatlashtirilgan.
-   **Asinxron Jarayonlar:** Email yuborish kabi amallar asosiy oqimni bloklamasdan, asinxron tarzda bajariladi.
-   **Toza Arxitektura:** So'rovlar (Requests) va javoblar (Responses) uchun alohida DTO'lar, xatoliklarni markazlashtirilgan boshqarish.
-   **Zamonaviy Frontend:** React yordamida yaratilgan, foydalanuvchi uchun qulay interfeys.
-   **Xavfsiz Konfiguratsiya:** Barcha maxfiy ma'lumotlar `.env` faylida saqlanadi va `.gitignore` orqali repozitoriyga qo'shilmaydi.

## Texnologiyalar Steki (Tech Stack)

### Backend
-   **Java 17+**
-   **Spring Boot 3+**
-   **Spring Security:** Avtorizatsiya va xavfsizlik uchun.
-   **Spring Data JPA (Hibernate):** Ma'lumotlar bazasi bilan ishlash uchun.
-   **PostgreSQL:** Asosiy ma'lumotlar bazasi.
-   **Redis:** Rate limiting va kesh uchun (kelajakda kengaytirish mumkin).
-   **JJWT (Java JWT):** JSON Web Token'larni yaratish va tekshirish uchun.
-   **Spring Mail:** Email yuborish uchun.
-   **SpringDoc OpenAPI (Swagger):** API hujjatlarini avtomatik generatsiya qilish uchun.
-   **Spring Dotenv:** `.env` fayllaridan foydalanish uchun.
-   **Maven:** Loyihani yig'ish va bog'liqliklarni boshqarish uchun.
-   **Lombok:** Kodni qisqartirish uchun.

### Frontend
-   **React 18+**
-   **React Router DOM:** Sahifalar orasida navigatsiya uchun.
-   **Axios:** Backend API bilan muloqot qilish uchun.
-   **React Context API:** Global holatni (authentication state) boshqarish uchun.

## O'rnatish va Ishga Tushirish (Installation and Setup)

### 1. Talablar (Prerequisites)

-   **Java JDK 17** yoki undan yuqori versiya.
-   **Apache Maven**
-   **Node.js va npm**
-   **PostgreSQL** ma'lumotlar bazasi.
-   **Redis** serveri.
-   **IntelliJ IDEA** yoki boshqa qulay IDE.
-   **Meta for Developers** akkaunti va Instagram'ga ulangan Facebook sahifasi.

### 2. Backend sozlamalari

1.  **Repozitoriyni yuklab oling:**
    ```bash
    git clone https://github.com/muhammadjonsaidov/verification-instagram.git
    cd https://github.com/muhammadjonsaidov/verification-instagram.git
    ```

2.  **Ma'lumotlar bazasini yarating:**
    PostgreSQL'da `instagram_verification_db` nomli yangi baza yarating.

3.  **Instagram (Meta) sozlamalari:**
    -   [Meta for Developers](https://developers.facebook.com/) saytida yangi ilova (App) yarating.
    -   Ilovaga **Instagram Graph API** va **Messenger** mahsulotlarini qo'shing.
    -   Instagram'ga ulangan Facebook sahifangizni tanlang.
    -   **Page Access Token** generatsiya qiling. Bu sizning `INSTAGRAM_ACCESS_TOKEN` bo'ladi.
    -   **Page ID**'ni nusxalab oling. Bu sizning `instagram.page-id` bo'ladi.
    -   **Webhook** sozlamalarida `messages` ob'ektiga obuna bo'ling va o'zingiz o'ylab topgan `Verify Token`'ni (`INSTAGRAM_VERIFY_TOKEN`) kiriting. Webhook URL'i uchun `ngrok` kabi vositadan foydalanib, lokal serveringizni tashqariga `https` qilib ochishingiz kerak bo'ladi (masalan, `https://your-ngrok-url.io/webhook`).

4.  **Muhit o'zgaruvchilarini (Environment Variables) sozlang:**
    Loyiha ildiz papkasida `.env` nomli fayl yarating va `.env.example` faylidan andoza sifatida foydalaning.

    **.env.example** (Namuna)
    ```env
    INSTAGRAM_VERIFY_TOKEN=MY_SECRET_VERIFY_TOKEN
    INSTAGRAM_ACCESS_TOKEN=YOUR_PAGE_ACCESS_TOKEN
    DB_URL=jdbc:postgresql://localhost:5432/instagram_verification_db
    DB_USERNAME=postgres
    DB_PASSWORD=your_db_password
    REDIS_HOST=localhost
    REDIS_PORT=6379
    REDIS_PASSWORD=
    MAIL_USERNAME=your_gmail@gmail.com
    MAIL_PASSWORD=your_gmail_app_password
    JWT_SECRET=bu_juda_maxfiy_kalit_bolishi_kerak_uzunroq
    ```

5.  **Backend'ni ishga tushiring:**
    ```bash
    mvn spring-boot:run
    ```
    Server `http://localhost:8080` manzilida ishga tushadi.

### 3. Frontend sozlamalari

1.  **Frontend papkasiga o'ting:**
    ```bash
    cd frontend
    ```

2.  **Bog'liqliklarni o'rnating:**
    ```bash
    npm install
    ```

3.  **Frontend'ni ishga tushiring:**
    ```bash
    npm start
    ```
    Ilova brauzerda `http://localhost:3000` manzilida ochiladi.

## API Hujjatlari (API Documentation)

Backend ishga tushgandan so'ng, barcha API endpoint'larining to'liq hujjatlarini quyidagi manzilda ko'rishingiz mumkin:

[**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

Swagger UI orqali endpoint'larni sinab ko'rish, so'rovlar yuborish va javoblarni tahlil qilish mumkin. Himoyalangan yo'llar uchun `/auth/login` dan olingan `accessToken`'dan foydalaning.

## Loyiha Arxitekturasi (Registration Flow)

1.  **Foydalanuvchi:** Ro'yxatdan o'tish formasini (`email`, `instagramUsername`, `password`) to'ldirib yuboradi.
2.  **Backend (`/auth/register`):** Ma'lumotlarni validatsiyadan o'tkazadi. Bu `email` va `instagramUsername` tizimda mavjud emasligini tekshiradi.
3.  **PendingVerification:** Ma'lumotlar (`email`, heshlangan parol, `instagramUsername`) va yangi generatsiya qilingan OTP kod `pending_verifications` jadvaliga 5 daqiqalik saqlash muddati bilan saqlanadi.
4.  **EmailService:** Foydalanuvchining emailiga OTP kodni yuboradi.
5.  **Foydalanuvchi:** O'z Instagram akkauntidan belgilangan biznes sahifaga shu kodni yuboradi.
6.  **Instagram Webhook:** Instagram bu xabarni ushlab, bizning backend'imizdagi `/webhook` manziliga yuboradi.
7.  **VerificationService:** Kelgan kodni va xabar yuboruvchining PSID'sini qabul qiladi.
8.  **Tasdiqlash:** Kod `pending_verifications` jadvalidan qidiriladi. Agar topilsa, muddati tekshiriladi.
9.  **User Yaratish:** Barcha tekshiruvlar muvaffaqiyatli bo'lsa, `pending_verifications` jadvalidagi ma'lumotlar asosida `users` jadvalida yangi foydalanuvchi yaratiladi va vaqtinchalik yozuv o'chiriladi.
10. **Yakuniy Xabar:** Foydalanuvchiga Instagram DM orqali ro'yxatdan o'tish muvaffaqiyatli yakunlangani haqida xabar yuboriladi.

## Muhim eslatmalar

-   **Instagram Access Token:** Facebook tomonidan beriladigan `Page Access Token` qisqa muddatli bo'lishi mumkin. Production muhiti uchun uzoq muddatli (long-lived) token olish kerak bo'ladi.
-   **Gmail App Password:** Agar email yuborish uchun Gmail'dan foydalansangiz, `MAIL_PASSWORD` sifatida akkauntingizning oddiy parolini emas, Google Account sozlamalaridan olingan **"App Password"**'ni ishlating.