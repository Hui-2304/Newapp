package com.group7.ch6.bai1
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Picasso
import com.squareup.picasso.Callback
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.File

class MainActivity : DemoActivity() {
 private val mode=1
 private val scope=CoroutineScope(SupervisorJob()+Dispatchers.Main)
 private val prefs by lazy { getSharedPreferences("image_demo_$mode",MODE_PRIVATE) }
 private lateinit var url:EditText
 private lateinit var status:TextView
 private lateinit var image:ImageView
 private lateinit var panel:LinearLayout
 private var library="Glide"
 private var bitmap:Bitmap?=null
 private var requestVersion=0
 private val urls=mutableListOf<String>()
 private val states=mutableMapOf<String,String>()
 private lateinit var listAdapter:PhotoAdapter
 private fun text(value:String)=TextView(this).apply { text=value;textSize=16f;setPadding(8,12,8,12);panel.addView(this) }
 private fun button(value:String,action:()->Unit)=Button(this).apply { text=value;backgroundTintList=android.content.res.ColorStateList.valueOf(0xFF6750A4.toInt());setTextColor(-1);panel.addView(this);setOnClickListener{action()} }
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  panel=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL;setPadding(16,8,16,8) }
  val root=if(mode==3) panel else ScrollView(this).apply { addView(panel) }
  setContentView(root)
  text(when(mode){1->"TẢI ẢNH VÀ LƯU URL";2->"LƯU VÀ ĐỌC ẢNH";else->"DANH SÁCH ẢNH INTERNET"})
  url=EditText(this).apply { hint="URL ảnh HTTPS";inputType=17;setSingleLine();setText(prefs.getString("url","https://picsum.photos/id/10/600/400"));panel.addView(this) }
  status=text("Sẵn sàng")
  if(mode==3){setupList();return}
  image=ImageView(this).apply { scaleType=ImageView.ScaleType.FIT_CENTER;setImageResource(android.R.drawable.ic_menu_gallery);panel.addView(this,LinearLayout.LayoutParams(-1,600)) }
  if(mode==1){
   val choose=Spinner(this);choose.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listOf("Glide","Picasso"));panel.addView(choose)
   choose.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(p:AdapterView<*>?){}
    override fun onItemSelected(p:AdapterView<*>?,v:View?,i:Int,id:Long){library=if(i==0)"Glide" else "Picasso"}
   }
   button("Lưu URL"){val u=validUrl()?:return@button;prefs.edit().putString("url",u).apply();status.text="Đã lưu URL: $u"}
   button("Đọc ảnh"){val u=prefs.getString("url",null);if(u==null)status.text="Chưa có URL đã lưu" else {url.setText(u);load(u)}}
   button("Xóa URL"){requestVersion++;Glide.with(this).clear(image);Picasso.get().cancelRequest(image);prefs.edit().remove("url").apply();url.text.clear();image.setImageResource(android.R.drawable.ic_menu_gallery);status.text="Đã xóa URL"}
   button("Tải ảnh bằng thư viện đã chọn"){validUrl()?.let { load(it) }}
  } else {
   button("Tải ảnh"){validUrl()?.let { loadBitmap(it) }}
   button("Lưu ảnh vào thư viện (MediaStore)"){save(true)}
   button("Lưu ảnh vào thư mục ứng dụng"){save(false)}
   button("Đọc ảnh đã lưu gần nhất"){showSaved()}
  }
 }
 private fun validUrl():String? {
  val value=url.text.toString().trim();val uri=Uri.parse(value)
  if(uri.scheme!="https"||uri.host.isNullOrBlank()){url.error="Nhập URL HTTPS hợp lệ";return null};return value
 }
 private fun load(u:String){
  requestVersion++;val version=requestVersion
  Glide.with(this).clear(image);Picasso.get().cancelRequest(image);status.text="Đang tải bằng $library..."
  if(library=="Picasso")Picasso.get().load(u).resize(1000,1000).centerInside().placeholder(android.R.drawable.ic_menu_gallery).error(android.R.drawable.ic_dialog_alert).into(image,object:Callback{
   override fun onSuccess(){if(version==requestVersion)status.text="Đã tải · Picasso"}
   override fun onError(e:Exception?){if(version==requestVersion)status.text="Lỗi tải ảnh. Kiểm tra URL và Internet."}
  }) else Glide.with(this).load(u).placeholder(android.R.drawable.ic_menu_gallery).error(android.R.drawable.ic_dialog_alert).listener(object:RequestListener<Drawable>{
   override fun onLoadFailed(e:GlideException?,m:Any?,t:Target<Drawable>,first:Boolean):Boolean {if(version==requestVersion)status.text="Lỗi tải ảnh. Kiểm tra URL và Internet.";return false}
   override fun onResourceReady(r:Drawable,m:Any,t:Target<Drawable>?,d:DataSource,first:Boolean):Boolean {if(version==requestVersion)status.text="Đã tải · Glide";return false}
  }).into(image)
 }
 private fun loadBitmap(u:String){
  requestVersion++;val version=requestVersion;bitmap=null;status.text="Đang tải ảnh..."
  scope.launch {
   try {
    val result=withContext(Dispatchers.IO){val target=Glide.with(applicationContext).asBitmap().load(u).submit(1600,1600);try{target.get().copy(Bitmap.Config.ARGB_8888,false)}finally{Glide.with(applicationContext).clear(target)}}
    if(version!=requestVersion)return@launch
    bitmap=result;image.setImageBitmap(result);prefs.edit().putString("url",u).apply();status.text="Đã tải ảnh, có thể lưu."
   }catch(e:CancellationException){throw e}catch(e:Exception){if(version==requestVersion)status.text="Không tải được ảnh: ${e.message}"}
  }
 }
 private fun save(gallery:Boolean){
  val b=bitmap?:run{status.text="Hãy tải ảnh thành công trước khi lưu.";return}
  scope.launch {
   try {
    val uri=withContext(Dispatchers.IO){
     val name="group7_${System.currentTimeMillis()}.jpg"
     if(gallery&&Build.VERSION.SDK_INT>=29){
      val values=ContentValues().apply{put(MediaStore.Images.Media.DISPLAY_NAME,name);put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/Group7");put(MediaStore.Images.Media.IS_PENDING,1)}
      val item=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)?:error("Không tạo được ảnh")
      try{contentResolver.openOutputStream(item)?.use{check(b.compress(Bitmap.CompressFormat.JPEG,92,it))}?:error("Không ghi được ảnh");contentResolver.update(item,ContentValues().apply{put(MediaStore.Images.Media.IS_PENDING,0)},null,null);item}
      catch(e:Exception){contentResolver.delete(item,null,null);throw e}
     }else{
      val file=File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),name);file.outputStream().use{check(b.compress(Bitmap.CompressFormat.JPEG,92,it))};Uri.fromFile(file)
     }
    }
    prefs.edit().putString("saved_uri",uri.toString()).apply()
    status.text=if(uri.scheme=="content")"Đã lưu vào Pictures/Group7 trong thư viện ảnh." else "Đã lưu vào thư mục ảnh của ứng dụng: $uri"
   }catch(e:CancellationException){throw e}catch(e:Exception){status.text="Lỗi lưu ảnh: ${e.message}"}
  }
 }
 private fun showSaved(){
  val saved=prefs.getString("saved_uri",null)?:run{status.text="Chưa có ảnh đã lưu";return}
  Glide.with(this).load(Uri.parse(saved)).error(android.R.drawable.ic_dialog_alert).into(image);status.text="Ảnh đã lưu: $saved"
 }
 private fun setupList(){
  val saved=JSONArray(prefs.getString("urls","[]"));for(i in 0 until saved.length())urls.add(saved.getString(i))
  if(urls.isEmpty())urls.addAll((10..21).map{"https://picsum.photos/id/$it/600/400"})
  val history=org.json.JSONObject(prefs.getString("states","{}"));urls.forEach{val old=history.optString(it,"Chờ tải");states[it]=if(old=="Đang tải")"Chờ tải" else old}
  button("Thêm URL"){val u=validUrl()?:return@button;if(u in urls){status.text="URL đã có trong danh sách";return@button};if(urls.size>=40){status.text="Tối đa 40 ảnh";return@button};urls.add(u);states[u]="Chờ tải";persist();listAdapter.notifyItemInserted(urls.lastIndex);summary()}
  button("Tải lại tất cả"){urls.forEach{states[it]="Chờ tải"};listAdapter.notifyDataSetChanged();summary()}
  button("Xóa danh sách"){AlertDialog.Builder(this).setMessage("Xóa toàn bộ danh sách URL?").setNegativeButton("Hủy",null).setPositiveButton("Xóa"){_,_->urls.clear();states.clear();persist();listAdapter.notifyDataSetChanged();summary()}.show()}
  val recycler=RecyclerView(this);recycler.layoutManager=LinearLayoutManager(this);listAdapter=PhotoAdapter();recycler.adapter=listAdapter;panel.addView(recycler,LinearLayout.LayoutParams(-1,0,1f));summary()
 }
 private fun persist(){prefs.edit().putString("urls",JSONArray(urls).toString()).apply()}
 private fun summary(){prefs.edit().putString("states",org.json.JSONObject(states as Map<*,*>).toString()).apply();status.text="Đã tải ${states.values.count{it=="Đã tải"}}/${urls.size} ảnh · Lỗi ${states.values.count{it=="Lỗi tải"}}"}
 inner class PhotoAdapter:RecyclerView.Adapter<PhotoAdapter.Holder>(){
  inner class Holder(val box:LinearLayout,val photo:ImageView,val label:TextView):RecyclerView.ViewHolder(box){var bound=""}
  override fun onCreateViewHolder(parent:ViewGroup,type:Int):Holder{
   val box=LinearLayout(parent.context).apply{orientation=LinearLayout.VERTICAL;layoutParams=RecyclerView.LayoutParams(-1,-2);setPadding(8,8,8,16)}
   val photo=ImageView(parent.context).apply{scaleType=ImageView.ScaleType.CENTER_CROP};box.addView(photo,LinearLayout.LayoutParams(-1,320));val label=TextView(parent.context);box.addView(label);return Holder(box,photo,label)
  }
  override fun getItemCount()=urls.size
  override fun onBindViewHolder(h:Holder,position:Int){
   val u=urls[position];h.bound=u;states[u]="Đang tải";summary();h.label.text="$u\nĐang tải..."
   Glide.with(h.photo).load(u).placeholder(android.R.drawable.ic_menu_gallery).error(android.R.drawable.ic_dialog_alert).listener(object:RequestListener<Drawable>{
    override fun onLoadFailed(e:GlideException?,m:Any?,t:Target<Drawable>,first:Boolean):Boolean{if(h.bound==u && u in urls){states[u]="Lỗi tải";h.label.text="$u\nLỗi tải · chạm để thử lại";summary()};return false}
    override fun onResourceReady(r:Drawable,m:Any,t:Target<Drawable>?,d:DataSource,first:Boolean):Boolean{if(h.bound==u && u in urls){states[u]="Đã tải";h.label.text="$u\nĐã tải";summary()};return false}
   }).into(h.photo)
   h.box.setOnClickListener{if(states[u]=="Lỗi tải"){notifyItemChanged(h.bindingAdapterPosition)}else{val preview=ImageView(this@MainActivity);Glide.with(this@MainActivity).load(u).into(preview);AlertDialog.Builder(this@MainActivity).setView(preview).setPositiveButton("Đóng",null).show()}}
  }
  override fun onViewRecycled(h:Holder){h.bound="";Glide.with(h.photo).clear(h.photo);super.onViewRecycled(h)}
 }
 override fun onDestroy(){requestVersion++;scope.cancel();super.onDestroy()}
}
