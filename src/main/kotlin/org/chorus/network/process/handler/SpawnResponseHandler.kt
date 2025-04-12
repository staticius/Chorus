package org.chorus.network.process.handler

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.data.property.EntityProperty.Companion.getPacketCache
import org.chorus.entity.data.property.EntityProperty.Companion.getPlayerPropertyCache
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.connection.BedrockSession
import org.chorus.network.protocol.*
import org.chorus.network.protocol.types.TrimData
import org.chorus.registry.ItemRegistry
import org.chorus.registry.ItemRuntimeIdRegistry
import org.chorus.registry.Registries
import org.chorus.utils.Loggable
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min

class SpawnResponseHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    init {
        val server: Server = Server.instance

        this.startGame()

        SpawnResponseHandler.log.debug("Sending component items")

        val itemRegistryPacket = ItemRegistryPacket()
        val entries = ObjectOpenHashSet<ItemRegistryPacket.Entry>()

        for (data in ItemRuntimeIdRegistry.ITEM_DATA) {
            var tag = CompoundTag()

            if (ItemRegistry.itemComponents.containsCompound(data.identifier)) {
                val itemTag = ItemRegistry.itemComponents.getCompound(data.identifier)
                tag.putCompound("components", itemTag.getCompound("components"))
            } else if (Registries.ITEM.customItemDefinition.containsKey(data.identifier)) {
                tag = Registries.ITEM.customItemDefinition[data.identifier]!!.nbt
            }

            entries.add(
                ItemRegistryPacket.Entry(
                    data.identifier,
                    data.runtimeId,
                    data.version,
                    data.componentBased,
                    tag
                )
            )
        }

        itemRegistryPacket.entries = entries.toTypedArray()
        player!!.dataPacket(itemRegistryPacket)

        SpawnResponseHandler.log.debug("Sending actor identifiers")
        player.dataPacket(
            AvailableActorIdentifiersPacket(
                Registries.ENTITY.tag
            )
        )

        // 注册实体属性
        // Register entity attributes
        SpawnResponseHandler.log.debug("Sending actor properties")
        for (pk in getPacketCache()) {
            player.dataPacket(pk)
        }

        SpawnResponseHandler.log.debug("Sending biome definitions")
        player.dataPacket(
            BiomeDefinitionListPacket(
                Registries.BIOME.biomeDefinitionListPacketData
            )
        )

        SpawnResponseHandler.log.debug("Sending attributes")
        player.syncAttributes()

        SpawnResponseHandler.log.debug("Sending available commands")
        this.session.syncAvailableCommands()

        // 发送玩家权限列表
        // Send player permission list
        SpawnResponseHandler.log.debug("Sending abilities")
        val col = setOf(player)
        server.onlinePlayers.values.forEach(Consumer<Player> { p: Player ->
            if (p !== player) {
                p.adventureSettings.sendAbilities(col)
                p.adventureSettings.updateAdventureSettings()
            }
        })

        SpawnResponseHandler.log.debug("Sending effects")
        player.sendPotionEffects(player)

        SpawnResponseHandler.log.debug("Sending actor metadata")
        player.sendData(player)

        SpawnResponseHandler.log.debug("Sending inventory")
        this.session.syncInventory()

        SpawnResponseHandler.log.debug("Sending creative content")
        this.session.syncCreativeContent()

        val trimDataPacket = TrimDataPacket()
        trimDataPacket.materials.addAll(TrimData.trimMaterials)
        trimDataPacket.patterns.addAll(TrimData.trimPatterns)
        this.session.sendPacket(trimDataPacket)

        player.setNameTagVisible(true)
        player.setNameTagAlwaysVisible(true)
        player.setCanClimb(true)
        player.sendMovementSpeed(player.movementSpeed)
        SpawnResponseHandler.log.debug("Sending player list")

        server.addOnlinePlayer(player)
        server.onPlayerCompleteLoginSequence(player)

        if (player.isOp || player.hasPermission("nukkit.textcolor")) {
            player.removeFormat = false
        }
    }

    private fun startGame() {
        val server: Server = Server.instance
        val startPk = StartGamePacket()

        startPk.entityUniqueId = player!!.getRuntimeID()
        startPk.entityRuntimeId = player.getRuntimeID()
        startPk.playerGamemode = Player.toNetworkGamemode(player.gamemode)

        startPk.x = player.position.x.toFloat()
        startPk.y =
            (if (player.isOnGround()) player.position.y + player.getEyeHeight() else player.position.y).toFloat() //防止在地上生成容易陷进地里
        startPk.z = player.position.z.toFloat()
        startPk.yaw = player.rotation.yaw.toFloat()
        startPk.pitch = player.rotation.pitch.toFloat()
        startPk.seed = -1L
        startPk.dimension = (player.level!!.dimension and 0xff).toByte()
        startPk.worldGamemode = Player.toNetworkGamemode(server.defaultGamemode)
        startPk.difficulty = server.getDifficulty()
        val spawn = player.safeSpawn
        startPk.spawnX = spawn.floorX
        startPk.spawnY = spawn.floorY
        startPk.spawnZ = spawn.floorZ
        startPk.hasAchievementsDisabled = true
        startPk.dayCycleStopTime = -1
        startPk.rainLevel = 0f
        startPk.lightningLevel = 0f
        startPk.commandsEnabled = player.isEnableClientCommand()
        startPk.gameRules = player.level!!.gameRules
        startPk.levelId = ""
        startPk.worldName = server.subMotd
        startPk.generator = ((player.level!!.dimension + 1) and 0xff).toByte()
            .toInt() //0 旧世界 Old world, 1 主世界 Main world, 2 下界 Nether, 3 末地 End
        startPk.serverAuthoritativeMovement = server.getServerAuthoritativeMovement()
        startPk.isInventoryServerAuthoritative = true //enable item stack request packet
        startPk.blockNetworkIdsHashed = true //enable blockhash
        // 写入自定义方块数据
        // Write custom block data
        startPk.blockProperties.addAll(Registries.BLOCK.customBlockDefinitionList)
        startPk.playerPropertyData = getPlayerPropertyCache()
        player.dataPacketImmediately(startPk)
    }

    override fun handle(pk: RequestChunkRadiusPacket) {
        player!!.viewDistance =
            max(2.0, min(pk.radius.toDouble(), player.viewDistance.toDouble())).toInt()
    }

    override fun handle(pk: SetLocalPlayerAsInitializedPacket) {
        SpawnResponseHandler.log.debug(
            "receive SetLocalPlayerAsInitializedPacket for {}",
            player!!.playerInfo.username
        )
        handle!!.onPlayerLocallyInitialized()
    }

    companion object : Loggable
}
