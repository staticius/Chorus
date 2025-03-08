package cn.nukkit.level.updater.item

import cn.nukkit.level.updater.Updater
import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext
import cn.nukkit.nbt.tag.CompoundTag
import lombok.experimental.UtilityClass
import java.util.function.Consumer

@UtilityClass
object ItemUpdaters {
    private val CONTEXT: CompoundTagUpdaterContext
    val latestVersion: Int

    init {
        val updaters: MutableList<Updater> = ArrayList()
        updaters.add(ItemUpdater_1_20_60.Companion.INSTANCE)
        updaters.add(ItemUpdater_1_20_70.Companion.INSTANCE)
        updaters.add(ItemUpdater_1_20_80.Companion.INSTANCE)
        updaters.add(ItemUpdater_1_21_0.Companion.INSTANCE)
        updaters.add(ItemUpdater_1_21_20.Companion.INSTANCE)
        updaters.add(ItemUpdater_1_21_30.Companion.INSTANCE)
        updaters.add(ItemUpdater_1_21_40.Companion.INSTANCE)

        val context = CompoundTagUpdaterContext()
        updaters.forEach(Consumer { updater: Updater -> updater.registerUpdaters(context) })
        CONTEXT = context
        latestVersion = context.latestVersion
    }

    @JvmStatic
    fun updateItem(tag: CompoundTag, version: Int): CompoundTag? {
        return CONTEXT.update(tag, version)
    }
}
