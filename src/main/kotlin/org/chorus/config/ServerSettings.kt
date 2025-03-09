package org.chorus.config

import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.annotation.Comment
import eu.okaeri.configs.annotation.CustomKey
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.experimental.Accessors

(callSuper = true)

@Accessors(fluent = true)
class ServerSettings : OkaeriConfig() {
    @Comment("nukkit.server.settings.baseSettings")
    @CustomKey("settings")
    val baseSettings = BaseSettings()

    @Comment("nukkit.server.settings.networkSettings")
    @CustomKey("network-settings")
    val networkSettings = NetworkSettings()

    @Comment("nukkit.server.settings.debugSettings")
    @CustomKey("debug-settings")
    val debugSettings = DebugSettings()

    @Comment("nukkit.server.settings.levelSettings")
    @CustomKey("level-settings")
    val levelSettings = LevelSettings()

    @Comment("nukkit.server.settings.chunkSettings")
    @CustomKey("chunk-settings")
    val chunkSettings = ChunkSettings()

    @Comment("nukkit.server.settings.freezearray")
    @CustomKey("memory-settings")
    val freezeArraySettings = FreezeArraySettings()

    @Comment("nukkit.server.settings.playersettings")
    @CustomKey("player-settings")
    val playerSettings = PlayerSettings()

    @Comment("nukkit.server.settings.gameplaysettings")
    @CustomKey("gameplay-settings")
    val gameplaySettings = GameplaySettings()

    (callSuper = true)

    @Accessors(fluent = true)
    class BaseSettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.baseSettings.language")
        var language: String = "eng"

        @Comment("nukkit.server.settings.baseSettings.forceServerTranslate")
        var forceServerTranslate: Boolean = false

        @Comment("nukkit.server.settings.baseSettings.shutdownMessage")
        var shutdownMessage: String = "Server closed"

        @Comment("nukkit.server.settings.baseSettings.queryPlugins")
        var queryPlugins: Boolean = true

        @Comment("nukkit.server.settings.baseSettings.deprecatedVerbose")
        var deprecatedVerbose: Boolean = true

        @Comment("nukkit.server.settings.baseSettings.asyncWorkers")
        var asyncWorkers: String = "auto"

        @Comment("nukkit.server.settings.baseSettings.safeSpawn")
        var safeSpawn: Boolean = true

        @Comment("nukkit.server.settings.baseSettings.installSpark")
        var installSpark: Boolean = true

        @Comment("nukkit.server.settings.baseSettings.waterdogpe")
        var waterdogpe: Boolean = false

        @Comment("nukkit.server.settings.baseSettings.autosave")
        var autosave: Int = 6000

        @Comment("nukkit.server.settings.baseSettings.saveUnknownBlock")
        var saveUnknownBlock: Boolean = true
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class NetworkSettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.networkSettings.compressionLevel")
        var compressionLevel: Int = 7

        @Comment("nukkit.server.settings.networkSettings.zlibProvider")
        var zlibProvider: Int = 3

        @Comment("nukkit.server.settings.networkSettings.snappy")
        var snappy: Boolean = false

        @Comment("nukkit.server.settings.networkSettings.compressionBufferSize")
        var compressionBufferSize: Int = 1048576

        @Comment("nukkit.server.settings.networkSettings.maxDecompressSize")
        var maxDecompressSize: Int = 67108864

        @Comment("nukkit.server.settings.networkSettings.packetLimit")
        var packetLimit: Int = 240
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class DebugSettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.debugSettings.level")
        var level: String = "INFO"

        @Comment("nukkit.server.settings.debugSettings.command")
        var command: Boolean = false

        @Comment("nukkit.server.settings.debugSettings.ignoredPackets")
        var ignoredPackets: ArrayList<String> = ArrayList()

        @Comment("nukkit.server.settings.debugSettings.allowBeta")
        var allowBeta: Boolean = false
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class LevelSettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.levelSettings.autoTickRate")
        var autoTickRate: Boolean = true

        @Comment("nukkit.server.settings.levelSettings.autoTickRateLimit")
        var autoTickRateLimit: Int = 20

        @Comment("nukkit.server.settings.levelSettings.baseTickRate")
        var baseTickRate: Int = 1

        @Comment("nukkit.server.settings.levelSettings.alwaysTickPlayers")
        var alwaysTickPlayers: Boolean = false

        @Comment("nukkit.server.settings.levelSettings.enableRedstone")
        var enableRedstone: Boolean = true

        @Comment("nukkit.server.settings.levelSettings.tickRedstone")
        var tickRedstone: Boolean = true

        @Comment("nukkit.server.settings.levelSettings.chunkUnloadDelay")
        var chunkUnloadDelay: Int = 15000

        @Comment("nukkit.server.settings.levelSettings.levelThread")
        var levelThread: Boolean = false
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class ChunkSettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.chunkSettings.perTickSend")
        var perTickSend: Int = 8

        @Comment("nukkit.server.settings.chunkSettings.spawnThreshold")
        var spawnThreshold: Int = 56

        @Comment("nukkit.server.settings.chunkSettings.chunksPerTicks")
        var chunksPerTicks: Int = 40

        @Comment("nukkit.server.settings.chunkSettings.tickRadius")
        var tickRadius: Int = 3

        @Comment("nukkit.server.settings.chunkSettings.lightUpdates")
        var lightUpdates: Boolean = true

        @Comment("nukkit.server.settings.chunkSettings.clearTickList")
        var clearTickList: Boolean = false

        @Comment("nukkit.server.settings.chunkSettings.generationQueueSize")
        var generationQueueSize: Int = 128
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class FreezeArraySettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.freezearray.enable")
        var enable: Boolean = true
        var slots: Int = 32
        var defaultTemperature: Int = 32
        var freezingPoint: Int = 0
        var boilingPoint: Int = 1024
        var absoluteZero: Int = -256
        var melting: Int = 16
        var singleOperation: Int = 1
        var batchOperation: Int = 32
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class PlayerSettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.playersettings.savePlayerData")
        var savePlayerData: Boolean = true

        @Comment("nukkit.server.settings.playersettings.skinChangeCooldown")
        var skinChangeCooldown: Int = 30

        @Comment("nukkit.server.settings.playersettings.forceSkinTrusted")
        var forceSkinTrusted: Boolean = false

        @Comment("nukkit.server.settings.playersettings.checkMovement")
        var checkMovement: Boolean = true

        @Comment("nukkit.server.settings.playersettings.spawnRadius")
        var spawnRadius: Int = 16
    }

    (callSuper = true)

    @Accessors(fluent = true)
    class GameplaySettings : OkaeriConfig() {
        @Comment("nukkit.server.settings.gameplaysettings.enableCommandBlocks")
        var enableCommandBlocks: Boolean = true
    }
}
