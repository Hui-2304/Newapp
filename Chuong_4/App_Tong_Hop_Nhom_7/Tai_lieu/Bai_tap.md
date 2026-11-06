# BÀI TẬP CHƯƠNG 4
# TÍCH HỢP CÁC DEMO CỦA NHÓM VÀO MỘT ỨNG DỤNG ANDROID
Sinh viên: Nguyễn Quang Bảo Huy     •     MSSV: N23DCCI033     •     Lớp: D23CQCI01-N

## 1 Yêu cầu bài toán
Xây dựng một ứng dụng Android chung chứa các demo chương 2, 3, 5 và 6 của nhóm. Người dùng chỉ cài và mở một ứng dụng; từ menu hoặc trang danh mục có thể chọn từng bài, thao tác trên giao diện riêng rồi quay về để mở bài khác. Chương 4 phụ trách thanh công cụ, menu và điều hướng của ứng dụng tổng hợp.
Giữ hai yêu cầu cơ bản trong bài giảng: Action Bar có Search và Settings, chọn mục hiện Toast; Navigation Drawer có Home, Profile, Settings, chọn mục hiện Toast. Mở rộng Drawer bằng các nhóm chương để đáp ứng yêu cầu tích hợp demo. [1, tr. 46–47]
Sản phẩm là project D:\Android\Chuong4_AppTongHopNhom7. Các project riêng của chương 2, 5 và 6 được giữ bên cạnh để đối chiếu. Ba project chương 3 có sẵn là nguồn mẫu về cấu hình và mã giao diện. Không cần mọi thành viên phát triển trên cùng một máy; bước tích hợp sử dụng mã nguồn được tập hợp về máy thực hiện.

## 2 Phân tích và thiết kế
### 2.1 Danh mục chức năng
Chương 2 – Bài 1: Máy tính hai số nguyên, chọn +, -, *, /, %, hiển thị kết quả và ngăn chia/lấy dư cho 0.
Chương 2 – Bài 2: Nhập mảng số nguyên, tìm lớn nhất/nhỏ nhất, tổng/trung bình, tổng chẵn và số lớn thứ hai phân biệt.
Chương 2 – Bài 3: CRUD sinh viên theo mã bằng danh sách trong bộ nhớ.
Chương 3 – Bài 1: Danh sách sinh viên với Custom ListView.
Chương 3 – Bài 2: Quản lý công việc: thêm, sửa, xóa và đánh dấu hoàn thành.
Chương 3 – Bài 3: Danh sách môn học, lựa chọn đã học và thống kê tín chỉ.
Chương 5 – Bài 1: GET danh sách bài viết và xem chi tiết qua REST API.
Chương 5 – Bài 2: POST, PUT, DELETE và hiển thị phản hồi HTTP.
Chương 5 – Bài 3: CRUD sinh viên bằng SQLite, lưu dữ liệu trên thiết bị.
Chương 6 – Bài 1: Tải ảnh bằng Glide/Picasso, lưu/đọc/xóa URL bằng SharedPreferences.
Chương 6 – Bài 2: Tải ảnh, lưu ảnh vào thư viện hoặc thư mục ứng dụng, đọc lại ảnh đã lưu.
Chương 6 – Bài 3: RecyclerView nhiều ảnh, trạng thái tải, thêm URL và thử lại khi tải lỗi.
### 2.2 Cấu trúc giao diện
Màn hình Home gồm Toolbar ở trên và danh sách 12 nút demo trong vùng cuộn. Toolbar có tiêu đề, nút ☰, Search và menu mở rộng Settings. Drawer gồm header giới thiệu nhóm, ba mục cơ bản và bốn nhóm chương. Một mục tương ứng một điểm đến cụ thể.
Mỗi demo hiển thị trên Activity riêng trong cùng app. Thanh công cụ của màn hình demo có tiêu đề bài và mũi tên quay lại. Giao diện chương 3 được dùng lại từ các project mẫu; chương 2 và 5 bám mã và yêu cầu trong Docs; chương 6 được viết lại bằng Kotlin từ tham khảo Flutter theo thống nhất của nhóm.
### 2.3 Tổ chức mã nguồn
MainActivity.kt ở package com.group7.tonghop tạo Home, Toolbar, Drawer và danh sách điểm đến. DemoActivity.kt cung cấp thanh công cụ cho các màn hình con. Package demo1 đến demo12 chứa mã từng bài. AndroidManifest.xml khai báo các Activity; chỉ màn hình chính là điểm khởi chạy của ứng dụng.
Tài nguyên của bài thứ n có tiền tố dn_: ví dụ d1_activity_main.xml là giao diện phép tính và d3_activity_main.xml là CRUD sinh viên chương 2. Mọi tham chiếu R.layout/R.id và tham chiếu trong XML phải được đổi đồng bộ. Cách tổ chức này tránh xung đột khi nhiều bài cùng có MainActivity hoặc activity_main.xml.

