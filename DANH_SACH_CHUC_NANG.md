# DANH SÁCH CHỨC NĂNG HỆ THỐNG COFFEE SHOP

## 📱 **ANDROID APP - ỨNG DỤNG DI ĐỘNG**

### 🔐 **1. Xác Thực & Tài Khoản**

#### **LoginActivity** - Đăng nhập/Đăng ký
- ✅ Đăng nhập bằng số điện thoại và mật khẩu
- ✅ Đăng ký tài khoản mới
- ✅ Tự động chuyển hướng nếu đã đăng nhập
- ✅ Validation số điện thoại và mật khẩu
- ✅ Lưu trữ token JWT

#### **SplashActivity** - Màn hình khởi động
- ✅ Hiển thị màn hình chào mừng
- ✅ Chuyển hướng đến MainActivity

#### **ProfileActivity** - Hồ sơ người dùng
- ✅ Xem thông tin cá nhân
- ✅ Chỉnh sửa thông tin (tên, email, avatar)
- ✅ Quản lý địa chỉ giao hàng
- ✅ Xem danh sách mã giảm giá
- ✅ Xem lịch sử đơn hàng
- ✅ Đăng xuất
- ✅ Chuyển đổi sang chế độ Admin (nếu có quyền)

#### **AccountInfoActivity** - Thông tin tài khoản
- ✅ Xem và chỉnh sửa thông tin cá nhân
- ✅ Cập nhật avatar

#### **CompleteProfileActivity** - Hoàn thiện hồ sơ
- ✅ Điền thông tin bổ sung khi đăng ký

#### **ChangePasswordActivity** - Đổi mật khẩu
- ✅ Thay đổi mật khẩu tài khoản

---

### 🏠 **2. Trang Chủ & Duyệt Sản Phẩm**

#### **MainActivity** - Trang chủ
- ✅ Hiển thị banner quảng cáo (ViewPager2)
- ✅ Danh sách danh mục sản phẩm (Categories)
- ✅ Sản phẩm bán chạy (Best Seller)
- ✅ Sản phẩm gợi ý (For You)
- ✅ Tin tức/Khuyến mãi (News)
- ✅ Navigation bar: Home, Search, Cart, Profile, Orders, Wishlist
- ✅ Badge số lượng sản phẩm trong giỏ hàng
- ✅ Tải dữ liệu từ API (Products, Categories)

#### **OrderDrinkActivity** - Danh sách sản phẩm
- ✅ Hiển thị tất cả sản phẩm
- ✅ Lọc sản phẩm theo danh mục
- ✅ Tìm kiếm sản phẩm
- ✅ Phân trang và scroll vô hạn
- ✅ Hiển thị giá, hình ảnh, mô tả

#### **ItemsListActivity** - Danh sách sản phẩm theo danh mục
- ✅ Hiển thị sản phẩm theo danh mục cụ thể
- ✅ Grid layout với hình ảnh

#### **DetailActivity** - Chi tiết sản phẩm
- ✅ Xem chi tiết sản phẩm
- ✅ Hình ảnh, mô tả, giá
- ✅ Chọn số lượng
- ✅ Thêm vào giỏ hàng
- ✅ Thêm vào danh sách yêu thích (Wishlist)
- ✅ Xem đánh giá (nếu có)

#### **SearchActivity** - Tìm kiếm
- ✅ Tìm kiếm sản phẩm theo tên
- ✅ Gợi ý tìm kiếm
- ✅ Lọc kết quả

---

### 🛒 **3. Giỏ Hàng & Thanh Toán**

#### **CartActivity** - Giỏ hàng
- ✅ Xem danh sách sản phẩm trong giỏ
- ✅ Thay đổi số lượng
- ✅ Xóa sản phẩm khỏi giỏ
- ✅ Tính tổng tiền
- ✅ Áp dụng mã giảm giá
- ✅ Chuyển đến trang thanh toán

#### **CheckoutActivity** - Thanh toán
- ✅ Xem lại đơn hàng
- ✅ Chọn địa chỉ giao hàng
- ✅ Chọn phương thức thanh toán (Tiền mặt/Thẻ)
- ✅ Áp dụng mã giảm giá
- ✅ Tính toán giảm giá tự động
- ✅ Xác nhận và tạo đơn hàng
- ✅ Tăng số lần sử dụng voucher sau khi đặt hàng thành công

