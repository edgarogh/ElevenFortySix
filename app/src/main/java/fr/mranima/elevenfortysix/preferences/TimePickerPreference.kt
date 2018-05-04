package fr.mranima.elevenfortysix.preferences

import android.content.Context
import android.preference.DialogPreference
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TimePicker

class TimePickerPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    private val picker: TimePicker = TimePicker(context).apply { setIs24HourView(true) }

    private val savedTime: Time
        get() = Time(getPersistedInt(Time(11, 46).time))

    init {
        setTime(savedTime)
    }

    override fun onCreateDialogView(): View = picker

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        val time = savedTime
        picker.currentHour = time.hour
        picker.currentMinute = time.minute
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val t = Time(picker.currentHour, picker.currentMinute)
            persistInt(t.time)
            setTime(t)
        }
    }

    private fun setTime(time: Time) {
        Log.d("TimePicker", "Set time" + time.toString())
        summary = time.toString()
    }

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

        override fun toString(): String = hour.toString() + ":" + minute
    }

}
