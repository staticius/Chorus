package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.dialog.response.FormResponseDialog
import org.chorus_oss.chorus.dialog.window.FormWindowDialog
import org.chorus_oss.chorus.entity.mob.EntityNPC
import org.chorus_oss.chorus.event.player.PlayerDialogRespondedEvent
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.NPCDialoguePacket
import org.chorus_oss.chorus.network.protocol.NPCRequestPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

class NPCRequestProcessor : DataPacketProcessor<NPCRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: NPCRequestPacket) {
        val player = playerHandle.player
        //若sceneName字段为空，则为玩家在编辑NPC，我们并不需要记录对话框，直接通过entityRuntimeId获取实体即可
        val entity = player.level!!.getEntity(pk.entityRuntimeId)
        if (pk.sceneName.isEmpty() && entity is EntityNPC) {
            val dialog: FormWindowDialog = entity.dialog

            val response = FormResponseDialog(pk, dialog)
            for (handler in dialog.handlers) {
                handler.handle(player, response)
            }

            val event = PlayerDialogRespondedEvent(player, dialog, response)
            Server.instance.pluginManager.callEvent(event)
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
            for (handler in dialog.handlers) {
                handler.handle(player, response)
            }

            val event = PlayerDialogRespondedEvent(player, dialog, response)
            Server.instance.pluginManager.callEvent(event)

            //close dialog after clicked button (otherwise the client will not be able to close the window)
            if (response.clickedButton != null && pk.requestType == NPCRequestPacket.RequestType.EXECUTE_ACTION) {
                val closeWindowPacket = NPCDialoguePacket()
                closeWindowPacket.runtimeEntityId = pk.entityRuntimeId
                closeWindowPacket.sceneName = response.sceneName
                closeWindowPacket.action = NPCDialoguePacket.NPCDialogAction.CLOSE
                player.dataPacket(closeWindowPacket)
            }
            if (response.clickedButton != null && response.requestType === NPCRequestPacket.RequestType.EXECUTE_ACTION && response.clickedButton!!.nextDialog != null) {
                response.clickedButton!!.nextDialog!!.send(player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.NPC_REQUEST_PACKET
}
