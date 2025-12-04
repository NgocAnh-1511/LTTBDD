# 🚀 Build APK Ngay - Hướng Dẫn Nhanh

## ✅ Code Đã Sẵn Sàng

- ✅ Railway URL đã được cập nhật: `https://lttbdd-production.up.railway.app/api/`
- ✅ Version code đã tăng: `2` (version `1.1`)

---

## 📱 Build APK Release

### **Cách 1: Android Studio (Dễ nhất)**

1. Mở Android Studio
2. **Build** → **Generate Signed Bundle / APK**
3. Chọn **APK**
4. **Nếu chưa có keystore:**
   - Click **Create new...**
   - Điền thông tin và tạo keystore
5. **Nếu đã có keystore:**
   - Chọn file keystore
   - Nhập passwords
6. Chọn **release** build variant
7. Click **Finish**
8. APK sẽ ở: `LTTBDD-main/app/build/outputs/apk/release/app-release.apk`

### **Cách 2: Command Line**

```bash
cd LTTBDD-main
./gradlew clean
./gradlew assembleRelease
```

APK sẽ ở: `app/build/outputs/apk/release/app-release.apk`

---

## ✅ Kiểm Tra APK

1. **Cài APK trên điện thoại**
2. **Mở app và xem logs:**
   - Dùng Logcat với filter `ApiClient`
   - Phải thấy: `API Base URL: https://lttbdd-production.up.railway.app/api/`
3. **Test đăng ký:**
   - Đăng ký user mới
   - Kiểm tra user hiển thị trong Admin Panel
4. **Test đặt hàng:**
   - Đặt hàng
   - Kiểm tra order hiển thị trong Admin Panel

---

## 📤 Phân Phối APK

1. **Upload lên Google Drive:**
   - Upload file `app-release.apk`
   - Chia sẻ link

2. **Hoặc cài trực tiếp:**
   - Copy APK vào điện thoại
   - Cài đặt

3. **Hướng dẫn người dùng:**
   - Xóa app cũ (nếu có)
   - Cài APK mới
   - Đăng ký/đăng nhập
   - Đặt hàng

---

## ✅ Checklist

- [x] Code đã có Railway URL
- [x] Version code đã tăng lên 2
- [ ] Đã build release APK
- [ ] Đã test APK trên điện thoại
- [ ] Đã kiểm tra logs thấy Railway URL
- [ ] Đã test đăng ký/đặt hàng
- [ ] Đã kiểm tra dữ liệu hiển thị trong Admin Panel

