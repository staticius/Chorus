package org.chorus.entity.ai.sensor

import org.chorus.block.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import lombok.Getter

//存储最近的玩家的Memory
@Getter
class MemorizedBlockSensor @JvmOverloads constructor(
    protected var range: Int,
    protected var lookY: Int,
    override var period: Int = 1
) :
    ISensor {
    override fun sense(entity: EntityMob) {
        val blockClass =
            entity.memoryStorage!!.get<Class<out Block>>(CoreMemoryTypes.Companion.LOOKING_BLOCK)
                ?: return
        var block: Block? = null
        for (x in -range..range) {
            for (z in -range..range) {
                for (y in -lookY..lookY) {
                    val lookTransform = entity.transform.add(x.toDouble(), y.toDouble(), z.toDouble())
                    val lookBlock = lookTransform.levelBlock
                    if (lookBlock.id == Block.DIRT || lookBlock.id == Block.GRASS_BLOCK || lookBlock.isAir || lookBlock.id == Block.BEDROCK) continue
                    if (blockClass.isAssignableFrom(lookBlock.javaClass)) {
                        block = lookBlock
                        break
                    }
                }
            }
        }
        entity.memoryStorage!!.put<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK, block)
    }
}
