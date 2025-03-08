package cn.nukkit.item.customitem

import cn.nukkit.item.ItemFood

/**
 * @author lt_name
 */
abstract class ItemCustomFood(id: String) : ItemFood(id), CustomItem {
    val isDrink: Boolean
        get() = false
}
