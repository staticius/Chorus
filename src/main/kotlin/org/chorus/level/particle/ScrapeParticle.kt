package org.chorus.level.particle

import org.chorus.math.Vector3
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.LevelEventPacket

/**
 * @author joserobjr
 * @since 2021-06-15
 */
class ScrapeParticle(pos: Vector3) : GenericParticle(pos, Particle.Companion.TYPE_WAX) {
    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_SCRAPE
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
