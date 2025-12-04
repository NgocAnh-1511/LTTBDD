# 🔧 Sửa Lỗi 401 Unauthorized

## ❌ Lỗi

```
Create order failed: 401 - {"message":"Unauthorized","statusCode":401}
```

## 🔍 Nguyên Nhân

**401 Unauthorized** có nghĩa là:
- Token không hợp lệ
- Token hết hạn
- Token không được gửi đúng
- User chưa đăng nhập

---

## ✅ Cách Sửa

### **Bước 1: Đăng Nhập Lại Trong App**

1. **Đăng xuất** (nếu đang đăng nhập)
2. **Đăng nhập lại** với:
   - Phone: `0846230059` (hoặc số điện thoại bạn đã đăng ký)
   - Password: `Nam26122005@` (hoặc password của bạn)

3. **Kiểm tra token đã được lưu:**
   - Xem logs trong Logcat với filter `UserManager`
   - Phải thấy: `Token saved` hoặc tương tự

### **Bước 2: Kiểm Tra Token Trong Logs**

Sau khi đăng nhập, khi tạo order, xem logs:

```
OrderManager: Token exists: xxx...
OrderManager: Full token length: XXX
OrderManager: Token preview: xxx...
```

**Nếu token length < 50:** Token có thể không đúng format

### **Bước 3: Kiểm Tra JWT_SECRET Trên Backend**

1. Vào **Railway** → Service **LTTBDD** → Variables
2. Kiểm tra `JWT_SECRET` có giá trị không
3. Đảm bảo `JWT_SECRET` là một chuỗi mạnh (ít nhất 32 ký tự)

### **Bước 4: Test Đăng Nhập Qua API**

Test trực tiếp với curl:

```bash
# Test login
curl -X POST https://lttbdd-production.up.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "0846230059",
    "password": "Nam26122005@"
  }'
```

Phải trả về:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {...}
}
```

### **Bước 5: Test Create Order Với Token**

Sau khi có token từ bước 4:

```bash
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." # Token từ login

curl -X POST https://lttbdd-production.up.railway.app/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "1764015318340",
    "totalPrice": 9.0,
    "items": [
      {
        "productName": "Test Product",
        "quantity": 1,
        "price": 9.0
      }
    ],
    "deliveryAddress": "Test Address",
    "phoneNumber": "0846230059",
    "customerName": "Test User",
    "paymentMethod": "Tiền mặt"
  }'
```

---

## 🐛 Các Trường Hợp Cụ Thể

### **Trường Hợp 1: Token null**

**Logs:**
```
OrderManager: Token is null - user not authenticated
```

**Giải pháp:**
- Đăng nhập lại trong app
- Kiểm tra login có thành công không

### **Trường Hợp 2: Token không hợp lệ**

**Logs:**
```
OrderManager: Token exists: xxx...
OrderManager: Create order failed: 401
```

**Giải pháp:**
- Token có thể hết hạn (JWT_EXPIRES_IN)
- Đăng nhập lại để lấy token mới
- Kiểm tra JWT_SECRET trên backend có đúng không

### **Trường Hợp 3: Token không được gửi**

**Kiểm tra trong OkHttp logs:**
- Tìm request header `Authorization`
- Phải có: `Authorization: Bearer xxx...`

**Nếu không có:** Có thể do interceptor không hoạt động

---

## ✅ Checklist

- [ ] Đã đăng nhập lại trong app
- [ ] Đã kiểm tra token có được lưu không (logs)
- [ ] Đã kiểm tra JWT_SECRET trên Railway
- [ ] Đã test login qua API (curl)
- [ ] Đã test create order với token (curl)
- [ ] Đã thử tạo order lại trong app

---

## 🎯 Sau Khi Sửa

Sau khi đăng nhập lại và token hợp lệ:
- Tạo order sẽ thành công
- Response code sẽ là 200 hoặc 201
- Order sẽ được tạo trong database
- Order sẽ hiển thị trong Admin Panel

