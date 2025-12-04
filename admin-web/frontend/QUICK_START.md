# 🚀 Quick Start - Frontend

## ✅ Đã hoàn thành

1. ✅ Dependencies đã được cài đặt (282 packages)
2. ✅ Frontend server đang chạy trong background

## 🌐 Truy cập

**Frontend:** http://localhost:3001

## 🔐 Đăng nhập

Mở browser và truy cập: **http://localhost:3001**

**Thông tin đăng nhập:**
- Phone: `admin`
- Password: `admin123`

## 📋 Các bước đã thực hiện

1. ✅ Cài đặt dependencies: `npm install`
2. ✅ Tạo file `.env` với API URL
3. ✅ Khởi động dev server: `npm run dev`

## 🎨 Giao diện

Sau khi đăng nhập, bạn sẽ thấy:

- **Sidebar** bên trái với menu:
  - Dashboard
  - Users
  - Orders
  - Products
  - Categories
  - Vouchers
  - Banners

- **Header** trên cùng với:
  - Tên user
  - Avatar
  - Logout button

- **Main Content** hiển thị:
  - Dashboard với statistics
  - Tables cho Users, Orders, Vouchers

## ⚠️ Lưu ý

1. **Backend phải đang chạy:**
   - URL: http://localhost:3000/api
   - Nếu chưa chạy, mở terminal khác và chạy:
     ```powershell
     cd E:\namngu\admin-web\backend
     $env:PATH += ";E:\nodejs"
     npm run start:dev
     ```

2. **Nếu frontend không chạy:**
   - Kiểm tra port 3001 có bị chiếm không
   - Chạy lại: `npm run dev` trong thư mục frontend

3. **Nếu gặp lỗi CORS:**
   - Backend đã enable CORS
   - Kiểm tra backend có đang chạy không

## 🐛 Troubleshooting

### Frontend không load được
- Kiểm tra terminal có lỗi gì không
- Đảm bảo backend đang chạy

### Lỗi 401 khi đăng nhập
- Kiểm tra backend có đang chạy không
- Kiểm tra API URL trong `.env`

### Không kết nối được API
- Kiểm tra backend: http://localhost:3000/api
- Kiểm tra file `.env` có đúng không

## 📝 Files quan trọng

- `.env` - Cấu hình API URL (cần tạo nếu chưa có)
- `package.json` - Dependencies
- `vite.config.ts` - Vite configuration
- `src/App.tsx` - Main app component
- `src/pages/` - Các pages

## 🎯 Next Steps

1. ✅ Mở browser: http://localhost:3001
2. ✅ Đăng nhập với admin/admin123
3. ✅ Xem giao diện admin dashboard
4. ✅ Test các pages: Users, Orders, Vouchers

