# 🔌 Thông Tin Kết Nối Railway MySQL

## 📋 Thông Tin Kết Nối

Dựa trên Railway Variables, đây là thông tin để kết nối:

### **Cho MySQL Workbench (Kết nối từ bên ngoài):**

```
Connection Name: Railway MySQL

Hostname: metro.proxy.rlwy.net
Port: 58353
Username: root
Password: nMaLpLUmLenRUzhCGPB1GkHUmKfgDUyv
Default Schema: railway
```

**⚠️ Lưu ý quan trọng:**
- **KHÔNG dùng** `mysql.railway.internal` (đây là internal host, chỉ dùng trong Railway network)
- **Phải dùng** `metro.proxy.rlwy.net` với port `58353` (từ MYSQL_PUBLIC_URL)

---

### **Cho Backend Service (Trong Railway):**

Backend service trong Railway nên dùng biến môi trường:

```
DB_HOST=${{MySQL.MYSQLHOST}}
DB_PORT=${{MySQL.MYSQLPORT}}
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
DB_NAME=${{MySQL.MYSQLDATABASE}}
```

Hoặc nếu không dùng interpolation:

```
DB_HOST=mysql.railway.internal
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=nMaLpLUmLenRUzhCGPB1GkHUmKfgDUyv
DB_NAME=railway
```

---

## 🔧 Cách Kết Nối MySQL Workbench

### **Bước 1: Tạo Connection**

1. Mở **MySQL Workbench**
2. Click **+** để tạo connection mới
3. Điền thông tin:

```
Connection Name: Railway MySQL

Hostname: metro.proxy.rlwy.net
Port: 58353
Username: root
Password: nMaLpLUmLenRUzhCGPB1GkHUmKfgDUyv
Default Schema: railway
```

4. Click **"Test Connection"**
5. Nếu thành công → Click **"OK"**

### **Bước 2: Import Database**

1. Double-click vào connection **"Railway MySQL"**
2. Vào **Server** → **Data Import**
3. Chọn **"Import from Self-Contained File"**
4. Browse đến: `E:\namngu\admin-web\backend\database\database.sql`
5. Chọn **Default Target Schema**: `railway`
6. Click **"Start Import"**

---

## 📝 Lưu Ý

1. **Public URL vs Internal URL:**
   - `metro.proxy.rlwy.net:58353` → Dùng cho MySQL Workbench (kết nối từ bên ngoài)
   - `mysql.railway.internal:3306` → Chỉ dùng trong Railway network (cho backend service)

2. **Database Name:**
   - Database hiện tại: `railway`
   - File SQL sẽ tạo database `CoffeShop` nếu chưa có
   - Có thể import vào database `railway` hoặc tạo database mới `CoffeShop`

3. **Security:**
   - Password đã được hiển thị, đảm bảo không chia sẻ công khai
   - Railway tự động tạo password mạnh

---

## ✅ Checklist

- [ ] Đã tạo connection với Hostname: `metro.proxy.rlwy.net`
- [ ] Đã dùng Port: `58353` (không phải 3306)
- [ ] Đã test connection thành công
- [ ] Đã import database.sql
- [ ] Đã kiểm tra các bảng đã tạo

