package org.chorus

import cn.nukkit.block.BlockComposter
import cn.nukkit.command.SimpleCommandMap
import cn.nukkit.config.ServerSettings
import cn.nukkit.config.YamlSnakeYamlConfigurer
import cn.nukkit.dispenser.DispenseBehaviorRegister
import cn.nukkit.entity.Attribute
import cn.nukkit.entity.data.Skin
import cn.nukkit.entity.data.profession.Profession
import cn.nukkit.event.server.QueryRegenerateEvent
import cn.nukkit.inventory.HumanEnderChestInventory
import cn.nukkit.inventory.HumanInventory
import cn.nukkit.inventory.HumanOffHandInventory
import cn.nukkit.inventory.Inventory
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.lang.BaseLang
import cn.nukkit.level.*
import cn.nukkit.level.format.LevelConfig
import cn.nukkit.level.format.LevelConfig.GeneratorConfig
import cn.nukkit.level.format.LevelProvider
import cn.nukkit.level.format.leveldb.LevelDBProvider
import cn.nukkit.math.Vector3
import cn.nukkit.network.Network
import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.process.DataPacketManager
import cn.nukkit.network.protocol.types.PlayerInfo
import cn.nukkit.permission.BanList
import cn.nukkit.plugin.JavaPluginLoader
import cn.nukkit.positiontracking.PositionTrackingService
import cn.nukkit.registry.BlockRegistry
import cn.nukkit.registry.Registries
import cn.nukkit.scheduler.ServerScheduler
import cn.nukkit.utils.ClientChainData
import cn.nukkit.utils.collection.FreezableArrayManager
import eu.okaeri.configs.ConfigManager
import eu.okaeri.configs.OkaeriConfig
import lombok.extern.slf4j.Slf4j
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.reflect.FieldUtils
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.mockito.ArgumentMatchers
import org.mockito.MockedStatic
import org.mockito.MockedStatic.Verification
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.io.*
import java.net.InetSocketAddress
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.LockSupport

@Slf4j
class GameMockExtension : MockitoExtension() {
    private var serverMockedStatic: MockedStatic<Server>? = null

    override fun beforeEach(context: ExtensionContext) {
        serverMockedStatic = Mockito.mockStatic(Server::class.java)
        serverMockedStatic.`when`<Any>(Verification { Server.getInstance() }).thenReturn(server)
        super.beforeEach(context)
    }

    override fun afterEach(context: ExtensionContext) {
        serverMockedStatic!!.close()
        super.afterEach(context)
    }

    @Throws(ParameterResolutionException::class)
    override fun supportsParameter(parameterContext: ParameterContext, context: ExtensionContext): Boolean {
        val b = super.supportsParameter(parameterContext, context)
        return b || parameterContext.parameter.type == GameMockExtension::class.java || parameterContext.parameter.type == BlockRegistry::class.java
                || parameterContext.parameter.type == LevelProvider::class.java
                || parameterContext.parameter.type == TestPlayer::class.java
                || parameterContext.parameter.type == TestPluginManager::class.java
                || parameterContext.parameter.type == Level::class.java
    }

    @Throws(ParameterResolutionException::class)
    override fun resolveParameter(parameterContext: ParameterContext, context: ExtensionContext): Any {
        if (parameterContext.parameter.type == GameMockExtension::class.java) {
            return gameMockExtension!!
        } else if (parameterContext.parameter.type == BlockRegistry::class.java) {
            return BLOCK_REGISTRY!!
        } else if (parameterContext.parameter.type == LevelProvider::class.java) {
            return level!!.provider
        } else if (parameterContext.parameter.type == Level::class.java) {
            return level!!
        } else if (parameterContext.parameter.type == TestPlayer::class.java) {
            return player!!
        } else if (parameterContext.parameter.type == TestPluginManager::class.java) {
            return pluginManager!!
        }
        return super.resolveParameter(parameterContext, context)
    }

    fun stopNetworkTickLoop() {
        running.set(false)
    }

