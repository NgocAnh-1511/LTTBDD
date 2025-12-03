# 🔄 Hướng Dẫn Dừng và Khởi Động Lại Service Backend Trên Railway

## 🛑 Cách Dừng Service

### **Cách 1: Dừng Tạm Thời (Suspend)**

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD** (backend)
3. Vào tab **Settings**
4. Scroll xuống tìm mục **"Suspend Service"** hoặc **"Pause"**
5. Click **Suspend** hoặc **Pause**
6. Service sẽ dừng và không tốn credit

**Lưu ý:**
- Service sẽ dừng hoàn toàn
- URL sẽ không hoạt động
- APK và Admin Panel sẽ không kết nối được
- Database vẫn hoạt động bình thường

---

### **Cách 2: Xóa Service (Delete)**

⚠️ **Cẩn thận:** Xóa service sẽ xóa toàn bộ deployment!

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD**
3. Vào tab **Settings**
4. Scroll xuống tìm mục **"Danger Zone"** hoặc **"Delete Service"**
5. Click **Delete** hoặc **Remove**
6. Xác nhận xóa

**Lưu ý:**
- Service sẽ bị xóa hoàn toàn
- Cần deploy lại từ đầu
- Database không bị ảnh hưởng (nếu là service riêng)

---

## ▶️ Cách Khởi Động Lại Service

### **Nếu Đã Suspend:**

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD**
3. Vào tab **Settings**
4. Tìm mục **"Resume Service"** hoặc **"Unpause"**
5. Click **Resume** hoặc **Unpause**
6. Railway sẽ tự động deploy lại

---

### **Nếu Đã Xóa Service:**

Cần tạo lại service:

1. Vào **Railway Dashboard**
2. Trong project, click **+ New**
3. Chọn **GitHub Repo**
4. Chọn repository: `vohoainam26/LTTBDD`
5. Chọn branch: `backend-deploy`
6. Railway sẽ tự động detect và deploy
7. **Quan trọng:** Set Root Directory: `admin-web/backend` (nếu có)
8. Set lại biến môi trường (Variables tab)

---

## 🔄 Cách Restart Service (Khởi Động Lại Nhanh)

### **Cách 1: Redeploy (Khuyến Nghị)**

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD**
3. Vào tab **Deployments**
4. Click vào deployment **ACTIVE** (màu xanh)
5. Click menu **⋮** (3 chấm)
6. Chọn **Redeploy** hoặc **Restart**
7. Railway sẽ restart service

---

### **Cách 2: Trigger Deploy Mới**

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD**
3. Vào tab **Settings**
4. Tìm mục **"Deploy"** hoặc **"Trigger Deploy"**
5. Click **Deploy** hoặc **Redeploy**
6. Railway sẽ deploy lại từ commit mới nhất

---

### **Cách 3: Push Code Mới (Tự Động Redeploy)**

1. Sửa một file bất kỳ (ví dụ: thêm comment)
2. Commit và push:
   ```bash
   git add .
   git commit -m "Trigger redeploy"
   git push fork backend-deploy
   ```
3. Railway sẽ tự động detect và deploy lại

---

## ⚙️ Các Tùy Chọn Khác

### **Scale Down (Giảm Resource)**

1. Vào **Railway Dashboard**
2. Click vào service **LTTBDD**
3. Vào tab **Settings**
4. Tìm mục **"Scaling"** hoặc **"Resources"**
5. Giảm số replica xuống 0
6. Service sẽ dừng nhưng không bị xóa

**Để khởi động lại:**
- Tăng replica lên 1

---

### **Pause Project (Dừng Toàn Bộ Project)**

1. Vào **Railway Dashboard**
2. Click vào project name (top left)
3. Vào **Settings**
4. Tìm mục **"Pause Project"**
5. Click **Pause**
6. Tất cả services trong project sẽ dừng

**Để khởi động lại:**
- Click **Resume Project**

---

## 📝 Lưu Ý

### **Khi Dừng Service:**

- ✅ **Database vẫn hoạt động** (nếu là service riêng)
- ❌ **Backend API không hoạt động**
- ❌ **APK không kết nối được**
- ❌ **Admin Panel không kết nối được**
- ✅ **Không tốn credit** (nếu suspend)

### **Khi Khởi Động Lại:**

- ⏱️ **Mất 1-2 phút** để deploy
- 🔄 **URL có thể thay đổi** (nếu xóa và tạo lại)
- ⚙️ **Cần set lại biến môi trường** (nếu tạo mới)
- 📊 **Logs sẽ bắt đầu từ đầu**

---

## ✅ Checklist

### **Trước Khi Dừng:**
- [ ] Đã thông báo cho người dùng (nếu có)
- [ ] Đã backup dữ liệu quan trọng
- [ ] Đã lưu lại URL và cấu hình

### **Sau Khi Khởi Động Lại:**
- [ ] Service đã deploy thành công
- [ ] URL vẫn hoạt động (hoặc đã cập nhật URL mới)
- [ ] Biến môi trường đã được set lại
- [ ] Đã test API hoạt động
- [ ] Đã test APK kết nối được
- [ ] Đã test Admin Panel kết nối được

---

## 🎯 Khuyến Nghị

**Để restart nhanh:**
- Dùng **Redeploy** (Cách 1) - nhanh nhất, không mất cấu hình

**Để dừng lâu dài:**
- Dùng **Suspend** - không tốn credit, dễ khởi động lại

**Để dừng hoàn toàn:**
- Dùng **Delete** - xóa hẳn, cần deploy lại từ đầu

