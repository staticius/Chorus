package org.chorus.item

import cn.nukkit.Player

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemMushroomStew @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.MUSHROOM_STEW, 0, count, "Mushroom Stew") {
    override val maxStackSize: Int
        get() = 1

    override val foodRestore: Int
        get() = 6

    override val saturationRestore: Float
        get() = 7.2f

    override fun onEaten(player: Player): Boolean {
        player.getInventory().addItem(ItemBowl())

        return true
    }
}
