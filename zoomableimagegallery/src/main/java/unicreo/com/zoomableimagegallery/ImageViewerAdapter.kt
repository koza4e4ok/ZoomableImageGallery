package unicreo.com.imageviewer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import java.util.*

class ImageViewerAdapter(private val context: Context, private val urls: ArrayList<String>) : PagerAdapter() {


    private var drawees: ArrayList<PhotoView>? = null

    init {
        generateDrawees()
    }

    override fun getCount(): Int {
        return urls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoDraweeView = drawees!![position]

        try {
            container.addView(
                photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return photoDraweeView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as PhotoView)
    }

    fun isScaled(index: Int): Boolean {
        return drawees!![index].getScale() > 1.0f
    }

    fun resetScale(index: Int) {
        drawees!![index].setScale(1.0f, true)
    }

    private fun generateDrawees() {
        drawees = ArrayList<PhotoView>()
        for (url in urls) {
            val drawee = PhotoView(context)
            Glide.with(context).load(url).into(drawee)
            drawees!!.add(drawee)
        }
    }

}
