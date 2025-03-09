package org.chorus.block

import cn.nukkit.block.Block.Companion.get
import cn.nukkit.block.property.type.BlockPropertyType.BlockPropertyValue.propertyType
import cn.nukkit.item.Item
import cn.nukkit.level.Locator
import cn.nukkit.registry.Registries
import org.jetbrains.annotations.UnmodifiableView

/**
 * Allay Project 2023/4/29
 *
 * @author daoge_cmd
 */
interface BlockState {
    @JvmField
    val identifier: String

    fun blockStateHash(): Int

    fun specialValue(): Short

    fun unsignedBlockStateHash(): Long

    @JvmField
    val blockPropertyValues: @UnmodifiableView MutableList<Any?>?

    val blockStateTag: @UnmodifiableView CompoundTagView?

    fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> getPropertyValue(p: PROPERTY): DATATYPE

    fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> setPropertyValue(
        properties: BlockProperties,
        property: PROPERTY,
        value: DATATYPE
    ): BlockState?

    fun setPropertyValue(properties: BlockProperties, propertyValue: BlockPropertyValue<*, *, *>?): BlockState?

    fun setPropertyValues(properties: BlockProperties, vararg values: BlockPropertyValue<*, *, *>?): BlockState?

    val isDefaultState: Boolean
        get() = Registries.BLOCK.getBlockProperties(identifier)
            .getDefaultState() === this

    fun toBlock(): Block {
        return get(this)
    }

    fun toBlock(locator: Locator): Block {
        return get(this, locator)
    }

    fun toItem(): Item? {
        return toBlock().toItem()
    }

    companion object {
        fun makeUnknownBlockState(hash: Int, blockTag: CompoundTag?): BlockState {
            return BlockStateImpl.Companion.makeUnknownBlockState(hash, blockTag)
        }

        fun computeSpecialValue(propertyValues: Array<BlockPropertyValue<*, *, *>>): Short {
            var specialValueBits: Byte = 0
            for (value in propertyValues) specialValueBits += value.propertyType.bitSize
            return computeSpecialValue(specialValueBits, propertyValues)
        }

        fun computeSpecialValue(specialValueBits: Byte, propertyValues: Array<BlockPropertyValue<*, *, *>>): Short {
            var specialValueBits = specialValueBits
            var specialValue: Short = 0
            for (value in propertyValues) {
                specialValue =
                    (specialValue.toInt() or ((value.index as Short) shl (specialValueBits - value.propertyType.bitSize)).toShort()
                        .toInt()).toShort()
                specialValueBits -= value.propertyType.bitSize
            }
            return specialValue
        }
    }
}
