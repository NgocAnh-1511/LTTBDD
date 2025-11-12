# 🔧 HƯỚNG DẪN SỬA LỖI KẾT NỐI MYSQL

## ⚠️ VẤN ĐỀ: `localhost` KHÔNG HOẠT ĐỘNG TRÊN ANDROID!

Android không thể dùng `localhost` hoặc `127.0.0.1` để kết nối đến MySQL trên máy tính.

## ✅ GIẢI PHÁP:

### BƯỚC 1: Xác định bạn đang dùng gì?

#### A. Nếu dùng Android Emulator (AVD):
```kotlin
private const val DB_URL = "jdbc:mysql://10.0.2.2:3306/CoffeeShopDB?..."
```
`10.0.2.2` là IP đặc biệt của emulator để kết nối đến localhost của máy host.

#### B. Nếu dùng thiết bị thật (điện thoại/tablet):

1. **Tìm IP máy tính chạy MySQL:**
   - Windows: Mở CMD → gõ `ipconfig` → Tìm "IPv4 Address" (ví dụ: `192.168.1.100`)
   - Linux/Mac: Mở Terminal → gõ `ifconfig` hoặc `ip addr` → Tìm IP trong mạng local

2. **Cập nhật DatabaseHelper.kt:**
   ```kotlin
   private const val DB_URL = "jdbc:mysql://192.168.1.100:3306/CoffeeShopDB?..."
   ```
   (Thay `192.168.1.100` bằng IP thực của bạn)

3. **Đảm bảo thiết bị Android và máy tính cùng mạng WiFi!**

### BƯỚC 2: Cấu hình MySQL cho phép kết nối từ xa

#### 1. Sửa file cấu hình MySQL:

**Windows:** `C:\ProgramData\MySQL\MySQL Server 8.0\my.ini`
**Linux:** `/etc/mysql/my.cnf` hoặc `/etc/my.cnf`

Tìm và sửa:
```ini
bind-address = 0.0.0.0
```
Hoặc comment dòng này:
```ini
# bind-address = 127.0.0.1
```

**Sau đó restart MySQL:**
- Windows: Services → MySQL80 → Restart
- Linux: `sudo systemctl restart mysql`

#### 2. Tạo user với quyền từ xa:

Mở MySQL Command Line hoặc MySQL Workbench:

```sql
-- Kết nối MySQL
mysql -u root -p

-- Tạo user với quyền từ xa
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '15112005!Nah';
GRANT ALL PRIVILEGES ON CoffeeShopDB.* TO 'root'@'%';
FLUSH PRIVILEGES;

-- Kiểm tra
SELECT user, host FROM mysql.user WHERE user='root';
```

### BƯỚC 3: Mở port 3306 trong Firewall

#### Windows:
```cmd
netsh advfirewall firewall add rule name="MySQL" dir=in action=allow protocol=TCP localport=3306
```

Hoặc thủ công:
1. Windows Defender Firewall → Advanced Settings
2. Inbound Rules → New Rule
3. Port → TCP → 3306 → Allow

#### Linux:
```bash
sudo ufw allow 3306/tcp
```

### BƯỚC 4: Test kết nối từ máy tính

Trước khi test từ Android, test từ máy tính trước:

```bash
# Test kết nối local
mysql -h 127.0.0.1 -u root -p

# Test kết nối từ IP (thay bằng IP thực của bạn)
mysql -h 192.168.1.100 -u root -p
```

**Nếu không kết nối được từ máy tính, sẽ KHÔNG kết nối được từ Android!**

### BƯỚC 5: Kiểm tra Database và bảng

```sql
-- Kiểm tra database tồn tại
SHOW DATABASES LIKE 'CoffeeShopDB';

-- Kiểm tra bảng Users
USE CoffeeShopDB;
SHOW TABLES;
DESCRIBE Users;

-- Nếu bảng chưa có cột phoneNumber, chạy:
ALTER TABLE Users 
ADD COLUMN phoneNumber VARCHAR(20) UNIQUE AFTER id,
ADD COLUMN fullName VARCHAR(100) AFTER phoneNumber,
ADD COLUMN createdAt BIGINT DEFAULT 0 AFTER email;
```

### BƯỚC 6: Xem Logcat để debug

1. Mở Android Studio
2. Mở Logcat (View → Tool Windows → Logcat)
3. Filter: `DatabaseHelper` hoặc `UserRepository`
4. Chạy app và thử đăng nhập
5. Xem các log để biết lỗi cụ thể

**Các log quan trọng:**
- `MySQL JDBC Driver loaded successfully` → Driver OK
- `Attempting to connect to: ...` → Đang thử kết nối
- `Database connection successful!` → ✅ Kết nối thành công
- `Database connection failed` → ❌ Xem error message bên dưới

### BƯỚC 7: Checklist

- [ ] Đã thay `localhost` bằng IP đúng (10.0.2.2 cho emulator hoặc IP máy tính cho thiết bị thật)
- [ ] MySQL server đang chạy
- [ ] bind-address = 0.0.0.0 trong my.ini/my.cnf
- [ ] Đã restart MySQL sau khi sửa bind-address
- [ ] User có quyền truy cập từ xa (root@'%')
- [ ] Port 3306 đã mở trong firewall
- [ ] Test kết nối từ máy tính thành công
- [ ] Thiết bị Android và máy tính cùng mạng WiFi (nếu dùng local IP)
- [ ] Database CoffeeShopDB đã được tạo
- [ ] Bảng Users có đủ các cột: id, phoneNumber, password, fullName, email, createdAt

## 🐛 CÁC LỖI THƯỜNG GẶP:

### "Communications link failure"
- ❌ MySQL server không chạy → Start MySQL
- ❌ IP/Port sai → Kiểm tra lại IP và port
- ❌ Firewall chặn → Mở port 3306

### "Access denied for user 'root'@..."
- ❌ Password sai → Kiểm tra lại password
- ❌ User không có quyền từ xa → Tạo user với @'%'

### "Unknown database 'CoffeeShopDB'"
- ❌ Database chưa được tạo → Tạo database
- ❌ Tên database sai → Kiểm tra chữ hoa/thường

### "Connection timed out"
- ❌ IP không đúng → Dùng IP thực, không dùng localhost
- ❌ Port bị chặn → Mở port 3306
- ❌ MySQL không lắng nghe trên IP đó → Sửa bind-address

## 📱 QUAN TRỌNG:

1. **KHÔNG BAO GIỜ dùng `localhost` trên Android!**
2. **Emulator:** Dùng `10.0.2.2`
3. **Thiết bị thật:** Dùng IP máy tính (ví dụ: `192.168.1.100`)
4. **Luôn test từ máy tính trước khi test từ Android**


