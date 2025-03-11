package org.chorus.entity

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.*
import org.chorus.entity.data.*
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.event.player.EntityFreezeEvent
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.AddPlayerPacket
import org.chorus.network.protocol.MovePlayerPacket
import org.chorus.network.protocol.RemoveEntityPacket
import org.chorus.network.protocol.SetEntityLinkPacket
import org.chorus.network.protocol.types.EntityLink
import java.util.*
import java.util.function.Predicate


open class EntityHuman(chunk: IChunk?, nbt: CompoundTag) : EntityHumanType(chunk, nbt) {
    @JvmField
    protected var uuid: UUID? = null
    @JvmField
    protected var rawUUID: ByteArray
    protected var skin: Skin? = null

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getLength(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.8f
    }

    override fun getSwimmingHeight(): Float {
        return 0.6f
    }

    override fun getEyeHeight(): Float {
        return (boundingBox!!.getMaxY() - boundingBox!!.getMinY() - 0.18).toFloat()
    }

    /**
     * 偏移客户端传输玩家位置的y轴误差
     *
     * @return the base offset
     */
    override fun getBaseOffset(): Float {
        return 1.62f
    }

    override fun getSkin(): Skin? {
        return skin
    }

    override fun setSkin(skin: Skin?) {
        this.skin = skin
    }

    override fun getUniqueId(): UUID? {
        return uuid
    }

    override fun setUniqueId(uuid: UUID?) {
        this.uuid = uuid
    }

    fun setRawUniqueId(rawUUID: ByteArray) {
        this.rawUUID = rawUUID
    }

    fun getRawUniqueId(): ByteArray {
        return rawUUID
    }

    override fun initEntity() {
        initHumanEntity(this)
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Human"
    }

    override fun getName(): String {
        return this.getNameTag()
    }

    override fun saveNBT() {
        super.saveNBT()
        saveHumanEntity(this)
    }

    override fun entityBaseTick(): Boolean {
        return this.entityBaseTick(1)
    }

    override fun entityBaseTick(tickDiff: Int): Boolean {
        val hasUpdate: Boolean = super.entityBaseTick(tickDiff)
        //handle human entity freeze
        val collidedWithPowderSnow: Boolean = getTickCachedCollisionBlocks()!!.stream().anyMatch(
            Predicate { block: Block -> block.getId() == Block.POWDER_SNOW })
        if (this.getFreezingTicks() < 140 && collidedWithPowderSnow) {
            if (getFreezingTicks() == 0) { //玩家疾跑进来要设置为非疾跑，统一为默认速度0.1
                this.setSprinting(false)
            }
            this.addFreezingTicks(1)
            val event: EntityFreezeEvent = EntityFreezeEvent(this)
            Server.instance.pluginManager.callEvent(event)
            if (!event.isCancelled) {
                this.setMovementSpeed(Math.max(0.05, getMovementSpeed() - 3.58e-4).toFloat())
            }
        } else if (this.getFreezingTicks() > 0 && !collidedWithPowderSnow) {
            this.addFreezingTicks(-1)
            this.setMovementSpeed(
                Math.min(Player.DEFAULT_SPEED.toDouble(), getMovementSpeed() + 3.58e-4).toFloat()
            ) //This magic number is to change the player's 0.05 speed within 140tick
        }
        if (this.getFreezingTicks() == 140 && level!!.getTick() % 40 == 0) {
            this.attack(EntityDamageEvent(this, DamageCause.FREEZING, getFrostbiteInjury().toFloat()))
        }
        return hasUpdate
    }

    override fun moveDelta() {
        val pk: MovePlayerPacket = MovePlayerPacket()
        pk.eid = this.getId()
        pk.x = position.x.toFloat()
        pk.y = position.y.toFloat()
        pk.z = position.z.toFloat()
        pk.yaw = rotation.yaw.toFloat()
        pk.headYaw = headYaw.toFloat()
        pk.pitch = rotation.pitch.toFloat()
        if (this.riding != null) {
            pk.ridingEid = riding!!.getId()
            pk.mode = MovePlayerPacket.MODE_PITCH
        }

        Server.broadcastPacket(getViewers().values(), pk)
    }

    override fun spawnTo(player: Player) {
        if (this !== player && !hasSpawned.containsKey(player.getLoaderId())) {
            hasSpawned.put(player.getLoaderId(), player)

            check(skin!!.isValid()) { this.getClass().getSimpleName() + " must have a valid skin set" }

            if (this is Player) Server.instance.updatePlayerListData(
                this.getUniqueId(),
                this.getId(), this.getDisplayName(),
                this.skin, this.loginChainData.XUID, arrayOf(player)
            )
            else Server.instance.updatePlayerListData(
                this.getUniqueId(),
                this.getId(),
                this.getName(),
                this.skin,
                arrayOf(player)
            )

            val pk: AddPlayerPacket = AddPlayerPacket()
            pk.uuid = this.getUniqueId()
            pk.username = this.getName()
            pk.entityUniqueId = this.getId()
            pk.entityRuntimeId = this.getId()
            pk.x = position.x.toFloat()
            pk.y = position.y.toFloat()
            pk.z = position.z.toFloat()
            pk.speedX = motion.x.toFloat()
            pk.speedY = motion.y.toFloat()
            pk.speedZ = motion.z.toFloat()
            pk.yaw = rotation.yaw.toFloat()
            pk.pitch = rotation.pitch.toFloat()
            pk.item = getInventory().getItemInHand()
            pk.entityData = this.entityDataMap
            player.dataPacket(pk)

            inventory!!.sendArmorContents(player)
            offhandInventory!!.sendContents(player)

            if (this.riding != null) {
                val pkk: SetEntityLinkPacket = SetEntityLinkPacket()
                pkk.vehicleUniqueId = riding!!.getId()
                pkk.riderUniqueId = this.getId()
                pkk.type = EntityLink.Type.RIDER
                pkk.immediate = 1

                player.dataPacket(pkk)
            }

            if (this !is Player) {
                Server.instance.removePlayerListData(this.getUniqueId(), player)
            }
        }
    }

    override fun despawnFrom(player: Player) {
        if (hasSpawned.containsKey(player.getLoaderId())) {
            val pk: RemoveEntityPacket = RemoveEntityPacket()
            pk.eid = this.getId()
            player.dataPacket(pk)
            hasSpawned.remove(player.getLoaderId())
        }
    }

    override fun close() {
        if (!this.closed) {
            if (inventory != null && (this !is Player || this.loggedIn)) {
                for (viewer: Player in inventory!!.getViewers()) {
                    viewer.removeWindow(this.inventory)
                }
            }

            super.close()
        }
    }

    override fun onBlock(entity: Entity?, event: EntityDamageEvent?, animate: Boolean) {
        super.onBlock(entity, event, animate)
        var shield: Item? = getInventory().getItemInHand()
        var shieldOffhand: Item? = getOffhandInventory()!!.getItem(0)
        if (shield is ItemShield) {
            shield = damageArmor(shield, entity, event!!)
            getInventory().setItemInHand(shield)
        } else if (shieldOffhand is ItemShield) {
            shieldOffhand = damageArmor(shieldOffhand, entity, event!!)
            getOffhandInventory()!!.setItem(0, shieldOffhand)
        }
    }
}
