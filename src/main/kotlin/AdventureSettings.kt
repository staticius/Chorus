package org.chorus_oss.chorus

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.nbt.tag.IntTag
import org.chorus_oss.chorus.network.protocol.RequestPermissionsPacket
import org.chorus_oss.chorus.network.protocol.types.CommandPermission
import org.chorus_oss.chorus.network.protocol.types.PlayerAbility
import org.chorus_oss.chorus.network.protocol.types.PlayerPermission
import org.chorus_oss.protocol.types.AbilitiesData
import org.chorus_oss.protocol.types.PlayerAbilitySet
import java.util.*

class AdventureSettings : Cloneable {
    val values: MutableMap<Type, Boolean> = EnumMap(
        Type::class.java
    )
    var playerPermission: PlayerPermission? = null
        set(value) {
            field = value
            player.isOp = value == PlayerPermission.OPERATOR
        }

    var commandPermission: CommandPermission? = null
    var player: Player

    constructor(player: Player) {
        this.player = player
        init(null)
    }

    constructor(player: Player, nbt: CompoundTag?) {
        this.player = player
        init(nbt)
    }

    fun init(nbt: CompoundTag?) {
        if (nbt == null || !nbt.contains(KEY_ABILITIES)) {
            set(Type.WORLD_IMMUTABLE, player.isAdventure || player.isSpectator)
            set(Type.WORLD_BUILDER, !player.isAdventure && !player.isSpectator)
            set(Type.AUTO_JUMP, true)
            set(Type.ALLOW_FLIGHT, player.isCreative || player.isSpectator)
            set(Type.NO_CLIP, player.isSpectator)
            set(Type.FLYING, player.isSpectator)
            set(Type.OPERATOR, player.isOp)
            set(Type.TELEPORT, player.isOp)

            commandPermission = if (player.isOp) CommandPermission.GAME_DIRECTOR else CommandPermission.ANY
            playerPermission = if (player.isOp) PlayerPermission.OPERATOR else PlayerPermission.MEMBER
        } else {
            readNBT(nbt)
        }

        //Offline deop
        if (playerPermission == PlayerPermission.OPERATOR && !player.isOp) {
            onOpChange(false)
        }
        //Offline by op
        if (playerPermission != PlayerPermission.OPERATOR && player.isOp) {
            onOpChange(true)
        }
    }

    public override fun clone(): AdventureSettings {
        try {
            val settings = super.clone() as AdventureSettings
            settings.values.putAll(this.values)
            settings.player = this.player
            settings.playerPermission = this.playerPermission
            settings.commandPermission = this.commandPermission
            return settings
        } catch (e: CloneNotSupportedException) {
            throw AssertionError() // This should never happen.
        }
    }

    operator fun set(ability: PlayerAbility, value: Boolean): AdventureSettings {
        val type = ability2TypeMap[ability]
        if (type != null) {
            values[type] = value
        }
        return this
    }

    operator fun set(type: Type, value: Boolean): AdventureSettings {
        values[type] = value
        return this
    }

    fun get(ability: PlayerAbility): Boolean {
        val type = ability2TypeMap[ability]
        requireNotNull(type) { "Unknown ability: $ability" }
        return values.getOrDefault(type, type.defaultValue)
    }

    operator fun get(type: Type): Boolean {
        return values.getOrDefault(type, type.defaultValue)
    }

    fun update() {
        //Permission to send to all players so they can see each other
        //Make sure it will be sent to yourself (eg: there is no such player among the online players when the player enters the server)
        val players: MutableCollection<Player> = HashSet<Player>(Server.instance.onlinePlayers.values)
        players.add(this.player)
        sendAbilities(players)
        updateAdventureSettings()
    }


    /**
     * This method will be called when the player's OP status changes.
     * Note that this method does not send a packet to the client to refresh the privilege information, you need to manually call the update() method to do so
     * @param op is OP or not
     */
    fun onOpChange(op: Boolean) {
        if (op) {
            for (controllableAbility in RequestPermissionsPacket.CONTROLLABLE_ABILITIES) {
                set(controllableAbility, true)
            }
        }
        //Set op-specific attributes
        set(Type.OPERATOR, op)
        set(Type.TELEPORT, op)

        commandPermission = if (op) CommandPermission.GAME_DIRECTOR else CommandPermission.ANY

        //Don't override customization/guest status
        if (op && playerPermission != PlayerPermission.OPERATOR) {
            playerPermission = PlayerPermission.OPERATOR
        }
        if (!op && playerPermission == PlayerPermission.OPERATOR) {
            playerPermission = PlayerPermission.MEMBER
        }
    }

