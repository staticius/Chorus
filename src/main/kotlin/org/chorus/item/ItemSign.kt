package org.chorus.item

import cn.nukkit.block.*

/**
 * 注意做好sign和standing_sign方块的映射关系，物品通过this.block指定，方块通过toItem指定
 */
abstract class ItemSign protected constructor(id: String) : Item(id) {
    init {
        if (id == ItemID.Companion.DARK_OAK_SIGN) {
            this.block = Block.get(BlockID.DARKOAK_STANDING_SIGN)
        } else if (id == ItemID.Companion.OAK_SIGN) {
            this.block = Block.get(BlockID.STANDING_SIGN)
        } else {
            this.block = Block.get(id.replace("_sign", "_standing_sign"))
        }
    }

    override val maxStackSize: Int
        get() = 16
}
