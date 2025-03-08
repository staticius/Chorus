package cn.nukkit.item

class ItemWoodenSword @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.WOODEN_SWORD, meta, count, "Wooden Sword") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_WOODEN

    override val isSword: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_WOODEN

    override val attackDamage: Int
        get() = 4
}