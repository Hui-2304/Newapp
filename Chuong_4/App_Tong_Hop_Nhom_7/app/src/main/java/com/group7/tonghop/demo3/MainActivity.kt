package com.group7.tonghop.demo3
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : DemoActivity() {

   // Danh sách dùng chung cho các chức năng.
   private val danhSach = mutableListOf<SinhVien>()

   private lateinit var edtMa: EditText
   private lateinit var edtTen: EditText
   private lateinit var edtTuoi: EditText
   private lateinit var txtThongBao: TextView
   private lateinit var txtDanhSach: TextView

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.d3_activity_main)

       // Chừa khoảng trống cho các thanh hệ thống.
       val mainView = findViewById<android.view.View>(R.id.d3_main)
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
       edtMa = findViewById(R.id.d3_edtMa)
       edtTen = findViewById(R.id.d3_edtTen)
       edtTuoi = findViewById(R.id.d3_edtTuoi)
       txtThongBao = findViewById(R.id.d3_txtThongBao)
       txtDanhSach = findViewById(R.id.d3_txtDanhSach)

       val btnThem = findViewById<Button>(R.id.d3_btnThem)
       val btnSua = findViewById<Button>(R.id.d3_btnSua)
       val btnXoa = findViewById<Button>(R.id.d3_btnXoa)
       val btnHienThi = findViewById<Button>(R.id.d3_btnHienThi)

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
