package cn.nukkit.level.particle

import cn.nukkit.block.Block
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

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
