package org.chorus.level.particle

import org.chorus.math.Vector3
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.LevelEventPacket

class BlockForceFieldParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 0) :
    GenericParticle(pos, Particle.Companion.TYPE_BLOCK_FORCE_FIELD) {
    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DENY_BLOCK
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
