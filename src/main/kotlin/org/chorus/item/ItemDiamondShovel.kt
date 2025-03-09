package org.chorus.item

class ItemDiamondShovel : ItemTool(ItemID.Companion.DIAMOND_SHOVEL) {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_DIAMOND

    override val isShovel: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_DIAMOND

    override val attackDamage: Int
        get() = 4
}