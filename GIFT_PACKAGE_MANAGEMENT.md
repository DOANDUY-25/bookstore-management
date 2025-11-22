# Hướng Dẫn Quản Lý Gói Quà

## Tính Năng Đã Thêm

Đã thêm chức năng quản lý gói quà với đầy đủ các tính năng CRUD (Create, Read, Update, Delete).

## Các File Đã Tạo/Cập Nhật

### 1. Backend - REST API Controller
- **File**: `src/main/java/com/bookshop/bookshop/controller/AdminGiftPackageApiController.java`
- **Chức năng**: Cung cấp REST API để quản lý gói quà
- **Endpoints**:
  - `GET /admin/api/gift-packages` - Lấy danh sách tất cả gói quà
  - `GET /admin/api/gift-packages/{id}` - Lấy thông tin chi tiết một gói quà
  - `POST /admin/api/gift-packages` - Tạo gói quà mới
  - `PUT /admin/api/gift-packages/{id}` - Cập nhật thông tin gói quà
  - `DELETE /admin/api/gift-packages/{id}` - Xóa gói quà

### 2. Frontend - Giao Diện Admin
- **File**: `src/main/resources/static/admin/gift-packages.html`
- **Chức năng**: Giao diện quản lý gói quà với các tính năng:
  - Hiển thị danh sách gói quà dạng bảng
  - Tìm kiếm gói quà theo tên, loại hộp, thiệp, giấy gói
  - Thêm gói quà mới qua modal form
  - Sửa thông tin gói quà
  - Xóa gói quà (có xác nhận)
  - Hiển thị hình ảnh gói quà

### 3. Cập Nhật Menu Admin
Đã cập nhật menu trong tất cả các trang admin để thêm link "Quản Lý Gói Quà":
- `dashboard.html`
- `books.html`
- `orders.html`
- `gift-orders.html`
- `users.html`
- `memberships.html`

## Cấu Trúc Dữ Liệu

### GiftPackage Entity
```java
- giftPackageId: Integer (ID tự động tăng)
- packageName: String (Tên gói quà - bắt buộc)
- giftBox: String (Loại hộp quà)
- greetingCard: String (Loại thiệp)
- paperType: String (Loại giấy gói)
- description: Text (Mô tả chi tiết)
- packageFee: BigDecimal (Phí gói quà)
- imageURL: String (URL hình ảnh)
```

## Cách Sử Dụng

### 1. Truy Cập Trang Quản Lý
1. Đăng nhập với tài khoản ADMIN
   - Username: `admin`
   - Password: `password123`
2. Truy cập: `http://localhost:8080/admin/gift-packages.html`

### 2. Thêm Gói Quà Mới
1. Click nút "Thêm Gói Quà Mới"
2. Điền thông tin:
   - Tên gói quà (bắt buộc)
   - Loại hộp quà
   - Loại thiệp
   - Loại giấy gói
   - Mô tả
   - Phí gói quà (bắt buộc)
   - URL hình ảnh
3. Click "Lưu"

### 3. Sửa Gói Quà
1. Click nút "Sửa" ở hàng gói quà cần sửa
2. Cập nhật thông tin trong form
3. Click "Lưu"

### 4. Xóa Gói Quà
1. Click nút "Xóa" ở hàng gói quà cần xóa
2. Xác nhận xóa trong dialog

### 5. Tìm Kiếm
- Nhập từ khóa vào ô tìm kiếm
- Hệ thống sẽ tự động lọc theo:
  - Tên gói quà
  - Loại hộp quà
  - Loại thiệp
  - Loại giấy gói
  - ID gói quà

## Dữ Liệu Mẫu

Hệ thống đã có sẵn 3 gói quà mẫu trong `data.sql`:
1. **Gói Quà Cơ Bản** - 15,000₫
2. **Gói Quà Cao Cấp** - 50,000₫
3. **Gói Quà Sinh Nhật** - 30,000₫

## Bảo Mật

- Tất cả các endpoint `/admin/api/gift-packages/**` yêu cầu quyền ADMIN
- Đã được cấu hình trong `SecurityConfig.java`
- Chỉ người dùng có role ADMIN mới có thể truy cập

## Validation

### Backend Validation (DTO)
- `packageName`: Bắt buộc, tối đa 100 ký tự
- `giftBox`: Tối đa 100 ký tự
- `greetingCard`: Tối đa 100 ký tự
- `paperType`: Tối đa 100 ký tự
- `packageFee`: Phải >= 0, tối đa 10 chữ số nguyên và 2 chữ số thập phân
- `imageURL`: Tối đa 255 ký tự

### Frontend Validation
- Form validation tự động với HTML5
- Thông báo lỗi rõ ràng khi validation thất bại

## Thông Báo

Hệ thống sử dụng `notification.js` để hiển thị thông báo:
- ✅ Thành công: Khi thêm/sửa/xóa thành công
- ❌ Lỗi: Khi có lỗi xảy ra

## Kiểm Tra

Để kiểm tra chức năng:
```bash
# Build project
mvn clean compile

# Chạy ứng dụng
mvn spring-boot:run

# Truy cập
http://localhost:8080/admin/gift-packages.html
```

## Lưu Ý

- Đảm bảo đã đăng nhập với tài khoản ADMIN
- Kiểm tra kết nối database trước khi sử dụng
- Dữ liệu mẫu sẽ được load tự động khi khởi động ứng dụng
