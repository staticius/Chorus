package org.chorus

import cn.nukkit.block.BlockComposter
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import cn.nukkit.command.PluginIdentifiableCommand
import cn.nukkit.command.SimpleCommandMap
import cn.nukkit.command.defaults.WorldCommand
import cn.nukkit.command.function.FunctionManager
import cn.nukkit.compression.ZlibChooser.setProvider
import cn.nukkit.config.ServerProperties
import cn.nukkit.config.ServerPropertiesKeys
import cn.nukkit.config.ServerSettings
import cn.nukkit.config.YamlSnakeYamlConfigurer
import cn.nukkit.console.NukkitConsole
import cn.nukkit.dispenser.DispenseBehaviorRegister
import cn.nukkit.entity.*
import cn.nukkit.entity.Attribute.getValue
import cn.nukkit.entity.EntityHuman.getUniqueId
import cn.nukkit.entity.data.Skin
import cn.nukkit.entity.data.profession.Profession
import cn.nukkit.entity.data.property.EntityProperty
import cn.nukkit.entity.data.property.EntityProperty.Companion.buildPacketData
import cn.nukkit.entity.data.property.EntityProperty.Companion.buildPlayerProperty
import cn.nukkit.event.HandlerList.Companion.unregisterAll
import cn.nukkit.event.level.LevelInitEvent
import cn.nukkit.event.level.LevelLoadEvent
import cn.nukkit.event.player.PlayerLoginEvent
import cn.nukkit.event.server.QueryRegenerateEvent
import cn.nukkit.event.server.ServerStartedEvent
import cn.nukkit.event.server.ServerStopEvent
import cn.nukkit.inventory.BaseInventory.size
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.item.enchantment.Enchantment.level
import cn.nukkit.lang.BaseLang
import cn.nukkit.lang.BaseLang.getLang
import cn.nukkit.lang.BaseLang.name
import cn.nukkit.lang.BaseLang.tr
import cn.nukkit.lang.LangCode
import cn.nukkit.lang.TextContainer
import cn.nukkit.level.*
import cn.nukkit.level.format.LevelConfig
import cn.nukkit.level.format.LevelConfig.GeneratorConfig
import cn.nukkit.level.format.LevelProviderManager.addProvider
import cn.nukkit.level.format.LevelProviderManager.getProvider
import cn.nukkit.level.format.LevelProviderManager.getProviderByName
import cn.nukkit.level.format.LevelProviderManager.getProviderName
import cn.nukkit.level.format.leveldb.LevelDBProvider
import cn.nukkit.level.generator.terra.PNXPlatform
import cn.nukkit.level.tickingarea.manager.SimpleTickingAreaManager
import cn.nukkit.level.tickingarea.manager.TickingAreaManager
import cn.nukkit.level.tickingarea.storage.JSONTickingAreaStorage
import cn.nukkit.level.updater.block.BlockStateUpdaterBase
import cn.nukkit.math.NukkitMath.round
import cn.nukkit.metadata.EntityMetadataStore
import cn.nukkit.metadata.LevelMetadataStore
import cn.nukkit.metadata.PlayerMetadataStore
import cn.nukkit.metrics.NukkitMetrics
import cn.nukkit.nbt.NBTIO.readCompressed
import cn.nukkit.nbt.NBTIO.writeGZIPCompressed
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.CompoundTag.contains
import cn.nukkit.nbt.tag.CompoundTag.entrySet
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.ListTag.size
import cn.nukkit.network.Network
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.PlayerListPacket
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.types.PlayerInfo
import cn.nukkit.network.protocol.types.XboxLivePlayerInfo
import cn.nukkit.network.rcon.RCON
import cn.nukkit.permission.BanList
import cn.nukkit.permission.BanList.entires
import cn.nukkit.permission.DefaultPermissions.registerCorePermissions
import cn.nukkit.plugin.*
import cn.nukkit.plugin.service.NKServiceManager
import cn.nukkit.plugin.service.ServiceManager
import cn.nukkit.positiontracking.PositionTrackingService
import cn.nukkit.recipe.Recipe
import cn.nukkit.registry.RecipeRegistry
import cn.nukkit.registry.Registries
import cn.nukkit.resourcepacks.ResourcePackManager
import cn.nukkit.resourcepacks.loader.JarPluginResourcePackLoader
import cn.nukkit.resourcepacks.loader.ZippedResourcePackLoader
import cn.nukkit.scheduler.ServerScheduler
import cn.nukkit.scheduler.Task
import cn.nukkit.scoreboard.manager.IScoreboardManager
import cn.nukkit.scoreboard.manager.ScoreboardManager
import cn.nukkit.scoreboard.storage.JSONScoreboardStorage
import cn.nukkit.tags.BiomeTags
import cn.nukkit.tags.BlockTags
import cn.nukkit.tags.ItemTags
import cn.nukkit.utils.*
import cn.nukkit.utils.BlockIterator.hasNext
import cn.nukkit.utils.BlockIterator.next
import cn.nukkit.utils.JSONUtils.from
import cn.nukkit.utils.JSONUtils.toPretty
import cn.nukkit.utils.MainLogger.error
import cn.nukkit.utils.SparkInstaller.initSpark
import cn.nukkit.utils.StartArgUtils.isShaded
import cn.nukkit.utils.StartArgUtils.isValidStart
import cn.nukkit.utils.Utils.allThreadDumps
import cn.nukkit.utils.Utils.getExceptionMessage
import cn.nukkit.utils.Utils.readFile
import cn.nukkit.utils.collection.FreezableArrayManager
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableMap
import eu.okaeri.configs.ConfigManager
import eu.okaeri.configs.OkaeriConfig
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongList
import it.unimi.dsi.fastutil.longs.LongLists
import lombok.extern.slf4j.Slf4j
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.Level
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.UnknownHostException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.security.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.ForkJoinWorkerThread
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a server object, global singleton.
 *
 * is instantiated in [Nukkit] and later the instance object is obtained via [cn.nukkit.Server.getInstance].
 * The constructor method of [cn.nukkit.Server] performs a number of operations, including but not limited to initializing configuration files, creating threads, thread pools, start plugins, registering recipes, blocks, entities, items, etc.
 *
 * @author MagicDroidX
 * @author Box
 */
@Slf4j
class Server internal constructor(filePath: String, dataPath: String, pluginPath: String, predefinedLanguage: String?) {
    // endregion
    // region Ban, OP and whitelist - Ban，OP与白名单
    val banByName: BanList
        // endregion
        get() {
            return field
        }
    private val nameBans: BanList
    val iPBans: Config
        get() = this.nameBans
    val ops: Config
        get() = iPBans
    private val isRunning = AtomicBoolean(true)
    private val busyingTime: LongList = LongLists.synchronize(LongArrayList(0))
    private var hasStopped = false
    @JvmField
    val pluginManager: PluginManager
    val scheduler: ServerScheduler

    /**
     * @return 返回服务器经历过的tick数<br></br>Returns the number of ticks recorded by the server
     */
    /**
     * 一个tick计数器,记录服务器已经经过的tick数
     */
    var tick: Int = 0
        private set
    var nextTick: Long = 0
        private set
    private val tickAverage =
        floatArrayOf(20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f)
    private val useAverage =
        floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var maxTick = 20f
    private var maxUse = 0f
    private var sendUsageTicker = 0
    private val console: NukkitConsole
    private val consoleThread: ConsoleThread

    /**
     * 负责地形生成，数据压缩等计算任务的FJP线程池<br></br>
     *
     *
     * FJP thread pool responsible for terrain generation, data compression and other computing tasks
     */
    val computeThreadPool: ForkJoinPool
    val commandMap: SimpleCommandMap
    val resourcePackManager: ResourcePackManager

    /**
     * 得到控制台发送者
     *
     *
     * Get the console sender
     *
     * @return [ConsoleCommandSender]
     */
    //todo: use ticker to check console
    val consoleSender: ConsoleCommandSender
    val scoreboardManager: IScoreboardManager
    val functionManager: FunctionManager
    val tickingAreaManager: TickingAreaManager
    private var maxPlayers: Int
    private var autoSave = true

    /**
     * 配置项是否检查登录时间.<P>Does the configuration item check the login time.
    </P> */
    var checkLoginTime: Boolean = false
    private var rcon: RCON? = null
    val entityMetadata: EntityMetadataStore
    val playerMetadata: PlayerMetadataStore
    val levelMetadata: LevelMetadataStore
    val network: Network
    private var serverAuthoritativeMovementMode = 0
    var getAllowFlight: Boolean? = null
    private var difficulty = Int.MAX_VALUE
    var defaultGamemode: Int = Int.MAX_VALUE
        /**
         * @return 获取默认gamemode<br></br>Get default gamemode
         */
        get() {
            if (field == Int.MAX_VALUE) {
                field = this.gamemode
            }
            return field
        }
        /**
         * Set default gamemode for the server.
         *
         * @param defaultGamemode the default gamemode
         */
        set(defaultGamemode) {
            field = defaultGamemode
            network.pong!!.gameType(
                getGamemodeString(
                    defaultGamemode,
                    true
                )
            ).update()
        }
    private var autoSaveTicker = 0
    private var autoSaveTicks = 6000
    private val baseLang: BaseLang
    var language: LangCode
        get() = baseLang

    /**
     * @return 服务器UUID<br></br>server UUID
     */
    val languageCode: UUID
        get() = language

    // endregion
    val filePath: String
    val dataPath: String
    val pluginPath: String
    private val uniquePlayers: MutableSet<UUID> = HashSet()
    val properties: ServerProperties
    private val players: MutableMap<InetSocketAddress?, Player> = ConcurrentHashMap()
    private val playerList: MutableMap<UUID, Player> = ConcurrentHashMap()
    var queryInformation: QueryRegenerateEvent
        private set
    private val positionTrackingService: PositionTrackingService? = null

    /**
     * @return 获得所有游戏世界<br></br>Get all the game world
     */
    val levels: MutableMap<Int, cn.nukkit.level.Level> = object : HashMap<Int, cn.nukkit.level.Level?>() {
        override fun put(key: Int, value: cn.nukkit.level.Level): cn.nukkit.level.Level? {
            val result = super.put(key, value)
            levelArray = values.toArray(cn.nukkit.level.Level.EMPTY_ARRAY)
            return result
        }

        override fun remove(key: Any, value: Any?): Boolean {
            val result = super.remove(key, value)
            levelArray = values.toArray(cn.nukkit.level.Level.EMPTY_ARRAY)
            return result
        }

        override fun remove(key: Any): cn.nukkit.level.Level? {
            val result = super.remove(key)
            levelArray = values.toArray(cn.nukkit.level.Level.EMPTY_ARRAY)
            return result
        }
    }
    private var levelArray: Array<cn.nukkit.level.Level>
    val serviceManager: ServiceManager = NKServiceManager()
    private val currentThread: Thread
    val launchTime: Long
    @JvmField
    val settings: ServerSettings
    private var watchdog: Watchdog? = null
    private val playerDataDB: DB? = null
    private var useTerra: Boolean
    val freezableArrayManager: FreezableArrayManager
    var enabledNetworkEncryption: Boolean

