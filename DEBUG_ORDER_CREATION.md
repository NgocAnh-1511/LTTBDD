# 🐛 Debug: Order Creation Returned Null

## ❌ Lỗi

```
Order creation returned null
```

## 🔍 Các Nguyên Nhân Có Thể

### **1. Token không hợp lệ hoặc hết hạn**
- User chưa đăng nhập
- Token đã hết hạn
- Token không được lưu đúng

### **2. Request format không đúng**
- Request body thiếu field bắt buộc
- Data type không đúng

### **3. Backend trả về lỗi**
- 401 Unauthorized (token không hợp lệ)
- 400 Bad Request (validation error)
- 500 Internal Server Error

### **4. Network error**
- Không kết nối được đến backend
- Timeout

---

## 🔧 Cách Debug

### **Bước 1: Kiểm Tra Logs Trong Android Studio**

Mở **Logcat** và filter theo tag `OrderManager`:

```
OrderManager: === Creating Order ===
OrderManager: Items count: 1
OrderManager: Total price: 9.0
OrderManager: User ID: xxx
OrderManager: Token exists: xxx...
OrderManager: Sending create order request to API...
OrderManager: Request body: {...}
OrderManager: Create order response code: XXX
OrderManager: Create order failed: XXX - error message
```

**Quan trọng:** Xem:
- Response code là gì? (401, 400, 500, etc.)
- Error message là gì?

### **Bước 2: Kiểm Tra HttpLoggingInterceptor**

ApiClient đã enable `HttpLoggingInterceptor.Level.BODY`, nên sẽ log:
- Request URL
- Request headers (bao gồm Authorization)
- Request body
- Response code
- Response body

Tìm trong Logcat với filter `OkHttp` hoặc `retrofit2`.

### **Bước 3: Kiểm Tra Backend Logs Trên Railway**

1. Vào **Railway** → Service **LTTBDD** → Tab **Deployments**
2. Xem logs của deployment mới nhất
3. Tìm:
   - Request đến `/api/orders` POST
   - Lỗi validation
   - Lỗi database
   - Lỗi authentication

### **Bước 4: Test API Trực Tiếp**

Dùng Postman hoặc curl để test:

```bash
# Lấy token từ app (xem trong SharedPreferences hoặc log)
TOKEN="your-token-here"

# Test create order
curl -X POST https://lttbdd-production.up.railway.app/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user_123",
    "totalPrice": 9.0,
    "items": [
      {
        "productName": "Test Product",
        "quantity": 1,
        "price": 9.0
      }
    ],
    "deliveryAddress": "Test Address",
    "phoneNumber": "0123456789",
    "customerName": "Test User",
    "paymentMethod": "Tiền mặt"
  }'
```

---

## ✅ Các Bước Sửa Lỗi

### **Nếu Response Code = 401 (Unauthorized):**

**Nguyên nhân:** Token không hợp lệ hoặc hết hạn

**Giải pháp:**
1. Đăng xuất và đăng nhập lại trong app
2. Kiểm tra token có được lưu đúng không:
   ```kotlin
   val token = ApiClient.getToken(context)
   Log.d("Debug", "Token: $token")
   ```

### **Nếu Response Code = 400 (Bad Request):**

**Nguyên nhân:** Request body không đúng format

**Giải pháp:**
1. Kiểm tra error message từ backend
2. Đảm bảo các field bắt buộc có đầy đủ:
   - `userId` (String, required)
   - `totalPrice` (Number, required)
   - `items` (Array, required, không được rỗng)
   - Mỗi item phải có: `productName`, `quantity`, `price`

### **Nếu Response Code = 500 (Internal Server Error):**

**Nguyên nhân:** Lỗi server (database, validation, etc.)

**Giải pháp:**
1. Xem logs backend trên Railway
2. Kiểm tra database connection
3. Kiểm tra các bảng có tồn tại không

### **Nếu Không Có Response (Network Error):**

**Nguyên nhân:** Không kết nối được đến backend

**Giải pháp:**
1. Kiểm tra BASE_URL trong ApiClient.kt:
   ```kotlin
   private const val DEFAULT_BASE_URL = "https://lttbdd-production.up.railway.app/api/"
   ```
2. Test backend có hoạt động không:
   ```bash
   curl https://lttbdd-production.up.railway.app/api
   ```
3. Kiểm tra internet connection

---

## 📝 Checklist Debug

- [ ] Đã xem logs trong Android Studio Logcat (filter: OrderManager)
- [ ] Đã xem HttpLoggingInterceptor logs (filter: OkHttp)
- [ ] Đã xem backend logs trên Railway
- [ ] Đã test API trực tiếp với Postman/curl
- [ ] Đã kiểm tra token có hợp lệ không
- [ ] Đã kiểm tra user có đăng nhập không
- [ ] Đã kiểm tra BASE_URL có đúng không

---

## 🎯 Sau Khi Thêm Logging

Sau khi build lại app với logging mới, bạn sẽ thấy:

```
OrderManager: === Creating Order ===
OrderManager: Items count: 1
OrderManager: Total price: 9.0
OrderManager: User ID: xxx
OrderManager: Token exists: xxx...
OrderManager: Sending create order request to API...
OrderManager: Request body: {"userId":"...","totalPrice":9.0,"items":[...]}
OrderManager: Authorization header: Bearer xxx...
OrderManager: Create order response code: 401
OrderManager: Create order failed: 401 - {"message":"Unauthorized"}
```

Từ đó sẽ biết chính xác lỗi là gì!

