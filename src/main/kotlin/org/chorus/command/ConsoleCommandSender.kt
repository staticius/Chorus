package org.chorus.command

import org.chorus.Server
import org.chorus.event.server.ConsoleCommandOutputEvent
import org.chorus.lang.CommandOutputContainer
import org.chorus.lang.TextContainer
import org.chorus.lang.TranslationContainer
import org.chorus.level.GameRule
import org.chorus.level.Locator
import org.chorus.level.Transform
import org.chorus.permission.PermissibleBase
import org.chorus.permission.Permission
import org.chorus.permission.PermissionAttachment
import org.chorus.plugin.Plugin
import org.chorus.utils.Loggable


open class ConsoleCommandSender : CommandSender {
    private val perm: PermissibleBase = PermissibleBase(this)

    override fun isPermissionSet(name: String): Boolean {
        return perm.isPermissionSet(name)
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        return perm.isPermissionSet(permission)
    }

    override fun hasPermission(name: String): Boolean {
        return perm.hasPermission(name)
    }

    override fun hasPermission(permission: Permission): Boolean {
        return perm.hasPermission(permission)
    }

    override fun addAttachment(plugin: Plugin): PermissionAttachment {
        return perm.addAttachment(plugin)
    }

    override fun addAttachment(plugin: Plugin, name: String?): PermissionAttachment {
        return perm.addAttachment(plugin, name)
    }

    override fun addAttachment(plugin: Plugin, name: String?, value: Boolean?): PermissionAttachment {
        return perm.addAttachment(plugin, name, value)
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        perm.removeAttachment(attachment)
    }

    override fun recalculatePermissions() {
        perm.recalculatePermissions()
    }

    override val isPlayer: Boolean
        get() = false

    override fun sendMessage(message: String) {
        for (line in message.trim { it <= ' ' }.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            ConsoleCommandSender.log.info(line)
        }
    }

    override fun sendMessage(message: TextContainer) {
        this.sendMessage(Server.instance.baseLang.tr(message))
    }

    override fun sendCommandOutput(container: CommandOutputContainer) {
        if (this.getTransform().level.gameRules.getBoolean(GameRule.SEND_COMMAND_FEEDBACK)) {
            for (msg in container.messages) {
                var text = Server.instance.baseLang.tr(TranslationContainer(msg.messageId, *msg.parameters))
                val event = ConsoleCommandOutputEvent(this, text)
                Server.instance.pluginManager.callEvent(event)
                if (event.isCancelled) continue
                text = event.message
                this.sendMessage(text)
            }
        }
    }

    override fun getLocator(): Locator {
        throw UnsupportedOperationException("Can't get locator of ConsoleCommandSender")
    }

    override fun getTransform(): Transform {
        throw UnsupportedOperationException("Can't get transform of ConsoleCommandSender")
    }

    override fun getName() = "CONSOLE"

    override var isOp = true
        set(_) = throw UnsupportedOperationException("Can't set isOp of ConsoleCommandSender")

    companion object : Loggable
}
