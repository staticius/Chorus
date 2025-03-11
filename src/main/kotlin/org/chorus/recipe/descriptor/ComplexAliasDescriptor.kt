package org.chorus.recipe.descriptor

import org.chorus.item.Item



class ComplexAliasDescriptor : ItemDescriptor {
    var name: String? = null

    override val type: ItemDescriptorType
        get() = ItemDescriptorType.COMPLEX_ALIAS

    override fun toItem(): Item {
        throw UnsupportedOperationException()
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): ItemDescriptor {
        return super.clone()
    }

    override val count: Int
        get() = 0
}
