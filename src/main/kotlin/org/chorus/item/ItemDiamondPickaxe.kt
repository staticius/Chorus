package org.chorus.item

class ItemDiamondPickaxe : ItemTool(ItemID.Companion.DIAMOND_PICKAXE) {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_DIAMOND

    override val isPickaxe: Boolean
        get() = true

    override val tier: Int
        get() = ItemTool.Companion.TIER_DIAMOND

    override val attackDamage: Int
        get() = 5
}