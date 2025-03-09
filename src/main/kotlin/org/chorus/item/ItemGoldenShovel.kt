package org.chorus.item

class ItemGoldenShovel @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.GOLDEN_SHOVEL, meta, count, "Golden Shovel") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_GOLD

    override val isShovel: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_GOLD

    override val attackDamage: Int
        get() = 1
}