package org.chorus.command.tree.node

import org.chorus.command.tree.ParamList

/**
 * 一个用来占位的空参数节点
 */
class VoidNode : IParamNode<Void?> {
    override fun fill(arg: String) {
    }

    override fun <E> get(): E? {
        return null
    }

    override fun reset() {
    }

    override val paramList: ParamList?
        get() = null

    override fun hasResult(): Boolean {
        return true
    }

    override val isOptional: Boolean
        get() = true
}
