package org.chorus.blockentity

/**
 * 表达一个能被命名的事物的接口。<br></br>
 * An interface describes an object that can be named.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * 
 */
interface BlockEntityNameable {
    /**
     * 返回这个事物的名字。<br></br>
     * Gets the name of this object.
     *
     * @return 这个事物的名字。<br></br>The name of this object.
     * 
     */
    /**
     * 设置或更改这个事物的名字。<br></br>
     * Changes the name of this object, or names it.
     *
     * @param name 这个事物的新名字。<br></br>The new name of this object.
     * 
     */
    @JvmField
    var name: String?

    /**
     * 返回这个事物是否有名字。<br></br>
     * Whether this object has a name.
     *
     * @return 如果有名字，返回 `true`。<br></br>`true` for this object has a name.
     * 
     */
    fun hasName(): Boolean
}
