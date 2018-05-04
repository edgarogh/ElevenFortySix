package fr.mranima.elevenfortysix

import android.content.Context
import android.preference.PreferenceManager
import java.util.*

object ClockSectors {

    private val sectors = ArrayList<IntArray>(12)

    init {
        addSector(+0, +12)
        addSector(+1, +13, -11)
        addSector(+2, +14, -10)
        addSector(+3, -9)
        addSector(+4, -8)
        addSector(+5, -7)
        addSector(+6, -6)
        addSector(+7, -5)
        addSector(+8, -4)
        addSector(+9, -3)
        addSector(+10, -2)
        addSector(+11, -1)
    }

    fun sectors(): Iterable<IntArray> {
        return sectors
    }

    fun getDefaultNameForSector(ctx: Context, index: Int, dst: Boolean): String {
        if (index >= 12 || index < 0)
            throw IndexOutOfBoundsException("Sector name too high: $index")

        val array = if (dst) R.array.sectors_dst else R.array.sectors
        return ctx.resources.getStringArray(array)[index]
    }

    fun getSectorNames(ctx: Context, dst: Boolean): Array<String> {
        val s = arrayOfNulls<String>(12)
        s.fill("")

        for (i in s.indices) {
            val key = ctx.getString(R.string.pref_sector) + i + if (dst) "_SUMMER" else ""
            val name = PreferenceManager.getDefaultSharedPreferences(ctx)
                    .getString(key, null)

            s[i] = name ?: getDefaultNameForSector(ctx, i, dst)
        }
        return s.filterNotNull().toTypedArray()
    }

    private fun addSector(vararg utcOffset: Int) {
        if (utcOffset.isEmpty()) return
        sectors.add(utcOffset[0], utcOffset)
    }

}
