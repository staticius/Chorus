package org.chorus_oss.chorus.dialog.response

import org.chorus_oss.chorus.dialog.element.ElementDialogButton
import org.chorus_oss.chorus.dialog.window.FormWindowDialog
import org.chorus_oss.chorus.network.protocol.NPCRequestPacket


class FormResponseDialog(packet: NPCRequestPacket, dialog: FormWindowDialog) {
    val entityRuntimeId = packet.entityRuntimeId
    val data: String = packet.data
    var clickedButton: ElementDialogButton? = null //can be null
    val sceneName: String
    val requestType: NPCRequestPacket.RequestType
    val skinType: Int

    init {
        try {
            this.clickedButton = dialog.getButtons()[packet.skinType]
        } catch (e: IndexOutOfBoundsException) {
            this.clickedButton = null
        }
        this.sceneName = packet.sceneName
        this.requestType = packet.requestType
        this.skinType = packet.skinType
    }
}
