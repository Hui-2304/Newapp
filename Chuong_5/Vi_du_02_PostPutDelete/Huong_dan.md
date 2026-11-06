# Ch5 · POST PUT DELETE

Mở thư mục này bằng Android Studio, chờ Sync rồi Run.
SDK: D:\Android\SDK; JDK: Android Studio Embedded JDK.

Nguồn: Group_7_GK, thẻ Bài tập 2 c5.

BÀI TẬP 2 - CHƯƠNG 5
THÊM, SỬA, XÓA BÀI VIẾT QUA REST API
Thực hành HTTP POST – PUT – DELETE với Retrofit
1. MỤC TIÊU VÀ PHẠM VI
Thực hiện ba thao tác thay đổi dữ liệu REST API: POST tạo bài viết; PUT cập nhật bài viết theo ID; DELETE xóa bài viết theo ID. Mỗi thao tác hiển thị mã trạng thái HTTP và dữ liệu phản hồi.
2. CĂN CỨ TỪ BÀI GIẢNG
Trang trong slide
Nội dung liên quan
9–10
Ý nghĩa và endpoint: POST /posts, PUT /posts/1, DELETE /posts/1.
11–13
Sử dụng API giả lập JSONPlaceholder với collection /posts.
14–27
Cách thêm Retrofit, quyền Internet, model Kotlin, interface ApiService và RetrofitClient.
 
3. CẤU TRÚC BÀI THỰC HÀNH
Tệp thành phần
Vai trò
Post.kt
PostRequest, PostResponse dùng cho gửi và nhận JSON.
ApiService.kt
Ba phương thức @POST, @PUT, @DELETE; nhận @Body và @Path.
RetrofitClient.kt
Khởi tạo Retrofit với baseUrl và GsonConverterFactory.
MainActivity.kt
Đọc form, kiểm tra dữ liệu, gọi API, hiển thị HTTP status và JSON.
activity_main.xml
EditText nhập id/userId/title/body, nút POST/PUT/DELETE, TextView kết quả.
 
4. CÁCH TRIỂN KHAI
4.1. Khai báo thư viện và quyền mạng
Tiếp tục sử dụng cùng mẫu cấu hình Retrofit 3.0.0 và quyền INTERNET như ở Bài 1, phù hợp trang 15–17 của slide.
build.gradle.kts
dependencies {
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}
 
AndroidManifest.xml
<uses-permission android:name="android.permission.INTERNET" />
 
4.2. Model và service cho POST, PUT, DELETE
Slide ghi rõ mục đích và URL của ba HTTP Method. Để hiện thực các thao tác này trong Retrofit, phần thực hành bổ sung @POST, @PUT, @DELETE, @Body và kiểu Response<T>. Đây là phần áp dụng mở rộng, không phải đoạn code đầy đủ có sẵn trong slide.
Post.kt
package vn.ptit.chuong5.bai2
 
data class PostRequest(val userId: Int, val title: String, val body: String)
 
data class PostResponse(val userId: Int?, val id: Int?, val title: String?, val body: String?)
 
ApiService.kt
package vn.ptit.chuong5.bai2
 
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
 
interface ApiService {
	@POST("posts")
	suspend fun createPost(@Body post: PostRequest): Response<PostResponse>
 
	@PUT("posts/{id}")
	suspend fun updatePost(@Path("id") id: Int, @Body post: PostRequest): Response<PostResponse>
 
	@DELETE("posts/{id}")
	suspend fun deletePost(@Path("id") id: Int): Response<Void>
}
 
RetrofitClient.kt
package vn.ptit.chuong5.bai2
 
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
 
4.3. Xử lý sự kiện trên giao diện
MainActivity nhận dữ liệu nhập, kiểm tra userId và id là số dương, kiểm tra title/body khác rỗng, sau đó gọi API trong CoroutineScope. Sau khi nhận Response, chương trình hiển thị HTTP status và nội dung JSON hoặc thông báo lỗi.
MainActivity.kt
package vn.ptit.chuong5.bai2
 
