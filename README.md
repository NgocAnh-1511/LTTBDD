# Coffee Shop Android App

Ứng dụng Android cho Coffee Shop, kết nối với REST API backend.

## 📋 Yêu Cầu

- **Android Studio**: Arctic Fox trở lên
- **JDK**: 11+
- **Backend**: Phải chạy tại http://localhost:3000 (hoặc IP máy tính)

## 🚀 Setup

### Bước 1: Clone và Mở Project

1. Clone repository hoặc copy folder `LTTBDD-main`
2. Mở **Android Studio**
3. File > Open > Chọn folder `LTTBDD-main`

### Bước 2: Cấu hình API URL

File: `app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`

**Cho Android Emulator:**
```kotlin
private const val BASE_URL = "http://10.0.2.2:3000/api/"
```

**Cho Real Device:**
1. Tìm IP máy tính:
   - Windows: `ipconfig` trong CMD
   - Mac/Linux: `ifconfig` trong Terminal
2. Sửa BASE_URL thành: `http://YOUR_IP:3000/api/`
   - Ví dụ: `http://192.168.1.100:3000/api/`
3. Đảm bảo máy tính và điện thoại **cùng mạng WiFi**

### Bước 3: Sync và Build

1. Android Studio sẽ tự động sync Gradle
2. Nếu có lỗi, click **Sync Project with Gradle Files**
3. Build project: Build > Make Project

### Bước 4: Chạy App

1. Kết nối emulator hoặc real device
2. Click **Run** (Shift + F10)
3. Chọn device và chạy

## ⚙️ Cấu Hình

### Network Security

App đã được cấu hình để cho phép HTTP (cleartext) traffic cho development:
- `AndroidManifest.xml`: `android:usesCleartextTraffic="true"`
- `network_security_config.xml`: Cho phép cleartext cho localhost

### Dependencies

App sử dụng:
- **Retrofit**: REST API client
- **OkHttp**: HTTP client với logging
- **Coroutines**: Async operations
- **Gson**: JSON parsing

## 🔧 Troubleshooting

### App không kết nối được backend

1. **Kiểm tra backend đã chạy:**
   - Mở browser: http://localhost:3000/api
   - Phải thấy response hoặc 401 (nếu chưa login)

2. **Kiểm tra BASE_URL:**
   - Emulator: `http://10.0.2.2:3000/api/`
   - Real device: `http://YOUR_IP:3000/api/`

3. **Kiểm tra network:**
   - Real device: Đảm bảo cùng WiFi với máy tính
   - Firewall: Mở port 3000 hoặc tắt firewall tạm thời

4. **Xem logs:**
   - Android Studio > Logcat
   - Filter: `ApiClient` hoặc `OrderManager`
   - Tìm lỗi network

### Lỗi CLEARTEXT communication

App đã được cấu hình để cho phép HTTP. Nếu vẫn lỗi:
- Kiểm tra `AndroidManifest.xml` có `usesCleartextTraffic="true"`
- Kiểm tra `network_security_config.xml` đã được thêm vào manifest

### Build errors

1. **Sync Gradle:**
   - File > Sync Project with Gradle Files

2. **Clean và Rebuild:**
   - Build > Clean Project
   - Build > Rebuild Project

3. **Invalidate Caches:**
   - File > Invalidate Caches / Restart

## 📱 Test App

### Đăng ký User mới
1. Mở app
2. Click "Đăng ký"
3. Nhập phone number, password
4. (Optional) Nhập fullName, email

### Đăng nhập
- Phone: `0846230059`
- Password: `Nam26122005@`

Hoặc dùng admin:
- Phone: `admin`
- Password: `admin123`

### Tạo đơn hàng
1. Thêm sản phẩm vào giỏ hàng
2. Vào giỏ hàng
3. Click "Thanh toán"
4. Điền thông tin đơn hàng
5. Click "Xác nhận thanh toán"

## 📝 Lưu Ý

1. **Backend phải chạy trước** khi test app
2. Với **real device**, đảm bảo cùng mạng WiFi
3. **Firewall** có thể chặn port 3000
4. Xem logs trong **Logcat** để debug

## 🔗 Liên Kết

- Backend API: http://localhost:3000/api
- Admin Panel: http://localhost:3001

