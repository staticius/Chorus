package org.chorus.network.connection.netty.codec.compression

import org.chorus.network.connection.netty.BedrockBatchWrapper
import org.chorus.network.protocol.types.CompressionAlgorithm


interface CompressionStrategy {
    fun getCompression(wrapper: BedrockBatchWrapper?): BatchCompression

    fun getCompression(algorithm: CompressionAlgorithm): BatchCompression?

    val defaultCompression: BatchCompression
}
