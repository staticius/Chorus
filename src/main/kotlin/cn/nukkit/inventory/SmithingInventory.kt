/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.block.BlockSmithingTable
import cn.nukkit.item.Item
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

/**
 * @author joserobjr | CoolLoong
 * @since 2020-09-28
 */
class SmithingInventory(blockSmithingTable: BlockSmithingTable?) :
    ContainerInventory(blockSmithingTable, InventoryType.SMITHING_TABLE, 3), CraftTypeInventory, SoleInventory {
    override fun init() {
        val map = super.networkSlotMap()
        for (i in 0..<getSize()) {
            map!![i] = 51 + i
        }

        val map2 = super.slotTypeMap()
        map2!![0] = ContainerSlotType.SMITHING_TABLE_INPUT
        map2[1] = ContainerSlotType.SMITHING_TABLE_MATERIAL
        map2[2] = ContainerSlotType.SMITHING_TABLE_TEMPLATE
    }

    var equipment: Item?
        get() = getItem(EQUIPMENT)
        set(equipment) {
            setItem(EQUIPMENT, equipment!!)
        }

    var ingredient: Item?
        get() = getItem(INGREDIENT)
        set(ingredient) {
            setItem(INGREDIENT, ingredient!!)
        }

    var template: Item?
        get() = getItem(TEMPLATE)
        set(template) {
            setItem(TEMPLATE, template!!)
        }

    override fun onClose(who: Player) {
        super.onClose(who)

        who.giveItem(getItem(EQUIPMENT), getItem(INGREDIENT), getItem(TEMPLATE))

        this.clear(EQUIPMENT)
        this.clear(INGREDIENT)
        this.clear(TEMPLATE)
    }

    companion object {
        private const val EQUIPMENT = 0
        private const val INGREDIENT = 1
        private const val TEMPLATE = 2
    }
}
