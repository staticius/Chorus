package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.level.Sound
import org.chorus.math.BlockFace

open class BlockCandleCake @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockState) {
    override val name: String
        get() = "Cake Block With $colorName Candle"

    protected val colorName: String
        get() = "Simple"

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 0.5

    override val waterloggingLevel: Int
        get() = 1

    override var minX: Double
        get() = position.x + (1 + blockState!!.specialValue() * 2) / 16.0
        set(minX) {
            super.minX = minX
        }

    override var minY: Double
        get() = position.y
        set(minY) {
            super.minY = minY
        }

    override var minZ: Double
        get() = position.z + 0.0625
        set(minZ) {
            super.minZ = minZ
        }

    override var maxX: Double
        get() = position.x - 0.0625 + 1
        set(maxX) {
            super.maxX = maxX
        }

    override var maxY: Double
        get() = position.y + 0.5
        set(maxY) {
            super.maxY = maxY
        }

    override var maxZ: Double
        get() = position.z - 0.0625 + 1
        set(maxZ) {
            super.maxZ = maxZ
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
        if (!down()!!.isAir) {
            level.setBlock(block.position, this, true, true)
            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down()!!.isAir) {
                level.setBlock(this.position, get(AIR), true)
                return Level.BLOCK_UPDATE_NORMAL
            }
        }

        return 0
    }

    open fun toCandleForm(): BlockCandle {
        return BlockCandle()
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(toCandleForm().toItem())
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT) && item.id != ItemID.FLINT_AND_STEEL) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT, false)
            level.addSound(this.position, Sound.RANDOM_FIZZ)
            level.setBlock(this.position, this, true, true)
            return true
        } else if (!getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT) && item.id == ItemID.FLINT_AND_STEEL) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT, true)
            level.addSound(this.position, Sound.FIRE_IGNITE)
            level.setBlock(this.position, this, true, true)
            return true
        } else if (player != null && (player.foodData!!.isHungry || player.isCreative)) {
            val cake: Block = BlockCake()
            level.setBlock(this.position, cake, true, true)
            level.dropItem(
                position.add(0.5, 0.5, 0.5),
                getDrops(null)!![0]!!
            )
            return level.getBlock(this.position)!!
                .onActivate(Item.get(AIR), player, blockFace, fx, fy, fz)
        }
        return false
    }

    override val comparatorInputOverride: Int
        get() = 14

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}