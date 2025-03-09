package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.level.StructureGrowEvent.blockList
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool
import cn.nukkit.level.Level
import cn.nukkit.level.generator.`object`.BlockManager.blocks
import cn.nukkit.level.generator.`object`.ObjectBigMushroom.generate
import cn.nukkit.level.generator.`object`.ObjectMangroveTree.generate
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import cn.nukkit.math.Vector3.floorX
import cn.nukkit.math.Vector3.floorY
import cn.nukkit.math.Vector3.floorZ
import cn.nukkit.utils.random.RandomSourceProvider.Companion.create
import java.util.concurrent.ThreadLocalRandom

abstract class BlockMushroom(blockState: BlockState?) : BlockFlowable(blockState), FlowerPotBlock,
    Natural {
    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!canStay()) {
                level.useBreakOn(this.position)

                return Level.BLOCK_UPDATE_NORMAL
            }
        }
        return 0
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
        if (canStay()) {
            level.setBlock(block.position, this, true, true)
            return true
        }
        return false
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isFertilizer) {
            if (player != null && (player.gamemode and 0x01) == 0) {
                item.count--
            }

            if (ThreadLocalRandom.current().nextFloat() < 0.4) {
                this.grow()
            }

            level.addParticle(BoneMealParticle(this.position))
            return true
        }
        return false
    }

    fun grow(): Boolean {
        level.setBlock(this.position, get(BlockID.AIR), true, false)

        val generator: ObjectBigMushroom = ObjectBigMushroom(type)

        val chunkManager: BlockManager = BlockManager(this.level)
        if (generator.generate(chunkManager, RandomSourceProvider.create(), this.position)) {
            val ev: StructureGrowEvent = StructureGrowEvent(this, chunkManager.blocks)
            level.server.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return false
            }
            for (block in ev.blockList) {
                level.setBlock(
                    Vector3(
                        block.position.floorX.toDouble(),
                        block.position.floorY.toDouble(),
                        block.position.floorZ.toDouble()
                    ), block
                )
            }
            return true
        } else {
            level.setBlock(this.position, this, true, false)
            return false
        }
    }

    fun canStay(): Boolean {
        val block = this.down()
        return block!!.id == BlockID.MYCELIUM || block.id == BlockID.PODZOL || block is BlockNylium || (!block.isTransparent && level.getFullLight(
            this.position
        ) < 13)
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    protected abstract val type: MushroomType?

    override val isFertilizable: Boolean
        get() = true
}
