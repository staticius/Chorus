package org.chorus.event.player

import org.chorus.Player
import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerChatEvent @JvmOverloads constructor(
    override var player: Player,
    message: String?,
    format: String = "chat.type.text",
    recipients: MutableSet<CommandSender>? = null
) :
    PlayerMessageEvent(), Cancellable {
    protected var recipients: MutableSet<CommandSender> = HashSet()

    @JvmField
    var format: String

    init {
        this.message = message

        this.format = format

        if (recipients == null) {
            for (permissible in Server.instance.pluginManager.getPermissionSubscriptions(Server.BROADCAST_CHANNEL_USERS)) {
                if (permissible is CommandSender) {
                    this.recipients.add(permissible)
                }
            }
        } else {
            this.recipients = recipients
        }
    }

    fun getRecipients(): Set<CommandSender> {
        return this.recipients
    }

    fun setRecipients(recipients: MutableSet<CommandSender>) {
        this.recipients = recipients
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
