package cn.nukkit.level.format.palette

import cn.nukkit.nbt.stream.NBTInputStream
import cn.nukkit.nbt.tag.*
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.utils.*
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.Pair
import java.io.IOException

/**
 * Allay Project 11/12/2023
 *
 * @author Cool_Loong
 */
object PaletteUtils {
    fun fastReadBlockHash(input: NBTInputStream, byteBuf: ByteBuf): Pair<Int?, SemVersion?>? {
        try {
            byteBuf.markReaderIndex()
            val typeId = input.readUnsignedByte()
            input.skipBytes(input.readUnsignedShort()) //Skip Root tag name
            return deserialize(input, byteBuf, typeId, 16)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    private fun deserialize(
        input: NBTInputStream,
        byteBuf: ByteBuf,
        type: Int,
        maxDepth: Int
    ): Pair<Int?, SemVersion?>? {
        require(maxDepth >= 0) { "NBT compound is too deeply nested" }
        when (type) {
            Tag.TAG_End -> {
            }

            Tag.TAG_Byte -> input.skipBytes(1)
            Tag.TAG_Short -> input.skipBytes(2)
            Tag.TAG_Int, Tag.TAG_Float -> input.skipBytes(4)
            Tag.TAG_Long, Tag.TAG_Double -> input.skipBytes(8)
            Tag.TAG_Byte_Array -> input.skipBytes(input.readInt())
            Tag.TAG_String -> input.skipBytes(input.readUnsignedShort())
            Tag.TAG_Compound -> {
                var nbtType: Int
                while ((input.readUnsignedByte().also { nbtType = it }) != Tag.TAG_End.toInt()) {
                    val end = byteBuf.readerIndex()
                    val name = input.readUTF()
                    if (name == "version") {
                        val version = input.readInt()
                        byteBuf.resetReaderIndex()
                        if (version != ProtocolInfo.BLOCK_STATE_VERSION_NO_REVISION) {
                            return Pair.of(null, getSemVersion(version))
                        }
                        val result = ByteArray(end - byteBuf.readerIndex())
                        input.readFully(result)
                        result[result.size - 1] = 0 //because an End Tag be put when at the end serialize tag

                        input.skipBytes(input.readUnsignedShort()) //UTF
                        deserialize(input, byteBuf, nbtType, maxDepth - 1) //Value
                        input.skipBytes(1) //end tag
                        var i = HashUtils.fnv1a_32(result)
                        if (i == 147887818) i = -2 //minecraft:unknown

                        return Pair.of(i, null)
                    }
                    deserialize(input, byteBuf, nbtType, maxDepth - 1)
                }
            }

            Tag.TAG_List -> {
                val typeId = input.readUnsignedByte()
                val listLength = input.readInt()
                for (i in 0..<listLength) {
                    deserialize(input, byteBuf, typeId, maxDepth - 1)
                }
            }

            Tag.TAG_Int_Array -> input.skipBytes(input.readInt() * 4)
        }
        return null
    }

    fun getSemVersion(version: Int): SemVersion {
        val major = (version shr 24) and 0xFF
        val minor = (version shr 16) and 0xFF
        val patch = (version shr 8) and 0xFF
        val revision = version and 0xFF
        return SemVersion(major, minor, patch, revision, 0)
    }
}
