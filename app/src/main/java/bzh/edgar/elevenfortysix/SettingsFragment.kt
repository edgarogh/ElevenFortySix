package bzh.edgar.elevenfortysix

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import bzh.edgar.elevenfortysix.preferences.DialogPreferenceDialogHolder
import bzh.edgar.elevenfortysix.preferences.TimeZoneNamePreference

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx: Context = requireContext()

        addPreferencesFromResource(R.xml.preferences)

        // Adding a preference item for each timezone group
        val category = preferenceManager.findPreference<PreferenceCategory>("category_timezones")!!
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
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        when (preference) {
            is DialogPreferenceDialogHolder -> {
                val dialog = preference.newDialog()
                dialog.arguments = Bundle(1).apply { putString("key", preference.key) }
                dialog.setTargetFragment(this, 0)
                dialog.show(parentFragmentManager, "")
            }
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}
