package com.group7.ch2.bai1
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
class MainActivity : DemoActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState); setContentView(R.layout.activity_main)
  val a=findViewById<EditText>(R.id.edtA);val b=findViewById<EditText>(R.id.edtB)
  val spinner=findViewById<Spinner>(R.id.spPhepTinh);val result=findViewById<TextView>(R.id.txtKetQua)
  spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listOf("+","-","*","/","%"))
  findViewById<Button>(R.id.btnTinh).setOnClickListener {
   val x=a.text.toString().toIntOrNull();val y=b.text.toString().toIntOrNull()
   if(x==null||y==null){result.text="Vui lòng nhập hai số nguyên hợp lệ.";return@setOnClickListener}
   val op=spinner.selectedItem.toString();val value=phepTinh(x,y,op)
   result.text=if(value==null) "Không thể chia hoặc lấy dư cho 0." else "Kết quả: $x $op $y = $value"
  }
 }
 fun phepTinh(a:Int,b:Int,op:String):Double?=when(op){
  "+"->a.toDouble()+b;"-"->a.toDouble()-b;"*"->a.toDouble()*b
  "/"->if(b!=0)a.toDouble()/b else null;"%"->if(b!=0)a.toDouble()%b else null;else->null
 }
}
