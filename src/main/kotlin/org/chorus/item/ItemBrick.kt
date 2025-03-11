package org.chorus.item


class ItemBrick @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.BRICK, 0, count, "Brick")
