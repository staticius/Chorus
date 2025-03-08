package cn.nukkit.utils.collection

import net.jpountz.lz4.LZ4Compressor
import net.jpountz.lz4.LZ4Factory
import net.jpountz.lz4.LZ4FastDecompressor

object LZ4Freezer {
    val factory: LZ4Factory = LZ4Factory.fastestInstance()
    val compressor: LZ4Compressor = factory.fastCompressor()
    val deepCompressor: LZ4Compressor = factory.highCompressor()
    val decompressor: LZ4FastDecompressor = factory.fastDecompressor()
}