## 3 Nguyên lý xử lý
### 3.1 Khởi động app
Android mở MainActivity chính → dựng DrawerLayout và Toolbar → tạo menu → liên kết ActionBarDrawerToggle → hiển thị trang Home với 12 bài. Việc khởi động không yêu cầu mở các project riêng hoặc cài các APK của từng thành viên.
### 3.2 Chọn bài từ Home hoặc Drawer
Danh sách demos lưu cặp tiêu đề và lớp Activity. Cả nút ở Home và mục Drawer đều lấy dữ liệu từ danh sách này. Khi chọn bài, hàm open(index) kiểm tra chỉ số và gọi Intent tường minh. Extra demo_title giúp màn hình con nhận đúng tên hiển thị.
```kotlin
private fun open(i: Int) {
    if (i !in demos.indices) return
    val demo = demos[i]
    startActivity(
        Intent(this, demo.second)
            .putExtra("demo_title", demo.first)
    )
}
```
### 3.3 Menu cơ bản của chương 4
Search và Settings trên Toolbar chỉ hiện Toast đúng yêu cầu bài 1. Home, Profile và Settings trong Drawer đều có Toast; đồng thời thay nội dung chính bằng danh mục hoặc thông tin giới thiệu. Drawer được đóng sau khi chọn. Không mô tả Search là chức năng tìm kiếm dữ liệu vì bài hiện tại chưa triển khai tìm kiếm thật.
### 3.4 Quay lại
Ở màn hình demo, mũi tên trên thanh công cụ gọi finish() để quay về màn hình trước. Ở Home, nếu Drawer đang mở thì phím Back đóng Drawer trước; nếu Drawer đã đóng thì Back rời Activity chính. Khi đang ở chi tiết bài viết, quay lại sẽ trở về danh sách bài viết trước khi về Home.
### 3.5 Phạm vi lưu trữ
Danh sách bộ nhớ của chương 2 và các bài chương 3 không có cơ chế lưu bền vững qua lần khởi tạo lại Activity. SQLite lưu sinh viên trên thiết bị; SharedPreferences lưu URL; ảnh được lưu theo lựa chọn của bài chương 6. Không coi việc đổi project riêng sang app chung là thao tác chuyển dữ liệu ứng dụng đã cài.
API JSONPlaceholder mô phỏng thao tác POST/PUT/DELETE; kết quả HTTP được hiển thị để quan sát. Thành công của yêu cầu không có nghĩa dữ liệu được lưu vĩnh viễn trên dịch vụ. Các bài API và ảnh cần Internet; các bài giao diện và SQLite có thể hoạt động cục bộ.

## 4 Hướng dẫn mở và chạy project
### 4.1 Mở ứng dụng tổng hợp
Trong Android Studio chọn File → Open → D:\Android\Chuong4_AppTongHopNhom7. Chờ Gradle Sync hoàn tất. Dùng SDK tại D:\Android\SDK và JDK đi kèm Android Studio đang cài trên máy. Cấu hình nền được lấy từ project Bai9MonHoc mẫu, không đổi tùy tiện phiên bản Kotlin hoặc plugin Android.
Chọn thiết bị Android hoặc máy ảo đã có, sau đó Run module app. Nếu SDK hoặc thư viện chưa đầy đủ, cần hoàn tất tải thành phần được yêu cầu trước khi chạy. Project tổng hợp tạo một app có nhãn “Nhóm 7 · Demo tổng hợp”.
### 4.2 Project riêng để đối chiếu
Ch2_Bai1_PhepTinh; Ch2_Bai2_ThongKeMang; Ch2_Bai3_CRUDSinhVien.
Bai1SinhVien; Bai4CongViec; Bai9MonHoc: ba project chương 3 nguồn mẫu được giữ nguyên.
Ch5_Bai1_GetPosts; Ch5_Bai2_PostPutDelete; Ch5_Bai3_SQLite.
Ch6_Bai1_TaiAnhLuuURL; Ch6_Bai2_LuuDocAnh; Ch6_Bai3_DanhSachAnh.
Các project riêng dùng để xem mã từng bài hoặc so sánh. Khi trình bày yêu cầu gộp app, chạy Chuong4_AppTongHopNhom7.

