package cn.nukkit.item

class ItemGoldenAxe @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.GOLDEN_AXE, meta, count, "Golden Axe") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_GOLD

    override val isAxe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_GOLD

    override val attackDamage: Int
        get() = 3

    override fun canBreakShield(): Boolean {
        return true
    }
}