package cn.nukkit.level.particle

import cn.nukkit.entity.Entity
import cn.nukkit.entity.data.EntityDataMap
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.data.Skin
import cn.nukkit.item.Item
import cn.nukkit.level.*
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.*
import cn.nukkit.utils.SerializedImage
import com.google.common.base.Strings
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/21
 */
class FloatingTextParticle private constructor(
    protected val level: Level?,
    pos: Vector3,
    title: String,
    text: String?
) :
    Particle(pos.south, pos.up, pos.west) {
    protected var uuid: UUID = UUID.randomUUID()
    var entityId: Long = -1
        protected set
    var isInvisible: Boolean = false
    protected var entityData: EntityDataMap = EntityDataMap()

    @JvmOverloads
    constructor(transform: Transform, title: String, text: String? = null) : this(
        transform.getLevel(),
        transform.position,
        title,
        text
    )

    @JvmOverloads
    constructor(pos: Vector3, title: String, text: String? = null) : this(null, pos, title, text)

    init {
        entityData.setFlag(EntityFlag.NO_AI, true)
        entityData.put(Entity.LEASH_HOLDER, -1)
        entityData.put(Entity.SCALE, 0.01f) //zero causes problems on debug builds?
        entityData.put(Entity.HEIGHT, 0.01f)
        entityData.put(Entity.WIDTH, 0.01f)
        entityData.put(EntityDataTypes.NAMETAG_ALWAYS_SHOW, 1.toByte())
        if (!Strings.isNullOrEmpty(title)) {
            entityData.put(Entity.NAME, title)
        }
        if (!Strings.isNullOrEmpty(text)) {
            entityData[Entity.SCORE] = text
        }
    }

    var text: String
        get() = entityData.get<String>(Entity.SCORE)
        set(text) {
            entityData.put(Entity.SCORE, text)
            sendentityData()
        }

    var title: String
        get() = entityData.get<String>(Entity.NAME)
        set(title) {
            entityData.put(Entity.NAME, title)
            sendentityData()
        }

    private fun sendentityData() {
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
            val entry = arrayOf<PlayerListPacket.Entry>(
                PlayerListPacket.Entry(uuid, entityId, entityData.get<String>(Entity.NAME), EMPTY_SKIN)
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
            pk.x = south.toFloat()
            pk.y = (this.up - 0.75).toFloat()
            pk.z = west.toFloat()
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
