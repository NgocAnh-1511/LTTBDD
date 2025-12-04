# 🚀 Hướng Dẫn Deploy Backend & Build APK

## 📋 Tổng Quan

Hiện tại, Android app đang kết nối với backend qua `http://10.0.2.2:3000/api/` - chỉ hoạt động trên **emulator**.

Để người khác tải APK và sử dụng, bạn cần:
1. ✅ Deploy backend lên server công khai (có thể truy cập từ internet)
2. ✅ Build APK với BASE_URL trỏ đến server của bạn
3. ✅ Đảm bảo MySQL database có thể truy cập từ backend server

---

## 🌐 Bước 1: Deploy Backend Lên Server

### Option 1: Railway.app (Khuyến nghị - Miễn phí)

1. **Đăng ký Railway**: https://railway.app
2. **Tạo Project mới**
3. **Deploy Backend**:
   ```bash
   # Từ thư mục admin-web/backend
   # Railway sẽ tự động detect Node.js và chạy npm start
   ```
4. **Setup Environment Variables**:
   - `DB_HOST`: MySQL host (Railway cung cấp hoặc dùng Railway MySQL)
   - `DB_PORT`: 3306
   - `DB_USERNAME`: MySQL username
   - `DB_PASSWORD`: MySQL password
   - `DB_DATABASE`: CoffeShop
   - `JWT_SECRET`: Random string (ví dụ: `your-secret-key-here`)
   - `PORT`: 3000

5. **Lấy URL**: Railway sẽ cung cấp URL dạng `https://your-app.railway.app`

### Option 2: Render.com (Miễn phí)

1. Đăng ký: https://render.com
2. Tạo Web Service mới
3. Connect GitHub repo hoặc upload code
4. Setup environment variables tương tự Railway
5. Lấy URL: `https://your-app.onrender.com`

### Option 3: VPS/Server riêng

1. Mua VPS (DigitalOcean, AWS, v.v.)
2. Cài đặt Node.js, MySQL
3. Clone code và chạy:
   ```bash
   cd admin-web/backend
   npm install
   npm run build
   npm run start:prod
   ```
4. Setup Nginx reverse proxy (tùy chọn)
5. Mở port 3000 trong firewall

### Option 4: Ngrok (Chỉ để test - KHÔNG dùng cho production)

```bash
# Chạy backend local
cd admin-web/backend
npm run start:dev

# Terminal khác, chạy ngrok
ngrok http 3000

# Lấy URL: https://xxxx.ngrok.io
# ⚠️ URL này thay đổi mỗi lần restart ngrok
```

---

## 📱 Bước 2: Cập Nhật BASE_URL Trong Android App

### Cách 1: Sửa trực tiếp trong code (Khuyến nghị cho production)

1. Mở file: `LTTBDD-main/app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`
2. Sửa dòng 14:
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://your-backend.railway.app/api/"
   // Thay your-backend.railway.app bằng URL thực tế của bạn
   ```
3. Build APK mới

### Cách 2: Build Variants (Cho nhiều môi trường)

Tạo file `build.gradle.kts` với build variants:

```kotlin
android {
    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000/api/\"")
        }
        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"https://your-backend.railway.app/api/\"")
        }
    }
}
```

Sau đó trong `ApiClient.kt`:
```kotlin
private const val DEFAULT_BASE_URL = BuildConfig.BASE_URL
```

### Cách 3: Cho phép user cấu hình URL (Cho testing)

App đã hỗ trợ set URL động qua `ApiClient.setBaseUrl()`. Bạn có thể thêm màn hình Settings để user nhập URL.

---

## 🔧 Bước 3: Build APK Release

### Trong Android Studio:

1. **Build > Generate Signed Bundle / APK**
2. Chọn **APK**
3. Tạo keystore mới (hoặc dùng keystore có sẵn)
4. Chọn **release** build variant
5. Click **Finish**

### Hoặc dùng command line:

```bash
cd LTTBDD-main
./gradlew assembleRelease

