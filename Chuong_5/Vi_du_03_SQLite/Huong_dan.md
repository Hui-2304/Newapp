# Ch5 · SQLite sinh viên

Mở thư mục này bằng Android Studio, chờ Sync rồi Run.
SDK: D:\Android\SDK; JDK: Android Studio Embedded JDK.

Nguồn: Group_7_GK, thẻ Bài tập 3 c5.

BÀI TẬP 3 - CHƯƠNG 5
QUẢN LÝ SINH VIÊN BẰNG SQLITE TRÊN ANDROID
Thực hành SQLiteOpenHelper và thao tác CRUD


1. MỤC TIÊU VÀ PHẠM VI
Tạo ứng dụng lưu thông tin sinh viên ngay trên điện thoại bằng SQLite, gồm: thêm sinh viên, hiển thị danh sách, sửa thông tin và xóa sinh viên. Dữ liệu được lưu trên thiết bị nên không yêu cầu kết nối Internet.
2. CĂN CỨ TỪ BÀI GIẢNG
Trang trong slide
Nội dung liên quan
44–46
SQLite là cơ sở dữ liệu quan hệ tích hợp trong Android; ví dụ quản lý sinh viên.
47–52
Câu lệnh CREATE TABLE, INSERT, SELECT, UPDATE, DELETE với bảng students (id, name, email, age).
53–59
Mô hình Activity → SQLiteOpenHelper → SQLiteDatabase; phương thức onCreate, ContentValues, truy vấn qua Cursor.
 
3. CẤU TRÚC BÀI THỰC HÀNH
Tệp thành phần
Vai trò
Student.kt
Lớp dữ liệu chứa id, name, email, age.
StudentDatabaseHelper.kt
Tạo bảng students, CRUD với SQLiteOpenHelper / SQLiteDatabase.
StudentAdapter.kt
Hiển thị từng sinh viên trong ListView.
MainActivity.kt
Nhập và kiểm tra dữ liệu; sự kiện thêm/sửa/xóa/tải lại.
activity_main.xml, item_student.xml
Giao diện quản lý và trình bày danh sách.
 
4. CƠ SỞ DỮ LIỆU VÀ THIẾT KẾ BẢNG
Theo đúng ví dụ SQL ở trang 48–52, bảng students có các cột id (khóa chính tự tăng), name, email, age. Khi đưa vào ứng dụng, mã thêm NOT NULL để ngăn thiếu dữ liệu tại các cột thông tin.
StudentDatabaseHelper.kt
CREATE TABLE students (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT NOT NULL,
	email TEXT NOT NULL,
	age INTEGER NOT NULL
)
 
5. CÁCH TRIỂN KHAI
5.1. Model Student
Student.kt
package vn.ptit.chuong5.bai3
 
data class Student(
	val id: Long,
	val name: String,
	val email: String,
	val age: Int
)
 
5.2. Xây dựng SQLiteOpenHelper
Tương tự cấu trúc PostDatabaseHelper trong trang 56–58 của slide, lớp StudentDatabaseHelper kế thừa SQLiteOpenHelper. onCreate tạo bảng; ContentValues hỗ trợ INSERT/UPDATE; Cursor đọc danh sách. Code triển khai thêm DELETE và UPDATE từ các câu SQL đã học.
StudentDatabaseHelper.kt
package vn.ptit.chuong5.bai3
 
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
 
class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(
	context, DATABASE_NAME, null, DATABASE_VERSION
) {
	companion object {
    	private const val DATABASE_NAME = "students.db"
    	private const val DATABASE_VERSION = 1
    	private const val TABLE_NAME = "students"
	}
 
	override fun onCreate(db: SQLiteDatabase) {
    	db.execSQL("""
        	CREATE TABLE students (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
            	name TEXT NOT NULL,
            	email TEXT NOT NULL,
            	age INTEGER NOT NULL
        	)
    	""".trimIndent())
	}
 
	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    	// Version 1 chưa có thay đổi lược đồ. Nếu nâng version, hãy tạo migration
    	// tương ứng thay vì DROP TABLE để giữ dữ liệu người dùng.
    	throw IllegalStateException("Chưa có migration cho database $oldVersion -> $newVersion")
	}
 
	fun addStudent(name: String, email: String, age: Int): Long {
    	val values = ContentValues().apply {
        	put("name", name)
        	put("email", email)
        	put("age", age)
    	}
    	return writableDatabase.insertOrThrow(TABLE_NAME, null, values)
	}
 
	fun getAllStudents(): List<Student> {
    	val result = mutableListOf<Student>()
    	readableDatabase.query(
        	TABLE_NAME, arrayOf("id", "name", "email", "age"),
        	null, null, null, null, "id DESC"
    	).use { cursor ->
        	val idCol = cursor.getColumnIndexOrThrow("id")
        	val nameCol = cursor.getColumnIndexOrThrow("name")
        	val emailCol = cursor.getColumnIndexOrThrow("email")
        	val ageCol = cursor.getColumnIndexOrThrow("age")
        	while (cursor.moveToNext()) {
            	result.add(Student(
                	cursor.getLong(idCol), cursor.getString(nameCol),
                	cursor.getString(emailCol), cursor.getInt(ageCol)
            	))
        	}
    	}
    	return result
	}
 
	fun updateStudent(student: Student): Int {
    	val values = ContentValues().apply {
        	put("name", student.name)
        	put("email", student.email)
        	put("age", student.age)
    	}
    	return writableDatabase.update(TABLE_NAME, values, "id = ?", arrayOf(student.id.toString()))
	}
 
	fun deleteStudent(id: Long): Int = writableDatabase.delete(
    	TABLE_NAME, "id = ?", arrayOf(id.toString())
	)
}
 
