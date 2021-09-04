package bzh.edgar.elevenfortysix

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Rational
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import java.util.*


class MainActivity : AppCompatActivity() {

    private var timer: Timer? = null

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val clock by lazy { findViewById<Clock>(R.id.clock) }

    private var itemCenterX: Int = 0
    private var itemCenterY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isInPictureInPictureMode) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }

            clock.setOnLongClickListener {
                enterPictureInPictureMode(PIP_PARAMS)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        // Super dirty, I need to find another way to compute the Toolbar item's center
        clock.postDelayed({
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            for (i in 0 until toolbar.childCount) {
                val child = toolbar.getChildAt(i)
                if (child is ActionMenuView) {
                    val rect = Rect()
                    child.getLocalVisibleRect(rect)
                    itemCenterX = window.decorView.width - rect.centerX()
                    itemCenterY = rect.centerY()
                }
            }
        }, 0)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                i.putExtra(SettingsActivity.EXTRA_CIRCULAR_REVEAL_X, itemCenterX)
                i.putExtra(SettingsActivity.EXTRA_CIRCULAR_REVEAL_Y, itemCenterY)
                startActivity(i)
                overridePendingTransition(0, 0)
            }
            else -> return false
        }
        return true
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        toolbar.visibility = if (isInPictureInPictureMode) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        clock.redraw()

        updateTitle()

        timer = Timer()
        timer?.schedule(1000) {
            runOnUiThread { clock.redraw() }
        }
    }

    override fun onStop() {
        timer?.cancel()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        clock.update()
        updateTitle()
    }

    private fun updateTitle() {
        val time = Time.fromSharedPreferences(this, R.string.pref_time)
            .run {
                if (this == Time.DEFAULT) {
                    null
                } else {
                    this
                }
            }

        var textView: AppCompatTextView? = null
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is AppCompatTextView) {
                textView = child
            }
        }

        if (time == null) {
            toolbar.title = Time.DEFAULT.toString()
        } else {
            toolbar.title = time.toString()
            @SuppressLint("SetTextI18n")
            if (textView != null) {
                textView.setText("${Time.DEFAULT} $time", TextView.BufferType.SPANNABLE)
                (textView.text as Spannable).setSpan(
                    StrikethroughSpan(), // TODO hide from accessibility tools
                    0,
                    5,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val PIP_PARAMS =
            PictureInPictureParams.Builder().setAspectRatio(Rational(1, 1)).build()
    }

}
