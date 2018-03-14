package fr.mranima.elevenfortysix

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.AccelerateInterpolator
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var revealX: Int = 0
    private var revealY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)

        supportActionBar!!.setTitle(R.string.settings)

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, SettingsFragment())
            transaction.commit()

        root.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {

            root.visibility = View.INVISIBLE
            root.elevation = 16f

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)


            val viewTreeObserver = root.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else {
            root.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("NewApi")
    fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = (Math.max(root.width, root.height) * 1.1).toFloat()

            // create the animator for this view (the start radius is zero)
            val circularReveal = ViewAnimationUtils.createCircularReveal(root, x, y, 0f, finalRadius)
            circularReveal.duration = 300
            circularReveal.interpolator = AccelerateInterpolator()

            // make the view visible and start the animation
            root.visibility = View.VISIBLE
            circularReveal.start()
        } else {
            finish()
        }
    }

    private fun unrevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish()
        } else {
            val finalRadius = (Math.max(root.width, root.height) * 1.1).toFloat()
            val circularReveal = ViewAnimationUtils.createCircularReveal(
                    root, revealX, revealY, finalRadius, 0f)

            circularReveal.duration = 300
            circularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    root.visibility = View.INVISIBLE
                    finish()
                    overridePendingTransition(0, 0)
                }
            })

            circularReveal.start()
        }
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, config: Configuration) {
        if (isInMultiWindowMode) onBackPressed()
        super.onMultiWindowModeChanged(isInMultiWindowMode, config)
    }

    override fun onBackPressed() {
        unrevealActivity()
    }

    companion object {

        const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"

    }

}
