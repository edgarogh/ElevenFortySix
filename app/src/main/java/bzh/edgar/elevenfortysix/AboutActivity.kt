package bzh.edgar.elevenfortysix

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList


class AboutActivity : MaterialAboutActivity() {

    override fun getActivityTitle(): CharSequence = getString(R.string.about)

    override fun getMaterialAboutList(ctx: Context): MaterialAboutList {

        // Top card: app info
        val appCard = MaterialAboutCard.Builder()
                .addItem(MaterialAboutTitleItem.Builder()
                        .text(R.string.app_name)
                        .icon(R.drawable.ic_launcher_foreground)
                        .build()
                )
                .addItem(MaterialAboutActionItem.Builder()
                        .text(R.string.version)
                        .subText(BuildConfig.VERSION_NAME)
                        .icon(R.drawable.ic_info)
                        .setOnClickAction {
                            openURL("https://github.com/edgarogh/ElevenFortySix/releases")
                        }
                        .build()
                )
                .build()

        // Middle cards: people
        val author = MaterialAboutCard.Builder()
                .title(R.string.author)
                .addItem(MaterialAboutActionItem.Builder()
                        .text("Edgar Onghena")
                        .icon(R.drawable.ic_person)
                        .build()
                )
                .addItem(MaterialAboutActionItem.Builder()
                        .text("GitHub")
                        .icon(R.drawable.ic_github_circle)
                        .setOnClickAction {
                           openURL("https://github.com/edgarogh")
                        }
                        .build()
                )
                .addItem(MaterialAboutActionItem.Builder()
                        .text(R.string.paypal)
                        .subText(getString(R.string.app_paypal_url))
                        .icon(R.drawable.ic_paypal)
                        .setOnClickAction {
                            openURL(getString(R.string.app_paypal_url))
                        }
                        .build()
                )
                .build()

        val contributor1 = MaterialAboutCard.Builder()
                .title(R.string.contributor)
                .addItem(MaterialAboutActionItem.Builder()
                        .text("Gitan des temps modernes")
                        .icon(R.drawable.ic_person_outline)
                        .build()
                )
                .build()

        // Last card: list of all libraries with their respective license
        val librariesCardBuilder = MaterialAboutCard.Builder()
                .title(R.string.libraries)

        for (license in LICENSES) {
            val licenseText = getString(license.license, license.year, license.holder)

            librariesCardBuilder.addItem(MaterialAboutActionItem.Builder()
                    .text(license.propertyName + " (${license.holder})")
                    .subTextHtml(licenseText)
                    .setOnClickAction {
                        openURL(license.propertyWebsite)
                    }
                    .icon(license.propertyIcon)
                    .build()
            )
        }

        val librariesCard = librariesCardBuilder.build()

        // Building
        return MaterialAboutList.Builder()
                .addCard(appCard)
                .addCard(author)
                .addCard(contributor1)
                .addCard(librariesCard)
                .build()
    }

    private fun openURL(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    companion object {

        class License(val propertyName: String,
                      val propertyWebsite: String,
                      @StringRes val license: Int,
                      val year: String,
                      val holder: String,
                      @DrawableRes val propertyIcon: Int = R.drawable.ic_code)

        /**
         * Manually updated list of licenses. If you add one to the project, don't forget to put it
         * here. If I forgot any license, open an issue or create a pull request.
         */
        val LICENSES = arrayOf(
                License("Android Support Libraries",
                        "https://goo.gl/mHFJW1",
                        R.string.license_apache2,
                        "2015",
                        "The Android Open Source Project"),
                License("Color Picker",
                        "https://github.com/jaredrummler/ColorPicker",
                        R.string.license_colorpicker,
                        "",
                        "Jared Rummler"),
                License("material-about-library",
                        "https://github.com/daniel-stoneuk/material-about-library",
                        R.string.license_apache2,
                        "????",
                        "daniel-stoneuk"),
                License("Material Design Icons",
                        "https://materialdesignicons.com/",
                        R.string.license_mdi,
                        "",
                        "Austin Andrews",
                        R.drawable.ic_icon_list),
                License("Icons8",
                        "https://icons8.com/",
                        R.string.license_icons8,
                        "",
                        "Icons8",
                        R.drawable.ic_icon_list),
        )

    }

}
