## 7 Mã nguồn trọng tâm chương 4
Các đoạn dưới đây được trích và trình bày lại cách xuống dòng từ MainActivity.kt và DemoActivity.kt của project Chuong4_AppTongHopNhom7. Nội dung tập trung vào Toolbar, menu, Navigation Drawer và điều hướng giữa các màn hình. Đây là các đoạn thành phần của project đã chạy, không phải các chương trình độc lập. Phần tính toán, danh sách, cơ sở dữ liệu và xử lý ảnh của các chương khác không được chép vào mục này.
### 7.1 Tạo Toolbar và bố cục chứa Drawer
Tệp: MainActivity.kt, trong onCreate(). DrawerLayout chứa nội dung chính và ngăn menu. Toolbar được đăng ký làm thanh chức năng bằng setSupportActionBar(). FrameLayout ngoài cùng nhận khoảng trống của các thanh hệ thống.
```kotlin
drawer = DrawerLayout(this)

val column = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
}
val toolbar = Toolbar(this).apply {
    title = "Nhóm 7 · Demo tổng hợp"
    setBackgroundColor(0xFF6750A4.toInt())
    setTitleTextColor(-1)
}
column.addView(toolbar, LinearLayout.LayoutParams(
    -1, (56 * resources.displayMetrics.density).toInt()
))

val scroll = ScrollView(this)
body = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    setPadding(24, 16, 24, 16)
}
scroll.addView(body)
column.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
drawer.addView(column, DrawerLayout.LayoutParams(-1, -1))

val nav = NavigationView(this).apply {
    setBackgroundColor(-1)
}
drawer.setDrawerElevation(0f)
drawer.addView(nav, DrawerLayout.LayoutParams(
    (300 * resources.displayMetrics.density).toInt(),
    -1, GravityCompat.START
))

val outer = FrameLayout(this)
outer.addView(drawer)
setContentView(outer)
setSupportActionBar(toolbar)

ViewCompat.setOnApplyWindowInsetsListener(outer) { v, i ->
    val bars = i.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
    WindowInsetsCompat.CONSUMED
}
```
Giải thích: -1 tương ứng MATCH_PARENT; chiều cao 0 kết hợp weight = 1 giúp vùng nội dung chiếm phần còn lại dưới Toolbar. GravityCompat.START đặt Drawer ở cạnh bắt đầu của màn hình. Theme của app dùng NoActionBar vì Toolbar đã được thêm trực tiếp.
### 7.2 Tạo menu Search, Settings và hiện Toast
Tệp: MainActivity.kt. Đây là phần đáp ứng bài Action Bar cơ bản: Search có thể hiện trên thanh công cụ; Settings nằm trong menu mở rộng. Cả hai chỉ hiển thị Toast khi bấm.
```kotlin
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.add(0, 10, 0, "Search")
        .setIcon(android.R.drawable.ic_menu_search)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(0, 11, 1, "Settings")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean =
    when (item.itemId) {
        10 -> {
            toast("Search clicked")
            true
        }
        11 -> {
            toast("Settings clicked")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

private fun toast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
```
Giải thích: itemId 10 và 11 xác định hai thao tác. Trả về true khi đã xử lý; những mục khác chuyển về lớp cha. “Search clicked” là nội dung của Toast, chưa phải chức năng tìm kiếm dữ liệu.
### 7.3 Tạo header và các mục Navigation Drawer
Tệp: MainActivity.kt, trong onCreate(), sau khi tạo NavigationView. Biến demos là danh sách 12 cặp tên bài và lớp Activity đích đã khai báo trong app. Đoạn này chỉ dùng danh sách để dựng menu, không chứa code chức năng của từng bài.
```kotlin
val header = TextView(this).apply {
    text = "NHÓM 7\nAndroid · Kotlin\nCác bài demo chương 2, 3, 5, 6"
    textSize = 20f
    setPadding(24, 48, 24, 24)
}
nav.addHeaderView(header)

val menu = nav.menu
menu.add(0, 1, 0, "Home")
menu.add(0, 2, 1, "Profile")
menu.add(0, 3, 2, "Settings")

for ((chapterIndex, ch) in
    listOf("Ch2", "Ch3", "Ch5", "Ch6").withIndex()) {
    val group = menu.addSubMenu(
        0, 200 + chapterIndex, 10 + chapterIndex,
        "Chương " + ch.removePrefix("Ch")
    )
    demos.forEachIndexed { i, p ->
        if (p.first.startsWith(ch)) {
            group.add(1, 100 + i, i, p.first.substringAfter(" · "))
        }
    }
}
```
Giải thích: Home/Profile/Settings có ID 1, 2, 3 và thứ tự 0, 1, 2. Nhóm chương có thứ tự từ 10 để luôn đứng phía sau ba mục cơ bản. Các bài dùng ID 100 + i, nhờ đó có thể xác định chỉ số bài khi người dùng chọn.
### 7.4 Liên kết nút ☰ và xử lý lựa chọn Drawer
Tệp: MainActivity.kt, trong onCreate(). ActionBarDrawerToggle đồng bộ nút ☰ với DrawerLayout. Listener đóng Drawer rồi xử lý mục vừa được chọn.
```kotlin
val toggle = ActionBarDrawerToggle(
    this, drawer, toolbar,
    R.string.drawer_open, R.string.drawer_close
)
drawer.addDrawerListener(toggle)
toggle.syncState()
toggle.drawerArrowDrawable.color = -1

nav.setNavigationItemSelectedListener { item ->
    drawer.closeDrawer(GravityCompat.START)
    when (item.itemId) {
        1 -> {
            toast("Home")
            home()
        }
        2 -> {
            toast("Profile")
            info("Nhóm 7",
                "Ứng dụng tích hợp bài demo của nhóm.\n" +
                "Chương 4: Action Bar, Toolbar và Navigation Drawer.")
        }
        3 -> {
            toast("Settings")
            info("Settings",
                "Các bài mạng và ảnh cần Internet.\n" +
                "Dữ liệu SQLite và URL được lưu trên thiết bị.")
        }
        else -> open(item.itemId - 100)
    }
    true
}
```
Giải thích: home() dựng lại danh mục, info() hiển thị tiêu đề và thông tin trên vùng nội dung chính. Với mục demo, lấy itemId trừ 100 để có chỉ số trong demos. Hai chuỗi drawer_open và drawer_close trong strings.xml lần lượt là “Mở menu” và “Đóng menu”.
### 7.5 Mở màn hình demo bằng Intent và xử lý Back
Tệp: MainActivity.kt. Đây là phần nối các màn hình thành một app; chỉ tham chiếu Activity đích và truyền tiêu đề, không chép lại chức năng của các chương khác.
```kotlin
private fun open(i: Int) {
    if (i !in demos.indices) return
    val demo = demos[i]
    startActivity(
        Intent(this, demo.second)
            .putExtra("demo_title", demo.first)
    )
}
```
Phần đăng ký xử lý Back sau đây đặt trong onCreate(). Nếu Drawer đang mở, Back đóng menu trước; nếu Drawer đã đóng, Back rời màn hình chính.
```kotlin
onBackPressedDispatcher.addCallback(
    this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }
    }
)
```
Giải thích: demo.second là lớp Activity cần mở; demo.first là tiêu đề bài. Các Activity đích đã được khai báo trong AndroidManifest.xml của cùng ứng dụng. Chỉ MainActivity tổng hợp có intent-filter MAIN/LAUNCHER.
### 7.6 Toolbar và mũi tên quay lại ở màn hình demo
Tệp: DemoActivity.kt. Lớp dùng chung bọc giao diện từng bài bằng Toolbar, lấy tiêu đề từ Intent và cung cấp mũi tên quay về màn hình trước. Code giao diện riêng của từng bài không nằm trong lớp này.
```kotlin
open class DemoActivity : AppCompatActivity() {
    override fun setContentView(layoutResID: Int) {
        setContentView(layoutInflater.inflate(layoutResID, null))
    }

    override fun setContentView(view: View?) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        val toolbar = Toolbar(this).apply {
            title = intent.getStringExtra("demo_title")
                ?: getString(R.string.app_name)
            setBackgroundColor(0xFF6750A4.toInt())
            setTitleTextColor(-1)
        }
        box.addView(toolbar, LinearLayout.LayoutParams(
            -1, (56 * resources.displayMetrics.density).toInt()
        ))
        box.addView(view, LinearLayout.LayoutParams(-1, 0, 1f))
        super.setContentView(box)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(-1)

        ViewCompat.setOnApplyWindowInsetsListener(box) { v, i ->
            val bars = i.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
```
Giải thích: setDisplayHomeAsUpEnabled(true) hiển thị nút quay lên. Khi bấm, onSupportNavigateUp() kết thúc màn hình hiện tại bằng finish(). Vùng nội dung được truyền qua setContentView() nên lớp dùng chung không phụ thuộc bài tính toán, ListView, API, SQLite hay tải ảnh.
