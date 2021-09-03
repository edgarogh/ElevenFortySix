package bzh.edgar.elevenfortysix

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import bzh.edgar.elevenfortysix.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private var timer: Timer? = null
    private val backgroundColor: Int by lazy {
        ContextCompat.getColor(this, R.color.colorPrimary)
    }

    private val root by lazy { findViewById<LinearLayout>(R.id.root) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val clock by lazy { findViewById<Clock>(R.id.clock) }

    private var itemCenterX: Int = 0
    private var itemCenterY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
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

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, config: Configuration) {
        toolbar.visibility = if (isInMultiWindowMode) View.GONE else View.VISIBLE
        super.onMultiWindowModeChanged(isInMultiWindowMode, config)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean,
                                               config: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, config)
        root.setBackgroundColor(if (isInPictureInPictureMode) Color.TRANSPARENT else backgroundColor)
    }

    override fun onStart() {
        super.onStart()

        clock.redraw()
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
    }

}
