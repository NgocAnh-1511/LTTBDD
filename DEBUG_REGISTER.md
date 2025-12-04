# Debug Đăng Ký

## 🔍 Kiểm tra

### 1. Backend có đang chạy không?
```powershell
# Kiểm tra backend
cd E:\namngu\admin-web\backend
npm run start:dev
```

Backend phải chạy tại: `http://localhost:3000`

### 2. Kiểm tra Logcat

Filter logcat với tag:
- `UserManager` - Logs từ UserManager
- `LoginActivity` - Logs từ LoginActivity  
- `ApiClient` - Logs từ ApiClient
- `OkHttp` - HTTP requests/responses

### 3. Các lỗi có thể gặp

#### a) Network Error
```
UnknownHostException hoặc ConnectException
```
**Giải pháp:**
- Kiểm tra backend có đang chạy không
- Kiểm tra BASE_URL trong `ApiClient.kt`
- Emulator: `http://10.0.2.2:3000/api/`
- Real device: `http://[IP_MÁY_TÍNH]:3000/api/`

#### b) 400 Bad Request
```
Register failed: 400 - {"message":[...]}
```
**Giải pháp:**
- Kiểm tra validation errors trong message
- Đã sửa email validation
- Đã sửa fullName validation

#### c) 401 Unauthorized
```
Register failed: 401
```
**Giải pháp:**
- Không cần token cho register, nếu có lỗi này thì có vấn đề với backend

### 4. Test API trực tiếp

Dùng Postman hoặc curl:
```bash
POST http://localhost:3000/api/auth/register
Content-Type: application/json

{
  "phoneNumber": "0123456789",
  "password": "password123",
  "fullName": "Test User",
  "email": null
}
```

## 📝 Đã thêm logging

- ✅ Log khi bắt đầu đăng ký
- ✅ Log request details
- ✅ Log response code
- ✅ Log error details
- ✅ Log network errors riêng biệt

## 🔄 Các bước debug

1. Mở Logcat trong Android Studio
2. Filter: `UserManager` hoặc `LoginActivity`
3. Thử đăng ký
4. Xem logs để biết:
   - Request có được gửi không?
   - Response code là gì?
   - Error message là gì?

## ⚠️ Lưu ý

- Backend phải đang chạy
- Network security config đã được thêm vào AndroidManifest
- BASE_URL đúng với môi trường (emulator vs device)

