package cn.nukkit.block

import cn.nukkit.block.property.enums.WoodType.name
import cn.nukkit.block.property.type.BlockPropertyType.BlockPropertyValue.propertyType
import cn.nukkit.block.property.type.BlockPropertyType.createValue
import cn.nukkit.block.property.type.BooleanPropertyType.createValue
import cn.nukkit.block.property.type.IntPropertyType.createValue
import cn.nukkit.nbt.tag.CompoundTag.putByte
import cn.nukkit.nbt.tag.CompoundTag.putCompound
import cn.nukkit.nbt.tag.CompoundTag.putInt
import cn.nukkit.nbt.tag.CompoundTag.putString
import cn.nukkit.utils.HashUtils
import cn.nukkit.utils.HashUtils.computeBlockStateHash
import com.google.common.base.Preconditions
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jetbrains.annotations.UnmodifiableView
import java.util.*
import java.util.List
import kotlin.collections.MutableList
import kotlin.collections.indices

/**
 * Allay Project 12/15/2023
 *
 * @author Cool_Loong
 */
class BlockStateImpl(
    override val identifier: String,
    val blockhash: Int,
    val specialValue: Short,
    blockPropertyValues: Array<BlockPropertyValue<*, *, *>>,
    blockStateTag: CompoundTagView
) : BlockState {
    constructor(identifier: String, blockStateHash: Int, propertyValues: Array<BlockPropertyValue<*, *, *>>) : this(
        identifier.intern(),
        blockStateHash,
        BlockState.Companion.computeSpecialValue(propertyValues),
        propertyValues,
        buildBlockStateTag(identifier, propertyValues)
    )

    constructor(identifier: String, propertyValues: Array<BlockPropertyValue<*, *, *>>) : this(
        identifier,
        HashUtils.computeBlockStateHash(identifier, propertyValues),
        propertyValues
    )

    override fun getBlockPropertyValues(): @UnmodifiableView MutableList<BlockPropertyValue<*, *, *>> {
        return List.of<BlockPropertyValue<*, *, *>>(*blockPropertyValues)
    }

    override fun blockStateHash(): Int {
        return this.blockhash
    }

    override fun unsignedBlockStateHash(): Long {
        return Integer.toUnsignedLong(this.blockhash)
    }

    override fun getBlockStateTag(): CompoundTagView? {
        return blockStateTag
    }

    override fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> getPropertyValue(p: PROPERTY): DATATYPE {
        for (property in this.blockPropertyValues) {
            if (property.propertyType === p) {
                return property.getValue()
            }
        }
        throw IllegalArgumentException("Property " + p + " is not supported by this block " + this.identifier)
    }

    override fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> setPropertyValue(
        properties: BlockProperties,
        property: PROPERTY,
        value: DATATYPE
    ): BlockState? {
        return setPropertyValue(properties, property.createValue(value))
    }

    override fun setPropertyValue(
        properties: BlockProperties,
        propertyValue: BlockPropertyValue<*, *, *>
    ): BlockState? {
        val blockPropertyValues: Array<BlockPropertyValue<*, *, *>> = blockPropertyValues
        val newPropertyValues: Array<BlockPropertyValue<*, *, *>> =
            arrayOfNulls<BlockPropertyValue<*, *, *>>(blockPropertyValues.size)
        var succeed = false
        for (i in blockPropertyValues.indices) {
            val v: BlockPropertyValue<*, out BlockPropertyType<*>?, *> = blockPropertyValues[i]
            if (v.propertyType === propertyValue.propertyType) {
                succeed = true
                newPropertyValues[i] = propertyValue
            } else newPropertyValues[i] = v
        }
        require(succeed) { "Property " + propertyValue.propertyType + " is not supported by this block " + this.identifier }
        return getNewBlockState(properties, newPropertyValues)
    }

    override fun setPropertyValues(
        properties: BlockProperties,
        vararg values: BlockPropertyValue<*, *, *>
    ): BlockState? {
        val newPropertyValues: Array<BlockPropertyValue<*, *, *>> = arrayOfNulls<BlockPropertyValue<*, *, *>>(
            blockPropertyValues.size
        )
        val succeed = BooleanArray(values.size)
        var succeedCount = 0
        for (i in blockPropertyValues.indices) {
            var index = -1
            for (j in values.indices) {
                if (values[j].propertyType === blockPropertyValues[i].propertyType) {
                    index = j
                    succeedCount++
                    succeed[index] = true
                    newPropertyValues[i] = values[j]
                }
            }
            if (index == -1) {
                newPropertyValues[i] = blockPropertyValues[i]
            }
        }
        if (succeedCount != values.size) {
            val errorMsgBuilder = StringBuilder("Properties ")
            for (i in values.indices) {
                if (!succeed[i]) {
                    errorMsgBuilder.append(values[i].propertyType.name)
                    if (i != values.size - 1) errorMsgBuilder.append(", ")
                }
            }
            errorMsgBuilder.append(" are not supported by this block ").append(this.identifier)
            throw IllegalArgumentException(errorMsgBuilder.toString())
        }
        return getNewBlockState(properties, newPropertyValues)
    }

    private fun getNewBlockState(properties: BlockProperties, values: Array<BlockPropertyValue<*, *, *>>): BlockState? {
        Preconditions.checkNotNull(properties)
        val bits = properties.specialValueBits
        if (bits <= 16) {
            return properties.getBlockState(BlockState.Companion.computeSpecialValue(bits, values))
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun toString(): String {
        return "BlockStateImpl{" +
                "identifier='" + identifier + '\'' +
                ", blockPropertyValues=" + Arrays.stream<BlockPropertyValue<*, *, *>>(blockPropertyValues)
            .map<Any>(BlockPropertyType.BlockPropertyValue::getValue).toList() +
                '}'
    }

    override val blockPropertyValues: Array<BlockPropertyValue<*, *, *>> = blockPropertyValues
    override val blockStateTag: CompoundTagView = blockStateTag

    companion object {
        var UNKNOWN_BLOCK_STATE_CACHE: Int2ObjectOpenHashMap<BlockStateImpl> = Int2ObjectOpenHashMap()

        fun makeUnknownBlockState(hash: Int, blockTag: CompoundTag?): BlockStateImpl {
            return UNKNOWN_BLOCK_STATE_CACHE.computeIfAbsent(
                hash,
                Int2ObjectFunction<BlockStateImpl> { h: Int ->
                    BlockStateImpl(
                        BlockID.UNKNOWN, -2, 0.toShort(), arrayOfNulls<BlockPropertyValue<*, *, *>>(0), CompoundTagView(
                            LinkedCompoundTag()
                                .putString("name", BlockID.UNKNOWN)
                                .putCompound("states", CompoundTag())
                                .putCompound("Block", blockTag)
                                .putInt("version", ProtocolInfo.BLOCK_STATE_VERSION_NO_REVISION)
                        )
                    )
                })
        }

        private fun buildBlockStateTag(
            identifier: String,
            propertyValues: Array<BlockPropertyValue<*, *, *>>
        ): CompoundTagView {
            //build block state tag
            val states: TreeMapCompoundTag = TreeMapCompoundTag()
            for (value in propertyValues) {
                when (value.propertyType.type) {
                    INT -> states.putInt(value.propertyType.name, value.serializedValue as Int)
                    ENUM -> states.putString(value.propertyType.name, value.serializedValue.toString())
                    BOOLEAN -> states.putByte(value.propertyType.name, (value.serializedValue as Byte).toInt())
                }
            }
            return CompoundTagView(
                LinkedCompoundTag()
                    .putString("name", identifier)
                    .putCompound("states", states)
                    .putInt("version", ProtocolInfo.BLOCK_STATE_VERSION_NO_REVISION)
            )
        }
    }
}
