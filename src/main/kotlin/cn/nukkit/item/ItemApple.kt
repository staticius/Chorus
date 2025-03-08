package cn.nukkit.item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemApple : ItemFood(ItemID.Companion.APPLE) {
    override val foodRestore: Int
        get() = 4

    override val saturationRestore: Float
        get() = 2.4f
}
