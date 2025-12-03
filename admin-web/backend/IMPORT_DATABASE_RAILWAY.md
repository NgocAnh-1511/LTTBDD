# 📥 Hướng Dẫn Import Database Vào Railway MySQL

## 🎯 Mục Tiêu

Import file `database/database.sql` vào Railway MySQL database.

---

## 📋 Các Cách Import

### **Cách 1: Dùng Railway MySQL Terminal (Khuyến Nghị)**

1. **Vào Railway Dashboard:**
   - Click vào service **MySQL**

2. **Mở MySQL Terminal:**
   - Vào tab **Connect** hoặc **Data**
   - Click **Open MySQL Terminal** hoặc **Connect**
   - Railway sẽ mở terminal với MySQL đã kết nối sẵn

3. **Import Database:**
   ```sql
   -- Copy toàn bộ nội dung file database/database.sql
   -- Paste vào terminal và Enter
   ```

   Hoặc nếu terminal hỗ trợ file upload:
   ```bash
   source /path/to/database.sql
   ```

---

### **Cách 2: Dùng MySQL Workbench**

1. **Lấy Connection Info:**
   - Vào Railway → Service **MySQL** → Tab **Variables**
   - Copy các giá trị:
     - `MYSQLHOST` → Host
     - `MYSQLPORT` → Port
     - `MYSQLUSER` → Username
     - `MYSQLPASSWORD` → Password
     - `MYSQLDATABASE` → Default Schema

2. **Tạo Connection:**
   - Mở MySQL Workbench
   - Click **+** để tạo connection mới
   - Điền thông tin:
     - **Connection Name:** Railway MySQL
     - **Hostname:** `MYSQLHOST` value
     - **Port:** `MYSQLPORT` value
     - **Username:** `MYSQLUSER` value
     - **Password:** Click "Store in Keychain" và nhập `MYSQLPASSWORD`
     - **Default Schema:** `MYSQLDATABASE` value

3. **Connect và Import:**
   - Click **Connect**
   - Vào **Server** → **Data Import**
   - Chọn **Import from Self-Contained File**
   - Browse đến file `admin-web/backend/database/database.sql`
   - Chọn **Default Target Schema:** `CoffeShop` (hoặc tên database của bạn)
   - Click **Start Import**

---

### **Cách 3: Dùng DBeaver**

1. **Lấy Connection Info:**
   - Vào Railway → Service **MySQL** → Tab **Variables**
   - Copy các giá trị như trên

2. **Tạo Connection:**
   - Mở DBeaver
   - Click **New Database Connection**
   - Chọn **MySQL**
   - Điền thông tin connection
   - Click **Test Connection** → **Finish**

3. **Import SQL:**
   - Right-click vào database
   - Chọn **SQL Editor** → **New SQL Script**
   - Mở file `admin-web/backend/database/database.sql`
   - Copy toàn bộ nội dung và paste vào SQL Editor
   - Click **Execute SQL Script** (Ctrl+Enter)

---

### **Cách 4: Dùng Command Line (Nếu có MySQL client)**

1. **Lấy Connection Info từ Railway:**
   ```bash
   # Vào Railway → MySQL → Variables
   # Copy các giá trị
   ```

2. **Import từ local:**
   ```bash
   mysql -h $MYSQLHOST \
         -P $MYSQLPORT \
         -u $MYSQLUSER \
         -p$MYSQLPASSWORD \
         $MYSQLDATABASE \
         < admin-web/backend/database/database.sql
   ```

   Hoặc nếu MySQL client trên Windows:
   ```powershell
   mysql -h $env:MYSQLHOST `
         -P $env:MYSQLPORT `
         -u $env:MYSQLUSER `
         -p$env:MYSQLPASSWORD `
         $env:MYSQLDATABASE `
         < admin-web\backend\database\database.sql
   ```

---

## ✅ Kiểm Tra Sau Khi Import

1. **Kiểm tra các bảng đã tạo:**
   ```sql
   USE CoffeShop;
   SHOW TABLES;
   ```

   Phải thấy các bảng:
   - `users`
   - `orders`
   - `order_items`
   - `products`
   - `categories`
   - `vouchers`
   - `banners`

2. **Kiểm tra dữ liệu:**
   ```sql
   SELECT COUNT(*) FROM users;
   SELECT COUNT(*) FROM products;
   SELECT COUNT(*) FROM orders;
   ```

3. **Kiểm tra user admin:**
   ```sql
   SELECT * FROM users WHERE phone_number = 'admin';
   ```

   Phải có user với:
   - `phone_number`: `admin`
   - `password`: (đã hash)
   - `is_admin`: `1`

---

## 🐛 Troubleshooting

### **Lỗi "Access Denied":**
- Kiểm tra username/password trong Railway Variables
- Đảm bảo đang dùng đúng `MYSQLUSER` và `MYSQLPASSWORD`

### **Lỗi "Database not found":**
- Kiểm tra `MYSQLDATABASE` trong Railway Variables
- Database sẽ tự tạo khi import nếu có `CREATE DATABASE` trong SQL

### **Lỗi "Table already exists":**
- File SQL có `DROP TABLE IF EXISTS` nên sẽ tự xóa và tạo lại
- Nếu vẫn lỗi, xóa các bảng thủ công trước khi import

### **Import thành công nhưng không có dữ liệu:**
- Kiểm tra file SQL có INSERT statements không
- File `database.sql` chỉ tạo cấu trúc, không có dữ liệu mẫu
- Cần tạo user admin thủ công hoặc đăng ký qua API

---

## 📝 Tạo User Admin Thủ Công (Nếu Cần)

Sau khi import database, có thể tạo user admin:

```sql
USE CoffeShop;

INSERT INTO users (
  user_id,
  phone_number,
  full_name,
  password,
  created_at,
  is_logged_in,
  is_admin
) VALUES (
  'admin_001',
  'admin',
  'Administrator',
  '$2b$10$YourHashedPasswordHere', -- Dùng bcrypt hash
  UNIX_TIMESTAMP(NOW()),
  0,
  1
);
```

**Hoặc đơn giản hơn:** Đăng ký user mới qua API, sau đó update `is_admin = 1` trong database.

---

## ✅ Checklist

- [ ] Đã lấy connection info từ Railway MySQL Variables
- [ ] Đã kết nối được với MySQL
- [ ] Đã import file `database.sql`
- [ ] Đã kiểm tra các bảng đã tạo
- [ ] Đã kiểm tra có user admin (hoặc đã tạo)
- [ ] Backend có thể kết nối được MySQL

