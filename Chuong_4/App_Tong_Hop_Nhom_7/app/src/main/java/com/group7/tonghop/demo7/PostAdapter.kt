package com.group7.tonghop.demo7
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity
 
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
 
class PostAdapter(context: Context) : ArrayAdapter<Post>(context, 0, mutableListOf()) {
	fun replaceAll(posts: List<Post>) {
    	clear()
    	addAll(posts)
    	notifyDataSetChanged()
	}
 
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    	val view = convertView ?: LayoutInflater.from(context)
        	.inflate(R.layout.d7_item_post, parent, false)
    	val post = getItem(position)!!
        view.findViewById<TextView>(R.id.d7_txtTitle).text = "#${post.id} — ${post.title}"
        view.findViewById<TextView>(R.id.d7_txtBody).text = post.body
    	return view
	}
}
