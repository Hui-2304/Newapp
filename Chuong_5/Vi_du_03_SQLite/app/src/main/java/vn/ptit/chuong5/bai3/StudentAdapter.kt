package vn.ptit.chuong5.bai3
 
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
        	.inflate(R.layout.item_student, parent, false)
    	val student = getItem(position)!!
        view.findViewById<TextView>(R.id.txtName).text = "#${student.id}  ${student.name}"
        view.findViewById<TextView>(R.id.txtInfo).text = "${student.email}  •  ${student.age} tuổi"
    	return view
	}
}
