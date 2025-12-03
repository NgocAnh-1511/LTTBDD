# Coffee Shop Project

Dự án Coffee Shop gồm 3 phần:
- **Backend**: NestJS API (Port 3000)
- **Frontend Admin**: React Admin Panel (Port 3001)
- **Android App**: Kotlin Android Application

## 📁 Cấu Trúc Dự Án

```
.
├── admin-web/
│   ├── backend/          # NestJS Backend API
│   │   ├── src/          # Source code
│   │   └── database/     # SQL database file
│   └── frontend/         # React Admin Panel
│
└── LTTBDD-main/          # Android Application
    └── app/
        └── src/main/
```

## 🚀 Quick Start

### 1. Setup Database

1. Mở **XAMPP** và start **MySQL**
2. Mở **phpMyAdmin**: http://localhost/phpmyadmin
3. Import file: `admin-web/backend/database/database.sql`

### 2. Chạy Backend

**Lưu ý:** Thay `E:\namngu` bằng đường dẫn thực tế của bạn

```bash
# Di chuyển đến thư mục backend
cd E:\namngu\admin-web\backend

# Hoặc nếu đã ở thư mục gốc:
cd admin-web\backend

# Cài đặt và chạy
npm install
npm run start:dev
```

Backend: http://localhost:3000/api

### 3. Chạy Frontend Admin

Mở terminal mới (giữ backend đang chạy):

```bash
# Di chuyển đến thư mục frontend
cd E:\namngu\admin-web\frontend

# Cài đặt và chạy
npm install
npm run dev
```

Admin Panel: http://localhost:3001

### 4. Chạy Android App

1. Mở **Android Studio**
2. File > Open > Chọn `LTTBDD-main`
3. Cấu hình API URL trong `ApiClient.kt`
4. Run app

## 📖 Chi Tiết Setup

Xem các file README chi tiết:
- **Backend + Frontend**: `admin-web/README.md`
- **Android App**: `LTTBDD-main/README.md`

## 🔑 Default Credentials

**Admin Account:**
- Phone: `admin`
- Password: `admin123`

**Test User:**
- Phone: `0846230059`
- Password: `Nam26122005@`

## 📝 Lưu Ý

1. **Backend phải chạy trước** khi test Frontend hoặc Android
2. **Database phải được import** trước khi chạy backend
3. Với **Android real device**, đảm bảo cùng mạng WiFi với máy tính
4. **Firewall** có thể chặn port 3000, cần mở port

## 🌐 Deploy & Phân Phối APK

### ❓ Người khác tải APK có lưu vào Database của tôi không?

**Trả lời:** 
- ❌ **KHÔNG** nếu backend chỉ chạy localhost (hiện tại)
- ✅ **CÓ** nếu bạn deploy backend lên server công khai

### 🚀 Để người khác dùng APK và lưu vào database của bạn:

1. **Deploy Backend** lên server công khai (Railway, Render, VPS...)
2. **Cập nhật BASE_URL** trong Android app thành URL của server
3. **Build APK release** và phân phối

📖 **Xem hướng dẫn chi tiết:** `LTTBDD-main/DEPLOY_GUIDE.md`

## 🛠️ Tech Stack

- **Backend**: NestJS, TypeORM, MySQL, JWT
- **Frontend**: React, Material-UI, React Query
- **Android**: Kotlin, Retrofit, OkHttp, Coroutines

## 📞 Support

Nếu gặp lỗi, kiểm tra:
1. Console logs của backend
2. Browser console (F12) của frontend
3. Logcat của Android app
4. Network tab để xem API calls

