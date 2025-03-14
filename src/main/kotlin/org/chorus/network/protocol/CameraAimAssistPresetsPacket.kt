package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.entity.Attribute.getValue
import org.chorus.nbt.tag.ListTag.get
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.camera.aimassist.*
import org.chorus.utils.OptionalValue
import java.util.List
import java.util.function.BiConsumer
import java.util.function.Function


class CameraAimAssistPresetsPacket : DataPacket() {
    private val categories: MutableList<CameraAimAssistCategories> = ObjectArrayList()
    private val presets: MutableList<CameraAimAssistPreset> = ObjectArrayList()
    private var cameraAimAssistPresetsPacketOperation: CameraAimAssistPresetsPacketOperation? = null

    override fun decode(byteBuf: HandleByteBuf) {
        categories.addAll(
            List.of(
                *byteBuf.readArray<CameraAimAssistCategories>(
                    CameraAimAssistCategories::class.java,
                    Function { byteBuf: HandleByteBuf -> this.readCategories(byteBuf) })
            )
        )
        presets.addAll(
            List.of(
                *byteBuf.readArray<CameraAimAssistPreset>(
                    CameraAimAssistPreset::class.java,
                    Function { byteBuf: HandleByteBuf -> this.readPreset(byteBuf) })
            )
        )
        this.cameraAimAssistPresetsPacketOperation =
            CameraAimAssistPresetsPacketOperation.Companion.VALUES.get(byteBuf.readByte().toInt())
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(
            categories,
            BiConsumer { byteBuf: HandleByteBuf, categories: CameraAimAssistCategories ->
                this.writeCategories(
                    byteBuf,
                    categories
                )
            })
        byteBuf.writeArray(
            presets,
            BiConsumer { byteBuf: HandleByteBuf, preset: CameraAimAssistPreset ->
                this.writeCameraAimAssist(
                    byteBuf,
                    preset
                )
            })
        byteBuf.writeByte(cameraAimAssistPresetsPacketOperation!!.ordinal())
    }

    private fun writeCategories(byteBuf: HandleByteBuf, categories: CameraAimAssistCategories) {
        byteBuf.writeString(categories.getIdentifier())
        byteBuf.writeArray(
            categories.getCategories(),
            BiConsumer { byteBuf: HandleByteBuf, category: CameraAimAssistCategory ->
                this.writeCategory(
                    byteBuf,
                    category
                )
            })
    }

    private fun writeCategory(byteBuf: HandleByteBuf, category: CameraAimAssistCategory) {
        byteBuf.writeString(category.getName())
        writePriorities(byteBuf, category.getPriorities())
    }

    private fun writePriorities(byteBuf: HandleByteBuf, priorities: CameraAimAssistCategoryPriorities) {
        byteBuf.writeArray<Map.Entry<String, Int>>(
            priorities.entities.entrySet(),
            BiConsumer<HandleByteBuf?, Map.Entry<String, Int>> { byteBuf: HandleByteBuf, priority: Map.Entry<String, Int> ->
                this.writePriority(
                    byteBuf,
                    priority
                )
            })
        byteBuf.writeArray<Map.Entry<String, Int>>(
            priorities.blocks.entrySet(),
            BiConsumer<HandleByteBuf?, Map.Entry<String, Int>> { byteBuf: HandleByteBuf, priority: Map.Entry<String, Int> ->
                this.writePriority(
                    byteBuf,
                    priority
                )
            })
    }

    private fun writePriority(byteBuf: HandleByteBuf, priority: Map.Entry<String, Int>) {
        byteBuf.writeString(priority.getKey())
        byteBuf.writeIntLE(priority.getValue())
    }

