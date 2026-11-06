# Ch2 · Máy tính đơn giản

Mở thư mục này bằng Android Studio, chờ Sync rồi Run.
SDK: D:\Android\SDK; JDK: Android Studio Embedded JDK.

Nguồn: Group_7_GK, thẻ Bài tập 1 Ch2.

Ví dụ 1. Hàm PhepTinh thực hiện các phép toán
1. Đề bài
Nhập hai số nguyên a, b và một phép toán trong các phép +, -, *, /, %. Viết hàm PhepTinh thực hiện phép toán và in kết quả.
Không thực hiện chia hoặc lấy dư khi b = 0.
2. Kiến thức vận dụng
Biến, kiểu dữ liệu, nhập xuất, toán tử, if, when, hàm và xử lý null.
3. Ý tưởng
Chuyển dữ liệu nhập thành số bằng toIntOrNull().
Kiểm tra dữ liệu và phép toán.
Sử dụng when để lựa chọn phép tính.
Chuyển sang Double khi chia để nhận kết quả thực.
Trả về null nếu không thực hiện được phép toán.
4. Chương trình
4.1. Thiết kế giao diện trong activity_main.xml 




4.2. Viết chương trình trong MainActivity.kt 



4.3 Kết quả
Phép cộng:


Phép trừ:











Phép nhân:










Phép chia:












Chia lấy dư:


