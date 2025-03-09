package org.chorus.entity.data

import cn.nukkit.nbt.stream.FastByteArrayOutputStream
import cn.nukkit.utils.*
import com.google.common.base.Preconditions
import lombok.EqualsAndHashCode
import lombok.ToString
import org.jose4j.json.internal.json_simple.JSONObject
import org.jose4j.json.internal.json_simple.JSONValue
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
@ToString(exclude = ["geometryData", "animationData"])
@EqualsAndHashCode(exclude = ["trusted"])
class Skin {
    private val animations: MutableList<SkinAnimation?> = ArrayList()
    private val personaPieces: MutableList<PersonaPiece?> = ArrayList()
    private val tintColors: MutableList<PersonaPieceTint?> = ArrayList()
    private var skinId: String? = null
    private var fullSkinId: String? = null

    private var playFabId: String? = ""
    private var skinResourcePatch: String = GEOMETRY_CUSTOM
    private var skinData: SerializedImage? = null
    private var capeData: SerializedImage? = null
    private var geometryData: String? = null
    private var animationData: String? = null
    private var premium: Boolean = false
    private var persona: Boolean = false
    private var capeOnClassic: Boolean = false
    private var primaryUser: Boolean = true
    private var capeId: String? = null
    private var skinColor: String? = "#0"
    private var armSize: String? = "wide"
    private var trusted: Boolean = true
    private var geometryDataEngineVersion: String? = "0.0.0"
    private var overridingPlayerAppearance: Boolean = true

    fun isValid(): Boolean {
        return isValidSkin() && isValidResourcePatch()
    }

    private fun isValidSkin(): Boolean {
        return skinId != null && !skinId!!.trim { it <= ' ' }
            .isEmpty() && skinId!!.length < 100 && skinData != null && skinData!!.width >= 32 && skinData!!.height >= 32 && skinData!!.data.size >= SINGLE_SKIN_SIZE &&
                (playFabId == null || playFabId!!.length < 100) &&
                (capeId == null || capeId!!.length < 100) &&
                (skinColor == null || skinColor!!.length < 100) &&
                (armSize == null || armSize!!.length < 100) &&
                (fullSkinId == null || fullSkinId!!.length < 200) &&
                (geometryDataEngineVersion == null || geometryDataEngineVersion!!.length < 100) &&
                (animationData == null || animationData!!.length < 1000) && animations.size <= 100 && personaPieces.size <= 100 && tintColors.size <= 100
    }

    private fun isValidResourcePatch(): Boolean {
        if (skinResourcePatch == null || skinResourcePatch.length > 1000) {
            return false
        }
        try {
            val `object`: JSONObject = JSONValue.parse(skinResourcePatch) as JSONObject
            val geometry: JSONObject = `object`.get("geometry") as JSONObject
            return geometry.containsKey("default") && geometry.get("default") is String
        } catch (e: ClassCastException) {
            return false
        } catch (e: NullPointerException) {
            return false
        }
    }

    fun getSkinData(): SerializedImage {
        if (skinData == null) {
            return SerializedImage.EMPTY
        }
        return skinData
    }

    fun setSkinData(skinData: ByteArray?) {
        setSkinData(SerializedImage.fromLegacy(skinData))
    }

    fun setSkinData(image: BufferedImage) {
        setSkinData(parseBufferedImage(image))
    }

    fun setSkinData(skinData: SerializedImage) {
        Objects.requireNonNull(skinData, "skinData")
        this.skinData = skinData
    }

    fun getSkinId(): String? {
        if (this.skinId == null) {
            this.generateSkinId("Custom")
        }
        return skinId
    }

    fun setSkinId(skinId: String?) {
        if (skinId == null || skinId.trim { it <= ' ' }.isEmpty()) {
            return
        }
        this.skinId = skinId
    }

    fun generateSkinId(name: String) {
        val data: ByteArray = Binary.appendBytes(
            getSkinData().data, *getSkinResourcePatch().toByteArray(
                StandardCharsets.UTF_8
            )
        )
        this.skinId = UUID.nameUUIDFromBytes(data).toString() + "." + name
    }

    fun setGeometryName(geometryName: String?) {
        if (geometryName == null || geometryName.trim { it <= ' ' }.isEmpty()) {
            skinResourcePatch = GEOMETRY_CUSTOM
            return
        }

        this.skinResourcePatch = "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}"
    }

    fun getSkinResourcePatch(): String {
        return Objects.requireNonNullElse(this.skinResourcePatch, "")
    }

    fun setSkinResourcePatch(skinResourcePatch: String?) {
        var skinResourcePatch: String? = skinResourcePatch
        if (skinResourcePatch == null || skinResourcePatch.trim { it <= ' ' }.isEmpty()) {
            skinResourcePatch = GEOMETRY_CUSTOM
        }
        this.skinResourcePatch = skinResourcePatch
    }

    fun getCapeData(): SerializedImage {
        if (capeData == null) {
            return SerializedImage.EMPTY
        }
        return capeData
    }

    fun setCapeData(capeData: ByteArray) {
        Objects.requireNonNull(capeData, "capeData")
        Preconditions.checkArgument(capeData.size == SINGLE_SKIN_SIZE || capeData.size == 0, "Invalid legacy cape")
        setCapeData(SerializedImage(64, 32, capeData))
    }

