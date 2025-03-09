package org.chorus.utils

import org.chorus.block.BlockID
import org.chorus.block.property.type.BlockPropertyType
import org.chorus.block.property.type.BlockPropertyType.BlockPropertyValue
import org.chorus.nbt.NBTIO.write
import org.chorus.nbt.tag.*
import lombok.SneakyThrows
import lombok.experimental.UtilityClass
import java.nio.ByteOrder

/**
 * Allay Project 2023/6/1
 *
 * @author Cool_Loong
 */
@UtilityClass
class HashUtils {
    fun computeBlockStateHash(identifier: String, propertyValues: List<BlockPropertyValue<*, *, *>>): Int {
        if (identifier == BlockID.UNKNOWN) {
            return -2 // This is special case
        }

        //build block state tag
        val states = TreeMapCompoundTag()
        for (value in propertyValues) {
            when (value.propertyType.type) {
                BlockPropertyType.Type.INT -> states.putInt(value.propertyType.name, value.serializedValue as Int)
                BlockPropertyType.Type.ENUM -> states.putString(
                    value.propertyType.name,
                    value.serializedValue.toString()
                )

                BlockPropertyType.Type.BOOLEAN -> states.putByte(
                    value.propertyType.name,
                    (value.serializedValue as Byte).toInt()
                )
            }
        }

        val tag = CompoundTag()
            .putString("name", identifier)
            .putCompound("states", states)
        return fnv1a_32_nbt(tag!!)
    }

    fun computeBlockStateHash(identifier: String, propertyValues: Array<BlockPropertyValue<*, *, *>>): Int {
        if (identifier == BlockID.UNKNOWN) {
            return -2 // This is special case
        }

        val states = TreeMapCompoundTag()
        for (value in propertyValues) {
            when (value.propertyType.type) {
                BlockPropertyType.Type.INT -> states.putInt(value.propertyType.name, value.serializedValue as Int)
                BlockPropertyType.Type.ENUM -> states.putString(
                    value.propertyType.name,
                    value.serializedValue.toString()
                )

                BlockPropertyType.Type.BOOLEAN -> states.putByte(
                    value.propertyType.name,
                    (value.serializedValue as Byte).toInt()
                )
            }
        }
        val tag = CompoundTag()
            .putString("name", identifier)
            .putCompound("states", states)
        return fnv1a_32_nbt(tag!!)
    }


    @SneakyThrows
    fun fnv1a_32_nbt(tag: CompoundTag): Int {
        if (tag.getString("name") == "minecraft:unknown") {
            return -2 // This is special case
        }
        return fnv1a_32(write(tag, ByteOrder.LITTLE_ENDIAN)!!)
    }

    @SneakyThrows
    fun fnv1a_32_nbt_palette(tag: CompoundTag): Int {
        if (tag.getString("name") == "minecraft:unknown") {
            return -2 // This is special case
        }
        val states: CompoundTag = if (tag is TreeMapCompoundTag) {
            tag
        } else {
            TreeMapCompoundTag()
        }
        for ((key, value): Map.Entry<String?, Tag?> in tag.getCompound("states")!!
            .getTags()) {
            states.put(key, value)
        }
        tag.put("states", states)
        if (tag.contains("version")) {
            tag.remove("version")
        }
        return fnv1a_32(write(tag, ByteOrder.LITTLE_ENDIAN)!!)
    }

    //CPU Ryzen PRO 5850U, 16G, Win11
    //Throughput 15736.451 ± 337.778  ops/ms
    fun fnv1a_32(data: ByteArray): Int {
        var hash = FNV1_32_INIT
        for (datum in data) {
            hash = hash xor (datum.toInt() and 0xff)
            hash *= FNV1_PRIME_32
        }
        return hash
    }

    /**
     * Shift int x to the left by 32 bits and int z to form a long value
     *
     * @param x the int x
     * @param z the int z
     * @return the long
     */
    fun hashXZ(x: Int, z: Int): Long {
        return (x.toLong() shl 32) or (z.toLong() and 0xffffffffL)
    }

    /**
     * Gets x from [.hashXZ]
     *
     * @param hashXZ a long value
     */
    fun getXFromHashXZ(hashXZ: Long): Int {
        return (hashXZ shr 32).toInt()
    }

    /**
     * Gets z from [.hashXZ]
     *
     * @param hashXZ a long value
     */
    fun getZFromHashXZ(hashXZ: Long): Int {
        return hashXZ.toInt()
    }

    fun hashChunkXYZ(x: Int, y: Int, z: Int): Int {
        //Make sure x and z are in the range of 0-15
        var x = x
        var z = z
        x = x and 0xF //4 bits
        z = z and 0xF //4 bits
        //Use the int type to store the result
        var result = 0
        //Place x in the top 4 digits
        result = result or (x shl 28)
        //Place y in the middle 24 bits
        result = result or ((y and 0xFFFFFF) shl 4)
        //Place z in the lowest 4 digits
        result = result or z
        return result
    }

    /**
     * Extract the value of x from the hash chunk xyz.
     * x occupies the highest 4 bits.
     *
     * @param encoded Encoded int containing x, y, and z.
     * @return The value of x.
     */
    fun getXFromHashChunkXYZ(encoded: Int): Int {
        return (encoded ushr 28)
    }

    /**
     * Extract the value of y from the hash chunk xyz.
     * y occupies the middle 24 bits.
     *
     * @param encoded Encoded int containing x, y, and z.
     * @return The value of y.
     */
    fun getYFromHashChunkXYZ(encoded: Int): Int {
        return (encoded ushr 4) and 0xFFFFFF
    }

    companion object {
        //https://gist.github.com/Alemiz112/504d0f79feac7ef57eda174b668dd345
        private const val FNV1_32_INIT = -0x7ee3623b
        private const val FNV1_PRIME_32 = 0x01000193
        private const val FNV1_64_INIT = -0x340d631b7bdddcdbL
        private const val FNV1_PRIME_64 = 1099511628211L

        fun fnv164(data: ByteArray): Long {
            var hash = FNV1_64_INIT
            for (datum in data) {
                hash = hash xor (datum.toInt() and 0xff).toLong()
                hash *= FNV1_PRIME_64
            }
            return hash
        }

        /**
         * Extract the value of z from the hash chunk xyz.
         * z occupies the lowest 4 bits.
         *
         * @param encoded Encoded int containing x, y, and z.
         * @return The value of z.
         */
        fun getZFromHashChunkXYZ(encoded: Int): Int {
            return encoded and 0xF
        }
    }
}
