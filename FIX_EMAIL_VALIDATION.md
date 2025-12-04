# Fix Email Validation Error

## ❌ Lỗi

```
Register failed: 400 - {"message":["email must be an email"],"error":"Bad Request","statusCode":400}
```

## 🔍 Nguyên nhân

- Backend có `@IsEmail()` và `@IsOptional()` cho email
- Khi Android gửi `email = ""` (chuỗi rỗng), class-validator vẫn validate và báo lỗi vì `""` không phải là email hợp lệ
- `@IsOptional()` chỉ bỏ qua validation nếu field là `undefined` hoặc `null`, không bỏ qua chuỗi rỗng `""`

## ✅ Đã sửa

### 1. Backend (`register.dto.ts`)
- ✅ Thêm `@ValidateIf()` để chỉ validate email khi có giá trị (không phải null, undefined, hoặc "")
- ✅ Import `ValidateIf` từ `class-validator`

### 2. Android (`UserManager.kt`)
- ✅ Nếu email rỗng, gửi `null` thay vì `""`
- ✅ Kiểm tra `email.isBlank()` trước khi gửi

### 3. Android (`ApiService.kt`)
- ✅ Đổi `RegisterRequest.email` từ `String = ""` thành `String? = null`
- ✅ Đổi `RegisterRequest.fullName` từ `String = ""` thành `String? = null`

## 🔄 Cần làm

1. **Restart backend server**:
   ```powershell
   cd E:\namngu\admin-web\backend
   npm run start:dev
   ```

2. **Rebuild Android app**:
   - Sync Gradle
   - Rebuild project

## 🧪 Test

1. Mở app Android
2. Chuyển sang tab "Đăng ký"
3. Nhập số điện thoại và mật khẩu (không cần email)
4. Click "Tiếp tục"
5. Kiểm tra: Đăng ký thành công

## 📝 Lưu ý

- Email giờ là optional và có thể để trống
- Nếu có email, phải là định dạng email hợp lệ
- Nếu không có email, gửi `null` thay vì `""`

