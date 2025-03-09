package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.config.ServerProperties.get
import org.chorus.entity.Entity.getServer
import org.chorus.entity.EntityHuman.getName
import org.chorus.event.player.PlayerFormRespondedEvent
import org.chorus.event.player.PlayerSettingsRespondedEvent
import org.chorus.form.element.Element
import org.chorus.form.response.CustomResponse
import org.chorus.form.response.ElementResponse
import org.chorus.form.window.CustomForm
import org.chorus.item.Item.Companion.get
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ModalFormResponsePacket
import org.chorus.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j
import java.lang.String
import kotlin.Any
import kotlin.Boolean
import kotlin.Float
import kotlin.Int


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
            player.getServer().getPluginManager().callEvent(event)
        } else if (playerHandle.serverSettings.containsKey(pk.formId)) {
            val window = playerHandle.serverSettings[pk.formId]!!

            val response = window.respond(player, pk.data.trim { it <= ' ' })

            val event = PlayerSettingsRespondedEvent(
                player, pk.formId, window,
                response!!
            )
            player.getServer().getPluginManager().callEvent(event)

            // Apply responses as default settings
            if (!event.isCancelled && window is CustomForm && response != null) {
                (response as CustomResponse).responses.forEach { (i: Int?, res: Any?) ->
                    val e: Element = window.elements().get(i)
                    when (e) {
                        -> dropdown.defaultOption((res as ElementResponse).elementId())
                        -> input.defaultText(String.valueOf(res))
                        -> slider.defaultValue(res as Float)
                        -> toggle.defaultValue(res as Boolean)
                        -> stepSlider.defaultStep((res as ElementResponse).elementId())
                        else -> ModalFormResponseProcessor.log.warn("Illegal element {} within ServerSettings", e)
                    }
                }
            }
        } else ModalFormResponseProcessor.log.warn("{} sent unknown form id {}", player.getName(), pk.formId)
    }

    override val packetId: Int
        get() = ProtocolInfo.MODAL_FORM_RESPONSE_PACKET
}
