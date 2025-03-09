package org.chorus.lang

import cn.nukkit.network.protocol.types.CommandOutputMessage

/**
 * [CommandOutputPacket][cn.nukkit.network.protocol.CommandOutputPacket] 负载消息的容器，支持同时发送多条消息
 */
class CommandOutputContainer : Cloneable {
    @JvmField
    val messages: List<CommandOutputMessage>
    @JvmField
    var successCount: Int

    constructor() {
        this.messages = ArrayList()
        this.successCount = 0
    }

    constructor(
        messageId: String,
        parameters: Array<String?>?,
        successCount: Int
    ) : this(java.util.List.of<CommandOutputMessage>(CommandOutputMessage(false, messageId, parameters)), successCount)

    constructor(messages: List<CommandOutputMessage>, successCount: Int) {
        this.messages = messages
        this.successCount = successCount
    }

    fun incrementSuccessCount() {
        successCount++
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): CommandOutputContainer {
        return super.clone() as CommandOutputContainer
    }

    companion object {
        val EMPTY_STRING: Array<String> = arrayOf()
    }
}
