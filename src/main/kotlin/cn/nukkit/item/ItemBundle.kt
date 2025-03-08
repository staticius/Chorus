package cn.nukkit.item

open class ItemBundle @JvmOverloads constructor(id: String = ItemID.Companion.BUNDLE) : Item(id) {
    override val maxStackSize: Int
        get() = 1
}

