# Hướng dẫn Debug Firebase Sync

## Vấn đề: Thay đổi giá trên admin nhưng Firebase và app không cập nhật

### Nguyên nhân đã được sửa:

1. **Cấu trúc dữ liệu không khớp**: 
   - Firebase service đang sync `pic` (string) 
   - App Android đọc `picUrl` (array)
   - ✅ **Đã sửa**: Firebase service giờ sync `picUrl` là array

### Các bước kiểm tra:

#### 1. Kiểm tra Firebase có được khởi tạo không

Xem logs trên Railway:
- Vào Railway Dashboard → Chọn service backend → Tab **Deployments** → Click vào deployment mới nhất → Xem **Logs**
- Tìm dòng: `Firebase Admin initialized successfully`
- Nếu thấy: `Firebase credentials not found. Firebase sync will be disabled.` → Cần thêm Firebase credentials vào Railway Variables

#### 2. Kiểm tra Firebase credentials trên Railway

Vào Railway Dashboard → Service backend → Tab **Variables**, đảm bảo có:
- `FIREBASE_PROJECT_ID`
- `FIREBASE_PRIVATE_KEY` (với `\n` được escape đúng)
- `FIREBASE_CLIENT_EMAIL`
- `FIREBASE_DATABASE_URL`

#### 3. Kiểm tra logs khi update product

Khi update product trên admin panel, xem logs trên Railway:
- Tìm dòng: `Product {id} synced to Firebase Items`
- Nếu thấy: `Failed to sync product to Firebase` → Có lỗi xảy ra, xem chi tiết lỗi

#### 4. Kiểm tra dữ liệu trên Firebase Console

1. Vào Firebase Console: https://console.firebase.google.com/
2. Chọn project: `coffeeshop1-9d0f0`
3. Vào **Realtime Database**
4. Kiểm tra node `Items/{productId}`:
   - `picUrl` phải là array: `["https://..."]` (không phải string)
   - `price` phải đúng giá mới
   - `title` phải đúng tên sản phẩm

#### 5. Kiểm tra app Android có đọc đúng không

App Android sử dụng `ValueEventListener` để lắng nghe thay đổi realtime từ Firebase. Nếu dữ liệu trên Firebase đúng nhưng app không cập nhật:

1. **Đóng và mở lại app** (để reload data)
2. **Kiểm tra Logcat** trong Android Studio:
   - Filter: `MainRepository`
   - Tìm log: `Loaded X popular items` hoặc `Error loading Popular`
3. **Kiểm tra kết nối Firebase** trong app:
   - Đảm bảo app có quyền truy cập internet
   - Kiểm tra Firebase rules cho phép read

### Cách test thủ công:

#### Test 1: Update một product và kiểm tra Firebase

1. Vào admin panel → Products
2. Edit một product (ví dụ: Red Eye Americano)
3. Thay đổi giá (ví dụ: từ 4.5 → 5.5)
4. Save
5. Vào Firebase Console → Realtime Database → `Items/27` (hoặc ID của product)
6. Kiểm tra `price` có = 5.5 không
7. Kiểm tra `picUrl` có là array không

#### Test 2: Kiểm tra app Android

1. Mở app Android
2. Vào màn hình hiển thị sản phẩm (Home hoặc Category)
3. Tìm sản phẩm vừa update
4. Kiểm tra giá có cập nhật không
5. Nếu chưa, đóng và mở lại app

### Sync lại tất cả dữ liệu (nếu cần):

Nếu dữ liệu trên Firebase không đúng, bạn có thể sync lại tất cả:

1. **Qua API** (nếu có endpoint):
   ```bash
   POST /api/firebase/sync-all
   Authorization: Bearer {token}
   ```

2. **Hoặc update từng product** trên admin panel để trigger sync

### Lưu ý quan trọng:

1. **picUrl phải là array**: `["https://..."]` không phải `"https://..."`
2. **Firebase credentials phải đúng** trên Railway
3. **App Android cần reload** để nhận dữ liệu mới (hoặc đợi realtime listener update)
4. **Kiểm tra Firebase Rules** cho phép read/write

### Nếu vẫn không hoạt động:

1. Kiểm tra logs trên Railway để xem có lỗi gì
2. Kiểm tra Firebase Console để xem dữ liệu có được sync không
3. Kiểm tra Firebase Rules có cho phép write từ backend không
4. Test với một product mới để xem sync có hoạt động không

