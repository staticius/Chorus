package cn.nukkit.recipe.descriptor

import cn.nukkit.item.Item

class DefaultDescriptor(@JvmField val item: Item) : ItemDescriptor {
    override val type: ItemDescriptorType
        get() = ItemDescriptorType.DEFAULT

    override fun toItem(): Item {
        return item.clone()
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): ItemDescriptor {
        return super.clone()
    }

    override val count: Int
        get() = item.getCount()

    override fun match(item: Item): Boolean {
        return this.item.equals(item, true, false)
    }

    override fun hashCode(): Int {
        return item.hashCode()
    }

    override fun toString(): String {
        return "DefaultDescriptor(item=" + this.item + ")"
    }
}