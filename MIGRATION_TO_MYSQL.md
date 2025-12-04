# Migration từ SQLite sang MySQL API

## ✅ Đã hoàn thành

1. ✅ Tạo API Service (Retrofit) để gọi backend NestJS
2. ✅ Cập nhật UserManager để dùng API thay vì SQLite
3. ✅ Cập nhật OrderManager để dùng API thay vì SQLite
4. ✅ Cập nhật VoucherManager để dùng API thay vì SQLite
5. ✅ Cập nhật App.kt để không khởi tạo SQLite
6. ✅ Thêm dependencies (Retrofit, OkHttp) vào build.gradle.kts

## 📋 Cấu trúc mới

### Network Layer
- `ApiService.kt` - Interface định nghĩa các API endpoints
- `ApiClient.kt` - Retrofit client với token management

### Manager Classes (đã cập nhật)
- `UserManager.kt` - Dùng API cho authentication và user management
- `OrderManager.kt` - Dùng API cho order operations
- `VoucherManager.kt` - Dùng API cho voucher operations

### Local Storage
- User data được lưu trong SharedPreferences (không còn SQLite)
- Token được lưu trong SharedPreferences

## 🔧 Cấu hình

### Base URL
Mặc định: `http://10.0.2.2:3000/api/` (Android emulator)

Để dùng trên thiết bị thật:
1. Tìm IP của máy tính: `ipconfig` (Windows) hoặc `ifconfig` (Mac/Linux)
2. Cập nhật trong `ApiClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://192.168.x.x:3000/api/"
   ```

### Backend Requirements
- Backend NestJS phải chạy tại `http://localhost:3000`
- Database MySQL phải có dữ liệu từ `CoffeShop` database
- JWT authentication phải được cấu hình đúng

## 🔄 Thay đổi chính

### UserManager
- `login()` - Gọi API `/auth/login`
- `registerUser()` - Gọi API `/auth/register`
- `getCurrentUser()` - Lấy từ SharedPreferences
- `refreshCurrentUser()` - Gọi API `/auth/profile`
- `updateUser()` - Gọi API `/users/{userId}`

### OrderManager
- `createOrder()` - Gọi API `/orders` (POST)
- `getAllOrders()` - Gọi API `/orders` (GET)
- `getOrderById()` - Gọi API `/orders/{orderId}`
- `updateOrderStatus()` - Gọi API `/orders/{orderId}/status`
- `deleteOrder()` - Gọi API `/orders/{orderId}` (DELETE)

### VoucherManager
- `getAllVouchers()` - Gọi API `/vouchers` (GET)
- `getVoucherByCode()` - Gọi API `/vouchers/code/{code}`
- `getVoucherById()` - Gọi API `/vouchers/{voucherId}`
- `addVoucher()` - Gọi API `/vouchers` (POST)
- `updateVoucher()` - Gọi API `/vouchers/{voucherId}` (PATCH)
- `deleteVoucher()` - Gọi API `/vouchers/{voucherId}` (DELETE)

## ⚠️ Lưu ý

1. **Tất cả các Manager methods đều là suspend functions** - Phải gọi từ coroutine scope
2. **Token management** - Token được tự động thêm vào headers qua OkHttp interceptor
3. **Error handling** - Tất cả API calls đều có try-catch và log errors
4. **SQLite đã bị loại bỏ** - Không còn dùng DatabaseHelper nữa

## 🚀 Sử dụng

### Ví dụ: Login
```kotlin
lifecycleScope.launch {
    val userManager = UserManager(context)
    val user = userManager.login("admin", "admin123")
    if (user != null) {
        // Login thành công
    }
}
```

### Ví dụ: Get Orders
```kotlin
lifecycleScope.launch {
    val orderManager = OrderManager(context)
    val orders = orderManager.getAllOrders()
    // Xử lý orders
}
```

## 📝 TODO

- [ ] Cập nhật AddressManager để dùng API (nếu có endpoint)
- [ ] Thêm endpoint để fetch order_items từ order_id
- [ ] Test tất cả API calls
- [ ] Xử lý offline mode (cache data)

