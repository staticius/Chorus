package org.chorus.lang


/**
 * 文本容器
 * 通过[.text]存放文本内容
 *
 * @author MagicDroidX (Nukkit Project)
 */

open class TextContainer(@JvmField var text: String) : Cloneable {
    /**
     * 等于[.getText]
     *
     *
     * equal [.getText]
     */
    override fun toString(): String {
        return this.text
    }

    public override fun clone(): Any {
        try {
            return super.clone() as TextContainer
        } catch (e: CloneNotSupportedException) {
            TextContainer.log.error("Failed to clone the text container {}", this.toString(), e)
        }
        return null
    }
}
