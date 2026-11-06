package com.example.bai1sinhvien

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
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
        val ds = listOf(
            SinhVien("Nguyễn Văn An", 20, true),
            SinhVien("Trần Thị Bình", 21, false),
            SinhVien("Lê Minh Huy", 19, true),
            SinhVien("Phạm Ngọc Mai", 20, false)
        )
        val listView = findViewById<ListView>(R.id.lvSinhVien)
        listView.adapter = SinhVienAdapter(this, ds)
        listView.setOnItemClickListener { _, _, position, _ ->
            val sv = ds[position]
            Toast.makeText(this, "${sv.ten} - ${sv.tuoi} tuổi",
                Toast.LENGTH_SHORT).show()
        }
    }
}