package org.chorus.item

import cn.nukkit.entity.EntityID

class ItemExperienceBottle @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ProjectileItem(ItemID.Companion.EXPERIENCE_BOTTLE, meta, count, "Bottle o' Enchanting") {
    override val projectileEntityType: String
        get() = EntityID.XP_BOTTLE

    override val throwForce: Float
        get() = 1f
}