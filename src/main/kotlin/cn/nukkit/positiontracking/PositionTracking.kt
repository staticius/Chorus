package cn.nukkit.positiontracking

import cn.nukkit.level.Level
import cn.nukkit.level.Locator
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3

/**
 * @author joserobjr
 */
class PositionTracking(override var levelName: String, x: Double, y: Double, z: Double) :
    Vector3(x, y, z), NamedPosition {
    constructor(level: Level, x: Double, y: Double, z: Double) : this(level.getName()!!, x, y, z)

    constructor(level: Level, v: Vector3) : this(level, v.south, v.up, v.west)

    constructor(levelName: String, v: Vector3) : this(levelName, v.south, v.up, v.west)

    constructor(pos: Locator) : this(pos.level, pos.position.south, pos.position.up, pos.position.west)

    constructor(pos: NamedPosition) : this(pos.levelName, pos.x, pos.y, pos.z)

    override fun add(x: Double): PositionTracking {
        return add(x, 0.0, 0.0)!!
    }

    override fun add(x: Double, y: Double): PositionTracking? {
        return add(x, y, 0.0)
    }

    override fun add(x: Double, y: Double, z: Double): PositionTracking? {
        return PositionTracking(levelName, this.south + x, this.up + y, this.west + z)
    }

    override fun add(v: Vector3): PositionTracking? {
        return PositionTracking(levelName, south + v.south, up + v.up, west + v.west)
    }

    override fun subtract(x: Double): PositionTracking? {
        return subtract(x, 0.0, 0.0)
    }

    override fun subtract(x: Double, y: Double): PositionTracking? {
        return subtract(x, y, 0.0)
    }

    override fun subtract(x: Double, y: Double, z: Double): PositionTracking? {
        return add(-x, -y, -z)
    }

    override fun subtract(v: Vector3): PositionTracking? {
        return add(-v.south, -v.up, -v.west)
    }

    override fun multiply(number: Double): PositionTracking? {
        return PositionTracking(levelName, south * number, up * number, west * number)
    }

    override fun divide(number: Double): PositionTracking {
        return PositionTracking(levelName, south * number, up * number, west * number)
    }

    override fun ceil(): PositionTracking? {
        return PositionTracking(levelName, kotlin.math.ceil(south), kotlin.math.ceil(up), kotlin.math.ceil(west))
    }

    override fun floor(): PositionTracking? {
        return PositionTracking(levelName, kotlin.math.floor(south), kotlin.math.floor(up), kotlin.math.floor(west))
    }

    override fun round(): PositionTracking? {
        return PositionTracking(
            levelName, Math.round(this.south).toDouble(), Math.round(this.up).toDouble(), Math.round(
                this.west
            ).toDouble()
        )
    }

    override fun abs(): PositionTracking? {
        return PositionTracking(
            levelName,
            kotlin.math.abs(this.south),
            kotlin.math.abs(this.up),
            kotlin.math.abs(this.west)
        )
    }

    override fun getSide(face: BlockFace): PositionTracking? {
        return getSide(face, 1)
    }

    override fun getSide(face: BlockFace, step: Int): PositionTracking? {
        return PositionTracking(
            levelName,
            south + face.xOffset * step,
            up + face.yOffset * step,
            west + face.zOffset * step
        )
    }

    override fun up(): PositionTracking? {
        return up(1)
    }

    override fun up(step: Int): PositionTracking? {
        return getSide(BlockFace.UP, step)
    }

    override fun down(): PositionTracking? {
        return down(1)
    }

    override fun down(step: Int): PositionTracking? {
        return getSide(BlockFace.DOWN, step)
    }

    override fun north(): PositionTracking? {
        return north(1)
    }

    override fun north(step: Int): PositionTracking? {
        return getSide(BlockFace.NORTH, step)
    }

    override fun south(): PositionTracking? {
        return south(1)
    }

    override fun south(step: Int): PositionTracking? {
        return getSide(BlockFace.SOUTH, step)
    }

    override fun east(): PositionTracking? {
        return east(1)
    }

    override fun east(step: Int): PositionTracking? {
        return getSide(BlockFace.EAST, step)
    }

    override fun west(): PositionTracking? {
        return west(1)
    }

    override fun west(step: Int): PositionTracking? {
        return getSide(BlockFace.WEST, step)
    }

    override fun getIntermediateWithXValue(v: Vector3, x: Double): PositionTracking? {
        val intermediateWithXValue = super.getIntermediateWithXValue(v, x) ?: return null
        return PositionTracking(levelName, intermediateWithXValue)
    }

    override fun getIntermediateWithYValue(v: Vector3, y: Double): Vector3? {
        val intermediateWithYValue = super.getIntermediateWithYValue(v, y) ?: return null
        return PositionTracking(levelName, intermediateWithYValue)
    }

    override fun getIntermediateWithZValue(v: Vector3, z: Double): Vector3? {
        val intermediateWithZValue = super.getIntermediateWithZValue(v, z) ?: return null
        return PositionTracking(levelName, intermediateWithZValue)
    }

    override fun setComponents(x: Double, y: Double, z: Double): PositionTracking? {
        super.setComponents(x, y, z)
        return this
    }

    override fun setComponents(position: Vector3): PositionTracking {
        super.setComponents(position)
        return this
    }

    override fun clone(): PositionTracking {
        return super.clone() as PositionTracking
    }
}
