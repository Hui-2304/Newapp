package com.example.bai4congviec

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class CongViecAdapter(context: Context, data: MutableList<CongViec>) :
    ArrayAdapter<CongViec>(context, 0, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_congviec, parent, false)
        val cv = requireNotNull(getItem(position))
        val cb = row.findViewById<CheckBox>(R.id.cbHoanThanh)
        val ten = row.findViewById<TextView>(R.id.tvTenCongViec)
        ten.text = cv.ten
        cb.contentDescription = "Hoàn thành công việc ${cv.ten}"
        // Gỡ listener trước khi gán trạng thái cho dòng tái sử dụng.
        cb.setOnCheckedChangeListener(null)
        cb.isChecked = cv.hoanThanh
        ten.setTextColor(if (cv.hoanThanh) Color.GRAY else Color.BLACK)
        cb.setOnCheckedChangeListener { _, checked ->
            cv.hoanThanh = checked
            ten.setTextColor(if (checked) Color.GRAY else Color.BLACK)
        }
        return row
    }
}