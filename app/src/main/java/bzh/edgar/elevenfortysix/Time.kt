package bzh.edgar.elevenfortysix

import android.content.Context
import androidx.annotation.StringRes
import androidx.preference.PreferenceManager

class Time {
    var time: Int = 0
        internal set

    val hour: Int
        get() = time / 60

    val minute: Int
        get() = time % 60

    constructor(time: Int) {
        this.time = time
    }

    constructor(hour: Int, minute: Int) {
        this.time = 60 * hour + minute
    }

    override fun equals(other: Any?) = (this.time == (other as? Time)?.time)

    override fun hashCode() = time

    private val hourString get() = hour.toString().padStart(2, '0')
    private val minuteString get() = minute.toString().padStart(2, '0')

    override fun toString(): String = "$hourString:$minuteString"

    companion object {
        val DEFAULT = Time(11, 46)

        fun fromSharedPreferences(
            context: Context,
            @StringRes key: Int,
        ) = Time(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.resources.getString(key), DEFAULT.time)
        )
    }
}
