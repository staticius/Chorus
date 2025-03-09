package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.Block.isAir
import cn.nukkit.block.BlockFlower.Companion.isSupportValid
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.level.StructureGrowEvent.blockList
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.level.generator.`object`.BlockManager.applySubChunkUpdate
import cn.nukkit.level.generator.`object`.BlockManager.blocks
import cn.nukkit.level.generator.`object`.ObjectBigMushroom.generate
import cn.nukkit.level.generator.`object`.ObjectMangroveTree.generate
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import java.util.concurrent.ThreadLocalRandom

class BlockMangrovePropagule @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockstate), FlowerPotBlock {
    override val name: String
        get() = "Mangrove Propaugle"

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
        //todo: 实现红树树苗放置逻辑
        if (isSupportValid(down()!!)) {
            level.setBlock(block.position, this, true, true)
            return true
        }

        return false
    }

    val isHanging: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.HANGING)

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
        if (item.isFertilizer) { // BoneMeal
            if (player != null && !player.isCreative) {
                item.count--
            }

            level.addParticle(BoneMealParticle(this.position))
            if (ThreadLocalRandom.current().nextFloat() >= 0.45) {
                return true
            }

            this.grow()

            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isSupportValid(down()!!)) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) { //Growth
            this.grow()
        }
        return Level.BLOCK_UPDATE_NORMAL
    }

    protected fun grow() {
        val chunkManager: BlockManager = BlockManager(this.level)
        val vector3 = Vector3(
            position.x, position.y - 1,
            position.z
        )
        val objectMangroveTree: ObjectMangroveTree = ObjectMangroveTree()
        objectMangroveTree.generate(chunkManager, NukkitRandom(), this.position)
        val ev: StructureGrowEvent = StructureGrowEvent(this, chunkManager.blocks)
        level.server.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return
        }
        chunkManager.applySubChunkUpdate(ev.blockList)
        level.setBlock(this.position, get(BlockID.AIR))
        if (level.getBlock(vector3)!!.id == BlockID.DIRT_WITH_ROOTS) {
            level.setBlock(vector3, get(BlockID.DIRT))
        }
        for (block in ev.blockList) {
            if (block.isAir) continue
            level.setBlock(block.position, block)
        }
    }

    override val waterloggingLevel: Int
        get() = 1

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MANGROVE_PROPAGULE,
            CommonBlockProperties.HANGING,
            CommonBlockProperties.PROPAGULE_STAGE
        )
            get() = Companion.field
    }
}