    fun sendAbilities(players: Collection<Player>) {
        val packet = org.chorus_oss.protocol.packets.UpdateAbilitiesPacket(
            abilitiesData = AbilitiesData(
                targetPlayerRawID = player.getUniqueID(),
                playerPermissions = org.chorus_oss.protocol.types.PlayerPermission.entries[playerPermission!!.ordinal],
                commandPermissions = org.chorus_oss.protocol.types.CommandPermission.entries[commandPermission!!.ordinal],
                layers = listOf(
                    org.chorus_oss.protocol.types.AbilityLayer(
                        layerType = org.chorus_oss.protocol.types.AbilityLayer.Companion.Type.Base,
                        abilitiesSet = PlayerAbilitySet(
                            flags = org.chorus_oss.protocol.types.PlayerAbility.entries.toMutableSet()
                        ),
                        abilityValues = PlayerAbilitySet(
                            flags = listOf(
                                Type.entries
                                    .filter { it.isAbility() && get(it) }
                                    .map { org.chorus_oss.protocol.types.PlayerAbility.entries[it.ability!!.ordinal] },
                                when (player.isCreative) {
                                    true -> listOf(org.chorus_oss.protocol.types.PlayerAbility.Instabuild)
                                    false -> emptyList()
                                },
                                listOf(
                                    org.chorus_oss.protocol.types.PlayerAbility.WalkSpeed,
                                    org.chorus_oss.protocol.types.PlayerAbility.FlySpeed,
                                )
                            ).flatten().toMutableSet()
                        ),
                        flySpeed = Player.DEFAULT_FLY_SPEED,
                        verticalFlySpeed = 1f,
                        walkSpeed = Player.DEFAULT_SPEED,
                    )
                )
            )
        )
        Server.broadcastPacket(players, packet)
    }

    /**
     * Save permissions to nbt
     */
    fun saveNBT() {
        val nbt = player.namedTag
        val abilityTag = CompoundTag()
        values.forEach { (type, bool) ->
            abilityTag.put(type.name, IntTag(if (bool) 1 else 0))
        }
        nbt!!.put(KEY_ABILITIES, abilityTag)
        nbt.putString(KEY_PLAYER_PERMISSION, playerPermission!!.name)
        nbt.putString(KEY_COMMAND_PERMISSION, commandPermission!!.name)
    }

    /**
     * Read permission data from nbt
     */
    private fun readNBT(nbt: CompoundTag) {
        val abilityTag = nbt.getCompound(KEY_ABILITIES)
        for ((key, value) in abilityTag.tags) {
            if (value is IntTag) {
                set(Type.valueOf(key), value.data == 1)
            }
        }
        playerPermission = PlayerPermission.valueOf(nbt.getString(KEY_PLAYER_PERMISSION))
        commandPermission = CommandPermission.valueOf(
            nbt.getString(
                KEY_COMMAND_PERMISSION
            )
        )
    }

    fun updateAdventureSettings() {
        val adventurePacket = org.chorus_oss.protocol.packets.UpdateAdventureSettingsPacket(
            noPvM = get(Type.NO_PVM),
            noMvP = get(Type.NO_MVP),
            immutableWorld = get(Type.WORLD_IMMUTABLE),
            showNameTags = get(Type.SHOW_NAME_TAGS),
            autoJump = get(Type.AUTO_JUMP)
        )
        player.sendPacket(adventurePacket)
        player.resetInAirTicks()
    }

    enum class Type {
        WORLD_IMMUTABLE(false),
        NO_PVM(false),
        NO_MVP(PlayerAbility.INVULNERABLE, false),
        SHOW_NAME_TAGS(false),
        AUTO_JUMP(true),
        ALLOW_FLIGHT(PlayerAbility.MAY_FLY, false),
        NO_CLIP(PlayerAbility.NO_CLIP, false),
        WORLD_BUILDER(PlayerAbility.WORLD_BUILDER, false),
        FLYING(PlayerAbility.FLYING, false),
        MUTED(PlayerAbility.MUTED, false),
        MINE(PlayerAbility.MINE, true),
        DOORS_AND_SWITCHED(PlayerAbility.DOORS_AND_SWITCHES, true),
        OPEN_CONTAINERS(PlayerAbility.OPEN_CONTAINERS, true),
        ATTACK_PLAYERS(PlayerAbility.ATTACK_PLAYERS, true),
        ATTACK_MOBS(PlayerAbility.ATTACK_MOBS, true),
        OPERATOR(PlayerAbility.OPERATOR_COMMANDS, false),
        TELEPORT(PlayerAbility.TELEPORT, false),
        BUILD(PlayerAbility.BUILD, true),
        PRIVILEGED_BUILDER(PlayerAbility.PRIVILEGED_BUILDER, false),
        VERTICAL_FLY_SPEED(PlayerAbility.VERTICAL_FLY_SPEED, true);

        val ability: PlayerAbility?
        val defaultValue: Boolean

        constructor(defaultValue: Boolean) {
            this.defaultValue = defaultValue
            this.ability = null
        }

        constructor(ability: PlayerAbility?, defaultValue: Boolean) {
            this.ability = ability
            this.defaultValue = defaultValue
            if (this.ability != null) {
                ability2TypeMap[this.ability] = this
            }
        }

        fun isAbility(): Boolean {
            return this.ability != null
        }
    }

    companion object {
        const val PERMISSION_NORMAL: Int = 0
        const val PERMISSION_OPERATOR: Int = 1
        const val PERMISSION_HOST: Int = 2
        const val PERMISSION_AUTOMATION: Int = 3
        const val PERMISSION_ADMIN: Int = 4

        const val KEY_ABILITIES: String = "Abilities"
        const val KEY_PLAYER_PERMISSION: String = "PlayerPermission"
        const val KEY_COMMAND_PERMISSION: String = "CommandPermission"

        private val ability2TypeMap: MutableMap<PlayerAbility, Type> = EnumMap(PlayerAbility::class.java)
    }
}