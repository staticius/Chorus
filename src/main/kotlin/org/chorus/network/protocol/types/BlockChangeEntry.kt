package org.chorus.network.protocol.types

import org.chorus.math.BlockVector3

@JvmRecord
data class BlockChangeEntry(
    val blockPos: BlockVector3,
    val runtimeID: Long,
    val updateFlags: Int,
    val messageEntityID: Long,
    val messageType: MessageType
) {
    enum class MessageType {
        NONE,
        CREATE,
        DESTROY
    }
}
