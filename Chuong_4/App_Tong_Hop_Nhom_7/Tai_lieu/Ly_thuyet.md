# CHƯƠNG 4
# ACTION BAR VÀ NAVIGATION DRAWER TRONG ANDROID
Sinh viên: Nguyễn Quang Bảo Huy     •     MSSV: N23DCCI033     •     Lớp: D23CQCI01-N

## A TÓM TẮT LÝ THUYẾT CHƯƠNG 4
Chương 4 trình bày cách tổ chức thanh chức năng và menu điều hướng trong ứng dụng Android. Action Bar hiển thị tiêu đề và các thao tác của màn hình; Toolbar cho phép bố trí thanh công cụ trực tiếp trong giao diện; Navigation Drawer cung cấp danh sách các khu vực hoặc màn hình của ứng dụng. Kiến thức của chương được vận dụng để tích hợp các bài demo chương 2, 3, 5 và 6 vào một ứng dụng chung của nhóm. [1, tr. 3–7, 25–30]
### 1 Action Bar và menu chức năng
Action Bar nằm ở đầu màn hình, có thể chứa tiêu đề, biểu tượng, các nút thao tác và menu mở rộng. Hai bước quan trọng là tạo các mục menu và xử lý sự kiện chọn mục. Ví dụ cơ bản của chương có Search và Settings; khi chọn, ứng dụng hiển thị Toast. [1, tr. 5–20, 46]
### 2 Toolbar
Toolbar là một ViewGroup có thể đặt trong bố cục. Với AppCompatActivity, gọi setSupportActionBar(toolbar) để dùng Toolbar làm thanh chức năng của Activity. Khi tự thêm Toolbar, giao diện dùng theme NoActionBar để tránh xuất hiện hai thanh tiêu đề. [1, tr. 25–27]
### 3 Navigation Drawer
DrawerLayout chứa nội dung chính và ngăn menu; NavigationView trình bày các mục điều hướng; ActionBarDrawerToggle liên kết nút ☰ trên Toolbar với trạng thái mở, đóng Drawer. Có thể thêm HeaderView để giới thiệu ứng dụng hoặc người dùng. [1, tr. 29–42]
### 4 Vận dụng vào ứng dụng nhóm
Ứng dụng chung có một màn hình danh mục, một Navigation Drawer và 12 màn hình demo. Mỗi bài vẫn có mã nguồn, giao diện và hành vi riêng nhưng cùng nằm trong một module app và được đóng gói thành một APK. Chương 4 đảm nhiệm việc kết nối và điều hướng, không thay thế nội dung học tập của các chương khác. [2]

## B NỘI DUNG LÝ THUYẾT CHI TIẾT
### 4.1 Vai trò của thanh chức năng và điều hướng
Khi ứng dụng có nhiều màn hình, người dùng cần biết mình đang ở đâu, có thể thực hiện thao tác gì và làm thế nào để đi tới khu vực khác. Tiêu đề trên thanh chức năng thể hiện ngữ cảnh; nút hành động thực hiện công việc tại màn hình; menu điều hướng đưa người dùng tới các bài hoặc khu vực của ứng dụng.
Trong bài tổng hợp của nhóm, mục “Ch5 · Danh sách bài viết” mở màn hình gọi API, còn mục “Ch3 · Quản lý công việc” mở màn hình ListView công việc. Hai chức năng cùng nằm trong một ứng dụng nhưng không phải hiển thị đồng thời trên cùng một màn hình. Navigation Drawer giúp lựa chọn rõ ràng từng bài. [2]
Phân biệt thao tác với điều hướng giúp đặt chức năng đúng chỗ. Search và Settings trong bài cơ bản là các action item trên thanh công cụ. Các mục chương 2, 3, 5 và 6 trong Drawer là điểm đến. Khi xây dựng bài demo, mỗi lần chọn phải có phản hồi rõ ràng: hiện Toast hoặc mở đúng Activity. [1, tr. 6–9, 29–30]

