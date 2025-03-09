package org.chorus.level.particle

import cn.nukkit.math.Vector3

/**
 * @author xtypr
 * @since 2015/11/21
 */
class CriticalParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 2) :
    GenericParticle(pos, Particle.Companion.TYPE_CRIT, scale)
