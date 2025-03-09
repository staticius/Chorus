package org.chorus.recipe.descriptor

import cn.nukkit.item.Item

class MolangDescriptor(val tagExpression: String, val molangVersion: Int, override val count: Int) : ItemDescriptor {
    override val type: ItemDescriptorType
        get() = ItemDescriptorType.MOLANG

    override fun toItem(): Item {
        throw UnsupportedOperationException()
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): ItemDescriptor {
        return super.clone()
    }

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is MolangDescriptor) return false
        val `this$tagExpression`: Any = this.tagExpression
        val `other$tagExpression`: Any = o.tagExpression
        if (`this$tagExpression` != `other$tagExpression`) return false
        if (this.molangVersion != o.molangVersion) return false
        return this.count == o.count
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$tagExpression`: Any = this.tagExpression
        result = result * PRIME + (`$tagExpression`?.hashCode() ?: 43)
        result = result * PRIME + this.molangVersion
        result = result * PRIME + this.count
        return result
    }

    override fun toString(): String {
        return "MolangDescriptor(tagExpression=" + this.tagExpression + ", molangVersion=" + this.molangVersion + ", count=" + this.count + ")"
    }
}
