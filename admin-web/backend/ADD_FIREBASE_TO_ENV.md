# 🔥 Thêm Firebase Config Vào .env

## 📋 Cách 1: Thêm Thủ Công (Khuyến Nghị)

Mở file `admin-web/backend/.env` và thêm vào cuối file:

```env
# Firebase Admin SDK
FIREBASE_PROJECT_ID=coffeeshop1-9d0f0
FIREBASE_PRIVATE_KEY_ID=a8b37dfb2074036629ec03acde2d1be9c17b32c4
FIREBASE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDycYolc0RUdzvp\nau0nzSajq9sZVuHzHrhT6bxekz+Pu9FaULavR4Lo7tIo+rS+MUF8iQa2GNKDCcG8\nL0oVm79EH1UYvx7zuVDlVj+aGsjND7DfSp2ryPDELGL8k0lXGkzwd8JXnEvDKYzy\nsrt6iYQIjUmURuuhp7hVf5/JrcdRpdm7tspEBc41/i7qdIMbCPRejk0ZUZL4TZCj\nZotSJjof5S7jcFft+y04GWHqETA3XIzCBeD/1pcTrD/wtQL1RyT/WvKjJKGOlvSM\n2y20ivuWEwBRcBzL5m095nnQy7Ha6t3fZXA+QVDEhvOAiVMsPAV8Qwdl6fmtYcCk\nZiqZ1PGPAgMBAAECggEAKF01Of6itzSyFwQUNOayfVh3nhSO13qGB4PS4U9pqzAC\nxHh2BIuxj2oc4ka56y+/LpE6ZVHk+9JsMhJmJrUrVxenrtA5woEtfJPrUJMPWxi2\n2A46XvyRnU+jMVhXmGFgGD8QGPmuzBuHBGyJIHcfB4oiYHCdpWiLnOx7zsjty46w\nKE8PaNfw+T48Xo7z+q08gZpwAiO/jYPPOWlvULw1q7ABsAmU91y3Z48e7QX2zXN5\ng6Swvq/DiGiLcz7MA7+4nu2qxjw3tzfeUXsOXZ9mbr9Dvku7wrGdeJrA02Scx1Wj\n6A9stdREi2csq1WZJ9a3E6sYi/Or4tKpUZes8zvHrQKBgQD6iA37kAPDxWsiP4SA\nzQ1urb5+hz0zQJYDrSsggGjQWf6AOS/X6hkFFKds459kcZDxsrvD/yEjxbl5iqNo\nW9Mn/08oJV3RHv76UvCRCDuGcwvVg4YqInaeWC7tfLtX8JQ7eIDLIZDaWOZgSuOk\nBvlQMNrq4HK+yz1S4BbvKB01PQKBgQD3vEpT7X5uEFHZa1vwK1DimdDyNTjOYfdD\nsyhiU243/hnELsma8npekHYwQYtitvzzkW/24Cug1zBM1I/nFMrRL1bPfrYdOq01\nutwesEfuxu4pHxj3/0+zQPLKtwBjlUMwtwxDKsRjjIQgG5IM6el8wN277yVnzUiF\npTjjKngmuwKBgE1RpA8ZWTAYQ4IkXD5sClM4J+xgzJB+xE2KKh8zwiVbQOJNw+g9\nBU++GhXPAig7rGILqUtJ9+5UXXOXR+szO63Ca6mWLqgViteytS/tfIF4DBTvLH7g\nd/9Sc9kzf5YaV4UrIYSPbWcVvLPHKQ48YMFQ8p8+eetMSjbDlhBlLu55AoGAdI0o\nZv34Gm4hI+onkgG/By0Yqasyc56Rh09Vs5TO6RbqJmtgvD5SJgVPJXrgyeuPbERD\naeGjhQNYnPzwSuZ0WSAtdmeF5JCDhyDHiMSkwwjiaYRQ6fQ7Gfl3gLnCPBewSc4E\nveD0DmXAj56JAvxVtjTC3TUw4voooX/PGMkVSBsCgYEA4A8Z/V4KgS0M9F6fMtKC\nCl+JlZMjtRw1+OCKi53oQRfbrys11pH/Ym8nyLlXTNJZjMuq4wDkbueS7oF8wWII\nOz2P7uAY2vFEKt6hY9vojZ4qhVw9sZvbG+ggNreaLoND4vpJ+xgNZxsTYdnaKgyZ\nr8/LApp+7A6C/wVlBi5b2q0=\n-----END PRIVATE KEY-----\n"
FIREBASE_CLIENT_EMAIL=firebase-adminsdk-fbsvc@coffeeshop1-9d0f0.iam.gserviceaccount.com
FIREBASE_CLIENT_ID=105118494699544004864
FIREBASE_CLIENT_X509_CERT_URL=https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40coffeeshop1-9d0f0.iam.gserviceaccount.com
FIREBASE_DATABASE_URL=https://coffeeshop1-9d0f0-default-rtdb.firebaseio.com
```

## 📋 Cách 2: Copy Từ File Này

1. Mở file này (`ADD_FIREBASE_TO_ENV.md`)
2. Copy toàn bộ block code trên (từ `# Firebase Admin SDK` đến hết)
3. Paste vào cuối file `.env`

## ✅ Sau Khi Thêm

Chạy test để kiểm tra:

```bash
cd admin-web/backend
npm run test:firebase
```

Nếu thấy:
- ✅ `Firebase Admin initialized successfully!`
- ✅ `Read successful!`
- ✅ `Write successful!`
- ✅ `All tests passed!`

→ Firebase đã được cấu hình đúng!

## 🚀 Tiếp Theo

1. **Restart backend** (nếu đang chạy)
2. **Test sync**: Tạo/Update/Delete Product hoặc Category trong Admin Panel
3. **Kiểm tra Firebase**: Vào [Firebase Console](https://console.firebase.google.com/) → Realtime Database

