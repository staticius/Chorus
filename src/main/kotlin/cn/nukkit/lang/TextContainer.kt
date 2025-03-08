package cn.nukkit.lang

import lombok.extern.slf4j.Slf4j

/**
 * 文本容器
 * 通过[.text]存放文本内容
 *
 * @author MagicDroidX (Nukkit Project)
 */
@Slf4j
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
