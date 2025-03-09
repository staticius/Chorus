package org.chorus.item

class ItemDiamondSword : ItemTool(ItemID.Companion.DIAMOND_SWORD) {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_DIAMOND

    override val isSword: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_DIAMOND

    override val attackDamage: Int
        get() = 7
}