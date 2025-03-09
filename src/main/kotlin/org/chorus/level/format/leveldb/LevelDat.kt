package org.chorus.level.format.leveldb

import org.chorus.level.GameRules
import org.chorus.math.BlockVector3
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.types.GameType
import org.chorus.utils.SemVersion
import lombok.*

@Getter
@Builder
@ToString
class LevelDat {
    @Builder.Default
    var biomeOverride: String = ""

    @Builder.Default
    var centerMapsToOrigin: Boolean = false

    @Builder.Default
    var confirmedPlatformLockedContent: Boolean = false

    @Builder.Default
    var difficulty: Int = 1

    @Builder.Default
    var flatWorldLayers: String = ""

    @Builder.Default
    var forceGameType: Boolean = false

    @Builder.Default
    var gameType: GameType = GameType.from(0)

    @Builder.Default
    var generator: Int = 1

    @Builder.Default
    var inventoryVersion: String = "1.20.60"

    @Builder.Default
    var LANBroadcast: Boolean = true

    @Builder.Default
    var LANBroadcastIntent: Boolean = true

    @Builder.Default
    var lastPlayed: Long = 0L

    @Builder.Default
    var name: String = "Bedrock level"

    @Builder.Default
    var limitedWorldOriginPoint: BlockVector3 = BlockVector3(0, 64, 0)

    @Builder.Default
    var minimumCompatibleClientVersion: SemVersion = SemVersion(
        1,
        20,
        50,
        0,
        0
    )

    @Builder.Default
    var multiplayerGame: Boolean = true

    @Builder.Default
    var multiplayerGameIntent: Boolean = false

    @Builder.Default
    var netherScale: Int = 8

    @Builder.Default
    var networkVersion: Int = ProtocolInfo.CURRENT_PROTOCOL

    @Builder.Default
    var platform: Int = 2

    @Builder.Default
    var platformBroadcastIntent: Int = 0

    @Builder.Default
    var randomSeed: Long = 1811906518383890446L

    @Builder.Default
    var spawnV1Villagers: Boolean = false

    /**
     * The overworld default spawn point
     */
    @Builder.Default
    var spawnPoint: BlockVector3 = BlockVector3(0, 70, 0)

    @Builder.Default
    var storageVersion: Int = 10

    @Builder.Default
    var time: Long = 0L

    @Builder.Default
    var worldVersion: Int = 1

    @Builder.Default
    var XBLBroadcastIntent: Int = 0

    @Builder.Default
    var abilities: Abilities = Abilities.builder().build()

    @Builder.Default
    var baseGameVersion: String = "*"

    @Builder.Default
    var bonusChestEnabled: Boolean = false

    @Builder.Default
    var bonusChestSpawned: Boolean = false

    @Builder.Default
    var cheatsEnabled: Boolean = false

    @Builder.Default
    var commandsEnabled: Boolean = true

    @Builder.Default
    @Getter(AccessLevel.NONE)
    var gameRules: GameRules? = GameRules.Companion.getDefault()

    @Builder.Default
    var currentTick: Long = 0L

    @Builder.Default
    var daylightCycle: Int = 0

    @Builder.Default
    var editorWorldType: Int = 0

    @Builder.Default
    var eduOffer: Int = 0

    @Builder.Default
    var educationFeaturesEnabled: Boolean = false

    @Builder.Default
    var experiments: Experiments = Experiments.builder().build()

    @Builder.Default
    var hasBeenLoadedInCreative: Boolean = true

    @Builder.Default
    var hasLockedBehaviorPack: Boolean = false

    @Builder.Default
    var hasLockedResourcePack: Boolean = false

    @Builder.Default
    var immutableWorld: Boolean = false

    @Builder.Default
    var isCreatedInEditor: Boolean = false

    @Builder.Default
    var isExportedFromEditor: Boolean = false

    @Builder.Default
    var isFromLockedTemplate: Boolean = false

    @Builder.Default
    var isFromWorldTemplate: Boolean = false

    @Builder.Default
    var isRandomSeedAllowed: Boolean = false

    @Builder.Default
    var isSingleUseWorld: Boolean = false

