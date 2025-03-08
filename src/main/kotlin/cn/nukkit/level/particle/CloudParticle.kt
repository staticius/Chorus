package cn.nukkit.level.particle

import cn.nukkit.math.Vector3

class CloudParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 0) :
    GenericParticle(pos, Particle.Companion.TYPE_EVAPORATION, scale)
