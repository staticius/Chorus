package cn.nukkit.item

/**
 * @author CreeperFace
 */
abstract class ItemMusicDisc protected constructor(id: String) : Item(id) {
    override val maxStackSize: Int
        get() = 1

    abstract val soundId: String
}
