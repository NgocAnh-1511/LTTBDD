# Đã sửa: Firebase Sync không cập nhật giá

## Vấn đề
Khi thay đổi giá trên admin panel, Firebase và app Android không cập nhật.

## Nguyên nhân
**Cấu trúc dữ liệu không khớp**:
- Firebase service sync `pic` (string)
- App Android đọc `picUrl` (ArrayList<String>)
- Dữ liệu không khớp → App không đọc được

## Đã sửa

### 1. Sửa cấu trúc dữ liệu trong `firebase.service.ts`

**Trước:**
```typescript
pic: product.imageUrl || product.pic || '',  // string
```

**Sau:**
```typescript
picUrl: picUrlArray,  // Array format: ["https://..."]
```

### 2. Cải thiện logging

Thêm logging chi tiết trong `products.service.ts`:
- Log khi bắt đầu sync
- Log khi sync thành công
- Log lỗi chi tiết nếu có

## Cách kiểm tra

### Bước 1: Deploy lại backend lên Railway

1. Commit và push code mới
2. Railway sẽ tự động deploy
3. Kiểm tra logs trên Railway để xem Firebase có được khởi tạo không

### Bước 2: Test update product

1. Vào admin panel → Products
2. Edit một product (ví dụ: Red Eye Americano - ID 27)
3. Thay đổi giá (ví dụ: 4.5 → 5.5)
4. Save

### Bước 3: Kiểm tra logs trên Railway

Xem logs để tìm:
```
[ProductsService] Syncing updated product 27 to Firebase (price: 5.5)...
[FirebaseService] Product 27 synced to Firebase Items
[ProductsService] Product 27 synced to Firebase successfully
```

### Bước 4: Kiểm tra Firebase Console

1. Vào https://console.firebase.google.com/
2. Chọn project: `coffeeshop1-9d0f0`
3. Vào **Realtime Database**
4. Kiểm tra `Items/27`:
   - `price` = 5.5 ✅
   - `picUrl` = `["https://..."]` (array, không phải string) ✅

### Bước 5: Kiểm tra app Android

1. Mở app Android
2. Đóng và mở lại app (để reload data từ Firebase)
3. Tìm sản phẩm vừa update
4. Kiểm tra giá có = 5.5 không

## Lưu ý

1. **App Android cần reload** để nhận dữ liệu mới (hoặc đợi realtime listener tự động update)
2. **Firebase credentials phải đúng** trên Railway Variables
3. **picUrl phải là array** trong Firebase: `["https://..."]` không phải `"https://..."`

## Nếu vẫn không hoạt động

Xem file `FIREBASE_SYNC_DEBUG.md` để debug chi tiết hơn.