---

### 📦 **4. Đơn Hàng**

#### **OrderActivity** - Lịch sử đơn hàng
- ✅ Xem tất cả đơn hàng của user
- ✅ Lọc theo trạng thái (Pending, Processing, Completed, Cancelled)
- ✅ Hiển thị thông tin: Mã đơn, ngày đặt, tổng tiền, trạng thái
- ✅ Pull to refresh

#### **OrderDetailActivity** - Chi tiết đơn hàng
- ✅ Xem chi tiết đơn hàng
- ✅ Danh sách sản phẩm trong đơn
- ✅ Thông tin giao hàng
- ✅ Trạng thái đơn hàng
- ✅ Tổng tiền và giảm giá

---

### 🎫 **5. Mã Giảm Giá**

#### **VoucherListActivity** - Danh sách mã giảm giá
- ✅ Xem tất cả mã giảm giá
- ✅ Lọc mã khả dụng/Không khả dụng
- ✅ Tìm kiếm mã giảm giá theo code
- ✅ Chọn mã giảm giá để áp dụng (từ CheckoutActivity)
- ✅ Hiển thị thông tin: Code, mô tả, giá trị giảm, hạn sử dụng
- ✅ Validation mã giảm giá (thời gian, số lần sử dụng, giá trị đơn hàng tối thiểu)

---

### ❤️ **6. Danh Sách Yêu Thích**

#### **WishlistActivity** - Yêu thích
- ✅ Xem danh sách sản phẩm yêu thích
- ✅ Xóa sản phẩm khỏi danh sách
- ✅ Thêm vào giỏ hàng từ wishlist

---

### 📍 **7. Địa Chỉ**

#### **AddressListActivity** - Quản lý địa chỉ
- ✅ Xem danh sách địa chỉ giao hàng
- ✅ Thêm địa chỉ mới
- ✅ Chỉnh sửa địa chỉ
- ✅ Xóa địa chỉ
- ✅ Đặt địa chỉ mặc định

---

### 👨‍💼 **8. Chức Năng Admin (Trong App)**

#### **AdminOrderActivity** - Quản lý đơn hàng (Admin)
- ✅ Xem tất cả đơn hàng
- ✅ Cập nhật trạng thái đơn hàng
- ✅ Xóa đơn hàng

#### **AdminVoucherActivity** - Quản lý mã giảm giá (Admin)
- ✅ Xem danh sách mã giảm giá
- ✅ Thêm mã giảm giá mới
- ✅ Chỉnh sửa mã giảm giá
- ✅ Xóa mã giảm giá
- ✅ Bật/tắt mã giảm giá

#### **RevenueReportActivity** - Báo cáo doanh thu (Admin)
- ✅ Xem báo cáo doanh thu
- ✅ Thống kê theo thời gian

---

## 💻 **ADMIN WEB PANEL - TRANG QUẢN TRỊ**

### 🔐 **1. Xác Thực**

#### **Login Page** - Đăng nhập Admin
- ✅ Đăng nhập bằng số điện thoại/username và mật khẩu
- ✅ JWT Authentication
- ✅ Protected Routes - Chỉ admin mới truy cập được
- ✅ Tự động redirect nếu chưa đăng nhập

---

### 📊 **2. Dashboard - Bảng Điều Khiển**

#### **Dashboard Page**
- ✅ **Thống kê tổng quan:**
  - Tổng số người dùng
  - Tổng số đơn hàng
  - Tổng doanh thu
  - Tổng số sản phẩm
  
- ✅ **Thống kê phụ:**
  - Đơn hàng đang chờ xử lý
  - Đơn hàng đã hoàn thành
  - Mã giảm giá đang hoạt động
  - Số lượng danh mục

- ✅ **Biểu đồ doanh thu:**
  - Bar chart doanh thu 7 ngày gần nhất
  - Hiển thị xu hướng tăng/giảm

- ✅ **Phân bổ trạng thái đơn hàng:**
  - Progress bars cho từng trạng thái
  - Tỷ lệ phần trăm

- ✅ **Bảng đơn hàng gần đây:**
  - 5 đơn hàng mới nhất
  - Thông tin: Mã đơn, khách hàng, tổng tiền, trạng thái

