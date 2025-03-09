package org.chorus.level.particle

import org.chorus.math.Vector3
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.LevelEventPacket

/**
 * @author xtypr
 * @since 2015/11/21
 */
class MobSpawnParticle(pos: Vector3, width: Float, height: Float) :
    Particle(pos.x, pos.y, pos.z) {
    protected val width: Int = width.toInt()
    protected val height: Int = height.toInt()

    override fun encode(): Array<DataPacket> {
        val packet = LevelEventPacket()
        packet.evid = LevelEventPacket.EVENT_PARTICLE_MOB_BLOCK_SPAWN
        packet.x = x.toFloat()
        packet.y = y.toFloat()
        packet.z = z.toFloat()
        packet.data = (this.width and 0xff) + ((this.height and 0xff) shl 8)

        return arrayOf(packet)
    }
}
