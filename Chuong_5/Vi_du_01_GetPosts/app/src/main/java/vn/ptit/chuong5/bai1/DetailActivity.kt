package vn.ptit.chuong5.bai1
 
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
 
class DetailActivity : DemoActivity() {
	companion object { const val EXTRA_POST_ID = "post_id" }
	private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
 
	override fun onCreate(savedInstanceState: Bundle?) {
    	super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    	val id = intent.getIntExtra(EXTRA_POST_ID, -1)
    	if (id < 1) {
            findViewById<TextView>(R.id.tvTitle).text = "ID bài viết không hợp lệ"
        	return
    	}
    	val progress = findViewById<ProgressBar>(R.id.progressDetail)
    	val title = findViewById<TextView>(R.id.tvTitle)
    	val body = findViewById<TextView>(R.id.tvBody)
    	progress.visibility = View.VISIBLE
    	title.text = "Đang tải bài #$id..."
    	scope.launch {
        	try {
            	val post = RetrofitClient.api.getPost(id)
            	title.text = "Bài #${post.id}: ${post.title}"
            	body.text = "Tác giả (userId): ${post.userId}\n\n${post.body}"
        	} catch (e: CancellationException) {
            	throw e
        	} catch (e: Exception) {
            	title.text = "Không tải được bài viết"
            	body.text = "Lỗi: ${e.message.orEmpty()}"
        	} finally {
            	progress.visibility = View.GONE
        	}
    	}
	}
 
	override fun onDestroy() {
    	scope.cancel()
    	super.onDestroy()
	}
}
