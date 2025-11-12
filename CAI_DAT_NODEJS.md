# Hướng dẫn cài đặt Node.js

## Bước 1: Download Node.js

1. Truy cập: https://nodejs.org/
2. Tải phiên bản **LTS** (Long Term Support) - khuyến nghị
3. Chọn file cài đặt cho Windows (`.msi`)

## Bước 2: Cài đặt Node.js

1. Chạy file `.msi` vừa tải
2. Nhấn "Next" qua các bước
3. **QUAN TRỌNG:** Đảm bảo tick vào "Add to PATH" (thường được tick sẵn)
4. Nhấn "Install"
5. Đợi cài đặt hoàn tất
6. Nhấn "Finish"

## Bước 3: Kiểm tra cài đặt

Mở **PowerShell mới** (quan trọng: phải mở mới sau khi cài đặt) và chạy:

```powershell
node --version
npm --version
```

Nếu thấy số phiên bản (ví dụ: `v20.11.0` và `10.2.4`) → Đã cài đặt thành công!

## Bước 4: Chạy Backend

Sau khi cài đặt Node.js xong:

```powershell
cd D:\LTTBDD-main\backend
npm install
npm start
```

---

## Nếu vẫn báo lỗi "npm is not recognized"

### Giải pháp 1: Khởi động lại PowerShell/Terminal
- Đóng tất cả cửa sổ PowerShell/Terminal
- Mở lại PowerShell mới
- Thử lại lệnh `npm --version`

### Giải pháp 2: Kiểm tra PATH
1. Mở **System Properties** → **Environment Variables**
2. Trong **System Variables**, tìm biến `Path`
3. Kiểm tra có đường dẫn đến Node.js không (ví dụ: `C:\Program Files\nodejs\`)
4. Nếu không có, thêm đường dẫn vào

### Giải pháp 3: Cài đặt lại Node.js
- Gỡ Node.js cũ
- Tải và cài đặt lại từ https://nodejs.org/
- Đảm bảo tick "Add to PATH"

---

## Lưu ý

- Sau khi cài đặt Node.js, **PHẢI mở lại PowerShell/Terminal mới**
- Nếu vẫn không được, thử khởi động lại máy tính

