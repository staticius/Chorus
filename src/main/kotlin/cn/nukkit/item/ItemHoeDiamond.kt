package cn.nukkit.item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemHoeDiamond @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.DIAMOND_HOE, meta, count, "Diamond Hoe") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_DIAMOND

    override val isHoe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_DIAMOND
}
