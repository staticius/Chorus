package cn.nukkit.blockentity

import cn.nukkit.block.BlockRedstoneComparator
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author CreeperFace
 */
class BlockEntityComparator(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    @JvmField
    var outputSignal: Int = 0

    override fun loadNBT() {
        super.loadNBT()
        if (!namedTag.contains("OutputSignal")) {
            namedTag.putInt("OutputSignal", 0)
        }

        this.outputSignal = namedTag.getInt("OutputSignal")
    }

    override val isBlockEntityValid: Boolean
        get() = this.levelBlock is BlockRedstoneComparator

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putInt("OutputSignal", this.outputSignal)
    }
}
