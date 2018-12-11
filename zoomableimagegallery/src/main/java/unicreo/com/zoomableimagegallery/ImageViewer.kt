package unicreo.com.imageviewer

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import java.util.*

class ImageViewer(private val builder: Builder) : OnDismissListener, DialogInterface.OnKeyListener {

    private val TAG = ImageViewer::class.java.simpleName

    private var dialog: AlertDialog? = null
    private var viewer: ImageViewerView? = null

    init {
        createDialog()
    }

    /**
     * Displays the built viewer if passed urls list isn't empty
     */
    fun show() {
        if (!builder.urls.isEmpty()) {
            dialog!!.show()
        } else {
            Log.e(TAG, "Urls list cannot be empty! Viewer ignored.")
        }
    }

    private fun createDialog() {
        viewer = ImageViewerView(builder.context)
        viewer!!.setUrls(builder.urls, builder.startPosition)
        viewer!!.setOnDismissListener(this)
        viewer!!.setBackgroundColor(builder.backgroundColor)

        dialog = AlertDialog.Builder(builder.context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
            .setView(viewer)
            .setOnKeyListener(this)
            .create()
    }

    /**
     * Fires when swipe to dismiss was initiated
     */
    override fun onDismiss() {
        dialog!!.cancel()
    }

    /**
     * Resets image on KeyEvent.KEYCODE_BACK to normal scale if needed, otherwise - hide the viewer.
     */
    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
            event.action == KeyEvent.ACTION_UP &&
            !event.isCanceled
        ) {
            if (viewer!!.isScaled()) {
                viewer!!.resetScale()
            } else {
                dialog.cancel()
            }
        }
        return true
    }

    /**
     * Builder class for [ImageViewer]
     */
    class Builder
    /**
     * Constructor using a context and images urls list for this builder and the [ImageViewer] it creates.
     */
        (val context: Context, val urls: ArrayList<String>) {
        @ColorInt
        var backgroundColor = Color.BLACK
        var startPosition: Int = 0

        /**
         * Constructor using a context and images urls array for this builder and the [ImageViewer] it creates.
         */
        constructor(context: Context, urls: Array<String>) : this(
            context,
            ArrayList<String>(Arrays.asList<String>(*urls))
        ) {
        }

        /**
         * Set background color resource for viewer
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorRes(@ColorRes color: Int): Builder {
            return this.setBackgroundColor(context.resources.getColor(color))
        }

        /**
         * Set background color int for viewer
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setBackgroundColor(@ColorInt color: Int): Builder {
            this.backgroundColor = color
            return this
        }

        /**
         * Set background color int for viewer
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setStartPosition(position: Int): Builder {
            this.startPosition = position
            return this
        }

        /**
         * Creates a [ImageViewer] with the arguments supplied to this builder. It does not
         * [ImageViewer.show] the dialog. This allows the user to do any extra processing
         * before displaying the dialog. Use [.show] if you don't have any other processing
         * to do and want this to be created and displayed.
         */
        fun build(): ImageViewer {
            return ImageViewer(this)
        }

        /**
         * Creates a [ImageViewer] with the arguments supplied to this builder and
         * [ImageViewer.show]'s the dialog.
         */
        fun show(): ImageViewer {
            val dialog = build()
            dialog.show()
            return dialog
        }
    }
}