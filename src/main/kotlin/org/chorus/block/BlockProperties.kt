package org.chorus.block

import cn.nukkit.block.BlockStateImpl
import cn.nukkit.block.property.type.BlockPropertyType
import cn.nukkit.block.property.type.BlockPropertyType.BlockPropertyValue
import cn.nukkit.tags.BlockTags.register
import cn.nukkit.utils.HashUtils
import cn.nukkit.utils.HashUtils.computeBlockStateHash
import cn.nukkit.utils.Identifier.Companion.assertValid
import com.google.common.collect.ImmutableList
import com.google.common.collect.Sets
import it.unimi.dsi.fastutil.Pair
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap
import lombok.Getter
import org.jetbrains.annotations.UnmodifiableView
import java.util.*
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * @author Cool_Loong
 */
class BlockProperties(identifier: String, blockTags: Set<String?>, vararg properties: BlockPropertyType<*>?) {
    @Getter
    private val identifier: String
    private val propertyTypeSet: Set<BlockPropertyType<*>>
    private var specialValueMap: Map<Short, BlockState>? = null

    @Getter
    private var defaultState: BlockState? = null
    val specialValueBits: Byte

    constructor(identifier: String, vararg properties: BlockPropertyType<*>?) : this(
        identifier,
        setOf<String>(),
        *properties
    )

    init {
        assertValid(identifier)
        register(identifier, blockTags)
        this.identifier = identifier.intern()
        this.propertyTypeSet = Sets.newHashSet<BlockPropertyType<*>>(*properties)

        var specialValueBits: Byte = 0
        for (value in this.propertyTypeSet) (specialValueBits += value.bitSize).toByte()
        this.specialValueBits = specialValueBits
        if (this.specialValueBits <= 16) {
            val mapBlockStatePair = initStates()
            val blockStateHashMap = mapBlockStatePair.left()
            this.defaultState = mapBlockStatePair.right()
            this.specialValueMap = blockStateHashMap
                .values
                .stream()
                .collect(
                    Collectors.toMap(
                        BlockStateImpl::specialValue, Function.identity(),
                        BinaryOperator { v1: BlockState, v2: BlockState? -> v1 },
                        Supplier { Short2ObjectOpenHashMap() })
                )
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun initStates(): Pair<Map<Int, BlockStateImpl>, BlockStateImpl> {
        val propertyTypeList = propertyTypeSet.stream().toList()
        val size = propertyTypeList.size
        if (size == 0) {
            val blockState = BlockStateImpl(identifier, arrayOf())
            return Pair.of(Int2ObjectArrayMap(intArrayOf(blockState.blockStateHash()), arrayOf(blockState)), blockState)
        }
        val blockStates = Int2ObjectOpenHashMap<BlockStateImpl>()

        // to keep track of next element in each of
        // the n arrays
        val indices = IntArray(size)

        // initialize with first element's index
        Arrays.fill(indices, 0)

        while (true) {
            // Generate BlockState
            val values = ImmutableList.builder<BlockPropertyValue<*, *, *>?>()
            for (i in 0..<size) {
                val type = propertyTypeList[i]
                values.add(type.tryCreateValue(type.validValues!![indices[i]]))
            }
            val state =
                BlockStateImpl(identifier, values.build().toArray<BlockPropertyValue<*, *, *>> { _Dummy_.__Array__() })
            blockStates.put(state.blockStateHash(), state)

            // find the rightmost array that has more
            // elements left after the current element
            // in that array
            var next = size - 1
            while (next >= 0 && (indices[next] + 1 >= propertyTypeList[next].validValues!!.size())) {
                next--
            }

            // no such array is found so no more
            // combinations left
            if (next < 0) break

            // if found move to next element in that
            // array
            indices[next]++

            // for all arrays to the right of this
            // array current index again points to
            // first element
            for (i in next + 1..<size) {
                indices[i] = 0
            }
        }
        val defaultStateHash: Int = HashUtils.computeBlockStateHash(
            this.identifier,
            propertyTypeSet.stream().map { p: BlockPropertyType<*> -> p.tryCreateValue(p.defaultValue) }.collect(
                Collectors.toList()
            )
        )
        var defaultState: BlockStateImpl? = null
        for (s in blockStates.values) {
            if (s.blockStateHash() == defaultStateHash) {
                defaultState = s
                break
            }
        }
        requireNotNull(defaultState) { "Can't find default block state for block: $identifier" }
        return Pair.of(blockStates, defaultState)
    }

    fun getBlockState(specialValue: Short): BlockState? {
        return specialValueMap!![specialValue]
    }

    fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> getBlockState(
        property: PROPERTY,
        value: DATATYPE
    ): BlockState {
        return defaultState!!.setPropertyValue(this, property, value)
    }

    fun getBlockState(propertyValue: BlockPropertyValue<*, *, *>?): BlockState {
        return defaultState!!.setPropertyValue(this, propertyValue)
    }

    fun getBlockState(vararg values: BlockPropertyValue<*, *, *>?): BlockState {
        return defaultState!!.setPropertyValues(this, *values)
    }

    fun containBlockState(blockState: BlockState): Boolean {
        return specialValueMap!!.containsValue(blockState)
    }

    fun containBlockState(specialValue: Short): Boolean {
        return specialValueMap!!.containsKey(specialValue)
    }

    fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> containProperty(property: PROPERTY): Boolean {
        return propertyTypeSet.contains(property)
    }

    fun <DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?> getPropertyValue(specialValue: Int, p: PROPERTY): DATATYPE {
        return getBlockState(specialValue.toShort())!!.getPropertyValue(p)
    }

    fun getPropertyTypeSet(): @UnmodifiableView MutableSet<BlockPropertyType<*>> {
        return Collections.unmodifiableSet(propertyTypeSet)
    }

    fun getSpecialValueMap(): @UnmodifiableView MutableMap<Short, BlockState> {
        return Collections.unmodifiableMap(specialValueMap)
    }
}
