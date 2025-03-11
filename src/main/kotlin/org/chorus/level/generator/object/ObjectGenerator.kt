package org.chorus.level.generator.`object`

import org.chorus.block.BlockID
import org.chorus.math.Vector3
import org.chorus.utils.random.RandomSourceProvider

abstract class ObjectGenerator : BlockID {
    abstract fun generate(level: BlockManager, rand: RandomSourceProvider, position: Vector3): Boolean
}
