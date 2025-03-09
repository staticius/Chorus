package org.chorus.item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemBook : Item(ItemID.Companion.BOOK) {
    override val enchantAbility: Int
        get() = 1
}
