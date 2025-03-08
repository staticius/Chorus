package cn.nukkit.item

class ItemDiamondAxe : ItemTool(ItemID.Companion.DIAMOND_AXE) {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_DIAMOND

    override val isAxe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_DIAMOND

    override val attackDamage: Int
        get() = 6

    override fun canBreakShield(): Boolean {
        return true
    }
}