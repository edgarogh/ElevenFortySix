package bzh.edgar.elevenfortysix.preferences

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat

class TimePickerPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs), DialogPreferenceDialogHolder {

    private val savedTime: Time
        get() = Time(getPersistedInt(Time(11, 46).time))

    override fun onAttached() {
        setTime(savedTime)
    }

    private fun setTime(time: Time) {
        summary = time.toString()
    }

    override fun newDialog(): PreferenceDialogFragmentCompat = Dialog(this)

    private class Time {
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

        val hourString get() = hour.toString().padStart(2, '0')
        val minuteString get() = minute.toString().padStart(2, '0')

        override fun toString(): String = "$hourString:$minuteString"
    }

    class Dialog(private val preference: TimePickerPreference) : PreferenceDialogFragmentCompat() {
        private val picker by lazy { TimePicker(context).apply { setIs24HourView(true) } }

        override fun onCreateDialogView(context: Context?) = picker

        override fun onBindDialogView(view: View?) {
            super.onBindDialogView(view)
            val time = preference.savedTime
            picker.currentHour = time.hour
            picker.currentMinute = time.minute
        }

        override fun onDialogClosed(positiveResult: Boolean) {
            if (positiveResult) {
                val t = Time(picker.currentHour, picker.currentMinute)
                preference.persistInt(t.time)
                preference.setTime(t)
            }
        }
    }

}
