# ✅ Kiểm Tra Sẵn Sàng Deploy Lên Railway

## 📋 Kết Quả Kiểm Tra

### ✅ **ĐẠT YÊU CẦU - Sẵn sàng deploy!**

---

## ✅ Các Điều Kiện Đã Đạt

### 1. **package.json** ✅
- ✅ Có script `build`: `"build": "nest build"`
- ✅ Có script `start`: `"start": "nest start"`
- ✅ Có script `start:prod`: `"start:prod": "node dist/main"`
- ✅ Có đầy đủ dependencies (NestJS, TypeORM, MySQL2, JWT...)
- ✅ Có devDependencies (TypeScript, NestJS CLI...)

### 2. **Cấu Trúc Thư Mục** ✅
- ✅ Có thư mục `src/` với source code
- ✅ Có `main.ts` - Entry point
- ✅ Có `app.module.ts` - Root module
- ✅ Có các modules: auth, users, products, orders, vouchers, banners, categories

### 3. **Cấu Hình TypeScript** ✅
- ✅ Có `tsconfig.json`
- ✅ Có `nest-cli.json`
- ✅ Compiler options đúng

### 4. **Cấu Hình Backend** ✅
- ✅ `main.ts` đọc PORT từ environment: `process.env.PORT || 3000`
- ✅ CORS đã được enable
- ✅ Global prefix: `/api`
- ✅ Validation pipe đã cấu hình

### 5. **Database Configuration** ✅
- ✅ Sử dụng `@nestjs/config` để đọc biến môi trường
- ✅ TypeORM đọc từ env:
  - `DB_HOST`
  - `DB_PORT`
  - `DB_USERNAME`
  - `DB_PASSWORD`
  - `DB_NAME`
- ✅ `synchronize: false` (an toàn cho production)

### 6. **Git Configuration** ✅
- ✅ Có `.gitignore`
- ✅ Đã ignore `node_modules`, `dist`, `.env`

---

## ⚠️ Cần Bổ Sung (Không Bắt Buộc Nhưng Nên Có)

### 1. **File .env.example** (Khuyến nghị)
- Tạo file `.env.example` để hướng dẫn cấu hình
- Railway sẽ dùng để biết cần set biến môi trường nào

### 2. **File railway.json** (Tùy chọn)
- Có thể thêm để cấu hình build/start commands rõ ràng hơn
- Railway có thể tự detect NestJS, nhưng file này giúp rõ ràng hơn

---

## 🚀 Các Bước Deploy Lên Railway

### **Bước 1: Push Code Lên GitHub**

Đảm bảo code đã được push lên GitHub repository:
```bash
git add .
git commit -m "Prepare for Railway deployment"
git push origin main
```

### **Bước 2: Tạo Project Trên Railway**

1. Đăng ký/Đăng nhập: https://railway.app
2. New Project → Deploy from GitHub
3. Chọn repository của bạn
4. **Quan trọng:** Chọn **Root Directory**: `admin-web/backend`
   - Railway sẽ build từ thư mục này

### **Bước 3: Cấu Hình Biến Môi Trường**

Trong Railway dashboard, thêm các biến môi trường:

```env
# Database (sẽ lấy từ Railway MySQL service)
DB_HOST=${{MySQL.MYSQLHOST}}
DB_PORT=${{MySQL.MYSQLPORT}}
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
DB_NAME=${{MySQL.MYSQLDATABASE}}

# Server
PORT=3000
NODE_ENV=production

# JWT
JWT_SECRET=your-very-strong-secret-key-here-min-32-chars
JWT_EXPIRES_IN=7d
```

**Hoặc nếu dùng database bên ngoài (PlanetScale, etc.):**
```env
DB_HOST=your-db-host
DB_PORT=3306
DB_USERNAME=your-username
DB_PASSWORD=your-password
DB_NAME=CoffeShop
PORT=3000
NODE_ENV=production
JWT_SECRET=your-very-strong-secret-key
JWT_EXPIRES_IN=7d
```

### **Bước 4: Setup Database**

**Option A: Railway MySQL (Khuyến nghị)**
1. Trong Railway project, Add Service → Database → MySQL
2. Railway tự động tạo database
3. Lấy connection info từ Variables tab
4. Import database:
   - Dùng MySQL client hoặc Railway MySQL terminal
   - Import file `database/database.sql`

**Option B: PlanetScale (Miễn phí)**
1. Tạo database trên PlanetScale
2. Import `database/database.sql`
3. Lấy connection string và set vào biến môi trường

### **Bước 5: Deploy**

1. Railway sẽ tự động:
   - Detect NestJS
   - Chạy `npm install`
   - Chạy `npm run build`
   - Chạy `npm start` (hoặc `npm run start:prod`)

2. Railway sẽ cung cấp URL: `https://your-app.railway.app`

3. Test API:
   - `https://your-app.railway.app/api`
   - Phải thấy response hoặc 401 (nếu chưa login)

---

## 📝 Lưu Ý Quan Trọng

### **Root Directory**
- Khi deploy, Railway cần biết thư mục backend
- Chọn: `admin-web/backend` trong Railway settings

### **Build Command**
- Railway tự detect: `npm run build`
- Nếu không tự động, set trong Settings:
  - Build Command: `npm run build`
  - Start Command: `npm run start:prod`

### **Port**
- Railway tự động set PORT
- Code đã đọc `process.env.PORT` nên không cần sửa

### **Database**
- Phải import database trước khi deploy
- Hoặc chạy migrations nếu có

---

## ✅ Checklist Trước Khi Deploy

- [ ] Code đã push lên GitHub
- [ ] Đã tạo Railway account
- [ ] Đã setup database (Railway MySQL hoặc PlanetScale)
- [ ] Đã import database từ `database.sql`
- [ ] Đã set biến môi trường trong Railway
- [ ] Đã set Root Directory: `admin-web/backend`
- [ ] Test API sau khi deploy

---

## 🐛 Troubleshooting

### **Build Failed**
- Kiểm tra logs trong Railway
- Đảm bảo Root Directory đúng: `admin-web/backend`
- Kiểm tra Node version (Railway tự detect)

### **Database Connection Error**
- Kiểm tra biến môi trường DB_*
- Kiểm tra database đã được tạo chưa
- Kiểm tra firewall/network của database

### **Port Error**
- Railway tự động set PORT, không cần config
- Code đã đọc `process.env.PORT`

### **CORS Error**
- Code đã enable CORS với `origin: true`
- Nếu vẫn lỗi, kiểm tra frontend URL

---

## 🎯 Kết Luận

**✅ Repository của bạn ĐỦ ĐIỀU KIỆN để deploy lên Railway!**

Tất cả các yêu cầu cơ bản đã đạt:
- ✅ package.json với scripts đầy đủ
- ✅ Cấu trúc thư mục đúng
- ✅ Cấu hình TypeScript
- ✅ Cấu hình environment variables
- ✅ CORS enabled
- ✅ Database configuration

**Bạn có thể deploy ngay bây giờ!**

