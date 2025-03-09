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
package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * @author joserobjr
 * @since 2020-010-06
 */
class BlockExplosionPrimeEvent @JvmOverloads constructor(
    block: Block,
    val player: Player?,
    @JvmField var force: Double,
    @JvmField var fireChance: Double = 0.0
) :
    BlockEvent(block), Cancellable {
    var isBlockBreaking: Boolean = true

    constructor(block: Block, force: Double, fireChance: Double) : this(block, null, force, fireChance)

    var isIncendiary: Boolean
        get() = fireChance > 0
        set(incendiary) {
            if (!incendiary) {
                fireChance = 0.0
            } else if (fireChance <= 0) {
                fireChance = 1.0 / 3.0
            }
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
