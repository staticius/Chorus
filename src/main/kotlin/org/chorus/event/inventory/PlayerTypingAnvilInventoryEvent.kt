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
package org.chorus.event.inventory

import cn.nukkit.Player
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.AnvilInventory
import cn.nukkit.inventory.Inventory
import lombok.ToString

/**
 * Fired when a player change anything in the item name in an open Anvil inventory window.
 *
 * @author joserobjr
 * @since 2021-02-14
 */
@ToString
class PlayerTypingAnvilInventoryEvent(
    val player: Player,
    inventory: AnvilInventory,
    val previousName: String?,
    val typedName: String
) :
    InventoryEvent(inventory) {
    override val inventory: Inventory
        get() = super.getInventory() as AnvilInventory

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
