package org.chorus.network.protocol.types


class CommandOutputMessage(
    var internal: Boolean,
    var messageId: String, var parameters: Array<String?>
) {
    constructor(messageId: String) : this(false, messageId, arrayOf<String?>())

    constructor(messageId: String, vararg parameters: String?) : this(false, messageId, parameters)
}
