# 📱 Hướng Dẫn: Làm Sao Để Người Khác Dùng APK Và Lưu Vào Database Của Bạn

## ❌ Câu Trả Lời Ngắn Gọn

**Hiện tại: KHÔNG** - Nếu người khác tải APK về và cài đặt, họ **KHÔNG THỂ** lưu dữ liệu vào database của bạn.

## 🔍 Tại Sao?

1. **APK đang trỏ đến localhost:**
   - File `ApiClient.kt` có `BASE_URL = "http://10.0.2.2:3000/api/"`
   - Đây là địa chỉ localhost, chỉ máy bạn mới truy cập được

2. **Backend đang chạy trên máy bạn:**
   - Backend chạy tại `http://localhost:3000`
   - Chỉ máy tính của bạn mới truy cập được

3. **Database ở máy local:**
   - MySQL đang chạy trên máy bạn
   - Người khác không thể kết nối được

**Kết quả:** Người khác tải APK → App không kết nối được backend → Không đăng ký/đặt hàng được

---

## ✅ Giải Pháp: Deploy Backend Lên Internet

Để người khác dùng được APK và dữ liệu lưu vào database của bạn, bạn cần:

### **Bước 1: Deploy Backend Lên Server Công Khai**

Bạn có thể dùng các dịch vụ sau (có cả miễn phí):

#### **Option 1: Railway (Dễ nhất, có miễn phí)**
1. Đăng ký: https://railway.app
2. Tạo project mới
3. Connect GitHub hoặc upload code
4. Chọn folder `admin-web/backend`
5. Thêm biến môi trường:
   ```
   DB_HOST=your-mysql-host
   DB_PORT=3306
   DB_USERNAME=your-username
   DB_PASSWORD=your-password
   DB_NAME=CoffeShop
   JWT_SECRET=your-secret-key
   ```
6. Railway sẽ cho URL: `https://your-app.railway.app`

#### **Option 2: Render (Miễn phí)**
1. Đăng ký: https://render.com
2. Tạo Web Service mới
3. Connect GitHub repository
4. Chọn folder `admin-web/backend`
5. Cấu hình tương tự Railway

#### **Option 3: VPS (DigitalOcean, AWS, Vultr)**
- Tự quản lý server
- Linh hoạt hơn nhưng phức tạp hơn
- Chi phí ~$5-10/tháng

### **Bước 2: Setup Database Trên Cloud**

Bạn cần database có thể truy cập từ internet:

#### **Option A: Railway MySQL (Tích hợp sẵn)**
- Railway có MySQL service
- Tự động kết nối với backend

#### **Option B: PlanetScale (Miễn phí)**
1. Đăng ký: https://planetscale.com
2. Tạo database mới
3. Import file `admin-web/backend/database/database.sql`
4. Lấy connection string

#### **Option C: MySQL Trên VPS**
- Cài MySQL trên cùng VPS với backend
- Cấu hình firewall mở port 3306

### **Bước 3: Cập Nhật APK**

Sau khi có URL backend công khai (ví dụ: `https://your-app.railway.app`):

1. Mở file: `LTTBDD-main/app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`

2. Tìm dòng:
   ```kotlin
   private const val DEFAULT_BASE_URL = "http://10.0.2.2:3000/api/"
   ```

3. Sửa thành:
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://your-app.railway.app/api/"
   ```
   (Thay `your-app.railway.app` bằng URL thực tế của bạn)

4. Build lại APK:
   - Android Studio > Build > Build Bundle(s) / APK(s) > Build APK(s)
   - Hoặc: Build > Generate Signed Bundle / APK

5. Phân phối APK mới

---

## 🧪 Giải Pháp Tạm Thời: Dùng Ngrok (Chỉ Cho Testing)

**Lưu ý:** Chỉ dùng để test, không phù hợp production!

1. **Cài Ngrok:**
   - Download: https://ngrok.com
   - Hoặc: `npm install -g ngrok`

2. **Chạy Backend:**
   ```bash
   cd admin-web/backend
   npm run start:dev
   ```

3. **Expose Backend:**
   ```bash
   ngrok http 3000
   ```

4. **Lấy URL:**
   - Ngrok hiển thị: `https://abc123.ngrok.io`
   - URL này có thể truy cập từ internet

5. **Cập Nhật APK:**
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://abc123.ngrok.io/api/"
   ```

6. **Build và test**

**⚠️ Lưu ý:** URL sẽ thay đổi mỗi lần restart ngrok (trừ khi dùng tài khoản trả phí)

---

## 📋 Checklist

### Trước Khi Phân Phối APK:

- [ ] Backend đã deploy lên server công khai
- [ ] Database đã setup trên cloud
- [ ] Test API hoạt động (dùng Postman hoặc browser)
- [ ] Đã cập nhật `BASE_URL` trong `ApiClient.kt`
- [ ] Đã build APK release
- [ ] Test đăng ký/đăng nhập/đặt hàng
- [ ] Kiểm tra dữ liệu lưu vào database

### Security:

- [ ] Đổi JWT_SECRET mạnh
- [ ] Đổi password admin mặc định
- [ ] Cấu hình firewall
- [ ] Setup SSL/HTTPS (bắt buộc)

---

## 💡 Ví Dụ Cụ Thể

### Scenario: Deploy lên Railway

1. **Backend URL:** `https://coffee-shop-backend.railway.app`
2. **API Base:** `https://coffee-shop-backend.railway.app/api`
3. **Cập nhật `ApiClient.kt`:**
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://coffee-shop-backend.railway.app/api/"
   ```
4. **Build APK và phân phối**

### Scenario: Deploy lên VPS

1. **Server IP:** `123.45.67.89`
2. **Backend URL:** `http://123.45.67.89:3000`
3. **API Base:** `http://123.45.67.89:3000/api`
4. **Cập nhật `ApiClient.kt`:**
   ```kotlin
   private const val DEFAULT_BASE_URL = "http://123.45.67.89:3000/api/"
   ```
5. **Lưu ý:** Cần mở port 3000 trên firewall

---

## ❓ Câu Hỏi Thường Gặp

### Q: Có thể dùng localhost không?
**A:** Không, người khác không thể truy cập localhost của bạn. Cần server công khai.

### Q: Chi phí deploy là bao nhiêu?
**A:** 
- **Miễn phí:** Railway, Render (có giới hạn)
- **Rẻ:** VPS ~$5-10/tháng
- **Trung bình:** AWS/GCP ~$10-20/tháng

### Q: Database có cần deploy không?
**A:** Có, database cũng cần truy cập được từ server backend. Có thể:
- Dùng cloud database (PlanetScale, Railway MySQL)
- Hoặc MySQL trên cùng VPS với backend

### Q: APK có cần build lại mỗi lần thay đổi API URL không?
**A:** Có, cần build lại APK mỗi khi thay đổi `BASE_URL`. 

**Lưu ý:** Code đã được cập nhật để hỗ trợ cấu hình URL động qua SharedPreferences, nhưng mặc định vẫn dùng `DEFAULT_BASE_URL`.

---

## 📞 Cần Giúp Đỡ?

Xem file `DEPLOYMENT_GUIDE.md` để biết hướng dẫn chi tiết hơn về:
- Các bước deploy cụ thể
- Cấu hình production
- Troubleshooting

