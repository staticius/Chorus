package org.chorus.entity.ai.route.data

import org.chorus.math.*
import lombok.Getter
import lombok.Setter
import java.util.*

/**
 * 寻路节点
 */
@Getter
@Setter
class Node(private val vector3: Vector3?, private val parent: Node?, private val G: Int, private val H: Int) :
    Comparable<Node?> {
    private val F = G + H

    override fun compareTo(o: Node): Int {
        Objects.requireNonNull(o)
        if (this.getF() != o.getF()) {
            return this.getF() - o.getF()
        }
        var breaking: Double
        return if (((this.getG() + (this.getH() * 0.1) - (o.getG() + (this.getH() * 0.1))).also {
                breaking = it
            }) > 0) {
            1
        } else if (breaking < 0) {
            -1
        } else {
            0
        }
    }

    override fun toString(): String {
        return vector3.toString() + "| G:" + this.G + " H:" + this.H + " F" + this.getF() + (if (this.parent != null) "\tparent:" + parent.getVector3() else "")
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val node = o as Node
        return G == node.G && H == node.H && F == node.F && vector3 == node.vector3 && parent == node.parent
    }

    override fun hashCode(): Int {
        return Objects.hash(vector3, parent, G, H, F)
    }
}
