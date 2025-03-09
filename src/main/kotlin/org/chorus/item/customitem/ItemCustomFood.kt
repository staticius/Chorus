package org.chorus.item.customitem

import org.chorus.item.ItemFood

/**
 * @author lt_name
 */
abstract class ItemCustomFood(id: String) : ItemFood(id), CustomItem {
    val isDrink: Boolean
        get() = false
}
