# Hệ thống IoT Quản lý Nhiệt độ, Độ ẩm và Ánh sáng (TH4)

Đây là toàn bộ mã nguồn của hệ thống IoT bao gồm thiết bị phần cứng, máy chủ Backend và ứng dụng di động. Hệ thống cho phép thu thập dữ liệu cảm biến (Nhiệt độ, độ ẩm, ánh sáng) theo thời gian thực và cho phép người dùng điều khiển thiết bị đèn từ xa thông qua ứng dụng di động.

## 🏗 Cấu trúc hệ thống

Dự án được chia thành 3 phần chính tương ứng với 3 thư mục:

### 1. `TH4/` (Thiết bị phần cứng - ESP32)
- **Công nghệ:** C++ (Arduino IDE).
- **Phần cứng:** Vi điều khiển ESP32, Cảm biến DHT11 (nhiệt độ, độ ẩm), Cảm biến LDR (ánh sáng), 3 đèn LED.
- **Chức năng:**
  - Thu thập dữ liệu môi trường.
  - Kết nối với MQTT Broker (`172.20.10.4:1884`) qua WiFi.
  - Publish dữ liệu cảm biến lên topic `esp32/sensor`.
  - Subscribe topic `esp32/devices/control` để nhận lệnh bật/tắt đèn và gửi kết quả về `esp32/led/status`.

### 2. `BE/` (Máy chủ Backend - Node.js)
- **Công nghệ:** Node.js, Express.js, Sequelize ORM (MySQL), Socket.io, MQTT.
- **Chức năng:**
  - Hoạt động như một cầu nối nhận dữ liệu qua giao thức MQTT và lưu vào cơ sở dữ liệu MySQL.
  - Cung cấp các **RESTful API** để ứng dụng di động truy xuất lịch sử, thống kê.
  - Có tích hợp **Socket.io** (`/test-socket.html`) để đẩy dữ liệu cảm biến thời gian thực tới App/Web.
  - Tích hợp sẵn Swagger phục vụ việc xem tài liệu API.
- Xem chi tiết hướng dẫn tại file `BE/README.md`.

### 3. `mobile/` (Ứng dụng di động - Android Native)
- **Công nghệ:** Android Native (Gradle, Kotlin/Java).
- **Chức năng:**
  - Ứng dụng người dùng dùng để trực quan hóa dữ liệu lấy từ Backend.
  - Hiển thị thông số theo thời gian thực (nhiệt độ, độ ẩm, theo dõi ánh sáng).
  - Giao diện bảng điều khiển công tắc để bật/tắt thiết bị đèn.
  - Xem lịch sử log hệ thống.

---

## 🚀 Hướng dẫn cài đặt và khởi chạy

### Bước 1: Khởi động Backend
1. Di chuyển vào thư mục BE: `cd BE`
2. Chạy lệnh cài đặt các gói NPM: `npm install`
3. Sao chép `.env.example` thành `.env`, sau đó cấp cấu hình cơ sở dữ liệu của bạn và địa chỉ MQTT Broker vào.
4. Chạy Backend bằng lệnh: `npm run dev`

### Bước 2: Nạp code ESP32
1. Mở file `TH4/TH4.ino` bằng Arduino IDE.
2. Sửa thông số mạng (WiFi SSID, Password) tại dòng:
   ```cpp
   const char* ssid = "YOUR_WIFI_SSID";
   const char* password = "YOUR_WIFI_PASSWORD";
   ```
3. Lưu ý sửa IP của máy chủ MQTT (thường là IP LAN của máy đang chạy Mosquitto) tại `mqtt_server`.
4. Cắm mạch ESP32 và tiến hành nạp code (Upload).

### Bước 3: Chạy ứng dụng Android Mobile
1. Khởi động Android Studio và chọn thư mục `mobile/`.
2. Để Android Studio đồng bộ gradle và tải các thư viện xuống.
3. Thay đổi thiết lập IP hoặc Base URL bên trong mã của Android trỏ về IP máy chủ phụ trách Backend và MQTT của bạn.
4. Build và Run ứng dụng trên máy ảo hoặc thiết bị thật.

---

## 🔗 Liên kết liên quan và Tài liệu
- MQTT Broker sử dụng địa chỉ cục bộ, vui lòng đảm bảo IP cấu hình trùng với máy chủ cài Mosquitto/EMQX.
- Xem tài liệu API Swagger bằng cách chạy Backend và truy cập: `http://localhost:<PORT>/api/docs`
