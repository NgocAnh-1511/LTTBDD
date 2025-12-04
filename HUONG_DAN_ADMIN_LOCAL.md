# 🎯 Hướng Dẫn: App Dùng Backend Công Khai, Admin Chạy Local

## ✅ Kiến Trúc Đề Xuất

```
┌─────────────────┐
│   Mobile App    │  →  Kết nối đến Backend công khai
│   (APK)         │      (https://your-backend.com/api)
└─────────────────┘

┌─────────────────┐
│  Admin Panel    │  →  Kết nối đến Backend công khai
│  (Local)        │      (https://your-backend.com/api)
│  localhost:3001 │
└─────────────────┘

┌─────────────────┐
│    Backend      │  ←  Deploy lên server công khai
│  (Production)   │      (Railway, Render, VPS...)
│  Port 3000     │
└─────────────────┘
         ↓
┌─────────────────┐
│    Database     │  ←  MySQL trên cloud
│    (Cloud)      │      (PlanetScale, Railway MySQL...)
└─────────────────┘
```

## 📋 Các Bước Thực Hiện

### **Bước 1: Deploy Backend Lên Server Công Khai**

Bạn **PHẢI** deploy backend lên server công khai để:
- ✅ App có thể kết nối được
- ✅ Admin local có thể quản lý từ xa

#### **Option 1: Railway (Khuyến nghị - Dễ nhất)**

1. Đăng ký: https://railway.app
2. Tạo project mới
3. Add Service → Deploy from GitHub
4. Chọn repository và folder `admin-web/backend`
5. Railway tự động detect NestJS và deploy
6. Thêm biến môi trường:
   ```
   DB_HOST=your-mysql-host
   DB_PORT=3306
   DB_USERNAME=your-username
   DB_PASSWORD=your-password
   DB_NAME=CoffeShop
   PORT=3000
   JWT_SECRET=your-strong-secret-key
   NODE_ENV=production
   ```
7. Railway sẽ cung cấp URL: `https://your-app.railway.app`
8. **Lưu URL này lại** - sẽ dùng cho cả app và admin

#### **Option 2: Render**

1. Đăng ký: https://render.com
2. New → Web Service
3. Connect GitHub và chọn folder `admin-web/backend`
4. Cấu hình tương tự Railway

#### **Option 3: VPS**

- Tự quản lý server
- Cần cấu hình firewall, nginx, PM2...

### **Bước 2: Setup Database Trên Cloud**

#### **Option A: Railway MySQL (Tích hợp)**

1. Trong Railway project, Add Service → Database → MySQL
2. Railway tự động tạo database
3. Lấy connection string từ Settings
4. Import database:
   - Mở MySQL terminal hoặc dùng MySQL Workbench
   - Import file `admin-web/backend/database/database.sql`

#### **Option B: PlanetScale (Miễn phí)**

1. Đăng ký: https://planetscale.com
2. Tạo database mới
3. Import `admin-web/backend/database/database.sql`
4. Lấy connection string

#### **Option C: MySQL Trên VPS**

- Cài MySQL trên VPS
- Import database
- Cấu hình firewall

### **Bước 3: Cập Nhật APK**

Sau khi có URL backend công khai (ví dụ: `https://coffee-backend.railway.app`):

1. Mở file: `LTTBDD-main/app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`

2. Tìm dòng:
   ```kotlin
   private const val DEFAULT_BASE_URL = "http://10.0.2.2:3000/api/"
   ```

3. Sửa thành:
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://coffee-backend.railway.app/api/"
   ```
   (Thay bằng URL thực tế của bạn)

4. Build APK:
   - Android Studio > Build > Build Bundle(s) / APK(s) > Build APK(s)
   - Hoặc: Build > Generate Signed Bundle / APK

5. Phân phối APK

### **Bước 4: Cấu Hình Admin Panel Chạy Local**

Admin panel sẽ chạy trên máy bạn (localhost:3001) nhưng kết nối đến backend công khai.

1. **Tạo file `.env` trong thư mục `admin-web/frontend/`:**

   ```env
   VITE_API_URL=https://coffee-backend.railway.app/api
   ```
   (Thay bằng URL backend thực tế của bạn)

2. **Chạy Admin Panel:**

   ```bash
   cd admin-web/frontend
   npm install  # Nếu chưa cài
   npm run dev
   ```

3. **Truy cập Admin Panel:**

   - Mở browser: http://localhost:3001
   - Đăng nhập với:
     - Phone: `admin`
     - Password: `admin123`

4. **Bạn có thể quản lý:**
   - ✅ Xem tất cả users (bao gồm người dùng đăng ký từ app)
   - ✅ Xem tất cả orders (bao gồm đơn hàng từ app)
   - ✅ Quản lý products, categories, vouchers, banners
   - ✅ Tất cả dữ liệu đều lưu trên database cloud

---

## 🎯 Kết Quả

### **Người Dùng (Tải APK):**
- ✅ Tải APK và cài đặt
- ✅ Đăng ký tài khoản → Lưu vào database cloud
- ✅ Đặt hàng → Lưu vào database cloud
- ✅ Tất cả dữ liệu lưu trên server của bạn

### **Bạn (Admin Local):**
- ✅ Chạy admin panel trên máy local (localhost:3001)
- ✅ Quản lý tất cả dữ liệu từ xa
- ✅ Xem users, orders từ app
- ✅ Không cần deploy frontend admin

---

## 📝 Ví Dụ Cụ Thể

### Scenario: Deploy lên Railway

1. **Backend URL:** `https://coffee-shop-api.railway.app`
2. **API Base:** `https://coffee-shop-api.railway.app/api`

