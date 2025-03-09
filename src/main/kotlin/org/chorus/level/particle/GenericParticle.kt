package org.chorus.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author xtypr
 * @since 2015/11/21
 */
open class GenericParticle @JvmOverloads constructor(pos: Vector3, id: Int, data: Int = 0) :
    Particle(pos.x, pos.y, pos.z) {
    protected val data: Int
    protected var id: Int = 0

    init {
        this.id = id
        this.data = data
    }

    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = (LevelEventPacket.EVENT_ADD_PARTICLE_MASK or this.id).toShort().toInt()
        pk.x = x.toFloat()
        pk.y = y.toFloat()
        pk.z = z.toFloat()
        pk.data = this.data

        return arrayOf(pk)
    }
}
