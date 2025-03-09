package org.chorus.block

import org.chorus.math.BlockFace
import org.chorus.math.Vector3.add

interface BlockConnectable {
    fun getSideAtLayer(layer: Int, face: BlockFace?): Block?

    fun canConnect(block: Block?): Boolean

    val isStraight: Boolean
        get() {
            val connections = connections
            if (connections.size != 2) {
                return false
            }

            val iterator = connections.iterator()
            val a = iterator.next()
            val b = iterator.next()
            return a.getOpposite() == b
        }

    val connections: Set<BlockFace>
        get() {
            val connections: EnumSet<BlockFace> = EnumSet.noneOf<BlockFace>(BlockFace::class.java)
            for (blockFace in BlockFace.Plane.HORIZONTAL) {
                if (isConnected(blockFace)) {
                    connections.add(blockFace)
                }
            }
            return connections
        }

    fun isConnected(face: BlockFace?): Boolean {
        return canConnect(getSideAtLayer(0, face))
    }
}