    /**default levels */
    private var defaultLevel: cn.nukkit.level.Level? = null
    private var defaultNether: cn.nukkit.level.Level? = null
    private var defaultEnd: cn.nukkit.level.Level? = null
    private val allowNether: Boolean
    val isNetherAllowed: Boolean
        get() = this.allowNether

    private fun loadLevels() {
        val file = File(this.dataPath + "/worlds")
        if (!file.isDirectory) throw RuntimeException("worlds isn't directory")
        //load all world from `worlds` folder
        for (f in Objects.requireNonNull(file.listFiles { obj: File -> obj.isDirectory })) {
            val levelConfig = getLevelConfig(f.name)
            if (levelConfig != null && !levelConfig.enable()) {
                continue
            }

            if (!this.loadLevel(f.name)) {
                this.generateLevel(f.name, null)
            }
        }

        if (this.getDefaultLevel() == null) {
            var levelFolder = properties.get(ServerPropertiesKeys.LEVEL_NAME, "world")
            if (levelFolder == null || levelFolder.trim { it <= ' ' }.isEmpty()) {
                Server.log.warn("level-name cannot be null, using default")
                levelFolder = "world"
                properties.get(ServerPropertiesKeys.LEVEL_NAME, levelFolder)
            }

            if (!this.loadLevel(levelFolder)) {
                //default world not exist
                //generate the default world
                val generatorConfig = HashMap<Int, GeneratorConfig>()
                //spawn seed
                var seed: Long
                val seedString =
                    properties.get(ServerPropertiesKeys.LEVEL_SEED, System.currentTimeMillis()).toString()
                seed = try {
                    seedString.toLong()
                } catch (e: NumberFormatException) {
                    seedString.hashCode().toLong()
                }
                //todo nether the_end overworld
                generatorConfig[0] = GeneratorConfig(
                    "flat",
                    seed,
                    false,
                    LevelConfig.AntiXrayMode.LOW,
                    true,
                    DimensionEnum.OVERWORLD.dimensionData,
                    emptyMap<Any, Any>()
                )
                val levelConfig = LevelConfig("leveldb", true, generatorConfig)
                this.generateLevel(levelFolder, levelConfig)
            }
            this.setDefaultLevel(this.getLevelByName("$levelFolder Dim0"))
        }
    }

    // region lifecycle & ticking - 生命周期与游戏刻
    /**
     * 重载服务器
     *
     *
     * Reload Server
     */
    fun reload() {
        Server.log.info("Reloading...")
        Server.log.info("Saving levels...")

        for (level in this.levelArray) {
            level.save()
        }

        scoreboardManager.save()
        pluginManager.disablePlugins()
        pluginManager.clearPlugins()
        commandMap.clearCommands()

        Server.log.info("Reloading properties...")
        properties.reload()
        this.maxPlayers = properties.get(ServerPropertiesKeys.MAX_PLAYERS, 20)
        if (properties.get(ServerPropertiesKeys.HARDCORE, false) && this.getDifficulty() < 3) {
            properties.get(ServerPropertiesKeys.DIFFICULTY, 3.also { difficulty = it })
        }

        nameBans.load()
        banByName.load()
        this.reloadWhitelist()
        iPBans.reload()

        for (entry in iPBans.entires.values) {
            try {
                network.blockAddress(InetAddress.getByName(entry.name), -1)
            } catch (e: UnknownHostException) {
                // ignore
            }
        }

        pluginManager.registerInterface(JavaPluginLoader::class.java)
        //todo enable js plugin when adapt
//        JSIInitiator.reset();
//        JSFeatures.clearFeatures();
//        JSFeatures.initInternalFeatures();
        scoreboardManager.read()

        Server.log.info("Reloading Registries...")
        run {
            Registries.POTION.reload()
            Registries.PACKET.reload()
            Registries.ENTITY.reload()
            Registries.BLOCKENTITY.reload()
            Registries.BLOCKSTATE_ITEMMETA.reload()
            Registries.BLOCKSTATE.reload()
            Registries.ITEM_RUNTIMEID.reload()
            Registries.BLOCK.reload()
            Registries.ITEM.reload()
            Registries.CREATIVE.reload()
            Registries.BIOME.reload()
            Registries.FUEL.reload()
            Registries.GENERATOR.reload()
            Registries.GENERATE_STAGE.reload()
            Registries.EFFECT.reload()
            Registries.RECIPE.reload()
            Enchantment.reload()
        }

        pluginManager.loadPlugins(this.pluginPath)
        functionManager.reload()

        this.enablePlugins(PluginLoadOrder.STARTUP)
        run {
            Registries.POTION.trim()
            Registries.PACKET.trim()
            Registries.ENTITY.trim()
            Registries.BLOCKENTITY.trim()
            Registries.BLOCKSTATE_ITEMMETA.trim()
            Registries.BLOCKSTATE.trim()
            Registries.ITEM_RUNTIMEID.trim()
            Registries.BLOCK.trim()
            Registries.ITEM.trim()
            Registries.CREATIVE.trim()
            Registries.BIOME.trim()
            Registries.FUEL.trim()
            Registries.GENERATOR.trim()
            Registries.GENERATE_STAGE.trim()
            Registries.EFFECT.trim()
            Registries.RECIPE.trim()
        }
        this.enablePlugins(PluginLoadOrder.POSTWORLD)
        val serverStartedEvent: ServerStartedEvent = ServerStartedEvent()
        pluginManager.callEvent(serverStartedEvent)
    }

    /**
     * 关闭服务器
     *
     *
     * Shut down the server
     */
    fun shutdown() {
        isRunning.compareAndSet(true, false)
    }

    /**
     * 强制关闭服务器
     *
     *
     * Force Shut down the server
     */
    fun forceShutdown() {
        if (this.hasStopped) {
            return
        }

        try {
            isRunning.compareAndSet(true, false)

            this.hasStopped = true

            val serverStopEvent: ServerStopEvent = ServerStopEvent()
            pluginManager.callEvent(serverStopEvent)

            if (this.rcon != null) {
                rcon!!.close()
            }

            for (player in ArrayList(players.values)) {
                player.close(player.leaveMessage, settings.baseSettings().shutdownMessage())
            }

            settings.save()

            Server.log.debug("Disabling all plugins")
            pluginManager.disablePlugins()

            Server.log.debug("Removing event handlers")
            unregisterAll()

            Server.log.debug("Saving scoreboards data")
            scoreboardManager.save()

            Server.log.debug("Stopping all tasks")
            scheduler.cancelAllTasks()
            scheduler.mainThreadHeartbeat((this.nextTick + 10000).toInt())

            Server.log.debug("Unloading all levels")
            for (level in this.levelArray) {
                this.unloadLevel(level, true)
                while (level.isThreadRunning) Thread.sleep(1)
            }
            if (positionTrackingService != null) {
                Server.log.debug("Closing position tracking service")
                positionTrackingService.close()
            }

            Server.log.debug("Closing console")
            consoleThread.interrupt()

            Server.log.debug("Stopping network interfaces")
            network.shutdown()
            playerDataDB!!.close()
            //close watchdog and metrics
            if (this.watchdog != null) {
                watchdog!!.running = false
            }
            NukkitMetrics.closeNow(this)
            //close threadPool
            ForkJoinPool.commonPool().shutdownNow()
            computeThreadPool.shutdownNow()
            //todo other things
        } catch (e: Exception) {
            Server.log.error("Exception happened while shutting down, exiting the process", e)
            System.exit(1)
        }
    }

    fun start() {
        for (entry in iPBans.entires.values) {
            try {
                network.blockAddress(InetAddress.getByName(entry.name))
            } catch (ignore: UnknownHostException) {
            }
        }
        this.tick = 0

        Server.log.info(
            language.tr(
                "nukkit.server.defaultGameMode", getGamemodeString(
                    gamemode
                )
            )
        )
        Server.log.info(
            language.tr(
                "nukkit.server.networkStart",
                TextFormat.YELLOW.toString() + (if (ip.isEmpty()) "*" else ip),
                TextFormat.YELLOW.toString() + port.toString()
            )
        )
        Server.log.info(
            language.tr(
                "nukkit.server.startFinished",
                ((System.currentTimeMillis() - Nukkit.START_TIME).toDouble() / 1000).toString()
            )
        )

        val serverStartedEvent: ServerStartedEvent = ServerStartedEvent()
        pluginManager.callEvent(serverStartedEvent)
        this.tickProcessor()
        this.forceShutdown()
    }

    fun tickProcessor() {
        scheduler.scheduleDelayedTask(InternalPlugin.INSTANCE, object : Task() {
            override fun onRun(currentTick: Int) {
                System.gc()
            }
        }, 60)

        this.nextTick = System.currentTimeMillis()
        try {
            while (isRunning.get()) {
                try {
                    this.tick()

                    val next = this.nextTick
                    val current = System.currentTimeMillis()

                    if (next - 0.1 > current) {
                        val allocated = next - current - 1
                        if (allocated > 0) {
                            Thread.sleep(allocated, 900000)
                        }
                    }
                } catch (e: RuntimeException) {
                    Server.log.error("A RuntimeException happened while ticking the server", e)
                }
            }
        } catch (e: Throwable) {
            Server.log.error("Exception happened while ticking server\n{}", allThreadDumps, e)
        }
    }

    private fun checkTickUpdates(currentTick: Int) {
        if (settings.levelSettings().alwaysTickPlayers()) {
            for (p in players.values) {
                p.onUpdate(currentTick)
            }
        }

        val baseTickRate: Int = settings.levelSettings().baseTickRate()
        //Do level ticks if level threading is disabled
        if (!settings.levelSettings().levelThread()) {
            for (level in levels.values) {
                if (level.tickRate > baseTickRate && --level.tickRateCounter > 0) {
                    continue
                }

                try {
                    val levelTime = System.currentTimeMillis()
                    //Ensures that the server won't try to tick a level without providers.
                    if (level.getProvider().level == null) {
                        Server.log.warn("Tried to tick Level " + level.getName() + " without a provider!")
                        continue
                    }
                    level.doTick(currentTick)
                    val tickMs = (System.currentTimeMillis() - levelTime).toInt()
                    level.tickRateTime = tickMs
                    if ((currentTick and 511) == 0) { // % 511
                        level.tickRateOptDelay = level.recalcTickOptDelay()
                    }

                    if (settings.levelSettings().autoTickRate()) {
                        if (tickMs < 50 && level.tickRate > baseTickRate) {
                            val r = level.tickRate - 1
                            level.tickRate = r
                            if (r > baseTickRate) {
                                level.tickRateCounter = level.tickRate
                            }
                            Server.log.debug(
                                "Raising level \"{}\" tick rate to {} ticks",
                                level.getName(),
                                level.tickRate
                            )
                        } else if (tickMs >= 50) {
                            val autoTickRateLimit: Int = settings.levelSettings().autoTickRateLimit()
                            if (level.tickRate == baseTickRate) {
                                level.tickRate = max(
                                    (baseTickRate + 1).toDouble(),
                                    min(autoTickRateLimit.toDouble(), (tickMs / 50).toDouble())
                                ).toInt()
                                Server.log.debug(
                                    "Level \"{}\" took {}ms, setting tick rate to {} ticks",
                                    level.getName(),
                                    round(tickMs.toDouble(), 2),
                                    level.tickRate
                                )
                            } else if ((tickMs / level.tickRate) >= 50 && level.tickRate < autoTickRateLimit) {
                                level.tickRate = level.tickRate + 1
                                Server.log.debug(
                                    "Level \"{}\" took {}ms, setting tick rate to {} ticks",
                                    level.getName(),
                                    round(tickMs.toDouble(), 2),
                                    level.tickRate
                                )
                            }
                            level.tickRateCounter = level.tickRate
                        }
                    }
                } catch (e: Exception) {
                    Server.log.error(
                        language.tr(
                            "nukkit.level.tickError",
                            level.folderPath, getExceptionMessage(e)
                        ), e
                    )
                }
            }
        }
    }

