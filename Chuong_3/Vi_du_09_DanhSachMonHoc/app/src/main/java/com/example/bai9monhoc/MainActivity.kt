package com.example.bai9monhoc

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val ds = mutableListOf(
        MonHoc("Lập trình Kotlin", 3),
        MonHoc("Lập trình Android", 3),
        MonHoc("Cơ sở dữ liệu", 3),
        MonHoc("Mạng máy tính", 2),
        MonHoc("Tiếng Anh", 2),
        MonHoc("Kiểm thử phần mềm", 3),
        MonHoc("Cấu trúc dữ liệu", 3),
        MonHoc("Hệ điều hành", 3)
    )
    private val daHoc = mutableListOf<String>()
    private lateinit var daHocAdapter: ArrayAdapter<String>
    private lateinit var tvTong: TextView
    private lateinit var tvRong: TextView

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
        tvTong = findViewById(R.id.tvTongTinChi)
        tvRong = findViewById(R.id.tvRong)
        daHocAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, daHoc)
        findViewById<ListView>(R.id.lvDaHoc).adapter = daHocAdapter
        findViewById<ListView>(R.id.lvMonHoc).adapter =
            MonHocAdapter(this, ds) { capNhatThongKe() }
        capNhatThongKe()
    }

    private fun capNhatThongKe() {
        val selected = ds.filter { it.daHoc }
        daHoc.clear()
        daHoc.addAll(selected.map { "${it.ten} - ${it.soTinChi} tín chỉ" })
        daHocAdapter.notifyDataSetChanged()
        tvTong.text = "Tổng tín chỉ đã học: ${selected.sumOf { it.soTinChi }}"
        tvRong.visibility = if (selected.isEmpty()) View.VISIBLE else View.GONE
    }
}

