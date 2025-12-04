# Coffee Shop Admin - Setup Guide

Hướng dẫn setup và chạy dự án Coffee Shop Admin (Backend + Frontend) trên máy mới.

## 📋 Yêu Cầu Hệ Thống

- **Node.js**: v18+ ([Download](https://nodejs.org/))
- **MySQL**: XAMPP hoặc MySQL Server
- **Git**: Để clone repository

## 🚀 Setup Backend

### Bước 1: Di chuyển đến thư mục dự án

**Quan trọng:** Đảm bảo bạn đang ở thư mục gốc của dự án (nơi có folder `admin-web`)

```bash
# Ví dụ: Nếu dự án ở E:\namngu
cd E:\namngu\admin-web\backend

# Hoặc nếu đã ở thư mục gốc:
cd admin-web\backend
```

### Bước 2: Cài đặt Dependencies

```bash
npm install
```

### Bước 3: Setup Database

1. Mở **XAMPP Control Panel** và start **MySQL**
2. Mở **phpMyAdmin**: http://localhost/phpmyadmin
3. Import file SQL:
   - Click tab **SQL**
   - Copy toàn bộ nội dung từ `database/database.sql`
   - Paste vào ô SQL và click **Go**

Hoặc dùng MySQL command line:
```bash
mysql -u root -p < database/database.sql
```

### Bước 4: Cấu hình Database (nếu cần)

File: `admin-web/backend/src/app.module.ts`

Mặc định:
- Host: `localhost`
- Port: `3306`
- Username: `root`
- Password: `` (empty)
- Database: `CoffeShop`

Nếu khác, sửa trong file hoặc tạo file `.env`:
```env
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=
DB_NAME=CoffeShop
```

### Bước 5: Chạy Backend

```bash
# Đảm bảo đang ở thư mục: admin-web/backend
npm run start:dev
```

Backend sẽ chạy tại: **http://localhost:3000/api**

## 🎨 Setup Frontend Admin

### Bước 1: Di chuyển đến thư mục frontend

Mở terminal mới (giữ backend đang chạy) và:

```bash
# Từ thư mục gốc dự án
cd admin-web\frontend

# Hoặc đường dẫn đầy đủ
cd E:\namngu\admin-web\frontend
```

### Bước 2: Cài đặt Dependencies

```bash
npm install
```

### Bước 3: Chạy Frontend

```bash
npm run dev
```

Frontend sẽ chạy tại: **http://localhost:3001**

### Bước 4: Đăng nhập Admin

- URL: http://localhost:3001/login
- Phone: `admin`
- Password: `admin123`

## 📱 Setup Android App

### Bước 1: Mở Project

1. Mở **Android Studio**
2. File > Open > Chọn folder `LTTBDD-main`

### Bước 2: Cấu hình API URL

File: `app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`

**Cho Android Emulator:**
```kotlin
private const val BASE_URL = "http://10.0.2.2:3000/api/"
```

**Cho Real Device:**
1. Tìm IP máy tính: `ipconfig` (Windows) hoặc `ifconfig` (Mac/Linux)
2. Sửa thành: `http://YOUR_IP:3000/api/`
   - Ví dụ: `http://192.168.1.100:3000/api/`
3. Đảm bảo máy tính và điện thoại cùng mạng WiFi

### Bước 3: Build và Run

1. Sync Gradle files
2. Chạy app trên emulator hoặc real device

## ✅ Kiểm Tra

### Backend
- ✅ API chạy: http://localhost:3000/api
- ✅ Test login: `POST http://localhost:3000/api/auth/login`
  ```json
  {
    "phoneNumber": "admin",
    "password": "admin123"
  }
  ```

### Frontend
- ✅ Admin panel: http://localhost:3001
- ✅ Đăng nhập thành công
- ✅ Xem được Users, Orders, Vouchers

### Android App
- ✅ Đăng ký/Đăng nhập thành công
- ✅ Tạo đơn hàng thành công
- ✅ Xem được orders

## 🔧 Troubleshooting

### Backend không kết nối được database
- Kiểm tra MySQL đã start chưa
- Kiểm tra database `CoffeShop` đã được tạo chưa
- Kiểm tra username/password trong `app.module.ts`

### Frontend không load được data
- Kiểm tra backend đã chạy chưa (http://localhost:3000/api)
- Kiểm tra token trong localStorage (F12 > Application > Local Storage)
- Thử refresh lại trang (Ctrl + F5)

### Android app không kết nối được backend
- Kiểm tra backend đã chạy chưa
- Kiểm tra BASE_URL trong `ApiClient.kt`
- Với real device: đảm bảo cùng mạng WiFi và firewall không chặn port 3000
- Kiểm tra network security config đã được thêm vào `AndroidManifest.xml`

### Port đã được sử dụng
- Backend (3000): Đổi port trong `src/main.ts` hoặc kill process đang dùng port 3000
- Frontend (3001): Đổi port trong `vite.config.ts` hoặc kill process đang dùng port 3001

## 📝 Lưu Ý

1. **Backend phải chạy trước** khi test Frontend hoặc Android app
2. **Database phải được import** trước khi chạy backend
3. Với **real device**, đảm bảo máy tính và điện thoại **cùng mạng WiFi**
4. **Firewall** có thể chặn port 3000, cần mở port hoặc tắt firewall tạm thời

## 🎯 Quick Start (Tóm tắt)

**Lưu ý:** Thay `E:\namngu` bằng đường dẫn thực tế của bạn

```bash
# 1. Setup Database
# Import admin-web/backend/database/database.sql vào MySQL

# 2. Backend
cd E:\namngu\admin-web\backend
npm install
npm run start:dev

# 3. Frontend (terminal mới)
cd E:\namngu\admin-web\frontend
npm install
npm run dev

# 4. Android
# Mở Android Studio > Open LTTBDD-main > Run
```

## 🌐 Deploy Cho Production (App + Admin Local)

Nếu bạn muốn:
- ✅ Người dùng tải APK và dùng được
- ✅ Admin chạy trên máy local để quản lý

**Xem hướng dẫn chi tiết:** `HUONG_DAN_ADMIN_LOCAL.md`

### Tóm tắt:

1. **Deploy Backend lên server công khai** (Railway, Render, VPS...)
2. **Cập nhật APK:** Sửa `BASE_URL` trong `ApiClient.kt` → URL backend công khai
3. **Cấu hình Admin Local:**
   - Tạo file `admin-web/frontend/.env`:
     ```env
     VITE_API_URL=https://your-backend.railway.app/api
     ```
   - Chạy: `npm run dev` (vẫn chạy localhost:3001)
   - Admin sẽ kết nối đến backend công khai

**Kết quả:**
- ✅ App users: Dùng app, dữ liệu lưu trên cloud
- ✅ Admin: Quản lý từ máy local, xem được tất cả dữ liệu

## ⚠️ Lưu Ý Quan Trọng

1. **Đường dẫn:** Đảm bảo bạn đang ở đúng thư mục trước khi chạy lệnh
2. **Kiểm tra đường dẫn hiện tại:**
   ```bash
   # Windows PowerShell
   pwd
   
   # Hoặc
   Get-Location
   ```
3. **Nếu lỗi "Cannot find path":**
   - Kiểm tra bạn đã extract/clone dự án chưa
   - Kiểm tra đường dẫn có đúng không
   - Dùng đường dẫn đầy đủ: `cd E:\namngu\admin-web\backend`

## 📞 Support

Nếu gặp lỗi, kiểm tra:
1. Console logs của backend
2. Browser console (F12) của frontend
3. Logcat của Android app
4. Network tab để xem API calls
