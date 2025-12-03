# 🚀 Hướng Dẫn Deploy - Để Người Khác Dùng APK

## ❌ Vấn Đề Hiện Tại

**Nếu người khác tải APK về và cài đặt, họ KHÔNG THỂ lưu dữ liệu vào database của bạn vì:**

1. ✅ App đang hardcode `BASE_URL = "http://10.0.2.2:3000/api/"` (chỉ dùng cho emulator)
2. ✅ Backend đang chạy trên `localhost:3000` (chỉ máy bạn mới truy cập được)
3. ✅ Database MySQL đang ở máy local của bạn

**Kết quả:** Người khác tải APK → App không kết nối được backend → Không đăng ký/đặt hàng được

---

## ✅ Giải Pháp

### **Giải Pháp 1: Deploy Backend Lên Server Công Khai (Khuyến Nghị)**

Để người khác dùng được APK và dữ liệu lưu vào database của bạn, bạn cần:

#### **Bước 1: Chọn Nền Tảng Deploy**

**Tùy chọn miễn phí/thấp:**
- **Railway** (https://railway.app) - Miễn phí $5/tháng
- **Render** (https://render.com) - Miễn phí tier
- **Heroku** (https://heroku.com) - Có phí
- **VPS** (DigitalOcean, AWS EC2, Vultr) - ~$5-10/tháng

**Tùy chọn có phí:**
- **AWS EC2** - Linh hoạt, tự quản lý
- **DigitalOcean Droplet** - Dễ dùng
- **Google Cloud Platform**

#### **Bước 2: Deploy Backend**

**Ví dụ với Railway (dễ nhất):**

1. Đăng ký tài khoản Railway
2. Tạo project mới
3. Connect GitHub repository
4. Chọn folder `admin-web/backend`
5. Railway tự động detect NestJS và deploy
6. Thêm biến môi trường:
   ```
   DB_HOST=your-mysql-host
   DB_PORT=3306
   DB_USERNAME=your-username
   DB_PASSWORD=your-password
   DB_NAME=CoffeShop
   PORT=3000
   JWT_SECRET=your-secret-key
   ```
7. Railway sẽ cung cấp URL: `https://your-app.railway.app`

#### **Bước 3: Setup Database**

**Tùy chọn A: Dùng MySQL trên Cloud**
- **PlanetScale** (https://planetscale.com) - Miễn phí tier
- **AWS RDS** - Có phí
- **Railway MySQL** - Tích hợp sẵn

**Tùy chọn B: Dùng MySQL trên VPS**
- Cài đặt MySQL trên VPS
- Import database từ `admin-web/backend/database/database.sql`
- Cấu hình firewall mở port 3306

#### **Bước 4: Cập Nhật APK**

Sau khi có URL backend công khai (ví dụ: `https://your-app.railway.app`):

1. Mở file: `LTTBDD-main/app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`
2. Sửa `BASE_URL`:
   ```kotlin
   private const val BASE_URL = "https://your-app.railway.app/api/"
   ```
3. Build lại APK và phân phối

---

### **Giải Pháp 2: Dùng Ngrok (Cho Testing Tạm Thời)**

**Lưu ý:** Chỉ dùng cho testing, không phù hợp production vì:
- URL thay đổi mỗi lần restart (trừ khi dùng tài khoản trả phí)
- Không ổn định
- Giới hạn bandwidth

#### **Các Bước:**

1. **Cài đặt Ngrok:**
   ```bash
   # Download từ https://ngrok.com
   # Hoặc dùng npm:
   npm install -g ngrok
   ```

2. **Chạy Backend:**
   ```bash
   cd admin-web/backend
   npm run start:dev
   ```

3. **Expose Backend:**
   ```bash
   ngrok http 3000
   ```

4. **Lấy URL công khai:**
   - Ngrok sẽ hiển thị: `https://abc123.ngrok.io`
   - URL này có thể truy cập từ internet

5. **Cập Nhật APK:**
   ```kotlin
   private const val BASE_URL = "https://abc123.ngrok.io/api/"
   ```

6. **Build và test APK**

**⚠️ Lưu ý:** Mỗi lần restart ngrok, URL sẽ thay đổi (trừ khi dùng tài khoản trả phí với domain cố định)

---

### **Giải Pháp 3: Cấu Hình API URL Động (Nâng Cao)**

Cho phép người dùng nhập API URL trong app (không khuyến nghị cho end-user).

Xem file `DEPLOYMENT_ADVANCED.md` để biết cách implement.

---

## 📋 Checklist Deploy Production

### Backend
- [ ] Deploy backend lên server công khai
- [ ] Cấu hình database trên cloud
- [ ] Setup biến môi trường (DB credentials, JWT secret)
- [ ] Test API endpoints hoạt động
- [ ] Cấu hình CORS (đã có sẵn trong code)
- [ ] Setup SSL/HTTPS (bắt buộc cho production)

### Android App
- [ ] Cập nhật `BASE_URL` trong `ApiClient.kt`
- [ ] Build APK release
- [ ] Test đăng ký/đăng nhập/đặt hàng
- [ ] Kiểm tra dữ liệu lưu vào database

### Security
- [ ] Đổi JWT_SECRET mạnh
- [ ] Đổi password admin mặc định
- [ ] Cấu hình firewall
- [ ] Setup rate limiting (nếu cần)
- [ ] Backup database định kỳ

---

## 🔧 Cấu Hình Backend Cho Production

### 1. Tạo file `.env` trên server:

```env
# Database
DB_HOST=your-db-host
DB_PORT=3306
DB_USERNAME=your-username
DB_PASSWORD=your-strong-password
DB_NAME=CoffeShop

# Server
PORT=3000
NODE_ENV=production

# JWT
JWT_SECRET=your-very-strong-secret-key-here
JWT_EXPIRES_IN=7d

# CORS (nếu cần)
ALLOWED_ORIGINS=https://your-frontend-domain.com
```

### 2. Build và chạy production:

```bash
cd admin-web/backend
npm install
npm run build
npm run start:prod
```

Hoặc dùng PM2 để quản lý process:

```bash
npm install -g pm2
pm2 start dist/main.js --name coffee-backend
pm2 save
pm2 startup
```

---

## 🌐 Ví Dụ Cấu Hình

### Railway Deployment

1. **Backend URL:** `https://coffee-backend.railway.app`
2. **API Base:** `https://coffee-backend.railway.app/api`
3. **Cập nhật APK:**
   ```kotlin
   private const val BASE_URL = "https://coffee-backend.railway.app/api/"
   ```

### VPS Deployment

1. **Backend URL:** `http://your-server-ip:3000` (hoặc domain nếu có)
2. **API Base:** `http://your-server-ip:3000/api`
3. **Cập nhật APK:**
   ```kotlin
   private const val BASE_URL = "http://your-server-ip:3000/api/"
   ```
4. **Lưu ý:** Cần mở port 3000 trên firewall

---

## ❓ FAQ

### Q: Có thể dùng localhost không?
**A:** Không, người khác không thể truy cập localhost của bạn. Cần server công khai.

### Q: Database có cần deploy không?
**A:** Có, database cũng cần truy cập được từ server backend. Có thể:
- Dùng cloud database (PlanetScale, AWS RDS)
- Hoặc MySQL trên cùng VPS với backend

### Q: Chi phí deploy là bao nhiêu?
**A:** 
- **Miễn phí:** Railway, Render (có giới hạn)
- **Rẻ:** VPS ~$5-10/tháng
- **Trung bình:** AWS/GCP ~$10-20/tháng

### Q: APK có cần build lại mỗi lần thay đổi API URL không?
**A:** Có, cần build lại APK mỗi khi thay đổi `BASE_URL`. Hoặc implement tính năng cho phép người dùng nhập URL (không khuyến nghị).

---

## 📞 Support

Nếu gặp vấn đề khi deploy:
1. Kiểm tra logs của backend
2. Kiểm tra network connectivity
3. Kiểm tra firewall/security groups
4. Test API bằng Postman/curl trước khi test app

