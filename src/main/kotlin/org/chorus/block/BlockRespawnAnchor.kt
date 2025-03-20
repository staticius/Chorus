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
package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.event.Event.isCancelled
import org.chorus.event.block.BlockExplosionPrimeEvent.isBlockBreaking
import org.chorus.event.block.BlockExplosionPrimeEvent.isIncendiary
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Explosion.explodeA
import org.chorus.level.Explosion.explodeB
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.utils.TextFormat

/**
 * @author joserobjr
 * @since 2020-10-06
 */
class BlockRespawnAnchor : Block {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Respawn Anchor"

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        val charge = charge
        if (item.BlockID.== BlockID . GLOWSTONE && charge < CommonBlockProperties . RESPAWN_ANCHOR_CHARGE . getMax ()) {
            if (player == null || !player.isCreative) {
                item.count--
            }

            this.charge = charge + 1
            level.setBlock(this.position, this)
            level.addSound(this.position, Sound.RESPAWN_ANCHOR_CHARGE)
            return true
        }

        if (player == null) {
            return false
        }

        return if (charge > 0) {
            attemptToSetSpawn(player)
        } else {
            false
        }
    }

    protected fun attemptToSetSpawn(player: Player): Boolean {
        if (level.dimension != Level.DIMENSION_NETHER) {
            if (level.gameRules.getBoolean(GameRule.TNT_EXPLODES)) {
                explode(player)
            }
            return true
        }

        if (player.spawn.left() == this) {
            return false
        }
        player.setSpawn(this, SpawnPointType.BLOCK)
        level.addSound(this.position, Sound.RESPAWN_ANCHOR_SET_SPAWN)
        player.sendMessage(TranslationContainer(TextFormat.GRAY.toString() + "%tile.respawn_anchor.respawnSet"))
        return true
    }

    fun explode(player: Player?) {
        val event: BlockExplosionPrimeEvent = BlockExplosionPrimeEvent(this, player, 5.0)
        event.isIncendiary = true
        if (event.isCancelled) {
            return
        }

        level.setBlock(this.position, get(BlockID.AIR))
        val explosion: Explosion = Explosion(this, event.force, this)
        explosion.fireChance = event.fireChance
        if (event.isBlockBreaking) {
            explosion.explodeA()
        }
        explosion.explodeB()
    }

    var charge: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.RESPAWN_ANCHOR_CHARGE)
        set(charge) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.RESPAWN_ANCHOR_CHARGE, charge)
        }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_DIAMOND

    override val resistance: Double
        get() = 1200.0

    override val hardness: Double
        get() = 50.0

    override val lightLevel: Int
        get() = when (charge) {
            0 -> 0
            1 -> 3
            2 -> 7
            else -> 15
        }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            level.addSound(this.position, Sound.RESPAWN_ANCHOR_DEPLETE)
            return type
        }
        return super.onUpdate(type)
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun getDrops(item: Item): Array<Item> {
        if (canHarvest(item)) {
            return arrayOf(Item.get(id))
        }
        return Item.EMPTY_ARRAY
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RESPAWN_ANCHOR, CommonBlockProperties.RESPAWN_ANCHOR_CHARGE)

    }
}
