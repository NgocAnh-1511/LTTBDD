# 🚀 Hướng Dẫn Deploy Nhanh Lên Railway

## ✅ Kết Quả Kiểm Tra

**Repository của bạn ĐỦ ĐIỀU KIỆN để deploy lên Railway!**

---

## 📋 Các Bước Deploy

### **1. Push Code Lên GitHub**

```bash
git add .
git commit -m "Prepare for Railway deployment"
git push origin main
```

### **2. Tạo Project Trên Railway**

1. Đăng ký/Đăng nhập: https://railway.app
2. Click **New Project**
3. Chọn **Deploy from GitHub repo**
4. Chọn repository của bạn
5. **Quan trọng:** Trong Settings → Service → Root Directory
   - Set: `admin-web/backend`

### **3. Setup Database**

**Option A: Railway MySQL (Dễ nhất)**
1. Trong Railway project, click **+ New**
2. Chọn **Database** → **MySQL**
3. Railway tự động tạo database
4. Import database:
   - Click vào MySQL service
   - Mở **Data** tab
   - Hoặc dùng MySQL client với connection info từ **Variables** tab
   - Import file `database/database.sql`

**Option B: PlanetScale (Miễn phí)**
1. Tạo database trên https://planetscale.com
2. Import `database/database.sql`
3. Lấy connection string

### **4. Cấu Hình Biến Môi Trường**

Trong Railway → Service → Variables, thêm:

**Nếu dùng Railway MySQL:**
```env
DB_HOST=${{MySQL.MYSQLHOST}}
DB_PORT=${{MySQL.MYSQLPORT}}
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
DB_NAME=${{MySQL.MYSQLDATABASE}}
PORT=3000
NODE_ENV=production
JWT_SECRET=your-very-strong-secret-key-min-32-chars
JWT_EXPIRES_IN=7d
```

**Nếu dùng PlanetScale hoặc database khác:**
```env
DB_HOST=your-db-host
DB_PORT=3306
DB_USERNAME=your-username
DB_PASSWORD=your-password
DB_NAME=CoffeShop
PORT=3000
NODE_ENV=production
JWT_SECRET=your-very-strong-secret-key-min-32-chars
JWT_EXPIRES_IN=7d
```

### **5. Deploy**

1. Railway tự động detect NestJS và deploy
2. Xem logs trong **Deployments** tab
3. Railway sẽ cung cấp URL: `https://your-app.railway.app`

### **6. Test**

Mở browser: `https://your-app.railway.app/api`

Phải thấy response hoặc 401 (nếu chưa login)

---

## ⚙️ Cấu Hình Build (Nếu Cần)

Railway tự động detect, nhưng nếu cần set thủ công:

**Settings → Build:**
- Build Command: `npm run build`
- Start Command: `npm run start:prod`

---

## 🔑 Lấy URL Backend

Sau khi deploy thành công:
1. Click vào service
2. Click tab **Settings**
3. Copy **Public Domain**: `https://your-app.railway.app`
4. **API Base URL**: `https://your-app.railway.app/api`

Dùng URL này để:
- Cập nhật APK (`ApiClient.kt`)
- Cấu hình Admin Panel (`.env`)

---

## ✅ Checklist

- [ ] Code đã push lên GitHub
- [ ] Đã tạo Railway project
- [ ] Đã set Root Directory: `admin-web/backend`
- [ ] Đã setup database và import `database.sql`
- [ ] Đã set biến môi trường
- [ ] Deploy thành công
- [ ] Test API hoạt động
- [ ] Lưu URL backend lại

---

## 🐛 Troubleshooting

### Build Failed
- Kiểm tra Root Directory: `admin-web/backend`
- Xem logs trong Deployments tab

### Database Connection Error
- Kiểm tra biến môi trường DB_*
- Kiểm tra database đã được tạo và import chưa

### Port Error
- Railway tự động set PORT
- Không cần config thêm

---

## 📞 Xem Chi Tiết

Xem file `RAILWAY_CHECK.md` để biết chi tiết kiểm tra và troubleshooting.

