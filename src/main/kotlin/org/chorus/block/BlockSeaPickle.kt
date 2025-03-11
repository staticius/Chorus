package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.event.Event.isCancelled
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.level.Level
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.BlockFace
import org.chorus.math.Vector2
import java.util.concurrent.ThreadLocalRandom

class BlockSeaPickle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate) {
    override val name: String
        get() = "Sea Pickle"

    var isDead: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.DEAD_BIT)
        set(dead) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.DEAD_BIT, dead)
        }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val down = down()
            if (!down!!.isSolid || down.id == BlockID.ICE) {
                level.useBreakOn(this.position)
                return type
            }

            val layer1 = getLevelBlockAtLayer(1)
            if (layer1 is BlockFlowingWater || layer1!!.id == BlockID.FROSTED_ICE) {
                if (isDead && (layer1.id == BlockID.FROSTED_ICE || layer1.getPropertyValue<Int, IntPropertyType>(
                        CommonBlockProperties.LIQUID_DEPTH
                    ) == 0 || layer1.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.LIQUID_DEPTH) == 8)
                ) {
                    val event: BlockFadeEvent = BlockFadeEvent(
                        this, BlockSeaPickle().setPropertyValue<Boolean, BooleanPropertyType>(
                            CommonBlockProperties.DEAD_BIT, !isDead
                        )
                    )
                    if (!event.isCancelled) {
                        level.setBlock(this.position, event.newState, true, true)
                    }
                    return type
                }
            } else if (!isDead) {
                val event: BlockFadeEvent = BlockFadeEvent(
                    this, BlockSeaPickle().setPropertyValue<Boolean, BooleanPropertyType>(
                        CommonBlockProperties.DEAD_BIT, !isDead
                    )
                )
                if (!event.isCancelled) {
                    level.setBlock(this.position, event.newState, true, true)
                }
            }

            return type
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
        if (target.id == BlockID.SEA_PICKLE && target.getPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.CLUSTER_COUNT
            ) < 3
        ) {
            target.setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.CLUSTER_COUNT, target.getPropertyValue<Int, IntPropertyType>(
                    CommonBlockProperties.CLUSTER_COUNT
                ) + 1
            )
            level.setBlock(target.position, target, true, true)
            return true
        }

        val down = block.down()!!.getLevelBlockAtLayer(0)
        if (down!!.isSolid && down.id != BlockID.ICE) {
            if (down is BlockSlab || down is BlockStairs || block.id == BlockID.BUBBLE_COLUMN) {
                return false
            }
            val layer1 = block.getLevelBlockAtLayer(1)
            if (layer1 is BlockFlowingWater) {
                if (layer1.liquidDepth != 0 && layer1.liquidDepth != 8) {
                    return false
                }

                if (layer1.liquidDepth == 8) {
                    level.setBlock(block.position, 1, BlockFlowingWater(), true, false)
                }
            } else {
                isDead = true
            }

            level.setBlock(block.position, 0, this, true, true)

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
        //Bone meal

        if (item.isFertilizer && down() is BlockCoralBlock && !isDead) {
            val block = clone() as BlockSeaPickle
            block.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CLUSTER_COUNT, 3)

            val blockGrowEvent: BlockGrowEvent = BlockGrowEvent(this, block)
            instance!!.pluginManager.callEvent(blockGrowEvent)

            if (blockGrowEvent.isCancelled) {
                return false
            }

            level.setBlock(this.position, blockGrowEvent.newState, false, true)
            level.addParticle(BoneMealParticle(this.position))

            if (player != null && (player.gamemode and 0x01) == 0) {
                item.count--
            }

            val random = ThreadLocalRandom.current()
            val blocksAround = level.getCollisionBlocks(
                SimpleAxisAlignedBB(
                    position.x - 2,
                    position.y - 2,
                    position.z - 2, position.x + 3, position.y, position.z + 3
                )
            )
            for (blockNearby in blocksAround) {
                if (blockNearby is BlockCoralBlock) {
                    val up = blockNearby.up()
                    if (up is BlockFlowingWater &&
                        (up.liquidDepth == 0 || up.liquidDepth == 8) && random.nextInt(6) == 0 && Vector2(
                            up.position.x,
                            up.position.z
                        ).distance(
                            Vector2(
                                position.x, position.z
                            )
                        ) <= 2
                    ) {
                        val blockSpreadEvent: BlockSpreadEvent = BlockSpreadEvent(
                            up, this, BlockSeaPickle().setPropertyValue<Int, IntPropertyType>(
                                CommonBlockProperties.CLUSTER_COUNT, random.nextInt(3)
                            )
                        )
                        if (!blockSpreadEvent.isCancelled) {
                            level.setBlock(up.position, 1, BlockFlowingWater(), true, false)
                            level.setBlock(up.position, blockSpreadEvent.newState, true, true)
                        }
                    }
                }
            }
        }

        return super.onActivate(item, player, blockFace, fx, fy, fz)
    }

    override val waterloggingLevel: Int
        get() = 1

    override val lightLevel: Int
        get() {
            return if (isDead) {
                0
            } else {
                6 + getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CLUSTER_COUNT) * 3
            }
        }

    override fun toItem(): Item? {
        return ItemBlock(BlockSeaPickle())
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf<Item?>(
            ItemBlock(
                BlockSeaPickle(), 0, getPropertyValue<Int, IntPropertyType>(
                    CommonBlockProperties.CLUSTER_COUNT
                )
            )
        )
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SEA_PICKLE, CommonBlockProperties.CLUSTER_COUNT, CommonBlockProperties.DEAD_BIT)

    }
}
