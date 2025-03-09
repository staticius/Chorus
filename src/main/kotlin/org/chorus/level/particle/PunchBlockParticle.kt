package org.chorus.level.particle

import org.chorus.block.Block
import org.chorus.math.Vector3
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.LevelEventPacket

class PunchBlockParticle(pos: Vector3, block: Block) :
    Particle(pos.x, pos.y, pos.z) {
    protected val data: Int

    init {
        this.data = block.blockState.blockStateHash()
    }

    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_CRACK_BLOCK
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
