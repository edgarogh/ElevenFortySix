package fr.mranima.elevenfortysix

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.preference.PreferenceManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import org.jetbrains.annotations.Contract
import java.util.*

class Clock : View {

    companion object {

        private val PAINT_OUTER_CIRCLE = Paint()
        private val PAINT_INNER_CIRCLE = Paint()
        private val PAINT_HAND_MINUTES = Paint()
        private val PAINT_HAND_HOUR = Paint()
        private val PAINT_TEXT = Paint()
        private val PAINT_MARKERS = Paint()

    }

    private val hour = 11
    private val minute = 46

    private var dst: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        update()
    }

    fun update() {
        PAINT_OUTER_CIRCLE.color = getColor(R.color.colorAccent, R.string.pref_color_border)
        PAINT_OUTER_CIRCLE.isAntiAlias = true
        PAINT_OUTER_CIRCLE.setShadowLayer(12f, 0f, 0f, Color.GRAY)
        setLayerType(View.LAYER_TYPE_SOFTWARE, PAINT_OUTER_CIRCLE)

        PAINT_INNER_CIRCLE.color = Color.WHITE
        PAINT_INNER_CIRCLE.isAntiAlias = true

        PAINT_HAND_MINUTES.color = getColor(R.color.handMinutes, R.string.pref_color_hand_minutes)
        PAINT_HAND_MINUTES.isAntiAlias = true
        PAINT_HAND_MINUTES.strokeWidth = 8f

        PAINT_HAND_HOUR.color = getColor(R.color.handHour, R.string.pref_color_hand_hour)
        PAINT_HAND_HOUR.isAntiAlias = true
        PAINT_HAND_HOUR.strokeWidth = 10f

        PAINT_TEXT.color = getColor(R.color.clockText, R.string.pref_color_text)
        PAINT_TEXT.isAntiAlias = true
        PAINT_TEXT.textSize = getDimen(R.dimen.clock_text).toFloat()

        PAINT_MARKERS.color = getColor(R.color.clockText, R.string.pref_color_markers)
        PAINT_MARKERS.isAntiAlias = true
        PAINT_MARKERS.strokeWidth = 8f

        dst = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(resources.getString(R.string.pref_dst), dst)
    }

    fun redraw() {
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val radius = Math.min(width, height) / 2 - 2 * getDimen(R.dimen.clock_padding)
        val cX = width / 2
        val cY = height / 2

        canvas.drawCircle(cX.toFloat(), cY.toFloat(), radius.toFloat(), PAINT_OUTER_CIRCLE)
        canvas.drawCircle(cX.toFloat(), cY.toFloat(), (radius - getDimen(R.dimen.clock_ring_width)).toFloat(), PAINT_INNER_CIRCLE)

        var i = 0
        while (i < 360) {
            val bx = (cX + radius * Math.sin(Math.toRadians(i.toDouble()))).toInt()
            val by = (cY + radius * Math.cos(Math.toRadians(i.toDouble()))).toInt()
            val x = (cX + 0.9 * radius.toDouble() * Math.sin(Math.toRadians(i.toDouble()))).toInt()
            val y = (cY + 0.9 * radius.toDouble() * Math.cos(Math.toRadians(i.toDouble()))).toInt()
            canvas.drawLine(bx.toFloat(), by.toFloat(), x.toFloat(), y.toFloat(), PAINT_MARKERS)
            i += 30
        }

        // Hands

        // Minute hand
        val c = Calendar.getInstance()
        // Number of seconds corresponding to 0-60 minutes (0-3600)
        val seconds = mod(((c.get(GregorianCalendar.MINUTE) - minute) * 60 + c.get(GregorianCalendar.SECOND)).toFloat(), 3600f)

        val minuteX = (cX + radius * Math.sin(Math.toRadians((-(seconds / 10) + 180f).toDouble()))).toInt()
        val minuteY = (cY + radius * Math.cos(Math.toRadians((-(seconds / 10) + 180f).toDouble()))).toInt()
        canvas.drawLine(cX.toFloat(), cY.toFloat(), minuteX.toFloat(), minuteY.toFloat(), PAINT_HAND_MINUTES)

        // Hour hand
        val c0 = Calendar.getInstance(TimeZone.getTimeZone("UTC+0"))
        // Number of minutes corresponding to 0-12h (0-720)
        val min = (c0.get(GregorianCalendar.MINUTE) - minute).toFloat()
        val minutes = mod((c0.get(GregorianCalendar.HOUR).toFloat() - hour) * 60f + min, 720f)

        val hourX = (cX + radius * 0.8 * Math.sin(Math.toRadians((-(minutes / 2) + 180f).toDouble()))).toInt()
        val hourY = (cY + radius * 0.8 * Math.cos(Math.toRadians((-(minutes / 2) + 180f).toDouble()))).toInt()
        canvas.drawLine(cX.toFloat(), cY.toFloat(), hourX.toFloat(), hourY.toFloat(), PAINT_HAND_HOUR)

        // Write timezones
        var index = -1
        for (sector in ClockSectors.getSectorNames(context, dst)) {
            index++
            val angle = index * -30
            val txtX = (cX + 0.7 * radius.toDouble() * Math.sin(Math.toRadians((-angle + 180f).toDouble()))).toInt()
            val txtY = (cY + 0.7 * radius.toDouble() * Math.cos(Math.toRadians((-angle + 180f).toDouble()))).toInt()
            canvas.drawTextCentered(sector, txtX, txtY, PAINT_TEXT)
        }
    }

    @Contract(pure = true)
    private fun mod(x: Float, y: Float): Float {
        val result = x % y
        return if (result < 0) result + y else result
    }

    private fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    private fun getColor(@ColorRes colorRes: Int, @StringRes prefKey: Int): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getString(prefKey), getColor(colorRes))
    }

    private fun getDimen(@DimenRes dimenRes: Int): Int {
        return context.resources.getDimensionPixelSize(dimenRes)
    }

    private fun Canvas.drawTextCentered(text: String, x: Int, y: Int, paint: Paint) {
        this.drawText(text, x - paint.measureText(text) / 2, y + paint.textSize / 2, paint)
    }

}
