package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.blockentity.BlockEntityItemFrame
import org.chorus.event.player.PlayerMapInfoRequestEvent
import org.chorus.item.*
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.MapInfoRequestPacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.plugin.InternalPlugin
import org.chorus.scheduler.AsyncTask


class MapInfoRequestProcessor : DataPacketProcessor<MapInfoRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: MapInfoRequestPacket) {
        val player = playerHandle.player
        var mapItem: Item? = null
        var index = 0
        var offhand = false

        for ((key, item1) in player.getOffhandInventory()!!.contents) {
            if (checkMapItemValid(item1, pk)) {
                mapItem = item1
                index = key
                offhand = true
            }
        }

        if (mapItem == null) {
            for ((key, item1) in player.getInventory().contents) {
                if (checkMapItemValid(item1, pk)) {
                    mapItem = item1
                    index = key
                }
            }
        }

        if (mapItem == null) {
            for (be in player.level!!.getBlockEntities().values) {
                if (be is BlockEntityItemFrame && checkMapItemValid(be.item, pk)) {
                    (be.item as ItemFilledMap).sendImage(player)
                    break
                }
            }
        }

        if (mapItem != null) {
            val event: PlayerMapInfoRequestEvent
            Server.instance.pluginManager.callEvent(PlayerMapInfoRequestEvent(player, mapItem).also { event = it })

            if (!event.isCancelled) {
                val map = mapItem as ItemFilledMap
                if (map.trySendImage(player)) {
                    return
                }

                val finalIndex = index
                val finalOffhand = offhand
                //TODO: 并行计算
                player.level!!.scheduler.scheduleAsyncTask(InternalPlugin.INSTANCE, object : AsyncTask() {
                    override fun onRun() {
                        map.renderMap(
                            player.level!!,
                            (player.position.floorX / 128) shl 7,
                            (player.position.floorZ / 128) shl 7,
                            1
                        )
                        if (finalOffhand) {
                            if (checkMapItemValid(
                                    player.getOffhandInventory()!!.getUnclonedItem(finalIndex),
                                    pk
                                )
                            ) player.getOffhandInventory()!!
                                .setItem(finalIndex, map)
                        } else {
                            if (checkMapItemValid(
                                    player.getInventory().getUnclonedItem(finalIndex),
                                    pk
                                )
                            ) player.getInventory().setItem(finalIndex, map)
                        }
                        map.sendImage(player)
                    }
                })
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.MAP_INFO_REQUEST_PACKET

    protected fun checkMapItemValid(item: Item?, pk: MapInfoRequestPacket): Boolean {
        return item is ItemFilledMap && item.mapId == pk.mapId
    }
}
