# Bài tập Mobile App — Nhóm 7

Repo gồm **12 ví dụ riêng lẻ** (mỗi chương 2, 3, 5, 6 có 3 ví dụ) và **1 app tổng hợp chương 4**.

## Mục lục các ví dụ

| Chương | Ví dụ / bài trong tài liệu | Nội dung | Mã nguồn |
|---|---|---|---|
| 2 | 1 | Hàm PhepTinh thực hiện các phép toán | [Mở dự án](Chuong_2/Vi_du_01_PhepTinh) |
| 2 | 2 | Nhập mảng và thống kê các phần tử | [Mở dự án](Chuong_2/Vi_du_02_ThongKeMang) |
| 2 | 3 | Quản lý sinh viên: thêm, xem, sửa, xóa | [Mở dự án](Chuong_2/Vi_du_03_CRUDSinhVien) |
| 3 | 1 | Danh sách sinh viên — bài 1 trong tài liệu | [Mở dự án](Chuong_3/Vi_du_01_DanhSachSinhVien) |
| 3 | 4 | Quản lý công việc — bài 4 trong tài liệu | [Mở dự án](Chuong_3/Vi_du_04_QuanLyCongViec) |
| 3 | 9 | Danh sách môn học — bài 9 trong tài liệu | [Mở dự án](Chuong_3/Vi_du_09_DanhSachMonHoc) |
| 5 | 1 | GET: lấy danh sách bài viết | [Mở dự án](Chuong_5/Vi_du_01_GetPosts) |
| 5 | 2 | REST API: POST, PUT, DELETE | [Mở dự án](Chuong_5/Vi_du_02_PostPutDelete) |
| 5 | 3 | SQLite: quản lý sinh viên | [Mở dự án](Chuong_5/Vi_du_03_SQLite) |
| 6 | 1 | Tải ảnh và lưu URL | [Mở dự án](Chuong_6/Vi_du_01_TaiAnhLuuURL) |
| 6 | 2 | Lưu và đọc ảnh | [Mở dự án](Chuong_6/Vi_du_02_LuuDocAnh) |
| 6 | 3 | Hiển thị danh sách ảnh | [Mở dự án](Chuong_6/Vi_du_03_DanhSachAnh) |

**Chương 3 giữ nguyên số bài trong tài liệu là 1, 4, 9**, không đổi thành 1, 2, 3. Tổng cộng vẫn là 3 ví dụ của chương 3.

## Chương 4 — App tổng hợp

[App tổng hợp nhóm 7](Chuong_4/App_Tong_Hop_Nhom_7) tích hợp cả 12 ví dụ trên, với Action Bar và Navigation Drawer để chọn chương và bài. Đây là dự án tổng hợp riêng, bên cạnh 12 dự án ví dụ.

## Cách tải và chạy

1. Chọn **Code → Download ZIP**, giải nén; hoặc clone repo.
2. Trong Android Studio, chọn **File → Open** và mở đúng thư mục ví dụ có `settings.gradle.kts` (hoặc thư mục app tổng hợp chương 4).
3. Chờ Gradle Sync; cài SDK và dùng JDK theo cấu hình Gradle của từng dự án.
4. Chọn máy ảo hoặc điện thoại Android, nhấn **Run**.

Các dự án có Gradle Wrapper riêng. Thư mục gốc repo chứa nhiều dự án độc lập; hãy mở từng dự án để chạy. `local.properties` được tạo theo đường dẫn SDK trên máy của bạn.

Một số ví dụ cần Internet để gọi API hoặc tải ảnh.
