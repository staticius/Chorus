package org.chorus.item

class ItemGoldenPickaxe @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.GOLDEN_PICKAXE, meta, count, "Golden Pickaxe") {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_GOLD

    override val isPickaxe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_GOLD

    override val attackDamage: Int
        get() = 2
}