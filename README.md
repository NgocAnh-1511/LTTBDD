# Ứng dụng Cửa hàng Cà phê - Coffee Shop Android App ☕

**Môn học**: Lập trình Di động – Nhóm 18  
**Trường**: Đại học Giao thông Vận tải TP.HCM – Viện Công nghệ Thông tin & Điện, Điện tử  
**Thời gian hoàn thành**: 30/11/2025

### Demo (screenshots)
![Demo](screenshots/demo.gif)  
*(Các ảnh chụp màn hình chi tiết xem trong thư mục `/screenshots`)*

<img width="1271" height="900" alt="image" src="https://github.com/user-attachments/assets/5ba7d339-4181-41f3-af26-9c579491bf17" />

### Kiến trúc hệ thống (MVVM + Repository Pattern)
![MVVM Architecture](docs/architecture/mvvm_architecture.png)

<img width="561" height="538" alt="image" src="https://github.com/user-attachments/assets/ac50b504-0a41-465b-a1b0-ea41a379ee27" />

### Sơ đồ Use Case (Luồng nghiệp vụ chính)
![Use Case Diagram](docs/usecase/usecase_diagram.png)

<img width="945" height="849" alt="image" src="https://github.com/user-attachments/assets/fd1cd9cc-5990-4120-98d0-ebf95dbba5f2" />

### Mô tả dự án
Ứng dụng đặt đồ uống cà phê dành cho khách hàng (Customer Mobile App), được xây dựng hoàn toàn bằng **Kotlin** theo kiến trúc **MVVM**, sử dụng giao diện XML truyền thống và các thành phần Android Jetpack hiện đại.

Ứng dụng cho phép:
- Xem menu đồ uống (hình ảnh, giá, mô tả)
- Tùy chỉnh món (size, đá, đường, topping…)
- Quản lý giỏ hàng & tính tiền tự động
- Lưu trữ dữ liệu offline bằng **SQLite (qua Room Database)**
- Đăng nhập / Đăng ký tài khoản
- Đặt hàng (COD) & xem lịch sử đơn hàng

### Tính năng chính
| Tính năng                     | Trạng thái  |
|-------------------------------|-------------|
| Đăng nhập / Đăng ký           | Hoàn thành  |
| Hiển thị Menu (RecyclerView + Glide) | Hoàn thành  |
| Chi tiết & tùy chỉnh món      | Hoàn thành  |
| Giỏ hàng (SQLite + LiveData)  | Hoàn thành  |
| Đặt hàng COD                  | Hoàn thành  |
| Lịch sử đơn hàng              | Hoàn thành  |
| Offline-first (SQLite Database) | Hoàn thành  |

### Công nghệ & Thư viện sử dụng
- **Ngôn ngữ**: Kotlin
- **IDE**: Android Studio (2025.x)
- **Kiến trúc**: MVVM + Repository Pattern
- **UI**: XML Layouts + View Binding + ConstraintLayout
- **Android Jetpack**:
  - ViewModel + LiveData
  - Room (SQLite database)
  - Navigation Component
- **Tải hình ảnh**: Glide
- **Lập trình bất đồng bộ**: Kotlin Coroutines + Flow
- **Cơ sở dữ liệu mẫu**: `CoffeeShopDB.db` (đặt trong thư mục `/assets`)

### Yêu cầu hệ thống
- Android 7.0 (API 24) trở lên
- Android Studio Iguana | Jellyfish | Koala hoặc mới hơn
- JDK 17

### Hướng dẫn cài đặt & chạy dự án
```bash
# 1. Clone repository
git clone https://github.com/NgocAnh-1511/LTTBDD.git

# 2. Mở project bằng Android Studio → Open an existing project

# 3. Chờ Gradle sync hoàn tất (5-10 phút lần đầu)

# 4. Run ứng dụng trên emulator hoặc thiết bị thật
