package cn.nukkit.item

import cn.nukkit.utils.DyeColor

class ItemBoneMeal : ItemDye(ItemID.Companion.BONE_MEAL) {
    override val isFertilizer: Boolean
        get() = true

    override val dyeColor: DyeColor
        get() = DyeColor.WHITE

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}