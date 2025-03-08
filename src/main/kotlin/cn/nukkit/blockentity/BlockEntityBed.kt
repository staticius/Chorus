package cn.nukkit.blockentity

import cn.nukkit.block.BlockID
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.DyeColor

/**
 * @author CreeperFace
 * @since 2.6.2017
 */
class BlockEntityBed(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    var color: Int = 0

    override fun loadNBT() {
        super.loadNBT()
        if (!namedTag.contains("color")) {
            namedTag.putByte("color", 0)
        }

        this.color = namedTag.getByte("color").toInt()
    }

    override val isBlockEntityValid: Boolean
        get() = level.getBlockIdAt(
            position.floorX,
            position.floorY,
            position.floorZ
        ) === BlockID.BED

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putByte("color", this.color)
    }

    override val spawnCompound: CompoundTag
        get() = super.getSpawnCompound().putByte("color", this.color)

    val dyeColor: DyeColor
        get() = DyeColor.getByWoolData(color)
}
