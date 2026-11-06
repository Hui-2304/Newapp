# Ch5 · Danh sách bài viết

Mở thư mục này bằng Android Studio, chờ Sync rồi Run.
SDK: D:\Android\SDK; JDK: Android Studio Embedded JDK.

Nguồn: Group_7_GK, thẻ Bài tập 1 c5.

BÀI TẬP 1 - CHƯƠNG 5
GỌI REST API GET VÀ HIỂN THỊ DANH SÁCH BÀI VIẾT
Thực hành Retrofit – Gson – ListView – xem chi tiết
1. MỤC TIÊU VÀ PHẠM VI
Sử dụng Retrofit để gửi yêu cầu GET đến JSONPlaceholder, ánh xạ dữ liệu JSON thành đối tượng Post và hiển thị danh sách bằng ListView. Khi chọn một dòng, ứng dụng gọi API lấy chi tiết bài viết theo ID.
2. CĂN CỨ TỪ BÀI GIẢNG
Trang trong slide
Nội dung liên quan
6–12
Khái niệm REST API, các HTTP Method, endpoint GET /posts và GET /posts/{id}.
14–27
Cài Retrofit 3.0.0, cấp Internet, Post data class, ApiService, RetrofitClient.
28–43
Hiển thị dữ liệu bằng ListView và PostAdapter, gọi GET bằng Kotlin coroutine.
 
3. CẤU TRÚC BÀI THỰC HÀNH
Tệp thành phần
Vai trò
Post.kt
Model Post, ánh xạ userId, id, title, body từ JSON.
ApiService.kt
Định nghĩa GET /posts và GET /posts/{id} bằng @GET, @Path.
RetrofitClient.kt
Base URL và chuyển đổi Gson.
PostAdapter.kt
Đổ dữ liệu của mỗi bài viết vào item_post.xml.
MainActivity.kt
Gọi API danh sách, gắn adapter, xử lý nhấn vào dòng và lỗi kết nối.
DetailActivity.kt
Lấy bài viết chi tiết theo ID.
activity_main.xml, item_post.xml
Giao diện danh sách bằng ListView và thành phần TextView.
 
4. CÁCH TRIỂN KHAI
4.1. Cài thư viện Retrofit và cấp quyền Internet
Trong file app/build.gradle.kts, dự án sử dụng đúng phiên bản Retrofit và converter-gson 3.0.0 được nêu ở trang 15 của slide, cùng thư viện coroutine để chạy tác vụ bất đồng bộ.
build.gradle.kts
dependencies {
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}
 
AndroidManifest.xml
<uses-permission android:name="android.permission.INTERNET" />
 
4.2. Model, API Service và Retrofit Client
Đúng cấu trúc slide trang 19–27: JSON → Gson → Post; interface khai báo endpoint; Retrofit.Builder() tạo API client.
Post.kt
package vn.ptit.chuong5.bai1
 
data class Post(
	val userId: Int,
	val id: Int,
	val title: String,
	val body: String
)
 
ApiService.kt
package vn.ptit.chuong5.bai1
 
import retrofit2.http.GET
import retrofit2.http.Path
 
interface ApiService {
	@GET("posts")
	suspend fun getPosts(): List<Post>
 
	@GET("posts/{id}")
	suspend fun getPost(@Path("id") id: Int): Post
}
 
RetrofitClient.kt
package vn.ptit.chuong5.bai1
 
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
 
object RetrofitClient {
	private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
 
	val api: ApiService by lazy {
    	Retrofit.Builder()
        	.baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        	.build()
        	.create(ApiService::class.java)
	}
}
 
4.3. Tạo ListView, Adapter và xử lý GET
Slide minh họa dùng ListView/ArrayAdapter; project cũng dùng ListView thay vì RecyclerView để sát phần mã mẫu của thầy. Adapter tái sử dụng convertView để hiển thị title/body.
PostAdapter.kt
package vn.ptit.chuong5.bai1
 
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
        	.inflate(R.layout.item_post, parent, false)
    	val post = getItem(position)!!
        view.findViewById<TextView>(R.id.txtTitle).text = "#${post.id} — ${post.title}"
        view.findViewById<TextView>(R.id.txtBody).text = post.body
    	return view
	}
}
 
