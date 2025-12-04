# Fix Đăng Ký Không Hoạt Động

## ❌ Vấn đề

Đăng ký không hoạt động vì:
1. Backend yêu cầu `fullName` là bắt buộc (`@IsNotEmpty()`)
2. Android app gửi `fullName = ""` (chuỗi rỗng)
3. Backend validation reject request

## ✅ Đã sửa

### 1. Backend (`admin-web/backend/src/auth/dto/register.dto.ts`)
- ✅ Đổi `fullName` từ `@IsNotEmpty()` thành `@IsOptional()`
- ✅ Cho phép `fullName` là optional

### 2. Backend (`admin-web/backend/src/auth/auth.service.ts`)
- ✅ Thêm default value: nếu `fullName` rỗng, dùng `phoneNumber` làm tên

### 3. Android (`UserManager.kt`)
- ✅ Nếu `fullName` rỗng, tự động dùng `phoneNumber` làm tên mặc định
- ✅ Thêm error logging chi tiết để debug

## 🔄 Cần làm

1. **Restart backend server** để áp dụng thay đổi:
   ```powershell
   cd E:\namngu\admin-web\backend
   npm run start:dev
   ```

2. **Rebuild Android app** để áp dụng thay đổi:
   - Sync Gradle
   - Rebuild project

## 🧪 Test

1. Mở app Android
2. Chuyển sang tab "Đăng ký"
3. Nhập số điện thoại và mật khẩu
4. Click "Tiếp tục"
5. Kiểm tra:
   - Nếu thành công: Hiển thị "Đăng ký thành công! Vui lòng đăng nhập."
   - Nếu thất bại: Kiểm tra log trong Logcat với tag "UserManager"

## 📝 Lưu ý

- Nếu vẫn lỗi, kiểm tra:
  - Backend server đang chạy tại `http://localhost:3000`
  - API base URL trong `ApiClient.kt` đúng (emulator: `10.0.2.2`, device: IP máy tính)
  - Network permissions trong AndroidManifest.xml
  - Logcat để xem error chi tiết