### 4.2 Action Bar: khái niệm, chức năng và thành phần
#### 4.2.1 Khái niệm
Action Bar là thành phần giao diện ở đầu màn hình, thường thể hiện tên ứng dụng hoặc tên màn hình và các thao tác quan trọng. Nó cung cấp vị trí nhất quán để người dùng tìm lệnh. Tùy theme, một Activity có thể được cung cấp thanh chức năng mặc định; trường hợp tự thiết kế Toolbar cần cấu hình tương ứng. [1, tr. 5–12]
#### 4.2.2 Thành phần
Title/Subtitle hiển thị tên và thông tin ngữ cảnh của màn hình. App Icon hoặc logo giới thiệu ứng dụng. Action Items là các mục thao tác trực tiếp như tìm kiếm, thêm hoặc sửa. Overflow Menu là vùng menu mở rộng, thường thể hiện bằng biểu tượng ⋮. Navigation Icon có thể là mũi tên quay lên hoặc nút ☰ mở Drawer. [1, tr. 7–9]
Số lượng mục hiển thị trực tiếp phụ thuộc diện tích thanh công cụ và cấu hình của từng item. Lệnh ít dùng có thể đặt trong Overflow Menu. Không nên cố đưa mọi bài tập lên thanh công cụ vì người dùng sẽ khó phân biệt lệnh thao tác và màn hình cần mở.
#### 4.2.3 Ý nghĩa khi vận dụng
Màn hình chính của app tổng hợp hiển thị tiêu đề “Nhóm 7 · Demo tổng hợp”. Nút Search hiển thị trực tiếp nếu đủ chỗ; Settings nằm trong menu mở rộng. Mỗi màn hình demo có tiêu đề riêng để nhận biết bài đang chạy. [2]
[CHỖ CHÈN HÌNH 4.1: Chụp thanh công cụ màn hình chính, thể hiện tiêu đề, nút ☰ và Search.]

### 4.3 Khai báo menu và thuộc tính của item
#### 4.3.1 Tổ chức tài nguyên menu
Menu có thể khai báo bằng XML trong res/menu hoặc tạo bằng Kotlin. Khai báo XML tách cấu trúc menu khỏi xử lý sự kiện; tạo bằng Kotlin phù hợp khi danh mục được dựng từ danh sách bài demo. Bài giảng minh họa file menu_main.xml; app tổng hợp dựng menu bằng Kotlin để dùng chung danh sách điểm đến. [1, tr. 13–16; 2]
#### 4.3.2 Các thuộc tính quan trọng
android:id xác định mục menu để xử lý khi được chọn. android:title là nhãn người dùng nhìn thấy. android:icon xác định hình biểu tượng. showAsAction quy định mục ưu tiên hiển thị trực tiếp hay chuyển vào Overflow Menu. Với AppCompat, ví dụ XML sau sử dụng namespace app cho thuộc tính app:showAsAction.
```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item android:id="@+id/action_search"
        android:title="Search"
        android:icon="@android:drawable/ic_menu_search"
        app:showAsAction="ifRoom" />
    <item android:id="@+id/action_settings"
        android:title="Settings"
        app:showAsAction="never" />
</menu>
```
Giá trị ifRoom cho phép đưa mục lên thanh khi còn chỗ; never giữ mục trong menu mở rộng. always yêu cầu ưu tiên hiển thị trực tiếp và cần sử dụng có cân nhắc. Tiêu đề vẫn cần được khai báo kể cả khi có icon để làm rõ ý nghĩa của mục.
#### 4.3.3 Cách dùng trong app chung
Mã thực tế dùng menu.add() trong onCreateOptionsMenu(). ID 10 đại diện Search, ID 11 đại diện Settings. Việc tạo bằng Kotlin và khai báo bằng XML có cùng mục tiêu; không cần triển khai hai bộ menu song song. [2]
```kotlin
menu.add(0, 10, 0, "Search")
    .setIcon(android.R.drawable.ic_menu_search)
    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
menu.add(0, 11, 1, "Settings")
    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
```

