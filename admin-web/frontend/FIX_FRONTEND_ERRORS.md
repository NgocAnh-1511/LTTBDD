# Fix Lỗi Frontend - Orders và Vouchers

## ❌ Lỗi

```
Cannot read properties of undefined (reading 'substring')
at Orders.tsx:75
```

## 🔍 Nguyên nhân

Frontend đang expect field `order_id` nhưng backend trả về `orderId` (camelCase) sau khi cập nhật entity.

## ✅ Đã sửa

### 1. Orders.tsx
- Hỗ trợ cả `orderId` và `order_id`
- Hỗ trợ cả `totalPrice` và `total_price`
- Hỗ trợ cả `customerName` và `customer_name`
- Fix format date cho BIGINT timestamp
- Thêm null checks

### 2. Vouchers.tsx
- Hỗ trợ cả `voucherId` và `voucher_id`
- Hỗ trợ cả camelCase và snake_case fields
- Thêm null checks

### 3. Dashboard.tsx
- Thêm loading state
- Hiển thị loading spinner khi đang fetch data

## 🔄 Refresh Frontend

Frontend sẽ tự động reload với Vite hot reload. Nếu không:
1. Refresh browser (F5)
2. Hoặc restart frontend:
   ```powershell
   cd E:\namngu\admin-web\frontend
   $env:PATH += ";E:\nodejs"
   npm run dev
   ```

## 📊 Dữ liệu sẽ hiển thị

Sau khi fix:
- **Orders:** 15 records với đầy đủ thông tin
- **Vouchers:** 5 records với discount info
- **Dashboard:** Statistics chính xác

## ⚠️ Lưu ý

Backend phải đã được restart sau khi cập nhật entities để trả về đúng format dữ liệu.

