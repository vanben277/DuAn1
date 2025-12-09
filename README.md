# User Management System (Frontend)

Dự án Frontend quản lý nhân sự và cửa hàng, được xây dựng bằng **Vue 3**, **TypeScript** và **Vite**. Hệ thống tích hợp chặt chẽ với Backend Spring Boot thông qua cơ chế xác thực JWT nâng cao.

## 🚀 Tính năng chính (Features)

### 🔐 Authentication & Security

- **Đăng nhập (Login):** Sử dụng Access Token (ngắn hạn) và Refresh Token (dài hạn).
- **Auto Refresh Token:** Tự động cấp lại token mới khi hết hạn thông qua Axios Interceptors (không làm gián đoạn trải nghiệm người dùng).
- **Đăng xuất (Logout):** Hỗ trợ cơ chế Blacklist token phía Server.
- **Navigation Guard:** Bảo vệ các Route yêu cầu đăng nhập, tự động chuyển hướng nếu chưa có quyền.

### 👥 User Management

- **Danh sách User:** Hiển thị danh sách, phân trang.
- **Tìm kiếm & Lọc:** Lọc theo Tên, Role (Staff/Manager), Store Code.
- **Đăng ký (Register):**
  - Validate dữ liệu chặt chẽ.
  - Check trùng lặp User ID.
  - Phân quyền (Chặn tạo Admin từ giao diện này).
- **Chỉnh sửa (Edit):** Cập nhật thông tin, tự động load dữ liệu cũ, disable các trường không được sửa (ID, Password).
- **Xóa (Delete):** Xóa mềm (Soft Delete) và vô hiệu hóa tài khoản.

### 🎨 User Interface

- **Layout:** Chia tách AuthLayout (Login) và DefaultLayout (Dashboard).
- **Sidebar & Header:** Điều hướng mượt mà, hiển thị thông tin người dùng đăng nhập.

## 🛠️ Công nghệ sử dụng (Tech Stack)

- **Core:** Vue 3 (Composition API, `<script setup>`)
- **Language:** TypeScript
- **Build Tool:** Vite
- **Routing:** Vue Router 4
- **HTTP Client:** Axios (với cấu hình Interceptor nâng cao)
- **Styling:** CSS Scoped (Custom Styling)

## 📂 Cấu trúc dự án (Project Structure)

````text
src/
├── apis/               # Các hàm gọi API (authApi, userApi, storeApi...)
├── assets/             # Tài nguyên tĩnh (Images, Global CSS)
├── components/         # Các component tái sử dụng (Header, Sidebar...)
├── router/             # Cấu hình Vue Router & Navigation Guards
├── utils/              # Tiện ích (axiosClient.ts - Config Interceptor)
├── views/              # Các màn hình chính (Login, UserManagement...)
├── App.vue             # Component gốc
└── main.ts             # Entry point

### Installation

```bash

npm install

Compile and Hot-Reload for Development

npm run dev

````
