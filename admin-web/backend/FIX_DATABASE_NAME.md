# 🔧 Sửa Lỗi: Table 'railway.users' doesn't exist

## ❌ Lỗi

```
Table 'railway.users' doesn't exist
QueryFailedError: Table 'railway.users' doesn't exist
```

## 🔍 Nguyên Nhân

Backend đang kết nối đến database `railway`, nhưng các bảng lại nằm trong database `CoffeShop` (vì file SQL tạo database `CoffeShop`).

## ✅ Cách Sửa

### **Bước 1: Vào Railway Variables**

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD** (backend)
3. Click tab **Variables**

### **Bước 2: Tìm hoặc Tạo Biến DB_NAME**

Tìm biến `DB_NAME` trong danh sách:

- **Nếu có biến `DB_NAME`:**
  - Click vào biến đó
  - Sửa giá trị từ `railway` hoặc `${{MySQL.MYSQLDATABASE}}` thành: `CoffeShop`
  - Click **Save**

- **Nếu chưa có biến `DB_NAME`:**
  - Click **"+ New Variable"**
  - Key: `DB_NAME`
  - Value: `CoffeShop`
  - Click **Add**

### **Bước 3: Kiểm Tra Các Biến Khác**

Đảm bảo có đầy đủ các biến sau:

```
DB_HOST=${{MySQL.MYSQLHOST}}
DB_PORT=${{MySQL.MYSQLPORT}}
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
DB_NAME=CoffeShop  ← Phải là CoffeShop (không dùng interpolation)
PORT=3000
NODE_ENV=production
JWT_SECRET=your-very-strong-secret-key-min-32-characters
JWT_EXPIRES_IN=7d
```

**⚠️ Lưu ý quan trọng:**
- `DB_NAME` phải là giá trị cố định `CoffeShop` (không dùng `${{MySQL.MYSQLDATABASE}}`)
- Các biến khác có thể dùng interpolation `${{MySQL.xxx}}`

### **Bước 4: Đợi Railway Redeploy**

Sau khi cập nhật biến môi trường:
- Railway sẽ tự động redeploy backend
- Đợi 1-2 phút để deployment hoàn tất
- Xem logs trong tab **Deployments**

### **Bước 5: Kiểm Tra Logs**

1. Vào Railway → Service LTTBDD → Tab **Deployments**
2. Xem logs của deployment mới nhất
3. Phải không còn lỗi "Table 'railway.users' doesn't exist"
4. Phải thấy backend start thành công

### **Bước 6: Test API**

Mở browser và truy cập:
- `https://lttbdd-production.up.railway.app/api`
- Phải thấy response hoặc 401 (nếu chưa login)

---

## 🐛 Nếu Vẫn Lỗi

### **Kiểm tra database có đúng tên không:**

1. Mở MySQL Workbench
2. Connect đến Railway MySQL
3. Kiểm tra có database `CoffeShop` không:
   ```sql
   SHOW DATABASES;
   ```
4. Kiểm tra các bảng trong database `CoffeShop`:
   ```sql
   USE CoffeShop;
   SHOW TABLES;
   ```
   Phải thấy: `users`, `orders`, `order_items`, `vouchers`, `addresses`

### **Kiểm tra biến môi trường:**

1. Vào Railway → Service LTTBDD → Variables
2. Kiểm tra `DB_NAME` có đúng là `CoffeShop` không
3. Kiểm tra các biến `DB_HOST`, `DB_USERNAME`, `DB_PASSWORD` có đúng không

---

## ✅ Sau Khi Sửa

- Backend sẽ kết nối đến database `CoffeShop`
- Các bảng `users`, `orders`, etc. sẽ được tìm thấy
- Admin Panel sẽ có dữ liệu

