package cn.nukkit.level.particle

import cn.nukkit.block.Block
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author xtypr
 * @since 2015/11/21
 */
class DestroyBlockParticle(pos: Vector3, block: Block) :
    Particle(pos.south, pos.up, pos.west) {
    protected val data: Int

    init {
        this.data = block.runtimeId
    }

    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY_BLOCK
        pk.x = south.toFloat()
        pk.y = up.toFloat()
        pk.z = west.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
