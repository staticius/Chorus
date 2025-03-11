package org.chorus.event.player

import org.chorus.Player
import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerChatEvent @JvmOverloads constructor(
    player: Player?,
    message: String?,
    format: String = "chat.type.text",
    recipients: MutableSet<CommandSender>? = null
) :
    PlayerMessageEvent(), Cancellable {
    protected var recipients: MutableSet<CommandSender> = HashSet()
    @JvmField
    var format: String

    init {
        this.player = player
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

    override var player: Player?
        get() = super.player
        /**
         * Changes the player that is sending the message
         *
         * @param player messenger
         */
        set(player) {
            this.player = player
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
