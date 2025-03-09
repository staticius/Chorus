package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.blockentity.*
import cn.nukkit.item.*
import cn.nukkit.level.Locator
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3

class BlockEndPortal @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockState), BlockEntityHolder<BlockEntityEndPortal> {
    override val name: String
        get() = "End Portal Block"

    override val blockEntityClass: Class<out E>
        get() = BlockEntityEndPortal::class.java

    override val blockEntityType: String
        get() = BlockEntity.END_PORTAL

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
        return BlockEntityHolder.Companion.setBlockAndCreateEntity<BlockEntityEndPortal?, BlockEndPortal>(this) != null
    }

    override fun canPassThrough(): Boolean {
        return false
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return player != null && player.isCreative
    }

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override val lightLevel: Int
        get() = 15

    override fun hasEntityCollision(): Boolean {
        return true
    }

    override val collisionBoundingBox: AxisAlignedBB
        get() = this

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override fun toItem(): Item? {
        return ItemBlock(get(AIR))
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override var maxY: Double
        get() = y + (12.0 / 16.0)
        set(maxY) {
            super.maxY = maxY
        }

    companion object {
        val properties: BlockProperties = BlockProperties(END_PORTAL)
            get() = Companion.field

        fun spawnObsidianPlatform(locator: Locator) {
            val level = locator.level
            val x = locator.position.floorX
            val y = locator.position.floorY
            val z = locator.position.floorZ

            for (blockX in x - 2..x + 2) {
                for (blockZ in z - 2..z + 2) {
                    level.setBlockStateAt(blockX, y - 1, blockZ, properties.defaultState)
                    for (blockY in y..y + 3) {
                        level.setBlockStateAt(blockX, blockY, blockZ, BlockAir.properties.defaultState)
                    }
                }
            }
        }
    }
}