MainActivity.kt
package vn.ptit.chuong5.bai1
 
import android.app.Activity
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
 
class MainActivity : Activity() {
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
 
4.4. Xem chi tiết một bài viết
Từ màn hình danh sách, một dòng được chọn sẽ chuyển post.id đến DetailActivity. Màn hình này gọi GET posts/{id} để nhận một Post và hiển thị title, body.
DetailActivity.kt
package vn.ptit.chuong5.bai1
 
import android.app.Activity
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
 
class DetailActivity : Activity() {
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
 
4.5. Thành phần giao diện chính
File activity_main.xml chứa ListView id=listPosts, nút Tải lại và trạng thái tải; item_post.xml có hai TextView txtTitle, txtBody. Giao diện XML đầy đủ được lưu trong project đi kèm.
item_post.xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
	android:background="#FFFFFF"
	android:padding="14dp"
    android:orientation="vertical">
	<TextView android:id="@+id/txtTitle" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:textColor="#164568"
    	android:textSize="16sp" android:textStyle="bold" />
	<TextView android:id="@+id/txtBody" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:textColor="#465363"
    	android:textSize="14sp" android:layout_marginTop="6dp" android:maxLines="3" />
</LinearLayout>
 
5. GIẢI THÍCH VÀ DỰ KIẾN KIỂM THỬ
Luồng xử lý: Nhấn “TẢI DANH SÁCH” → MainActivity gọi getPosts() → Retrofit gửi HTTP GET → Gson giải mã JSON → PostAdapter nhận danh sách → ListView hiển thị. Chạm một bài viết → getPost(id) → chuyển nội dung lên màn hình chi tiết.
Thao tác
Điều cần quan sát
Mở app khi có Internet
Danh sách bài viết và số lượng tải được hiển thị; không nên khẳng định trước số lượng tại mọi thời điểm.
Nhấn một dòng
Màn hình chi tiết hiển thị đúng ID, title, body.
Ngắt mạng và tải lại
Ứng dụng hiển thị thông báo lỗi kết nối, không bị đóng đột ngột.
Bật lại mạng, thử lại
Có thể tải lại danh sách nếu API phản hồi.
 
HƯỚNG DẪN CHẠY VÀ MINH CHỨNG
1.      Mở Android Studio → File → Open → chọn thư mục project Bài 1 tương ứng đã cung cấp.
2.      Chờ Gradle Sync; cài Android SDK Platform 35 và sử dụng JDK 17 nếu môi trường yêu cầu.
3.      Chọn máy ảo Android hoặc điện thoại Android thật rồi nhấn Run.
4.      Thực hiện các tình huống kiểm thử bên dưới và chụp màn hình kết quả THẬT.
5.      Chèn các ảnh vừa chụp vào vị trí dành cho minh chứng trong file Word trước khi nộp.
Vị trí bổ sung ảnh minh chứng
Ảnh 1: Danh sách bài viết lấy từ GET /

Ảnh 2: Chi tiết bài viết từ GET /posts/{id} 
Ảnh 3: Thông báo lỗi khi thử mất kết nối 
KẾT LUẬN
Bài tập minh họa chu trình lấy dữ liệu từ web API vào ứng dụng Android theo đúng thứ tự Model → ApiService → RetrofitClient → Adapter/ListView đã học trong chương.
TÀI LIỆU THAM KHẢO
·       Slide “Chương 5 database” do giảng viên cung cấp: RESTful API trong Android và Cơ sở dữ liệu SQLite trong Android (bộ 61 trang, ngày ghi trên slide 12/10/2026).
·       Mã nguồn project Android Studio “Bài 1 - Chương 5” đi kèm báo cáo (các file Kotlin, XML, Gradle).
Dịch vụ JSONPlaceholder: https://jsonplaceholder.typicode.co 
