# Ứng dụng Cửa hàng Cà phê - Coffee Shop Android App ☕  
**Môn học**: Lập trình Di động – Nhóm 17  
**Trường**: Đại học Giao thông Vận tải TP.HCM – Viện Công nghệ Thông tin & Điện, Điện tử  
**Thời gian hoàn thành**: 30/11/2025  

### Demo (screenshots)
![Demo](screenshots/demo.gif)  
*(Các ảnh chụp màn hình chi tiết xem trong thư mục `/screenshots`)*
<br/> 
<img width="633" height="631" alt="image" src="https://github.com/user-attachments/assets/2eb0a374-2d1b-49f2-9462-7d7d552d6841" />
<br/> 
<img width="945" height="511" alt="image" src="https://github.com/user-attachments/assets/ae8de482-7f60-42dc-80bc-d6ab3b9aae06" />

### Kiến trúc hệ thống (MVVM + Repository Pattern)
![MVVM Architecture](docs/architecture/mvvm_architecture.png)
<br/>
<img width="561" height="538" alt="image" src="https://github.com/user-attachments/assets/ac50b504-0a41-465b-a1b0-ea41a379ee27" />

### Sơ đồ Use Case (Luồng nghiệp vụ chính)
![Use Case Diagram](docs/usecase/usecase_diagram.png)
<br/>
<img width="945" height="849" alt="image" src="https://github.com/user-attachments/assets/fd1cd9cc-5990-4120-98d0-ebf95dbba5f2" />


### Mô tả dự án
Ứng dụng đặt đồ uống cà phê dành cho khách hàng (Customer Mobile App), được xây dựng hoàn toàn bằng **Kotlin** theo kiến trúc **MVVM**, sử dụng giao diện XML truyền thống và các thành phần Android Jetpack hiện đại.

Ứng dụng cho phép:
- Xem menu đồ uống (hình ảnh, giá, mô tả)
- Tùy chỉnh món (size, đá, đường, topping…)
- Quản lý giỏ hàng & tính tiền tự động
- Lưu trữ offline bằng Room Database
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
| Offline-first (Room DB)   | Hoàn thành |

### Công nghệ & Thư viện
- **Ngôn ngữ**: Kotlin
- **IDE**: Android Studio (2025.x)
- **Kiến trúc**: MVVM
- **UI**: XML Layouts + View Binding + ConstraintLayout
- **Android Jetpack**:
  - ViewModel + LiveData
  - Room Database
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
git clone https://github.com/username/CoffeeShop-Android-App.git

# 2. Mở bằng Android Studio → Open project

# 3. Chờ Gradle sync xong (5-10 phút lần đầu)
