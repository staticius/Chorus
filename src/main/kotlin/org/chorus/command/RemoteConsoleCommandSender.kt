package org.chorus.command

/**
 * Represents an RCON command sender.
 *
 * @author Tee7even
 */
class RemoteConsoleCommandSender : ConsoleCommandSender() {
    private val messages = StringBuilder()

    override fun sendMessage(message: String) {
        var message = message
        message = this.server.baseLang.tr(message)
        messages.append(message.trim { it <= ' ' }).append("\n")
    }

    override fun sendMessage(message: TextContainer) {
        this.sendMessage(this.server.baseLang.tr(message))
    }

    fun getMessages(): String {
        return messages.toString()
    }

    override val name: String
        get() = "Rcon"
}