### 4.4 Tạo menu và xử lý sự kiện trong Kotlin
#### 4.4.1 onCreateOptionsMenu()
onCreateOptionsMenu() được ghi đè để cung cấp các mục menu cho thanh chức năng. Khi dùng XML, menuInflater.inflate() đọc tài nguyên menu_main và đưa các item vào đối tượng Menu. Hàm trả về true để cho phép hiển thị menu. [1, tr. 18–20]
```kotlin
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
}
```
Đoạn trên minh họa cách dùng XML. Trong app tổng hợp, phần inflate được thay bằng các lời gọi menu.add() đã nêu ở mục 4.3.3.
#### 4.4.2 onOptionsItemSelected()
Khi một action item được chọn, Activity nhận đối tượng MenuItem. Thuộc tính itemId giúp xác định mục nào vừa được bấm. Trả về true khi sự kiện đã được xử lý; mục chưa xử lý cần chuyển cho lớp cha để giữ các hành vi mặc định. [1, tr. 18–20]
```kotlin
override fun onOptionsItemSelected(item: MenuItem): Boolean =
    when (item.itemId) {
        10 -> { toast("Search clicked"); true }
        11 -> { toast("Settings clicked"); true }
        else -> super.onOptionsItemSelected(item)
    }
```
Hàm toast() trong app gọi Toast.makeText(this, nộiDung, Toast.LENGTH_SHORT).show(). Toast là phản hồi ngắn cho thao tác, phù hợp yêu cầu bài cơ bản. Bài tập không yêu cầu xây dựng công cụ tìm kiếm thật hay trang cấu hình hoàn chỉnh. [1, tr. 46; 2]
#### 4.4.3 Luồng xử lý
Người dùng bấm Search → hệ thống chuyển MenuItem tới Activity → when kiểm tra itemId → gọi Toast → hàm trả về true. Đối với Settings trong menu ⋮, người dùng mở menu trước rồi chọn lệnh; cơ chế xử lý sự kiện vẫn tương tự.
[CHỖ CHÈN HÌNH 4.2: Chụp Toast “Search clicked” và menu mở rộng chứa Settings.]

### 4.5 Toolbar và cấu hình AppCompat
#### 4.5.1 Đặt Toolbar trong giao diện
Toolbar có thể khai báo trong XML hoặc tạo bằng Kotlin. Nó có các thuộc tính bố cục như những View khác và có thể đặt trong cấu trúc màn hình chứa nội dung chính. Tính linh hoạt này giúp ứng dụng dùng một kiểu thanh công cụ nhất quán cho các bài demo. [1, tr. 25–27]
```xml
<androidx.appcompat.widget.Toolbar
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize" />
```
Đây là đoạn XML minh họa. App tổng hợp tạo Toolbar trực tiếp bằng Kotlin, đặt trước phần nội dung trong LinearLayout theo chiều dọc. Chiều cao Toolbar được quy đổi từ dp theo mật độ màn hình. [2]
#### 4.5.2 Liên kết Toolbar với Activity
Với Activity kế thừa AppCompatActivity, setSupportActionBar(toolbar) đăng ký Toolbar làm thanh chức năng. Các hàm onCreateOptionsMenu() và onOptionsItemSelected() sau đó hoạt động trên thanh này. Theme dùng Theme.MaterialComponents.DayNight.NoActionBar để tránh tạo thêm thanh mặc định. [2]
```kotlin
setSupportActionBar(toolbar)
supportActionBar?.setDisplayHomeAsUpEnabled(true)
```
Dòng thứ hai phù hợp màn hình demo cần nút quay lên. Ở màn hình chính dùng Drawer, ActionBarDrawerToggle phụ trách biểu tượng ☰ thay cho mũi tên.
#### 4.5.3 Khoảng trống của thanh hệ thống
Thanh trạng thái và thanh điều hướng của hệ điều hành có thể chồng lên nội dung nếu bố cục không xử lý WindowInsets. App chung tính khoảng trống này tại lớp bao ngoài, sau đó phân bổ Toolbar và nội dung. Đây là phần triển khai bổ sung từ cách xử lý giao diện trong project chương 3, giúp thanh công cụ và nội dung không bị che. [2]

### 4.6 Navigation Drawer: khái niệm và cấu trúc
#### 4.6.1 Khái niệm
Navigation Drawer là ngăn menu trượt từ cạnh màn hình. Trong tài liệu, người dùng mở menu bằng vuốt từ trái hoặc bấm biểu tượng hamburger. Drawer thường chứa danh sách khu vực chính, giúp chuyển tới nhiều màn hình mà không phải đặt toàn bộ mục trên thanh công cụ. [1, tr. 29]
#### 4.6.2 DrawerLayout
DrawerLayout là bố cục gốc chứa phần nội dung và phần ngăn menu. Nội dung chính được bố trí trước; ngăn menu đặt tại cạnh start. GravityCompat.START thể hiện cạnh bắt đầu theo hướng ngôn ngữ, phù hợp hơn việc gắn cứng cạnh trái trong mã. [1, tr. 30, 34]
#### 4.6.3 NavigationView
NavigationView hiển thị menu trong Drawer, hỗ trợ các mục, nhóm mục và phần header. Các mục có thể được khai báo trong res/menu/menu_drawer.xml hoặc bổ sung bằng Kotlin. Trong app chung, bốn nhóm “Chương 2”, “Chương 3”, “Chương 5”, “Chương 6” được dựng từ danh sách 12 demo. [1, tr. 30, 36; 2]
#### 4.6.4 ActionBarDrawerToggle
ActionBarDrawerToggle liên kết Toolbar với DrawerLayout, điều khiển hành vi của biểu tượng mở menu và cập nhật trạng thái tương ứng. Sau khi tạo toggle, cần đăng ký bằng addDrawerListener() và gọi syncState(). [1, tr. 30, 37–41]
```kotlin
val toggle = ActionBarDrawerToggle(
    this, drawer, toolbar,
    R.string.drawer_open, R.string.drawer_close
)
drawer.addDrawerListener(toggle)
toggle.syncState()
```
Hai tài nguyên chuỗi mô tả thao tác mở và đóng menu, hỗ trợ khả năng tiếp cận. Chỉ vẽ biểu tượng ☰ mà chưa gắn sự kiện sẽ không tạo được hành vi Drawer.

