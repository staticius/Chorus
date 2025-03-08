package cn.nukkit.recipe.descriptor

import cn.nukkit.item.Item

class InvalidDescriptor private constructor() : ItemDescriptor {
    override var count: Int = 0

    override val type: ItemDescriptorType
        get() = ItemDescriptorType.INVALID

    override fun toItem(): Item {
        throw UnsupportedOperationException()
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): ItemDescriptor {
        return super.clone()
    }

    override fun toString(): String {
        return "InvalidDescriptor(count=" + this.count + ")"
    }

    companion object {
        val INSTANCE: InvalidDescriptor = InvalidDescriptor()
    }
}
