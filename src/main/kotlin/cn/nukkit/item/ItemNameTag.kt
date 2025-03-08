package cn.nukkit.item

class ItemNameTag @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.NAME_TAG, meta, count, "Name Tag")
