package cn.nukkit.command

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.lang.CommandOutputContainer
import cn.nukkit.level.Locator
import cn.nukkit.level.Transform
import cn.nukkit.permission.Permission
import cn.nukkit.plugin.Plugin

class NPCCommandSender(npc: EntityNPC, val initiator: Player) : CommandSender {
    protected var perm: PermissibleBase = PermissibleBase(this)
    private val npc: EntityNPC = npc

    fun getNpc(): EntityNPC {
        return npc
    }

    override fun sendMessage(message: String) {}

    override fun sendMessage(message: TextContainer) {}

    override fun sendCommandOutput(container: CommandOutputContainer) {}

    override val server: Server?
        get() = npc.getServer()

    override val name: String
        get() = npc.getName()

    override val isPlayer: Boolean
        get() = false

    override val isEntity: Boolean
        get() = true

    override fun asEntity(): Entity? {
        return npc
    }

    override fun asPlayer(): Player? {
        return null
    }

    override val locator: Locator
        get() = npc.getLocator()

    override val transform: Transform
        get() = npc.getTransform()

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

    override fun isOp(): Boolean {
        return true
    }

    override fun setOp(value: Boolean) {}
}