    @Builder.Default
    var isWorldTemplateOptionLocked: Boolean = false

    @Builder.Default
    var lastOpenedWithVersion: SemVersion = SemVersion(
        1,
        20,
        40,
        1,
        0
    )

    @Builder.Default
    var lightningLevel: Float = 0.0f

    @Builder.Default
    var lightningTime: Int = 0 //thunderTime

    @Builder.Default
    var limitedWorldDepth: Int = 16

    @Builder.Default
    var limitedWorldWidth: Int = 16

    @Builder.Default
    var permissionsLevel: Int = 0

    @Builder.Default
    var playerPermissionsLevel: Int = 1

    @Builder.Default
    var playersSleepingPercentage: Int = 100

    @Builder.Default
    var prid: String = ""

    @Builder.Default
    var rainLevel: Float = 0.0f

    @Builder.Default
    var rainTime: Int = 0 //rainTime

    @Builder.Default
    var randomTickSpeed: Int = 1

    @Builder.Default
    var recipesUnlock: Boolean = false

    @Builder.Default
    var requiresCopiedPackRemovalCheck: Boolean = false

    @Builder.Default
    var serverChunkTickRange: Int = 4

    @Builder.Default
    var spawnMobs: Boolean = true

    @Builder.Default
    var startWithMapEnabled: Boolean = false

    @Builder.Default
    var texturePacksRequired: Boolean = false

    @Builder.Default
    var useMsaGamertagsOnly: Boolean = true

    @Builder.Default
    var worldStartCount: Long = 0L

    @Builder.Default
    var worldPolicies: WorldPolicies = WorldPolicies()

    @Builder.Default
    var raining: Boolean = false //PNX Custom field

    @Builder.Default
    var thundering: Boolean = false //PNX Custom field

    fun setRandomSeed(seed: Long) {
        this.randomSeed = seed
    }

    fun setCurrentTick(currentTick: Int) {
        this.currentTick = currentTick.toLong()
    }

    fun setLightningTime(lightningTime: Int) {
        this.lightningTime = lightningTime
    }

    fun setThundering(thundering: Boolean) {
        this.thundering = thundering
    }

    fun setRainTime(rainTime: Int) {
        this.rainTime = rainTime
    }

    fun setRaining(raining: Boolean) {
        this.raining = raining
    }

    fun addTime() {
        time++
    }

    fun setCurrentTick(currentTick: Long) {
        this.currentTick = currentTick
    }

    @Value
    @Builder
    @ToString
    class Abilities {
        @Builder.Default
        var attackMobs: Boolean = true

        @Builder.Default
        var attackPlayers: Boolean = true

        @Builder.Default
        var build: Boolean = true

        @Builder.Default
        var doorsAndSwitches: Boolean = true

        @Builder.Default
        var flySpeed: Float = 0.05f

        @Builder.Default
        var flying: Boolean = false

        @Builder.Default
        var instaBuild: Boolean = false

        @Builder.Default
        var invulnerable: Boolean = false

        @Builder.Default
        var lightning: Boolean = false

        @Builder.Default
        var mayFly: Boolean = false

        @Builder.Default
        var mine: Boolean = true

        @Builder.Default
        var op: Boolean = false

        @Builder.Default
        var openContainers: Boolean = true

        @Builder.Default
        var teleport: Boolean = false

        @Builder.Default
        var walkSpeed: Float = 0.1f
    }

    @Value
    @Builder
    @ToString
    class Experiments {
        @Builder.Default
        var experimentsEverUsed: Boolean = false

        @Builder.Default
        var savedWithToggledExperiments: Boolean = false

        @Builder.Default
        var cameras: Boolean = false

        @Builder.Default
        var dataDrivenBiomes: Boolean = false

        @Builder.Default
        var dataDrivenItems: Boolean = false

        @Builder.Default
        var experimentalMolangFeatures: Boolean = false

        @Builder.Default
        var gametest: Boolean = false

        @Builder.Default
        var upcomingCreatorFeatures: Boolean = false

        @Builder.Default
        var villagerTradesRebalance: Boolean = false
    }

    @Value
    @Builder
    @ToString
    class WorldPolicies
}
