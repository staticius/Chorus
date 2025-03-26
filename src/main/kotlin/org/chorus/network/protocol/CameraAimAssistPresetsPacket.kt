package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.camera.aimassist.*
import org.chorus.utils.OptionalValue
import java.util.function.BiConsumer

class CameraAimAssistPresetsPacket : DataPacket() {
    private val categories: MutableList<CameraAimAssistCategories> = ObjectArrayList()
    private val presets: MutableList<CameraAimAssistPreset> = ObjectArrayList()
    private var cameraAimAssistPresetsPacketOperation: CameraAimAssistPresetsPacketOperation? = null

    override fun decode(byteBuf: HandleByteBuf) {
        categories.addAll(
            byteBuf.readArray(
                    CameraAimAssistCategories::class.java
            ) { this.readCategories(byteBuf) }
        )
        presets.addAll(
            byteBuf.readArray(
                CameraAimAssistPreset::class.java
            ) { this.readPreset(byteBuf) }
        )
        this.cameraAimAssistPresetsPacketOperation = CameraAimAssistPresetsPacketOperation.VALUES[byteBuf.readByte().toInt()]
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(categories) { buf, categories ->
            this.writeCategories(
                buf,
                categories
            )
        }
        byteBuf.writeArray(presets) { buf, preset ->
            this.writeCameraAimAssist(
                buf,
                preset
            )
        }
        byteBuf.writeByte(cameraAimAssistPresetsPacketOperation!!.ordinal)
    }

    private fun writeCategories(byteBuf: HandleByteBuf, categories: CameraAimAssistCategories) {
        byteBuf.writeString(categories.identifier)
        byteBuf.writeArray(categories.categories) { buf, category ->
            this.writeCategory(
                buf,
                category
            )
        }
    }

    private fun writeCategory(byteBuf: HandleByteBuf, category: CameraAimAssistCategory) {
        byteBuf.writeString(category.name)
        writePriorities(byteBuf, category.priorities)
    }

    private fun writePriorities(byteBuf: HandleByteBuf, priorities: CameraAimAssistCategoryPriorities) {
        byteBuf.writeArray(priorities.entities.entries) { buf, priority ->
            this.writePriority(
                buf,
                priority
            )
        }
        byteBuf.writeArray(priorities.blocks.entries) { buf, priority ->
            this.writePriority(
                buf,
                priority
            )
        }
    }

    private fun writePriority(byteBuf: HandleByteBuf, priority: Map.Entry<String, Int>) {
        byteBuf.writeString(priority.key)
        byteBuf.writeIntLE(priority.value)
    }

    private fun writeCameraAimAssist(byteBuf: HandleByteBuf, preset: CameraAimAssistPreset) {
        byteBuf.writeString(preset.identifier)
        byteBuf.writeString(preset.categories)
        byteBuf.writeArray(preset.exclusionList) { str -> byteBuf.writeString(str) }
        byteBuf.writeArray(preset.liquidTargetingList) { str -> byteBuf.writeString(str) }
        byteBuf.writeArray(preset.itemSettings.entries) { buf, itemSetting ->
            this.writeItemSetting(
                buf,
                itemSetting
            )
        }
        byteBuf.writeOptional(preset.defaultItemSettings) { str -> byteBuf.writeString(str) }
        byteBuf.writeOptional(preset.handSettings) { str -> byteBuf.writeString(str) }
    }

    private fun writeItemSetting(byteBuf: HandleByteBuf, itemSetting: Map.Entry<String, String>) {
        byteBuf.writeString(itemSetting.key)
        byteBuf.writeString(itemSetting.value)
    }

    private fun readCategories(byteBuf: HandleByteBuf): CameraAimAssistCategories {
        return CameraAimAssistCategories(
            identifier = byteBuf.readString(),
            categories = List(byteBuf.readUnsignedVarInt()) {
                readCategory(byteBuf)
            }
        )
    }

    private fun readCategory(byteBuf: HandleByteBuf): CameraAimAssistCategory {
        return CameraAimAssistCategory(
            name = byteBuf.readString(),
            priorities = readPriorities(byteBuf),
        )
    }

    private fun readPriorities(byteBuf: HandleByteBuf): CameraAimAssistCategoryPriorities {
        return CameraAimAssistCategoryPriorities(
            entities = List(byteBuf.readUnsignedVarInt()) {
                readPriority(byteBuf).let {
                    it.key to it.value
                }
            }.toMap(),
            blocks = List(byteBuf.readUnsignedVarInt()) {
                readPriority(byteBuf).let {
                    it.key to it.value
                }
            }.toMap()
        )
    }

    private fun readPriority(byteBuf: HandleByteBuf): Map.Entry<String, Int> {
        return java.util.Map.entry(byteBuf.readString(), byteBuf.readIntLE())
    }

    private fun readPreset(byteBuf: HandleByteBuf): CameraAimAssistPreset {
        return CameraAimAssistPreset(
            identifier = byteBuf.readString(),
            categories = byteBuf.readString(),
            exclusionList = byteBuf.readArray(
                String::class.java,
            ) { obj: HandleByteBuf -> obj.readString() }.toList(),
            liquidTargetingList = byteBuf.readArray(
                String::class.java,
            ) { obj: HandleByteBuf -> obj.readString() }.toList(),
            itemSettings = List(byteBuf.readUnsignedVarInt()) {
                readItemSetting(byteBuf).let {
                    it.key to it.value
                }
            }.toMap(),
            defaultItemSettings = OptionalValue.ofNullable(byteBuf.readOptional(null) { byteBuf.readString() }),
            handSettings = OptionalValue.ofNullable(byteBuf.readOptional(null) { byteBuf.readString() })
        )
    }

    private fun readItemSetting(byteBuf: HandleByteBuf): Map.Entry<String, String> {
        return java.util.Map.entry(byteBuf.readString(), byteBuf.readString())
    }

    override fun pid(): Int {
        return ProtocolInfo.CAMERA_AIM_ASSIST_PRESETS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
