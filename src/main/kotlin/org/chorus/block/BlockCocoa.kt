package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.block.property.type.IntPropertyType
import org.chorus.event.block.BlockGrowEvent
import org.chorus.item.*
import org.chorus.item.Item.Companion.get
import org.chorus.level.Level
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromHorizontalIndex
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.math.SimpleAxisAlignedBB
import org.chorus.utils.Faceable
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.set

class BlockCocoa @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockstate), Faceable {
    override val name: String
        get() = "Cocoa"

    override var minX: Double
        get() = position.x + relativeBoundingBox.minX
        set(minX) {
            super.minX = minX
        }

    override var maxX: Double
        get() = position.x + relativeBoundingBox.maxX
        set(maxX) {
            super.maxX = maxX
        }

    override var minY: Double
        get() = position.y + relativeBoundingBox.minY
        set(minY) {
            super.minY = minY
        }

    override var maxY: Double
        get() = position.y + relativeBoundingBox.maxY
        set(maxY) {
            super.maxY = maxY
        }

    override var minZ: Double
        get() = position.z + relativeBoundingBox.minZ
        set(minZ) {
            super.minZ = minZ
        }

    override var maxZ: Double
        get() = position.z + relativeBoundingBox.maxZ
        set(maxZ) {
            super.maxZ = maxZ
        }

    private val relativeBoundingBox: AxisAlignedBB
        get() {
            val face = blockFace
            val axisAlignedBBS = ALL[face]
            if (axisAlignedBBS != null) {
                return axisAlignedBBS[age]
            }
            val bbs = when (face) {
                BlockFace.EAST -> EAST
                BlockFace.SOUTH -> SOUTH
                BlockFace.WEST -> WEST
                else -> NORTH
            }
            ALL[face] = bbs
            return bbs[age]
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
        if (target is BlockJungleLog || target is BlockWood && target.getWoodType() == WoodType.JUNGLE) {
            if (face != BlockFace.DOWN && face != BlockFace.UP) {
                setPropertyValue<Int, IntPropertyType>(
                    CommonBlockProperties.DIRECTION,
                    faces[face.index]
                )
                level.setBlock(block.position, this, true, true)
                return true
            }
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val side =
                this.getSide(fromIndex(faces2[getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION)])!!)
            if (!((side is BlockWood && side.getWoodType() == WoodType.JUNGLE) || side is BlockJungleLog)) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (ThreadLocalRandom.current().nextInt(2) == 1) {
                if (this.age < 2) {
                    if (!this.grow()) {
                        return Level.BLOCK_UPDATE_RANDOM
                    }
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }

        return 0
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
            if (this.age < 2) {
                if (!this.grow()) {
                    return false
                }
                level.addParticle(BoneMealParticle(this.position))

                if (player != null && (player.gamemode and 0x01) == 0) {
                    item.count--
                }
            }

            return true
        }

        return false
    }

    fun grow(): Boolean {
        val block = this.clone()
        block.setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_3, age + 1)
        val ev = BlockGrowEvent(this, block)
        instance!!.pluginManager.callEvent(ev)
        return !ev.isCancelled && level.setBlock(
            this.position,
            ev.newState!!, true, true
        )
    }

    override val resistance: Double
        get() = 15.0

    override val hardness: Double
        get() = 0.2

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override val itemId: String
        get() = ItemID.COCOA_BEANS

    override fun toItem(): Item? {
        return Item.get(ItemID.COCOA_BEANS)
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            get(
                ItemID.COCOA_BEANS,
                0,
                if (getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_3) >= 1) 3 else 1
            )
        )
    }

    override var blockFace: BlockFace?
        get() {
            val propertyValue =
                getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION)
            return fromHorizontalIndex(propertyValue)
        }
        set(face) {
            val horizontalIndex = face!!.horizontalIndex
            setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.DIRECTION,
                if (horizontalIndex == -1) 0 else horizontalIndex
            )
        }

    var age: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_3)
        set(age) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_3, age)
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.COCOA, CommonBlockProperties.AGE_3, CommonBlockProperties.DIRECTION)


        protected val EAST: Array<AxisAlignedBB> = arrayOf<SimpleAxisAlignedBB>(
            SimpleAxisAlignedBB(0.6875, 0.4375, 0.375, 0.9375, 0.75, 0.625),
            SimpleAxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875),
            SimpleAxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875)
        )
        protected val WEST: Array<AxisAlignedBB> = arrayOf<SimpleAxisAlignedBB>(
            SimpleAxisAlignedBB(0.0625, 0.4375, 0.375, 0.3125, 0.75, 0.625),
            SimpleAxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875),
            SimpleAxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875)
        )
        protected val NORTH: Array<AxisAlignedBB> = arrayOf<SimpleAxisAlignedBB>(
            SimpleAxisAlignedBB(0.375, 0.4375, 0.0625, 0.625, 0.75, 0.3125),
            SimpleAxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375),
            SimpleAxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375)
        )
        protected val SOUTH: Array<AxisAlignedBB> = arrayOf<SimpleAxisAlignedBB>(
            SimpleAxisAlignedBB(0.375, 0.4375, 0.6875, 0.625, 0.75, 0.9375),
            SimpleAxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375),
            SimpleAxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375)
        )
        protected val ALL: Object2ObjectArrayMap<BlockFace?, Array<AxisAlignedBB>> = Object2ObjectArrayMap(4)

        val faces: IntArray = intArrayOf(
            0,
            0,
            0,
            2,
            3,
            1,
        )

        val faces2: IntArray = intArrayOf(
            3, 4, 2, 5, 3, 4, 2, 5, 3, 4, 2, 5
        )
    }
}
