package com.group7.tonghop.demo8
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity
 
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response
 
class MainActivity : DemoActivity() {
	private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
	private val prettyGson = GsonBuilder().setPrettyPrinting().create()
	private lateinit var inputUserId: EditText
	private lateinit var inputPostId: EditText
	private lateinit var inputTitle: EditText
	private lateinit var inputBody: EditText
	private lateinit var output: TextView
	private lateinit var progress: ProgressBar
	private lateinit var buttons: List<Button>
 
	override fun onCreate(savedInstanceState: Bundle?) {
    	super.onCreate(savedInstanceState)
    	setContentView(R.layout.d8_activity_main)
    	inputUserId = findViewById(R.id.d8_edtUserId)
    	inputPostId = findViewById(R.id.d8_edtPostId)
    	inputTitle = findViewById(R.id.d8_edtTitle)
    	inputBody = findViewById(R.id.d8_edtBody)
    	output = findViewById(R.id.d8_tvResult)
    	progress = findViewById(R.id.d8_progress)
    	val add = findViewById<Button>(R.id.d8_btnPost)
    	val update = findViewById<Button>(R.id.d8_btnPut)
    	val delete = findViewById<Button>(R.id.d8_btnDelete)
    	buttons = listOf(add, update, delete)
 
    	add.setOnClickListener {
        	val body = readRequest() ?: return@setOnClickListener
        	runRequest("POST /posts") { RetrofitClient.api.createPost(body) }
    	}
    	update.setOnClickListener {
        	val id = readPostId() ?: return@setOnClickListener
        	val body = readRequest() ?: return@setOnClickListener
        	runRequest("PUT /posts/$id") { RetrofitClient.api.updatePost(id, body) }
    	}
    	delete.setOnClickListener {
        	val id = readPostId() ?: return@setOnClickListener
        	runRequest("DELETE /posts/$id") { RetrofitClient.api.deletePost(id) }
    	}
	}
 
	private fun readPostId(): Int? {
    	val id = inputPostId.text.toString().toIntOrNull()
    	if (id == null || id < 1) {
        	inputPostId.error = "ID bài viết phải là số nguyên dương"
        	return null
    	}
    	return id
	}
 
	private fun readRequest(): PostRequest? {
    	val userId = inputUserId.text.toString().toIntOrNull()
    	val title = inputTitle.text.toString().trim()
    	val body = inputBody.text.toString().trim()
    	if (userId == null || userId < 1) {
        	inputUserId.error = "User ID phải là số nguyên dương"
        	return null
    	}
    	if (title.isEmpty()) {
        	inputTitle.error = "Hãy nhập tiêu đề"
        	return null
    	}
    	if (body.isEmpty()) {
        	inputBody.error = "Hãy nhập nội dung"
        	return null
    	}
    	return PostRequest(userId, title, body)
	}
 
	private fun runRequest(method: String, request: suspend () -> Response<*>) {
    	scope.launch {
        	buttons.forEach { it.isEnabled = false }
        	progress.visibility = View.VISIBLE
        	output.text = "Đang gửi $method ..."
        	try {
            	val response = request()
            	val body = response.body()
            	val display = when {
                	!response.isSuccessful -> "LỖI HTTP ${response.code()}\n${response.errorBody()?.string().orEmpty()}"
                	body == null -> "THÀNH CÔNG — HTTP ${response.code()} (không có dữ liệu trả về)"
                	else -> "THÀNH CÔNG — HTTP ${response.code()}\n${prettyGson.toJson(body)}"
            	}
            	output.text = "$method\n$display"
        	} catch (e: CancellationException) {
            	throw e
        	} catch (e: Exception) {
            	output.text = "$method\nLỗi kết nối: ${e.message.orEmpty()}"
        	} finally {
            	progress.visibility = View.GONE
            	buttons.forEach { it.isEnabled = true }
        	}
    	}
	}
 
	override fun onDestroy() {
    	scope.cancel()
    	super.onDestroy()
	}
}
