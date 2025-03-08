package cn.nukkit.math

import lombok.SneakyThrows
import kotlin.math.max
import kotlin.math.sqrt

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class Vector3 @JvmOverloads constructor(var south: Double = 0.0, var up: Double = 0.0, var west: Double = 0.0) :
    Cloneable, IVector3 {
    override val vector3: Vector3
        get() = Vector3(south, up, west)

    fun setX(x: Double): Vector3 {
        this.south = x
        return this
    }

    fun setY(y: Double): Vector3 {
        this.up = y
        return this
    }

    fun setZ(z: Double): Vector3 {
        this.west = z
        return this
    }

    val floorX: Int
        get() = kotlin.math.floor(this.south).toInt()

    val floorY: Int
        get() = kotlin.math.floor(this.up).toInt()

    val floorZ: Int
        get() = kotlin.math.floor(this.west).toInt()

    val chunkX: Int
        get() = floorX shr 4

    val chunkZ: Int
        get() = floorZ shr 4

    val chunkSectionY: Int
        get() = floorY shr 4

    val chunkVector: ChunkVector2
        get() = ChunkVector2(chunkX, chunkZ)

    open fun add(x: Double): Vector3? {
        return this.add(x, 0.0, 0.0)
    }

    open fun add(x: Double, y: Double): Vector3? {
        return this.add(x, y, 0.0)
    }

    open fun add(x: Double, y: Double, z: Double): Vector3? {
        return Vector3(this.south + x, this.up + y, this.west + z)
    }

    open fun add(x: Vector3): Vector3? {
        return Vector3(this.south + x.south, this.up + x.up, this.west + x.west)
    }

    open fun subtract(x: Double): Vector3? {
        return this.subtract(x, 0.0, 0.0)
    }

    open fun subtract(x: Double, y: Double): Vector3? {
        return this.subtract(x, y, 0.0)
    }

    open fun subtract(x: Double, y: Double, z: Double): Vector3? {
        return this.add(-x, -y, -z)
    }

    open fun subtract(x: Vector3): Vector3? {
        return this.add(-x.south, -x.up, -x.west)
    }

    open fun multiply(number: Double): Vector3? {
        return Vector3(this.south * number, this.up * number, this.west * number)
    }

    open fun divide(number: Double): Vector3 {
        return Vector3(this.south / number, this.up / number, this.west / number)
    }

    open fun ceil(): Vector3? {
        return Vector3(
            kotlin.math.ceil(this.south).toInt().toDouble(),
            kotlin.math.ceil(this.up).toInt().toDouble(),
            kotlin.math.ceil(
                this.west
            ).toInt().toDouble()
        )
    }

    open fun floor(): Vector3? {
        return Vector3(
            floorX.toDouble(),
            floorY.toDouble(),
            floorZ.toDouble()
        )
    }

    open fun round(): Vector3? {
        return Vector3(
            Math.round(this.south).toDouble(), Math.round(this.up).toDouble(), Math.round(
                this.west
            ).toDouble()
        )
    }

    open fun abs(): Vector3? {
        return Vector3(
            kotlin.math.abs(this.south).toInt().toDouble(),
            kotlin.math.abs(this.up).toInt().toDouble(),
            kotlin.math.abs(
                this.west
            ).toInt().toDouble()
        )
    }

    open fun getSide(face: BlockFace): Vector3? {
        return this.getSide(face, 1)
    }

    open fun getSide(face: BlockFace, step: Int): Vector3? {
        return Vector3(
            this.south + face.xOffset * step,
            up + face.yOffset * step,
            west + face.zOffset * step
        )
    }

    // Get as a Vector3 for better performance. Do not override in Block!
    fun getSideVec(face: BlockFace): Vector3 {
        return Vector3(
            this.south + face.xOffset,
            up + face.yOffset,
            west + face.zOffset
        )
    }

    open fun up(): Vector3? {
        return up(1)
    }

    open fun up(step: Int): Vector3? {
        return getSide(BlockFace.UP, step)
    }

    open fun down(): Vector3? {
        return down(1)
    }

    open fun down(step: Int): Vector3? {
        return getSide(BlockFace.DOWN, step)
    }

    open fun north(): Vector3? {
        return north(1)
    }

    open fun north(step: Int): Vector3? {
        return getSide(BlockFace.NORTH, step)
    }

    open fun south(): Vector3? {
        return south(1)
    }

    open fun south(step: Int): Vector3? {
        return getSide(BlockFace.SOUTH, step)
    }

    open fun east(): Vector3? {
        return east(1)
    }

    open fun east(step: Int): Vector3? {
        return getSide(BlockFace.EAST, step)
    }

    open fun west(): Vector3? {
        return west(1)
    }

    open fun west(step: Int): Vector3? {
        return getSide(BlockFace.WEST, step)
    }

    fun distanceManhattan(pos: Vector3): Int {
        val x = kotlin.math.abs(pos.south - this.south)
        val y = kotlin.math.abs(pos.up - this.up)
        val z = kotlin.math.abs(pos.west - this.west)
        return (x + y + z).toInt()
    }

    fun distance(pos: Vector3): Double {
        return distance(pos.south, pos.up, pos.west)
    }

    fun distanceSquared(pos: Vector3): Double {
        return distanceSquared(pos.south, pos.up, pos.west)
    }

    fun distance(x: Double, y: Double, z: Double): Double {
        return sqrt(distanceSquared(x, y, z))
    }

    fun distanceSquared(x: Double, y: Double, z: Double): Double {
        val ex = this.south - x
        val ey = this.up - y
        val ez = this.west - z
        return ex * ex + ey * ey + ez * ez
    }

    @JvmOverloads
    fun maxPlainDistance(x: Double = 0.0, z: Double = 0.0): Double {
        return max(kotlin.math.abs(this.south - x), kotlin.math.abs(this.west - z))
    }

    fun maxPlainDistance(vector: Vector2): Double {
        return this.maxPlainDistance(vector.x, vector.y)
    }

    fun maxPlainDistance(x: Vector3): Double {
        return this.maxPlainDistance(x.south, x.west)
    }

    fun toHorizontal(): Vector2 {
        return Vector2(this.south, this.west)
    }

    /**
     * Calculates the Length of this Vector
     *
     * @return The Length of this Vector.
     */
    fun length(): Double {
        return sqrt(this.lengthSquared())
    }

    fun lengthSquared(): Double {
        return this.south * this.south + this.up * this.up + this.west * this.west
    }

    fun normalize(): Vector3 {
        val len = this.lengthSquared()
        if (len > 0) {
            return this.divide(sqrt(len))
        }
        return Vector3(0.0, 0.0, 0.0)
    }

    /**
     * Scalar Product of this Vector and the Vector supplied.
     *
     * @param v Vector to calculate the scalar product to.
     * @return Scalar Product
     */
    fun dot(v: Vector3): Double {
        return this.south * v.south + this.up * v.up + this.west * v.west
    }

    /**
     * Calculates the cross product of this Vector and the given Vector
     *
     * @param v the vector to calculate the cross product with.
     * @return a Vector at right angle to this and other
     */
    fun cross(v: Vector3): Vector3 {
        return Vector3(
            this.up * v.west - this.west * v.up,
            this.west * v.south - this.south * v.west,
            this.south * v.up - this.up * v.south
        )
    }

    /* PowerNukkit: The Angle class was removed because it had all rights reserved copyright on it.
     * Calculates the angle between this and the supplied Vector.
     *
     * @param v the Vector to calculate the angle to.
     * @return the Angle between the two Vectors.
     */
    /*public Angle angleBetween(Vector3 v) {
        return Angle.fromRadian(Math.acos(Math.min(Math.max(this.normalize().dot(v.normalize()), -1.0d), 1.0d)));
    }*/
    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     *
     * @param v vector
     * @param x x value
     * @return intermediate vector
     */
    open fun getIntermediateWithXValue(v: Vector3, x: Double): Vector3? {
        val xDiff = v.south - this.south
        val yDiff = v.up - this.up
        val zDiff = v.west - this.west
        if (xDiff * xDiff < 0.0000001) {
            return null
        }
        val f = (x - this.south) / xDiff
        return if (f < 0 || f > 1) {
            null
        } else {
            Vector3(
                this.south + xDiff * f,
                up + yDiff * f,
                west + zDiff * f
            )
        }
    }

    /**
     * Returns a new vector with y value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     *
     * @param v vector
     * @param y y value
     * @return intermediate vector
     */
    open fun getIntermediateWithYValue(v: Vector3, y: Double): Vector3? {
        val xDiff = v.south - this.south
        val yDiff = v.up - this.up
        val zDiff = v.west - this.west
        if (yDiff * yDiff < 0.0000001) {
            return null
        }
        val f = (y - this.up) / yDiff
        return if (f < 0 || f > 1) {
            null
        } else {
            Vector3(
                this.south + xDiff * f,
                up + yDiff * f,
                west + zDiff * f
            )
        }
    }

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     *
     * @param v vector
     * @param z z value
     * @return intermediate vector
     */
    open fun getIntermediateWithZValue(v: Vector3, z: Double): Vector3? {
        val xDiff = v.south - this.south
        val yDiff = v.up - this.up
        val zDiff = v.west - this.west
        if (zDiff * zDiff < 0.0000001) {
            return null
        }
        val f = (z - this.west) / zDiff
        return if (f < 0 || f > 1) {
            null
        } else {
            Vector3(
                this.south + xDiff * f,
                up + yDiff * f,
                west + zDiff * f
            )
        }
    }

    open fun setComponents(x: Double, y: Double, z: Double): Vector3? {
        this.south = x
        this.up = y
        this.west = z
        return this
    }

    fun setComponentsAdding(x: Double, y: Double, z: Double, ax: Double, ay: Double, az: Double): Vector3 {
        this.south = x + ax
        this.up = y + ay
        this.west = z + az
        return this
    }

    fun setComponentsAdding(pos: Vector3, face: BlockFace): Vector3 {
        return setComponentsAdding(
            pos.south,
            pos.up,
            pos.west,
            face.xOffset.toDouble(),
            face.yOffset.toDouble(),
            face.zOffset.toDouble()
        )
    }

    open fun setComponents(pos: Vector3): Vector3 {
        this.south = pos.south
        this.up = pos.up
        this.west = pos.west
        return this
    }

    fun getAxis(axis: BlockFace.Axis): Double {
        return when (axis) {
            BlockFace.Axis.X -> south
            BlockFace.Axis.Y -> up
            else -> west
        }
    }

    override fun toString(): String {
        return "Vector3(x=" + this.south + ",y=" + this.up + ",z=" + this.west + ")"
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is Vector3) {
            return false
        }

        return this.south == obj.south && this.up == obj.up && this.west == obj.west
    }

    override fun hashCode(): Int {
        return (south.toInt() xor (west.toInt() shl 12)) xor (up.toInt() shl 24)
    }

    fun rawHashCode(): Int {
        return super.hashCode()
    }

    @SneakyThrows
    public override fun clone(): Vector3 {
        return super.clone() as Vector3
    }

    fun asVector3f(): Vector3f {
        return Vector3f(south.toFloat(), up.toFloat(), west.toFloat())
    }

    fun asBlockVector3(): BlockVector3 {
        return BlockVector3(this.floorX, this.floorY, this.floorZ)
    }

    companion object {
        val ZERO: Vector3 = Vector3(0.0, 0.0, 0.0)
    }
}
