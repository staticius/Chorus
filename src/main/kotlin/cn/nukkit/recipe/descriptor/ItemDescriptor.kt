package cn.nukkit.recipe.descriptor

import cn.nukkit.item.Item

interface ItemDescriptor : Cloneable {
    val type: ItemDescriptorType

    fun toItem(): Item

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): ItemDescriptor

    val count: Int

    fun match(item: Item): Boolean {
        return false
    }
}