### 4.7 Xây dựng bố cục và menu Drawer
#### 4.7.1 Cấu trúc XML minh họa
```xml
<androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    <com.google.android.material.navigation.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:menu="@menu/menu_drawer" />
</androidx.drawerlayout.widget.DrawerLayout>
```
DrawerLayout bao toàn bộ giao diện; FrameLayout đại diện vùng nội dung; NavigationView đặt tại cạnh start. Đoạn mã là minh họa dựa trên cấu trúc bài giảng, có khai báo namespace app cho thuộc tính app:menu. App chung tạo cùng các thành phần bằng Kotlin và dùng LinearLayout/ScrollView cho trang danh mục. [1, tr. 34; 2]
#### 4.7.2 Home, Profile và Settings
Yêu cầu cơ bản của bài 2 gồm ba mục Home, Profile, Settings và hiển thị Toast khi chọn. App tổng hợp giữ ba mục này ở đầu Drawer, đồng thời bổ sung các nhóm chương ở bên dưới. Như vậy vẫn quan sát được yêu cầu cơ bản và có thể chuyển tới các demo của nhóm. [1, tr. 47; 2]
#### 4.7.3 Đặt ID và nhóm mục
Mỗi điểm đến cần ID riêng. Trong mã thực tế, Home/Profile/Settings dùng ID 1, 2, 3; các demo dùng ID từ 100 cộng với chỉ số bài. Cách đặt này phân biệt nhóm mục cơ bản và danh sách bài. ID phải được giữ nhất quán giữa lúc tạo menu và lúc xử lý lựa chọn. [2]
[CHỖ CHÈN HÌNH 4.3: Chụp Drawer đang mở, thấy header, Home/Profile/Settings và các nhóm chương.]

### 4.8 Xử lý lựa chọn và đóng Drawer
#### 4.8.1 Lắng nghe lựa chọn
setNavigationItemSelectedListener() nhận MenuItem do người dùng chọn. App đóng Drawer, phân loại itemId và thực hiện hành vi tương ứng. Home hiển thị danh mục; Profile và Settings hiển thị nội dung giới thiệu hoặc thông tin sử dụng; cả ba đồng thời có Toast để minh họa bài cơ bản. [2]
```kotlin
nav.setNavigationItemSelectedListener { item ->
    drawer.closeDrawer(GravityCompat.START)
    when (item.itemId) {
        1 -> { toast("Home"); home() }
        2 -> { toast("Profile"); /* hiển thị giới thiệu */ }
        3 -> { toast("Settings"); /* hiển thị thông tin */ }
        else -> open(item.itemId - 100)
    }
    true
}
```
Đây là trích đoạn rút gọn để giải thích luồng sự kiện; phần triển khai đầy đủ nằm trong MainActivity.kt. Nếu mục chưa được nhận diện hoặc chỉ số ngoài danh sách, hàm open() không mở màn hình. [2]
#### 4.8.2 Quay lại khi Drawer đang mở
Khi người dùng bấm Back trong lúc Drawer mở, ứng dụng nên đóng Drawer trước. App dùng OnBackPressedCallback: nếu ngăn menu đang mở thì closeDrawer(), còn khi đang ở trang chính và Drawer đóng thì kết thúc Activity. [2]
#### 4.8.3 HeaderView
HeaderView là vùng đầu menu, có thể chứa hình ảnh hoặc thông tin người dùng. Bài giảng giới thiệu việc bổ sung ảnh đại diện và tiêu đề. Trong app nhóm, header dùng chữ “NHÓM 7”, “Android · Kotlin” và các chương đã tích hợp để giới thiệu phạm vi demo; chưa sử dụng tài khoản đăng nhập hoặc ảnh đại diện cá nhân. [1, tr. 42–44; 2]

