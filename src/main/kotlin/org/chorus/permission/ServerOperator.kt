package org.chorus.permission

/**
 * 能成为服务器管理员(OP)的对象。<br></br>
 * Who can be an operator(OP).
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @see org.chorus.permission.Permissible
 *
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
interface ServerOperator {
    /**
     * 返回这个对象是不是服务器管理员。<br></br>
     * Returns if this object is an operator.
     *
     * @return 这个对象是不是服务器管理员。<br></br>if this object is an operator.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    /**
     * 把这个对象设置成服务器管理员。<br></br>
     * Sets this object to be an operator or not to be.
     *
     * @param value `true`为授予管理员，`false`为取消管理员。<br></br>
     * `true` for giving this operator or `false` for cancelling.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    var isOp: Boolean
}
