# KIỂM TRA VÀ CHỤP ẢNH

## Đã xác minh

- Cả 9 project riêng và project tổng hợp biên dịch tạo APK thành công.
- App chung cài được trên máy ảo Android API 37.
- Mở đủ 12 demo từ Home và quay lại danh mục thành công.
- Đạt: Phép tính 8+10
- Đạt: Mảng 5 5 3
- Đạt: CRUD thêm sinh viên
- Đạt: SQLite thêm, mở lại, xóa dữ liệu thử
- Đạt: GET bài viết qua Internet
- Đạt: Tải, lưu MediaStore và đọc ảnh
- Đạt: Drawer mở, Profile, Settings và mở demo; Search và overflow Settings

## Chưa khẳng định đã kiểm tra toàn bộ

Các kiểm tra trên là biên dịch, điều hướng và các chức năng đại diện. Chưa kiểm tra mọi tổ hợp dữ liệu, mọi kích thước màn hình, mọi phiên bản Android hoặc mọi tình huống mạng. Phản hồi dịch vụ Internet có thể thay đổi. Android API 24–28 lưu ảnh vào thư mục ứng dụng thay vì MediaStore.

Nội dung, đề mục và các vị trí ảnh trên Google Docs đã được đọc lại. Xuất PDF từ kết nối Drive bị từ chối quyền (403), nên chưa kiểm chứng trực quan phân trang bản in của Google Docs. Khi chèn ảnh thật, kiểm tra lại ngắt trang trước khi nộp.

## Thứ tự chụp ảnh cho báo cáo

1. Chạy app chung và chụp trang danh mục.
2. Bấm Search rồi chụp Toast; mở menu ⋮, chọn Settings và chụp Toast.
3. Mở ☰, chụp header, Home/Profile/Settings và các nhóm chương; cuộn để chụp phần còn lại nếu cần.
4. Chọn Home, Profile, Settings trong Drawer và chụp phản hồi tương ứng.
5. Mở ít nhất một bài của mỗi chương 2, 3, 5, 6; chụp khi đã thao tác ra kết quả.
6. Quay về danh mục rồi mở bài khác để chứng minh cùng một app.
7. Chèn ảnh vào đúng chú thích ở thẻ Chương 4 → Bài tập. Không thay bằng ảnh mẫu Flutter của chương 6.

Mã nguồn app chung có package demo1..demo12, tài nguyên d1_..d12_. Hai yêu cầu cơ bản chương 4 được giữ cùng các nhóm điều hướng bổ sung.

Đã kiểm tra thêm trên một phiên máy ảo mới dùng SwiftShader: Home, Drawer, Profile và mở demo từ Drawer hiển thị đúng. Máy ảo cũ từng tạo ảnh chụp bị sọc/lặp khung hình; app không bị đổi cấu hình tắt tăng tốc đồ họa để che lỗi này. Nếu gặp lại, kiểm tra trên phiên máy ảo mới hoặc điện thoại thật.