### 4.9 Điều hướng giữa các bài bằng Activity và Intent
#### 4.9.1 Mỗi demo là một màn hình riêng
Mỗi bài được tổ chức trong package riêng để giữ lớp MainActivity và model của bài mà không bị trùng tên với bài khác. Các Activity cùng được khai báo trong AndroidManifest.xml của app chung. Chỉ MainActivity cấp ứng dụng có intent-filter MAIN/LAUNCHER; các màn hình còn lại được mở từ danh mục. [2]
#### 4.9.2 Intent tường minh
Intent tường minh xác định trực tiếp lớp Activity cần mở. App dùng danh sách cặp tiêu đề và lớp Activity; cùng một danh sách được dùng cho nút trên trang Home và mục trong Drawer. Cách này giúp hai đường điều hướng luôn trỏ tới cùng bài. [2]
```kotlin
val demo = demos[index]
startActivity(
    Intent(this, demo.second)
        .putExtra("demo_title", demo.first)
)
```
Extra demo_title cung cấp tiêu đề cho thanh công cụ của bài. Đây là phần bổ sung phục vụ tích hợp; không phải truyền toàn bộ dữ liệu hoặc mã nguồn giữa các app.
#### 4.9.3 Quay về danh mục
Lớp DemoActivity cung cấp Toolbar và mũi tên quay lên cho các màn hình con. onSupportNavigateUp() gọi finish() để quay về màn hình trước. Nếu đang xem chi tiết bài viết, màn hình trước là danh sách bài viết; nếu đang ở màn hình đầu của demo, màn hình trước là danh mục app chung. [2]

### 4.10 Tích hợp các project và tránh xung đột
#### 4.10.1 Project, module và APK
Project là cấu trúc làm việc trong Android Studio; module app chứa thành phần được biên dịch thành ứng dụng; APK là gói cài đặt. Các thành viên có thể phát triển trên máy khác nhau, sau đó gửi mã nguồn để tích hợp. Sản phẩm cuối cùng của yêu cầu nhóm là một app chạy được các demo, không phải một thư mục chứa nhiều APK riêng.
#### 4.10.2 Package và tài nguyên
Trong app chung, mã nguồn từng bài thuộc com.group7.tonghop.demo1 đến demo12. Tài nguyên layout, drawable và ID của từng bài có tiền tố tương ứng d1_ đến d12_. Nhờ đó, activity_main.xml hoặc các ID như btnThem của nhiều bài không ghi đè nhau. Khi đổi tên tài nguyên phải cập nhật cả Kotlin và các tham chiếu trong XML. [2]
#### 4.10.3 Cấu hình dùng chung
App tổng hợp dùng cấu hình Gradle, Android SDK và thư viện nền theo project mẫu chương 3 đang có trên máy. Các thư viện Retrofit, Gson, coroutine, Glide, Picasso và RecyclerView được khai báo để đáp ứng bài chương 5 và 6. Không gộp nhiều AndroidManifest hoặc nhiều applicationId thành các app độc lập trong bản tổng hợp. [2]
#### 4.10.4 Quyền và dữ liệu
App có quyền INTERNET cho REST API và tải ảnh. SQLite lưu dữ liệu trên thiết bị. SharedPreferences lưu URL hoặc danh sách URL. Trên Android 10 trở lên, chức năng lưu ảnh vào thư viện dùng MediaStore; thư mục ảnh riêng của ứng dụng là lựa chọn khác và có thể bị xóa khi gỡ app. Dữ liệu của project riêng và app chung nằm trong các vùng lưu trữ ứng dụng khác nhau. [2; 3]

### 4.11 Liên hệ các chương trong ứng dụng tổng hợp
Chương 2 cung cấp Kotlin thông qua ba bài: máy tính hai số nguyên; nhập mảng và thống kê; CRUD sinh viên bằng danh sách trong bộ nhớ. Kiến thức được vận dụng gồm biến, hàm, when, vòng lặp, collection, model và kiểm tra null.
Chương 3 cung cấp giao diện và sự kiện thông qua danh sách sinh viên, quản lý công việc và danh sách môn học. App tích hợp dùng lại mã nguồn ba project mẫu, thay đổi package và tiền tố tài nguyên để ghép vào ứng dụng chung. [2]
Chương 5 gồm GET danh sách và chi tiết bài viết, POST/PUT/DELETE qua REST API và CRUD sinh viên bằng SQLite. JSONPlaceholder là dịch vụ mô phỏng nên phản hồi thành công của thao tác ghi không chứng minh dữ liệu được lưu vĩnh viễn trên server.
Chương 6 gồm tải ảnh và lưu URL, lưu/đọc ảnh, danh sách nhiều ảnh kèm trạng thái tải. Bản tích hợp được viết lại bằng Kotlin theo chức năng và giao diện tham khảo Flutter trong tài liệu nhóm. Ba ví dụ này không yêu cầu kết nối Firebase. [2]
Chương 4 kết nối các bài nói trên thông qua Toolbar, Navigation Drawer và Intent. Điểm cần chứng minh khi demo là người dùng chỉ mở một app, chọn được từng bài và quay lại danh mục để tiếp tục. [2]

