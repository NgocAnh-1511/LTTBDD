# Hướng dẫn Import Categories và Products từ Firebase

Script này sẽ tạo 2 bảng `categories` và `products` và import dữ liệu từ Firebase JSON.

## Bước 1: Kết nối đến Railway MySQL

1. Vào Railway Dashboard
2. Chọn service MySQL của bạn
3. Vào tab **Variables**
4. Copy các thông tin kết nối:
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
   - `MYSQLDATABASE` (thường là `railway`)

## Bước 2: Import Script SQL

### Cách 1: Sử dụng MySQL Workbench

1. Mở MySQL Workbench
2. Tạo connection mới với thông tin từ Railway:
   - Host: `MYSQLHOST`
   - Port: `MYSQLPORT`
   - Username: `MYSQLUSER`
   - Password: `MYSQLPASSWORD`
3. Kết nối và chọn database `CoffeShop`
4. Mở file `create_categories_products_from_firebase.sql`
5. Chạy toàn bộ script (Ctrl+Shift+Enter)

### Cách 2: Sử dụng Command Line

```bash
mysql -h <MYSQLHOST> -P <MYSQLPORT> -u <MYSQLUSER> -p<MYSQLPASSWORD> <MYSQLDATABASE> < create_categories_products_from_firebase.sql
```

Thay thế các giá trị trong `<>` bằng thông tin thực tế từ Railway.

## Bước 3: Kiểm tra dữ liệu

Sau khi import, kiểm tra bằng các câu lệnh:

```sql
-- Kiểm tra số lượng categories
SELECT COUNT(*) FROM categories;

-- Kiểm tra số lượng products
SELECT COUNT(*) FROM products;

-- Xem danh sách categories
SELECT * FROM categories;

-- Xem danh sách products với category
SELECT p.id, p.name, p.price, c.name as category_name 
FROM products p 
LEFT JOIN categories c ON p.categoryId = c.id 
LIMIT 10;
```

## Kết quả mong đợi

- **Categories**: 5 categories (Esspersso, Cappuccino, Latte, Americano, Hot Chocolate)
- **Products**: 35 products được phân loại theo các categories

## Lưu ý

- Script sẽ xóa và tạo lại bảng nếu đã tồn tại
- Dữ liệu cũ sẽ bị mất nếu bảng đã có dữ liệu
- Đảm bảo đã backup dữ liệu trước khi chạy script nếu cần

## Sau khi import thành công

Backend sẽ tự động sync dữ liệu này lên Firebase khi:
- Tạo mới product/category
- Cập nhật product/category
- Xóa product/category

Bạn có thể quản lý products và categories từ admin panel, và mọi thay đổi sẽ được sync tự động lên Firebase.

