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