    private fun writeCameraAimAssist(byteBuf: HandleByteBuf, preset: CameraAimAssistPreset) {
        byteBuf.writeString(preset.getIdentifier())
        byteBuf.writeString(preset.getCategories())
        byteBuf.writeArray(
            preset.getExclusionList()
        ) { str: String? -> byteBuf.writeString(str!!) }
        byteBuf.writeArray(
            preset.getLiquidTargetingList()
        ) { str: String? -> byteBuf.writeString(str!!) }
        byteBuf.writeArray<Map.Entry<String, String>>(
            preset.getItemSettings().entrySet(),
            BiConsumer<HandleByteBuf?, Map.Entry<String, String>> { byteBuf: HandleByteBuf, itemSetting: Map.Entry<String, String> ->
                this.writeItemSetting(
                    byteBuf,
                    itemSetting
                )
            })
        byteBuf.writeOptional(
            preset.getDefaultItemSettings()
        ) { str: String? -> byteBuf.writeString(str!!) }
        byteBuf.writeOptional(
            preset.getHandSettings()
        ) { str: String? -> byteBuf.writeString(str!!) }
    }

    private fun writeItemSetting(byteBuf: HandleByteBuf, itemSetting: Map.Entry<String, String>) {
        byteBuf.writeString(itemSetting.getKey())
        byteBuf.writeString(itemSetting.getValue())
    }

    // READ
    private fun readCategories(byteBuf: HandleByteBuf): CameraAimAssistCategories {
        val categories = CameraAimAssistCategories()
        categories.setIdentifier(byteBuf.readString())
        val categoryLength = byteBuf.readUnsignedVarInt()
        for (i in 0..<categoryLength) {
            categories.getCategories().add(readCategory(byteBuf))
        }
        return categories
    }

    private fun readCategory(byteBuf: HandleByteBuf): CameraAimAssistCategory {
        val category = CameraAimAssistCategory()
        category.setName(byteBuf.readString())
        category.setPriorities(readPriorities(byteBuf))
        return category
    }

    private fun readPriorities(byteBuf: HandleByteBuf): CameraAimAssistCategoryPriorities {
        val priorities = CameraAimAssistCategoryPriorities()
        val entityPriorityLength = byteBuf.readUnsignedVarInt()
        for (i in 0..<entityPriorityLength) {
            val entry = readPriority(byteBuf)
            priorities.getEntities()[entry.getKey()] = entry.getValue()
        }
        val blockPriorityLength = byteBuf.readUnsignedVarInt()
        for (i in 0..<blockPriorityLength) {
            val entry = readPriority(byteBuf)
            priorities.getBlocks()[entry.getKey()] = entry.getValue()
        }
        return priorities
    }

    private fun readPriority(byteBuf: HandleByteBuf): Map.Entry<String, Int> {
        return java.util.Map.entry(byteBuf.readString(), byteBuf.readIntLE())
    }

    private fun readPreset(byteBuf: HandleByteBuf): CameraAimAssistPreset {
        val preset = CameraAimAssistPreset()
        preset.setIdentifier(byteBuf.readString())
        preset.setCategories(byteBuf.readString())
        preset.getExclusionList().addAll(
            List.of(
                *byteBuf.readArray<String>(
                    String::class.java,
                    Function { obj: HandleByteBuf -> obj.readString() })
            )
        )
        preset.getLiquidTargetingList().addAll(
            List.of(
                *byteBuf.readArray<String>(
                    String::class.java,
                    Function { obj: HandleByteBuf -> obj.readString() })
            )
        )
        val itemSettingsLength = byteBuf.readUnsignedVarInt()
        for (i in 0..<itemSettingsLength) {
            val entry = readItemSetting(byteBuf)
            preset.getItemSettings()[entry.getKey()] = entry.getValue()
        }
        preset.setDefaultItemSettings(
            OptionalValue.of(
                byteBuf.readOptional(
                    null
                ) { byteBuf.readString() })
        )
        preset.setHandSettings(
            OptionalValue.of(
                byteBuf.readOptional(
                    null
                ) { byteBuf.readString() })
        )
        return preset
    }

    private fun readItemSetting(byteBuf: HandleByteBuf): Map.Entry<String, String> {
        return java.util.Map.entry(byteBuf.readString(), byteBuf.readString())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CAMERA_AIM_ASSIST_PRESETS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