# APK sẽ ở: app/build/outputs/apk/release/app-release.apk
```

---

## ✅ Bước 4: Kiểm Tra

### 1. Test Backend API:

```bash
# Kiểm tra backend có hoạt động không
curl https://your-backend.railway.app/api/users

# Hoặc mở browser:
https://your-backend.railway.app/api/users
```

### 2. Test APK:

1. Cài APK lên điện thoại thật (không phải emulator)
2. Đăng ký tài khoản mới
3. Đặt hàng
4. Kiểm tra database xem có dữ liệu mới không

---

## 🔒 Bước 5: Bảo Mật (Quan trọng!)

### 1. HTTPS (Bắt buộc cho production)

- Railway/Render tự động cung cấp HTTPS
- Nếu dùng VPS, setup SSL certificate (Let's Encrypt)

### 2. CORS Configuration

Trong `admin-web/backend/src/main.ts`:

```typescript
app.enableCors({
  origin: '*', // Cho phép tất cả (chỉ dùng cho dev)
  // Production: chỉ cho phép domain cụ thể
  // origin: ['https://your-admin-panel.com'],
  credentials: true,
});
```

### 3. Environment Variables

**KHÔNG** commit file `.env` lên Git. Dùng environment variables của hosting service.

### 4. Database Security

- Đặt password mạnh cho MySQL
- Chỉ cho phép kết nối từ backend server (whitelist IP)
- Backup database thường xuyên

---

## 📊 Kiểm Tra Database

Sau khi có user đăng ký/đặt hàng qua APK:

```sql
-- Xem users mới
SELECT * FROM users ORDER BY created_at DESC LIMIT 10;

-- Xem orders mới
SELECT * FROM orders ORDER BY order_date DESC LIMIT 10;

-- Xem order items
SELECT * FROM order_items ORDER BY id DESC LIMIT 10;
```

---

## 🐛 Troubleshooting

### Lỗi: "CLEARTEXT communication not permitted"

- Backend phải dùng HTTPS (không phải HTTP)
- Hoặc thêm domain vào `network_security_config.xml` (chỉ cho dev)

### Lỗi: "Connection refused" hoặc "Unknown host"

- Kiểm tra BASE_URL có đúng không
- Kiểm tra backend có đang chạy không
- Kiểm tra firewall có chặn port không

### Lỗi: "401 Unauthorized"

- Kiểm tra JWT_SECRET có giống nhau giữa backend và app không
- Kiểm tra token có được gửi đúng trong header không

### Database không có dữ liệu mới

- Kiểm tra backend logs xem có lỗi không
- Kiểm tra database connection string
- Kiểm tra MySQL có chấp nhận connection từ backend server không

---

## 📝 Checklist Trước Khi Phát Hành APK

- [ ] Backend đã deploy và có thể truy cập từ internet
- [ ] BASE_URL trong APK trỏ đến backend production
- [ ] Database connection hoạt động
- [ ] Test đăng ký user mới
- [ ] Test đặt hàng
- [ ] Kiểm tra dữ liệu có lưu vào database không
- [ ] HTTPS đã được setup
- [ ] CORS đã được cấu hình đúng
- [ ] Environment variables đã được set
- [ ] APK đã được sign với release keystore

---

## 🎯 Tóm Tắt

**Câu trả lời ngắn gọn:**

✅ **CÓ**, nếu bạn:
1. Deploy backend lên server công khai (Railway, Render, VPS...)
2. Sửa BASE_URL trong APK thành URL của server
3. Build và phân phối APK mới

❌ **KHÔNG**, nếu:
- Backend chỉ chạy localhost (localhost:3000)
- BASE_URL vẫn là `10.0.2.2` (chỉ hoạt động trên emulator)
- Chưa deploy backend lên internet

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, kiểm tra:
1. Backend logs trên hosting service
2. Android Logcat khi chạy app
3. Network requests trong Android Studio Network Profiler


