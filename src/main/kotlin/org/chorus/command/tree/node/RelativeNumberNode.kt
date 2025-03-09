package org.chorus.command.tree.node

/**
 * @author daoge_cmd <br></br>
 * Date: 2023/6/11 <br></br>
 * PowerNukkitX Project <br></br>
 */
abstract class RelativeNumberNode<T : Number?> : ParamNode<T>() {
    override fun <E> get(): E? {
        throw UnsupportedOperationException()
    }

    abstract fun get(base: T): T
}
