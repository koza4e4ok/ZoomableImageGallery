package unicreo.com.zoomableimagegallery

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.RelativeLayout
import java.util.*

class ImageViewerView(context: Context?) : RelativeLayout(
    context
), OnDismissListener, SwipeToDismissListener.OnViewMoveListener {

    private var backgroundView: View? = null
    private var pager: MultiTouchViewPager? = null
    private var adapter: ImageViewerAdapter? = null
    private var directionDetector: SwipeDirectionDetector? = null
    private var scaleDetector: ScaleGestureDetector? = null

    private lateinit var dismissContainer: View
    private var swipeDismissListener: SwipeToDismissListener? = null

    private var direction: SwipeDirectionDetector.Direction? = null

    private var wasScaled: Boolean = false
    private var onDismissListener: OnDismissListener? = null

    init {
        init()
    }

    fun setUrls(urls: ArrayList<String>, startPosition: Int) {
        adapter = ImageViewerAdapter(getContext(), urls)
        pager!!.adapter = adapter
        setStartPosition(startPosition)
    }

    override fun setBackgroundColor(color: Int) {
        findViewById<View>(R.id.backgroundView)
            .setBackgroundColor(color)
    }

    private fun init() {
        View.inflate(getContext(), R.layout.image_viewer, this)

        backgroundView = findViewById(R.id.backgroundView)
        pager = findViewById(R.id.pager) as MultiTouchViewPager

        dismissContainer = findViewById(R.id.container)
        swipeDismissListener = SwipeToDismissListener(findViewById(R.id.dismissView), this, this)
        dismissContainer!!.setOnTouchListener(swipeDismissListener)

        directionDetector = object : SwipeDirectionDetector(getContext()) {
            override fun onDirectionDetected(direction: Direction) {
                this@ImageViewerView.direction = direction
            }
        }

        scaleDetector = ScaleGestureDetector(
            getContext(),
            ScaleGestureDetector.SimpleOnScaleGestureListener()
        )
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        scaleDetector!!.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_UP) {
            direction = null
            wasScaled = false
            pager!!.dispatchTouchEvent(event)
            swipeDismissListener!!.onTouch(dismissContainer, event)
        }

        if (event.action == MotionEvent.ACTION_DOWN) {
            swipeDismissListener!!.onTouch(dismissContainer, event)
            pager!!.dispatchTouchEvent(event)
        }

        if (direction == null) {
            if (scaleDetector!!.isInProgress || event.pointerCount > 1) {
                wasScaled = true
                return pager!!.dispatchTouchEvent(event)
            }
        }

        if (!adapter!!.isScaled(pager!!.currentItem)) {
            directionDetector!!.onTouchEvent(event)
            if (direction != null) {
                when (direction) {
                    SwipeDirectionDetector.Direction.UP, SwipeDirectionDetector.Direction.DOWN -> {
                        if (!wasScaled && pager!!.isScrolled) {
                            return swipeDismissListener!!.onTouch(dismissContainer, event)
                        }
                    }
                    SwipeDirectionDetector.Direction.LEFT, SwipeDirectionDetector.Direction.RIGHT -> return pager!!.dispatchTouchEvent(
                        event
                    )
                }
            }
            return true
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDismiss() {
        onDismissListener?.onDismiss()
    }

    override fun onViewMove(translationY: Float, translationLimit: Int) {
        val alpha = 1.0f - 1.0f / translationLimit.toFloat() / 4f * Math.abs(translationY)
        backgroundView!!.alpha = alpha
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    fun resetScale() {
        adapter!!.resetScale(pager!!.currentItem)
    }

    fun isScaled(): Boolean {
        return adapter!!.isScaled(pager!!.currentItem)
    }

    private fun setStartPosition(position: Int) {
        pager!!.currentItem = position
    }

}
