package org.chorus.level.particle

import org.chorus.math.Vector3
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.LevelEventPacket

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
