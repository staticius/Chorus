package cn.nukkit.item

class ItemCookedBeef : ItemFood(ItemID.Companion.COOKED_BEEF) {
    override val foodRestore: Int
        get() = 8

    override val saturationRestore: Float
        get() = 12.8f
}