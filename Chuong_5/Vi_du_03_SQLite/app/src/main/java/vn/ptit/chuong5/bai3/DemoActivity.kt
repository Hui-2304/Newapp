package vn.ptit.chuong5.bai3
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
open class DemoActivity:AppCompatActivity(){
 override fun setContentView(layoutResID:Int){ setContentView(layoutInflater.inflate(layoutResID,null)) }
 override fun setContentView(view:View?){
  val box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL}
  val toolbar=Toolbar(this).apply{title=intent.getStringExtra("demo_title")?:getString(R.string.app_name);setBackgroundColor(0xFF6750A4.toInt());setTitleTextColor(-1)}
  box.addView(toolbar,LinearLayout.LayoutParams(-1,(56*resources.displayMetrics.density).toInt()))
  box.addView(view,LinearLayout.LayoutParams(-1,0,1f));super.setContentView(box)
  setSupportActionBar(toolbar);supportActionBar?.setDisplayHomeAsUpEnabled(true);toolbar.navigationIcon?.setTint(-1)
  ViewCompat.setOnApplyWindowInsetsListener(box){v,i->val bars=i.getInsets(WindowInsetsCompat.Type.systemBars());v.setPadding(bars.left,bars.top,bars.right,bars.bottom);WindowInsetsCompat.CONSUMED}
 }
 override fun onSupportNavigateUp():Boolean{finish();return true}
}
