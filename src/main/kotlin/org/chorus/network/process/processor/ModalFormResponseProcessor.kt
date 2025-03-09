package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.config.ServerProperties.get
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.event.player.PlayerSettingsRespondedEvent
import cn.nukkit.form.element.Element
import cn.nukkit.form.response.CustomResponse
import cn.nukkit.form.response.ElementResponse
import cn.nukkit.form.window.CustomForm
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ModalFormResponsePacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j
import java.lang.String
import kotlin.Any
import kotlin.Boolean
import kotlin.Float
import kotlin.Int

@Slf4j
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
