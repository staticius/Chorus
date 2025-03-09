package org.chorus.utils

import org.chorus.Player
import org.chorus.entity.Attribute
import org.chorus.entity.Attribute.Companion.getAttribute
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.data.EntityDataMap
import org.chorus.network.protocol.*
import org.chorus.registry.Registries
import java.util.concurrent.ThreadLocalRandom

/**
 * @author boybook (Nukkit Project)
 */
class DummyBossBar private constructor(builder: Builder) {
    val player: Player = builder.player
    @JvmField
    val bossBarId: Long = builder.bossBarId

    private var text: String
    private var length: Float
    private var color: BossBarColor?

    init {
        this.text = builder.text
        this.length = builder.length
        this.color = builder.color
    }

    class Builder(val player: Player) {
        val bossBarId: Long =
            1095216660480L + ThreadLocalRandom.current().nextLong(0, 0x7fffffffL)

        var text: String = ""
        var length: Float = 100f
        var color: BossBarColor? = null

        fun text(text: String): Builder {
            this.text = text
            return this
        }

        fun length(length: Float): Builder {
            if (length >= 0 && length <= 100) this.length = length
            return this
        }

        fun color(color: BossBarColor?): Builder {
            this.color = color
            return this
        }

        fun build(): DummyBossBar {
            return DummyBossBar(this)
        }
    }

    fun getText(): String {
        return text
    }

    fun setText(text: String) {
        if (this.text != text) {
            this.text = text
            this.updateBossEntityNameTag()
            this.sendSetBossBarTitle()
        }
    }

    fun getLength(): Float {
        return length
    }

    fun setLength(length: Float) {
        if (this.length != length) {
            this.length = length
            this.sendAttributes()
            this.sendSetBossBarLength()
        }
    }

    fun setColor(color: BossBarColor?) {
        val currentColor = this.color
        if (currentColor == null || currentColor != color) {
            this.color = color
            this.sendSetBossBarTexture()
        }
    }

    fun getColor(): BossBarColor? {
        return this.color
    }

    private fun createBossEntity() {
        val pkAdd = AddEntityPacket()

        pkAdd.type = Registries.ENTITY.getEntityNetworkId(EntityID.CREEPER)
        pkAdd.entityUniqueId = bossBarId
        pkAdd.entityRuntimeId = bossBarId
        pkAdd.x = player.position.x.toFloat()
        pkAdd.y = -74f // Below the bedrock
        pkAdd.z = player.position.z.toFloat()
        pkAdd.speedX = 0f
        pkAdd.speedY = 0f
        pkAdd.speedZ = 0f
        val entityDataMap = EntityDataMap()
        entityDataMap.getOrCreateFlags()
        entityDataMap.put(Entity.AIR_SUPPLY, 400)
        entityDataMap.put(Entity.AIR_SUPPLY_MAX, 400)
        entityDataMap.put(Entity.LEASH_HOLDER, -1)
        entityDataMap.put(Entity.NAME, text)
        entityDataMap.put(Entity.SCALE, 0)
        pkAdd.entityData = entityDataMap
        player.dataPacket(pkAdd)
    }

    private fun sendAttributes() {
        val pkAttributes = UpdateAttributesPacket()
        pkAttributes.entityId = bossBarId
        val attr = getAttribute(Attribute.MAX_HEALTH)
        attr!!.setMaxValue(100f) // Max value - We need to change the max value first, or else the "setValue" will return a IllegalArgumentException
        attr.setValue(length) // Entity health
        pkAttributes.entries = arrayOf<Attribute?>(attr)
        player.dataPacket(pkAttributes)
    }

    private fun sendShowBossBar() {
        val pkBoss = BossEventPacket()
        pkBoss.bossEid = bossBarId
        pkBoss.type = BossEventPacket.TYPE_SHOW
        pkBoss.title = text
        pkBoss.healthPercent = this.length / 100
        player.dataPacket(pkBoss)
    }

    private fun sendHideBossBar() {
        val pkBoss = BossEventPacket()
        pkBoss.bossEid = bossBarId
        pkBoss.type = BossEventPacket.TYPE_HIDE
        player.dataPacket(pkBoss)
    }

    private fun sendSetBossBarTexture() {
        val pk = BossEventPacket()
        pk.bossEid = this.bossBarId
        pk.type = BossEventPacket.TYPE_TEXTURE
        pk.color = if (color != null) color!!.ordinal else 0
        player.dataPacket(pk)
    }

    private fun sendSetBossBarTitle() {
        val pkBoss = BossEventPacket()
        pkBoss.bossEid = bossBarId
        pkBoss.type = BossEventPacket.TYPE_TITLE
        pkBoss.title = text
        pkBoss.healthPercent = this.length / 100
        player.dataPacket(pkBoss)
    }

    private fun sendSetBossBarLength() {
        val pkBoss = BossEventPacket()
        pkBoss.bossEid = bossBarId
        pkBoss.type = BossEventPacket.TYPE_HEALTH_PERCENT
        pkBoss.healthPercent = this.length / 100
        player.dataPacket(pkBoss)
    }

    /**
     * Don't let the entity go too far from the player, or the BossBar will disappear.
     * Update boss entity's position when teleport and each 5s.
     */
    fun updateBossEntityPosition() {
        val pk = MoveEntityAbsolutePacket()
        pk.eid = this.bossBarId
        pk.x = player.position.x
        pk.y = -74.0
        pk.z = player.position.z
        pk.headYaw = 0.0
        pk.yaw = 0.0
        pk.pitch = 0.0
        player.dataPacket(pk)
    }

    private fun updateBossEntityNameTag() {
        val pk = SetEntityDataPacket()
        pk.eid = this.bossBarId
        val entityDataMap = EntityDataMap()
        entityDataMap.put(Entity.NAME, text)
        pk.entityData = entityDataMap
        player.dataPacket(pk)
    }

    private fun removeBossEntity() {
        val pkRemove = RemoveEntityPacket()
        pkRemove.eid = bossBarId
        player.dataPacket(pkRemove)
    }

    fun create() {
        createBossEntity()
        sendAttributes()
        sendShowBossBar()
        sendSetBossBarLength()
        if (color != null) this.sendSetBossBarTexture()
    }

    /**
     * Once the player has teleported, resend Show BossBar
     */
    fun reshow() {
        updateBossEntityPosition()
        sendShowBossBar()
        sendSetBossBarLength()
    }

    fun destroy() {
        sendHideBossBar()
        removeBossEntity()
    }
}
