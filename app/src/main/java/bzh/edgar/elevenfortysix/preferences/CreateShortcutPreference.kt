package bzh.edgar.elevenfortysix.preferences

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import bzh.edgar.elevenfortysix.R

@RequiresApi(Build.VERSION_CODES.O)
class CreateShortcutPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    init {
        layoutResource = R.layout.pref_time_picker
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        val createShortcutButton = holder!!.findViewById(android.R.id.button1) as Button
        createShortcutButton.setOnClickListener { createShortcut() }
    }

    // TODO
    private fun createShortcut() {
        val launchIntent = context.packageManager
                .getLaunchIntentForPackage(context.packageName)!!

        val shortcutManager = context.getSystemService(ShortcutManager::class.java)
        shortcutManager.requestPinShortcut(
                ShortcutInfo.Builder(context, "29:90")
                        .setIntent(launchIntent)
                        .setShortLabel("29:90")
                        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                        .build(),
                null)
    }

}
