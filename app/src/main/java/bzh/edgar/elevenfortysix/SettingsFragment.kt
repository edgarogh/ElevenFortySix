package bzh.edgar.elevenfortysix

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import bzh.edgar.elevenfortysix.R
import bzh.edgar.elevenfortysix.preferences.TimeZoneNamePreference

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx: Context = activity

        addPreferencesFromResource(R.xml.preferences)

        // Adding a preference item for each timezone group
        val category = preferenceManager.findPreference("category_timezones") as PreferenceCategory
        for (offsets in ClockSectors.sectors()) {
            // Used to build the timezone group name. i.e : UTC0 UTC12
            val builder = StringBuilder()
            for (offset in offsets) {
                builder.append("UTC").append(offset).append(' ')
            }

            val e = TimeZoneNamePreference(ctx, null).apply {
                defaultDstWinter = ClockSectors.getDefaultNameForSector(ctx, offsets[0], false)
                defaultDstSummer = ClockSectors.getDefaultNameForSector(ctx, offsets[0], true)
                key = getString(R.string.pref_sector) + offsets[0]
                key2 = getString(R.string.pref_sector) + offsets[0] + "_SUMMER"
                title = builder.toString()
            }

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
