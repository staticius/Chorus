package org.chorus.item

class ItemBakedPotato : ItemFood(ItemID.Companion.BAKED_POTATO) {
    override val foodRestore: Int
        get() = 5

    override val saturationRestore: Float
        get() = 7.2f
}