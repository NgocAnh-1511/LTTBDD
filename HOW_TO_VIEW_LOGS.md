# Cách Xem Log trong Android Studio

## 📱 Xem Logcat (Logs từ App)

### Bước 1: Mở Logcat
1. Mở Android Studio
2. Ở dưới cùng màn hình, click tab **"Logcat"**
3. Nếu không thấy, vào: **View > Tool Windows > Logcat**

### Bước 2: Filter Logs
Trong ô filter (có icon kính lúp), nhập một trong các tag sau:

**Để xem lỗi tạo đơn hàng:**
```
OrderManager
```

**Hoặc xem tất cả logs liên quan:**
```
OrderManager|CheckoutActivity|ApiClient|OkHttp
```

### Bước 3: Xem Logs
Sau khi filter, bạn sẽ thấy:
- ✅ **Logs màu đen**: Thông tin bình thường
- ⚠️ **Logs màu vàng**: Cảnh báo (Warning)
- ❌ **Logs màu đỏ**: Lỗi (Error)

### Bước 4: Tìm Lỗi Tạo Đơn Hàng
Tìm các dòng có:
- `OrderManager` - Logs từ OrderManager
- `Create order error` - Lỗi khi tạo order
- `Create order response code` - Response code từ API
- `Network error` - Lỗi kết nối

## 🔍 Các Tag Quan Trọng

### Để Debug Đăng Ký/Đăng Nhập:
```
UserManager|LoginActivity
```

### Để Debug Tạo Đơn Hàng:
```
OrderManager|CheckoutActivity
```

### Để Debug API Calls:
```
ApiClient|OkHttp
```

### Để Xem Tất Cả:
```
com.example.coffeeshop
```

## 📋 Ví Dụ Logs Bạn Sẽ Thấy

### Khi Tạo Đơn Hàng Thành Công:
```
OrderManager: Creating order with userId: user_123...
OrderManager: Create order response code: 201
OrderManager: Order created successfully: order_456
```

### Khi Tạo Đơn Hàng Thất Bại:
```
OrderManager: Creating order with userId: user_123...
OrderManager: Create order response code: 400
OrderManager: Create order error: Bad Request - {"message":[...]}
```

### Khi Không Kết Nối Được Backend:
```
OrderManager: Create order error
java.net.ConnectException: Failed to connect to /10.0.2.2:3000
```

## 🛠️ Cách Copy Logs

1. Click vào dòng log bạn muốn copy
2. Right-click > **Copy** hoặc **Ctrl+C**
3. Paste vào đây để tôi xem

## 💡 Tips

1. **Clear Logs**: Click icon **🗑️** để xóa logs cũ
2. **Save Logs**: Click icon **💾** để lưu logs ra file
3. **Search**: Dùng **Ctrl+F** để tìm từ khóa trong logs
4. **Filter by Level**: Chọn **Error**, **Warning**, **Info** để chỉ xem loại log đó

## 🚨 Nếu Không Thấy Logs

1. **Kiểm tra device/emulator đã kết nối:**
   - Xem ở trên có hiển thị device không
   - Nếu không, chạy app lại

2. **Kiểm tra filter:**
   - Xóa filter và xem tất cả logs
   - Thử filter: `package:com.example.coffeeshop`

3. **Restart Logcat:**
   - Click icon **🔄** để restart Logcat

## 📸 Cách Chụp Màn Hình Logs

1. Scroll đến dòng log lỗi
2. Chọn các dòng log liên quan (Shift + Click)
3. Copy (Ctrl+C)
4. Paste vào đây

Hoặc:
1. Chụp màn hình Logcat
2. Gửi ảnh cho tôi xem

