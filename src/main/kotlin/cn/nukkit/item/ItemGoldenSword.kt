package cn.nukkit.item

class ItemGoldenSword @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.GOLDEN_SWORD, meta, count, "Golden Sword") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_GOLD

    override val isSword: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_GOLD

    override val attackDamage: Int
        get() = 4
}