- ✅ **Top sản phẩm bán chạy:**
  - 5 sản phẩm được đặt nhiều nhất
  - Số lượng đã bán

- ✅ **UI/UX:**
  - Gradient cards với hover effects
  - Icons và màu sắc phân biệt
  - Responsive design
  - Auto-refresh data

---

### 📦 **3. Quản Lý Sản Phẩm (Products)**

#### **Products Page**
- ✅ **Xem danh sách:**
  - Hiển thị tất cả sản phẩm trong bảng
  - Thông tin: Hình ảnh, Tên, Danh mục, Giá, Mô tả, Stock, Trạng thái
  - Phân trang và tìm kiếm

- ✅ **Thêm sản phẩm:**
  - Form nhập: Tên, Mô tả, Giá, Giá gốc, Hình ảnh (URL), Stock, Danh mục, Trạng thái
  - Validation đầy đủ
  - Upload/Chọn hình ảnh

- ✅ **Chỉnh sửa sản phẩm:**
  - Sửa tất cả thông tin sản phẩm
  - Dialog form với dữ liệu đã điền sẵn

- ✅ **Xóa sản phẩm:**
  - Xác nhận trước khi xóa
  - Dialog xác nhận

- ✅ **Thống kê:**
  - Card hiển thị tổng số sản phẩm
  - Card hiển thị sản phẩm đang hoạt động

- ✅ **Tính năng khác:**
  - Refresh dữ liệu
  - Loading states
  - Error handling
  - Snackbar notifications

---

### 🏷️ **4. Quản Lý Danh Mục (Categories)**

#### **Categories Page**
- ✅ **Xem danh sách:**
  - Hiển thị tất cả danh mục trong bảng
  - Thông tin: Hình ảnh, Tên, Mô tả, Trạng thái

- ✅ **Thêm danh mục:**
  - Form nhập: Tên, Mô tả, Hình ảnh (URL), Trạng thái
  - Validation

- ✅ **Chỉnh sửa danh mục:**
  - Sửa tất cả thông tin danh mục
  - Dialog form

- ✅ **Xóa danh mục:**
  - Xác nhận trước khi xóa

- ✅ **Thống kê:**
  - Card tổng số danh mục
  - Card danh mục đang hoạt động

---

### 🛒 **5. Quản Lý Đơn Hàng (Orders)**

#### **Orders Page**
- ✅ **Xem danh sách:**
  - Hiển thị tất cả đơn hàng trong bảng
  - Thông tin: Mã đơn, Khách hàng, Tổng tiền, Ngày đặt, Trạng thái, Địa chỉ
  - Sắp xếp theo ngày (mới nhất trước)

- ✅ **Xem chi tiết đơn hàng:**
  - Dialog hiển thị đầy đủ thông tin
  - Danh sách sản phẩm trong đơn
  - Thông tin giao hàng
  - Phương thức thanh toán

- ✅ **Cập nhật trạng thái đơn hàng:**
  - Dropdown chọn trạng thái mới
  - Các trạng thái: Pending, Confirmed, Processing, Shipping, Delivered, Completed, Cancelled
  - Color-coded status chips

- ✅ **Xóa đơn hàng:**
  - Xác nhận trước khi xóa

- ✅ **Thống kê:**
  - Card tổng số đơn hàng
  - Card tổng doanh thu
  - Card đơn hàng đang chờ xử lý
  - Card đơn hàng đã hoàn thành

---

### 👥 **6. Quản Lý Người Dùng (Users)**

#### **Users Page**
- ✅ **Xem danh sách:**
  - Hiển thị tất cả người dùng trong bảng
  - Thông tin: Avatar, Tên, Email, Số điện thoại, Quyền Admin, Ngày tạo

- ✅ **Chỉnh sửa người dùng:**
  - Sửa thông tin: Tên, Email, Avatar
  - Dialog form

- ✅ **Thay đổi quyền Admin:**
  - Toggle switch để bật/tắt quyền admin
  - Xác nhận trước khi thay đổi

- ✅ **Xóa người dùng:**
  - Xác nhận trước khi xóa

- ✅ **Thống kê:**
  - Card tổng số người dùng
  - Card số lượng admin

---