3. **Cập nhật APK (`ApiClient.kt`):**
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://coffee-shop-api.railway.app/api/"
   ```

4. **Cập nhật Admin (`admin-web/frontend/.env`):**
   ```env
   VITE_API_URL=https://coffee-shop-api.railway.app/api
   ```

5. **Chạy Admin:**
   ```bash
   cd admin-web/frontend
   npm run dev
   # Truy cập: http://localhost:3001
   ```

---

## ⚙️ Cấu Hình Chi Tiết

### **File: `admin-web/frontend/.env`**

```env
# Backend API URL (công khai)
VITE_API_URL=https://your-backend.railway.app/api
```

**Lưu ý:**
- File `.env` phải ở thư mục `admin-web/frontend/`
- Sau khi sửa `.env`, cần restart dev server (`npm run dev`)
- Vite chỉ đọc biến môi trường bắt đầu với `VITE_`

### **File: `LTTBDD-main/app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`**

```kotlin
private const val DEFAULT_BASE_URL = "https://your-backend.railway.app/api/"
```

**Lưu ý:**
- Phải có `/api/` ở cuối
- Phải dùng HTTPS (không dùng HTTP cho production)
- Sau khi sửa, cần build lại APK

---

## 🔒 Security

### **Backend (Production):**

1. **Đổi JWT_SECRET mạnh:**
   ```env
   JWT_SECRET=your-very-strong-random-secret-key-here-min-32-chars
   ```

2. **Đổi password admin:**
   - Đăng nhập vào database
   - Update password cho user `admin` (hash bằng bcrypt)

3. **CORS đã được cấu hình:**
   - Code đã có `app.enableCors({ origin: true })`
   - Cho phép tất cả origins (có thể hạn chế sau)

4. **SSL/HTTPS:**
   - Railway/Render tự động có SSL
   - VPS cần cấu hình nginx với Let's Encrypt

---

## ✅ Checklist

### **Backend:**
- [ ] Deploy backend lên Railway/Render/VPS
- [ ] Setup database trên cloud
- [ ] Import database từ `database.sql`
- [ ] Cấu hình biến môi trường (DB, JWT_SECRET)
- [ ] Test API hoạt động (dùng Postman hoặc browser)
- [ ] Lưu URL backend lại

### **APK:**
- [ ] Cập nhật `BASE_URL` trong `ApiClient.kt`
- [ ] Build APK release
- [ ] Test đăng ký/đăng nhập/đặt hàng
- [ ] Kiểm tra dữ liệu lưu vào database

### **Admin Panel:**
- [ ] Tạo file `.env` với `VITE_API_URL`
- [ ] Chạy `npm run dev`
- [ ] Test đăng nhập admin
- [ ] Kiểm tra xem được users/orders từ app

---

## 🐛 Troubleshooting

### **Admin không kết nối được backend:**

1. Kiểm tra file `.env`:
   ```bash
   # Trong admin-web/frontend/
   cat .env
   # Phải có: VITE_API_URL=https://your-backend.com/api
   ```

2. Kiểm tra backend có đang chạy:
   - Mở browser: `https://your-backend.railway.app/api`
   - Phải thấy response hoặc 401 (nếu chưa login)

3. Restart dev server:
   ```bash
   # Dừng (Ctrl+C) và chạy lại
   npm run dev
   ```

### **App không kết nối được:**

1. Kiểm tra `BASE_URL` trong `ApiClient.kt`
2. Kiểm tra backend có đang chạy
3. Kiểm tra internet connection
4. Xem logs trong Logcat (Android Studio)

### **CORS Error:**

- Backend đã enable CORS
- Kiểm tra backend có đang chạy không
- Kiểm tra URL có đúng không

---

## 💡 Lợi Ích Của Kiến Trúc Này

✅ **App users:** Dùng app bình thường, dữ liệu lưu trên cloud  
✅ **Admin:** Quản lý từ máy local, không cần deploy frontend  
✅ **Database:** Tập trung trên cloud, dễ backup  
✅ **Chi phí:** Chỉ cần deploy backend (miễn phí với Railway/Render)  
✅ **Bảo mật:** Admin chỉ chạy local, không expose ra internet  

---

## 📞 Cần Giúp Đỡ?

Xem thêm:
- `DEPLOYMENT_GUIDE.md` - Hướng dẫn deploy chi tiết
- `HUONG_DAN_DEPLOY.md` - Hướng dẫn bằng tiếng Việt

