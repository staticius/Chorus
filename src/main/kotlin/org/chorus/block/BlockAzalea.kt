package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.level.StructureGrowEvent.blockList
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.level.generator.`object`.BlockManager.blocks
import cn.nukkit.level.generator.`object`.ObjectGenerator.generate
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import cn.nukkit.utils.random.RandomSourceProvider.Companion.create
import java.util.concurrent.ThreadLocalRandom

/**
 * @author LoboMetalurgico
 * @since 13/06/2021
 */
open class BlockAzalea @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), FlowerPotBlock {
    override val name: String
        get() = "Azalea"

    override val waterloggingLevel: Int
        get() = 1

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    override val isFertilizable: Boolean
        get() = true

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isFertilizer) { // BoneMeal
            if (player != null && !player.isCreative) {
                item.count--
            }

            level.addParticle(BoneMealParticle(this.position))
            if (ThreadLocalRandom.current().nextInt(4) == 0) {
                this.grow()
                return true
            }
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        val chance = ThreadLocalRandom.current().nextDouble(1.0)
        val aged = chance > 0.8
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!BlockFlower.isSupportValid(down())) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) { //Growth
            if (ThreadLocalRandom.current().nextInt(1, 8) == 1 && level.getFullLight(
                    position.add(0.0, 1.0, 0.0)!!
                ) >= BlockCrops.minimumLightLevel
            ) {
                if (aged) {
                    this.grow()
                } else {
                    level.setBlock(this.position, this, true)
                    return Level.BLOCK_UPDATE_RANDOM
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }
        return Level.BLOCK_UPDATE_NORMAL
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (BlockFlower.isSupportValid(down())) {
            level.setBlock(block.position, this, true, true)
            return true
        }

        return false
    }

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(toItem())
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    fun isSameType(pos: Vector3): Boolean {
        val block = level.getBlock(pos)
        return block!!.id == this.id && block.properties === this.properties
    }

    private fun grow() {
        val generator: ObjectGenerator

        generator = ObjectAzaleaTree()
        val vector3 = position.add(0.0, 0.0, 0.0)

        val chunkManager: BlockManager = BlockManager(this.level)
        val success: Boolean = generator.generate(chunkManager, RandomSourceProvider.create(), vector3)
        val ev: StructureGrowEvent = StructureGrowEvent(this, chunkManager.blocks)
        level.server.pluginManager.callEvent(ev)
        if (ev.isCancelled || !success) {
            return
        }

        for (block in ev.blockList) {
            level.setBlock(block.position, block)
        }

        level.setBlock(this.position, get(OAK_LOG))
    }

    companion object {
        val properties: BlockProperties = BlockProperties(AZALEA)
            get() = Companion.field
    }
}
