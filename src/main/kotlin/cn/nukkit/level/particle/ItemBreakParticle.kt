package cn.nukkit.level.particle

import cn.nukkit.item.Item
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author xtypr
 * @since 2015/11/21
 */
class ItemBreakParticle(pos: Vector3, item: Item) :
    Particle(pos.x, pos.y, pos.z) {
    private val data: Int

    init {
        this.data = (item.runtimeId shl 16 or item.damage)
    }

    override fun encode(): Array<DataPacket> {
        val packet = LevelEventPacket()
        packet.evid = (LevelEventPacket.EVENT_ADD_PARTICLE_MASK or Particle.Companion.TYPE_ICON_CRACK).toShort().toInt()
        packet.x = x.toFloat()
        packet.y = y.toFloat()
        packet.z = z.toFloat()
        packet.data = this.data
        return arrayOf(packet)
    }
}
