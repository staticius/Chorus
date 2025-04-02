package org.chorus.entity

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.utils.DyeColor
import java.util.concurrent.*

interface EntityColor : EntityComponent {
    fun setColor(color: DyeColor) {
        getMemoryStorage()[CoreMemoryTypes.COLOR] = Integer.valueOf(color.woolData).toByte()
    }

    fun setColor2(color: DyeColor) {
        getMemoryStorage()[CoreMemoryTypes.COLOR2] = Integer.valueOf(color.woolData).toByte()
    }

    fun getColor(): DyeColor {
        return DyeColor.getByWoolData(getMemoryStorage()[CoreMemoryTypes.COLOR]!!.toInt())
    }

    fun getColor2(): DyeColor {
        return DyeColor.getByWoolData(getMemoryStorage()[CoreMemoryTypes.COLOR2]!!.toInt())
    }

    fun hasColor(): Boolean {
        return getMemoryStorage().notEmpty(CoreMemoryTypes.COLOR)
    }

    fun hasColor2(): Boolean {
        return getMemoryStorage().notEmpty(CoreMemoryTypes.COLOR2)
    }

    fun getRandomColor(): DyeColor {
        val random: ThreadLocalRandom = ThreadLocalRandom.current()
        val colors: Array<DyeColor> = DyeColor.entries.toTypedArray()
        val c: Int = random.nextInt(colors.size)
        return colors[c]
    }
}
