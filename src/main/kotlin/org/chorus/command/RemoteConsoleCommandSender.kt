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
        message = Server.instance.baseLang.tr(message)
        messages.append(message.trim { it <= ' ' }).append("\n")
    }

    override fun sendMessage(message: TextContainer) {
        this.sendMessage(Server.instance.baseLang.tr(message))
    }

    fun getMessages(): String {
        return messages.toString()
    }

    override val name: String
        get() = "Rcon"
}
