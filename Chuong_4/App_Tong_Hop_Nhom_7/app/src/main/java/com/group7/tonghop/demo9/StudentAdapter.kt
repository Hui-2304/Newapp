package com.group7.tonghop.demo9
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity
 
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
 
class StudentAdapter(context: Context) : ArrayAdapter<Student>(context, 0, mutableListOf()) {
	fun replaceAll(students: List<Student>) {
    	clear()
    	addAll(students)
    	notifyDataSetChanged()
	}
 
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    	val view = convertView ?: LayoutInflater.from(context)
        	.inflate(R.layout.d9_item_student, parent, false)
    	val student = getItem(position)!!
        view.findViewById<TextView>(R.id.d9_txtName).text = "#${student.id}  ${student.name}"
        view.findViewById<TextView>(R.id.d9_txtInfo).text = "${student.email}  •  ${student.age} tuổi"
    	return view
	}
}
