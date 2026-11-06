# Ch2 · CRUD sinh viên

Mở thư mục này bằng Android Studio, chờ Sync rồi Run.
SDK: D:\Android\SDK; JDK: Android Studio Embedded JDK.

Nguồn: Group_7_GK, thẻ Bài tập 3 Ch2.

Ví dụ 3. Tạo lớp SinhVien và thực hiện CRUD
1. Đề bài
Tạo lớp SinhVien gồm:
Mã sinh viên.
Họ tên.
Tuổi.
Thực hiện các chức năng CRUD:
Create: Thêm sinh viên.
Read: Hiển thị danh sách.
Update: Cập nhật sinh viên theo mã.
Delete: Xóa sinh viên theo mã.
Ví dụ sử dụng dữ liệu có sẵn để dễ theo dõi từng thao tác.
2. Kiến thức vận dụng
Lớp, đối tượng, constructor, init, this, data class, MutableList, hàm, vòng lặp, điều kiện, lambda và nullable.
3. Ý tưởng
Dùng data class để biểu diễn sinh viên.
Dùng MutableList để quản lý danh sách.
Mỗi chức năng CRUD được viết thành một phương thức.
Kiểm tra trùng mã khi thêm.
Dùng find để tìm sinh viên; kết quả có thể null.
Kiểm tra dữ liệu trước khi cập nhật.
4. Chương trình activity_main.xml
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
       android:text="QUẢN LÝ SINH VIÊN"
       android:textSize="24sp"
       android:textStyle="bold"
       android:gravity="center"
       android:layout_marginBottom="16dp" />

   <EditText
       android:id="@+id/edtMa"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:hint="Mã sinh viên"
       android:inputType="text"
       android:singleLine="true"
       android:minHeight="48dp" />

   <EditText
       android:id="@+id/edtTen"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:hint="Họ tên"
       android:inputType="textPersonName"
       android:singleLine="true"
       android:minHeight="48dp" />

   <EditText
       android:id="@+id/edtTuoi"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:hint="Tuổi"
       android:inputType="number"
       android:minHeight="48dp" />

   <LinearLayout
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:orientation="horizontal"
       android:layout_marginTop="12dp">

       <Button
           android:id="@+id/btnThem"
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_weight="1"
           android:text="Thêm" />

       <Button
           android:id="@+id/btnSua"
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_weight="1"
           android:text="Sửa" />

       <Button
           android:id="@+id/btnXoa"
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_weight="1"
           android:text="Xóa" />

   </LinearLayout>

   <Button
       android:id="@+id/btnHienThi"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:text="Hiển thị danh sách" />

   <TextView
       android:id="@+id/txtThongBao"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:text="Nhập thông tin để thêm sinh viên."
       android:layout_marginTop="12dp"
       android:textSize="16sp" />

   <ScrollView
       android:layout_width="match_parent"
       android:layout_height="0dp"
       android:layout_weight="1"
       android:layout_marginTop="16dp">

       <TextView
           android:id="@+id/txtDanhSach"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:text="Danh sách trống."
           android:textSize="18sp"
           android:textIsSelectable="true" />

   </ScrollView>

</LinearLayout>