    fun setCapeData(image: BufferedImage) {
        setCapeData(parseBufferedImage(image))
    }

    fun setCapeData(capeData: SerializedImage) {
        Objects.requireNonNull(capeData, "capeData")
        this.capeData = capeData
    }

    fun getCapeId(): String {
        if (capeId == null) {
            return ""
        }
        return capeId
    }

    fun setCapeId(capeId: String?) {
        var capeId: String? = capeId
        if (capeId == null || capeId.trim { it <= ' ' }.isEmpty()) {
            capeId = null
        }
        this.capeId = capeId
    }

    fun getGeometryData(): String {
        if (geometryData == null) {
            return ""
        }
        return geometryData
    }

    fun setGeometryData(geometryData: String) {
        Preconditions.checkNotNull(geometryData, "geometryData")
        if (geometryData != this.geometryData) {
            this.geometryData = geometryData
        }
    }

    fun getAnimationData(): String {
        if (animationData == null) {
            return ""
        }
        return animationData
    }

    fun setAnimationData(animationData: String) {
        Preconditions.checkNotNull(animationData, "animationData")
        if (animationData != this.animationData) {
            this.animationData = animationData
        }
    }

    fun getAnimations(): MutableList<SkinAnimation?> {
        return animations
    }

    fun getPersonaPieces(): MutableList<PersonaPiece?> {
        return personaPieces
    }

    fun getTintColors(): MutableList<PersonaPieceTint?> {
        return tintColors
    }

    fun isPremium(): Boolean {
        return premium
    }

    fun setPremium(premium: Boolean) {
        this.premium = premium
    }

    fun isPersona(): Boolean {
        return persona
    }

    fun setPersona(persona: Boolean) {
        this.persona = persona
    }

    fun isCapeOnClassic(): Boolean {
        return capeOnClassic
    }

    fun setCapeOnClassic(capeOnClassic: Boolean) {
        this.capeOnClassic = capeOnClassic
    }

    fun isPrimaryUser(): Boolean {
        return primaryUser
    }

    fun setPrimaryUser(primaryUser: Boolean) {
        this.primaryUser = primaryUser
    }

    fun getGeometryDataEngineVersion(): String? {
        return geometryDataEngineVersion
    }

    fun setGeometryDataEngineVersion(geometryDataEngineVersion: String?) {
        this.geometryDataEngineVersion = geometryDataEngineVersion
    }

    fun isTrusted(): Boolean {
        return trusted
    }

    fun setTrusted(trusted: Boolean) {
        this.trusted = trusted
    }

    fun getSkinColor(): String? {
        return skinColor
    }

    fun setSkinColor(skinColor: String?) {
        this.skinColor = skinColor
    }

    fun getArmSize(): String? {
        return armSize
    }

    fun setArmSize(armSize: String?) {
        this.armSize = armSize
    }

    fun getFullSkinId(): String {
        if (fullSkinId == null) fullSkinId = skinId + (if (capeId != null) capeId else "")
        return fullSkinId!!
    }

    fun setFullSkinId(fullSkinId: String?) {
        this.fullSkinId = fullSkinId
    }

    fun getPlayFabId(): String? {
        if (this.persona && (this.playFabId == null || playFabId!!.isEmpty())) {
            try {
                this.playFabId =
                    skinId!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().get(5)
            } catch (e: Exception) {
                this.playFabId = getFullSkinId().replace("-", "").substring(16)
            }
        }
        return this.playFabId
    }

    fun setPlayFabId(playFabId: String?) {
        this.playFabId = playFabId
    }

    fun isOverridingPlayerAppearance(): Boolean {
        return this.overridingPlayerAppearance
    }

    fun setOverridingPlayerAppearance(override: Boolean) {
        this.overridingPlayerAppearance = override
    }

    companion object {
        val GEOMETRY_CUSTOM: String = convertLegacyGeometryName("geometry.humanoid.custom")
        val GEOMETRY_CUSTOM_SLIM: String = convertLegacyGeometryName("geometry.humanoid.customSlim")
        private const val PIXEL_SIZE: Int = 4
        @JvmField
        val SINGLE_SKIN_SIZE: Int = 32 * 32 * PIXEL_SIZE
        @JvmField
        val SKIN_64_32_SIZE: Int = 64 * 32 * PIXEL_SIZE
        @JvmField
        val DOUBLE_SKIN_SIZE: Int = 64 * 64 * PIXEL_SIZE
        @JvmField
        val SKIN_128_64_SIZE: Int = 128 * 64 * PIXEL_SIZE
        @JvmField
        val SKIN_128_128_SIZE: Int = 128 * 128 * PIXEL_SIZE
        private fun parseBufferedImage(image: BufferedImage): SerializedImage {
            val outputStream: FastByteArrayOutputStream = FastByteArrayOutputStream()
            for (y in 0..<image.getHeight()) {
                for (x in 0..<image.getWidth()) {
                    val color: Color = Color(image.getRGB(x, y), true)
                    outputStream.write(color.getRed())
                    outputStream.write(color.getGreen())
                    outputStream.write(color.getBlue())
                    outputStream.write(color.getAlpha())
                }
            }
            image.flush()
            return SerializedImage(image.getWidth(), image.getHeight(), outputStream.toByteArray())
        }

        private fun convertLegacyGeometryName(geometryName: String): String {
            return "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}"
        }
    }
}
