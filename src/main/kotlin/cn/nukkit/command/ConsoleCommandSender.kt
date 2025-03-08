package cn.nukkit.command

import cn.nukkit.Server
import cn.nukkit.lang.CommandOutputContainer
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.level.GameRule
import cn.nukkit.permission.Permission
import cn.nukkit.plugin.Plugin
import lombok.extern.slf4j.Slf4j

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Slf4j
open class ConsoleCommandSender : CommandSender {
    private val perm: PermissibleBase

    init {
        this.perm = PermissibleBase(this)
    }

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

    override fun addAttachment(plugin: Plugin, name: String): PermissionAttachment {
        return perm.addAttachment(plugin, name)
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment {
        return perm.addAttachment(plugin, name, value)
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        perm.removeAttachment(attachment)
    }

    override fun recalculatePermissions() {
        perm.recalculatePermissions()
    }

    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> {
        return perm.getEffectivePermissions()
    }

    override val isPlayer: Boolean
        get() = false

    override val server: Server?
        get() = Server.getInstance()

    override fun sendMessage(message: String) {
        for (line in message.trim { it <= ' ' }.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            ConsoleCommandSender.log.info(line)
        }
    }

    override fun sendMessage(message: TextContainer) {
        this.sendMessage(server!!.language.tr(message))
    }

    override fun sendCommandOutput(container: CommandOutputContainer) {
        if (this.transform.level.gameRules.getBoolean(GameRule.SEND_COMMAND_FEEDBACK)) {
            for (msg in container.messages) {
                var text =
                    server!!.language.tr(TranslationContainer(msg.messageId, *msg.parameters))
                val event: ConsoleCommandOutputEvent = ConsoleCommandOutputEvent(this, text)
                server!!.pluginManager.callEvent(event)
                if (event.isCancelled()) continue
                text = event.getMessage()
                this.sendMessage(text)
            }
        }
    }

    override val name: String
        get() = "CONSOLE"

    override fun isOp(): Boolean {
        return true
    }

    override fun setOp(value: Boolean) {
    }
}
