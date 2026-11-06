package com.group7.tonghop.demo4
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity

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
            .inflate(R.layout.d4_item_sinhvien, parent, false)
        val sv = requireNotNull(getItem(position))
        row.findViewById<TextView>(R.id.d4_tvTen).text = sv.ten
        row.findViewById<TextView>(R.id.d4_tvTuoi).text = "Tuổi: ${sv.tuoi}"
        row.findViewById<ImageView>(R.id.d4_imgGioiTinh).apply {
            setImageResource(if (sv.laNam) R.drawable.d4_ic_nam else R.drawable.d4_ic_nu)
            contentDescription = if (sv.laNam) "Nam" else "Nữ"
        }
        return row
    }
}