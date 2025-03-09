package org.chorus.command.data

import org.chorus.Server
import org.chorus.camera.data.CameraPreset.Companion.presets
import org.chorus.item.enchantment.Enchantment
import org.chorus.network.protocol.UpdateSoftEnumPacket
import org.chorus.registry.Registries
import org.chorus.utils.Identifier
import com.google.common.collect.ImmutableList
import java.util.*
import java.util.function.Supplier

/**
 * @author CreeperFace
 */
class CommandEnum {
    @JvmField
    val name: String
    private val values: List<String?>?


    val isSoft: Boolean
    private val supplier: Supplier<Collection<String?>>?


    constructor(name: String, vararg values: String?) : this(name, Arrays.asList<String?>(*values))

    /**
     * 构建一个枚举参数
     *
     * @param name   该枚举的名称，会显示到命令中
     * @param values 该枚举的可选值，不能为空，但是可以为空列表
     * @param soft   当为False  时，客户端显示枚举参数会带上枚举名称[CommandEnum.getName],当为true时 则判定为String
     */
    @JvmOverloads
    constructor(name: String, values: List<String?>?, soft: Boolean = false) {
        this.name = name
        this.values = values
        this.isSoft = soft
        this.supplier = null
    }

    /**
     * Instantiates a new Soft Command enum.
     *
     * @param name     the name
     * @param supplier the str list supplier
     */
    constructor(name: String, supplier: Supplier<Collection<String?>>?) {
        this.name = name
        this.values = null
        this.isSoft = true
        this.supplier = supplier
    }

    fun getValues(): List<String?>? {
        return if (this.supplier == null) {
            values
        } else {
            supplier.get().stream().toList()
        }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun updateSoftEnum(mode: UpdateSoftEnumPacket.Type?, vararg value: String) {
        if (!this.isSoft) return
        val packet = UpdateSoftEnumPacket()
        packet.name = this.name
        packet.values = Arrays.stream(value).toList()
        packet.type = mode
        Server.broadcastPacket(Server.getInstance().onlinePlayers.values, packet)
    }

    fun updateSoftEnum() {
        if (!this.isSoft && this.supplier == null) return
        val packet = UpdateSoftEnumPacket()
        packet.name = this.name
        packet.values = this.getValues()
        packet.type = UpdateSoftEnumPacket.Type.SET
        Server.broadcastPacket(Server.getInstance().onlinePlayers.values, packet)
    }

    companion object {
        val ENUM_ENCHANTMENT: CommandEnum = CommandEnum(
            "enchantmentName"
        ) {
            Enchantment.getEnchantmentName2IDMap().keys.stream()
                .map { name: String ->
                    if (name.startsWith(Identifier.DEFAULT_NAMESPACE)) name.substring(
                        10
                    ) else name
                }
                .toList()
        }

        val ENUM_EFFECT: CommandEnum = CommandEnum(
            "Effect", Registries.EFFECT.effectStringId2TypeMap
                .keys
                .stream()
                .toList()
        )

        val FUNCTION_FILE: CommandEnum = CommandEnum(
            "filepath"
        ) { Server.getInstance().functionManager.functions.keys }

        @JvmField
        val SCOREBOARD_OBJECTIVES: CommandEnum = CommandEnum(
            "ScoreboardObjectives"
        ) { Server.getInstance().scoreboardManager.scoreboards.keys }

        val CAMERA_PRESETS: CommandEnum = CommandEnum(
            "preset"
        ) { presets.keys }

        val CHAINED_COMMAND_ENUM: CommandEnum = CommandEnum(
            "ExecuteChainedOption_0",
            "run",
            "as",
            "at",
            "positioned",
            "if",
            "unless",
            "in",
            "align",
            "anchored",
            "rotated",
            "facing"
        )

        val ENUM_BOOLEAN: CommandEnum = CommandEnum("Boolean", ImmutableList.of("true", "false"))

        val ENUM_GAMEMODE: CommandEnum =
            CommandEnum("GameMode", ImmutableList.of("survival", "creative", "s", "c", "adventure", "a", "spectator"))

        val ENUM_BLOCK: CommandEnum = CommandEnum("Block", emptyList<String>())

        val ENUM_ITEM: CommandEnum = CommandEnum("Item", emptyList<String>())

        val ENUM_ENTITY: CommandEnum = CommandEnum("Entity", emptyList<String>())
    }
}
