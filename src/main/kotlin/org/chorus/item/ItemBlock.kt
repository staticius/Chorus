package org.chorus.item

import org.chorus.block.Block
import org.chorus.registry.Registries


open class ItemBlock @JvmOverloads constructor(block: Block, aux: Int = 0, count: Int = 1) :
    Item(block, aux, count, block.name, true) {
    override var damage: Int
        get() = super.damage
        set(meta) {
            if (meta != 0) {
                val i = Registries.BLOCKSTATE_ITEMMETA[block!!.id, meta]
                if (i != 0) {
                    val blockState = Registries.BLOCKSTATE[i]
                    this.block = Registries.BLOCK[blockState]
                }
            }
        }

    override fun clone(): ItemBlock {
        val block = super.clone() as ItemBlock
        block.block = this.block!!.clone()
        return block
    }

    override fun getSafeBlock(): Block {
        return block!!.clone()
    }

    override val isLavaResistant: Boolean
        get() = block!!.isLavaResistant
}
