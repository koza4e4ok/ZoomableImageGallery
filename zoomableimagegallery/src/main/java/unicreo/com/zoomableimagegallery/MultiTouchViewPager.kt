package unicreo.com.zoomableimagegallery

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

internal class MultiTouchViewPager : androidx.viewpager.widget.ViewPager {

    private var isDisallowIntercept: Boolean = false
    var isScrolled = true
        private set

    constructor(context: Context) : super(context) {
        setScrollStateListener()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setScrollStateListener()
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        isDisallowIntercept = disallowIntercept
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1 && isDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false)
            val handled = super.dispatchTouchEvent(ev)
            requestDisallowInterceptTouchEvent(true)
            return handled
        } else {
            return super.dispatchTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (ev.pointerCount > 1) {
            false
        } else {
            try {
                super.onInterceptTouchEvent(ev)
            } catch (ex: IllegalArgumentException) {
                false
            }

        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            return false
        }

    }

    private fun setScrollStateListener() {
        addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                isScrolled = positionOffset == 0.0f
            }
        })
    }
}