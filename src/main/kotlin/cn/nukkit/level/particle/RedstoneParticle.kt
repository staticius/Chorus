package cn.nukkit.level.particle

import cn.nukkit.math.Vector3

/**
 * @author xtypr
 * @since 2015/11/21
 */
class RedstoneParticle @JvmOverloads constructor(pos: Vector3, lifetime: Int = 1) :
    GenericParticle(pos, Particle.Companion.TYPE_RED_DUST, lifetime)
