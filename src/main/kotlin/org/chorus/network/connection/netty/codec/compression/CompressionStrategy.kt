package org.chorus.network.connection.netty.codec.compression

import cn.nukkit.network.connection.netty.BedrockBatchWrapper
import cn.nukkit.network.protocol.types.CompressionAlgorithm


interface CompressionStrategy {
    fun getCompression(wrapper: BedrockBatchWrapper?): BatchCompression

    fun getCompression(algorithm: CompressionAlgorithm): BatchCompression?

    val defaultCompression: BatchCompression
}
