package bzh.edgar.elevenfortysix.preferences

import android.content.Context
import android.content.Intent
import android.preference.Preference
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import bzh.edgar.elevenfortysix.R

class CreateShortcutPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    init {
        widgetLayoutResource = R.layout.pref_time_picker
    }

    override fun onCreateView(parent: ViewGroup): View {
        super.onCreateView(parent)

        val v = LayoutInflater.from(context).inflate(R.layout.pref_time_picker, parent, false)

        val createShortcutButton = v.findViewById<Button>(android.R.id.button1)
        createShortcutButton.setOnClickListener { _ -> createShortcut() }

        return v
    }

    private fun createShortcut() {
        val icon = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher)

        val intent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")

        val launchIntent = context.packageManager
                .getLaunchIntentForPackage(context.packageName)

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent)
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "29:90")
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon)

        context.applicationContext.sendBroadcast(intent)
    }

}
