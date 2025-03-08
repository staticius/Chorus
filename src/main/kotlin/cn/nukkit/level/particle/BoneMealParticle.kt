package cn.nukkit.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.LevelEventPacket

/**
 * @author CreeperFace
 * @since 15.4.2017
 */
class BoneMealParticle(pos: Vector3) : Particle(pos.south, pos.up, pos.west) {
    override fun encode(): Array<DataPacket> {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_PARTICLE_CROP_GROWTH
        pk.x = south.toFloat()
        pk.y = up.toFloat()
        pk.z = west.toFloat()
        pk.data = 0

        return arrayOf(pk)
    }
}