### 🎫 **7. Quản Lý Mã Giảm Giá (Vouchers)**

#### **Vouchers Page**
- ✅ **Xem danh sách:**
  - Hiển thị tất cả mã giảm giá trong bảng
  - Thông tin: Code, Tên, Loại (PERCENT/AMOUNT), Giá trị, Min Order, Đã dùng/Giới hạn, Trạng thái

- ✅ **Thêm mã giảm giá:**
  - Form nhập: Code, Tên, Mô tả, Loại (PERCENT/FIXED), Giá trị, Min Purchase Amount, Max Discount Amount, Usage Limit, Start Date, End Date, Trạng thái
  - Validation đầy đủ
  - Date picker cho ngày bắt đầu/kết thúc

- ✅ **Chỉnh sửa mã giảm giá:**
  - Sửa tất cả thông tin mã giảm giá
  - Xử lý date parsing an toàn (timestamp/number)
  - Map AMOUNT ↔ FIXED cho UI

- ✅ **Xóa mã giảm giá:**
  - Xác nhận trước khi xóa

- ✅ **Thống kê:**
  - Card tổng số mã giảm giá
  - Card mã giảm giá đang hoạt động

- ✅ **Tính năng đặc biệt:**
  - Xử lý date từ backend (timestamp) sang date picker
  - Map type AMOUNT (backend) ↔ FIXED (UI)
  - Error handling cho invalid dates

---

### 🖼️ **8. Quản Lý Banner (Banners)**

#### **Banners Page**
- ⚠️ **Đang phát triển:**
  - Placeholder page
  - Sẽ có CRUD đầy đủ trong tương lai

---

## 🔧 **BACKEND API - CÁC ENDPOINT**

### 🔐 **Authentication (`/api/auth`)**
- ✅ `POST /auth/login` - Đăng nhập
- ✅ `POST /auth/register` - Đăng ký
- ✅ `GET /auth/profile` - Lấy thông tin profile (JWT required)

### 👥 **Users (`/api/users`)**
- ✅ `POST /users` - Tạo user mới
- ✅ `GET /users` - Lấy danh sách users (Admin only)
- ✅ `GET /users/:id` - Lấy thông tin user
- ✅ `PATCH /users/:id` - Cập nhật user
- ✅ `DELETE /users/:id` - Xóa user

### 📦 **Products (`/api/products`)**
- ✅ `POST /products` - Tạo sản phẩm mới (Admin only)
- ✅ `GET /products` - Lấy danh sách sản phẩm (Admin only)
- ✅ `GET /products/:id` - Lấy chi tiết sản phẩm (Admin only)
- ✅ `PATCH /products/:id` - Cập nhật sản phẩm (Admin only)
- ✅ `DELETE /products/:id` - Xóa sản phẩm (Admin only)

### 📦 **Public Products (`/api/public/products`)**
- ✅ `GET /public/products` - Lấy danh sách sản phẩm (Public, không cần auth)
- ✅ `GET /public/products?categoryId=X` - Lọc sản phẩm theo danh mục
- ✅ `GET /public/products/:id` - Lấy chi tiết sản phẩm (Public)

### 🏷️ **Categories (`/api/categories`)**
- ✅ `POST /categories` - Tạo danh mục mới (Admin only)
- ✅ `GET /categories` - Lấy danh sách danh mục (Admin only)
- ✅ `GET /categories/:id` - Lấy chi tiết danh mục (Admin only)
- ✅ `PATCH /categories/:id` - Cập nhật danh mục (Admin only)
- ✅ `DELETE /categories/:id` - Xóa danh mục (Admin only)

### 🏷️ **Public Categories (`/api/public/categories`)**
- ✅ `GET /public/categories` - Lấy danh sách danh mục (Public)
- ✅ `GET /public/categories/:id` - Lấy chi tiết danh mục (Public)

### 🛒 **Orders (`/api/orders`)**
- ✅ `POST /orders` - Tạo đơn hàng mới
- ✅ `GET /orders` - Lấy danh sách đơn hàng (Admin: tất cả, User: của mình)
- ✅ `GET /orders/:id` - Lấy chi tiết đơn hàng
- ✅ `PATCH /orders/:id` - Cập nhật đơn hàng
- ✅ `PATCH /orders/:id/status` - Cập nhật trạng thái đơn hàng
- ✅ `DELETE /orders/:id` - Xóa đơn hàng (Admin only)

