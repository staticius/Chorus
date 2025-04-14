package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemBlock


class BlockUnknown @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate) {

    override val name: String
        get() = "Unknown"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.UNKNOWN)
    }
}
