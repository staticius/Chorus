package org.chorus.level.particle

import cn.nukkit.block.*
import cn.nukkit.math.Vector3

/**
 * @author xtypr
 * @since 2015/11/21
 */
class TerrainParticle(pos: Vector3, block: Block) :
    GenericParticle(pos, Particle.Companion.TYPE_TERRAIN, block.runtimeId)
