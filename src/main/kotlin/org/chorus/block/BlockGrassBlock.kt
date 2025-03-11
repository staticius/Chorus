package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.event.Event.isCancelled
import org.chorus.event.block.BlockFadeEvent
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.level.Sound
import org.chorus.level.generator.`object`.BlockManager.applyBlockUpdate
import org.chorus.level.generator.`object`.legacytree.LegacyTallGrass.growGrass
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.*
import java.util.concurrent.ThreadLocalRandom

open class BlockGrassBlock(blockstate: BlockState?) : BlockDirt(blockstate) {
    override val resistance: Double
        get() = 0.6

    override val name: String
        get() = "Grass Block"

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (!up()!!.canBeReplaced()) {
            return false
        }

        if (item.isFertilizer) {
            if (player != null && (player.gamemode and 0x01) == 0) {
                item.count--
            }
            level.addParticle(BoneMealParticle(this.position))
            val blockManager: BlockManager = BlockManager(this.level)
            LegacyTallGrass.growGrass(blockManager, this.position, NukkitRandom())
            blockManager.applyBlockUpdate()
            return true
        } else if (item.isHoe) {
            item.useOn(this)
            level.setBlock(this.position, get(FARMLAND))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        } else if (item.isShovel) {
            item.useOn(this)
            level.setBlock(this.position, get(GRASS_PATH))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }

        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            // Grass dies and changes to dirt after a random time (when a random tick lands on the block)
            // if directly covered by any opaque block.
            // Transparent blocks can kill grass in a similar manner,
            // but only if they cause the light level above the grass block to be four or below (like water does),
            // and the surrounding area is not otherwise sufficiently lit up.
            if (up()!!.lightFilter > 1) {
                val ev = BlockFadeEvent(this, get(DIRT))
                instance!!.pluginManager.callEvent(ev)
                if (!ev.isCancelled) {
                    level.setBlock(this.position, ev.newState)
                    return type
                }
            }

            // Grass can spread to nearby dirt blocks.
            // Grass spreading without player intervention depends heavily on the time of day.
            // For a dirt block to accept grass from a nearby grass block, the following requirements must be met:

            // The source block must have a light level of 9 or brighter directly above it.
            if (level.getFullLight(position.add(0.0, 1.0, 0.0)!!) >= BlockCrops.minimumLightLevel) {
                // The dirt block receiving grass must be within a 3×5×3 range of the source block
                // where the source block is in the center of the second topmost layer of that range.

                val random = ThreadLocalRandom.current()
                val x = random.nextInt(position.x.toInt() - 1, position.x.toInt() + 1 + 1)
                val y = random.nextInt(position.y.toInt() - 3, position.y.toInt() + 1 + 1)
                val z = random.nextInt(position.z.toInt() - 1, position.z.toInt() + 1 + 1)
                val block = level.getBlock(Vector3(x.toDouble(), y.toDouble(), z.toDouble()))
                if (block!!.id == Block.DIRT // The dirt block must have a light level of at least 4 above it.
                    && level.getFullLight(block.position) >= 4 // Any block directly above the dirt block must not reduce light by 2 levels or more.
                    && block.up()!!.lightFilter < 2
                ) {
                    val ev: BlockSpreadEvent = BlockSpreadEvent(block, this, get(GRASS_BLOCK))
                    instance!!.pluginManager.callEvent(ev)
                    if (!ev.isCancelled) {
                        level.setBlock(block.position, ev.newState)
                    }
                }
            }
            return type
        }
        return 0
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(get(DIRT).toItem())
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRASS_BLOCK)

    }
}