    fun mockNetworkTickLoop() {
        val main = Thread.currentThread()
        val t = Thread {
            while (running.get()) {
                try {
                    network!!.process()
                } catch (ignore: Exception) {
                }
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
            LockSupport.unpark(main)
        }
        t.isDaemon = true
        t.start()
        LockSupport.park()
    }

    companion object {
        var banList: BanList = Mockito.mock(BanList::class.java)
        var pluginManager: TestPluginManager? = null
        var simpleCommandMap: SimpleCommandMap = Mockito.mock(SimpleCommandMap::class.java)
        var serverScheduler: ServerScheduler? = null
        var freezableArrayManager: FreezableArrayManager? = null
        var network: Network? = null
        var level: Level? = null

        val server: Server = Mockito.mock(Server::class.java)
        var gameMockExtension: GameMockExtension? = null
        var BLOCK_REGISTRY: BlockRegistry? = null
        var player: TestPlayer? = null

        init {
            Mockito.mockStatic(Server::class.java).use { serverMockedStatic ->
                serverMockedStatic.`when`<Any> { Server.getInstance() }.thenReturn(server)
                Registries.PACKET.init()
                Registries.ENTITY.init()
                Profession.init()
                Registries.BLOCKENTITY.init()
                Registries.BLOCKSTATE_ITEMMETA.init()
                Registries.BLOCK.init()
                Enchantment.init()
                Registries.ITEM_RUNTIMEID.init()
                Registries.POTION.init()
                Registries.ITEM.init()
                Registries.CREATIVE.init()
                Registries.BIOME.init()
                Registries.FUEL.init()
                Registries.GENERATE_STAGE.init()
                Registries.GENERATOR.init()
                Registries.RECIPE.init()
                Registries.EFFECT.init()
                Attribute.init()
                BlockComposter.init()
                DispenseBehaviorRegister.init()
                BLOCK_REGISTRY = Registries.BLOCK

                serverScheduler = ServerScheduler()
                Mockito.`when`(server.scheduler).thenReturn(serverScheduler)
                Mockito.`when`(banList.entires).thenReturn(LinkedHashMap())
                Mockito.`when`(server.ipBans).thenReturn(banList)
                Mockito.`when`(server.language).thenReturn(BaseLang("eng", "src/main/resources/language"))
                val serverSettings = ConfigManager.create(
                    ServerSettings::class.java
                ) { it: OkaeriConfig ->
                    it.withConfigurer(YamlSnakeYamlConfigurer())
                    it.withBindFile("nukkit.yml")
                    it.withRemoveOrphans(true)
                    it.saveDefaults()
                    it.load(true)
                }
                Mockito.`when`(server.settings).thenReturn(serverSettings)
                Mockito.`when`(server.apiVersion).thenReturn("1.0.0")
                Mockito.`when`(simpleCommandMap.commands).thenReturn(emptyMap())

                pluginManager = TestPluginManager(server, simpleCommandMap)
                pluginManager!!.registerInterface(JavaPluginLoader::class.java)
                Mockito.`when`(server.pluginManager).thenReturn(pluginManager)
                pluginManager!!.loadInternalPlugin()

                freezableArrayManager = FreezableArrayManager(
                    server.settings.freezeArraySettings().enable(),
                    server.settings.freezeArraySettings().slots(),
                    server.settings.freezeArraySettings().defaultTemperature(),
                    server.settings.freezeArraySettings().freezingPoint(),
                    server.settings.freezeArraySettings().absoluteZero(),
                    server.settings.freezeArraySettings().boilingPoint(),
                    server.settings.freezeArraySettings().melting(),
                    server.settings.freezeArraySettings().singleOperation(),
                    server.settings.freezeArraySettings().batchOperation()
                )
                Mockito.`when`(server.freezableArrayManager).thenReturn(freezableArrayManager)

                Mockito.`when`(server.motd).thenReturn("PNX")
                Mockito.`when`(server.onlinePlayers).thenReturn(HashMap())
                Mockito.`when`(server.gamemode).thenReturn(1)
                Mockito.`when`(server.name).thenReturn("PNX")
                Mockito.`when`(server.nukkitVersion).thenReturn("1.0.0")
                Mockito.`when`(server.gitCommit).thenReturn("1.0.0")
                Mockito.`when`(server.maxPlayers).thenReturn(100)
                Mockito.`when`(server.hasWhitelist()).thenReturn(false)
                Mockito.`when`(server.port).thenReturn(19132)
                Mockito.`when`(server.ip).thenReturn("127.0.0.1")

                val queryRegenerateEvent = QueryRegenerateEvent(server)
                Mockito.`when`(server.queryInformation).thenReturn(queryRegenerateEvent)
                Mockito.`when`(server.network).thenCallRealMethod()
                Mockito.`when`(server.autoSave).thenReturn(false)
                Mockito.`when`(server.tick).thenReturn(1)
                Mockito.`when`(server.viewDistance).thenReturn(4)
                Mockito.`when`(server.recipeRegistry).thenCallRealMethod()

                val pool = ForkJoinPool(Runtime.getRuntime().availableProcessors())
                Mockito.`when`(server.computeThreadPool).thenReturn(pool)
                Mockito.`when`(server.commandMap).thenReturn(simpleCommandMap)
                Mockito.`when`(server.scoreboardManager).thenReturn(null)
                try {
                    val positionTrackingService =
                        PositionTrackingService(File(Nukkit.DATA_PATH, "services/position_tracking_db"))
                    Mockito.`when`(server.positionTrackingService).thenReturn(positionTrackingService)
                } catch (e: FileNotFoundException) {
                    throw RuntimeException(e)
                }
                Mockito.doNothing().`when`(server).sendRecipeList(ArgumentMatchers.any())
                try {
                    FieldUtils.writeDeclaredField(server, "levelArray", Level.EMPTY_ARRAY, true)
                    FieldUtils.writeDeclaredField(server, "autoSave", false, true)
                    FieldUtils.writeDeclaredField(
                        server,
                        "tickAverage",
                        floatArrayOf(
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f
                        ),
                        true
                    )
                    FieldUtils.writeDeclaredField(
                        server,
                        "useAverage",
                        floatArrayOf(
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f,
                            20f
                        ),
                        true
                    )
                    network = Network(server)
                    FieldUtils.writeDeclaredField(server, "network", network, true)
                    FieldUtils.writeDeclaredStaticField(Server::class.java, "instance", server, true)
                } catch (e: IllegalAccessException) {
                    throw RuntimeException(e)
                }
            }
        }

        //mock player
        init {
            val serverSession = Mockito.mock(BedrockSession::class.java)
            val info = PlayerInfo(
                "test",
                UUID.randomUUID(),
                null,
                Mockito.mock(ClientChainData::class.java)
            )
            val dataPacketManager = DataPacketManager()
            Mockito.`when`(serverSession.dataPacketManager).thenReturn(dataPacketManager)
            Mockito.doNothing().`when`(serverSession).sendPacketImmediately(ArgumentMatchers.any())
            Mockito.doNothing().`when`(serverSession).sendPacket(ArgumentMatchers.any())
            player = TestPlayer(serverSession, info)
            player.adventureSettings = AdventureSettings(player)
            player.loggedIn = true
            player.spawned = true
            TestUtils.setField(
                Player::class.java, player, "info", PlayerInfo(
                    "test", UUID.nameUUIDFromBytes(byteArrayOf(1, 2, 3)), Mockito.mock(
                        Skin::class.java
                    ), Mockito.mock(ClientChainData::class.java)
                )
            )
            player.setInventories(
                arrayOf<Inventory>(
                    HumanInventory(player),
                    HumanOffHandInventory(player),
                    HumanEnderChestInventory(player)
                )
            )
            val playerHandle = PlayerHandle(player)
            playerHandle.addDefaultWindows()
            TestUtils.setField(Player::class.java, player, "foodData", PlayerFood(player, 20, 20f))
            try {
                FileUtils.copyDirectory(File("src/test/resources/level"), File("src/test/resources/newlevel"))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            level = Level(
                Server.getInstance(),
                "newlevel",
                "src/test/resources/newlevel",
                1,
                LevelDBProvider::class.java,
                GeneratorConfig(
                    "flat",
                    114514,
                    false,
                    LevelConfig.AntiXrayMode.LOW,
                    true,
                    DimensionEnum.OVERWORLD.dimensionData,
                    HashMap()
                )
            )
            level!!.initLevel()

            val map = HashMap<Int, Level?>()
            map[1] = level
            Mockito.`when`(server.levels).thenReturn(map)

            val players: MutableMap<InetSocketAddress, Player?> = HashMap()
            players[InetSocketAddress("127.0.0.1", 63333)] = player
            TestUtils.setField(Server::class.java, server, "players", players)

            player.level = level!!
            player.setPosition(Vector3(0.0, 100.0, 0.0))

            val t = Thread {
                level!!.close()
                try {
                    val file1 = Path.of("services").toFile()
                    if (file1.exists()) {
                        FileUtils.deleteDirectory(file1)
                    }
                    val file2 = Path.of("src/test/resources/newlevel").toFile()
                    if (file2.exists()) {
                        FileUtils.deleteDirectory(file2)
                    }
                    val file3 = Path.of("config.yml").toFile()
                    if (file3.exists()) {
                        FileUtils.delete(file3)
                    }
                    println("TEST END!!!!!")
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
            Runtime.getRuntime().addShutdownHook(t)

            gameMockExtension = GameMockExtension()
        }

        val running: AtomicBoolean = AtomicBoolean(true)
    }
}
