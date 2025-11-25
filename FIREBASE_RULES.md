# Firebase Realtime Database Rules

## Rules hoàn chỉnh cho ứng dụng Coffee Shop

Copy và paste rules này vào Firebase Console > Realtime Database > Rules:

```json
{
  "rules": {
    "Banner": {
      ".read": true,
      ".write": false
    },
    "Category": {
      ".read": true,
      ".write": false
    },
    "Popular": {
      ".read": true,
      ".write": false
    },
    "Items": {
      ".read": true,
      ".write": false
    },
    "News": {
      ".read": true,
      ".write": false
    },
    "users": {
      ".read": true,
      ".write": true,
      "$userId": {
        ".read": true,
        ".write": true,
        ".indexOn": ["phoneNumber"]
      }
    },
    "carts": {
      "$userId": {
        ".read": true,
        ".write": true
      }
    },
    "orders": {
      "$userId": {
        ".read": true,
        ".write": true
      }
    },
    "addresses": {
      "$userId": {
        ".read": true,
        ".write": true
      }
    },
    "wishlist": {
      "$userId": {
        ".read": true,
        ".write": true
      }
    }
  }
}
```

## Giải thích chi tiết

### 1. Dữ liệu công khai (Public Data)
- **Banner, Category, Popular, Items, News**: 
  - `.read: true` - Ai cũng có thể đọc (không cần đăng nhập)
  - `.write: false` - Không ai được ghi từ app (chỉ admin qua Firebase Console)

### 2. Dữ liệu người dùng (User Data)

#### **users** (Thông tin người dùng)
- `.read: true` - Cho phép đọc để tìm user khi đăng nhập
- `.write: true` - Cho phép ghi (tạo/cập nhật user)
- `.indexOn: ["phoneNumber"]` - **QUAN TRỌNG**: Index cho phoneNumber để query nhanh khi đăng nhập

#### **carts** (Giỏ hàng)
- `.read: true` - Cho phép đọc giỏ hàng
- `.write: true` - Cho phép ghi giỏ hàng

#### **orders** (Đơn hàng)
- `.read: true` - Cho phép đọc đơn hàng
- `.write: true` - Cho phép ghi đơn hàng

#### **addresses** (Địa chỉ)
- `.read: true` - Cho phép đọc địa chỉ
- `.write: true` - Cho phép ghi địa chỉ

#### **wishlist** (Danh sách yêu thích)
- `.read: true` - Cho phép đọc wishlist
- `.write: true` - Cho phép ghi wishlist

## Cách áp dụng

1. Mở Firebase Console: https://console.firebase.google.com
2. Chọn project của bạn (CoffeeShop1)
3. Vào **Realtime Database** > **Rules**
4. Copy toàn bộ rules ở trên và paste vào
5. Click **Publish**

## Lưu ý quan trọng

### ✅ Ưu điểm của rules này:
- **Đơn giản**: Không có validation phức tạp, dễ hiểu và không lỗi
- **Hoạt động ngay**: Public data (Banner, Category, Items) load được trên trang chủ
- **Index cho phoneNumber**: Có `.indexOn: ["phoneNumber"]` giúp query nhanh khi đăng nhập
- **Linh hoạt**: Cho phép đọc/ghi dữ liệu user để đồng bộ SQLite với Firebase

### ⚠️ Lưu ý:
- **Không dùng Firebase Auth**: App hiện tại không dùng Firebase Authentication, nên rules dùng `true` để cho phép đọc/ghi
- **Bảo mật**: Rules này cho phép mọi người đọc/ghi dữ liệu user (phù hợp với app không dùng Auth)
- **Index cho phoneNumber**: Đã thêm `.indexOn: ["phoneNumber"]` trong `users`, giúp query `orderByChild("phoneNumber")` nhanh hơn

### 🔧 Troubleshooting:
- Nếu query `orderByChild("phoneNumber")` báo lỗi "index not found":
  1. Đảm bảo đã publish rules có `.indexOn: ["phoneNumber"]`
  2. Đợi vài phút để Firebase tạo index
  3. Hoặc code đã có fallback method để lấy tất cả users và filter trong code

## Rules cho Production (nếu dùng Firebase Auth)

Nếu sau này tích hợp Firebase Authentication, có thể dùng rules này:

```json
{
  "rules": {
    "users": {
      "$userId": {
        ".read": "auth != null && $userId == auth.uid",
        ".write": "auth != null && $userId == auth.uid"
      }
    },
    "carts": {
      "$userId": {
        ".read": "auth != null && $userId == auth.uid",
        ".write": "auth != null && $userId == auth.uid"
      }
    }
    // ... tương tự cho orders, addresses, wishlist
  }
}
```