5. Chương trình MainActivity.kt
package com.example.quanlysinhvien

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

   // Danh sách dùng chung cho các chức năng.
   private val danhSach = mutableListOf<SinhVien>()

   private lateinit var edtMa: EditText
   private lateinit var edtTen: EditText
   private lateinit var edtTuoi: EditText
   private lateinit var txtThongBao: TextView
   private lateinit var txtDanhSach: TextView

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

       // Liên kết giao diện.
       edtMa = findViewById(R.id.edtMa)
       edtTen = findViewById(R.id.edtTen)
       edtTuoi = findViewById(R.id.edtTuoi)
       txtThongBao = findViewById(R.id.txtThongBao)
       txtDanhSach = findViewById(R.id.txtDanhSach)

       val btnThem = findViewById<Button>(R.id.btnThem)
       val btnSua = findViewById<Button>(R.id.btnSua)
       val btnXoa = findViewById<Button>(R.id.btnXoa)
       val btnHienThi = findViewById<Button>(R.id.btnHienThi)

       btnThem.setOnClickListener {
           themSinhVien()
       }

       btnSua.setOnClickListener {
           suaSinhVien()
       }

       btnXoa.setOnClickListener {
           xoaSinhVien()
       }

       btnHienThi.setOnClickListener {
           hienThiDanhSach()
       }
   }

   // Đọc và kiểm tra thông tin trên giao diện.
   private fun docThongTin(): SinhVien? {
       val ma = edtMa.text.toString().trim()
       val ten = edtTen.text.toString().trim()
       val tuoi = edtTuoi.text.toString().trim().toIntOrNull()

       if (ma.isBlank() || ten.isBlank()) {
           txtThongBao.text = "Mã sinh viên và họ tên không được trống."
           return null
       }

       if (tuoi == null || tuoi <= 0) {
           txtThongBao.text = "Tuổi phải là số nguyên lớn hơn 0."
           return null
       }

       return SinhVien(ma, ten, tuoi)
   }

   // CREATE: thêm sinh viên.
   private fun themSinhVien() {
       val sv = docThongTin() ?: return

       val daTonTai = danhSach.any { it.maSV == sv.maSV }

       if (daTonTai) {
           txtThongBao.text = "Mã ${sv.maSV} đã tồn tại."
           return
       }

       danhSach.add(sv)
       txtThongBao.text = "Đã thêm sinh viên ${sv.maSV}."

       hienThiDanhSach()
       xoaOInput()
   }

   // READ: hiển thị danh sách.
   private fun hienThiDanhSach() {
       if (danhSach.isEmpty()) {
           txtDanhSach.text = "Danh sách trống."
           return
       }

       val noiDung = StringBuilder()

       for ((index, sv) in danhSach.withIndex()) {
           noiDung.append("${index + 1}. Mã: ${sv.maSV}\n")
           noiDung.append("Họ tên: ${sv.hoTen}\n")
           noiDung.append("Tuổi: ${sv.tuoi}\n\n")
       }

       txtDanhSach.text = noiDung.toString()
   }

   // UPDATE: cập nhật theo mã sinh viên.
   private fun suaSinhVien() {
       val thongTinMoi = docThongTin() ?: return

       val sv = danhSach.find {
           it.maSV == thongTinMoi.maSV
       }

       if (sv == null) {
           txtThongBao.text = "Không tìm thấy mã ${thongTinMoi.maSV}."
           return
       }

       sv.hoTen = thongTinMoi.hoTen
       sv.tuoi = thongTinMoi.tuoi

       txtThongBao.text = "Đã cập nhật sinh viên ${sv.maSV}."

       hienThiDanhSach()
       xoaOInput()
   }

   // DELETE: xóa theo mã sinh viên.
   private fun xoaSinhVien() {
       val ma = edtMa.text.toString().trim()

       if (ma.isBlank()) {
           txtThongBao.text = "Nhập mã sinh viên cần xóa."
           return
       }

       val sv = danhSach.find { it.maSV == ma }

       if (sv == null) {
           txtThongBao.text = "Không tìm thấy mã $ma."
           return
       }

       danhSach.remove(sv)
       txtThongBao.text = "Đã xóa sinh viên $ma."

       hienThiDanhSach()
       xoaOInput()
   }

   // Xóa nội dung các ô sau khi thao tác thành công.
   private fun xoaOInput() {
       edtMa.text.clear()
       edtTen.text.clear()
       edtTuoi.text.clear()
       edtMa.requestFocus()
   }
}

6 Chương trình SinhVien.kt
package com.example.quanlysinhvien

data class SinhVien(
   val maSV: String,
   var hoTen: String,
   var tuoi: Int
)

7. Kết quả



Thêm sinh viên:




Sửa lại:



