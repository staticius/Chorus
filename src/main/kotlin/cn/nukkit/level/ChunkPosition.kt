package cn.nukkit.level

import cn.nukkit.math.MathHelper
import cn.nukkit.math.Vector3

/**
 * @author Adam Matthew (Nukkit Project)
 */
class ChunkPosition(val x: Int, val y: Int, val z: Int) {
    constructor(vec3d: Vector3) : this(MathHelper.floor(vec3d.x), MathHelper.floor(vec3d.y), MathHelper.floor(vec3d.z))

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
