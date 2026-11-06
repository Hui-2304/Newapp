package com.example.bai4congviec

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val ds = mutableListOf(
        CongViec("Đọc lý thuyết chương 3"),
        CongViec("Làm bài tập ListView")
    )
    private lateinit var adapter: CongViecAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val padding = (16 * resources.displayMetrics.density).toInt()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left + padding, bars.top + padding,
                bars.right + padding, bars.bottom + padding)
            insets
        }
        val edt = findViewById<EditText>(R.id.edtCongViec)
        val lv = findViewById<ListView>(R.id.lvCongViec)
        adapter = CongViecAdapter(this, ds)
        lv.adapter = adapter

        findViewById<Button>(R.id.btnThem).setOnClickListener {
            val ten = edt.text.toString().trim()
            if (ten.isBlank()) {
                edt.error = "Vui lòng nhập tên công việc"
                edt.requestFocus()
            } else {
                ds.add(CongViec(ten))
                adapter.notifyDataSetChanged()
                edt.text.clear()
            }
        }
        lv.setOnItemClickListener { _, _, position, _ -> suaCongViec(ds[position]) }
        lv.setOnItemLongClickListener { _, _, position, _ ->
            val cv = ds[position]
            AlertDialog.Builder(this)
                .setTitle("Xóa công việc")
                .setMessage("Bạn có muốn xóa \"${cv.ten}\" không?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa") { _, _ ->
                    ds.removeAt(position)
                    adapter.notifyDataSetChanged()
                }.show()
            true
        }
    }

    private fun suaCongViec(cv: CongViec) {
        val input = EditText(this).apply {
            setText(cv.ten)
            setSelection(text.length)
            maxLines = 1
            filters = arrayOf(android.text.InputFilter.LengthFilter(100))
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle("Sửa công việc")
            .setView(input)
            .setNegativeButton("Hủy", null)
            .setPositiveButton("Lưu", null)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val ten = input.text.toString().trim()
                if (ten.isBlank()) {
                    input.error = "Tên công việc không được để trống"
                } else {
                    cv.ten = ten
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}