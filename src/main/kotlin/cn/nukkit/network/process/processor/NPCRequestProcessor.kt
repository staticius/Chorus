package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.dialog.response.FormResponseDialog
import cn.nukkit.dialog.window.FormWindowDialog
import cn.nukkit.entity.Entity.equals
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.mob.EntityNPC
import cn.nukkit.entity.mob.EntityNPC.getDialog
import cn.nukkit.event.player.PlayerDialogRespondedEvent
import cn.nukkit.item.Item.equals
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.NPCDialoguePacket
import cn.nukkit.network.protocol.NPCRequestPacket
import cn.nukkit.network.protocol.ProtocolInfo

class NPCRequestProcessor : DataPacketProcessor<NPCRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: NPCRequestPacket) {
        val player = playerHandle.player
        //若sceneName字段为空，则为玩家在编辑NPC，我们并不需要记录对话框，直接通过entityRuntimeId获取实体即可
        if (pk.sceneName.isEmpty() && player.level!!.getEntity(pk.entityRuntimeId) is EntityNPC) {
            val dialog: FormWindowDialog = npcEntity.getDialog()

            val response = FormResponseDialog(pk, dialog)
            for (handler in dialog.getHandlers()) {
                handler.handle(player, response)
            }

            val event = PlayerDialogRespondedEvent(player, dialog, response)
            player.getServer().getPluginManager().callEvent(event)
            return
        }
        if (playerHandle.dialogWindows.getIfPresent(pk.sceneName) != null) {
            //remove the window from the map only if the requestType is EXECUTE_CLOSING_COMMANDS
            val dialog: FormWindowDialog?
            if (pk.requestType == NPCRequestPacket.RequestType.EXECUTE_CLOSING_COMMANDS) {
                dialog = playerHandle.dialogWindows.getIfPresent(pk.sceneName)
                playerHandle.dialogWindows.invalidate(pk.sceneName)
            } else {
                dialog = playerHandle.dialogWindows.getIfPresent(pk.sceneName)
            }

            val response = FormResponseDialog(pk, dialog!!)
            for (handler in dialog.getHandlers()) {
                handler.handle(player, response)
            }

            val event = PlayerDialogRespondedEvent(player, dialog, response)
            player.getServer().getPluginManager().callEvent(event)

            //close dialog after clicked button (otherwise the client will not be able to close the window)
            if (response.getClickedButton() != null && pk.requestType == NPCRequestPacket.RequestType.EXECUTE_ACTION) {
                val closeWindowPacket = NPCDialoguePacket()
                closeWindowPacket.runtimeEntityId = pk.entityRuntimeId
                closeWindowPacket.sceneName = response.getSceneName()
                closeWindowPacket.action = NPCDialoguePacket.NPCDialogAction.CLOSE
                player.dataPacket(closeWindowPacket)
            }
            if (response.getClickedButton() != null && response.getRequestType() === NPCRequestPacket.RequestType.EXECUTE_ACTION && response.getClickedButton().nextDialog != null) {
                response.getClickedButton().nextDialog.send(player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.NPC_REQUEST_PACKET
}
