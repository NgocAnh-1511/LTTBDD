# 🐳 Hướng Dẫn Deploy Backend Với Docker

## 📋 Yêu Cầu

- Docker và Docker Compose đã cài đặt
- Server/VPS có thể truy cập từ internet (để APK và Admin có thể kết nối)
- Domain name (tùy chọn, có thể dùng IP)

---

## 🚀 Các Bước Deploy

### **Bước 1: Chuẩn Bị Server**

**Nếu dùng VPS/Cloud Server:**
- Ubuntu 20.04+ hoặc tương đương
- Tối thiểu 1GB RAM, 1 CPU core
- Cài Docker và Docker Compose:
  ```bash
  # Cài Docker
  curl -fsSL https://get.docker.com -o get-docker.sh
  sudo sh get-docker.sh
  
  # Cài Docker Compose
  sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
  ```

**Nếu dùng máy local (để test):**
- Cài Docker Desktop (Windows/Mac) hoặc Docker Engine (Linux)

---

### **Bước 2: Clone Code Lên Server**

```bash
# Clone repository
git clone https://github.com/vohoainam26/LTTBDD.git
cd LTTBDD/admin-web/backend

# Hoặc nếu đã có code, copy lên server
```

---

### **Bước 3: Tạo File .env**

Tạo file `.env` trong thư mục `admin-web/backend`:

```env
# Database Configuration
DB_ROOT_PASSWORD=your-strong-root-password
DB_USERNAME=admin
DB_PASSWORD=your-strong-db-password
DB_NAME=CoffeShop
DB_PORT=3306

# Server Configuration
PORT=3000
NODE_ENV=production

# JWT Configuration
JWT_SECRET=your-very-strong-secret-key-min-32-characters-long
JWT_EXPIRES_IN=7d
```

**⚠️ Lưu ý:** Thay các giá trị `your-strong-*` bằng mật khẩu mạnh thực tế!

---

### **Bước 4: Deploy Với Docker Compose**

**Cho môi trường Production:**
```bash
docker-compose -f docker-compose.prod.yml --env-file .env up -d
```

**Cho môi trường Development (test):**
```bash
docker-compose --env-file .env up -d
```

**Kiểm tra logs:**
```bash
# Xem logs backend
docker-compose logs -f backend

# Xem logs MySQL
docker-compose logs -f mysql
```

---

### **Bước 5: Cấu Hình Firewall (Nếu dùng VPS)**

Mở port 3000 để backend có thể truy cập từ internet:

```bash
# Ubuntu/Debian
sudo ufw allow 3000/tcp
sudo ufw reload

# Hoặc nếu dùng iptables
sudo iptables -A INPUT -p tcp --dport 3000 -j ACCEPT
```

---

### **Bước 6: Cấu Hình Domain/Reverse Proxy (Tùy chọn)**

**Nếu có domain (ví dụ: api.yourdomain.com):**

Cài Nginx làm reverse proxy:

```nginx
# /etc/nginx/sites-available/coffeeshop-api
server {
    listen 80;
    server_name api.yourdomain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

Sau đó:
```bash
sudo ln -s /etc/nginx/sites-available/coffeeshop-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

**Nếu không có domain:**
- Dùng IP server: `http://YOUR_SERVER_IP:3000/api`

---

### **Bước 7: Test API**

```bash
# Test từ server
curl http://localhost:3000/api

# Test từ máy khác (thay YOUR_SERVER_IP)
curl http://YOUR_SERVER_IP:3000/api
```

---

## 🔧 Quản Lý Docker Containers

### **Xem trạng thái:**
```bash
docker-compose ps
```

### **Dừng services:**
```bash
docker-compose down
```

### **Khởi động lại:**
```bash
docker-compose restart
```

### **Xem logs:**
```bash
docker-compose logs -f backend
docker-compose logs -f mysql
```

### **Cập nhật code:**
```bash
# Pull code mới
git pull

# Rebuild và restart
docker-compose build --no-cache backend
docker-compose up -d
```

---

## 📱 Cấu Hình APK và Admin Panel

### **1. Cập Nhật APK (Android App)**

Trong file `ApiClient.kt`, thay đổi:

```kotlin
// Thay vì
BASE_URL = "http://10.0.2.2:3000/api/"

// Dùng
BASE_URL = "http://YOUR_SERVER_IP:3000/api/"
// Hoặc nếu có domain
BASE_URL = "https://api.yourdomain.com/api/"
```

### **2. Cập Nhật Admin Panel**

Trong file `.env` của frontend admin:

```env
VITE_API_URL=http://YOUR_SERVER_IP:3000/api
# Hoặc
VITE_API_URL=https://api.yourdomain.com/api
```

---

## 🔒 Bảo Mật

### **1. Đổi mật khẩu mặc định:**
- Đổi `DB_ROOT_PASSWORD`, `DB_PASSWORD` trong `.env`
- Đổi `JWT_SECRET` thành một chuỗi ngẫu nhiên mạnh

### **2. Sử dụng HTTPS:**
- Cài SSL certificate (Let's Encrypt miễn phí)
- Cấu hình Nginx với SSL

### **3. Firewall:**
- Chỉ mở port cần thiết (3000 cho backend, 80/443 cho Nginx)
- Đóng port 3306 (MySQL) - chỉ backend mới truy cập được

---

## 🐛 Troubleshooting

### **Backend không kết nối được MySQL:**
```bash
# Kiểm tra MySQL đã chạy chưa
docker-compose ps mysql

# Kiểm tra logs MySQL
docker-compose logs mysql

# Kiểm tra network
docker network ls
docker network inspect admin-web_backend_coffeeshop-network
```

### **Port đã được sử dụng:**
```bash
# Tìm process đang dùng port 3000
sudo lsof -i :3000
# Hoặc
sudo netstat -tulpn | grep 3000

# Kill process hoặc đổi PORT trong .env
```

### **Database chưa được import:**
```bash
# Import thủ công
docker exec -i coffeeshop-mysql mysql -u admin -padminpassword CoffeShop < database/database.sql
```

---

## ✅ Checklist

- [ ] Docker và Docker Compose đã cài đặt
- [ ] Code đã clone lên server
- [ ] File `.env` đã tạo với mật khẩu mạnh
- [ ] Docker containers đã chạy (`docker-compose ps`)
- [ ] Backend API đã test thành công
- [ ] Firewall đã mở port 3000
- [ ] APK đã cập nhật BASE_URL
- [ ] Admin Panel đã cập nhật API_URL
- [ ] HTTPS đã cấu hình (nếu có domain)

---

## 🎯 So Sánh Docker vs Railway

| Tiêu chí | Docker | Railway |
|---------|--------|---------|
| **Chi phí** | Tự host (VPS ~$5-10/tháng) | Miễn phí $5/tháng |
| **Kiểm soát** | Toàn quyền | Giới hạn |
| **Cấu hình** | Linh hoạt hơn | Đơn giản hơn |
| **Bảo mật** | Tự quản lý | Railway quản lý |
| **Scale** | Tự scale | Railway tự scale |

**Khuyến nghị:**
- **Docker:** Nếu bạn có VPS/server riêng, muốn kiểm soát hoàn toàn
- **Railway:** Nếu muốn deploy nhanh, không muốn quản lý server

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, kiểm tra:
1. Logs: `docker-compose logs -f`
2. Trạng thái containers: `docker-compose ps`
3. Network: `docker network ls`

