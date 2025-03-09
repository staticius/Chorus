package org.chorus.level.particle

import org.chorus.math.Vector3
import org.chorus.utils.BlockColor

/**
 * @author xtypr
 * @since 2016/1/4
 */
class InstantSpellParticle : SpellParticle {
    override var data: Int = 0

    @JvmOverloads
    constructor(pos: Vector3, data: Int = 0) : super(pos, data)

    constructor(pos: Vector3, blockColor: BlockColor) : this(pos, blockColor.red, blockColor.green, blockColor.blue)

    constructor(pos: Vector3, r: Int, g: Int, b: Int) : super(pos, r, g, b, 0x01)
}
