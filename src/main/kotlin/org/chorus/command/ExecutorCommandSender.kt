package org.chorus.command

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.Entity
import org.chorus.lang.CommandOutputContainer
import org.chorus.level.Locator
import org.chorus.level.Transform
import org.chorus.permission.Permission
import org.chorus.plugin.Plugin

//used for executing commands in place of an entity
class ExecutorCommandSender(executor: CommandSender, entity: Entity?, executeTransform: Transform?) :
    CommandSender {
    private var executor: CommandSender? = null
    private val entity: Entity?
    private val executeTransform: Transform?

    init {
        if (executor is ExecutorCommandSender) {
            this.executor = executor.getExecutor()
        } else {
            this.executor = executor
        }
        this.entity = entity
        this.executeTransform = executeTransform
    }

    override fun sendMessage(message: String) {
        executor.sendMessage(message)
    }

    override fun sendMessage(message: TextContainer) {
        executor.sendMessage(message)
    }

    override fun sendCommandOutput(container: CommandOutputContainer) {
        executor!!.sendCommandOutput(container)
    }

    override val server: Server?
        get() = executor.getServer()

    override val name: String
        get() = entity!!.name

    override val isPlayer: Boolean
        get() = entity is Player

    override fun isEntity(): Boolean {
        return true
    }

    override fun asEntity(): Entity? {
        return this.entity
    }

    override fun asPlayer(): Player? {
        return if (isPlayer) entity as Player? else null
    }

    override val locator: Locator
        get() = (executeTransform ?: entity!!.locator).clone()

    override val transform: Transform
        get() = (executeTransform ?: entity!!.transform).clone()

    override fun isOp(): Boolean {
        return executor!!.isOp
    }

    override fun setOp(value: Boolean) {
        executor!!.isOp = value
    }

    override fun isPermissionSet(name: String): Boolean {
        return executor!!.isPermissionSet(name)
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        return executor!!.isPermissionSet(permission)
    }

    override fun hasPermission(name: String): Boolean {
        return executor!!.hasPermission(name)
    }

    override fun hasPermission(permission: Permission): Boolean {
        return executor!!.hasPermission(permission)
    }

    override fun addAttachment(plugin: Plugin): PermissionAttachment {
        return executor!!.addAttachment(plugin)
    }

    override fun addAttachment(plugin: Plugin, name: String): PermissionAttachment {
        return executor!!.addAttachment(plugin, name)
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment {
        return executor!!.addAttachment(plugin, name, value)
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        executor!!.removeAttachment(attachment)
    }

    override fun recalculatePermissions() {
        executor!!.recalculatePermissions()
    }

    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> {
        return executor!!.effectivePermissions
    }

    fun getExecutor(): CommandSender? {
        return if (executor is ExecutorCommandSender) executor.getExecutor()
        else executor
    }
}
