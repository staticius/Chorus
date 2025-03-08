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
    Particle(pos.south, pos.up, pos.west) {
    private val data: Int

    init {
        this.data = (item.runtimeId shl 16 or item.damage)
    }

    override fun encode(): Array<DataPacket> {
        val packet = LevelEventPacket()
        packet.evid = (LevelEventPacket.EVENT_ADD_PARTICLE_MASK or Particle.Companion.TYPE_ICON_CRACK).toShort().toInt()
        packet.x = south.toFloat()
        packet.y = up.toFloat()
        packet.z = west.toFloat()
        packet.data = this.data
        return arrayOf(packet)
    }
}
