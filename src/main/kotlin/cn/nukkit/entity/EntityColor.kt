package cn.nukkit.entity

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.utils.DyeColor
import java.util.concurrent.*

interface EntityColor : EntityComponent {
    fun setColor(color: DyeColor) {
        getMemoryStorage().put<Byte>(CoreMemoryTypes.Companion.COLOR, Integer.valueOf(color.getWoolData()).toByte())
    }

    fun setColor2(color: DyeColor) {
        getMemoryStorage().put<Byte>(CoreMemoryTypes.Companion.COLOR2, Integer.valueOf(color.getWoolData()).toByte())
    }

    fun getColor(): DyeColor {
        return DyeColor.getByWoolData(getMemoryStorage().get<Byte>(CoreMemoryTypes.Companion.COLOR).toInt())
    }

    fun getColor2(): DyeColor {
        return DyeColor.getByWoolData(getMemoryStorage().get<Byte>(CoreMemoryTypes.Companion.COLOR2).toInt())
    }

    fun hasColor(): Boolean {
        return getMemoryStorage().notEmpty(CoreMemoryTypes.Companion.COLOR)
    }

    fun hasColor2(): Boolean {
        return getMemoryStorage().notEmpty(CoreMemoryTypes.Companion.COLOR2)
    }

    fun getRandomColor(): DyeColor {
        val random: ThreadLocalRandom = ThreadLocalRandom.current()
        val colors: Array<DyeColor> = DyeColor.entries.toTypedArray()
        val c: Int = random.nextInt(colors.size)
        return colors.get(c)
    }
}
