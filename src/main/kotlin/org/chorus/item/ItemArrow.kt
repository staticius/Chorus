package org.chorus.item

import cn.nukkit.entity.effect.PotionType

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemArrow @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.ARROW, meta, count, GENERIC_NAME) {
    init {
        updateName()
    }

    override var damage: Int
        get() = super.damage
        set(meta) {
            super.setDamage(meta)
            updateName()
        }

    private fun updateName() {
        val type = getDamage()
        if (type <= 0) {
            name = GENERIC_NAME
            return
        }

        val potion = PotionType.get(type - 1)
        this.name = when (potion.stringId) {
            "minecraft:water" -> "Arrow of Splashing"
            "minecraft:mundane", "minecraft:long_mundane", "minecraft:thick", "minecraft:awkward" -> "Tipped Arrow"
            else -> ItemPotion.Companion.buildName(potion, GENERIC_NAME, false)
        }
    }

    val tippedArrowPotion: PotionType?
        get() {
            val damage = getDamage()
            if (damage > 0) {
                return PotionType.get(damage - 1)
            }
            return null
        }

    companion object {
        private const val GENERIC_NAME = "Arrow"
    }
}
