package org.chorus_oss.chorus.utils

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.Attribute
import org.chorus_oss.chorus.entity.Attribute.Companion.getAttribute
import org.chorus_oss.chorus.entity.EntityID
import org.chorus_oss.chorus.entity.data.EntityDataMap
import org.chorus_oss.chorus.entity.data.EntityDataTypes
import org.chorus_oss.chorus.experimental.network.protocol.utils.invoke
import org.chorus_oss.chorus.network.protocol.*
import org.chorus_oss.protocol.types.ActorProperties
import org.chorus_oss.protocol.types.actor_data.ActorDataMap
import java.util.concurrent.ThreadLocalRandom

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
            if (length in 0.0..100.0) this.length = length
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
        player.sendPacket(
            org.chorus_oss.protocol.packets.AddActorPacket(
                actorUniqueID = this.bossBarId,
                actorRuntimeID = this.bossBarId.toULong(),
                actorType = EntityID.CREEPER,
                position = org.chorus_oss.protocol.types.Vector3f(player.position.asVector3f().setY(-74f)),
                velocity = org.chorus_oss.protocol.types.Vector3f(0f, 0f, 0f),
                rotation = org.chorus_oss.protocol.types.Vector2f(0f, 0f),
                headYaw = 0f,
                bodyYaw = 0f,
                attributes = emptyList(),
                actorData = run {
                    val entityDataMap = EntityDataMap()
                    entityDataMap.getOrCreateFlags()
                    entityDataMap[EntityDataTypes.AIR_SUPPLY] = 400
                    entityDataMap[EntityDataTypes.AIR_SUPPLY_MAX] = 400
                    entityDataMap[EntityDataTypes.LEASH_HOLDER] = -1
                    entityDataMap[EntityDataTypes.NAME] = text
                    entityDataMap[EntityDataTypes.SCALE] = 0
                    ActorDataMap(entityDataMap)
                },
                actorProperties = ActorProperties(emptyList(), emptyList()),
                actorLinks = emptyList()
            )
        )
    }

    private fun sendAttributes() {
        val pkAttributes = UpdateAttributesPacket()
        pkAttributes.entityId = bossBarId
        val attr = getAttribute(Attribute.MAX_HEALTH)
        attr.setMaxValue(100f) // Max value - We need to change the max value first, or else the "setValue" will return a IllegalArgumentException
        attr.setValue(length) // Entity health
        pkAttributes.entries = arrayOf(attr)
        player.dataPacket(pkAttributes)
    }

    private fun sendShowBossBar() {
        player.dataPacket(
            BossEventPacket(
                targetActorID = bossBarId,
                eventType = BossEventPacket.EventType.ADD,
                eventData = BossEventPacket.EventType.Companion.AddData(
                    name = text,
                    filteredName = text,
                    healthPercent = this.length / 100,
                    darkenScreen = 0,
                    color = 0,
                    overlay = 0
                )
            )
        )
    }

    private fun sendHideBossBar() {
        player.dataPacket(
            BossEventPacket(
                targetActorID = bossBarId,
                eventType = BossEventPacket.EventType.REMOVE,
                eventData = null,
            )
        )
    }

    private fun sendSetBossBarTexture() {
        player.dataPacket(
            BossEventPacket(
                targetActorID = this.bossBarId,
                eventType = BossEventPacket.EventType.UPDATE_STYLE,
                eventData = BossEventPacket.EventType.Companion.UpdateStyleData(
                    color = if (color != null) color!!.ordinal else 0,
                    overlay = 0,
                )
            )
        )
    }

    private fun sendSetBossBarTitle() {
        player.dataPacket(
            BossEventPacket(
                targetActorID = bossBarId,
                eventType = BossEventPacket.EventType.UPDATE_NAME,
                eventData = BossEventPacket.EventType.Companion.UpdateNameData(
                    name = text,
                    filteredName = text,
                )
            )
        )
    }

    private fun sendSetBossBarLength() {
        player.dataPacket(
            BossEventPacket(
                targetActorID = bossBarId,
                eventType = BossEventPacket.EventType.UPDATE_PERCENT,
                eventData = BossEventPacket.EventType.Companion.UpdatePercentData(
                    healthPercent = this.length / 100
                )
            )
        )
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
        entityDataMap[EntityDataTypes.NAME] = text
        pk.entityData = entityDataMap
        player.dataPacket(pk)
    }

    private fun removeBossEntity() {
        val pkRemove = RemoveActorPacket()
        pkRemove.actorUniqueID = bossBarId
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
