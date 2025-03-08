package cn.nukkit.item

class ItemIronBoots @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemArmor(ItemID.Companion.IRON_BOOTS, meta, count, "Iron Boots") {
    override val tier: Int
        get() = ItemArmor.Companion.TIER_IRON

    override val isBoots: Boolean
        get() = true

    override val armorPoints: Int
        get() = 2

    override val maxDurability: Int
        get() = 196
}