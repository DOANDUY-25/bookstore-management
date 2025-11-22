-- Insert sample users (password is 'password123' encoded with BCrypt)
INSERT INTO user (userName, password, role, fullName, email, address, phone) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'Administrator', 'admin@bookshop.com', 'Hà Nội', '0123456789'),
('user1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 'Nguyễn Văn A', 'user1@example.com', 'Hà Nội', '0987654321'),
('user2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 'Trần Thị B', 'user2@example.com', 'Hồ Chí Minh', '0912345678');

-- Insert sample books
INSERT INTO book (title, author, category, price, stock, description, coverImageURL, rating) VALUES
('Đắc Nhân Tâm', 'Dale Carnegie', 'Kỹ Năng Sống', 86000, 50, 'Cuốn sách kinh điển về nghệ thuật giao tiếp và ứng xử', 'https://salt.tikicdn.com/cache/w1200/ts/product/7e/14/90/67e6b3c6a53c0b5bf6d0b0b3f3f8c9e1.jpg', 4.8),
('Nhà Giả Kim', 'Paulo Coelho', 'Tiểu Thuyết', 79000, 45, 'Câu chuyện về hành trình tìm kiếm kho báu và ý nghĩa cuộc sống', 'https://salt.tikicdn.com/cache/w1200/ts/product/45/3b/fc/aa81d0a534b45706ae1eee1e344e80d9.jpg', 4.9),
('Sapiens: Lược Sử Loài Người', 'Yuval Noah Harari', 'Lịch Sử', 198000, 30, 'Câu chuyện về sự tiến hóa của loài người', 'https://salt.tikicdn.com/cache/w1200/ts/product/5e/18/24/2a6154ba08df6ce6161c13f4303fa19e.jpg', 4.7),
('Tuổi Trẻ Đáng Giá Bao Nhiêu', 'Rosie Nguyễn', 'Kỹ Năng Sống', 80000, 60, 'Những bài học về cuộc sống dành cho tuổi trẻ', 'https://salt.tikicdn.com/cache/w1200/ts/product/46/0e/6b/aa7c3a6b8f7c6f3c3e3e3e3e3e3e3e3e.jpg', 4.6),
('Cà Phê Cùng Tony', 'Tony Buổi Sáng', 'Kinh Doanh', 68000, 40, 'Những bài học kinh doanh và cuộc sống', 'https://salt.tikicdn.com/cache/w1200/ts/product/ca/eb/26/8befd6b8f7c6f3c3e3e3e3e3e3e3e3e.jpg', 4.5),
('Tôi Tài Giỏi, Bạn Cũng Thế', 'Adam Khoo', 'Kỹ Năng Sống', 86000, 35, 'Phương pháp học tập hiệu quả', 'https://salt.tikicdn.com/cache/w1200/ts/product/5e/18/24/2a6154ba08df6ce6161c13f4303fa19e.jpg', 4.4),
('Nghệ Thuật Bán Hàng', 'Brian Tracy', 'Kinh Doanh', 120000, 25, 'Kỹ năng bán hàng chuyên nghiệp', 'https://salt.tikicdn.com/cache/w1200/ts/product/ca/eb/26/8befd6b8f7c6f3c3e3e3e3e3e3e3e3e.jpg', 4.3),
('Đời Ngắn Đừng Ngủ Dài', 'Robin Sharma', 'Kỹ Năng Sống', 108000, 55, 'Sống một cuộc đời ý nghĩa', 'https://salt.tikicdn.com/cache/w1200/ts/product/46/0e/6b/aa7c3a6b8f7c6f3c3e3e3e3e3e3e3e3e.jpg', 4.7),
('Khởi Nghiệp Tinh Gọn', 'Eric Ries', 'Kinh Doanh', 189000, 20, 'Phương pháp khởi nghiệp hiệu quả', 'https://salt.tikicdn.com/cache/w1200/ts/product/5e/18/24/2a6154ba08df6ce6161c13f4303fa19e.jpg', 4.6),
('Tư Duy Nhanh Và Chậm', 'Daniel Kahneman', 'Tâm Lý Học', 199000, 28, 'Hai hệ thống tư duy của con người', 'https://salt.tikicdn.com/cache/w1200/ts/product/ca/eb/26/8befd6b8f7c6f3c3e3e3e3e3e3e3e3e.jpg', 4.8);

-- Insert sample gift packages
INSERT INTO giftpackage (packageName, giftBox, greetingCard, paperType, description, packageFee, imageURL) VALUES
('Gói Quà Cơ Bản', 'Hộp giấy kraft', 'Thiệp đơn giản', 'Giấy kraft', 'Gói quà đơn giản, phù hợp mọi dịp', 15000, 'https://via.placeholder.com/300x200?text=Basic+Package'),
('Gói Quà Cao Cấp', 'Hộp gỗ sang trọng', 'Thiệp cao cấp', 'Giấy gói hoa', 'Gói quà sang trọng cho dịp đặc biệt', 50000, 'https://via.placeholder.com/300x200?text=Premium+Package'),
('Gói Quà Sinh Nhật', 'Hộp màu sắc', 'Thiệp sinh nhật', 'Giấy bóng', 'Gói quà dành riêng cho sinh nhật', 30000, 'https://via.placeholder.com/300x200?text=Birthday+Package');
