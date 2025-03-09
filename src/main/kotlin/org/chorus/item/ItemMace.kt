package org.chorus.item

import org.chorus.entity.*
import org.chorus.math.NukkitMath

class ItemMace : ItemTool(ItemID.Companion.MACE) {
    override val maxDurability: Int
        get() = 501

    override fun getAttackDamage(entity: Entity): Int {
        val height = NukkitMath.floorDouble(entity.highestPosition - entity.position.y)
        if (height < 1.5f) return 6
        var damage = 0
        for (i in 0..height) {
            if (i < 3) damage += 4
            else if (i < 8) damage += 2
            else damage++
        }

        entity.resetFallDistance()
        return damage
    }
}