package cn.nukkit.math

import lombok.SneakyThrows
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class Vector3f @JvmOverloads constructor(var south: Float = 0f, var up: Float = 0f, var west: Float = 0f) :
    Cloneable {
    fun setX(x: Float): Vector3f {
        this.south = x
        return this
    }

    fun setY(y: Float): Vector3f {
        this.up = y
        return this
    }

    fun setZ(z: Float): Vector3f {
        this.west = z
        return this
    }

    val floorX: Int
        get() = NukkitMath.floorFloat(this.south)

    val floorY: Int
        get() = NukkitMath.floorFloat(this.up)

    val floorZ: Int
        get() = NukkitMath.floorFloat(this.west)

    fun add(x: Float): Vector3f {
        return this.add(x, 0f, 0f)
    }

    fun add(x: Float, y: Float): Vector3f {
        return this.add(x, y, 0f)
    }

    fun add(x: Float, y: Float, z: Float): Vector3f {
        return Vector3f(this.south + x, this.up + y, this.west + z)
    }

    fun add(x: Vector3f): Vector3f {
        return Vector3f(this.south + x.south, this.up + x.up, this.west + x.west)
    }

    @JvmOverloads
    fun subtract(x: Float = 0f, y: Float = 0f, z: Float = 0f): Vector3f {
        return this.add(-x, -y, -z)
    }

    fun subtract(x: Vector3f): Vector3f {
        return this.add(-x.south, -x.up, -x.west)
    }

    fun multiply(number: Float): Vector3f {
        return Vector3f(this.south * number, this.up * number, this.west * number)
    }

    fun divide(number: Float): Vector3f {
        return Vector3f(this.south / number, this.up / number, this.west / number)
    }

    fun ceil(): Vector3f {
        return Vector3f(
            kotlin.math.ceil(south.toDouble()).toInt().toFloat(), kotlin.math.ceil(
                up.toDouble()
            ).toInt().toFloat(), kotlin.math.ceil(west.toDouble()).toInt().toFloat()
        )
    }

    fun floor(): Vector3f {
        return Vector3f(
            floorX.toFloat(),
            floorY.toFloat(),
            floorZ.toFloat()
        )
    }

    fun round(): Vector3f {
        return Vector3f(
            Math.round(this.south).toFloat(), Math.round(this.up).toFloat(), Math.round(
                this.west
            ).toFloat()
        )
    }

    fun abs(): Vector3f {
        return Vector3f(
            kotlin.math.abs(south.toDouble()).toInt().toFloat(), kotlin.math.abs(
                up.toDouble()
            ).toInt().toFloat(), kotlin.math.abs(west.toDouble()).toInt().toFloat()
        )
    }

    fun getSide(side: Int): Vector3f {
        return this.getSide(side, 1)
    }

    fun getSide(side: Int, step: Int): Vector3f {
        return when (side) {
            SIDE_DOWN -> Vector3f(
                this.south,
                up - step,
                west
            )

            SIDE_UP -> Vector3f(
                this.south,
                up + step,
                west
            )

            SIDE_NORTH -> Vector3f(
                this.south,
                up,
                west - step
            )

            SIDE_SOUTH -> Vector3f(
                this.south,
                up,
                west + step
            )

            SIDE_WEST -> Vector3f(
                this.south - step,
                up,
                west
            )

            SIDE_EAST -> Vector3f(
                this.south + step,
                up,
                west
            )

            else -> this
        }
    }

    fun distance(pos: Vector3f): Double {
        return sqrt(this.distanceSquared(pos))
    }

    fun distanceSquared(pos: Vector3f): Double {
        return (this.south - pos.south).toDouble().pow(2.0) + (this.up - pos.up).toDouble()
            .pow(2.0) + (this.west - pos.west).toDouble().pow(2.0)
    }

    @JvmOverloads
    fun maxPlainDistance(x: Float = 0f, z: Float = 0f): Float {
        return max(kotlin.math.abs((this.south - x).toDouble()), kotlin.math.abs((this.west - z).toDouble())).toFloat()
    }

    fun maxPlainDistance(vector: Vector2f): Float {
        return this.maxPlainDistance(vector.x, vector.y)
    }

    fun toHorizontal(): Vector2f {
        return Vector2f(this.south, this.west)
    }

    fun maxPlainDistance(x: Vector3f): Float {
        return this.maxPlainDistance(x.south, x.west)
    }

    /**
     * Calculates the Length of this Vector
     *
     * @return The Length of this Vector.
     */
    fun length(): Double {
        return sqrt(lengthSquared().toDouble())
    }

    fun lengthSquared(): Float {
        return this.south * this.south + this.up * this.up + this.west * this.west
    }

    fun normalize(): Vector3f {
        val len = this.lengthSquared()
        if (len > 0) {
            return this.divide(sqrt(len.toDouble()).toFloat())
        }
        return Vector3f(0f, 0f, 0f)
    }

    /**
     * Scalar Product of this Vector and the Vector supplied.
     *
     * @param v Vector to calculate the scalar product to.
     * @return Scalar Product
     */
    fun dot(v: Vector3f): Float {
        return this.south * v.south + this.up * v.up + this.west * v.west
    }

    /**
     * Calculates the cross product of this Vector and the given Vector
     *
     * @param v the vector to calculate the cross product with.
     * @return a Vector at right angle to this and other
     */
    fun cross(v: Vector3f): Vector3f {
        return Vector3f(
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
    /*public Angle angleBetween(Vector3f v) {
        return Angle.fromRadian(Math.acos(Math.min(Math.max(this.normalize().dot(v.normalize()), -1.0f), 1.0f)));
    }*/
    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     *
     * @param v vector
     * @param x x value
     * @return intermediate vector
     */
    fun getIntermediateWithXValue(v: Vector3f, x: Float): Vector3f? {
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
            Vector3f(
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
    fun getIntermediateWithYValue(v: Vector3f, y: Float): Vector3f? {
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
            Vector3f(
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
    fun getIntermediateWithZValue(v: Vector3f, z: Float): Vector3f? {
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
            Vector3f(
                this.south + xDiff * f,
                up + yDiff * f,
                west + zDiff * f
            )
        }
    }

    fun setComponents(x: Float, y: Float, z: Float): Vector3f {
        this.south = x
        this.up = y
        this.west = z
        return this
    }

    fun getAxis(axis: BlockFace.Axis): Float {
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
        if (obj !is Vector3f) {
            return false
        }

        return this.south == obj.south && this.up == obj.up && this.west == obj.west
    }

    fun rawHashCode(): Int {
        return super.hashCode()
    }

    @SneakyThrows
    public override fun clone(): Vector3f {
        return super.clone() as Vector3f
    }

    fun asVector3(): Vector3 {
        return Vector3(south.toDouble(), up.toDouble(), west.toDouble())
    }

    fun asBlockVector3(): BlockVector3 {
        return BlockVector3(floorX, floorY, floorZ)
    }

    companion object {
        const val SIDE_DOWN: Int = 0
        const val SIDE_UP: Int = 1
        const val SIDE_NORTH: Int = 2
        const val SIDE_SOUTH: Int = 3
        const val SIDE_WEST: Int = 4
        const val SIDE_EAST: Int = 5

        fun getOppositeSide(side: Int): Int {
            return when (side) {
                SIDE_DOWN -> SIDE_UP
                SIDE_UP -> SIDE_DOWN
                SIDE_NORTH -> SIDE_SOUTH
                SIDE_SOUTH -> SIDE_NORTH
                SIDE_WEST -> SIDE_EAST
                SIDE_EAST -> SIDE_WEST
                else -> -1
            }
        }
    }
}
