package org.chorus.network.protocol

import cn.nukkit.nbt.tag.ListTag.size
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.LegacySetItemSlotData
import cn.nukkit.network.protocol.types.inventory.transaction.*
import cn.nukkit.network.protocol.types.inventory.transaction.UseItemData.PredictedResult
import cn.nukkit.network.protocol.types.inventory.transaction.UseItemData.TriggerType
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.util.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class InventoryTransactionPacket : DataPacket() {
    var transactionType: Int = 0
    var actions: Array<NetworkInventoryAction>
    var transactionData: TransactionData? = null
    val legacySlots: MutableList<LegacySetItemSlotData> = ObjectArrayList()

    var legacyRequestId: Int = 0
    private var triggerType: TriggerType? = null
    private var clientInteractPrediction: PredictedResult? = null

    /**
     * NOTE: THESE FIELDS DO NOT EXIST IN THE PROTOCOL, it's merely used for convenience for us to easily
     * determine whether we're doing a crafting or enchanting transaction.
     */
    var isCraftingPart: Boolean = false
    var isEnchantingPart: Boolean = false
    var isRepairItemPart: Boolean = false
    var isTradeItemPart: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.legacyRequestId = byteBuf.readVarInt()
        if (legacyRequestId != 0) {
            val length = byteBuf.readUnsignedVarInt()
            for (i in 0..<length) {
                val containerId = byteBuf.readByte()
                val slots = byteBuf.readByteArray()
                legacySlots.add(LegacySetItemSlotData(containerId.toInt(), slots))
            }
        }
        //InventoryTransactionType
        this.transactionType = byteBuf.readUnsignedVarInt()

        val length = byteBuf.readUnsignedVarInt()
        val actions: MutableCollection<NetworkInventoryAction> = ArrayDeque()
        for (i in 0..<length) {
            actions.add(NetworkInventoryAction().read(this, byteBuf))
        }
        this.actions = actions.toArray<NetworkInventoryAction>(NetworkInventoryAction.Companion.EMPTY_ARRAY)

        when (this.transactionType) {
            TYPE_NORMAL, TYPE_MISMATCH -> {}
            TYPE_USE_ITEM -> {
                val itemData = UseItemData()

                itemData.actionType = byteBuf.readUnsignedVarInt()
                itemData.triggerType = TriggerType.entries[byteBuf.readUnsignedVarInt()]
                itemData.blockPos = byteBuf.readBlockVector3()
                itemData.face = byteBuf.readBlockFace()
                itemData.hotbarSlot = byteBuf.readVarInt()
                itemData.itemInHand = byteBuf.readSlot()
                itemData.playerPos = byteBuf.readVector3f().asVector3()
                itemData.clickPos = byteBuf.readVector3f()
                itemData.blockRuntimeId = byteBuf.readUnsignedVarInt()
                itemData.clientInteractPrediction = PredictedResult.entries[byteBuf.readUnsignedVarInt()]

                this.transactionData = itemData
            }

            TYPE_USE_ITEM_ON_ENTITY -> {
                val useItemOnEntityData = UseItemOnEntityData()

                useItemOnEntityData.entityRuntimeId = byteBuf.readEntityRuntimeId()
                useItemOnEntityData.actionType = byteBuf.readUnsignedVarInt()
                useItemOnEntityData.hotbarSlot = byteBuf.readVarInt()
                useItemOnEntityData.itemInHand = byteBuf.readSlot()
                useItemOnEntityData.playerPos = byteBuf.readVector3f().asVector3()
                useItemOnEntityData.clickPos = byteBuf.readVector3f().asVector3()

                this.transactionData = useItemOnEntityData
            }

            TYPE_RELEASE_ITEM -> {
                val releaseItemData = ReleaseItemData()

                releaseItemData.actionType = byteBuf.readUnsignedVarInt()
                releaseItemData.hotbarSlot = byteBuf.readVarInt()
                releaseItemData.itemInHand = byteBuf.readSlot()
                releaseItemData.headRot = byteBuf.readVector3f().asVector3()

                this.transactionData = releaseItemData
            }

            else -> throw RuntimeException("Unknown transaction type " + this.transactionType)
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.legacyRequestId)
        byteBuf.writeUnsignedVarInt(this.transactionType)

        //slots array
        if (legacyRequestId != 0) {
            byteBuf.writeUnsignedVarInt(legacySlots.size())
            for (slot in legacySlots) {
                byteBuf.writeByte(slot.containerId.toByte().toInt())
                byteBuf.writeByteArray(slot.slots)
            }
        }

        byteBuf.writeUnsignedVarInt(actions.size)
        for (action in this.actions) {
            action.write(byteBuf)
        }

        when (this.transactionType) {
            TYPE_NORMAL, TYPE_MISMATCH -> {}
            TYPE_USE_ITEM -> {
                val useItemData = transactionData as UseItemData?
                byteBuf.writeUnsignedVarInt(useItemData!!.actionType)
                byteBuf.writeUnsignedVarInt(useItemData.triggerType!!.ordinal())
                byteBuf.writeBlockVector3(useItemData.blockPos!!)
                byteBuf.writeBlockFace(useItemData.face!!)
                byteBuf.writeVarInt(useItemData.hotbarSlot)
                byteBuf.writeSlot(useItemData.itemInHand)
                byteBuf.writeVector3f(useItemData.playerPos!!.asVector3f())
                byteBuf.writeVector3f(useItemData.clickPos!!)
                byteBuf.writeUnsignedVarInt(useItemData.blockRuntimeId)
                byteBuf.writeUnsignedVarInt(useItemData.clientInteractPrediction!!.ordinal())
            }

            TYPE_USE_ITEM_ON_ENTITY -> {
                val useItemOnEntityData = transactionData as UseItemOnEntityData?

                byteBuf.writeEntityRuntimeId(useItemOnEntityData!!.entityRuntimeId)
                byteBuf.writeUnsignedVarInt(useItemOnEntityData.actionType)
                byteBuf.writeVarInt(useItemOnEntityData.hotbarSlot)
                byteBuf.writeSlot(useItemOnEntityData.itemInHand)
                byteBuf.writeVector3f(useItemOnEntityData.playerPos!!.asVector3f())
                byteBuf.writeVector3f(useItemOnEntityData.clickPos!!.asVector3f())
            }

            TYPE_RELEASE_ITEM -> {
                val releaseItemData = transactionData as ReleaseItemData?

                byteBuf.writeUnsignedVarInt(releaseItemData!!.actionType)
                byteBuf.writeVarInt(releaseItemData.hotbarSlot)
                byteBuf.writeSlot(releaseItemData.itemInHand)
                byteBuf.writeVector3f(releaseItemData.headRot!!.asVector3f())
            }

            else -> throw RuntimeException("Unknown transaction type " + this.transactionType)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.INVENTORY_TRANSACTION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val TYPE_NORMAL: Int = 0
        const val TYPE_MISMATCH: Int = 1
        const val TYPE_USE_ITEM: Int = 2
        const val TYPE_USE_ITEM_ON_ENTITY: Int = 3
        const val TYPE_RELEASE_ITEM: Int = 4

        const val USE_ITEM_ACTION_CLICK_BLOCK: Int = 0
        const val USE_ITEM_ACTION_CLICK_AIR: Int = 1
        const val USE_ITEM_ACTION_BREAK_BLOCK: Int = 2

        const val RELEASE_ITEM_ACTION_RELEASE: Int = 0 //bow shoot
        const val RELEASE_ITEM_ACTION_CONSUME: Int = 1 //eat food, drink potion

        const val USE_ITEM_ON_ENTITY_ACTION_INTERACT: Int = 0
        const val USE_ITEM_ON_ENTITY_ACTION_ATTACK: Int = 1


        const val ACTION_MAGIC_SLOT_DROP_ITEM: Int = 0
        const val ACTION_MAGIC_SLOT_PICKUP_ITEM: Int = 1

        const val ACTION_MAGIC_SLOT_CREATIVE_DELETE_ITEM: Int = 0
        const val ACTION_MAGIC_SLOT_CREATIVE_CREATE_ITEM: Int = 1
    }
}
