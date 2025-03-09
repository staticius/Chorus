package cn.nukkit.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author joserobjr
 * @since 2021-06-14
 */
class WaxOffParticle(pos: Vector3) : GenericParticle(pos, Particle.Companion.TYPE_WAX) {
    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_WAX_OFF
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
