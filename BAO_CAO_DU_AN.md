# BÁO CÁO DỰ ÁN
## HỆ THỐNG QUẢN LÝ CỬA HÀNG CÀ PHÊ

---

## MỤC LỤC

1. [Giới thiệu dự án](#1-giới-thiệu-dự-án)
2. [Mục tiêu dự án](#2-mục-tiêu-dự-án)
3. [Công nghệ sử dụng](#3-công-nghệ-sử-dụng)
4. [Kiến trúc hệ thống](#4-kiến-trúc-hệ-thống)
5. [Phân tích chức năng](#5-phân-tích-chức-năng)
6. [Cơ sở dữ liệu](#6-cơ-sở-dữ-liệu)
7. [Giao diện người dùng](#7-giao-diện-người-dùng)
8. [API và Endpoints](#8-api-và-endpoints)
9. [Kết quả đạt được](#9-kết-quả-đạt-được)
10. [Kết luận và hướng phát triển](#10-kết-luận-và-hướng-phát-triển)

---

## 1. GIỚI THIỆU DỰ ÁN

### 1.1. Tổng quan

Hệ thống Quản lý Cửa hàng Cà phê là một giải pháp toàn diện được xây dựng để quản lý và vận hành một cửa hàng cà phê hiện đại. Dự án bao gồm ba thành phần chính:

- **Backend API**: Hệ thống quản lý dữ liệu và xử lý nghiệp vụ
- **Admin Web Panel**: Giao diện quản trị dành cho nhân viên và quản lý
- **Mobile Application**: Ứng dụng di động dành cho khách hàng

### 1.2. Bối cảnh và lý do thực hiện

Với sự phát triển của công nghệ và nhu cầu số hóa trong ngành F&B, việc xây dựng một hệ thống quản lý tích hợp giúp:

- Tối ưu hóa quy trình quản lý sản phẩm, đơn hàng
- Nâng cao trải nghiệm khách hàng với ứng dụng di động
- Tăng hiệu quả xử lý đơn hàng và quản lý kho
- Hỗ trợ marketing qua hệ thống voucher và khuyến mãi

### 1.3. Phạm vi dự án

Dự án tập trung vào các chức năng cốt lõi:

- Quản lý sản phẩm và danh mục
- Quản lý đơn hàng và thanh toán
- Quản lý người dùng và phân quyền
- Quản lý voucher và khuyến mãi
- Thống kê và báo cáo

---

## 2. MỤC TIÊU DỰ ÁN

### 2.1. Mục tiêu chính

1. **Xây dựng hệ thống quản lý toàn diện**
   - Quản lý sản phẩm, danh mục, đơn hàng
   - Quản lý người dùng và phân quyền
   - Thống kê và báo cáo doanh thu

2. **Phát triển ứng dụng di động**
   - Cho phép khách hàng đặt hàng trực tuyến
   - Quản lý giỏ hàng và lịch sử đơn hàng
   - Áp dụng voucher và khuyến mãi

3. **Xây dựng giao diện quản trị**
   - Dashboard thống kê trực quan
   - Quản lý CRUD đầy đủ cho tất cả module
   - Giao diện hiện đại, dễ sử dụng

### 2.2. Mục tiêu kỹ thuật

- Sử dụng kiến trúc RESTful API
- Bảo mật với JWT Authentication
- Responsive design cho mọi thiết bị
- Tối ưu hiệu năng và trải nghiệm người dùng

---

## 3. CÔNG NGHỆ SỬ DỤNG

### 3.1. Backend

**Framework và Core:**
- **NestJS** (v10.0.0): Framework Node.js với TypeScript, hỗ trợ kiến trúc modular
- **TypeORM** (v0.3.17): ORM cho TypeScript/JavaScript, hỗ trợ MySQL
- **MySQL**: Hệ quản trị cơ sở dữ liệu quan hệ
- **JWT (JSON Web Token)**: Xác thực và phân quyền người dùng
- **bcrypt**: Mã hóa mật khẩu

**Thư viện hỗ trợ:**
- **class-validator**: Validation cho DTOs
- **class-transformer**: Transform objects
- **@nestjs/config**: Quản lý cấu hình môi trường

### 3.2. Frontend Admin

**Core Framework:**
- **React** (v18.2.0): Thư viện JavaScript cho xây dựng UI
- **TypeScript**: Ngôn ngữ lập trình với type safety
- **Vite** (v5.0.8): Build tool và dev server nhanh

**UI Framework và Libraries:**
- **Material-UI (MUI)** (v5.15.0): Component library với Material Design
- **React Router** (v6.20.0): Routing cho single-page application
- **React Query (TanStack Query)** (v5.12.2): Data fetching và state management
- **Axios** (v1.6.2): HTTP client cho API calls
- **date-fns** (v2.30.0): Xử lý và format ngày tháng

### 3.3. Mobile Application

**Ngôn ngữ và Framework:**
- **Kotlin**: Ngôn ngữ lập trình chính
- **Android SDK**: Platform Android

**Thư viện chính:**
- **Retrofit**: REST API client cho Android
- **OkHttp**: HTTP client với logging và interceptors
- **Kotlin Coroutines**: Xử lý bất đồng bộ
- **Gson**: JSON parsing
- **Glide**: Load và cache images

**Kiến trúc:**
- **MVVM (Model-View-ViewModel)**: Kiến trúc ứng dụng
- **Repository Pattern**: Tách biệt data layer

### 3.4. Database

- **MySQL**: Hệ quản trị cơ sở dữ liệu quan hệ
- **TypeORM**: ORM framework cho TypeScript

---

## 4. KIẾN TRÚC HỆ THỐNG

### 4.1. Kiến trúc tổng thể

Hệ thống được xây dựng theo mô hình **3-tier architecture**:

```
┌─────────────────────────────────────────┐
│         CLIENT LAYER                    │
├─────────────────────────────────────────┤
│  Mobile App (Android)  │  Admin Web    │
│  (Kotlin)              │  (React)       │
└──────────────┬──────────┬───────────────┘
               │          │
               │ HTTP/REST│
               │          │
┌──────────────▼──────────▼───────────────┐
│         API LAYER (Backend)             │
├─────────────────────────────────────────┤
│  NestJS REST API                        │
│  - Authentication (JWT)                 │
│  - Business Logic                       │
│  - Data Validation                       │
└──────────────┬──────────────────────────┘
               │
               │ SQL
               │
┌──────────────▼──────────────────────────┐
│      DATA LAYER                        │
├─────────────────────────────────────────┤
│  MySQL Database                         │
│  - Users, Products, Orders, etc.        │
└─────────────────────────────────────────┘
```

### 4.2. Kiến trúc Backend (NestJS)

Backend được tổ chức theo mô hình **Modular Architecture**:

```
backend/
├── src/
│   ├── app.module.ts          # Root module
│   ├── main.ts                 # Entry point
│   ├── auth/                   # Authentication module
│   │   ├── auth.controller.ts
│   │   ├── auth.service.ts
│   │   ├── guards/            # JWT guards
│   │   └── strategies/        # Passport strategies
│   ├── users/                  # User management
│   ├── products/               # Product management
│   │   ├── products.controller.ts
│   │   ├── products-public.controller.ts  # Public API
│   │   ├── products.service.ts
│   │   └── entities/
│   ├── categories/             # Category management
│   ├── orders/                  # Order management
│   ├── vouchers/                # Voucher management
│   └── banners/                 # Banner management
```

**Đặc điểm kiến trúc:**
- **Modular Design**: Mỗi feature là một module độc lập
- **Dependency Injection**: Quản lý dependencies tự động
- **Guards**: Bảo vệ routes với JWT authentication
- **DTOs**: Data Transfer Objects cho validation
- **Entities**: TypeORM entities mapping với database

### 4.3. Kiến trúc Frontend (React)

Frontend sử dụng **Component-based Architecture**:

```
frontend/
├── src/
│   ├── App.tsx                 # Root component
│   ├── components/            # Reusable components
│   │   ├── Layout.tsx         # Main layout với sidebar
│   │   └── ProtectedRoute.tsx  # Route protection
│   ├── pages/                 # Page components
│   │   ├── Dashboard.tsx      # Dashboard với stats
│   │   ├── Products.tsx        # Product management
│   │   ├── Categories.tsx      # Category management
│   │   ├── Orders.tsx          # Order management
│   │   ├── Users.tsx           # User management
│   │   └── Vouchers.tsx        # Voucher management
│   └── services/              # API services
│       ├── api.ts             # Axios instance
│       └── auth.service.ts    # Auth service
```

**State Management:**
- **React Query**: Quản lý server state, caching, và synchronization
- **React Hooks**: Local component state
- **Context API**: Global state (nếu cần)

### 4.4. Kiến trúc Mobile App (Android)

Mobile app sử dụng **MVVM + Repository Pattern**:

```
app/src/main/java/com/example/coffeeshop/
├── Activity/                  # Views (Activities)
│   ├── MainActivity.kt
│   ├── LoginActivity.kt
│   ├── CartActivity.kt
│   └── OrderActivity.kt
├── ViewModel/                 # ViewModels
│   └── MainViewModel.kt
├── Repository/                 # Data layer
│   └── MainRepository.kt
├── Network/                    # API layer
│   ├── ApiService.kt          # Retrofit interface
│   └── ApiClient.kt            # Retrofit client
├── Domain/                     # Domain models
│   ├── ProductModel.kt
│   ├── OrderModel.kt
│   └── UserModel.kt
└── Manager/                    # Business logic
    ├── CartManager.kt
    └── OrderManager.kt
```

---

## 5. PHÂN TÍCH CHỨC NĂNG

### 5.1. Module Quản lý Người dùng (Users)

**Chức năng:**
- Đăng ký tài khoản mới
- Đăng nhập/Đăng xuất
- Quản lý thông tin cá nhân
- Phân quyền Admin/User

**API Endpoints:**
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/login` - Đăng nhập
- `GET /api/users` - Lấy danh sách users (Admin)
- `GET /api/users/:id` - Lấy thông tin user
- `PATCH /api/users/:id` - Cập nhật thông tin
- `DELETE /api/users/:id` - Xóa user (Admin)

**Admin Panel:**
- Xem danh sách tất cả users
- Chỉnh sửa thông tin user
- Toggle quyền Admin
- Xóa user

### 5.2. Module Quản lý Sản phẩm (Products)

**Chức năng:**
- Quản lý danh sách sản phẩm
- Thêm/Sửa/Xóa sản phẩm
- Phân loại theo category
- Quản lý giá và stock

**API Endpoints:**
- `GET /api/products` - Lấy tất cả sản phẩm
- `GET /api/products/:id` - Lấy chi tiết sản phẩm
- `POST /api/products` - Tạo sản phẩm mới (Admin)
- `PATCH /api/products/:id` - Cập nhật sản phẩm (Admin)
- `DELETE /api/products/:id` - Xóa sản phẩm (Admin)
- `GET /api/public/products` - Public API (không cần auth)
- `GET /api/public/products?categoryId=X` - Lọc theo category

**Admin Panel:**
- Dashboard hiển thị tổng số sản phẩm
- Bảng danh sách với hình ảnh, tên, giá, category
- Form thêm/sửa với đầy đủ trường: name, description, price, imageUrl, stock, categoryId
- Xóa sản phẩm với confirmation

**Mobile App:**
- Hiển thị danh sách sản phẩm
- Lọc theo category
- Xem chi tiết sản phẩm
- Thêm vào giỏ hàng

### 5.3. Module Quản lý Danh mục (Categories)

**Chức năng:**
- Quản lý danh mục sản phẩm
- Thêm/Sửa/Xóa danh mục
- Gán sản phẩm vào danh mục

**API Endpoints:**
- `GET /api/categories` - Lấy tất cả danh mục
- `GET /api/categories/:id` - Lấy chi tiết danh mục
- `POST /api/categories` - Tạo danh mục mới (Admin)
- `PATCH /api/categories/:id` - Cập nhật danh mục (Admin)
- `DELETE /api/categories/:id` - Xóa danh mục (Admin)
- `GET /api/public/categories` - Public API

**Danh mục hiện có:**
1. Esspersso (ID: 0)
2. Cappuccino (ID: 1)
3. Latte (ID: 2)
4. Americano (ID: 3)
5. Hot Chocolate (ID: 4)

### 5.4. Module Quản lý Đơn hàng (Orders)

**Chức năng:**
- Tạo đơn hàng mới
- Quản lý trạng thái đơn hàng
- Xem chi tiết đơn hàng
- Thống kê doanh thu

**Trạng thái đơn hàng:**
- Pending: Chờ xử lý
- Confirmed: Đã xác nhận
- Processing: Đang xử lý
- Shipping: Đang giao hàng
- Delivered: Đã giao
- Completed: Hoàn thành
- Cancelled: Đã hủy

**API Endpoints:**
- `GET /api/orders` - Lấy danh sách đơn hàng
- `GET /api/orders/:id` - Lấy chi tiết đơn hàng
- `POST /api/orders` - Tạo đơn hàng mới
- `PATCH /api/orders/:id` - Cập nhật đơn hàng
- `PATCH /api/orders/:id/status` - Cập nhật trạng thái
- `DELETE /api/orders/:id` - Xóa đơn hàng (Admin)

**Admin Panel:**
- Dashboard hiển thị: Tổng orders, Tổng revenue, Pending orders
- Bảng danh sách với: Order ID, Customer, Total, Status, Date
- Xem chi tiết đơn hàng: Thông tin khách hàng, địa chỉ, items, tổng tiền
- Cập nhật trạng thái đơn hàng
- Xóa đơn hàng

**Mobile App:**
- Tạo đơn hàng từ giỏ hàng
- Xem lịch sử đơn hàng
- Theo dõi trạng thái đơn hàng

### 5.5. Module Quản lý Voucher

**Chức năng:**
- Tạo và quản lý voucher
- Áp dụng voucher cho đơn hàng
- Theo dõi số lần sử dụng

**Loại voucher:**
- PERCENT: Giảm theo phần trăm
- AMOUNT: Giảm số tiền cố định

**API Endpoints:**
- `GET /api/vouchers` - Lấy danh sách voucher
- `GET /api/vouchers/:id` - Lấy chi tiết voucher
- `GET /api/vouchers/code/:code` - Tìm voucher theo code
- `POST /api/vouchers` - Tạo voucher mới (Admin)
- `PATCH /api/vouchers/:id` - Cập nhật voucher (Admin)
- `DELETE /api/vouchers/:id` - Xóa voucher (Admin)

**Admin Panel:**
- Quản lý voucher với form đầy đủ: code, name, type, value, minPurchaseAmount, usageLimit, dates
- Xem thống kê sử dụng

### 5.6. Module Dashboard

**Chức năng:**
- Hiển thị thống kê tổng quan
- Biểu đồ doanh thu
- Phân tích dữ liệu

**Thống kê hiển thị:**
- Total Revenue: Tổng doanh thu
- Total Orders: Tổng số đơn hàng
- Total Users: Tổng số người dùng
- Active Products: Số sản phẩm đang hoạt động
- Pending Orders: Đơn hàng chờ xử lý
- Completed Orders: Đơn hàng đã hoàn thành
- Active Vouchers: Voucher đang hoạt động

**Biểu đồ:**
- Revenue Chart: Doanh thu 7 ngày gần nhất
- Order Status Distribution: Phân bố trạng thái đơn hàng

**Danh sách:**
- Recent Orders: 5 đơn hàng gần nhất
- Top Products: Top 5 sản phẩm bán chạy

---

## 6. CƠ SỞ DỮ LIỆU

### 6.1. Thiết kế Database

Hệ thống sử dụng **MySQL** với các bảng chính:

#### 6.1.1. Bảng `users`
Quản lý thông tin người dùng và admin.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| user_id | VARCHAR(255) | Primary Key |
| phone_number | VARCHAR(50) | Số điện thoại (unique) |
| full_name | VARCHAR(255) | Họ tên |
| email | VARCHAR(255) | Email |
| password | VARCHAR(255) | Mật khẩu (đã hash) |
| avatar_path | TEXT | Đường dẫn avatar |
| created_at | BIGINT | Thời gian tạo |
| is_logged_in | TINYINT(1) | Trạng thái đăng nhập |
| is_admin | TINYINT(1) | Quyền admin |

#### 6.1.2. Bảng `categories`
Quản lý danh mục sản phẩm.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| id | INT(11) | Primary Key, Auto Increment |
| name | VARCHAR(255) | Tên danh mục |
| description | TEXT | Mô tả |
| imageUrl | VARCHAR(500) | URL hình ảnh |
| isActive | TINYINT(1) | Trạng thái hoạt động |
| createdAt | DATETIME(6) | Thời gian tạo |
| updatedAt | DATETIME(6) | Thời gian cập nhật |

**Danh mục hiện có:**
- Esspersso (ID: 0)
- Cappuccino (ID: 1)
- Latte (ID: 2)
- Americano (ID: 3)
- Hot Chocolate (ID: 4)

#### 6.1.3. Bảng `products`
Quản lý sản phẩm.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| id | INT(11) | Primary Key, Auto Increment |
| name | VARCHAR(255) | Tên sản phẩm |
| description | TEXT | Mô tả |
| price | DECIMAL(10,2) | Giá bán |
| originalPrice | DECIMAL(10,2) | Giá gốc |
| imageUrl | VARCHAR(500) | URL hình ảnh |
| stock | INT(11) | Số lượng tồn kho |
| isActive | TINYINT(1) | Trạng thái hoạt động |
| categoryId | INT(11) | Foreign Key → categories.id |
| createdAt | DATETIME(6) | Thời gian tạo |
| updatedAt | DATETIME(6) | Thời gian cập nhật |

**Số lượng sản phẩm:** ~34 sản phẩm thuộc 5 danh mục

#### 6.1.4. Bảng `orders`
Quản lý đơn hàng.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| order_id | VARCHAR(255) | Primary Key |
| user_id | VARCHAR(255) | Foreign Key → users.user_id |
| total_price | DECIMAL(10,2) | Tổng tiền |
| order_date | BIGINT | Thời gian đặt hàng (timestamp) |
| status | VARCHAR(50) | Trạng thái đơn hàng |
| delivery_address | TEXT | Địa chỉ giao hàng |
| phone_number | VARCHAR(50) | Số điện thoại |
| customer_name | VARCHAR(255) | Tên khách hàng |
| payment_method | VARCHAR(100) | Phương thức thanh toán |
| created_at | DATETIME(6) | Thời gian tạo |
| updated_at | DATETIME(6) | Thời gian cập nhật |

#### 6.1.5. Bảng `order_items`
Quản lý chi tiết items trong đơn hàng.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| id | INT(11) | Primary Key, Auto Increment |
| order_id | VARCHAR(255) | Foreign Key → orders.order_id |
| item_title | VARCHAR(255) | Tên sản phẩm |
| item_json | TEXT | JSON chứa thông tin chi tiết |
| quantity | INT(11) | Số lượng |
| price | DECIMAL(10,2) | Giá đơn vị |
| subtotal | DECIMAL(10,2) | Thành tiền |
| created_at | DATETIME(6) | Thời gian tạo |

#### 6.1.6. Bảng `vouchers`
Quản lý voucher và khuyến mãi.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| voucher_id | VARCHAR(255) | Primary Key |
| code | VARCHAR(100) | Mã voucher (unique) |
| discount_percent | DECIMAL(5,2) | Phần trăm giảm |
| discount_amount | DECIMAL(10,2) | Số tiền giảm |
| discount_type | VARCHAR(20) | Loại: PERCENT/AMOUNT |
| min_order_amount | DECIMAL(10,2) | Đơn hàng tối thiểu |
| max_discount_amount | DECIMAL(10,2) | Giảm tối đa |
| start_date | BIGINT | Ngày bắt đầu |
| end_date | BIGINT | Ngày kết thúc |
| usage_limit | INT(11) | Giới hạn sử dụng |
| used_count | INT(11) | Số lần đã dùng |
| is_active | TINYINT(1) | Trạng thái hoạt động |
| description | TEXT | Mô tả |

#### 6.1.7. Bảng `addresses`
Quản lý địa chỉ giao hàng của người dùng.

| Cột | Kiểu dữ liệu | Mô tả |
|-----|--------------|-------|
| id | VARCHAR(255) | Primary Key |
| user_id | VARCHAR(255) | Foreign Key → users.user_id |
| name | VARCHAR(255) | Tên người nhận |
| phone | VARCHAR(50) | Số điện thoại |
| address | TEXT | Địa chỉ chi tiết |
| is_default | TINYINT(1) | Địa chỉ mặc định |
| created_at | DATETIME(6) | Thời gian tạo |
| updated_at | DATETIME(6) | Thời gian cập nhật |

### 6.2. Quan hệ giữa các bảng

```
users (1) ──< (N) orders
users (1) ──< (N) addresses

categories (1) ──< (N) products

orders (1) ──< (N) order_items
```

### 6.3. Indexes và Performance

- Index trên `users.phone_number` để tìm kiếm nhanh khi đăng nhập
- Index trên `orders.user_id` và `orders.status` để query nhanh
- Index trên `products.categoryId` để filter theo category
- Index trên `vouchers.code` để tìm voucher nhanh

---

## 7. GIAO DIỆN NGƯỜI DÙNG

### 7.1. Admin Web Panel

#### 7.1.1. Dashboard
- **Layout**: Gradient stat cards với hover animations
- **Thống kê chính**: Revenue, Orders, Users, Products
- **Biểu đồ**: Revenue chart (7 ngày), Order status distribution
- **Danh sách**: Recent orders, Top products
- **Màu sắc**: Professional gradient scheme

#### 7.1.2. Products Management
- **Bảng dữ liệu**: Hiển thị ID, Image, Name, Category, Price, Description
- **Chức năng**: Add, Edit, Delete
- **Form**: Dialog với đầy đủ trường (name, description, price, imageUrl, stock, category)
- **Stats Cards**: Total Products, Active Products

#### 7.1.3. Categories Management
- **Bảng dữ liệu**: ID, Image, Name, Description
- **Chức năng**: Add, Edit, Delete
- **Form**: Dialog với name, description, imageUrl, isActive

#### 7.1.4. Orders Management
- **Bảng dữ liệu**: Order ID, Customer, Total, Status (với color chips), Date
- **Chức năng**: View Details, Update Status, Delete
- **Stats Cards**: Total Orders, Total Revenue, Pending Orders
- **Status Update**: Dialog với buttons cho từng trạng thái
- **Order Details**: Dialog hiển thị đầy đủ thông tin và items

#### 7.1.5. Users Management
- **Bảng dữ liệu**: Avatar, User ID, Phone, Name, Email, Role (Admin/User)
- **Chức năng**: Edit, Toggle Admin, Delete
- **Stats Cards**: Total Users, Admins count
- **Form**: Edit user info, toggle admin status

#### 7.1.6. Vouchers Management
- **Bảng dữ liệu**: Code, Name, Type, Value, Min Order, Used/Limit, Status
- **Chức năng**: Add, Edit, Delete
- **Form**: Dialog với đầy đủ trường voucher

#### 7.1.7. Design System
- **Color Scheme**: Primary (Blue), Success (Green), Warning (Orange), Error (Red)
- **Typography**: Material-UI typography system
- **Icons**: Material Icons
- **Spacing**: Consistent 8px grid system
- **Shadows**: Elevation system cho depth
- **Animations**: Smooth transitions và hover effects

### 7.2. Mobile Application

#### 7.2.1. Màn hình chính
- **Home Screen**: Banner, Categories, Popular Products
- **Navigation**: Bottom navigation bar
- **Search**: Tìm kiếm sản phẩm

#### 7.2.2. Chi tiết sản phẩm
- Hiển thị hình ảnh, tên, giá, mô tả
- Tùy chỉnh: Size, đá, đường, topping
- Thêm vào giỏ hàng

#### 7.2.3. Giỏ hàng
- Danh sách sản phẩm đã chọn
- Tính tổng tiền tự động
- Áp dụng voucher
- Thanh toán

#### 7.2.4. Đơn hàng
- Tạo đơn hàng mới
- Xem lịch sử đơn hàng
- Theo dõi trạng thái

#### 7.2.5. Tài khoản
- Đăng nhập/Đăng ký
- Thông tin cá nhân
- Địa chỉ giao hàng
- Lịch sử đơn hàng

---

## 8. API VÀ ENDPOINTS

### 8.1. Authentication API

```
POST   /api/auth/register      # Đăng ký
POST   /api/auth/login         # Đăng nhập
GET    /api/auth/profile       # Lấy thông tin user hiện tại
```

### 8.2. Users API

```
GET    /api/users              # Lấy danh sách users (Admin)
GET    /api/users/:id          # Lấy chi tiết user
PATCH  /api/users/:id          # Cập nhật user
DELETE /api/users/:id          # Xóa user (Admin)
```

### 8.3. Products API

**Admin (cần JWT):**
```
GET    /api/products           # Lấy tất cả sản phẩm
GET    /api/products/:id       # Lấy chi tiết sản phẩm
POST   /api/products           # Tạo sản phẩm mới
PATCH  /api/products/:id      # Cập nhật sản phẩm
DELETE /api/products/:id      # Xóa sản phẩm
```

**Public (không cần JWT):**
```
GET    /api/public/products              # Lấy tất cả sản phẩm
GET    /api/public/products/:id          # Lấy chi tiết sản phẩm
GET    /api/public/products?categoryId=X # Lọc theo category
```

### 8.4. Categories API

**Admin:**
```
GET    /api/categories         # Lấy danh sách categories
GET    /api/categories/:id     # Lấy chi tiết category
POST   /api/categories         # Tạo category mới
PATCH  /api/categories/:id    # Cập nhật category
DELETE /api/categories/:id     # Xóa category
```

**Public:**
```
GET    /api/public/categories        # Lấy danh sách categories
GET    /api/public/categories/:id     # Lấy chi tiết category
```

### 8.5. Orders API

```
GET    /api/orders             # Lấy danh sách orders
GET    /api/orders/:id         # Lấy chi tiết order
POST   /api/orders             # Tạo order mới
PATCH  /api/orders/:id         # Cập nhật order
PATCH  /api/orders/:id/status  # Cập nhật trạng thái
DELETE /api/orders/:id         # Xóa order (Admin)
```

### 8.6. Vouchers API

```
GET    /api/vouchers           # Lấy danh sách vouchers
GET    /api/vouchers/:id       # Lấy chi tiết voucher
GET    /api/vouchers/code/:code # Tìm voucher theo code
POST   /api/vouchers           # Tạo voucher mới (Admin)
PATCH  /api/vouchers/:id       # Cập nhật voucher (Admin)
DELETE /api/vouchers/:id       # Xóa voucher (Admin)
```

### 8.7. Bảo mật

- **JWT Authentication**: Tất cả API (trừ public endpoints) yêu cầu JWT token
- **Password Hashing**: Sử dụng bcrypt với salt rounds
- **Input Validation**: DTOs với class-validator
- **CORS**: Cấu hình cho phép requests từ frontend và mobile app

---

## 9. KẾT QUẢ ĐẠT ĐƯỢC

### 9.1. Chức năng đã hoàn thành

#### Backend
✅ RESTful API đầy đủ cho tất cả modules
✅ JWT Authentication và Authorization
✅ Public API endpoints cho mobile app
✅ Database schema hoàn chỉnh
✅ Data validation với DTOs
✅ Error handling

#### Frontend Admin
✅ Dashboard với thống kê và biểu đồ
✅ CRUD đầy đủ cho Products, Categories, Orders, Users, Vouchers
✅ Giao diện hiện đại với Material-UI
✅ Responsive design
✅ Real-time data với React Query
✅ Protected routes với authentication

#### Mobile App
✅ Đăng ký/Đăng nhập
✅ Xem danh sách sản phẩm và categories
✅ Giỏ hàng và thanh toán
✅ Tạo và xem đơn hàng
✅ Áp dụng voucher
✅ MVVM architecture

### 9.2. Số liệu thống kê

- **Số lượng modules**: 6 modules chính (Users, Products, Categories, Orders, Vouchers, Banners)
- **API endpoints**: 30+ endpoints
- **Database tables**: 7 bảng chính
- **Sản phẩm**: ~34 sản phẩm
- **Danh mục**: 5 danh mục
- **Frontend pages**: 8 pages
- **Mobile screens**: 10+ screens

### 9.3. Chất lượng code

- **TypeScript**: Type safety cho backend và frontend
- **Modular Architecture**: Code dễ maintain và mở rộng
- **Best Practices**: Tuân thủ NestJS và React best practices
- **Error Handling**: Xử lý lỗi đầy đủ
- **Code Organization**: Cấu trúc rõ ràng, dễ đọc

---

## 10. KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN

### 10.1. Kết luận

Dự án Hệ thống Quản lý Cửa hàng Cà phê đã được xây dựng thành công với đầy đủ các chức năng cốt lõi:

1. **Hệ thống quản lý toàn diện**: Backend API mạnh mẽ với NestJS, hỗ trợ đầy đủ CRUD operations
2. **Giao diện quản trị chuyên nghiệp**: Admin panel với dashboard thống kê, giao diện hiện đại
3. **Ứng dụng di động**: Mobile app với đầy đủ chức năng cho khách hàng
4. **Bảo mật**: JWT authentication, password hashing, input validation
5. **Hiệu năng**: Optimized queries, caching với React Query

Dự án đã đáp ứng được các mục tiêu ban đầu và sẵn sàng để triển khai trong môi trường thực tế.

### 10.2. Hướng phát triển

#### Ngắn hạn
1. **Thêm tính năng thanh toán online**: Tích hợp payment gateway (VNPay, Momo)
2. **Push notifications**: Thông báo cho khách hàng về trạng thái đơn hàng
3. **Đánh giá sản phẩm**: Cho phép khách hàng đánh giá và review
4. **Tìm kiếm nâng cao**: Full-text search cho sản phẩm

#### Trung hạn
1. **Multi-language**: Hỗ trợ đa ngôn ngữ
2. **Inventory management**: Quản lý kho hàng chi tiết
3. **Reporting**: Báo cáo chi tiết với export Excel/PDF
4. **Analytics**: Phân tích hành vi khách hàng

#### Dài hạn
1. **Multi-store**: Hỗ trợ nhiều cửa hàng
2. **Loyalty program**: Chương trình tích điểm
3. **AI recommendations**: Gợi ý sản phẩm bằng AI
4. **Mobile app cho Admin**: Ứng dụng quản lý trên mobile

### 10.3. Bài học kinh nghiệm

1. **Kiến trúc modular**: Giúp code dễ maintain và mở rộng
2. **TypeScript**: Type safety giúp giảm lỗi trong quá trình phát triển
3. **API design**: Public API riêng giúp mobile app không cần authentication
4. **State management**: React Query giúp quản lý server state hiệu quả
5. **UI/UX**: Material-UI giúp tạo giao diện đẹp và nhất quán

---

## PHỤ LỤC

### A. Cấu trúc thư mục chi tiết

```
.
├── admin-web/
│   ├── backend/
│   │   ├── src/
│   │   │   ├── auth/          # Authentication module
│   │   │   ├── users/         # User management
│   │   │   ├── products/      # Product management
│   │   │   ├── categories/    # Category management
│   │   │   ├── orders/        # Order management
│   │   │   ├── vouchers/      # Voucher management
│   │   │   └── banners/       # Banner management
│   │   └── database/          # SQL scripts
│   └── frontend/
│       └── src/
│           ├── components/    # Reusable components
│           ├── pages/        # Page components
│           └── services/     # API services
└── LTTBDD-main/              # Android app
    └── app/src/main/
        ├── java/.../Activity/    # Activities
        ├── java/.../ViewModel/   # ViewModels
        ├── java/.../Repository/  # Repositories
        └── java/.../Network/     # API clients
```

### B. Thông tin đăng nhập mặc định

**Admin:**
- Phone: `admin`
- Password: `admin123`

**Test User:**
- Phone: `0846230059`
- Password: `Nam26122005@`

### C. URLs và Ports

- Backend API: `http://localhost:3000/api`
- Admin Panel: `http://localhost:3001`
- Database: `localhost:3306` (MySQL)

### D. Công nghệ và phiên bản

**Backend:**
- Node.js: v18+
- NestJS: v10.0.0
- TypeORM: v0.3.17
- MySQL: Latest

**Frontend:**
- React: v18.2.0
- TypeScript: v5.2.2
- Material-UI: v5.15.0
- Vite: v5.0.8

**Mobile:**
- Kotlin: Latest
- Android SDK: API 28+
- Retrofit: Latest
- OkHttp: Latest

---

**Ngày hoàn thành báo cáo:** [Ngày hiện tại]
**Phiên bản:** 1.0
**Tác giả:** [Tên tác giả]

---

*Báo cáo này mô tả chi tiết về dự án Hệ thống Quản lý Cửa hàng Cà phê, bao gồm kiến trúc, chức năng, công nghệ và kết quả đạt được.*

