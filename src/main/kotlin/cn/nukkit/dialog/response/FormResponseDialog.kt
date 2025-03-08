package cn.nukkit.dialog.response

import cn.nukkit.dialog.element.ElementDialogButton
import cn.nukkit.dialog.window.FormWindowDialog
import cn.nukkit.network.protocol.NPCRequestPacket
import lombok.Getter

@Getter
class FormResponseDialog(packet: NPCRequestPacket, dialog: FormWindowDialog) {
    private val entityRuntimeId = packet.entityRuntimeId
    private val data: String = packet.data
    private var clickedButton: ElementDialogButton? = null //can be null
    private val sceneName: String
    private val requestType: NPCRequestPacket.RequestType
    private val skinType: Int

    init {
        try {
            this.clickedButton = dialog.buttons[packet.skinType]
        } catch (e: IndexOutOfBoundsException) {
            this.clickedButton = null
        }
        this.sceneName = packet.sceneName
        this.requestType = packet.requestType
        this.skinType = packet.skinType
    }
}
