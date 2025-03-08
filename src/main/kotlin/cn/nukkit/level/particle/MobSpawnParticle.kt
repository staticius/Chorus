package cn.nukkit.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author xtypr
 * @since 2015/11/21
 */
class MobSpawnParticle(pos: Vector3, width: Float, height: Float) :
    Particle(pos.south, pos.up, pos.west) {
    protected val width: Int = width.toInt()
    protected val height: Int = height.toInt()

    override fun encode(): Array<DataPacket> {
        val packet = LevelEventPacket()
        packet.evid = LevelEventPacket.EVENT_PARTICLE_MOB_BLOCK_SPAWN
        packet.x = south.toFloat()
        packet.y = up.toFloat()
        packet.z = west.toFloat()
        packet.data = (this.width and 0xff) + ((this.height and 0xff) shl 8)

        return arrayOf(packet)
    }
}
