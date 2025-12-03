# Admin Frontend - React Admin UI

React Admin Dashboard cho Coffee Shop Management System.

## 🚀 Quick Start

### 1. Cài đặt dependencies

```bash
cd admin-web/frontend
npm install
```

### 2. Tạo file .env

Tạo file `.env` trong thư mục `frontend`:

```env
VITE_API_URL=http://localhost:3000/api
```

### 3. Chạy development server

```bash
npm run dev
```

Frontend sẽ chạy tại: **http://localhost:3001**

## 📁 Cấu trúc

```
frontend/
├── src/
│   ├── components/     # Components dùng chung
│   │   ├── Layout.tsx  # Layout với sidebar
│   │   └── ProtectedRoute.tsx
│   ├── pages/          # Các pages
│   │   ├── Login.tsx
│   │   ├── Dashboard.tsx
│   │   ├── Users.tsx
│   │   ├── Orders.tsx
│   │   ├── Products.tsx
│   │   ├── Categories.tsx
│   │   ├── Vouchers.tsx
│   │   └── Banners.tsx
│   ├── services/       # API services
│   │   ├── api.ts
│   │   └── auth.service.ts
│   ├── App.tsx
│   └── main.tsx
```

## 🎨 Features

- ✅ Material-UI (MUI) components
- ✅ React Router for navigation
- ✅ React Query for data fetching
- ✅ Protected routes với authentication
- ✅ Responsive layout với sidebar
- ✅ Login page
- ✅ Dashboard với statistics
- ✅ Users management page
- ✅ Orders management page
- ✅ Vouchers management page

## 🔐 Authentication

- Login với phone number và password
- JWT token được lưu trong localStorage
- Auto redirect to login nếu chưa authenticated
- Auto logout nếu token hết hạn (401)

## 📝 Pages

### Login
- Đăng nhập với phone: `admin`, password: `admin123`

### Dashboard
- Hiển thị thống kê tổng quan
- Số lượng users, orders, vouchers, products

### Users
- Danh sách tất cả users
- Hiển thị thông tin user

### Orders
- Danh sách orders
- Hiển thị status với color coding
- Format date và price

### Vouchers
- Danh sách vouchers
- Hiển thị discount type và value
- Usage statistics

### Products, Categories, Banners
- Coming soon (sẽ load từ Firebase)

## 🛠️ Tech Stack

- **React 18** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool
- **Material-UI** - UI components
- **React Router** - Routing
- **React Query** - Data fetching
- **Axios** - HTTP client

## 📦 Build

```bash
npm run build
```

Output sẽ ở trong thư mục `dist/`

## 🧪 Development

```bash
npm run dev
```

Server sẽ chạy tại http://localhost:3001 với hot reload.

