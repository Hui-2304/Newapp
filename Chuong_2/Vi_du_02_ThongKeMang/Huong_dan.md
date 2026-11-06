# Ch2 · Thống kê mảng

Mở thư mục này bằng Android Studio, chờ Sync rồi Run.
SDK: D:\Android\SDK; JDK: Android Studio Embedded JDK.

Nguồn: Group_7_GK, thẻ Bài tập 2 Ch2.

Ví dụ 2. Nhập mảng và thống kê các phần tử
1. Đề bài
Nhập một mảng gồm n số nguyên, với n > 0. Thực hiện:
Tìm số lớn nhất, nhỏ nhất.
Tính tổng và trung bình cộng.
Tính tổng các phần tử chẵn.
Tìm số lớn thứ hai phân biệt trong mảng.
Ví dụ: Với mảng 5, 5, 3, số lớn thứ hai phân biệt là 3.
2. Kiến thức vận dụng
Mảng, danh sách, do...while, for, hàm, lambda, filter, sắp xếp và xử lý null.
3. Ý tưởng
Dùng một hàm nhập số nguyên để yêu cầu nhập lại khi sai.
Lưu dữ liệu vào mảng.
Dùng các hàm có sẵn để thống kê.
Lọc số chẵn bằng điều kiện it % 2 == 0.
Loại phần tử trùng, sắp xếp giảm dần và lấy phần tử có chỉ số 1 để tìm số lớn thứ hai phân biệt.
4 Chương trình trong activity_main.xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:id="@+id/main"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   android:orientation="vertical"
   android:padding="24dp">

   <TextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:text="THỐNG KÊ MẢNG SỐ NGUYÊN"
       android:textSize="22sp"
       android:textStyle="bold"
       android:gravity="center"
       android:layout_marginBottom="24dp" />

   <TextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:text="Nhập các số nguyên, cách nhau bằng khoảng trắng."
       android:textSize="16sp" />

   <EditText
       android:id="@+id/edtMang"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:hint="Ví dụ: 10 5 20 8 15"
       android:inputType="text"
       android:singleLine="true"
       android:minHeight="48dp"
       android:layout_marginTop="12dp" />

   <Button
       android:id="@+id/btnThongKe"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:text="Thống kê"
       android:layout_marginTop="16dp" />

   <ScrollView
       android:layout_width="match_parent"
       android:layout_height="0dp"
       android:layout_weight="1"
       android:layout_marginTop="20dp">

       <TextView
           android:id="@+id/txtKetQua"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:text="Kết quả sẽ hiển thị tại đây."
           android:textSize="18sp"
           android:textIsSelectable="true" />

   </ScrollView>

</LinearLayout>

5. Chương trình trong MainActivity.kt
package com.example.thongkemang

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

       // Chừa khoảng trống cho các thanh hệ thống.
       val mainView = findViewById<android.view.View>(R.id.main)
       val padding = (24 * resources.displayMetrics.density).toInt()

       ViewCompat.setOnApplyWindowInsetsListener(mainView) { view, insets ->
           val bars = insets.getInsets(
               WindowInsetsCompat.Type.systemBars()
           )

           view.setPadding(
               padding + bars.left,
               padding + bars.top,
               padding + bars.right,
               padding + bars.bottom
           )

           insets
       }

       // Liên kết với các thành phần giao diện.
       val edtMang = findViewById<EditText>(R.id.edtMang)
       val btnThongKe = findViewById<Button>(R.id.btnThongKe)
       val txtKetQua = findViewById<TextView>(R.id.txtKetQua)

       // Xử lý khi bấm nút Thống kê.
       btnThongKe.setOnClickListener {
           val noiDung = edtMang.text.toString().trim()

           if (noiDung.isEmpty()) {
               txtKetQua.text = "Vui lòng nhập ít nhất một số nguyên."
               return@setOnClickListener
           }

           // Tách nội dung theo một hoặc nhiều khoảng trắng.
           val cacPhanTu = noiDung.split(Regex("\\s+"))

           // Tạo mảng có số phần tử tương ứng.
           val mang = Array(cacPhanTu.size) { 0 }

           // Kiểm tra và chuyển từng phần tử thành số nguyên.
           for (i in cacPhanTu.indices) {
               val so = cacPhanTu[i].toIntOrNull()

               if (so == null) {
                   txtKetQua.text =
                       "Phần tử thứ ${i + 1} không phải số nguyên hợp lệ: " +
                               cacPhanTu[i]

                   return@setOnClickListener
               }

               mang[i] = so
           }

           // Gọi hàm thống kê và hiển thị kết quả.
           txtKetQua.text = thongKeMang(mang)
       }
   }

   fun thongKeMang(mang: Array<Int>): String {
       if (mang.isEmpty()) {
           return "Mảng không có phần tử."
       }

       val lonNhat = mang.maxOrNull()
       val nhoNhat = mang.minOrNull()

       // Tính tổng bằng Long để giảm nguy cơ tràn số Int.
       val tong = mang.sumOf { it.toLong() }
       val trungBinh = mang.average()

       // Lọc các số chẵn và tính tổng.
       val soChan = mang.filter { it % 2 == 0 }
       val tongChan = soChan.sumOf { it.toLong() }

       // Loại số trùng, sắp xếp giảm dần.
       val danhSachGiam = mang.distinct().sortedDescending()
       val lonThuHai = danhSachGiam.getOrNull(1)

       val chuoiSoChan = if (soChan.isEmpty()) {
           "Không có"
       } else {
           soChan.joinToString()
       }

       val chuoiLonThuHai = lonThuHai?.toString() ?: "Không có"

       return """
           Mảng: ${mang.joinToString()}
           Số phần tử: ${mang.size}
           Số lớn nhất: $lonNhat
           Số nhỏ nhất: $nhoNhat
           Tổng: $tong
           Trung bình cộng: $trungBinh
           Các số chẵn: $chuoiSoChan
           Tổng các số chẵn: $tongChan
           Số lớn thứ hai phân biệt: $chuoiLonThuHai
       """.trimIndent()
   }
}


6. Kết quả:







