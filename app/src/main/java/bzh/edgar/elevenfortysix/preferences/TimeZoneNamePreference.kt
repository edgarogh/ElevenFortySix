package bzh.edgar.elevenfortysix.preferences

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import bzh.edgar.elevenfortysix.R

class TimeZoneNamePreference(context: Context, attr: AttributeSet?) :
        DialogPreference(context, attr), DialogPreferenceDialogHolder {

    var key2: String? = null

    var defaultDstWinter = ""
    var defaultDstSummer = ""

    override fun newDialog() = Dialog(this)

    override fun setDefaultValue(defaultValue: Any?) = throw NotImplementedError()

    class Dialog(private val preference: TimeZoneNamePreference) : PreferenceDialogFragmentCompat() {
        private val dialogView by lazy {
            View.inflate(context, R.layout.dialog_timezone_name, null)
        }

        private val editDstWinter by lazy { dialogView.findViewById<EditText>(R.id.edit_dst_winter) }
        private val editDstSummer by lazy { dialogView.findViewById<EditText>(R.id.edit_dst_summer) }

        override fun onCreateDialogView(context: Context?): View = dialogView

        override fun onBindDialogView(view: View?) {
            super.onBindDialogView(view)
            editDstWinter.apply {
                text.clear()
                append(preference.sharedPreferences.getString(preference.key, preference.defaultDstWinter))
            }
            editDstSummer.apply {
                text.clear()
                append(preference.sharedPreferences.getString(preference.key2, preference.defaultDstSummer))
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            outState.putParcelableArray(this.javaClass.name, arrayOf(
                    editDstWinter.onSaveInstanceState(),
                    editDstSummer.onSaveInstanceState(),
            ))
        }

        override fun onViewStateRestored(savedInstanceState: Bundle?) {
            super.onViewStateRestored(savedInstanceState)
            savedInstanceState?.getParcelableArray(this.javaClass.name)?.run {
                editDstWinter.onRestoreInstanceState(get(0))
                editDstWinter.onRestoreInstanceState(get(1))
            }
        }

        override fun onDialogClosed(positiveResult: Boolean) {
            if (positiveResult) {
                val editor = preference.sharedPreferences.edit()
                val winter = if (editDstWinter.text.isNotEmpty()) { editDstWinter.text.toString() } else { null }
                val summer = if (editDstWinter.text.isNotEmpty()) { editDstWinter.text.toString() } else { null }
                editor.putString(preference.key, winter)
                editor.putString(preference.key2, summer)
                editor.apply()
            }
        }
    }

}
