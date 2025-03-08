package cn.nukkit.network.process.handler

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.data.property.EntityProperty.Companion.getPacketCache
import cn.nukkit.entity.data.property.EntityProperty.Companion.getPlayerPropertyCache
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.protocol.*
import cn.nukkit.network.protocol.types.TrimData
import cn.nukkit.registry.ItemRegistry
import cn.nukkit.registry.ItemRuntimeIdRegistry
import cn.nukkit.registry.Registries
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import lombok.extern.slf4j.Slf4j
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min

@Slf4j
class SpawnResponseHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    init {
        val server: Server = player.getServer()

        this.startGame()

        SpawnResponseHandler.log.debug("Sending component items")

        val itemRegistryPacket = ItemRegistryPacket()
        val entries = ObjectOpenHashSet<ItemRegistryPacket.Entry>()

        for (data in ItemRuntimeIdRegistry.getITEMDATA()) {
            var tag = CompoundTag()

            if (ItemRegistry.getItemComponents().containsCompound(data.identifier)) {
                val item_tag = ItemRegistry.getItemComponents().getCompound(data.identifier)
                tag.putCompound("components", item_tag!!.getCompound("components"))
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

        itemRegistryPacket.entries = entries.toArray(ItemRegistryPacket.Entry.EMPTY_ARRAY)
        player!!.dataPacket(itemRegistryPacket)

        SpawnResponseHandler.log.debug("Sending actor identifiers")
        player.dataPacket(AvailableEntityIdentifiersPacket())

        // 注册实体属性
        // Register entity attributes
        SpawnResponseHandler.log.debug("Sending actor properties")
        for (pk in getPacketCache()) {
            player.dataPacket(pk)
        }

        SpawnResponseHandler.log.debug("Sending biome definitions")
        player.dataPacket(BiomeDefinitionListPacket())

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
        player.sendMovementSpeed(player.getMovementSpeed())
        SpawnResponseHandler.log.debug("Sending player list")

        server.addOnlinePlayer(player)
        server.onPlayerCompleteLoginSequence(player)

        if (player.isOp || player.hasPermission("nukkit.textcolor")) {
            player.removeFormat = false
        }
    }

    private fun startGame() {
        val server: Server = player.getServer()
        val startPk = StartGamePacket()

        startPk.entityUniqueId = player!!.getId()
        startPk.entityRuntimeId = player.getId()
        startPk.playerGamemode = Player.toNetworkGamemode(player.gamemode)

        startPk.x = player.position.south.toFloat()
        startPk.y =
            (if (player.isOnGround()) player.position.up + player.getEyeHeight() else player.position.up).toFloat() //防止在地上生成容易陷进地里
        startPk.z = player.position.west.toFloat()
        startPk.yaw = player.rotation.yaw.toFloat()
        startPk.pitch = player.rotation.pitch.toFloat()
        startPk.seed = -1L
        startPk.dimension = (player.level!!.dimension and 0xff).toByte()
        startPk.worldGamemode = Player.toNetworkGamemode(server.defaultGamemode)
        startPk.difficulty = server.difficulty
        val spawn = player.safeSpawn
        startPk.spawnX = spawn.floorX
        startPk.spawnY = spawn.floorY
        startPk.spawnZ = spawn.floorZ
        startPk.hasAchievementsDisabled = true
        startPk.dayCycleStopTime = -1
        startPk.rainLevel = 0f
        startPk.lightningLevel = 0f
        startPk.commandsEnabled = player.isEnableClientCommand
        startPk.gameRules = player.level!!.gameRules
        startPk.levelId = ""
        startPk.worldName = server.subMotd
        startPk.generator = ((player.level!!.dimension + 1) and 0xff).toByte()
            .toInt() //0 旧世界 Old world, 1 主世界 Main world, 2 下界 Nether, 3 末地 End
        startPk.serverAuthoritativeMovement = server.serverAuthoritativeMovement
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
}
