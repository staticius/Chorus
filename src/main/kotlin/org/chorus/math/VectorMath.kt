package org.chorus.math

import org.chorus.math.BlockFace.AxisDirection
import java.util.*
import java.util.stream.Collectors
import kotlin.math.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
object VectorMath {
    fun getDirection2D(azimuth: Double): Vector2 {
        return Vector2(cos(azimuth), sin(azimuth))
    }

    @JvmStatic
    fun calculateAxis(base: Vector3, side: Vector3): BlockFace.Axis {
        val vector = side.subtract(base)
        return if (vector.x != 0.0) BlockFace.Axis.X else if (vector.z != 0.0) BlockFace.Axis.Z else BlockFace.Axis.Y
    }

    @JvmStatic
    fun calculateFace(base: Vector3, side: Vector3): BlockFace {
        val vector = side.subtract(base)
        val axis =
            if (vector.x != 0.0) BlockFace.Axis.X else if (vector.z != 0.0) BlockFace.Axis.Z else BlockFace.Axis.Y
        val direction = vector!!.getAxis(axis)
        return BlockFace.Companion.fromAxis(if (direction < 0) AxisDirection.NEGATIVE else AxisDirection.POSITIVE, axis)
    }

    fun getPassByVector3(from: Vector3, to: Vector3): List<Vector3?> {
        require(from != to) { "from == to" }

        val xCuts = LinkedList<FixedVector3>()
        var lastXCut = if (from.x < to.x) from else to
        val targetXCut = if (from.x > to.x) from else to
        if (from.x != to.x) {
            for (xCut in NukkitMath.ceilDouble(min(from.x, to.x))..<NukkitMath.floorDouble(max(from.x, to.x)) + 1) {
                val ratio = (xCut - from.x) / (to.x - from.x)
                val currentXCut =
                    Vector3(xCut.toDouble(), from.y + (to.y - from.y) * ratio, from.z + (to.z - from.z) * ratio)
                if (xCut.toDouble() != lastXCut.x) {
                    xCuts.add(FixedVector3(lastXCut, currentXCut))
                }
                lastXCut = currentXCut
                if (xCut + 1 > NukkitMath.floorDouble(max(from.x, to.x))) {
                    xCuts.add(FixedVector3(lastXCut, targetXCut))
                }
            }
        }

        if (xCuts.isEmpty()) xCuts.add(FixedVector3(from, to))

        val zCuts = LinkedList<FixedVector3>()
        if (from.z != to.z) {
            for (xCut in xCuts) {
                var lastZCut = if (xCut.from.z < xCut.to.z) xCut.from else xCut.to
                val targetZCut = if (xCut.from.z > xCut.to.z) xCut.from else xCut.to
                val oldSize = zCuts.size
                for (zCut in NukkitMath.ceilDouble(
                    min(
                        xCut.from.z,
                        xCut.to.z
                    )
                )..<NukkitMath.floorDouble(max(xCut.from.z, xCut.to.z)) + 1) {
                    val ratio = (zCut - xCut.from.z) / (xCut.to.z - xCut.from.z)
                    val currentZCut = Vector3(
                        xCut.from.x + (xCut.to.x - xCut.from.x) * ratio,
                        xCut.from.y + (xCut.to.y - xCut.from.y) * ratio,
                        zCut.toDouble()
                    )
                    if (zCut.toDouble() != lastZCut.z) {
                        zCuts.add(FixedVector3(lastZCut, currentZCut))
                    }
                    lastZCut = currentZCut
                    if (zCut + 1 > NukkitMath.floorDouble(max(xCut.from.z, xCut.to.z))) {
                        zCuts.add(FixedVector3(lastZCut, targetZCut))
                    }
                }
                if (oldSize == zCuts.size) zCuts.add(xCut)
            }
        }

        var yCuts = LinkedList<FixedVector3>()
        if (from.y != to.y) {
            for (zCut in zCuts) {
                var lastYCut = if (zCut.from.y < zCut.to.y) zCut.from else zCut.to
                val targetYCut = if (zCut.from.y > zCut.to.y) zCut.from else zCut.to
                val oldSize = yCuts.size
                for (yCut in NukkitMath.ceilDouble(
                    min(
                        zCut.from.y,
                        zCut.to.y
                    )
                )..<NukkitMath.floorDouble(max(zCut.from.y, zCut.to.y)) + 1) {
                    val ratio = (yCut - zCut.from.y) / (zCut.to.y - zCut.from.y)
                    val currentYCut = Vector3(
                        zCut.from.x + (zCut.to.x - zCut.from.x) * ratio,
                        yCut.toDouble(),
                        zCut.from.z + (zCut.to.z - zCut.from.z) * ratio
                    )
                    if (yCut.toDouble() != lastYCut.y) {
                        yCuts.add(FixedVector3(lastYCut, currentYCut))
                    }
                    lastYCut = currentYCut
                    if (yCut + 1 > NukkitMath.floorDouble(max(zCut.from.y, zCut.to.y))) {
                        yCuts.add(FixedVector3(lastYCut, targetYCut))
                    }
                }
                if (oldSize == yCuts.size) yCuts.add(zCut)
            }
        } else {
            yCuts = zCuts
        }

        return yCuts
            .stream()
            .map { yCut: FixedVector3 ->
                Vector3(
                    (yCut.from.x + yCut.to.x) * 0.5,
                    (yCut.from.y + yCut.to.y) * 0.5,
                    (yCut.from.z + yCut.to.z) * 0.5
                ).floor()
            }
            .collect(Collectors.toList())
    }

    @JvmRecord
    internal data class FixedVector3(val from: Vector3, val to: Vector3) {
        override fun toString(): String {
            return from.x.toString() + " " + from.y + " " + from.z + " -> " + to.x + " " + to.y + " " + to.z
        }
    }
}