5.3. Hiển thị danh sách bằng Adapter
StudentAdapter.kt
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
 
5.4. Giao diện và xử lý CRUD trong MainActivity
Mỗi khi thao tác thêm/sửa/xóa hoàn thành, Activity đọc lại SQLite và cập nhật ListView. Tác vụ đọc/ghi database chạy trên Dispatchers.IO để không làm nghẽn luồng giao diện; đây là bổ sung thực hành ngoài ví dụ SQL cơ bản.
MainActivity.kt
package vn.ptit.chuong5.bai3
 
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
 
class MainActivity : Activity() {
	private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
	private lateinit var db: StudentDatabaseHelper
	private lateinit var adapter: StudentAdapter
	private lateinit var nameInput: EditText
	private lateinit var emailInput: EditText
	private lateinit var ageInput: EditText
	private lateinit var status: TextView
	private lateinit var selection: TextView
	private lateinit var buttons: List<Button>
	private var selectedId: Long? = null
 
	override fun onCreate(savedInstanceState: Bundle?) {
    	super.onCreate(savedInstanceState)
    	setContentView(R.layout.activity_main)
    	db = StudentDatabaseHelper(this)
    	nameInput = findViewById(R.id.edtName)
    	emailInput = findViewById(R.id.edtEmail)
    	ageInput = findViewById(R.id.edtAge)
    	status = findViewById(R.id.tvStatus)
    	selection = findViewById(R.id.tvSelection)
    	adapter = StudentAdapter(this)
        findViewById<ListView>(R.id.listStudents).apply {
        	adapter = this@MainActivity.adapter
        	setOnItemClickListener { _, _, position, _ ->
            	val student = this@MainActivity.adapter.getItem(position)
                	?: return@setOnItemClickListener
            	selectedId = student.id
            	nameInput.setText(student.name)
                emailInput.setText(student.email)
                ageInput.setText(student.age.toString())
            	selection.text = "Đang chọn sinh viên ID #${student.id}"
            	status.text = "Bạn có thể sửa thông tin rồi nhấn Cập nhật, hoặc nhấn Xóa."
        	}
    	}
    	val add = findViewById<Button>(R.id.btnAdd)
    	val update = findViewById<Button>(R.id.btnUpdate)
    	val delete = findViewById<Button>(R.id.btnDelete)
    	val refresh = findViewById<Button>(R.id.btnRefresh)
    	val clear = findViewById<Button>(R.id.btnClear)
    	buttons = listOf(add, update, delete, refresh, clear)
 
    	add.setOnClickListener {
        	val form = readForm() ?: return@setOnClickListener
        	execute("Đã thêm sinh viên") {
            	db.addStudent(form.name, form.email, form.age)
        	}
    	}
    	update.setOnClickListener {
        	val id = requireSelectedId() ?: return@setOnClickListener
        	val form = readForm() ?: return@setOnClickListener
        	execute("Đã cập nhật sinh viên ID #$id") {
            	val rows = db.updateStudent(form.copy(id = id))
            	check(rows == 1) { "Không tìm thấy sinh viên để cập nhật" }
        	}
    	}
    	delete.setOnClickListener {
        	val id = requireSelectedId() ?: return@setOnClickListener
        	AlertDialog.Builder(this)
            	.setTitle("Xác nhận xóa")
            	.setMessage("Bạn muốn xóa sinh viên ID #$id khỏi SQLite?")
            	.setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa") { _, _ ->
                	execute("Đã xóa sinh viên ID #$id") {
                    	val rows = db.deleteStudent(id)
                    	check(rows == 1) { "Không tìm thấy sinh viên để xóa" }
                	}
            	}
            	.show()
    	}
    	refresh.setOnClickListener { loadStudents() }
    	clear.setOnClickListener { clearForm() }
    	loadStudents()
	}
 
	private fun requireSelectedId(): Long? {
    	if (selectedId == null) status.text = "Hãy chạm vào sinh viên trong danh sách trước."
    	return selectedId
	}
 
	private fun readForm(): Student? {
    	val name = nameInput.text.toString().trim()
    	val email = emailInput.text.toString().trim()
    	val age = ageInput.text.toString().toIntOrNull()
    	if (name.isBlank()) { nameInput.error = "Hãy nhập họ tên"; return null }
    	if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        	emailInput.error = "Email không hợp lệ"; return null
    	}
    	if (age == null || age !in 1..120) {
        	ageInput.error = "Tuổi phải từ 1 đến 120"; return null
    	}
    	return Student(id = 0L, name = name, email = email, age = age)
	}
 
	private fun setBusy(busy: Boolean) = buttons.forEach { it.isEnabled = !busy }
 
	private fun clearForm() {
    	selectedId = null
    	nameInput.text.clear()
    	emailInput.text.clear()
    	ageInput.text.clear()
    	selection.text = "Chưa chọn sinh viên"
	}
 
	private fun execute(success: String, operation: () -> Unit) {
    	scope.launch {
        	setBusy(true)
        	try {
            	val students = withContext(Dispatchers.IO) {
                	operation()
                	db.getAllStudents()
            	}
            	adapter.replaceAll(students)
            	clearForm()
            	status.text = "$success. Tổng số: ${students.size}."
        	} catch (e: CancellationException) {
            	throw e
        	} catch (e: Exception) {
            	status.text = "Lỗi SQLite: ${e.message.orEmpty()}"
        	} finally {
            	setBusy(false)
        	}
    	}
	}
 
	private fun loadStudents() {
    	scope.launch {
        	setBusy(true)
        	try {
            	val students = withContext(Dispatchers.IO) { db.getAllStudents() }
            	adapter.replaceAll(students)
            	status.text = "Đang lưu trong SQLite: ${students.size} sinh viên."
        	} catch (e: CancellationException) {
            	throw e
        	} catch (e: Exception) {
            	status.text = "Không thể đọc SQLite: ${e.message.orEmpty()}"
        	} finally {
            	setBusy(false)
        	}
    	}
	}
 
	override fun onDestroy() {
    	scope.cancel()
    	db.close()
    	super.onDestroy()
	}
}
 
