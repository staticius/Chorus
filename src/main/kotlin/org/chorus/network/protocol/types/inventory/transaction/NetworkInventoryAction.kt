package org.chorus.network.protocol.types.inventory.transaction

import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.InventoryTransactionPacket
import lombok.*
import lombok.extern.slf4j.Slf4j

/**
 * @author CreeperFace
 */


class NetworkInventoryAction {
    var inventorySource: InventorySource? = null
    var inventorySlot: Int = 0
    var oldItem: Item? = null
    var newItem: Item? = null


    fun read(pk: InventoryTransactionPacket, byteBuf: HandleByteBuf): NetworkInventoryAction {
        //read InventorySource
        val type: InventorySource.Type = InventorySource.Type.Companion.byId(
            byteBuf.readUnsignedVarInt()
        )
        when (type) {
            InventorySource.Type.UNTRACKED_INTERACTION_UI -> inventorySource =
                InventorySource.Companion.fromUntrackedInteractionUI(byteBuf.readVarInt())

            InventorySource.Type.CONTAINER -> {
                inventorySource = InventorySource.Companion.fromContainerWindowId(byteBuf.readVarInt())
            }

            InventorySource.Type.GLOBAL -> inventorySource = InventorySource.Companion.fromGlobalInventory()
            InventorySource.Type.WORLD_INTERACTION -> {
                val flag = InventorySource.Flag.entries[byteBuf.readUnsignedVarInt()]
                inventorySource = InventorySource.Companion.fromWorldInteraction(flag)
            }

            InventorySource.Type.CREATIVE -> inventorySource = InventorySource.Companion.fromCreativeInventory()
            InventorySource.Type.NON_IMPLEMENTED_TODO -> {
                val wid = byteBuf.readVarInt()
                when (wid) {
                    SOURCE_TYPE_CRAFTING_RESULT, SOURCE_TYPE_CRAFTING_USE_INGREDIENT -> pk.isCraftingPart = true
                    SOURCE_TYPE_ENCHANT_INPUT, SOURCE_TYPE_ENCHANT_OUTPUT, SOURCE_TYPE_ENCHANT_MATERIAL -> pk.isEnchantingPart =
                        true

                    SOURCE_TYPE_ANVIL_INPUT, SOURCE_TYPE_ANVIL_MATERIAL, SOURCE_TYPE_ANVIL_RESULT -> pk.isRepairItemPart =
                        true

                    SOURCE_TYPE_TRADING_INPUT_1, SOURCE_TYPE_TRADING_INPUT_2, SOURCE_TYPE_TRADING_USE_INPUTS, SOURCE_TYPE_TRADING_OUTPUT -> pk.isTradeItemPart =
                        true
                }
                inventorySource = InventorySource.Companion.fromNonImplementedTodo(wid)
            }

            else -> inventorySource = InventorySource.Companion.fromInvalid()
        }
        this.inventorySlot = byteBuf.readUnsignedVarInt()
        this.oldItem = byteBuf.readSlot()
        this.newItem = byteBuf.readSlot()
        return this
    }

    fun write(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(inventorySource.getType().id())
        when (inventorySource.getType()) {
            InventorySource.Type.CONTAINER, InventorySource.Type.UNTRACKED_INTERACTION_UI, InventorySource.Type.NON_IMPLEMENTED_TODO -> byteBuf.writeVarInt(
                inventorySource.getContainerId()
            )

            InventorySource.Type.WORLD_INTERACTION -> byteBuf.writeUnsignedVarInt(inventorySource.getFlag().ordinal)
        }
        byteBuf.writeUnsignedVarInt(this.inventorySlot)
        byteBuf.writeSlot(this.oldItem)
        byteBuf.writeSlot(this.newItem)
    }

    companion object {
        val EMPTY_ARRAY: Array<NetworkInventoryAction?> = arrayOfNulls(0)
        const val SOURCE_TYPE_CRAFTING_RESULT: Int = -4
        const val SOURCE_TYPE_CRAFTING_USE_INGREDIENT: Int = -5
        const val SOURCE_TYPE_ANVIL_INPUT: Int = -10
        const val SOURCE_TYPE_ANVIL_MATERIAL: Int = -11
        const val SOURCE_TYPE_ANVIL_RESULT: Int = -12
        const val SOURCE_TYPE_ENCHANT_INPUT: Int = -15
        const val SOURCE_TYPE_ENCHANT_MATERIAL: Int = -16
        const val SOURCE_TYPE_ENCHANT_OUTPUT: Int = -17
        const val SOURCE_TYPE_TRADING_OUTPUT: Int = -30
        const val SOURCE_TYPE_TRADING_INPUT_1: Int = -31
        const val SOURCE_TYPE_TRADING_INPUT_2: Int = -32
        const val SOURCE_TYPE_TRADING_USE_INPUTS: Int = -33
    }
}
