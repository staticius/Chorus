package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.level.StructureGrowEvent.blockList
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.level.generator.`object`.BlockManager.applySubChunkUpdate
import cn.nukkit.level.generator.`object`.BlockManager.blocks
import cn.nukkit.level.generator.`object`.ObjectCherryTree.generate
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.*
import cn.nukkit.utils.random.RandomSourceProvider.Companion.create
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Predicate

class BlockCherrySapling @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSapling(blockstate), FlowerPotBlock {
    override fun getWoodType(): WoodType {
        return WoodType.CHERRY
    }

    override val name: String
        get() = "Cherry Sapling"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (isSupportInvalid) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) { //Growth
            if (level.getFullLight(position.add(0.0, 1.0, 0.0)!!) >= BlockCrops.Companion.MINIMUM_LIGHT_LEVEL) {
                if (isAged) {
                    this.grow()
                } else {
                    isAged = true
                    level.setBlock(this.position, this, true)
                    return Level.BLOCK_UPDATE_RANDOM
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }
        return Level.BLOCK_UPDATE_NORMAL
    }

    private fun grow() {
        val blockManager: BlockManager = BlockManager(this.level)
        val vector3 = Vector3(
            position.x, position.y - 1,
            position.z
        )
        val objectCherryTree: ObjectCherryTree = ObjectCherryTree()
        val generate: Boolean = objectCherryTree.generate(blockManager, RandomSourceProvider.create(), this.position)
        if (generate) {
            val ev: StructureGrowEvent = StructureGrowEvent(this, blockManager.blocks)
            level.server.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return
            }
            if (level.getBlock(vector3)!!.id == DIRT_WITH_ROOTS) {
                level.setBlock(vector3, get(DIRT))
            }
            blockManager.applySubChunkUpdate(
                ev.blockList,
                Predicate { block: Block -> !block.isAir })
        }
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
        if (isSupportInvalid) {
            return false
        }

        if (levelBlock is BlockLiquid || getLevelBlockAtLayer(1) is BlockLiquid) {
            return false
        }

        level.setBlock(this.position, this, true, true)
        return true
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
        if (item.isFertilizer) { // BoneMeal and so on
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

    private val isSupportInvalid: Boolean
        get() {
            val downId = down()!!.id
            return !(downId == DIRT || downId == GRASS_BLOCK || downId == SAND || downId == GRAVEL || downId == PODZOL)
        }

    var isAge: Boolean
        get() = this.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.AGE_BIT)
        set(age) {
            this.setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.AGE_BIT,
                age
            )
        }

    override fun toItem(): Item? {
        return ItemBlock(BlockCherrySapling())
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(CHERRY_SAPLING, CommonBlockProperties.AGE_BIT)
            get() = Companion.field
    }
}
