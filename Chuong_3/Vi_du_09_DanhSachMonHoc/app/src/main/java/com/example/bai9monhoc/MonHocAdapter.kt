package com.example.bai9monhoc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class MonHocAdapter(
    context: Context,
    data: MutableList<MonHoc>,
    private val onChanged: () -> Unit
) : ArrayAdapter<MonHoc>(context, 0, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_monhoc, parent, false)
        val mon = requireNotNull(getItem(position))
        row.findViewById<TextView>(R.id.tvTenMon).text = mon.ten
        row.findViewById<TextView>(R.id.tvTinChi).text = "${mon.soTinChi} tín chỉ"
        val cb = row.findViewById<CheckBox>(R.id.cbDaHoc)
        cb.contentDescription = "Đã học môn ${mon.ten}"
        cb.setOnCheckedChangeListener(null)
        cb.isChecked = mon.daHoc
        cb.setOnCheckedChangeListener { _, checked ->
            mon.daHoc = checked
            onChanged()
        }
        return row
    }
}

