package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.block.property.type.IntPropertyType
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.math.BlockFace

open class BlockCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockstate) {
    open fun toCakeForm(): Block {
        return BlockCandleCake()
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
        var target = target
        if (target.id == CAKE && target.isDefaultState) { //必须是完整的蛋糕才能插蜡烛
            target.level.setBlock(target.position, toCakeForm(), true, true)
            return true
        }
        if (target !is BlockCandle && target.up() is BlockCandle) {
            target = target.up()
        }
        if (target.id == id) {
            if (target.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CANDLES) < 3) {
                target.setPropertyValue<Int, IntPropertyType>(
                    CommonBlockProperties.CANDLES, target.getPropertyValue<Int, IntPropertyType>(
                        CommonBlockProperties.CANDLES
                    ) + 1
                )
                level.setBlock(target.position, target, true, true)
                return true
            }
            return false
        } else if (target is BlockCandle) {
            return false
        }

        setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CANDLES, 0)
        level.setBlock(this.position, this, true, true)

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
        if (item.id != ItemID.FLINT_AND_STEEL && !item.isNull) {
            return false
        }

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
        }

        return false
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            ItemBlock(this, 0, getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CANDLES) + 1)
        )
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override val name: String
        get() = "Candle"

    override val lightLevel: Int
        get() = if (getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.LIT)) getPropertyValue<Int, IntPropertyType>(
            CommonBlockProperties.CANDLES
        ) * 3 else 0

    override val hardness: Double
        get() = 0.1

    override val resistance: Double
        get() = 0.1

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}