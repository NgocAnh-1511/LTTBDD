# Ứng dụng Cửa hàng Cà phê - Coffee Shop Android App ☕  
**Môn học**: Lập trình Di động – Nhóm 17  
**Trường**: Đại học Giao thông Vận tải TP.HCM – Viện Công nghệ Thông tin & Điện, Điện tử  
**Thời gian hoàn thành**: 30/11/2025  

### Demo (screenshots)
![Demo](screenshots/demo.gif)  
*(Các ảnh chụp màn hình chi tiết xem trong thư mục `/screenshots`)*
<br/>
<img width="850" height="542" alt="image" src="https://github.com/user-attachments/assets/58918f88-6dc0-4522-8cb6-6dcc15790642" />


### Kiến trúc hệ thống (MVVM + Repository Pattern)
![MVVM Architecture](docs/architecture/mvvm_architecture.png)
<br/>
<img width="561" height="538" alt="image" src="https://github.com/user-attachments/assets/ac50b504-0a41-465b-a1b0-ea41a379ee27" />

### Sơ đồ Use Case (Luồng nghiệp vụ chính)
![Use Case Diagram](docs/usecase/usecase_diagram.png)
<br/>
<img width="850" height="524" alt="image" src="https://github.com/user-attachments/assets/a3cfdaf4-7bca-4ba6-8c1a-7a888ee333c6" />

### Mô tả dự án
Ứng dụng đặt đồ uống cà phê dành cho khách hàng (Customer Mobile App), được xây dựng hoàn toàn bằng **Kotlin** theo kiến trúc **MVVM**, sử dụng giao diện XML truyền thống và các thành phần Android Jetpack hiện đại.

Ứng dụng cho phép:
- Xem menu đồ uống (hình ảnh, giá, mô tả)
- Tùy chỉnh món (size, đá, đường, topping…)
- Quản lý giỏ hàng & tính tiền tự động
- Lưu trữ offline bằng MySQL Database
- Đăng nhập / Đăng ký
- Đặt hàng (COD) & xem lịch sử đơn hàng

### Tính năng chính
| Tính năng                | Trạng thái |
|--------------------------|------------|
| Đăng nhập / Đăng ký       | Hoàn thành |
| Hiển thị Menu (RecyclerView + Glide) | Hoàn thành |
| Chi tiết & tùy chỉnh món   | Hoàn thành |
| Giỏ hàng (Room + LiveData) | Hoàn thành |
| Đặt hàng COD              | Hoàn thành |
| Lịch sử đơn hàng          | Hoàn thành |


### Công nghệ & Thư viện
- **Ngôn ngữ**: Kotlin
- **IDE**: Android Studio (2025.x)
- **Kiến trúc**: MVVM
- **UI**: XML Layouts + View Binding + ConstraintLayout
- **Android Jetpack**:
  - ViewModel + LiveData
  - MySQL Database
  - Navigation Component
- **Hình ảnh**: Glide
- **Bất đồng bộ**: Kotlin Coroutines
- **Database mẫu**: `CoffeeShopDB.db` (trong `/assets`)

### Yêu cầu hệ thống
- Android 7.0 (API 24) trở lên
- Android Studio Iguana/Jellyfish hoặc mới hơn
- JDK 17

### Hướng dẫn cài đặt & chạy dự án
```bash
# 1. Clone repository
git clone https://github.com/NgocAnh-1511/LTTBDD.git
# 2. Mở bằng Android Studio → Open project

# 3. Chờ Gradle sync xong (5-10 phút lần đầu)

