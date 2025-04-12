package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerFormRespondedEvent
import org.chorus.event.player.PlayerSettingsRespondedEvent
import org.chorus.form.element.Element
import org.chorus.form.element.custom.*
import org.chorus.form.response.CustomResponse
import org.chorus.form.response.ElementResponse
import org.chorus.form.window.CustomForm
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ModalFormResponsePacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.utils.Loggable

class ModalFormResponseProcessor : DataPacketProcessor<ModalFormResponsePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ModalFormResponsePacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        if (pk.data.length > 1024) {
            player.close("§cPacket handling error")
            return
        }

        if (playerHandle.formWindows.containsKey(pk.formId)) {
            val window = playerHandle.formWindows.remove(pk.formId)!!

            val response = window.respond(player, pk.data.trim { it <= ' ' })

            val event = PlayerFormRespondedEvent(player, pk.formId, window, response!!)
            Server.instance.pluginManager.callEvent(event)
        } else if (playerHandle.serverSettings.containsKey(pk.formId)) {
            val window = playerHandle.serverSettings[pk.formId]!!

            val response = window.respond(player, pk.data.trim { it <= ' ' })

            val event = PlayerSettingsRespondedEvent(
                player, pk.formId, window,
                response!!
            )
            Server.instance.pluginManager.callEvent(event)

            // Apply responses as default settings
            if (!event.isCancelled && window is CustomForm) {
                (response as CustomResponse).responses.forEach { (i, res) ->
                    when (val e: Element = window.elements[i]) {
                        is ElementDropdown -> e.defaultOption = ((res as ElementResponse).elementId)
                        is ElementInput -> e.defaultText = (res.toString())
                        is ElementSlider -> e.defaultValue = (res as Float)
                        is ElementToggle -> e.defaultValue = (res as Boolean)
                        is ElementStepSlider -> e.defaultStep = ((res as ElementResponse).elementId)
                        else -> ModalFormResponseProcessor.log.warn("Illegal element {} within ServerSettings", e)
                    }
                }
            }
        } else ModalFormResponseProcessor.log.warn("{} sent unknown form id {}", player.getEntityName(), pk.formId)
    }

    override val packetId: Int
        get() = ProtocolInfo.MODAL_FORM_RESPONSE_PACKET

    companion object : Loggable
}
