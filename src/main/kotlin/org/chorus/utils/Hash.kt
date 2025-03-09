package org.chorus.utils

import cn.nukkit.math.Vector3

object Hash {
    fun hashBlock(x: Int, y: Int, z: Int): Long {
        return (y.toLong() shl 52) + ((z.toLong() and 0x3ffffffL) shl 26) + (x.toLong() and 0x3ffffffL)
        //return y + (((long) x & 0x3FFFFFF) << 8) + (((long) z & 0x3FFFFFF) << 34);
    }

    fun hashBlockX(triple: Long): Int {
        return (((triple and 0x3ffffffL) shl 38) shr 38).toInt()
        //return (int) ((((triple >> 8) & 0x3FFFFFF) << 38) >> 38);
    }

    fun hashBlockY(triple: Long): Int {
        return (triple shr 52).toInt()
        //return (int) (triple & 0xFF);
    }

    fun hashBlockZ(triple: Long): Int {
        return ((((triple shr 26) and 0x3ffffffL) shl 38) shr 38).toInt()
        //return (int) ((((triple >> 34) & 0x3FFFFFF) << 38) >> 38);
    }

    @JvmStatic
    fun hashBlock(blockPos: Vector3): Long {
        return hashBlock(blockPos.floorX, blockPos.floorY, blockPos.floorZ)
    }
}
