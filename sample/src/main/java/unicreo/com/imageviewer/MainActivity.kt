package unicreo.com.imageviewer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val urlList: ArrayList<String> = ArrayList()
        urlList.add("https://i.guim.co.uk/img/media/12d620faf92a9ab33667757ac2c4b8e6ff09fd16/0_0_3208_2197/master/3208.jpg?width=1010&quality=85&auto=format&fit=max&s=061ef95c257a94ef40b1ffdf8acbed5b")
        urlList.add("https://i.guim.co.uk/img/media/db780e9efdca37117b4f93b0c220f3441bfd3ea8/357_0_1812_1087/master/1812.jpg?width=1010&quality=85&auto=format&fit=max&s=bbe94b66d582f300fbd505edbcf8088f")
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener { ImageViewer.Builder(this, urlList).show() }
    }
}
