package com.group7.tonghop
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.activity.OnBackPressedCallback
class MainActivity:AppCompatActivity(){
 private lateinit var drawer:DrawerLayout
 private lateinit var body:LinearLayout
 private val demos=listOf(
        "Ch2 · Máy tính đơn giản" to com.group7.tonghop.demo1.MainActivity::class.java,
        "Ch2 · Thống kê mảng" to com.group7.tonghop.demo2.MainActivity::class.java,
        "Ch2 · CRUD sinh viên" to com.group7.tonghop.demo3.MainActivity::class.java,
        "Ch3 · Danh sách sinh viên" to com.group7.tonghop.demo4.MainActivity::class.java,
        "Ch3 · Quản lý công việc" to com.group7.tonghop.demo5.MainActivity::class.java,
        "Ch3 · Danh sách môn học" to com.group7.tonghop.demo6.MainActivity::class.java,
        "Ch5 · Danh sách bài viết" to com.group7.tonghop.demo7.MainActivity::class.java,
        "Ch5 · POST PUT DELETE" to com.group7.tonghop.demo8.MainActivity::class.java,
        "Ch5 · SQLite sinh viên" to com.group7.tonghop.demo9.MainActivity::class.java,
        "Ch6 · Tải ảnh và lưu URL" to com.group7.tonghop.demo10.MainActivity::class.java,
        "Ch6 · Lưu và đọc ảnh" to com.group7.tonghop.demo11.MainActivity::class.java,
        "Ch6 · Danh sách ảnh" to com.group7.tonghop.demo12.MainActivity::class.java
 )
 override fun onCreate(state:Bundle?){
  super.onCreate(state)
  drawer=DrawerLayout(this)
  val column=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL}
  val toolbar=Toolbar(this).apply{title="Nhóm 7 · Demo tổng hợp";setBackgroundColor(0xFF6750A4.toInt());setTitleTextColor(-1)}
  column.addView(toolbar,LinearLayout.LayoutParams(-1,(56*resources.displayMetrics.density).toInt()))
  val scroll=ScrollView(this);body=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,16,24,16)};scroll.addView(body);column.addView(scroll,LinearLayout.LayoutParams(-1,0,1f));drawer.addView(column,DrawerLayout.LayoutParams(-1,-1))
  val nav=NavigationView(this).apply { setBackgroundColor(-1) };drawer.setDrawerElevation(0f)
  val header=TextView(this).apply{text="NHÓM 7\nAndroid · Kotlin\nCác bài demo chương 2, 3, 5, 6";textSize=20f;setPadding(24,48,24,24)};nav.addHeaderView(header)
  val menu=nav.menu
  menu.add(0,1,0,"Home");menu.add(0,2,1,"Profile");menu.add(0,3,2,"Settings")
  for((chapterIndex,ch) in listOf("Ch2","Ch3","Ch5","Ch6").withIndex()){
   val group=menu.addSubMenu(0,200+chapterIndex,10+chapterIndex,"Chương "+ch.removePrefix("Ch"))
   demos.forEachIndexed{i,p->if(p.first.startsWith(ch))group.add(1,100+i,i,p.first.substringAfter(" · "))}
  }
  drawer.addView(nav,DrawerLayout.LayoutParams((300*resources.displayMetrics.density).toInt(),-1,GravityCompat.START))
  val outer=FrameLayout(this);outer.addView(drawer);setContentView(outer);setSupportActionBar(toolbar)
  val toggle=ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close);drawer.addDrawerListener(toggle);toggle.syncState();toggle.drawerArrowDrawable.color=-1
  ViewCompat.setOnApplyWindowInsetsListener(outer){v,i->val bars=i.getInsets(WindowInsetsCompat.Type.systemBars());v.setPadding(bars.left,bars.top,bars.right,bars.bottom);WindowInsetsCompat.CONSUMED}
  nav.setNavigationItemSelectedListener{item->drawer.closeDrawer(GravityCompat.START);when(item.itemId){1->{toast("Home");home()};2->{toast("Profile");info("Nhóm 7", "Ứng dụng tích hợp bài demo của nhóm.\nChương 4: Action Bar, Toolbar và Navigation Drawer.")};3->{toast("Settings");info("Settings", "Các bài mạng và ảnh cần Internet.\nDữ liệu SQLite và URL được lưu trên thiết bị.")};else->open(item.itemId-100)};true}
  onBackPressedDispatcher.addCallback(this,object:OnBackPressedCallback(true){override fun handleOnBackPressed(){if(drawer.isDrawerOpen(GravityCompat.START))drawer.closeDrawer(GravityCompat.START)else finish()}})
  home()
 }
 private fun toast(s:String){Toast.makeText(this,s,Toast.LENGTH_SHORT).show()}
 private fun info(title:String,message:String){body.removeAllViews();body.addView(TextView(this).apply{text=title;textSize=24f});body.addView(TextView(this).apply{text=message;textSize=17f;setPadding(0,16,0,16)})}
 private fun home(){info("DANH MỤC DEMO", "Chọn bài bên dưới hoặc mở menu ☰. Mỗi bài chạy trong cùng một ứng dụng.");demos.forEachIndexed{i,p->body.addView(Button(this).apply{backgroundTintList=android.content.res.ColorStateList.valueOf(0xFF6750A4.toInt());setTextColor(-1);text=p.first;setOnClickListener{open(i)}})}}
 private fun open(i:Int){if(i !in demos.indices)return;val demo=demos[i];startActivity(Intent(this,demo.second).putExtra("demo_title",demo.first))}
 override fun onCreateOptionsMenu(menu:Menu):Boolean{menu.add(0,10,0,"Search").setIcon(android.R.drawable.ic_menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);menu.add(0,11,1,"Settings").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);return true}
 override fun onOptionsItemSelected(item:MenuItem):Boolean=when(item.itemId){10->{toast("Search clicked");true};11->{toast("Settings clicked");true};else->super.onOptionsItemSelected(item)}
}
