package org.chorus.level

import org.chorus.math.Vector3
import kotlin.math.floor

class ChunkPosition(val x: Int, val y: Int, val z: Int) {
    constructor(vec3d: Vector3) : this(floor(vec3d.x).toInt(), floor(vec3d.y).toInt(), floor(vec3d.z).toInt())

    override fun equals(`object`: Any?): Boolean {
        return if (`object` !is ChunkPosition) {
            false
        } else {
            `object`.x == this.x && `object`.y == this.y && `object`.z == this.z
        }
    }

    override fun hashCode(): Int {
        return this.x * 8976890 + this.y * 981131 + this.z
    }
}
