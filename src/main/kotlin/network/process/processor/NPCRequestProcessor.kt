package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.dialog.response.FormResponseDialog
import org.chorus_oss.chorus.dialog.window.FormWindowDialog
import org.chorus_oss.chorus.entity.mob.EntityNPC
import org.chorus_oss.chorus.event.player.PlayerDialogRespondedEvent
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.NPCRequestPacket

class NPCRequestProcessor : DataPacketProcessor<NPCRequestPacket>() {
    override fun handle(player: Player, pk: NPCRequestPacket) {
        val player = player.player
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
        if (player.player.dialogWindows.getIfPresent(pk.sceneName) != null) {
            //remove the window from the map only if the requestType is EXECUTE_CLOSING_COMMANDS
            val dialog: FormWindowDialog?
            if (pk.requestType == NPCRequestPacket.RequestType.EXECUTE_CLOSING_COMMANDS) {
                dialog = player.player.dialogWindows.getIfPresent(pk.sceneName)
                player.player.dialogWindows.invalidate(pk.sceneName)
            } else {
                dialog = player.player.dialogWindows.getIfPresent(pk.sceneName)
            }

            val response = FormResponseDialog(pk, dialog!!)
            for (handler in dialog.handlers) {
                handler.handle(player, response)
            }

            val event = PlayerDialogRespondedEvent(player, dialog, response)
            Server.instance.pluginManager.callEvent(event)

            //close dialog after clicked button (otherwise the client will not be able to close the window)
            if (response.clickedButton != null && pk.requestType == NPCRequestPacket.RequestType.EXECUTE_ACTION) {
                val closeWindowPacket = org.chorus_oss.protocol.packets.NPCDialoguePacket(
                    entityUniqueID = pk.entityRuntimeId,
                    actionType = org.chorus_oss.protocol.packets.NPCDialoguePacket.Companion.ActionType.Close,
                    dialogue = "",
                    sceneName = response.sceneName,
                    npcName = "",
                    actionJSON = "",
                )
                player.sendPacket(closeWindowPacket)
            }
            if (response.clickedButton != null && response.requestType === NPCRequestPacket.RequestType.EXECUTE_ACTION && response.clickedButton!!.nextDialog != null) {
                response.clickedButton!!.nextDialog!!.send(player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.NPC_REQUEST_PACKET
}
