package fr.mranima.elevenfortysix

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast
import java.util.*

fun Timer.schedule(period: Long, task: (()->Unit)) {
    this.schedule(object : TimerTask() {
        override fun run() {
            task()
        }
    }, 0, period)
}

fun Context.toast(@StringRes message: Int, long: Boolean = false) {
    toast(getString(message), long)
}

fun Context.toast(message: String, long: Boolean = false) {
    Toast.makeText(this, message, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
                .show()
}