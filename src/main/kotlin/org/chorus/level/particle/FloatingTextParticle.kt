package org.chorus.level.particle

import com.google.common.base.Strings
import org.chorus.entity.data.*
import org.chorus.item.Item
import org.chorus.level.*
import org.chorus.math.Vector3
import org.chorus.network.protocol.*
import org.chorus.utils.SerializedImage
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.set

class FloatingTextParticle private constructor(
    private val level: Level?,
    pos: Vector3,
    title: String,
    text: String
) :
    Particle(pos.x, pos.y, pos.z) {
    private var uuid: UUID = UUID.randomUUID()
    var entityId: Long = -1
        private set
    private var isInvisible: Boolean = false
    private var entityData: EntityDataMap = EntityDataMap()

    @JvmOverloads
    constructor(transform: Transform, title: String, text: String = "") : this(
        transform.level,
        transform.position,
        title,
        text
    )

    @JvmOverloads
    constructor(pos: Vector3, title: String, text: String = "") : this(null, pos, title, text)

    init {
        entityData.setFlag(EntityFlag.NO_AI, true)
        entityData[EntityDataTypes.LEASH_HOLDER] = -1
        entityData[EntityDataTypes.SCALE] = 0.01f // zero causes problems on debug builds?
        entityData[EntityDataTypes.HEIGHT] = 0.01f
        entityData[EntityDataTypes.WIDTH] = 0.01f
        entityData[EntityDataTypes.NAMETAG_ALWAYS_SHOW] = 1.toByte()
        if (!Strings.isNullOrEmpty(title)) {
            entityData[EntityDataTypes.NAME] = title
        }
        if (!Strings.isNullOrEmpty(text)) {
            entityData[EntityDataTypes.SCORE] = text
        }
    }

    var text: String
        get() = entityData.get<String>(EntityDataTypes.SCORE)
        set(text) {
            entityData[EntityDataTypes.SCORE] = text
            sendEntityData()
        }

    var title: String
        get() = entityData.get<String>(EntityDataTypes.NAME)
        set(title) {
            entityData[EntityDataTypes.NAME] = title
            sendEntityData()
        }

    private fun sendEntityData() {
        if (level != null) {
            val packet = SetEntityDataPacket()
            packet.eid = entityId
            packet.entityData = entityData
            level.addChunkPacket(chunkX, chunkZ, packet)
        }
    }

    fun setInvisible() {
        this.isInvisible = true
    }

    override fun encode(): Array<DataPacket> {
        val packets = ArrayList<DataPacket>()

        if (this.entityId == -1L) {
            this.entityId = 1095216660480L + ThreadLocalRandom.current().nextLong(0, 0x7fffffffL)
        } else {
            val pk = RemoveEntityPacket()
            pk.eid = this.entityId

            packets.add(pk)
        }

        if (!this.isInvisible) {
            val entry = arrayOf(
                PlayerListPacket.Entry(uuid, entityId, entityData.get<String>(EntityDataTypes.NAME), EMPTY_SKIN)
            )
            val playerAdd = PlayerListPacket()
            playerAdd.entries = entry
            playerAdd.type = PlayerListPacket.TYPE_ADD
            packets.add(playerAdd)

            val pk = AddPlayerPacket()
            pk.uuid = uuid
            pk.username = ""
            pk.entityUniqueId = this.entityId
            pk.entityRuntimeId = this.entityId
            pk.x = x.toFloat()
            pk.y = (this.y - 0.75).toFloat()
            pk.z = z.toFloat()
            pk.speedX = 0f
            pk.speedY = 0f
            pk.speedZ = 0f
            pk.yaw = 0f
            pk.pitch = 0f
            pk.entityData = this.entityData
            pk.item = Item.AIR
            packets.add(pk)

            val playerRemove = PlayerListPacket()
            playerRemove.entries = entry
            playerRemove.type = PlayerListPacket.TYPE_REMOVE
            packets.add(playerRemove)
        }

        return packets.toArray(DataPacket.EMPTY_ARRAY)
    }

    companion object {
        private val EMPTY_SKIN = Skin()
        private val SKIN_DATA: SerializedImage = SerializedImage.fromLegacy(ByteArray(4096))

        init {
            EMPTY_SKIN.setSkinData(SKIN_DATA)
            EMPTY_SKIN.generateSkinId("FloatingText")
        }
    }
}
