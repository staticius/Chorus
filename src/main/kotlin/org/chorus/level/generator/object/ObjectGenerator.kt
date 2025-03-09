package org.chorus.level.generator.`object`

import cn.nukkit.block.BlockID
import cn.nukkit.math.Vector3

abstract class ObjectGenerator : BlockID {
    abstract fun generate(level: BlockManager, rand: RandomSourceProvider, position: Vector3): Boolean
}
