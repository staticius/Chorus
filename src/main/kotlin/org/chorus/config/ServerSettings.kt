package org.chorus.config

import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.annotation.Comment
import eu.okaeri.configs.annotation.CustomKey

class ServerSettings : OkaeriConfig() {
    @Comment("chorus.server.settings.baseSettings")
    @CustomKey("settings")
    val baseSettings = BaseSettings()

    @Comment("chorus.server.settings.networkSettings")
    @CustomKey("network-settings")
    val networkSettings = NetworkSettings()

    @Comment("chorus.server.settings.debugSettings")
    @CustomKey("debug-settings")
    val debugSettings = DebugSettings()

    @Comment("chorus.server.settings.levelSettings")
    @CustomKey("level-settings")
    val levelSettings = LevelSettings()

    @Comment("chorus.server.settings.chunkSettings")
    @CustomKey("chunk-settings")
    val chunkSettings = ChunkSettings()

    @Comment("chorus.server.settings.playerSettings")
    @CustomKey("player-settings")
    val playerSettings = PlayerSettings()

    @Comment("chorus.server.settings.gameplaySettings")
    @CustomKey("gameplay-settings")
    val gameplaySettings = GameplaySettings()

    open class BaseSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.baseSettings.language")
        open var language: String = "eng"

        @Comment("chorus.server.settings.baseSettings.forceServerTranslate")
        open var forceServerTranslate: Boolean = false

        @Comment("chorus.server.settings.baseSettings.shutdownMessage")
        open var shutdownMessage: String = "Server closed"

        @Comment("chorus.server.settings.baseSettings.queryPlugins")
        open var queryPlugins: Boolean = true

        @Comment("chorus.server.settings.baseSettings.deprecatedVerbose")
        open var deprecatedVerbose: Boolean = true

        @Comment("chorus.server.settings.baseSettings.asyncWorkers")
        open var asyncWorkers: String = "auto"

        @Comment("chorus.server.settings.baseSettings.safeSpawn")
        open var safeSpawn: Boolean = true

        @Comment("chorus.server.settings.baseSettings.installSpark")
        open var installSpark: Boolean = true

        @Comment("chorus.server.settings.baseSettings.waterdogpe")
        open var waterdogpe: Boolean = false

        @Comment("chorus.server.settings.baseSettings.autosave")
        open var autosave: Int = 6000

        @Comment("chorus.server.settings.baseSettings.saveUnknownBlock")
        open var saveUnknownBlock: Boolean = true
    }

    open class NetworkSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.networkSettings.compressionLevel")
        open var compressionLevel: Int = 7

        @Comment("chorus.server.settings.networkSettings.zlibProvider")
        open var zlibProvider: Int = 3

        @Comment("chorus.server.settings.networkSettings.snappy")
        open var snappy: Boolean = false

        @Comment("chorus.server.settings.networkSettings.compressionBufferSize")
        open var compressionBufferSize: Int = 1048576

        @Comment("chorus.server.settings.networkSettings.maxDecompressSize")
        open var maxDecompressSize: Int = 67108864

        @Comment("chorus.server.settings.networkSettings.packetLimit")
        open var packetLimit: Int = 240
    }

    open class DebugSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.debugSettings.level")
        open var level: String = "INFO"

        @Comment("chorus.server.settings.debugSettings.command")
        open var command: Boolean = false

        @Comment("chorus.server.settings.debugSettings.ignoredPackets")
        open var ignoredPackets: ArrayList<String> = ArrayList()

        @Comment("chorus.server.settings.debugSettings.allowBeta")
        open var allowBeta: Boolean = false
    }

    open class LevelSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.levelSettings.autoTickRate")
        open var autoTickRate: Boolean = true

        @Comment("chorus.server.settings.levelSettings.autoTickRateLimit")
        open var autoTickRateLimit: Int = 20

        @Comment("chorus.server.settings.levelSettings.baseTickRate")
        open var baseTickRate: Int = 1

        @Comment("chorus.server.settings.levelSettings.alwaysTickPlayers")
        open var alwaysTickPlayers: Boolean = false

        @Comment("chorus.server.settings.levelSettings.enableRedstone")
        open var enableRedstone: Boolean = true

        @Comment("chorus.server.settings.levelSettings.tickRedstone")
        open var tickRedstone: Boolean = true

        @Comment("chorus.server.settings.levelSettings.chunkUnloadDelay")
        open var chunkUnloadDelay: Int = 15000

        @Comment("chorus.server.settings.levelSettings.levelThread")
        open var levelThread: Boolean = false
    }

    open class ChunkSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.chunkSettings.perTickSend")
        open var perTickSend: Int = 8

        @Comment("chorus.server.settings.chunkSettings.spawnThreshold")
        open var spawnThreshold: Int = 56

        @Comment("chorus.server.settings.chunkSettings.chunksPerTicks")
        open var chunksPerTicks: Int = 40

        @Comment("chorus.server.settings.chunkSettings.tickRadius")
        open var tickRadius: Int = 3

        @Comment("chorus.server.settings.chunkSettings.lightUpdates")
        open var lightUpdates: Boolean = true

        @Comment("chorus.server.settings.chunkSettings.clearTickList")
        open var clearTickList: Boolean = false

        @Comment("chorus.server.settings.chunkSettings.generationQueueSize")
        open var generationQueueSize: Int = 128
    }

    open class PlayerSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.playerSettings.savePlayerData")
        open var savePlayerData: Boolean = true

        @Comment("chorus.server.settings.playerSettings.skinChangeCooldown")
        open var skinChangeCooldown: Int = 30

        @Comment("chorus.server.settings.playerSettings.forceSkinTrusted")
        open var forceSkinTrusted: Boolean = false

        @Comment("chorus.server.settings.playerSettings.checkMovement")
        open var checkMovement: Boolean = true

        @Comment("chorus.server.settings.playerSettings.spawnRadius")
        open var spawnRadius: Int = 16
    }

    open class GameplaySettings : OkaeriConfig() {
        @Comment("chorus.server.settings.gameplaySettings.enableCommandBlocks")
        open var enableCommandBlocks: Boolean = true
    }
}
