package cn.nukkit.network.protocol

import cn.nukkit.math.Vector3f
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*
import java.util.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SpawnParticleEffectPacket : DataPacket() {
    var dimensionId: Int = 0
    var uniqueEntityId: Long = -1
    var position: Vector3f? = null
    var identifier: String? = null

    /**
     * @since v503
     */
    var molangVariablesJson: Optional<String> = Optional.empty()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(dimensionId.toByte().toInt())
        byteBuf.writeEntityUniqueId(uniqueEntityId)
        byteBuf.writeVector3f(position!!)
        byteBuf.writeString(identifier!!)
        byteBuf.writeBoolean(molangVariablesJson.isPresent)
        molangVariablesJson.ifPresent { str: String? ->
            byteBuf.writeString(
                str!!
            )
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SPAWN_PARTICLE_EFFECT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
