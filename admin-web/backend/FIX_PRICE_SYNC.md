# Đã sửa: Giá sản phẩm không cập nhật trên Firebase và App

## Vấn đề
Khi thay đổi giá trên admin panel, Firebase và app Android không cập nhật giá mới.

## Nguyên nhân

### 1. App Android không lắng nghe thay đổi realtime
- `loadItemCategory()` sử dụng `addListenerForSingleValueEvent` → chỉ đọc một lần
- Khi giá thay đổi trên Firebase, app không tự động cập nhật
- ✅ **Đã sửa**: Đổi sang `addValueEventListener` để lắng nghe thay đổi realtime

### 2. Giá có thể bị format sai
- MySQL lưu DECIMAL(10,2) nhưng có thể bị convert thành string
- ✅ **Đã sửa**: Đảm bảo giá luôn là number khi sync lên Firebase

## Đã sửa

### 1. Sửa MainRepository.kt (Android App)

**Trước:**
```kotlin
query.addListenerForSingleValueEvent(...)  // Chỉ đọc một lần
```

**Sau:**
```kotlin
query.addValueEventListener(...)  // Lắng nghe thay đổi realtime
```

### 2. Cải thiện Firebase Service

- Đảm bảo giá luôn là number (không phải string)
- Thêm logging chi tiết khi sync giá

## Cách test

### Bước 1: Build lại app Android

1. Build lại app với code mới
2. Cài đặt app mới lên thiết bị

### Bước 2: Test update giá

1. Vào admin panel → Products
2. Edit một product (ví dụ: Red Eye Americano - ID 27)
3. Thay đổi giá (ví dụ: 4.5 → 5.5)
4. Save

### Bước 3: Kiểm tra logs trên Railway

Xem logs để tìm:
```
[ProductsService] Syncing updated product 27 to Firebase (price: 5.5)...
[FirebaseService] Product 27 synced to Firebase Items - Price: 5.5, Title: Red Eye Americano
[ProductsService] Product 27 synced to Firebase successfully
```

### Bước 4: Kiểm tra Firebase Console

1. Vào https://console.firebase.google.com/
2. Chọn project: `coffeeshop1-9d0f0`
3. Vào **Realtime Database**
4. Kiểm tra `Items/27`:
   - `price` = 5.5 (number, không phải string) ✅

### Bước 5: Kiểm tra app Android

1. Mở app Android (đã build lại với code mới)
2. Vào màn hình hiển thị sản phẩm (Category hoặc Home)
3. Tìm sản phẩm vừa update
4. **Giá sẽ tự động cập nhật** (không cần đóng/mở lại app) ✅

## Lưu ý

1. **App cần build lại** với code mới để có realtime listener
2. **Giá trong Firebase phải là number** không phải string
3. **Realtime listener** sẽ tự động cập nhật khi giá thay đổi trên Firebase
4. Nếu vẫn không cập nhật, kiểm tra:
   - Firebase credentials trên Railway
   - Logs trên Railway để xem có lỗi sync không
   - Firebase Console để xem giá có được update không

## Debug

Xem file `FIREBASE_SYNC_DEBUG.md` để debug chi tiết hơn.

