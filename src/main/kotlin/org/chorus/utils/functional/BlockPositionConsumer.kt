package org.chorus.utils.functional

import org.chorus.math.IntIncrementSupplier
import org.chorus.math.NukkitMath.floorFloat
import com.google.common.base.Preconditions

fun interface BlockPositionConsumer {
    fun accept(x: Int, y: Int, z: Int)


    companion object {
        fun validate(
            fromX: Int,
            fromY: Int,
            fromZ: Int,
            toX: Int,
            toY: Int,
            toZ: Int,
            xInc: Int,
            yInc: Int,
            zInc: Int
        ) {
            if (fromX <= toX) {
                Preconditions.checkArgument(xInc > 0, "Invalid xInc")
            } else {
                Preconditions.checkArgument(xInc < 0, "Invalid xInc")
            }

            if (fromY <= toY) {
                Preconditions.checkArgument(yInc > 0, "Invalid yInc")
            } else {
                Preconditions.checkArgument(yInc < 0, "Invalid yInc")
            }

            if (fromZ <= toZ) {
                Preconditions.checkArgument(zInc > 0, "Invalid zInc")
            } else {
                Preconditions.checkArgument(zInc < 0, "Invalid zInc")
            }
        }

        fun xzy(
            fromX: Int,
            fromY: Int,
            fromZ: Int,
            toX: Int,
            toY: Int,
            toZ: Int,
            xInc: Int,
            yInc: Int,
            zInc: Int,
            iterator: BlockPositionConsumer
        ) {
            validate(fromX, fromY, fromZ, toX, toY, toZ, xInc, yInc, zInc)

            val xStream =
                IntIncrementSupplier(fromX, xInc).stream().limit(floorFloat((toX - fromX) / xInc.toFloat()).toLong())
            val yStream =
                IntIncrementSupplier(fromY, yInc).stream().limit(floorFloat((toY - fromY) / yInc.toFloat()).toLong())
            val zStream =
                IntIncrementSupplier(fromZ, zInc).stream().limit(floorFloat((toZ - fromZ) / zInc.toFloat()).toLong())

            xStream.forEachOrdered { x: Int ->
                zStream.forEachOrdered { z: Int ->
                    yStream.forEachOrdered { y: Int ->
                        iterator.accept(
                            x,
                            y,
                            z
                        )
                    }
                }
            }
        }

        fun xzy(fromX: Int, fromY: Int, fromZ: Int, toX: Int, toY: Int, toZ: Int, iterator: BlockPositionConsumer) {
            xzy(
                fromX,
                fromY,
                fromZ,
                toX,
                toY,
                toZ,
                if (fromX <= toX) 1 else -1,
                if (fromY <= toY) 1 else -1,
                if (fromZ <= toZ) 1 else -1,
                iterator
            )
        }

        fun xzy(toX: Int, toY: Int, toZ: Int, iterator: BlockPositionConsumer) {
            xzy(0, 0, 0, toX, toY, toZ, iterator)
        }
    }
}
