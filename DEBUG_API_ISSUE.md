# 🔍 Debug: App Không Nhận Dữ Liệu Products và Categories

## ✅ Đã Thêm Logging Chi Tiết

Đã thêm logging chi tiết vào `MainRepository.kt` để debug vấn đề.

## 🔍 Các Bước Debug

### 1. Kiểm Tra Logs

Chạy app và xem logs:

```bash
adb logcat | grep -E "MainRepository|ApiClient"
```

Bạn sẽ thấy:
- `MainRepository: ✅ MainRepository initialized successfully` - Repository đã khởi tạo
- `MainRepository: Calling API: getCategories()` - Đang gọi API
- `MainRepository: Response code: XXX` - Mã response
- `MainRepository: Response body: ...` - Nội dung response

### 2. Kiểm Tra API Endpoints

Test trực tiếp API:

```bash
# Test Categories
curl https://lttbdd-production.up.railway.app/api/public/categories

# Test Products
curl https://lttbdd-production.up.railway.app/api/public/products
```

### 3. Các Vấn Đề Có Thể Xảy Ra

#### A. Backend Chưa Deploy
- ✅ Kiểm tra: Backend có đang chạy không?
- ✅ Kiểm tra: URL có đúng không? `https://lttbdd-production.up.railway.app/api/`

#### B. Response Format Không Đúng
- ✅ Backend trả về format khác với `ProductResponse` và `CategoryResponse`
- ✅ Kiểm tra: Response có đúng structure không?

#### C. Network Error
- ✅ App không kết nối được với backend
- ✅ Kiểm tra: Internet connection
- ✅ Kiểm tra: SSL certificate

#### D. App.getInstance() Throw Exception
- ✅ App chưa được khởi tạo
- ✅ Kiểm tra: `AndroidManifest.xml` có `android:name=".App"` không?

## 🛠️ Cách Sửa

### Nếu Backend Chưa Deploy

```bash
cd admin-web/backend
git add .
git commit -m "Add public API endpoints"
git push origin backend-deploy
```

### Nếu Response Format Không Đúng

Kiểm tra response thực tế và sửa `ProductResponse` và `CategoryResponse` cho đúng.

### Nếu App.getInstance() Throw Exception

Đảm bảo `AndroidManifest.xml` có:
```xml
<application
    android:name=".App"
    ...>
```

## 📋 Checklist Debug

- [ ] Backend đang chạy
- [ ] API endpoints hoạt động (test bằng curl)
- [ ] App có internet connection
- [ ] Logs hiển thị đúng
- [ ] Response format đúng
- [ ] App.getInstance() không throw exception

## 🎯 Kết Quả Mong Đợi

Sau khi fix, logs sẽ hiển thị:
```
MainRepository: ✅ MainRepository initialized successfully
MainRepository: Calling API: getCategories()
MainRepository: Response code: 200, isSuccessful: true
MainRepository: Response body: [...]
MainRepository: ✅ Loaded X categories from API
```

