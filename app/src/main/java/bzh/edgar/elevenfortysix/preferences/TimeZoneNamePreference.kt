package bzh.edgar.elevenfortysix.preferences

import android.content.Context
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import bzh.edgar.elevenfortysix.R

class TimeZoneNamePreference(context: Context, attr: AttributeSet?) :
        DialogPreference(context, attr) {

    var key2: String? = null

    var defaultDstWinter = ""
    var defaultDstSummer = ""

    private var view: View? = null
    private val editDstWinter by lazy { view!!.findViewById<EditText>(R.id.edit_dst_winter) }
    private val editDstSummer by lazy { view!!.findViewById<EditText>(R.id.edit_dst_summer) }

    override fun onCreateDialogView(): View {
        view = View.inflate(context, R.layout.dialog_timezone_name, null)
        return view!!
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        editDstWinter.apply {
            text.clear()
            append(sharedPreferences.getString(key, defaultDstWinter))
        }
        editDstSummer.apply {
            text.clear()
            append(sharedPreferences.getString(key2, defaultDstSummer))
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val editor = sharedPreferences.edit()
            editor.putString(key, editDstWinter.text.toString())
            editor.putString(key2, editDstSummer.text.toString())
            editor.apply()
        }
    }

    override fun setDefaultValue(defaultValue: Any?) = throw NotImplementedError()

}