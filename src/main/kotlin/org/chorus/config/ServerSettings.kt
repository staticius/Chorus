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

    class BaseSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.baseSettings.language")
        var language: String = "eng"

        @Comment("chorus.server.settings.baseSettings.forceServerTranslate")
        var forceServerTranslate: Boolean = false

        @Comment("chorus.server.settings.baseSettings.shutdownMessage")
        var shutdownMessage: String = "Server closed"

        @Comment("chorus.server.settings.baseSettings.queryPlugins")
        var queryPlugins: Boolean = true

        @Comment("chorus.server.settings.baseSettings.deprecatedVerbose")
        var deprecatedVerbose: Boolean = true

        @Comment("chorus.server.settings.baseSettings.asyncWorkers")
        var asyncWorkers: String = "auto"

        @Comment("chorus.server.settings.baseSettings.safeSpawn")
        var safeSpawn: Boolean = true

        @Comment("chorus.server.settings.baseSettings.installSpark")
        var installSpark: Boolean = true

        @Comment("chorus.server.settings.baseSettings.waterdogpe")
        var waterdogpe: Boolean = false

        @Comment("chorus.server.settings.baseSettings.autosave")
        var autosave: Int = 6000

        @Comment("chorus.server.settings.baseSettings.saveUnknownBlock")
        var saveUnknownBlock: Boolean = true
    }

    class NetworkSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.networkSettings.compressionLevel")
        var compressionLevel: Int = 7

        @Comment("chorus.server.settings.networkSettings.zlibProvider")
        var zlibProvider: Int = 3

        @Comment("chorus.server.settings.networkSettings.snappy")
        var snappy: Boolean = false

        @Comment("chorus.server.settings.networkSettings.compressionBufferSize")
        var compressionBufferSize: Int = 1048576

        @Comment("chorus.server.settings.networkSettings.maxDecompressSize")
        var maxDecompressSize: Int = 67108864

        @Comment("chorus.server.settings.networkSettings.packetLimit")
        var packetLimit: Int = 240
    }

    class DebugSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.debugSettings.level")
        var level: String = "INFO"

        @Comment("chorus.server.settings.debugSettings.command")
        var command: Boolean = false

        @Comment("chorus.server.settings.debugSettings.ignoredPackets")
        var ignoredPackets: ArrayList<String> = ArrayList()

        @Comment("chorus.server.settings.debugSettings.allowBeta")
        var allowBeta: Boolean = false
    }

    class LevelSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.levelSettings.autoTickRate")
        var autoTickRate: Boolean = true

        @Comment("chorus.server.settings.levelSettings.autoTickRateLimit")
        var autoTickRateLimit: Int = 20

        @Comment("chorus.server.settings.levelSettings.baseTickRate")
        var baseTickRate: Int = 1

        @Comment("chorus.server.settings.levelSettings.alwaysTickPlayers")
        var alwaysTickPlayers: Boolean = false

        @Comment("chorus.server.settings.levelSettings.enableRedstone")
        var enableRedstone: Boolean = true

        @Comment("chorus.server.settings.levelSettings.tickRedstone")
        var tickRedstone: Boolean = true

        @Comment("chorus.server.settings.levelSettings.chunkUnloadDelay")
        var chunkUnloadDelay: Int = 15000

        @Comment("chorus.server.settings.levelSettings.levelThread")
        var levelThread: Boolean = false
    }

    class ChunkSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.chunkSettings.perTickSend")
        var perTickSend: Int = 8

        @Comment("chorus.server.settings.chunkSettings.spawnThreshold")
        var spawnThreshold: Int = 56

        @Comment("chorus.server.settings.chunkSettings.chunksPerTicks")
        var chunksPerTicks: Int = 40

        @Comment("chorus.server.settings.chunkSettings.tickRadius")
        var tickRadius: Int = 3

        @Comment("chorus.server.settings.chunkSettings.lightUpdates")
        var lightUpdates: Boolean = true

        @Comment("chorus.server.settings.chunkSettings.clearTickList")
        var clearTickList: Boolean = false

        @Comment("chorus.server.settings.chunkSettings.generationQueueSize")
        var generationQueueSize: Int = 128
    }

    class PlayerSettings : OkaeriConfig() {
        @Comment("chorus.server.settings.playerSettings.savePlayerData")
        var savePlayerData: Boolean = true

        @Comment("chorus.server.settings.playerSettings.skinChangeCooldown")
        var skinChangeCooldown: Int = 30

        @Comment("chorus.server.settings.playerSettings.forceSkinTrusted")
        var forceSkinTrusted: Boolean = false

        @Comment("chorus.server.settings.playerSettings.checkMovement")
        var checkMovement: Boolean = true

        @Comment("chorus.server.settings.playerSettings.spawnRadius")
        var spawnRadius: Int = 16
    }

    class GameplaySettings : OkaeriConfig() {
        @Comment("chorus.server.settings.gameplaySettings.enableCommandBlocks")
        var enableCommandBlocks: Boolean = true
    }
}
