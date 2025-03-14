package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntity.Companion.getDefaultCompound
import org.chorus.blockentity.BlockEntityBanner.baseColor
import org.chorus.blockentity.BlockEntityBanner.dyeColor
import org.chorus.item.Item
import org.chorus.item.Item.namedTag
import org.chorus.item.ItemDye.dyeColor
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.CompassRoseDirection.Companion.from
import org.chorus.nbt.tag.CompoundTag.getInt
import org.chorus.nbt.tag.CompoundTag.getList
import org.chorus.nbt.tag.CompoundTag.put
import org.chorus.nbt.tag.ListTag.size
import org.chorus.utils.BlockColor.equals

import kotlin.math.floor


open class BlockStandingBanner @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockTransparent(blockstate), Faceable, BlockEntityHolder<BlockEntityBanner?> {
    override val blockEntityType: String
        get() = BlockEntity.BANNER

    override val blockEntityClass: Class<out Any>
        get() = BlockEntityBanner::class.java

    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 5.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val name: String
        get() = "Banner"

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        return null
    }

    override fun canPassThrough(): Boolean {
        return true
    }

    override val waterloggingLevel: Int
        get() = 1

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
        if (face == BlockFace.DOWN) {
            return false
        }

        val layer0 = level.getBlock(this.position, 0)
        val layer1 = level.getBlock(this.position, 1)

        if (face == BlockFace.UP) {
            val direction: CompassRoseDirection = CompassRoseDirection.from(
                floor((((player?.rotation?.yaw ?: 0.0) + 180) * 16 / 360) + 0.5).toInt() and 0x0f
            )
            this.direction = direction
            if (!level.setBlock(block.position, this, true)) {
                return false
            }
        } else {
            val wall = get(BlockID.WALL_BANNER) as BlockStandingBanner
            wall.blockFace = face
            if (!level.setBlock(block.position, wall, true)) {
                return false
            }
        }

        val nbt: CompoundTag = getDefaultCompound(this.position, BlockEntity.BANNER)
            .putInt("Base", item.damage and 0xf)

        val type = item.getNamedTagEntry("Type")
        if (type is IntTag) {
            nbt.put("Type", type)
        }
        val patterns = item.getNamedTagEntry("Patterns")
        if (patterns is ListTag<*>) {
            nbt.put("Patterns", patterns)
        }

        try {
            createBlockEntity(nbt)
            return true
        } catch (e: Exception) {
            BlockStandingBanner.log.error(
                "Failed to create the block entity {} at {}",
                getBlockEntityType(),
                locator,
                e
            )
            level.setBlock(layer0!!.position, 0, layer0, true)
            level.setBlock(layer0.position, 1, layer1!!, true)
            return false
        }
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down()!!.id === BlockID.AIR) {
                level.useBreakOn(this.position)

                return Level.BLOCK_UPDATE_NORMAL
            }
        }

        return 0
    }

    override val itemId: String
        get() = ItemID.BANNER

    override fun toItem(): Item? {
        val banner: BlockEntityBanner? = blockEntity
        val item = Item.get(ItemID.BANNER)
        if (banner != null) {
            item.damage = banner.baseColor and 0xf
            val type: Int = banner.namedTag.getInt("Type")
            if (type > 0) {
                item.setNamedTag(
                    (if (item.hasCompoundTag()) item.namedTag else CompoundTag())
                        .putInt("Type", type)
                )
            }
            val patterns: ListTag<CompoundTag?> =
                banner.namedTag.getList<CompoundTag>("Patterns", CompoundTag::class.java)
            if (patterns.size() > 0) {
                item.setNamedTag(
                    (if (item.hasCompoundTag()) item.namedTag else CompoundTag())
                        .putList("Patterns", patterns)
                )
            }
        }
        return item
    }

    open var direction: CompassRoseDirection
        get() = CompassRoseDirection.from(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROUND_SIGN_DIRECTION))
        set(direction) {
            setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.GROUND_SIGN_DIRECTION,
                direction.index
            )
        }

    var blockFace: BlockFace?
        get() = direction.closestBlockFace
        set(face) {
            direction = face!!.compassRoseDirection
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    val dyeColor: DyeColor
        get() {
            if (this.level != null) {
                val blockEntity: BlockEntityBanner? = blockEntity

                if (blockEntity != null) {
                    return blockEntity.dyeColor
                }
            }

            return DyeColor.WHITE
        }

    override val isSolid: Boolean
        get() = false

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STANDING_BANNER, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}