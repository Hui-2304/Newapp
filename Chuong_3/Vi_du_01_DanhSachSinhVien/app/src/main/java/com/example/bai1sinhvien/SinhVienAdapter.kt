package com.example.bai1sinhvien

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class SinhVienAdapter(context: Context, data: List<SinhVien>) :
    ArrayAdapter<SinhVien>(context, 0, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_sinhvien, parent, false)
        val sv = requireNotNull(getItem(position))
        row.findViewById<TextView>(R.id.tvTen).text = sv.ten
        row.findViewById<TextView>(R.id.tvTuoi).text = "Tuổi: ${sv.tuoi}"
        row.findViewById<ImageView>(R.id.imgGioiTinh).apply {
            setImageResource(if (sv.laNam) R.drawable.ic_nam else R.drawable.ic_nu)
            contentDescription = if (sv.laNam) "Nam" else "Nữ"
        }
        return row
    }
}