## 5 Kịch bản kiểm tra và kết quả cần quan sát
### 5.1 Kiểm tra thanh công cụ và Drawer
Mở app: trang Home hiển thị danh mục demo. Bấm Search: có Toast “Search clicked”. Mở ⋮ và chọn Settings: có Toast “Settings clicked”. Bấm ☰: Drawer trượt ra và hiển thị các nhóm chương. Chọn Home/Profile/Settings trong Drawer: có Toast tương ứng và Drawer đóng.
### 5.2 Kiểm tra điều hướng đủ 12 bài
Lần lượt mở từng bài từ Home, quan sát tiêu đề và giao diện tương ứng, sau đó quay lại danh mục. Thử mở một bài từ Drawer để đối chiếu đường điều hướng. Không kết luận đã kiểm tra đầy đủ chỉ vì một bài mở được; cần kiểm tra cả 12 điểm đến.
### 5.3 Kiểm tra chức năng đại diện
Phép tính: nhập 8 và 10, chọn +, kết quả 18.0; thử chia cho 0 để xem thông báo. Thống kê mảng: nhập 5 5 3, số lớn thứ hai phân biệt là 3. CRUD sinh viên: thêm mã SV01, sửa thông tin rồi xóa theo mã; kiểm tra mã trùng và dữ liệu trống.
Chương 3: quan sát danh sách sinh viên; thêm công việc, đánh dấu và sửa/xóa; chọn môn học và kiểm tra tổng tín chỉ. Chương 5: tải bài viết, mở chi tiết; gửi POST/PUT/DELETE và quan sát HTTP; thêm sinh viên SQLite, mở lại app để kiểm tra dữ liệu vẫn còn.
Chương 6: dùng URL HTTPS hợp lệ để tải; lưu URL, mở lại và đọc; tải ảnh trước khi lưu rồi đọc ảnh đã lưu; cuộn danh sách ảnh để quan sát trạng thái; nhập URL không tải được để kiểm tra thông báo và thao tác thử lại.
### 5.4 Ghi nhận kết quả
Các mô tả ở mục 5 là kịch bản và kết quả cần quan sát. Khi hoàn thiện báo cáo, đối chiếu nhật ký kiểm tra đi kèm project và ảnh chụp thực tế. Không thay ảnh chưa chụp bằng ảnh mẫu của người khác. Những bài mạng còn phụ thuộc phản hồi dịch vụ và kết nối tại thời điểm chạy.

## 6 Ảnh minh chứng thực tế
### 6.1 Màn hình chính của app chung
[CHÈN ẢNH 4.1 TẠI ĐÂY]



Chú thích: Một ứng dụng có danh mục các demo chương 2, 3, 5, 6; chụp rõ tiêu đề và các nút bài.
### 6.2 Thanh công cụ và Toast
[CHÈN ẢNH 4.2a: SEARCH TẠI ĐÂY]



[CHÈN ẢNH 4.2b: SETTINGS TẠI ĐÂY]



Chú thích: Thao tác Search/Settings hiển thị Toast theo bài 1 chương 4.
### 6.3 Navigation Drawer
[CHÈN ẢNH 4.3 TẠI ĐÂY]



Chú thích: Drawer có Home/Profile/Settings, header và các nhóm bài. Nếu danh sách dài, chụp thêm phần dưới sau khi cuộn menu.
### 6.4 Các mục cơ bản trong Drawer
[CHÈN ẢNH 4.4a: HOME TẠI ĐÂY]



[CHÈN ẢNH 4.4b: PROFILE TẠI ĐÂY]



[CHÈN ẢNH 4.4c: SETTINGS TẠI ĐÂY]



Chú thích: Mỗi lựa chọn có Toast tương ứng và nội dung chính thay đổi.
### 6.5 Demo đại diện của các chương trong cùng app
[CHÈN ẢNH 4.5a: MỘT BÀI CHƯƠNG 2 TẠI ĐÂY]



[CHÈN ẢNH 4.5b: MỘT BÀI CHƯƠNG 3 TẠI ĐÂY]



[CHÈN ẢNH 4.5c: MỘT BÀI CHƯƠNG 5 TẠI ĐÂY]



[CHÈN ẢNH 4.5d: MỘT BÀI CHƯƠNG 6 TẠI ĐÂY]



Chú thích: Chụp từ app chung, có tiêu đề bài để xác định đúng điểm đến. Ảnh chức năng của từng bài có thể bổ sung trong phần chương tương ứng khi nhóm hoàn thiện minh chứng.
### 6.6 Quay lại danh mục
[CHÈN ẢNH 4.6 TẠI ĐÂY]



Chú thích: Sau khi chạy một demo, dùng mũi tên hoặc Back để về danh mục rồi mở bài khác trong cùng ứng dụng.

## 7 Kết luận
Bài tập vận dụng Action Bar/Toolbar, menu, Navigation Drawer và Intent để kết nối các demo của nhóm trong một app. Mỗi bài giữ giao diện và chức năng riêng; menu chung giúp lựa chọn và quay lại nhất quán. Việc tách package và đổi đồng bộ tên tài nguyên là bước cần thiết khi tích hợp các project Android có cấu trúc giống nhau.

## 8 Tài liệu tham khảo
[1] Bài giảng “Chương 4 Action Bar – Navigation Drawer”, PDF giảng viên cung cấp, 47 trang; bài 1 ở trang 46 và bài 2 ở trang 47.
[2] Group_7_GK, các thẻ chương 2, 3, 5, 6 và mã nguồn ba project chương 3 mẫu trên máy.
[3] Mã nguồn project Chuong4_AppTongHopNhom7, README.md và nhật ký kiểm tra kèm theo.
