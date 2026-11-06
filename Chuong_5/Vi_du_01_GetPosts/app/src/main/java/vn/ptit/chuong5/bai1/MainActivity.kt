package vn.ptit.chuong5.bai1
 
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
 
class MainActivity : DemoActivity() {
	private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
	private lateinit var adapter: PostAdapter
	private lateinit var progress: ProgressBar
	private lateinit var status: TextView
	private lateinit var loadButton: Button
 
	override fun onCreate(savedInstanceState: Bundle?) {
    	super.onCreate(savedInstanceState)
    	setContentView(R.layout.activity_main)
 
    	progress = findViewById(R.id.progress)
    	status = findViewById(R.id.tvStatus)
    	loadButton = findViewById(R.id.btnLoad)
    	adapter = PostAdapter(this)
        findViewById<ListView>(R.id.listPosts).apply {
        	adapter = this@MainActivity.adapter
        	setOnItemClickListener { _, _, position, _ ->
            	val post = this@MainActivity.adapter.getItem(position) ?: return@setOnItemClickListener
                startActivity(Intent(this@MainActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_POST_ID, post.id))
        	}
    	}
    	loadButton.setOnClickListener { loadPosts() }
    	loadPosts()
	}
 
	private fun loadPosts() {
    	scope.launch {
        	loadButton.isEnabled = false
        	progress.visibility = View.VISIBLE
        	status.text = "Đang tải danh sách từ API..."
        	try {
            	val posts = RetrofitClient.api.getPosts()
            	adapter.replaceAll(posts)
            	status.text = "Đã tải ${posts.size} bài viết. Nhấn vào một bài để xem chi tiết."
        	} catch (e: CancellationException) {
            	throw e
        	} catch (e: Exception) {
            	status.text = "Không thể tải dữ liệu. Kiểm tra Internet rồi thử lại. ${e.message.orEmpty()}"
        	} finally {
            	progress.visibility = View.GONE
            	loadButton.isEnabled = true
        	}
    	}
	}
 
	override fun onDestroy() {
    	scope.cancel()
    	super.onDestroy()
	}
}
