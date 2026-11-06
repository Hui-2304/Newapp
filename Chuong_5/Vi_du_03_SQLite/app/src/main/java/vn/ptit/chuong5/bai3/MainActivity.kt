package vn.ptit.chuong5.bai3
 
import androidx.appcompat.app.AppCompatActivity
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
 
class MainActivity : DemoActivity() {
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