    fun doAutoSave() {
        if (this.getAutoSave()) {
            for (player in ArrayList(players.values)) {
                if (player.isOnline) {
                    player.save(true)
                } else if (!player.isConnected) {
                    this.removePlayer(player)
                }
            }

            for (level in this.levelArray) {
                level.save()
            }
            scoreboardManager.save()
        }
    }

    private fun tick() {
        val tickTime = System.currentTimeMillis()

        val time = tickTime - this.nextTick
        if (time < -25) {
            try {
                Thread.sleep(max(5.0, (-time - 25).toDouble()).toLong())
            } catch (e: InterruptedException) {
                Server.log.debug("The thread {} got interrupted", Thread.currentThread().name, e)
            }
        }

        val tickTimeNano = System.nanoTime()
        if ((tickTime - this.nextTick) < -25) {
            return
        }

        ++this.tick
        network.processInterfaces()

        if (this.rcon != null) {
            rcon!!.check()
        }

        scheduler.mainThreadHeartbeat(this.tick)

        this.checkTickUpdates(this.tick)

        if ((this.tick and 15) == 0) {
            this.titleTick()
            this.maxTick = 20f
            this.maxUse = 0f

            if ((this.tick and 511) == 0) {
                try {
                    pluginManager.callEvent(QueryRegenerateEvent(this, 5).also { this.queryInformation = it })
                } catch (e: Exception) {
                    Server.log.error("", e)
                }
            }
        }

        if (this.autoSave && ++this.autoSaveTicker >= this.autoSaveTicks) {
            this.autoSaveTicker = 0
            this.doAutoSave()
        }

        if (this.sendUsageTicker > 0 && --this.sendUsageTicker == 0) {
            this.sendUsageTicker = 6000
            //todo sendUsage
        }

        // 处理可冻结数组
        val freezableArrayCompressTime = (50 - (System.currentTimeMillis() - tickTime)).toInt()
        if (freezableArrayCompressTime > 4) {
            freezableArrayManager.setMaxCompressionTime(freezableArrayCompressTime).tick()
        }

        val nowNano = System.nanoTime()
        val tick = min(20.0, 1000000000 / max(1000000.0, (nowNano.toDouble() - tickTimeNano))).toFloat()
        val use = min(1.0, ((nowNano - tickTimeNano).toDouble()) / 50000000).toFloat()

        if (this.maxTick > tick) {
            this.maxTick = tick
        }

        if (this.maxUse < use) {
            this.maxUse = use
        }

        System.arraycopy(
            this.tickAverage, 1, this.tickAverage, 0,
            tickAverage.size - 1
        )
        tickAverage[tickAverage.size - 1] = tick

        System.arraycopy(
            this.useAverage, 1, this.useAverage, 0,
            useAverage.size - 1
        )
        useAverage[useAverage.size - 1] = use

        if ((this.nextTick - tickTime) < -1000) {
            this.nextTick = tickTime
        } else {
            this.nextTick += 50
        }
    }

    val ticksPerSecond: Float
        get() = (Math.round(this.maxTick * 100).toFloat()) / 100

    val ticksPerSecondAverage: Float
        get() {
            var sum = 0f
            val count = tickAverage.size
            for (aTickAverage in this.tickAverage) {
                sum += aTickAverage
            }
            return round((sum / count).toDouble(), 2).toFloat()
        }

    val tickUsage: Float
        get() = round((this.maxUse * 100).toDouble(), 2).toFloat()

    val tickUsageAverage: Float
        get() {
            var sum = 0f
            val count = useAverage.size
            for (aUseAverage in this.useAverage) {
                sum += aUseAverage
            }
            return (Math.round(sum / count * 100).toFloat()) / 100
        }

    // TODO: Fix title tick
    fun titleTick() {
        if (!Nukkit.ANSI || !Nukkit.TITLE) {
            return
        }

        val runtime = Runtime.getRuntime()
        val used = round((runtime.totalMemory() - runtime.freeMemory()).toDouble() / 1024 / 1024, 2)
        val max = round((runtime.maxMemory().toDouble()) / 1024 / 1024, 2)
        val usage = Math.round(used / max * 100).toString() + "%"
        var title = (0x1b.toChar().toString() + "]0;" + this.name + " "
                + this.nukkitVersion
                + " | " + this.gitCommit
                + " | Online " + players.size + "/" + this.getMaxPlayers()
                + " | Memory " + usage)
        if (!Nukkit.shortTitle) {
            title += (" | U " + round((network.upload / 1024 * 1000), 2)
                    + " D " + round((network.download / 1024 * 1000), 2) + " kB/s")
        }
        title += (" | TPS " + this.ticksPerSecond
                + " | Load " + this.tickUsage + "%" + 0x07.toChar())

        print(title)
    }

    fun isRunning(): Boolean {
        return isRunning.get()
    }

    /**
     * 将服务器设置为繁忙状态，这可以阻止相关代码认为服务器处于无响应状态。
     * 请牢记，必须在设置之后清除。
     *
     * @param busyTime 单位为毫秒
     * @return id
     */
    fun addBusying(busyTime: Long): Int {
        busyingTime.add(busyTime)
        return busyingTime.size - 1
    }

    fun removeBusying(index: Int) {
        busyingTime.removeLong(index)
    }

    fun getBusyingTime(): Long {
        if (busyingTime.isEmpty()) {
            return -1
        }
        return busyingTime.getLong(busyingTime.size - 1)
    }

