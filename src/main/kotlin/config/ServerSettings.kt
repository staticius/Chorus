package org.chorus_oss.chorus.config

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.annotations.TomlComments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
class ServerSettings {
    @TomlComments("chorus.server.settings.baseSettings")
    @SerialName("settings")
    val baseSettings = BaseSettings()

    @TomlComments("chorus.server.settings.networkSettings")
    @SerialName("network-settings")
    val networkSettings = NetworkSettings()

    @TomlComments("chorus.server.settings.debugSettings")
    @SerialName("debug-settings")
    val debugSettings = DebugSettings()

    @TomlComments("chorus.server.settings.levelSettings")
    @SerialName("level-settings")
    val levelSettings = LevelSettings()

    @TomlComments("chorus.server.settings.chunkSettings")
    @SerialName("chunk-settings")
    val chunkSettings = ChunkSettings()

    @TomlComments("chorus.server.settings.playerSettings")
    @SerialName("player-settings")
    val playerSettings = PlayerSettings()

    @TomlComments("chorus.server.settings.gameplaySettings")
    @SerialName("gameplay-settings")
    val gameplaySettings = GameplaySettings()

    @Serializable
    class BaseSettings {
        @TomlComments("chorus.server.settings.baseSettings.language")
        var language: String = "eng"

        @TomlComments("chorus.server.settings.baseSettings.forceServerTranslate")
        var forceServerTranslate: Boolean = false

        @TomlComments("chorus.server.settings.baseSettings.shutdownMessage")
        var shutdownMessage: String = "Server closed"

        @TomlComments("chorus.server.settings.baseSettings.queryPlugins")
        var queryPlugins: Boolean = true

        @TomlComments("chorus.server.settings.baseSettings.deprecatedVerbose")
        var deprecatedVerbose: Boolean = true

        @TomlComments("chorus.server.settings.baseSettings.asyncWorkers")
        var asyncWorkers: String = "auto"

        @TomlComments("chorus.server.settings.baseSettings.safeSpawn")
        var safeSpawn: Boolean = true

        @TomlComments("chorus.server.settings.baseSettings.installSpark")
        var installSpark: Boolean = true

        @TomlComments("chorus.server.settings.baseSettings.waterdogpe")
        var waterdogpe: Boolean = false

        @TomlComments("chorus.server.settings.baseSettings.autosave")
        var autosave: Int = 6000

        @TomlComments("chorus.server.settings.baseSettings.saveUnknownBlock")
        var saveUnknownBlock: Boolean = true
    }

    @Serializable
    class NetworkSettings {
        @TomlComments("chorus.server.settings.networkSettings.compressionLevel")
        var compressionLevel: Int = 7

        @TomlComments("chorus.server.settings.networkSettings.zlibProvider")
        var zlibProvider: Int = 3

        @TomlComments("chorus.server.settings.networkSettings.snappy")
        var snappy: Boolean = false

        @TomlComments("chorus.server.settings.networkSettings.compressionBufferSize")
        var compressionBufferSize: Int = 1048576

        @TomlComments("chorus.server.settings.networkSettings.maxDecompressSize")
        var maxDecompressSize: Int = 67108864

        @TomlComments("chorus.server.settings.networkSettings.packetLimit")
        var packetLimit: Int = 240
    }

    @Serializable
    class DebugSettings {
        @TomlComments("chorus.server.settings.debugSettings.level")
        var level: String = "INFO"

        @TomlComments("chorus.server.settings.debugSettings.command")
        var command: Boolean = false

        @TomlComments("chorus.server.settings.debugSettings.ignoredPackets")
        var ignoredPackets: ArrayList<String> = ArrayList()

        @TomlComments("chorus.server.settings.debugSettings.allowBeta")
        var allowBeta: Boolean = false
    }

    @Serializable
    class LevelSettings {
        @TomlComments("chorus.server.settings.levelSettings.autoTickRate")
        var autoTickRate: Boolean = true

        @TomlComments("chorus.server.settings.levelSettings.autoTickRateLimit")
        var autoTickRateLimit: Int = 20

        @TomlComments("chorus.server.settings.levelSettings.baseTickRate")
        var baseTickRate: Int = 1

        @TomlComments("chorus.server.settings.levelSettings.alwaysTickPlayers")
        var alwaysTickPlayers: Boolean = false

        @TomlComments("chorus.server.settings.levelSettings.enableRedstone")
        var enableRedstone: Boolean = true

        @TomlComments("chorus.server.settings.levelSettings.tickRedstone")
        var tickRedstone: Boolean = true

        @TomlComments("chorus.server.settings.levelSettings.chunkUnloadDelay")
        var chunkUnloadDelay: Int = 15000

        @TomlComments("chorus.server.settings.levelSettings.levelThread")
        var levelThread: Boolean = false
    }

    @Serializable
    class ChunkSettings {
        @TomlComments("chorus.server.settings.chunkSettings.perTickSend")
        var perTickSend: Int = 8

        @TomlComments("chorus.server.settings.chunkSettings.spawnThreshold")
        var spawnThreshold: Int = 56

        @TomlComments("chorus.server.settings.chunkSettings.chunksPerTicks")
        var chunksPerTicks: Int = 40

        @TomlComments("chorus.server.settings.chunkSettings.tickRadius")
        var tickRadius: Int = 3

        @TomlComments("chorus.server.settings.chunkSettings.lightUpdates")
        var lightUpdates: Boolean = true

        @TomlComments("chorus.server.settings.chunkSettings.clearTickList")
        var clearTickList: Boolean = false

        @TomlComments("chorus.server.settings.chunkSettings.generationQueueSize")
        var generationQueueSize: Int = 128
    }

    @Serializable
    class PlayerSettings {
        @TomlComments("chorus.server.settings.playerSettings.savePlayerData")
        var savePlayerData: Boolean = true

        @TomlComments("chorus.server.settings.playerSettings.skinChangeCooldown")
        var skinChangeCooldown: Int = 30

        @TomlComments("chorus.server.settings.playerSettings.forceSkinTrusted")
        var forceSkinTrusted: Boolean = false

        @TomlComments("chorus.server.settings.playerSettings.checkMovement")
        var checkMovement: Boolean = true

        @TomlComments("chorus.server.settings.playerSettings.spawnRadius")
        var spawnRadius: Int = 16
    }

    @Serializable
    class GameplaySettings {
        @TomlComments("chorus.server.settings.gameplaySettings.enableCommandBlocks")
        var enableCommandBlocks: Boolean = true
    }

    fun save(file: File) {
        file.writeText(Toml.encodeToString(this))
    }

    companion object {
        fun load(file: File): ServerSettings {
            return Toml.decodeFromString<ServerSettings>(file.readText())
        }
    }
}
