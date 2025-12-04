# 📱 Build APK Với Railway URL - Hướng Dẫn Chi Tiết

## ❌ Vấn Đề

**Khi người khác tải APK và đăng ký/đặt hàng, dữ liệu không cập nhật lên admin vì:**
- APK đang dùng URL cũ (localhost hoặc IP local)
- APK được build trước khi cập nhật Railway URL
- SharedPreferences có thể đã lưu URL cũ

---

## ✅ Giải Pháp: Build APK Mới Với Railway URL

### **Bước 1: Kiểm Tra Code Đã Có Railway URL**

File: `LTTBDD-main/app/src/main/java/com/example/coffeeshop/Network/ApiClient.kt`

Phải có:
```kotlin
private const val DEFAULT_BASE_URL = "https://lttbdd-production.up.railway.app/api/"
```

**Nếu chưa có, sửa ngay!**

---

### **Bước 2: Clean Project**

Trong Android Studio:

1. **Build** → **Clean Project**
2. Đợi clean xong
3. **Build** → **Rebuild Project**

Hoặc command line:
```bash
cd LTTBDD-main
./gradlew clean
```

---

### **Bước 3: Build Release APK**

#### **Cách 1: Trong Android Studio (Khuyến nghị)**

1. **Build** → **Generate Signed Bundle / APK**
2. Chọn **APK** (không phải Android App Bundle)
3. **Nếu chưa có keystore:**
   - Click **Create new...**
   - Điền thông tin:
     - Key store path: Chọn nơi lưu
     - Password: Nhập password mạnh
     - Key alias: `coffeeshop`
     - Key password: Nhập password
     - Validity: 25 years
     - Certificate: Điền thông tin
   - Click **OK**
4. **Nếu đã có keystore:**
   - Chọn keystore file
   - Nhập passwords
5. Chọn **release** build variant
6. Click **Finish**
7. APK sẽ ở: `LTTBDD-main/app/build/outputs/apk/release/app-release.apk`

#### **Cách 2: Command Line**

```bash
cd LTTBDD-main

# Build release APK
./gradlew assembleRelease

# APK sẽ ở: app/build/outputs/apk/release/app-release.apk
```

**Lưu ý:** Nếu chưa có keystore, cần tạo trước:
```bash
keytool -genkey -v -keystore coffeeshop.keystore -alias coffeeshop -keyalg RSA -keysize 2048 -validity 10000
```

---

### **Bước 4: Kiểm Tra APK Có Railway URL**

**Cách 1: Decompile APK (phức tạp)**
- Dùng jadx hoặc apktool để xem code

**Cách 2: Test APK**
- Cài APK mới trên điện thoại
- Đăng ký/đăng nhập
- Xem logs trong Logcat:
  ```
  ApiClient: API Base URL: https://lttbdd-production.up.railway.app/api/
  ```
- Nếu thấy URL này → APK đúng
- Nếu thấy URL khác → APK cũ

---

### **Bước 5: Xóa Cache Nếu Cần**

Nếu user đã cài APK cũ, SharedPreferences có thể đã lưu URL cũ.

**Giải pháp:**
1. **Xóa app** trên điện thoại
2. **Cài APK mới**
3. Hoặc thêm code để reset URL trong lần đầu mở app

---

## 🔧 Thêm Code Reset URL (Tùy chọn)

Nếu muốn đảm bảo APK luôn dùng Railway URL, thêm code reset:

```kotlin
// Trong ApiClient.kt, thêm hàm:
fun resetBaseUrl(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences("CoffeeShopPrefs", Context.MODE_PRIVATE)
    prefs.edit().remove(PREF_BASE_URL).apply()
    retrofit = null
    apiService = null
    android.util.Log.d("ApiClient", "Base URL reset to default")
}

// Gọi trong App.kt hoặc MainActivity khi app khởi động:
ApiClient.resetBaseUrl(this)
```

---

## ✅ Checklist Build APK

- [ ] Code đã có Railway URL: `https://lttbdd-production.up.railway.app/api/`
- [ ] Đã clean project
- [ ] Đã build release APK
- [ ] Đã test APK trên điện thoại
- [ ] Đã kiểm tra logs thấy Railway URL
- [ ] Đã test đăng ký/đặt hàng
- [ ] Đã kiểm tra dữ liệu hiển thị trong Admin Panel

---

## 🎯 Sau Khi Build APK Mới

1. **Phân phối APK mới:**
   - Upload lên Google Drive
   - Hoặc chia sẻ qua link
   - Hoặc cài trực tiếp

2. **Hướng dẫn người dùng:**
   - Xóa app cũ (nếu có)
   - Cài APK mới
   - Đăng ký/đăng nhập
   - Đặt hàng

3. **Kiểm tra:**
   - Dữ liệu phải hiển thị trong Admin Panel
   - Orders phải xuất hiện trong Railway database

---

## 🐛 Troubleshooting

### **APK vẫn không kết nối được:**

1. **Kiểm tra logs:**
   - Xem Logcat với filter `ApiClient`
   - Phải thấy Railway URL

2. **Kiểm tra internet:**
   - Đảm bảo điện thoại có internet
   - Test mở browser: `https://lttbdd-production.up.railway.app/api`

3. **Kiểm tra backend:**
   - Vào Railway → Service LTTBDD
   - Xem logs có request đến không

### **Dữ liệu không hiển thị trong Admin:**

1. **Kiểm tra database:**
   - Vào Railway → MySQL → Data
   - Xem có dữ liệu mới không

2. **Kiểm tra Admin Panel:**
   - Refresh trang
   - Đảm bảo Admin Panel đang kết nối Railway URL

---

## 📝 Lưu Ý Quan Trọng

1. **Mỗi lần build APK mới:**
   - Phải đảm bảo code có Railway URL
   - Phải clean project trước khi build
   - Phải test APK trước khi phân phối

2. **Version Code:**
   - Nên tăng version code trong `build.gradle.kts`:
     ```kotlin
     versionCode = 2  // Tăng từ 1 lên 2
     versionName = "1.1"
     ```

3. **Keystore:**
   - **Lưu keystore cẩn thận!**
   - Nếu mất keystore, không thể update APK lên Google Play
   - Backup keystore ở nơi an toàn

