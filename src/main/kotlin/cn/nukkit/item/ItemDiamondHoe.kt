package cn.nukkit.item

class ItemDiamondHoe : ItemTool(ItemID.Companion.DIAMOND_HOE) {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_DIAMOND

    override val isHoe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_DIAMOND
}