# 🔥 Cấu Hình Firebase Admin SDK

## 📋 Bước 1: Tạo Firebase Service Account

1. Vào [Firebase Console](https://console.firebase.google.com/)
2. Chọn project của bạn
3. Vào **Project Settings** (⚙️) → **Service Accounts**
4. Click **Generate New Private Key**
5. Download file JSON

## 📋 Bước 2: Lấy Thông Tin Từ JSON

Mở file JSON vừa download, bạn sẽ thấy:

```json
{
  "type": "service_account",
  "project_id": "your-project-id",
  "private_key_id": "your-private-key-id",
  "private_key": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-xxxxx@your-project.iam.gserviceaccount.com",
  "client_id": "your-client-id",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/..."
}
```

## 📋 Bước 3: Lấy Database URL

1. Vào Firebase Console → **Realtime Database**
2. Copy **Database URL** (ví dụ: `https://your-project-default-rtdb.firebaseio.com`)

## 📋 Bước 4: Thêm Environment Variables

Thêm vào `admin-web/backend/.env`:

```env
# Firebase Admin SDK
FIREBASE_PROJECT_ID=your-project-id
FIREBASE_PRIVATE_KEY_ID=your-private-key-id
FIREBASE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n"
FIREBASE_CLIENT_EMAIL=firebase-adminsdk-xxxxx@your-project.iam.gserviceaccount.com
FIREBASE_CLIENT_ID=your-client-id
FIREBASE_CLIENT_X509_CERT_URL=https://www.googleapis.com/robot/v1/metadata/x509/...
FIREBASE_DATABASE_URL=https://your-project-default-rtdb.firebaseio.com
```

## 📋 Bước 5: Thêm Vào Railway (Nếu Deploy)

1. Vào Railway → Service **LTTBDD** → **Variables**
2. Thêm tất cả các biến trên
3. **Lưu ý**: `FIREBASE_PRIVATE_KEY` phải giữ nguyên format với `\n`

## ✅ Kiểm Tra

Sau khi cấu hình, khi bạn:
- Tạo/Update/Delete Product → Tự động sync lên Firebase
- Tạo/Update/Delete Category → Tự động sync lên Firebase

App Android sẽ tự động nhận update real-time!

## ⚠️ Lưu Ý

- Nếu không cấu hình Firebase, backend vẫn hoạt động bình thường (chỉ không sync Firebase)
- Firebase sync là optional, không ảnh hưởng đến MySQL database
- Logs sẽ hiển thị warning nếu Firebase không available