    // endregion
    // region chat & commands - 聊天与命令
    /**
     * 广播一条消息给所有玩家
     *
     *Broadcast a message to all players
     *
     * @param message 消息
     * @return int 玩家数量<br></br>Number of players
     */
    fun broadcastMessage(message: String): Int {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS)
    }

    /**
     * @see .broadcastMessage
     */
    fun broadcastMessage(message: TextContainer?): Int {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS)
    }

    /**
     * 广播一条消息给指定的[recipients][CommandSender]
     *
     *Broadcast a message to the specified [recipients][CommandSender]
     *
     * @param message 消息
     * @return int [recipients][CommandSender]数量<br></br>Number of [recipients][CommandSender]
     */
    fun broadcastMessage(message: String, recipients: Array<CommandSender>): Int {
        for (recipient in recipients) {
            recipient.sendMessage(message)
        }

        return recipients.size
    }

    /**
     * @see .broadcastMessage
     */
    fun broadcastMessage(message: String, recipients: Collection<CommandSender>): Int {
        for (recipient in recipients) {
            recipient.sendMessage(message)
        }

        return recipients.size
    }

    /**
     * @see .broadcastMessage
     */
    fun broadcastMessage(message: TextContainer?, recipients: Collection<CommandSender>): Int {
        for (recipient in recipients) {
            recipient.sendMessage(message)
        }

        return recipients.size
    }

    /**
     * 从指定的许可名获取发送者们，广播一条消息给他们.可以指定多个许可名，以** ; **分割.<br></br>
     * 一个permission在`PluginManager#permSubs`对应一个[发送者][CommandSender]Set.
     *
     *
     * Get the sender to broadcast a message from the specified permission name, multiple permissions can be specified, split by ** ; **<br></br>
     * The permission corresponds to a [Sender][CommandSender] set in `PluginManager#permSubs`.
     *
     * @param message     消息内容<br></br>Message content
     * @param permissions 许可名，需要先通过[subscribeToPermission][PluginManager.subscribeToPermission]注册<br></br>Permissions name, need to register first through [subscribeToPermission][PluginManager.subscribeToPermission]
     * @return int 接受到消息的[发送者][CommandSender]数量<br></br>Number of [senders][CommandSender] who received the message
     */
    fun broadcast(message: String, permissions: String): Int {
        val recipients: MutableSet<CommandSender> = HashSet()

        for (permission in permissions.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            for (permissible in pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible is CommandSender && permissible.hasPermission(permission)) {
                    recipients.add(permissible)
                }
            }
        }

        for (recipient in recipients) {
            recipient.sendMessage(message)
        }

        return recipients.size
    }

    /**
     * @see .broadcast
     */
    fun broadcast(message: TextContainer?, permissions: String): Int {
        val recipients: MutableSet<CommandSender> = HashSet()

        for (permission in permissions.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            for (permissible in pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible is CommandSender && permissible.hasPermission(permission)) {
                    recipients.add(permissible)
                }
            }
        }

        for (recipient in recipients) {
            recipient.sendMessage(message)
        }

        return recipients.size
    }

    /**
     * 以sender身份执行一行命令
     *
     *
     * Execute one line of command as sender
     *
     * @param sender      命令执行者
     * @param commandLine 一行命令
     * @return 返回0代表执行失败, 返回大于等于1代表执行成功<br></br>Returns 0 for failed execution, greater than or equal to 1 for successful execution
     * @throws ServerException 服务器异常
     */
    @Throws(ServerException::class)
    fun executeCommand(sender: CommandSender, commandLine: String): Int {
        // First we need to check if this command is on the main thread or not, if not, merge it in the main thread.
        if (!this.isPrimaryThread()) {
            scheduler.scheduleTask(null) { executeCommand(sender, commandLine) }
            return 1
        }
        if (sender == null) {
            throw ServerException("CommandSender is not valid")
        }

        var cmd = commandLine.trimStart()
        if (cmd.isEmpty()) {
            return 0
        }
        cmd = if (cmd[0] == '/') cmd.substring(1) else cmd

        return commandMap.executeCommand(sender, cmd)
    }

    /**
     * 以该控制台身份静音执行这些命令，无视权限
     *
     *
     * Execute these commands silently as the console, ignoring permissions.
     *
     * @param commands the commands
     * @throws ServerException 服务器异常
     */
    fun silentExecuteCommand(vararg commands: String?) {
        this.silentExecuteCommand(null, *commands)
    }

    /**
     * 以该玩家身份静音执行这些命令无视权限
     *
     *
     * Execute these commands silently as this player, ignoring permissions.
     *
     * @param sender   命令执行者<br></br>command sender
     * @param commands the commands
     * @throws ServerException 服务器异常
     */
    fun silentExecuteCommand(sender: Player?, vararg commands: String) {
        val revert = ArrayList<cn.nukkit.level.Level>()
        val server = instance
        for (level in server!!.levels.values) {
            if (level.gameRules!!.getBoolean(GameRule.SEND_COMMAND_FEEDBACK)) {
                level.gameRules!!.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false)
                revert.add(level)
            }
        }
        if (sender == null) {
            for (cmd in commands) {
                server.executeCommand(server.consoleSender, cmd)
            }
        } else {
            for (cmd in commands) {
                server.executeCommand(server.consoleSender, "execute as " + "\"" + sender.getName() + "\" run " + cmd)
            }
        }

        for (level in revert) {
            level.gameRules!!.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true)
        }
    }

    fun getPluginCommand(name: String): PluginIdentifiableCommand? {
        val command = commandMap.getCommand(name)
        return if (command is PluginIdentifiableCommand) {
            command
        } else {
            null
        }
    }

    // endregion
    // region plugins - 插件相关
    /**
     * 以指定插件加载顺序启用插件
     *
     *
     * Enable plugins in the specified plugin loading order
     *
     * @param type 插件加载顺序<br></br>Plugin loading order
     */
    fun enablePlugins(type: PluginLoadOrder) {
        for (plugin in ArrayList<Plugin>(pluginManager.getPlugins().values)) {
            if (!plugin.isEnabled && type == plugin.description.getOrder()) {
                this.enablePlugin(plugin)
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            registerCorePermissions()
        }
    }

    /**
     * 启用一个指定插件
     *
     *
     * Enable a specified plugin
     *
     * @param plugin 插件实例<br></br>Plugin instance
     */
    fun enablePlugin(plugin: Plugin) {
        pluginManager.enablePlugin(plugin)
    }

    /**
     * 禁用全部插件
     *
     *Disable all plugins
     */
    fun disablePlugins() {
        pluginManager.disablePlugins()
    }

    // endregion
    // region Players - 玩家相关
    fun onPlayerCompleteLoginSequence(player: Player) {
        this.sendFullPlayerListData(player)
    }

    fun onPlayerLogin(socketAddress: InetSocketAddress?, player: Player) {
        val ev: PlayerLoginEvent
        pluginManager.callEvent(PlayerLoginEvent(player, "Plugin reason").also { ev = it })
        if (ev.isCancelled) {
            player.close(player.leaveMessage, ev.kickMessage)
            return
        }

        players[socketAddress] = player
        if (this.sendUsageTicker > 0) {
            uniquePlayers.add(player.getUniqueId()!!)
        }
    }

    @ApiStatus.Internal
    fun addOnlinePlayer(player: Player) {
        playerList[player.getUniqueId()!!] = player
        this.updatePlayerListData(
            player.getUniqueId(),
            player.getId(),
            player.getDisplayName(),
            player.getSkin(),
            player.getLoginChainData().xUID
        )
        network.pong!!.playerCount(playerList.size).update()
    }

    @ApiStatus.Internal
    fun removeOnlinePlayer(player: Player) {
        if (playerList.containsKey(player.getUniqueId())) {
            playerList.remove(player.getUniqueId())

            val pk = PlayerListPacket()
            pk.type = PlayerListPacket.TYPE_REMOVE
            pk.entries = arrayOf(
                PlayerListPacket.Entry(
                    player.getUniqueId()!!
                )
            )

            broadcastPacket(playerList.values, pk)
            network.pong!!.playerCount(playerList.size).update()
        }
    }

    /**
     * @see .updatePlayerListData
     */
    fun updatePlayerListData(uuid: UUID, entityId: Long, name: String, skin: Skin, players: Array<Player>) {
        this.updatePlayerListData(uuid, entityId, name, skin, "", players)
    }

    /**
     * 更新指定玩家们(players)的[PlayerListPacket]数据包(即玩家列表数据)
     *
     *
     * Update [PlayerListPacket] data packets (i.e. player list data) for specified players
     *
     * @param uuid       uuid
     * @param entityId   实体id
     * @param name       名字
     * @param skin       皮肤
     * @param xboxUserId xbox用户id
     * @param players    指定接受数据包的玩家
     */
    fun updatePlayerListData(
        uuid: UUID,
        entityId: Long,
        name: String,
        skin: Skin,
        xboxUserId: String?,
        players: Array<Player>
    ) {
        // In some circumstances, the game sends confidential data in this string,
        // so under no circumstances should it be sent to all players on the server.
        // @Zwuiix
        skin.setSkinId("")

        val pk = PlayerListPacket()
        pk.type = PlayerListPacket.TYPE_ADD
        pk.entries = arrayOf(PlayerListPacket.Entry(uuid, entityId, name, skin, xboxUserId))
        broadcastPacket(players, pk)
    }

    /**
     * @see .updatePlayerListData
     */
    /**
     * @see .updatePlayerListData
     */
    /**
     * @see .updatePlayerListData
     */
    @JvmOverloads
    fun updatePlayerListData(
        uuid: UUID,
        entityId: Long,
        name: String,
        skin: Skin,
        xboxUserId: String? = "",
        players: Collection<Player> = playerList.values
    ) {
        // In some circumstances, the game sends confidential data in this string,
        // so under no circumstances should it be sent to all players on the server.
        // @Zwuiix
        skin.setSkinId("")
        this.updatePlayerListData(
            uuid,
            entityId,
            name,
            skin,
            xboxUserId,
            players.toArray<Player>(Player.Companion.EMPTY_ARRAY)
        )
    }

    /**
     * 移除玩家数组中所有玩家的玩家列表数据.
     *
     *
     * Remove player list data for all players in the array.
     *
     * @param players 玩家数组
     */
    fun removePlayerListData(uuid: UUID, players: Array<Player>) {
        val pk = PlayerListPacket()
        pk.type = PlayerListPacket.TYPE_REMOVE
        pk.entries = arrayOf(PlayerListPacket.Entry(uuid))
        broadcastPacket(players, pk)
    }

    /**
     * 移除这个玩家的玩家列表数据.
     *
     *
     * Remove this player's player list data.
     *
     * @param player 玩家
     */
    fun removePlayerListData(uuid: UUID, player: Player) {
        val pk = PlayerListPacket()
        pk.type = PlayerListPacket.TYPE_REMOVE
        pk.entries = arrayOf(PlayerListPacket.Entry(uuid))
        player.dataPacket(pk)
    }

    @JvmOverloads
    fun removePlayerListData(uuid: UUID, players: Collection<Player> = playerList.values) {
        this.removePlayerListData(uuid, players.toArray<Player>(Player.Companion.EMPTY_ARRAY))
    }

    /**
     * 发送玩家列表数据包给一个玩家.
     *
     *
     * Send a player list packet to a player.
     *
     * @param player 玩家
     */
    fun sendFullPlayerListData(player: Player) {
        val pk = PlayerListPacket()
        pk.type = PlayerListPacket.TYPE_ADD
        pk.entries = playerList.values.stream()
            .map<PlayerListPacket.Entry> { p: Player ->
                PlayerListPacket.Entry(
                    p.getUniqueId()!!,
                    p.getId(),
                    p.getDisplayName(),
                    p.getSkin()!!,
                    p.getLoginChainData().xUID
                )
            }
            .toArray<PlayerListPacket.Entry?> { _Dummy_.__Array__() }

        player.dataPacket(pk)
    }

    /**
     * 从指定的UUID得到玩家实例.
     *
     *
     * Get the player instance from the specified UUID.
     *
     * @param uuid uuid
     * @return 玩家实例，可为空<br></br>Player example, can be empty
     */
    fun getPlayer(uuid: UUID): Optional<Player> {
        Preconditions.checkNotNull(uuid, "uuid")
        return Optional.ofNullable(playerList[uuid])
    }

    /**
     * 从数据库中查找指定玩家名对应的UUID.
     *
     *
     * Find the UUID corresponding to the specified player name from the database.
     *
     * @param name 玩家名<br></br>player name
     * @return 玩家的UUID，可为空.<br></br>The player's UUID, which can be empty.
     */
    fun lookupName(name: String): Optional<UUID> {
        val nameBytes = name.lowercase().toByteArray(StandardCharsets.UTF_8)
        val uuidBytes = playerDataDB!![nameBytes] ?: return Optional.empty()

        if (uuidBytes.size != 16) {
            Server.log.warn("Invalid uuid in name lookup database detected! Removing")
            playerDataDB.delete(nameBytes)
            return Optional.empty()
        }

        val buffer = ByteBuffer.wrap(uuidBytes)
        return Optional.of(UUID(buffer.getLong(), buffer.getLong()))
    }

    /**
     * 更新数据库中指定玩家名的UUID，若不存在则添加.
     *
     *
     * Update the UUID of the specified player name in the database, or add it if it does not exist.
     *
     * @param info the player info
     */
    fun updateName(info: PlayerInfo) {
        val uniqueId = info.getUniqueId()
        val name = info.getUsername()

        val nameBytes: ByteArray = name.toLowerCase(Locale.ENGLISH).getBytes(StandardCharsets.UTF_8)

        val buffer = ByteBuffer.allocate(16)
        buffer.putLong(uniqueId.getMostSignificantBits())
        buffer.putLong(uniqueId.getLeastSignificantBits())
        val array = buffer.array()
        val bytes = playerDataDB!![array]
        if (bytes == null) {
            playerDataDB.put(nameBytes, array)
        }
        val xboxAuthEnabled = properties.get(ServerPropertiesKeys.XBOX_AUTH, false)
        if (info is XboxLivePlayerInfo || !xboxAuthEnabled) {
            playerDataDB.put(nameBytes, array)
        }
    }

    fun getOfflinePlayer(name: String): IPlayer {
        val result: IPlayer? = this.getPlayerExact(name.lowercase())
        if (result != null) {
            return result
        }

        return lookupName(name).map { uuid: UUID? -> OfflinePlayer(this, uuid) }
            .orElse(OfflinePlayer(this, name))
    }

    /**
     * 从指定的UUID得到一个玩家实例,可以是在线玩家也可以是离线玩家.
     *
     *
     * Get a player instance from the specified UUID, either online or offline.
     *
     * @param uuid uuid
     * @return 玩家<br></br>player
     */
    fun getOfflinePlayer(uuid: UUID): IPlayer {
        Preconditions.checkNotNull(uuid, "uuid")
        val onlinePlayer = getPlayer(uuid)
        if (onlinePlayer.isPresent) {
            return onlinePlayer.get()
        }

        return OfflinePlayer(this, uuid)
    }

    /**
     * create为false
     *
     *
     * create is false
     *
     * @see .getOfflinePlayerData
     */
    fun getOfflinePlayerData(uuid: UUID?): CompoundTag? {
        return getOfflinePlayerData(uuid, false)
    }

    /**
     * 获得UUID指定的玩家的NBT数据
     *
     * @param uuid   要获取数据的玩家UUID<br></br>UUID of the player to get data from
     * @param create 如果玩家数据不存在是否创建<br></br>If player data does not exist whether to create.
     * @return [CompoundTag]
     */
    fun getOfflinePlayerData(uuid: UUID?, create: Boolean): CompoundTag? {
        return getOfflinePlayerDataInternal(uuid, create)
    }

    fun getOfflinePlayerData(name: String): CompoundTag? {
        return getOfflinePlayerData(name, false)
    }

    fun getOfflinePlayerData(name: String, create: Boolean): CompoundTag? {
        val uuid = lookupName(name)
        if (uuid.isEmpty) {
            Server.log.warn("Invalid uuid in name lookup database detected! Removing")
            playerDataDB!!.delete(name.toByteArray(StandardCharsets.UTF_8))
            return null
        }
        return getOfflinePlayerDataInternal(uuid.get(), create)
    }

    fun hasOfflinePlayerData(name: String): Boolean {
        val uuid = lookupName(name)
        if (uuid.isEmpty) {
            Server.log.warn("Invalid uuid in name lookup database detected! Removing")
            playerDataDB!!.delete(name.toByteArray(StandardCharsets.UTF_8))
            return false
        }
        return hasOfflinePlayerData(uuid.get())
    }

    fun hasOfflinePlayerData(uuid: UUID): Boolean {
        val buffer = ByteBuffer.allocate(16)
        buffer.putLong(uuid.mostSignificantBits)
        buffer.putLong(uuid.leastSignificantBits)
        val bytes = playerDataDB!![buffer.array()]
        return bytes != null
    }

    private fun getOfflinePlayerDataInternal(uuid: UUID?, create: Boolean): CompoundTag? {
        if (uuid == null) {
            Server.log.error("UUID is empty, cannot query player data")
            return null
        }
        try {
            val buffer = ByteBuffer.allocate(16)
            buffer.putLong(uuid.mostSignificantBits)
            buffer.putLong(uuid.leastSignificantBits)
            val bytes = playerDataDB!![buffer.array()]
            if (bytes != null) {
                return readCompressed(bytes)
            }
        } catch (e: IOException) {
            Server.log.warn(language.tr("nukkit.data.playerCorrupted", uuid), e)
        }

        if (create) {
            if (settings.playerSettings().savePlayerData()) {
                Server.log.info(language.tr("nukkit.data.playerNotFound", uuid))
            }
            val spawn = getDefaultLevel()!!.safeSpawn
            val nbt = CompoundTag()
                .putLong("firstPlayed", System.currentTimeMillis() / 1000)
                .putLong("lastPlayed", System.currentTimeMillis() / 1000)
                .putList(
                    "Pos", ListTag<FloatTag?>()
                        .add(FloatTag(spawn.position.x))
                        .add(FloatTag(spawn.position.y))
                        .add(FloatTag(spawn.position.z))
                )
                .putString("Level", getDefaultLevel()!!.getName()!!)
                .putList("Inventory", ListTag())
                .putCompound("Achievements", CompoundTag())
                .putInt("playerGameType", this.gamemode)
                .putList(
                    "Motion", ListTag<FloatTag?>()
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                )
                .putList(
                    "Rotation", ListTag<FloatTag?>()
                        .add(FloatTag(0f))
                        .add(FloatTag(0f))
                )
                .putFloat("FallDistance", 0f)
                .putShort("Fire", 0)
                .putShort("Air", 300)
                .putBoolean("OnGround", true)
                .putBoolean("Invulnerable", false)

            this.saveOfflinePlayerData(uuid, nbt!!, true)
            return nbt
        } else {
            Server.log.error("Player {} does not exist and cannot read playerdata", uuid)
            return null
        }
    }

    /**
     * @see .saveOfflinePlayerData
     */
    /**
     * @see .saveOfflinePlayerData
     */
    @JvmOverloads
    fun saveOfflinePlayerData(uuid: UUID, tag: CompoundTag, async: Boolean = false) {
        this.saveOfflinePlayerData(uuid.toString(), tag, async)
    }

    /**
     * @see .saveOfflinePlayerData
     */
    fun saveOfflinePlayerData(name: String, tag: CompoundTag) {
        this.saveOfflinePlayerData(name, tag, false)
    }

    /**
     * 保存玩家数据，玩家在线离线都行.
     *
     *
     * Save player data, players can be offline.
     *
     * @param nameOrUUid the name or uuid
     * @param tag        NBT数据<br></br>nbt data
     * @param async      是否异步保存<br></br>Whether to save asynchronously
     */
    fun saveOfflinePlayerData(nameOrUUid: String, tag: CompoundTag, async: Boolean) {
        val uuid = lookupName(nameOrUUid).orElse(UUID.fromString(nameOrUUid))
        if (settings.playerSettings().savePlayerData()) {
            scheduler.scheduleTask(InternalPlugin.INSTANCE, object : Task() {
                val hasRun: AtomicBoolean = AtomicBoolean(false)

                override fun onRun(currentTick: Int) {
                    this.onCancel()
                }

                //doing it like this ensures that the playerdata will be saved in a server shutdown
                override fun onCancel() {
                    if (!hasRun.getAndSet(true)) {
                        saveOfflinePlayerDataInternal(tag, uuid)
                    }
                }
            }, async)
        }
    }

    private fun saveOfflinePlayerDataInternal(tag: CompoundTag, uuid: UUID) {
        try {
            val bytes = writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)
            val buffer = ByteBuffer.allocate(16)
            buffer.putLong(uuid.mostSignificantBits)
            buffer.putLong(uuid.leastSignificantBits)
            playerDataDB!!.put(buffer.array(), bytes)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    /**
     * 从玩家名获得一个在线玩家，这个方法是模糊匹配，只要玩家名带有name前缀就会被返回.
     *
     *
     * Get an online player from the player name, this method is a fuzzy match and will be returned as long as the player name has the name prefix.
     *
     * @param name 玩家名<br></br>player name
     * @return 玩家实例对象，获取失败为null<br></br>Player instance object,failed to get null
     */
    fun getPlayer(name: String): Player? {
        var name = name
        var found: Player? = null
        name = name.lowercase()
        var delta = Int.MAX_VALUE
        for (player in onlinePlayers.values) {
            if (player.getName().lowercase().startsWith(name)) {
                val curDelta: Int = player.getName().length - name.length
                if (curDelta < delta) {
                    found = player
                    delta = curDelta
                }
                if (curDelta == 0) {
                    break
                }
            }
        }

        return found
    }

    /**
     * 从玩家名获得一个在线玩家，这个方法是精确匹配，当玩家名字符串完全相同时返回.
     *
     *
     * Get an online player from a player name, this method is an exact match and returns when the player name string is identical.
     *
     * @param name 玩家名<br></br>player name
     * @return 玩家实例对象，获取失败为null<br></br>Player instance object,failed to get null
     */
    fun getPlayerExact(name: String): Player? {
        var name = name
        name = name.lowercase()
        for (player in onlinePlayers.values) {
            if (player.getName().lowercase() == name) {
                return player
            }
        }

        return null
    }

    /**
     * 指定一个部分玩家名，返回所有包含或者等于该名称的玩家.
     *
     *
     * Specify a partial player name and return all players with or equal to that name.
     *
     * @param partialName 部分玩家名<br></br>partial name
     * @return 匹配到的所有玩家, 若匹配不到则为一个空数组<br></br>All players matched, if not matched then an empty array
     */
    fun matchPlayer(partialName: String): Array<Player> {
        var partialName = partialName
        partialName = partialName.lowercase()
        val matchedPlayer: MutableList<Player> = ArrayList()
        for (player in onlinePlayers.values) {
            if (player.getName().lowercase() == partialName) {
                return arrayOf(player)
            } else if (player.getName().lowercase().contains(partialName)) {
                matchedPlayer.add(player)
            }
        }

        return matchedPlayer.toArray<Player>(Player.Companion.EMPTY_ARRAY)
    }

    @ApiStatus.Internal
    fun removePlayer(player: Player) {
        val toRemove = players.remove(player.getRawSocketAddress())
        if (toRemove != null) {
            return
        }

        for (socketAddress in ArrayList(players.keys)) {
            val p = players[socketAddress]
            if (player === p) {
                players.remove(socketAddress)
                break
            }
        }
    }

    val onlinePlayers: Map<UUID, Player>
        /**
         * 获得所有在线的玩家Map.
         *
         *
         * Get all online players Map.
         *
         * @return 所有的在线玩家Map
         */
        get() = ImmutableMap.copyOf(playerList)

    // endregion
    // region constants - 常量
    val name: String
        /**
         * @return 服务器名称<br></br>The name of server
         */
        get() = "PowerNukkitX"

    val nukkitVersion: String?
        get() = Nukkit.VERSION

    val bStatsNukkitVersion: String?
        get() = Nukkit.VERSION

    val gitCommit: String
        get() = Nukkit.GIT_COMMIT

    val codename: String
        get() = Nukkit.CODENAME

    val version: String
        get() = ProtocolInfo.MINECRAFT_VERSION

    val apiVersion: String
        get() = Nukkit.API_VERSION

    val logger: MainLogger
        get() = MainLogger.logger

    fun getPositionTrackingService(): PositionTrackingService {
        return positionTrackingService!!
    }

    // region crafting & recipe - 合成与配方
    /**
     * 发送配方列表数据包给一个玩家.
     *
     *
     * Send a recipe list packet to a player.
     *
     * @param player 玩家
     */
    fun sendRecipeList(player: Player) {
        player.getSession().sendRawPacket(ProtocolInfo.CRAFTING_DATA_PACKET, Registries.RECIPE.craftingPacket)
    }

    /**
     * 注册配方到配方管理器
     *
     *
     * Register Recipe to Recipe Manager
     *
     * @param recipe 配方
     */
    fun addRecipe(recipe: Recipe) {
        Registries.RECIPE.register(recipe)
    }

    val recipeRegistry: RecipeRegistry
        get() = Registries.RECIPE

    // endregion
    // region Levels - 游戏世界相关

    /**
     * @return Get the default overworld
     */
    fun getDefaultLevel(): cn.nukkit.level.Level? {
        return defaultLevel
    }

    /**
     * Set default overworld
     */
    fun setDefaultLevel(defaultLevel: cn.nukkit.level.Level?) {
        if (defaultLevel == null || (this.isLevelLoaded(defaultLevel.getName()!!) && defaultLevel != this.defaultLevel)) {
            this.defaultLevel = defaultLevel
        }
    }

    var defaultNetherLevel: cn.nukkit.level.Level?
        /**
         * @return Get the default nether
         */
        get() = defaultNether
        /**
         * Set default nether
         */
        set(defaultLevel) {
            if (defaultLevel == null || (this.isLevelLoaded(defaultLevel.getName()!!) && defaultLevel != this.defaultNether)) {
                this.defaultNether = defaultLevel
            }
        }

    var defaultEndLevel: cn.nukkit.level.Level?
        /**
         * @return Get the default the_end level
         */
        get() = defaultLevel
        /**
         * Set default the_end level
         */
        set(defaultLevel) {
            if (defaultLevel == null || (this.isLevelLoaded(defaultLevel.getName()!!) && defaultLevel != this.defaultEnd)) {
                this.defaultEnd = defaultLevel
            }
        }

    /** */
    init {
        var predefinedLanguage = predefinedLanguage
        Preconditions.checkState(instance == null, "Already initialized!")
        launchTime = System.currentTimeMillis()
        currentThread =
            Thread.currentThread() // Saves the current thread instance as a reference, used in Server#isPrimaryThread()
        instance = this

        this.filePath = filePath
        if (!File(dataPath + "worlds/").exists()) {
            File(dataPath + "worlds/").mkdirs()
        }
        if (!File(dataPath + "players/").exists()) {
            File(dataPath + "players/").mkdirs()
        }
        if (!File(pluginPath).exists()) {
            File(pluginPath).mkdirs()
        }
        this.dataPath = File(dataPath).absolutePath + "/"
        this.pluginPath = File(pluginPath).absolutePath + "/"
        val commandDataPath = File(dataPath).absolutePath + "/command_data"
        if (!File(commandDataPath).exists()) {
            File(commandDataPath).mkdirs()
        }

        this.console = NukkitConsole(this)
        this.consoleThread = ConsoleThread()
        consoleThread.start()

        val config = File(this.dataPath + "nukkit.yml")
        var chooseLanguage: String? = null
        if (!config.exists()) {
            Server.log.info("{}Welcome! Please choose a language first!", TextFormat.GREEN)
            try {
                val languageList = javaClass.module.getResourceAsStream("language/language.list")
                checkNotNull(languageList) { "language/language.list is missing. If you are running a development version, make sure you have run 'git submodule update --init'." }
                val lines = readFile(languageList).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (line in lines) {
                    Server.log.info(line)
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            while (chooseLanguage == null) {
                val lang: String
                if (predefinedLanguage != null) {
                    Server.log.info("Trying to load language from predefined language: {}", predefinedLanguage)
                    lang = predefinedLanguage
                } else {
                    lang = console.readLine()
                }

                try {
                    javaClass.classLoader.getResourceAsStream("language/$lang/lang.json").use { conf ->
                        if (conf != null) {
                            chooseLanguage = lang
                        } else if (predefinedLanguage != null) {
                            Server.log.warn(
                                "No language found for predefined language: {}, please choose a valid language",
                                predefinedLanguage
                            )
                            predefinedLanguage = null
                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        } else {
            val configInstance = Config(config)
            chooseLanguage = configInstance.getString("settings.language", "eng")
        }
        this.baseLang = BaseLang(chooseLanguage!!)
        this.language = mapInternalLang(chooseLanguage!!)
        Server.log.info("Loading {}...", TextFormat.GREEN.toString() + "nukkit.yml" + TextFormat.RESET)
        this.settings = ConfigManager.create<ServerSettings>(
            ServerSettings::class.java
        ) { it: OkaeriConfig ->
            it.withConfigurer(YamlSnakeYamlConfigurer())
            it.withBindFile(config)
            it.withRemoveOrphans(true)
            it.saveDefaults()
            it.load(true)
        }
        settings.baseSettings().language(chooseLanguage)

        this.computeThreadPool = ForkJoinPool(
            min(
                TODO("Could not convert double literal '0x7fff' to Kotlin"),
                Runtime.getRuntime().availableProcessors().toDouble()
            ).toInt(), ComputeThreadPoolThreadFactory(), null, false
        )

        levelArray = cn.nukkit.level.Level.EMPTY_ARRAY

        val targetLevel = Level.getLevel(settings.debugSettings().level())
        val currentLevel = Nukkit.getLogLevel()
        if (targetLevel != null && targetLevel.intLevel() > currentLevel!!.intLevel()) {
            Nukkit.setLogLevel(targetLevel)
        }

        Server.log.info("Loading {}...", TextFormat.GREEN.toString() + "server.properties" + TextFormat.RESET)
        this.properties = ServerProperties(this.dataPath)

        val isShaded = isShaded
        if (!isValidStart || (JarStart.isUsingJavaJar() && !isShaded)) {
            Server.log.error(language.tr("nukkit.start.invalid"))
            return
        }
        if (!properties.get(ServerPropertiesKeys.ALLOW_SHADED, false) && isShaded) {
            Server.log.error(language.tr("nukkit.start.shaded1"))
            Server.log.error(language.tr("nukkit.start.shaded2"))
            Server.log.error(language.tr("nukkit.start.shaded3"))
            return
        }

        this.allowNether = properties.get(ServerPropertiesKeys.ALLOW_NETHER, true)
        this.isNetherAllowed = properties.get(ServerPropertiesKeys.ALLOW_THE_END, true)
        this.useTerra = properties.get(ServerPropertiesKeys.USE_TERRA, false)
        this.checkLoginTime = properties.get(ServerPropertiesKeys.CHECK_LOGIN_TIME, false)

        Server.log.info(language.tr("language.selected", language.name, language.getLang()))
        Server.log.info(
            language.tr(
                "nukkit.server.start",
                TextFormat.AQUA.toString() + this.version + TextFormat.RESET
            )
        )

        val poolSize: String = settings.baseSettings().asyncWorkers()
        var poolSizeNumber = try {
            poolSize.toInt()
        } catch (e: Exception) {
            max(Runtime.getRuntime().availableProcessors().toDouble(), 4.0).toInt()
        }
        ServerScheduler.WORKERS = poolSizeNumber
        this.scheduler = ServerScheduler()

        setProvider(settings.networkSettings().zlibProvider())

        this.serverAuthoritativeMovementMode =
            when (properties.get(ServerPropertiesKeys.SERVER_AUTHORITATIVE_MOVEMENT, "server-auth")) {
                "client-auth" -> 0
                "server-auth" -> 1
                "server-auth-with-rewind" -> 2
                else -> throw IllegalArgumentException()
            }
        this.enabledNetworkEncryption =
            properties.get(ServerPropertiesKeys.NETWORK_ENCRYPTION, true)
        if (settings.baseSettings().waterdogpe()) {
            this.checkLoginTime = false
        }

        if (properties.get(ServerPropertiesKeys.ENABLE_RCON, false)) {
            try {
                this.rcon = RCON(
                    this,
                    properties.get(ServerPropertiesKeys.RCON_PASSWORD, ""), if (this.ip != "") ip else "0.0.0.0",
                    port
                )
            } catch (e: IllegalArgumentException) {
                Server.log.error(language.tr(e.message, e.cause!!.message))
            }
        }
        this.entityMetadata = EntityMetadataStore()
        this.playerMetadata = PlayerMetadataStore()
        this.levelMetadata = LevelMetadataStore()
        this.iPBans = Config(this.dataPath + "ops.txt", Config.ENUM)
        this.ops = Config(this.dataPath + "white-list.txt", Config.ENUM)
        this.banByName = BanList(this.dataPath + "banned-players.json")
        banByName.load()
        this.nameBans = BanList(this.dataPath + "banned-ips.json")
        nameBans.load()
        this.maxPlayers = properties.get(ServerPropertiesKeys.MAX_PLAYERS, 20)
        this.setAutoSave(properties.get(ServerPropertiesKeys.AUTO_SAVE, true))
        if (properties.get(ServerPropertiesKeys.HARDCORE, false) && this.getDifficulty() < 3) {
            properties.get(ServerPropertiesKeys.DIFFICULTY, 3)
        }

        Server.log.info(
            language.tr(
                "nukkit.server.info",
                name,
                TextFormat.YELLOW.toString() + this.nukkitVersion + TextFormat.RESET + " (" + TextFormat.YELLOW + this.gitCommit + TextFormat.RESET + ")" + TextFormat.RESET,
                apiVersion
            )
        )
        Server.log.info(language.tr("nukkit.server.license"))
        this.consoleSender = ConsoleCommandSender()

        // Initialize metrics
        NukkitMetrics.startNow(this)

        run {
            //init
            Registries.POTION.init()
            Registries.PACKET.init()
            Registries.ENTITY.init()
            Registries.BLOCKENTITY.init()
            Registries.BLOCKSTATE_ITEMMETA.init()
            Registries.ITEM_RUNTIMEID.init()
            Registries.BLOCK.init()
            Registries.BLOCKSTATE.init()
            Registries.ITEM.init()
            Registries.CREATIVE.init()
            Registries.BIOME.init()
            Registries.FUEL.init()
            Registries.GENERATOR.init()
            Registries.GENERATE_STAGE.init()
            Registries.EFFECT.init()
            Registries.RECIPE.init()
            Profession.init()
            val a = BlockTags.ACACIA
            val b = ItemTags.ARROW
            val c = BiomeTags.WARM
            val d = BlockStateUpdaterBase.INSTANCE
            Enchantment.init()
            Attribute.init()
            BlockComposter.init()
            DispenseBehaviorRegister.init()
        }

        if (useTerra) { //load terra
            val instance = PNXPlatform.instance
        }

        freezableArrayManager = FreezableArrayManager(
            settings.freezeArraySettings().enable(),
            settings.freezeArraySettings().slots(),
            settings.freezeArraySettings().defaultTemperature(),
            settings.freezeArraySettings().freezingPoint(),
            settings.freezeArraySettings().absoluteZero(),
            settings.freezeArraySettings().boilingPoint(),
            settings.freezeArraySettings().melting(),
            settings.freezeArraySettings().singleOperation(),
            settings.freezeArraySettings().batchOperation()
        )
        scoreboardManager = ScoreboardManager(JSONScoreboardStorage("$commandDataPath/scoreboard.json"))
        functionManager = FunctionManager("$commandDataPath/functions")
        tickingAreaManager = SimpleTickingAreaManager(JSONTickingAreaStorage(this.dataPath + "worlds/"))

        // Convert legacy data before plugins get the chance to mess with it.
        try {
            playerDataDB = Iq80DBFactory.factory.open(
                File(dataPath, "players"), Options()
                    .createIfMissing(true)
                    .compressionType(CompressionType.ZLIB_RAW)
            )
        } catch (e: IOException) {
            Server.log.error("", e)
            System.exit(1)
        }
        this.resourcePackManager = ResourcePackManager(
            ZippedResourcePackLoader(File(Nukkit.DATA_PATH, "resource_packs")),
            JarPluginResourcePackLoader(File(this.pluginPath))
        )
        this.commandMap = SimpleCommandMap(this)
        this.pluginManager = PluginManager(this, this.commandMap)
        pluginManager.subscribeToPermission(BROADCAST_CHANNEL_ADMINISTRATIVE, this.consoleSender)
        pluginManager.registerInterface(JavaPluginLoader::class.java)
        console.setExecutingCommands(true)

        try {
            Server.log.debug("Loading position tracking service")
            this.positionTrackingService =
                PositionTrackingService(File(Nukkit.DATA_PATH, "services/position_tracking_db"))
        } catch (e: IOException) {
            Server.log.error("Failed to start the Position Tracking DB service!", e)
        }
        pluginManager.loadInternalPlugin()

        this.languageCode = UUID.randomUUID()
        pluginManager.loadPlugins(this.pluginPath)
        run {
            //trim
            Registries.POTION.trim()
            Registries.PACKET.trim()
            Registries.ENTITY.trim()
            Registries.BLOCKENTITY.trim()
            Registries.BLOCKSTATE_ITEMMETA.trim()
            Registries.BLOCKSTATE.trim()
            Registries.ITEM_RUNTIMEID.trim()
            Registries.BLOCK.trim()
            Registries.ITEM.trim()
            Registries.CREATIVE.trim()
            Registries.BIOME.trim()
            Registries.FUEL.trim()
            Registries.GENERATOR.trim()
            Registries.GENERATE_STAGE.trim()
            Registries.EFFECT.trim()
            Registries.RECIPE.trim()
        }

        this.enablePlugins(PluginLoadOrder.STARTUP)

        addProvider("leveldb", LevelDBProvider::class.java)

        loadLevels()

        this.queryInformation = QueryRegenerateEvent(this, 5)
        this.network = Network(this)
        tickingAreaManager.loadAllTickingArea()

        properties.save()

        if (this.getDefaultLevel() == null) {
            Server.log.error(language.tr("nukkit.level.defaultError"))
            this.forceShutdown()

            return
        }

        this.autoSaveTicks = settings.baseSettings().autosave()

        this.enablePlugins(PluginLoadOrder.POSTWORLD)


        EntityProperty.init()
        buildPacketData()
        buildPlayerProperty()

        if (settings.baseSettings().installSpark()) {
            initSpark(this)
        }

        if (!System.getProperty("disableWatchdog", "false").toBoolean()) {
            this.watchdog = Watchdog(this, 60000) //60s
            watchdog!!.start()
        }
        Runtime.getRuntime().addShutdownHook(Thread { this.shutdown() })
        this.start()
    }

    /**
     * @param name 世界名字
     * @return 世界是否已经加载<br></br>Is the world already loaded
     */
    fun isLevelLoaded(name: String): Boolean {
        if (!name.matches(levelDimPattern.toRegex())) {
            for (i in 0..2) {
                if (this.getLevelByName("$name Dim$i") != null) {
                    return true
                }
            }
            return false
        } else {
            return this.getLevelByName(name) != null
        }
    }

    /**
     * 从世界id得到世界,0主世界 1 地狱 2 末地
     *
     *
     * Get world from world id,0 OVERWORLD 1 NETHER 2 THE_END
     *
     * @param levelId 世界id<br></br>world id
     * @return level实例<br></br>level instance
     */
    fun getLevel(levelId: Int): cn.nukkit.level.Level? {
        if (levels.containsKey(levelId)) {
            return levels[levelId]
        }
        return null
    }

    /**
     * 从世界名得到世界,overworld 主世界 nether 地狱 the_end 末地
     *
     *
     * Get world from world name,`overworld nether the_end`
     *
     * @param name 世界名<br></br>world name
     * @return level实例<br></br>level instance
     */
    fun getLevelByName(name: String): cn.nukkit.level.Level? {
        var name = name
        if (!name.matches(levelDimPattern.toRegex())) {
            name = "$name Dim0"
        }
        for (level in this.levelArray) {
            if (level.getName().equals(name, ignoreCase = true)) {
                return level
            }
        }
        return null
    }

    /**
     * 卸载世界
     *
     *
     * unload level
     *
     * @param level       世界
     * @param forceUnload 是否强制卸载<br></br>whether to force uninstallation.
     * @return 卸载是否成功
     */
    @JvmOverloads
    fun unloadLevel(level: cn.nukkit.level.Level, forceUnload: Boolean = false): Boolean {
        check(!(level == this.getDefaultLevel() && !forceUnload)) { "The default level cannot be unloaded while running, please switch levels." }

        return level.unload(forceUnload)
    }

    fun getLevelConfig(levelFolderName: String): LevelConfig? {
        if (levelFolderName.trim { it <= ' ' } == "") {
            throw LevelException("Invalid empty level name")
        }
        var path = if (levelFolderName.contains("/") || levelFolderName.contains("\\")) {
            levelFolderName
        } else {
            File(this.dataPath, "worlds/$levelFolderName").absolutePath
        }
        val jpath = Path.of(path)
        path = jpath.toString()
        if (!jpath.toFile().exists()) {
            Server.log.warn(language.tr("nukkit.level.notFound", levelFolderName))
            return null
        }

        val config = jpath.resolve("config.json").toFile()
        val levelConfig: LevelConfig
        if (config.exists()) {
            try {
                levelConfig = from(config, LevelConfig::class.java)
                FileUtils.write(config, toPretty(levelConfig), StandardCharsets.UTF_8)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        } else {
            //verify the provider
            val provider = getProvider(path)
            if (provider == null) {
                Server.log.error(language.tr("nukkit.level.loadError", levelFolderName, "Unknown provider"))
                return null
            }
            val map: MutableMap<Int, GeneratorConfig> = HashMap()
            //todo nether the_end overworld
            map[0] = GeneratorConfig(
                "flat",
                System.currentTimeMillis(),
                false,
                LevelConfig.AntiXrayMode.LOW,
                true,
                DimensionEnum.OVERWORLD.dimensionData,
                emptyMap<Any, Any>()
            )
            levelConfig = LevelConfig(getProviderName(provider), true, map)
            try {
                config.createNewFile()
                FileUtils.write(config, toPretty(levelConfig), StandardCharsets.UTF_8)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        return levelConfig
    }

    /**
     * @param levelFolderName the level folder name
     * @return whether load success
     */
    fun loadLevel(levelFolderName: String): Boolean {
        var levelFolderName = levelFolderName
        if (levelFolderName.matches(levelDimPattern.toRegex())) {
            levelFolderName = levelFolderName.replaceFirst("\\sDim\\d$".toRegex(), "")
        }
        val levelConfig = getLevelConfig(levelFolderName) ?: return false
        val path = if (levelFolderName.contains("/") || levelFolderName.contains("\\")) {
            levelFolderName
        } else {
            File(this.dataPath, "worlds/$levelFolderName").absolutePath
        }
        val pathS = Path.of(path).toString()
        val provider = getProvider(pathS)

        val generators: Map<Int, GeneratorConfig?> = levelConfig.generators()
        for ((key, value): Map.Entry<Int, GeneratorConfig?> in generators) {
            val levelName = "$levelFolderName Dim$key"
            if (this.isLevelLoaded(levelName)) {
                return true
            }
            val level: cn.nukkit.level.Level
            try {
                if (provider == null) {
                    Server.log.error(language.tr("nukkit.level.loadError", levelFolderName, "the level does not exist"))
                    return false
                }
                level = Level(
                    this, levelName, pathS, generators.size, provider,
                    value!!
                )
            } catch (e: Exception) {
                Server.log.error(language.tr("nukkit.level.loadError", levelFolderName, e.message), e)
                return false
            }
            levels[level.id] = level
            level.initLevel()
            pluginManager.callEvent(LevelLoadEvent(level))
            level.tickRate = settings.levelSettings().baseTickRate()
        }
        if (tick != 0) { //update world enum when load  
            WorldCommand.WORLD_NAME_ENUM.updateSoftEnum()
        }
        return true
    }

    fun generateLevel(name: String, levelConfig: LevelConfig?): Boolean {
        var levelConfig = levelConfig
        if (name.isBlank()) {
            return false
        }
        var path = if (name.contains("/") || name.contains("\\")) {
            name
        } else {
            dataPath + "worlds/" + name + "/"
        }

        val jpath = Path.of(path)
        path = jpath.toString()
        val config = jpath.resolve("config.json").toFile()
        if (config.exists()) {
            try {
                levelConfig = from(FileReader(config), LevelConfig::class.java)
                FileUtils.write(config, toPretty<LevelConfig?>(levelConfig), StandardCharsets.UTF_8)
            } catch (e: Exception) {
                Server.log.error("The levelConfig is not exists under the {} path", path)
                return false
            }
        } else if (levelConfig != null) {
            try {
                jpath.toFile().mkdirs()
                config.createNewFile()
                FileUtils.write(config, toPretty<LevelConfig?>(levelConfig), StandardCharsets.UTF_8)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else {
            Server.log.error("The levelConfig is not specified and no config.json exists under the {} path", path)
            return false
        }

        for (entry in levelConfig.generators().entrySet()) {
            val generatorConfig: GeneratorConfig = entry.getValue()
            val provider = getProviderByName(levelConfig.format())
            val level: cn.nukkit.level.Level
            try {
                provider.getMethod("generate", String::class.java, String::class.java, GeneratorConfig::class.java)
                    .invoke(null, path, name, generatorConfig)
                val levelName = name + " Dim" + entry.getKey()
                if (this.isLevelLoaded(levelName)) {
                    Server.log.warn("level {} has already been loaded!", levelName)
                    continue
                }
                level = Level(this, levelName, path, levelConfig.generators().size(), provider, generatorConfig)

                levels[level.id] = level
                level.initLevel()
                level.tickRate = settings.levelSettings().baseTickRate()
                pluginManager.callEvent(LevelInitEvent(level))
                pluginManager.callEvent(LevelLoadEvent(level))
            } catch (e: Exception) {
                Server.log.error(language.tr("nukkit.level.generationError", name, getExceptionMessage(e)), e)
                return false
            }
        }
        return true
    }

    fun addOp(name: String) {
        iPBans.set(name.lowercase(), true)
        val player = this.getPlayerExact(name)
        if (player != null) {
            player.recalculatePermissions()
            player.getAdventureSettings()!!.onOpChange(true)
            player.getAdventureSettings()!!.update()
            player.getSession().syncAvailableCommands()
        }
        iPBans.save(true)
    }

    fun removeOp(name: String) {
        iPBans.remove(name.lowercase())
        val player = this.getPlayerExact(name)
        if (player != null) {
            player.recalculatePermissions()
            player.getAdventureSettings()!!.onOpChange(false)
            player.getAdventureSettings()!!.update()
            player.getSession().syncAvailableCommands()
        }
        iPBans.save()
    }

    fun addWhitelist(name: String) {
        ops.set(name.lowercase(), true)
        ops.save(true)
    }

    fun removeWhitelist(name: String) {
        ops.remove(name.lowercase())
        ops.save(true)
    }

    fun isWhitelisted(name: String): Boolean {
        return !this.hasWhitelist() || iPBans.exists(name, true) || ops.exists(name, true)
    }

    fun isOp(name: String?): Boolean {
        return name != null && iPBans.exists(name, true)
    }

    fun reloadWhitelist() {
        ops.reload()
    }

    // endregion
    // region configs - 配置相关
    fun getMaxPlayers(): Int {
        return maxPlayers
    }

    /**
     * Set the players count is allowed
     *
     * @param maxPlayers the max players
     */
    fun setMaxPlayers(maxPlayers: Int) {
        this.maxPlayers = maxPlayers
        network.pong!!.maximumPlayerCount(maxPlayers).update()
    }

    val port: Int
        /**
         * @return 服务器端口<br></br>server port
         */
        get() = properties.get(ServerPropertiesKeys.SERVER_PORT, 19132)

    val viewDistance: Int
        /**
         * @return 可视距离<br></br>server view distance
         */
        get() = properties.get(ServerPropertiesKeys.VIEW_DISTANCE, 10)

    val ip: String
        /**
         * @return 服务器网络地址<br></br>server ip
         */
        get() = properties.get(ServerPropertiesKeys.SERVER_IP, "0.0.0.0")

    /**
     * @return 服务器是否会自动保存<br></br>Does the server automatically save
     */
    fun getAutoSave(): Boolean {
        return this.autoSave
    }

    /**
     * 设置服务器自动保存
     *
     *
     * Set server autosave
     *
     * @param autoSave 是否自动保存<br></br>Whether to save automatically
     */
    fun setAutoSave(autoSave: Boolean) {
        this.autoSave = autoSave
        for (level in this.levelArray) {
            level.autoSave = this.autoSave
        }
    }

    val gamemode: Int
        /**
         * 得到服务器的gamemode
         *
         *
         * Get the gamemode of the server
         *
         * @return gamemode id
         */
        get() {
            return try {
                properties.get(ServerPropertiesKeys.GAMEMODE, 0) and 3
            } catch (exception: NumberFormatException) {
                getGamemodeFromString(
                    properties.get(
                        ServerPropertiesKeys.GAMEMODE,
                        "survival"
                    )
                ) and 3
            }
        }

    val forceGamemode: Boolean
        get() = properties.get(ServerPropertiesKeys.FORCE_GAMEMODE, false)

    /**
     * 获得服务器游戏难度
     *
     *
     * Get server game difficulty
     *
     * @return 游戏难度id<br></br>game difficulty id
     */
    fun getDifficulty(): Int {
        if (this.difficulty == Int.MAX_VALUE) {
            this.difficulty = getDifficultyFromString(properties.get(ServerPropertiesKeys.DIFFICULTY, "1"))
        }
        return this.difficulty
    }

    /**
     * 设置服务器游戏难度
     *
     *
     * set server game difficulty
     *
     * @param difficulty 游戏难度id<br></br>game difficulty id
     */
    fun setDifficulty(difficulty: Int) {
        var value = difficulty
        if (value < 0) value = 0
        if (value > 3) value = 3
        this.difficulty = value
        properties.get(ServerPropertiesKeys.DIFFICULTY, value)
    }

    /**
     * @return 是否开启白名单<br></br>Whether to start server whitelist
     */
    fun hasWhitelist(): Boolean {
        return properties.get(ServerPropertiesKeys.WHITE_LIST, false)
    }

    val spawnRadius: Int
        /**
         * @return 得到服务器出生点保护半径<br></br>Get server birth point protection radius
         */
        get() = properties.get(ServerPropertiesKeys.SPAWN_PROTECTION, 16)

    val allowFlight: Boolean
        /**
         * @return 服务器是否允许飞行<br></br>Whether the server allows flying
         */
        get() {
            if (getAllowFlight == null) {
                getAllowFlight = properties.get(ServerPropertiesKeys.ALLOW_FLIGHT, false)
            }
            return getAllowFlight
        }

    val isHardcore: Boolean
        /**
         * @return 服务器是否为硬核模式<br></br>Whether the server is in hardcore mode
         */
        get() = properties.get(ServerPropertiesKeys.HARDCORE, false)

    var motd: String
        /**
         * @return 得到服务器标题<br></br>Get server motd
         */
        get() = properties.get(ServerPropertiesKeys.MOTD, "PowerNukkitX Server")
        /**
         * Set the motd of server.
         *
         * @param motd the motd content
         */
        set(motd) {
            properties.get(ServerPropertiesKeys.MOTD, motd)
            network.pong!!.motd(motd).update()
        }

    var subMotd: String
        /**
         * @return 得到服务器子标题<br></br>Get the server subheading
         */
        get() {
            var subMotd =
                properties.get(ServerPropertiesKeys.SUB_MOTD, "v2.powernukkitx.com")
            if (subMotd.isEmpty()) {
                subMotd = "v2.powernukkitx.com"
            }
            return subMotd
        }
        /**
         * Set the sub motd of server.
         *
         * @param subMotd the sub motd
         */
        set(subMotd) {
            properties.get(ServerPropertiesKeys.SUB_MOTD, subMotd)
            network.pong!!.subMotd(subMotd).update()
        }

    val forceResources: Boolean
        /**
         * @return 是否强制使用服务器资源包<br></br>Whether to force the use of server resourcepack
         */
        get() = properties.get(ServerPropertiesKeys.FORCE_RESOURCES, false)

    val forceResourcesAllowOwnPacks: Boolean
        /**
         * @return 是否强制使用服务器资源包的同时允许加载客户端资源包<br></br>Whether to force the use of server resourcepack while allowing the loading of client resourcepack
         */
        get() = properties.get(ServerPropertiesKeys.FORCE_RESOURCES_ALLOW_CLIENT_PACKS, false)

    private fun mapInternalLang(langName: String): LangCode {
        return when (langName) {
            "bra" -> LangCode.valueOf("pt_BR")
            "chs" -> LangCode.valueOf("zh_CN")
            "cht" -> LangCode.valueOf("zh_TW")
            "cze" -> LangCode.valueOf("cs_CZ")
            "deu" -> LangCode.valueOf("de_DE")
            "fin" -> LangCode.valueOf("fi_FI")
            "eng" -> LangCode.valueOf("en_US")
            "fra" -> LangCode.valueOf("fr_FR")
            "idn" -> LangCode.valueOf("id_ID")
            "jpn" -> LangCode.valueOf("ja_JP")
            "kor" -> LangCode.valueOf("ko_KR")
            "ltu" -> LangCode.valueOf("en_US")
            "pol" -> LangCode.valueOf("pl_PL")
            "rus" -> LangCode.valueOf("ru_RU")
            "spa" -> LangCode.valueOf("es_ES")
            "tur" -> LangCode.valueOf("tr_TR")
            "ukr" -> LangCode.valueOf("uk_UA")
            "vie" -> LangCode.valueOf("en_US")
            else -> throw IllegalArgumentException()
        }
    }

    fun isTheEndAllowed(): Boolean {
        return this.isNetherAllowed
    }

    fun isIgnoredPacket(clazz: Class<out DataPacket?>): Boolean {
        return settings.debugSettings().ignoredPackets().contains(clazz.simpleName)
    }

    fun getServerAuthoritativeMovement(): Int {
        return serverAuthoritativeMovementMode
    }

    // endregion
    // region threading - 并发基础设施
    /**
     * Checks the current thread against the expected primary thread for the
     * server.
     *
     *
     * **Note:** this method should not be used to indicate the current
     * synchronized state of the runtime. A current thread matching the main
     * thread indicates that it is synchronized, but a mismatch does not
     * preclude the same assumption.
     *
     * @return true if the current thread matches the expected primary thread,
     * false otherwise
     */
    fun isPrimaryThread(): Boolean {
        return (Thread.currentThread() === currentThread)
    }

    fun getPrimaryThread(): Thread {
        return currentThread
    }

    //todo NukkitConsole 会阻塞关不掉
    private inner class ConsoleThread : Thread("Console Thread"), InterruptibleThread {
        override fun run() {
            console.start()
        }
    }

    private class ComputeThread(pool: ForkJoinPool, threadCount: AtomicInteger) : ForkJoinWorkerThread(pool) {
        /**
         * Creates a ForkJoinWorkerThread operating in the given pool.
         *
         * @param pool the pool this thread works in
         * @throws NullPointerException if pool is null
         */
        init {
            this.name = "ComputeThreadPool-thread-" + threadCount.getAndIncrement()
        }
    }

    private class ComputeThreadPoolThreadFactory : ForkJoinWorkerThreadFactory {
        override fun newThread(pool: ForkJoinPool): ForkJoinWorkerThread {
            return AccessController.doPrivileged(PrivilegedAction<ForkJoinWorkerThread> {
                ComputeThread(
                    pool,
                    threadCount
                )
            }, ACC)
        }

        companion object {
            private val threadCount = AtomicInteger(0)
            private val ACC = contextWithPermissions(
                RuntimePermission("getClassLoader"),
                RuntimePermission("setContextClassLoader")
            )

            fun contextWithPermissions(vararg perms: Permission?): AccessControlContext {
                val permissions = Permissions()
                for (perm in perms) permissions.add(perm)
                return AccessControlContext(arrayOf(ProtectionDomain(null, permissions)))
            }
        }
    } // endregion

    companion object {
        const val BROADCAST_CHANNEL_ADMINISTRATIVE: String = "nukkit.broadcast.admin"
        const val BROADCAST_CHANNEL_USERS: String = "nukkit.broadcast.user"

        // endregion
        // region server singleton - Server 单例
        @JvmStatic
        var instance: Server? = null
            private set

        // endregion
        // region networking - 网络相关
        /**
         * @see .broadcastPacket
         */
        fun broadcastPacket(players: Collection<Player>, packet: DataPacket) {
            for (player in players) {
                player.dataPacket(packet)
            }
        }

        /**
         * 广播一个数据包给指定的玩家们.
         *
         *Broadcast a packet to the specified players.
         *
         * @param players 接受数据包的所有玩家<br></br>All players receiving the data package
         * @param packet  数据包
         */
        fun broadcastPacket(players: Array<Player>, packet: DataPacket) {
            for (player in players) {
                player.dataPacket(packet)
            }
        }

        const val levelDimPattern: String = "^.*Dim[0-9]$"

        /**
         * 默认`direct=false`
         *
         * @see .getGamemodeString
         */
        fun getGamemodeString(mode: Int): String {
            return getGamemodeString(mode, false)
        }

        /**
         * 从gamemode id获取游戏模式字符串.
         *
         *
         * Get game mode string from gamemode id.
         *
         * @param mode   gamemode id
         * @param direct 如果为true就直接返回字符串,为false返回代表游戏模式的硬编码字符串.<br></br>If true, the string is returned directly, and if false, the hard-coded string representing the game mode is returned.
         * @return 游戏模式字符串<br></br>Game Mode String
         */
        fun getGamemodeString(mode: Int, direct: Boolean): String {
            return when (mode) {
                Player.Companion.SURVIVAL -> if (direct) "Survival" else "%gameMode.survival"
                Player.Companion.CREATIVE -> if (direct) "Creative" else "%gameMode.creative"
                Player.Companion.ADVENTURE -> if (direct) "Adventure" else "%gameMode.adventure"
                Player.Companion.SPECTATOR -> if (direct) "Spectator" else "%gameMode.spectator"
                else -> "UNKNOWN"
            }
        }

        /**
         * 从字符串获取gamemode
         *
         *
         * Get gamemode from string
         *
         * @param str 代表游戏模式的字符串，例如0,survival...<br></br>A string representing the game mode, e.g. 0,survival...
         * @return 游戏模式id<br></br>gamemode id
         */
        fun getGamemodeFromString(str: String): Int {
            return when (str.trim { it <= ' ' }.lowercase()) {
                "0", "survival", "s" -> Player.Companion.SURVIVAL
                "1", "creative", "c" -> Player.Companion.CREATIVE
                "2", "adventure", "a" -> Player.Companion.ADVENTURE
                "3", "spectator" -> Player.Companion.SPECTATOR
                else -> -1
            }
        }

        /**
         * 从字符串获取游戏难度
         *
         *
         * Get game difficulty from string
         *
         * @param str 代表游戏难度的字符串，例如0,peaceful...<br></br>A string representing the game difficulty, e.g. 0,peaceful...
         * @return 游戏难度id<br></br>game difficulty id
         */
        fun getDifficultyFromString(str: String): Int {
            when (str.trim { it <= ' ' }.lowercase()) {
                "0", "peaceful", "p" -> return 0

                "1", "easy", "e" -> return 1

                "2", "normal", "n" -> return 2

                "3", "hard", "h" -> return 3
            }
            return -1
        }
    }
}
