package org.chorus.level.particle

import org.chorus.block.Block
import org.chorus.math.Vector3

class TerrainParticle(pos: Vector3, block: Block) : GenericParticle(pos, TYPE_TERRAIN, block.runtimeId)
