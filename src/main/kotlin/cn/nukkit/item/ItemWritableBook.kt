package cn.nukkit.item

/**
 * alias BookAndQuill
 */
class ItemWritableBook : ItemBookWritable(ItemID.Companion.WRITABLE_BOOK) {
    override val maxStackSize: Int
        get() = 1
}