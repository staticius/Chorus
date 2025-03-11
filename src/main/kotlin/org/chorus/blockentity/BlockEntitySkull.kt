package org.chorus.blockentity

import org.chorus.block.BlockHead
import org.chorus.level.format.IChunk
import org.chorus.math.ChorusMath
import org.chorus.nbt.tag.CompoundTag

/**
 * @author Snake1999
 * @since 2016/2/3
 */
class BlockEntitySkull(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    private var mouthMoving = false

    private var mouthTickCount = 0


    override fun loadNBT() {
        super.loadNBT()
        if (!namedTag.contains("SkullType")) {
            namedTag.putByte("SkullType", 0)
        }
        if (!namedTag.contains("Rot")) {
            namedTag.putByte("Rot", 0)
        }

        if (namedTag.containsByte("MouthMoving")) {
            mouthMoving = namedTag.getBoolean("MouthMoving")
        }

        if (namedTag.containsInt("MouthTickCount")) {
            mouthTickCount = ChorusMath.clamp(namedTag.getInt("MouthTickCount"), 0, 60)
        }
    }

    override fun onUpdate(): Boolean {
        if (isMouthMoving()) {
            mouthTickCount++
            setDirty()
            return true
        }
        return false
    }

    fun setMouthMoving(mouthMoving: Boolean) {
        if (this.mouthMoving == mouthMoving) {
            return
        }
        this.mouthMoving = mouthMoving
        if (mouthMoving) {
            scheduleUpdate()
        }
        level.updateComparatorOutputLevelSelective(this.position, true)
        spawnToAll()
        if (chunk != null) {
            setDirty()
        }
    }

    override val isObservable: Boolean
        get() = false

    override fun setDirty() {
        chunk!!.setChanged()
    }

    fun isMouthMoving(): Boolean {
        return mouthMoving
    }

    fun getMouthTickCount(): Int {
        return mouthTickCount
    }

    fun setMouthTickCount(mouthTickCount: Int) {
        if (this.mouthTickCount == mouthTickCount) {
            return
        }
        this.mouthTickCount = mouthTickCount
        spawnToAll()
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag
            .putBoolean("MouthMoving", this.mouthMoving)
            .putInt("MouthTickCount", mouthTickCount)
            .remove("Creator")
    }

    override val isBlockEntityValid: Boolean
        get() = block is BlockHead

    override val spawnCompound: CompoundTag
        get() = super.spawnCompound
            .put("SkullType", namedTag["SkullType"])
            .put("Rot", namedTag["Rot"])
            .putBoolean("MouthMoving", this.mouthMoving)
            .putInt("MouthTickCount", mouthTickCount)
}
