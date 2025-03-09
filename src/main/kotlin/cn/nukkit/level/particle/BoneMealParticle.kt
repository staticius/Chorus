package cn.nukkit.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author CreeperFace
 * @since 15.4.2017
 */
class BoneMealParticle(pos: Vector3) : Particle(pos.x, pos.y, pos.z) {
    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_CROP_GROWTH
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = 0

        return arrayOf(pk)
    }
}
