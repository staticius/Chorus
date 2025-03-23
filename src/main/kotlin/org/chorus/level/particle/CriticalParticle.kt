package org.chorus.level.particle

import org.chorus.math.Vector3

class CriticalParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 2) : GenericParticle(pos, TYPE_CRIT, scale)
