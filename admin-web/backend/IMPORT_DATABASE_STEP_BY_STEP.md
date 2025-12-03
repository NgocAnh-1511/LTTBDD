# 📥 Hướng Dẫn Import Database - Step by Step

## ⚠️ Lỗi Thường Gặp

**Lỗi:** `Access denied for user 'root'@'localhost'`

**Nguyên nhân:** Đang kết nối đến MySQL local thay vì Railway MySQL

---

## ✅ Cách Sửa - Step by Step

### **Bước 1: Lấy Thông Tin Từ Railway**

1. Vào **Railway Dashboard**
2. Click vào service **MySQL**
3. Click tab **Variables** (quan trọng!)
4. Tìm và copy các giá trị sau:

```
MYSQLHOST = containers-us-west-xxx.railway.app  ← Copy cái này
MYSQLPORT = 3306                                ← Copy cái này
MYSQLUSER = root                                 ← Copy cái này
MYSQLPASSWORD = xxxxxx                          ← Copy cái này (click để hiện)
MYSQLDATABASE = railway                          ← Copy cái này
```

**⚠️ Lưu ý:** 
- **KHÔNG** dùng `localhost` hoặc `127.0.0.1`
- Phải dùng **MYSQLHOST** từ Railway (dạng `containers-us-west-xxx.railway.app`)

---

### **Bước 2: Tạo Connection Mới Trong MySQL Workbench**

1. **Mở MySQL Workbench**

2. **Tạo Connection Mới:**
   - Click dấu **+** bên cạnh "MySQL Connections"
   - Hoặc: **Database** → **Manage Connections** → **New**

3. **Điền Thông Tin:**
   ```
   Connection Name: Railway MySQL
   
   Hostname: [Paste MYSQLHOST từ Railway]
            Ví dụ: containers-us-west-xxx.railway.app
            ⚠️ KHÔNG dùng localhost!
   
   Port: [Paste MYSQLPORT từ Railway]
         Thường là: 3306
   
   Username: [Paste MYSQLUSER từ Railway]
            Thường là: root
   
   Password: [Click "Store in Keychain" và paste MYSQLPASSWORD]
   
   Default Schema: [Paste MYSQLDATABASE từ Railway]
                  Thường là: railway
   ```

4. **Test Connection:**
   - Click **"Test Connection"**
   - Nếu thành công → Click **"OK"**
   - Nếu lỗi → Kiểm tra lại thông tin

---

### **Bước 3: Import Database**

1. **Connect:**
   - Double-click vào connection **"Railway MySQL"** vừa tạo
   - Đợi kết nối thành công

2. **Import SQL:**
   - Vào menu: **Server** → **Data Import**
   - Chọn: **"Import from Self-Contained File"**
   - Click **"..."** và browse đến:
     ```
     E:\namngu\admin-web\backend\database\database.sql
     ```
   - Chọn **"Default Target Schema"**: 
     - Dropdown → Chọn database từ `MYSQLDATABASE` (thường là `railway`)
     - Hoặc tạo mới: `CoffeShop`
   
3. **Start Import:**
   - Click **"Start Import"** ở góc dưới bên phải
   - Đợi import xong (sẽ hiện "Import completed successfully")

---

### **Bước 4: Kiểm Tra**

1. **Kiểm tra các bảng đã tạo:**
   - Trong MySQL Workbench, click vào database (bên trái)
   - Expand "Tables"
   - Phải thấy các bảng:
     - `users`
     - `orders`
     - `order_items`
     - `vouchers`
     - `addresses`

2. **Kiểm tra dữ liệu:**
   ```sql
   USE railway;  -- hoặc tên database của bạn
   SELECT COUNT(*) FROM users;
   SELECT COUNT(*) FROM orders;
   ```

---

## 🐛 Troubleshooting

### **Lỗi: "Access denied"**

**Nguyên nhân:** Đang dùng thông tin localhost

**Giải pháp:**
- Đảm bảo Hostname là **MYSQLHOST** từ Railway (không phải localhost)
- Đảm bảo Username và Password đúng từ Railway Variables

### **Lỗi: "Can't connect to MySQL server"**

**Nguyên nhân:** 
- Hostname sai
- Port sai
- Firewall chặn

**Giải pháp:**
- Kiểm tra lại MYSQLHOST và MYSQLPORT từ Railway
- Đảm bảo Railway MySQL service đang chạy

### **Lỗi: "Unknown database"**

**Nguyên nhân:** Database name sai

**Giải pháp:**
- Kiểm tra MYSQLDATABASE trong Railway Variables
- Hoặc tạo database mới trong MySQL Workbench:
  ```sql
  CREATE DATABASE CoffeShop;
  USE CoffeShop;
  ```
  Sau đó import lại

### **File SQL không import được**

**Nguyên nhân:** 
- File path sai
- File encoding sai

**Giải pháp:**
- Kiểm tra đường dẫn file: `E:\namngu\admin-web\backend\database\database.sql`
- Đảm bảo file tồn tại
- Thử copy nội dung file và paste vào SQL Editor thay vì import file

---

## ✅ Checklist

- [ ] Đã lấy thông tin từ Railway → MySQL → Variables
- [ ] Đã tạo connection mới với Railway MySQL (không dùng localhost)
- [ ] Đã test connection thành công
- [ ] Đã import file database.sql
- [ ] Đã kiểm tra các bảng đã tạo
- [ ] Đã kiểm tra có dữ liệu trong bảng

---

## 📝 Lưu Ý Quan Trọng

1. **KHÔNG dùng localhost:** Railway MySQL không chạy trên localhost
2. **Dùng đúng Hostname:** Phải là MYSQLHOST từ Railway (dạng domain)
3. **Kiểm tra Variables:** Luôn lấy thông tin từ Railway → MySQL → Variables
4. **Database name:** Có thể là `railway` hoặc tên khác, kiểm tra MYSQLDATABASE

