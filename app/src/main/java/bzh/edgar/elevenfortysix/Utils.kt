package bzh.edgar.elevenfortysix

import java.util.*

fun Timer.schedule(period: Long, task: (()->Unit)) {
    this.schedule(object : TimerTask() {
        override fun run() {
            task()
        }
    }, 0, period)
}
