package cn.nukkit.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

class BlockForceFieldParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 0) :
    GenericParticle(pos, Particle.Companion.TYPE_BLOCK_FORCE_FIELD) {
    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DENY_BLOCK
        pk.x = south.toFloat()
        pk.y = up.toFloat()
        pk.z = west.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
