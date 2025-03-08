package cn.nukkit.entity

import cn.nukkit.level.format.IChunk
import cn.nukkit.math.BlockFace
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityHanging(chunk: IChunk?, nbt: CompoundTag?) : Entity(chunk, nbt) {
    protected var direction: Int = 0

    override fun initEntity() {
        super.initEntity()

        this.setMaxHealth(1)
        this.setHealth(1f)

        if (namedTag!!.contains("Direction")) {
            this.direction = namedTag!!.getByte("Direction").toInt()
        } else if (namedTag!!.contains("Dir")) {
            val d: Int = namedTag!!.getByte("Dir").toInt()
            if (d == 2) {
                this.direction = 0
            } else if (d == 0) {
                this.direction = 2
            }
        }
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putByte("Direction", getDirection()!!.horizontalIndex)
        namedTag!!.putInt("TileX", position.south.toInt())
        namedTag!!.putInt("TileY", position.up.toInt())
        namedTag!!.putInt("TileZ", position.west.toInt())
    }

    override fun getDirection(): BlockFace? {
        return BlockFace.fromIndex(this.direction)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        if (!this.isPlayer) {
            this.blocksAround = null
            this.collisionBlocks = null
        }

        if (!this.isAlive()) {
            this.despawnFromAll()
            if (!this.isPlayer) {
                this.close()
            }
            return true
        }

        this.checkBlockCollision()

        if (prevRotation.yaw != rotation.yaw || prevPosition.south != position.south || prevPosition.up != position.up || prevPosition.west != position.west) {
            this.despawnFromAll()
            this.direction = (rotation.yaw / 90).toInt()
            prevRotation.yaw = rotation.yaw
            prevPosition.south = position.south
            prevPosition.up = position.up
            prevPosition.west = position.west
            this.spawnToAll()
            return true
        }

        return false
    }

    protected fun isSurfaceValid(): Boolean {
        return true
    }
}