6. LIÊN HỆ CÁC LỆNH SQL TRONG SLIDE
Lệnh trong slide
Phương thức Kotlin đang dùng
Chức năng trên app
CREATE TABLE students (...)
onCreate(db) / db.execSQL(...)
Tạo bảng trong students.db.
INSERT INTO students (...) VALUES (...)
writableDatabase.insertOrThrow(...)
Nút THÊM.
SELECT * FROM students
readableDatabase.query(...)
Danh sách + TẢI LẠI.
UPDATE students SET ... WHERE id = ...
writableDatabase.update(...)
Nút SỬA.
DELETE FROM students WHERE id = ...
writableDatabase.delete(...)
Nút XÓA.
 
7. KỊCH BẢN KIỂM THỬ
Bước
Dữ liệu nhập / thao tác
Kết quả cần xác minh
1
Thêm: Nguyen Van A, a@example.com, 20
Dòng sinh viên mới xuất hiện trong danh sách.
2
Chạm vào sinh viên, sửa tuổi thành 21, nhấn CẬP NHẬT
Dòng có cùng ID nhưng tuổi thay đổi.
3
Chạm sinh viên, nhấn XÓA, xác nhận
Dòng bị xóa khỏi ListView.
4
Thêm một sinh viên, tắt rồi mở lại app
Sinh viên vẫn có trong SQLite sau khi mở lại.
5
Để trống tên/nhập email sai/tuổi không hợp lệ
Ứng dụng thông báo yêu cầu sửa dữ liệu.
 
PHẠM VI LƯU TRỮ: Dữ liệu SQLite nằm trong bộ nhớ nội bộ của ứng dụng và có thể mất khi gỡ cài đặt hoặc xóa dữ liệu ứng dụng. Không cần quyền INTERNET cho ví dụ này.
HƯỚNG DẪN CHẠY VÀ MINH CHỨNG
1.      Mở Android Studio → File → Open → chọn thư mục project Bài 3 tương ứng đã cung cấp.
2.      Chờ Gradle Sync; cài Android SDK Platform 35 và sử dụng JDK 17 nếu môi trường yêu cầu.
3.      Chọn máy ảo Android hoặc điện thoại Android thật rồi nhấn Run.
4.      Thực hiện các tình huống kiểm thử bên dưới và chụp màn hình kết quả THẬT.
5.      Chèn các ảnh vừa chụp vào vị trí dành cho minh chứng trong file Word trước khi nộp.
Vị trí bổ sung ảnh minh chứng
Ảnh 1: Danh sách sinh viên sau khi thêm
Ảnh 2: Sinh viên được sửa thông tin và cập nhật
Ảnh 3: Danh sách sau khi xóa và khi khởi động lại để xác minh dữ liệu 
KẾT LUẬN
Ứng dụng vận dụng trực tiếp nhóm lệnh SQL và cách quản lý database SQLiteOpenHelper được giảng trong Chương 5. Các chức năng thêm/đọc/sửa/xóa cho thấy một chu trình CRUD cơ bản trên thiết bị Android.
TÀI LIỆU THAM KHẢO
·       Slide “Chương 5 database” do giảng viên cung cấp: RESTful API trong Android và Cơ sở dữ liệu SQLite trong Android (bộ 61 trang, ngày ghi trên slide 12/10/2026).
·       Mã nguồn project Android Studio “Bài 3 - Chương 5” đi kèm báo cáo (các file Kotlin, XML, Gradle).
·       Android SDK SQLite: các lớp SQLiteOpenHelper, SQLiteDatabase và ContentValues được sử dụng trong code mẫu.

