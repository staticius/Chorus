package org.chorus.level.particle

import cn.nukkit.math.Vector3
import cn.nukkit.utils.BlockColor

/**
 * @author xtypr
 * @since 2015/11/21
 */
class DustParticle @JvmOverloads constructor(pos: Vector3, r: Int, g: Int, b: Int, a: Int = 255) :
    GenericParticle(
        pos,
        Particle.Companion.TYPE_FALLING_DUST,
        ((a and 0xff) shl 24) or ((r and 0xff) shl 16) or ((g and 0xff) shl 8) or (b and 0xff)
    ) {
    constructor(pos: Vector3, blockColor: BlockColor) : this(
        pos,
        blockColor.red,
        blockColor.green,
        blockColor.blue,
        blockColor.alpha
    )
}
