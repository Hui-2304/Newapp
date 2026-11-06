package com.group7.ch2.bai2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : DemoActivity() {

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
