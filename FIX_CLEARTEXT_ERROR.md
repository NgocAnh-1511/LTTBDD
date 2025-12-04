# Fix CLEARTEXT Communication Error

## ❌ Lỗi

```
java.net.UnknownServiceException: CLEARTEXT communication to 10.0.2.2 not permitted by network security policy
```

## 🔍 Nguyên nhân

Từ Android 9 (API 28) trở lên, mặc định chỉ cho phép HTTPS. HTTP (cleartext) bị chặn.

## ✅ Đã sửa

### 1. AndroidManifest.xml
- ✅ Thêm `android:networkSecurityConfig="@xml/network_security_config"`
- ✅ Thêm `android:usesCleartextTraffic="true"`

### 2. network_security_config.xml
- ✅ Đã có `cleartextTrafficPermitted="true"` trong base-config
- ✅ Thêm domain-config cho localhost và emulator (10.0.2.2)

## 🔄 Cần làm

1. **Rebuild Android app**:
   - Sync Gradle
   - Clean và Rebuild project
   - Hoặc: Build > Clean Project, sau đó Build > Rebuild Project

2. **Uninstall app cũ** (nếu cần):
   - Gỡ app khỏi emulator/device
   - Cài lại app mới

## 🧪 Test

1. Rebuild và cài lại app
2. Thử đăng ký lại
3. Kiểm tra logcat - không còn lỗi CLEARTEXT

## 📝 Lưu ý

- **Production**: Không nên dùng `usesCleartextTraffic="true"` trong production
- **Development**: OK để dùng cho localhost/emulator
- **Alternative**: Có thể setup HTTPS cho localhost (phức tạp hơn)

## 🔒 Security

File `network_security_config.xml` chỉ cho phép cleartext cho:
- `localhost`
- `127.0.0.1`
- `10.0.2.2` (Android emulator)

Các domain khác vẫn yêu cầu HTTPS.

