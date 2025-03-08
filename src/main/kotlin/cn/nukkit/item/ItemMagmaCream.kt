package cn.nukkit.item

/**
 * @author Leonidius20
 * @since 18.08.18
 */
class ItemMagmaCream(meta: Int, count: Int) :
    Item(ItemID.Companion.MAGMA_CREAM, meta, count, "Magma Cream") {
    @JvmOverloads
    constructor(meta: Int? = 0) : this(0, 1)
}
