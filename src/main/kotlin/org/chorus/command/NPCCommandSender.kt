package org.chorus.command

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.entity.mob.EntityNPC
import org.chorus.lang.CommandOutputContainer
import org.chorus.lang.TextContainer
import org.chorus.level.Locator
import org.chorus.level.Transform
import org.chorus.permission.PermissibleBase
import org.chorus.permission.Permission
import org.chorus.permission.PermissionAttachment
import org.chorus.plugin.Plugin

class NPCCommandSender(private val npc: EntityNPC, val initiator: Player) : CommandSender {
    protected var perm: PermissibleBase = PermissibleBase(this)

    fun getNpc(): EntityNPC {
        return npc
    }

    override fun sendMessage(message: String) {}

    override fun sendMessage(message: TextContainer) {}

    override fun sendCommandOutput(container: CommandOutputContainer) {}

    override fun getName() = npc.getEntityName()

    override val isPlayer: Boolean
        get() = false

    override val isEntity: Boolean
        get() = true

    override fun asEntity(): Entity {
        return npc
    }

    override fun asPlayer(): Player? {
        return null
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

    override fun getLocator(): Locator {
        return npc.locator
    }

    override fun getTransform(): Transform {
        return npc.transform
    }

    override var isOp: Boolean = true
        set(_) = throw UnsupportedOperationException("Can't set isOp of NPCCommandSender")
}
