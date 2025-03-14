package org.chorus.item

class ItemDiamondHoe : ItemTool(ItemID.Companion.DIAMOND_HOE) {
    override val maxDurability: Int
        get() = DURABILITY_DIAMOND

    override val isHoe: Boolean
        get() = true

    override val tier: Int
        get() = TIER_DIAMOND
}