### 🎫 **Vouchers (`/api/vouchers`)**
- ✅ `POST /vouchers` - Tạo mã giảm giá mới (Admin only)
- ✅ `GET /vouchers` - Lấy danh sách mã giảm giá
- ✅ `GET /vouchers/code/:code` - Lấy mã giảm giá theo code
- ✅ `GET /vouchers/:id` - Lấy chi tiết mã giảm giá
- ✅ `PATCH /vouchers/:id` - Cập nhật mã giảm giá (Admin only)
- ✅ `DELETE /vouchers/:id` - Xóa mã giảm giá (Admin only)

### 🖼️ **Banners (`/api/banners`)**
- ✅ `POST /banners` - Tạo banner mới (Admin only)
- ✅ `GET /banners` - Lấy danh sách banner
- ✅ `GET /banners/:id` - Lấy chi tiết banner
- ✅ `PATCH /banners/:id` - Cập nhật banner (Admin only)
- ✅ `DELETE /banners/:id` - Xóa banner (Admin only)

---

## 🗄️ **DATABASE - CẤU TRÚC DỮ LIỆU**

### **Các bảng chính:**
1. ✅ **users** - Người dùng
2. ✅ **categories** - Danh mục sản phẩm
3. ✅ **products** - Sản phẩm
4. ✅ **orders** - Đơn hàng
5. ✅ **order_items** - Chi tiết đơn hàng
6. ✅ **vouchers** - Mã giảm giá
7. ✅ **banners** - Banner quảng cáo

### **File database hoàn chỉnh:**
- ✅ `database_complete.sql` - Script tạo database từ đầu đến cuối với dữ liệu mẫu

---

## 🎨 **UI/UX FEATURES**

### **Admin Panel:**
- ✅ Material-UI (MUI) components
- ✅ Responsive design
- ✅ Gradient cards với hover effects
- ✅ Charts và visualizations (Dashboard)
- ✅ Loading states và error handling
- ✅ Snackbar notifications
- ✅ Dialog forms cho CRUD operations
- ✅ Color-coded status chips
- ✅ Icons và tooltips

### **Android App:**
- ✅ Material Design
- ✅ Edge-to-edge layout
- ✅ Smooth animations
- ✅ Pull-to-refresh
- ✅ Image loading với Glide
- ✅ RecyclerView với adapters
- ✅ Navigation bar
- ✅ Badge notifications

---

## 🔒 **BẢO MẬT**

- ✅ JWT Authentication
- ✅ Protected Routes (Admin Panel)
- ✅ Role-based access control (Admin/User)
- ✅ Password hashing (bcrypt)
- ✅ Token storage (SharedPreferences - Android, localStorage - Web)

---

## 📱 **TÍCH HỢP API**

### **Android App:**
- ✅ Retrofit2 cho API calls
- ✅ Gson cho JSON parsing
- ✅ OkHttp với logging interceptor
- ✅ Coroutines cho async operations
- ✅ Error handling và retry logic

### **Admin Panel:**
- ✅ Axios cho API calls
- ✅ React Query cho data fetching và caching
- ✅ Auto-refresh và optimistic updates

---

## 📊 **THỐNG KÊ & BÁO CÁO**

### **Dashboard:**
- ✅ Tổng số users, orders, products, vouchers
- ✅ Doanh thu 7 ngày gần nhất
- ✅ Phân bổ trạng thái đơn hàng
- ✅ Top sản phẩm bán chạy
- ✅ Đơn hàng gần đây

---

## 🚀 **DEPLOYMENT**

- ✅ Backend: Railway.app
- ✅ Database: MySQL (Railway)
- ✅ Frontend: Vite + React
- ✅ Android: APK build

---

## 📝 **GHI CHÚ**

- ✅ Tất cả chức năng đã được test và hoạt động
- ✅ Database đã được migrate từ Firebase sang MySQL
- ✅ API endpoints đã được tối ưu và có error handling
- ✅ UI/UX đã được cải thiện với modern design
- ✅ Code đã được commit và push lên GitHub

---

**Cập nhật lần cuối:** 2024-12-04