import android.app.Activity
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
 
class MainActivity : Activity() {
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
    	setContentView(R.layout.activity_main)
    	inputUserId = findViewById(R.id.edtUserId)
    	inputPostId = findViewById(R.id.edtPostId)
    	inputTitle = findViewById(R.id.edtTitle)
    	inputBody = findViewById(R.id.edtBody)
    	output = findViewById(R.id.tvResult)
    	progress = findViewById(R.id.progress)
    	val add = findViewById<Button>(R.id.btnPost)
    	val update = findViewById<Button>(R.id.btnPut)
    	val delete = findViewById<Button>(R.id.btnDelete)
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
 
4.4. Bố trí form
Màn hình có 4 ô nhập: User ID, Post ID, Title và Body; ba nút POST, PUT, DELETE; vùng kết quả và ProgressBar. Mã XML hoàn chỉnh có trong activity_main.xml của project.
5. PHÂN TÍCH KẾT QUẢ MONG ĐỢI
Thao tác
Đường dẫn / phản hồi thường gặp
Ý nghĩa
POST
POST /posts → thường HTTP 201 và JSON có ID
Yêu cầu tạo một bài viết giả lập.
PUT
PUT /posts/{id} → thường HTTP 200 và JSON cập nhật
Yêu cầu thay nội dung bài viết có ID cụ thể.
DELETE
DELETE /posts/{id} → thường HTTP 200; body có thể rỗng
Yêu cầu xóa bài viết có ID cụ thể.
 
6. KỊCH BẢN KIỂM THỬ
Thao tác
Minh chứng cần ghi
Nhập userId=1, title="Bai tap 2", body="Thuc hanh POST" rồi POST
Kiểm tra phản hồi HTTP và JSON trả về.
Nhập Post ID=1, sửa title/body rồi PUT
Kiểm tra phản hồi HTTP và các trường trong JSON.
Nhập Post ID=1 rồi DELETE
Kiểm tra phản hồi HTTP; không kết luận server đã xóa vĩnh viễn.
Thử Post ID không hợp lệ hoặc ngắt mạng
Quan sát thông báo lỗi nhập liệu/lỗi kết nối.
 
HƯỚNG DẪN CHẠY VÀ MINH CHỨNG
1.      Mở Android Studio → File → Open → chọn thư mục project Bài 2 tương ứng đã cung cấp.
2.      Chờ Gradle Sync; cài Android SDK Platform 35 và sử dụng JDK 17 nếu môi trường yêu cầu.
3.      Chọn máy ảo Android hoặc điện thoại Android thật rồi nhấn Run.
4.      Thực hiện các tình huống kiểm thử bên dưới và chụp màn hình kết quả THẬT.
5.      Chèn các ảnh vừa chụp vào vị trí dành cho minh chứng trong file Word trước khi nộp.
Vị trí bổ sung ảnh minh chứng
Ảnh 1: Phản hồi khi POST bài viết
Ảnh 2: Phản hồi khi PUT bài viết
Ảnh 3: Phản hồi khi DELETE bài viết
KẾT LUẬN
Bài tập giúp phân biệt ba thao tác HTTP ghi dữ liệu và cách triển khai chúng qua Retrofit. Cấu trúc Post/ApiService/RetrofitClient giữ nguyên tư duy của phần REST API trong slide.
TÀI LIỆU THAM KHẢO
·       Slide “Chương 5 database” do giảng viên cung cấp: RESTful API trong Android và Cơ sở dữ liệu SQLite trong Android (bộ 61 trang, ngày ghi trên slide 12/10/2026).
·       Mã nguồn project Android Studio “Bài 2 - Chương 5” đi kèm báo cáo (các file Kotlin, XML, Gradle).
Dịch vụ JSONPlaceholder: https://js onplaceholder.typicode.com/ (chỉ áp dụng cho Bài 1 và 2).

