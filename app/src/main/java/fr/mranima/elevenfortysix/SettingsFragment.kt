package fr.mranima.elevenfortysix

import android.content.Context
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx: Context = activity

        addPreferencesFromResource(R.xml.preferences)

        val category = preferenceManager.findPreference("category_timezones") as PreferenceCategory
        for (offsets in ClockSectors.sectors()) {
            val builder = StringBuilder()
            for (offset in offsets) {
                builder.append("UTC").append(offset).append(' ')
            }
            val e = EditTextPreference(ctx)
            e.setDefaultValue(ClockSectors.getDefaultNameForSector(ctx, offsets[0]))
            e.key = getString(R.string.pref_sector) + offsets[0]
            e.title = builder.toString()
            category.addPreference(e)
        }

        // TODO Remove when I'll add the possibility to change the hour
        val catTime = preferenceManager.findPreference("category_time") as PreferenceCategory
        if (!BuildConfig.DEBUG) {
            catTime.isEnabled = false
            catTime.title = catTime.title.toString() + " " + resources.getString(R.string.not_implemented)
        }
    }

}
