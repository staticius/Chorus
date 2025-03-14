package org.chorus.level.particle

import org.chorus.math.Vector3

/**
 * @author xtypr
 * @since 2015/11/21
 */
class InkParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 0) :
    GenericParticle(pos, TYPE_INK, scale)