### 4.12 Lỗi thường gặp và cách kiểm tra
Nếu xuất hiện hai thanh tiêu đề, kiểm tra theme có cung cấp Action Bar mặc định trong khi màn hình đã thêm Toolbar hay không. Khi dùng Toolbar với setSupportActionBar(), chọn theme NoActionBar và kiểm tra lớp Activity có kế thừa AppCompatActivity. [2]
Nếu nút ☰ không mở Drawer, kiểm tra Toolbar, DrawerLayout và ActionBarDrawerToggle có cùng thuộc màn hình đang hiển thị; kiểm tra addDrawerListener() và syncState(). Nếu menu không phản hồi, kiểm tra listener, itemId và giá trị trả về sau khi xử lý.
Nếu chọn bài bị mở nhầm hoặc báo không tìm thấy Activity, đối chiếu danh sách demos với lớp thực tế và khai báo manifest. Nếu R.layout hoặc R.id không tồn tại sau khi gộp, kiểm tra tên tài nguyên, namespace và mọi tham chiếu trong Kotlin/XML. Biên dịch giúp phát hiện lỗi mã và tài nguyên nhưng vẫn cần chạy app để xác minh điều hướng. [2]
Nếu bài API hoặc ảnh không tải, kiểm tra Internet, URL HTTPS và phản hồi lỗi. Không dùng dữ liệu giả để thay thế rồi ghi nhận là kết quả gọi mạng thành công. Nếu dữ liệu trong danh sách bộ nhớ bị mất sau khi mở lại màn hình, đối chiếu đúng phạm vi bài; chỉ SQLite, SharedPreferences và ảnh đã lưu mới có cơ chế lưu bền vững trong các ví dụ đã chọn.

## C TỔNG KẾT VÀ VẬN DỤNG
### 1 Kiến thức cần nắm
Người học cần phân biệt Action Bar, Toolbar và Navigation Drawer; biết cách tạo menu, xử lý sự kiện theo itemId, liên kết toggle với Drawer và điều hướng tới Activity. Việc đặt tiêu đề, nút quay lại và xử lý khoảng trống hệ thống giúp các màn hình có cách sử dụng thống nhất.
### 2 Vận dụng vào bài tập nhóm
Sản phẩm chương 4 là project Chuong4_AppTongHopNhom7. Người dùng mở một ứng dụng để truy cập 12 demo chương 2, 3, 5, 6. Hai yêu cầu cơ bản của bài giảng vẫn được giữ: Search/Settings trên Toolbar và Home/Profile/Settings trong Drawer có Toast. Các mục chương là phần mở rộng để đáp ứng yêu cầu tích hợp của giảng viên. [1, tr. 46–47; 2]
### 3 Minh chứng cần bổ sung
Ảnh minh chứng phải được chụp từ app chạy thực tế: trang danh mục, Drawer, Toast, một số màn hình đại diện và thao tác quay lại. Các vị trí dành cho ảnh trong tài liệu chỉ là chỗ chèn, chưa phải kết quả kiểm thử. Chi tiết thao tác và vị trí ảnh nằm trong thẻ Bài tập chương 4.

## D TÀI LIỆU THAM KHẢO
[1] Bài giảng “Chương 4 Action Bar – Navigation Drawer”, tài liệu PDF giảng viên cung cấp, 47 trang; số trang trích dẫn tính theo trang của file PDF.
[2] Mã nguồn ứng dụng Chuong4_AppTongHopNhom7 và ba project chương 3 mẫu; tài liệu nhóm Group_7_GK, các thẻ chương 2, 3, 5 và 6. Phần Intent, tổ chức package và tách tài nguyên là triển khai tích hợp của nhóm.
[3] Android Developers, “Access media files from shared storage”: https://developer.android.com/training/data-storage/shared/media
