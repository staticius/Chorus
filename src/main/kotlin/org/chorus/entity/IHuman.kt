package org.chorus.entity

import org.chorus.Player
import org.chorus.entity.data.*
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.level.*
import org.chorus.math.BlockVector3
import org.chorus.math.ChorusMath
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.nbt.tag.StringTag
import org.chorus.utils.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

interface IHuman : InventoryHolder {
    fun initHumanEntity(human: Entity) {
        human.setPlayerFlag(PlayerFlag.SLEEP)
        human.setDataFlag(EntityFlag.HAS_GRAVITY)
        human.setDataProperty(EntityDataTypes.Companion.BED_POSITION, BlockVector3(0, 0, 0), false)

        if (human !is Player) {
            if (human.namedTag!!.contains("NameTag")) {
                human.setNameTag(human.namedTag!!.getString("NameTag"))
            }

            if (human.namedTag!!.contains("Skin") && human.namedTag!!.get("Skin") is CompoundTag) {
                val skinTag: CompoundTag = human.namedTag!!.getCompound("Skin")
                if (!skinTag.contains("Transparent")) {
                    skinTag.putBoolean("Transparent", false)
                }
                val newSkin: Skin = Skin()
                if (skinTag.contains("ModelId")) {
                    newSkin.setSkinId(skinTag.getString("ModelId"))
                }
                if (skinTag.contains("PlayFabId")) {
                    newSkin.setPlayFabId(skinTag.getString("PlayFabId"))
                }
                if (skinTag.contains("Data")) {
                    val data: ByteArray = skinTag.getByteArray("Data")
                    if (skinTag.contains("SkinImageWidth") && skinTag.contains("SkinImageHeight")) {
                        val width: Int = skinTag.getInt("SkinImageWidth")
                        val height: Int = skinTag.getInt("SkinImageHeight")
                        newSkin.setSkinData(SerializedImage(width, height, data))
                    } else {
                        newSkin.setSkinData(data)
                    }
                }
                if (skinTag.contains("CapeId")) {
                    newSkin.setCapeId(skinTag.getString("CapeId"))
                }
                if (skinTag.contains("CapeData")) {
                    val data: ByteArray = skinTag.getByteArray("CapeData")
                    if (skinTag.contains("CapeImageWidth") && skinTag.contains("CapeImageHeight")) {
                        val width: Int = skinTag.getInt("CapeImageWidth")
                        val height: Int = skinTag.getInt("CapeImageHeight")
                        newSkin.setCapeData(SerializedImage(width, height, data))
                    } else {
                        newSkin.setCapeData(data)
                    }
                }
                if (skinTag.contains("GeometryName")) {
                    newSkin.setGeometryName(skinTag.getString("GeometryName"))
                }
                if (skinTag.contains("SkinResourcePatch")) {
                    newSkin.setSkinResourcePatch(
                        kotlin.String(
                            skinTag.getByteArray("SkinResourcePatch"),
                            StandardCharsets.UTF_8
                        )
                    )
                }
                if (skinTag.contains("GeometryData")) {
                    newSkin.setGeometryData(kotlin.String(skinTag.getByteArray("GeometryData"), StandardCharsets.UTF_8))
                }
                if (skinTag.contains("SkinAnimationData")) {
                    newSkin.setAnimationData(
                        kotlin.String(
                            skinTag.getByteArray("SkinAnimationData"),
                            StandardCharsets.UTF_8
                        )
                    )
                } else if (skinTag.contains("AnimationData")) { // backwards compatible
                    newSkin.setAnimationData(
                        kotlin.String(
                            skinTag.getByteArray("AnimationData"),
                            StandardCharsets.UTF_8
                        )
                    )
                }
                if (skinTag.contains("PremiumSkin")) {
                    newSkin.setPremium(skinTag.getBoolean("PremiumSkin"))
                }
                if (skinTag.contains("PersonaSkin")) {
                    newSkin.setPersona(skinTag.getBoolean("PersonaSkin"))
                }
                if (skinTag.contains("CapeOnClassicSkin")) {
                    newSkin.setCapeOnClassic(skinTag.getBoolean("CapeOnClassicSkin"))
                }
                if (skinTag.contains("AnimatedImageData")) {
                    val list: ListTag<CompoundTag> = skinTag.getList("AnimatedImageData", CompoundTag::class.java)
                    for (animationTag: CompoundTag in list.getAll()) {
                        val frames: Float = animationTag.getFloat("Frames")
                        val type: Int = animationTag.getInt("Type")
                        val image: ByteArray = animationTag.getByteArray("Image")
                        val width: Int = animationTag.getInt("ImageWidth")
                        val height: Int = animationTag.getInt("ImageHeight")
                        val expression: Int = animationTag.getInt("AnimationExpression")
                        newSkin.getAnimations()
                            .add(SkinAnimation(SerializedImage(width, height, image), type, frames, expression))
                    }
                }
                if (skinTag.contains("ArmSize")) {
                    newSkin.setArmSize(skinTag.getString("ArmSize"))
                }
                if (skinTag.contains("SkinColor")) {
                    newSkin.setSkinColor(skinTag.getString("SkinColor"))
                }
                if (skinTag.contains("PersonaPieces")) {
                    val pieces: ListTag<CompoundTag> = skinTag.getList("PersonaPieces", CompoundTag::class.java)
                    for (piece: CompoundTag in pieces.getAll()) {
                        newSkin.getPersonaPieces().add(
                            PersonaPiece(
                                piece.getString("PieceId"),
                                piece.getString("PieceType"),
                                piece.getString("PackId"),
                                piece.getBoolean("IsDefault"),
                                piece.getString("ProductId")
                            )
                        )
                    }
                }
                if (skinTag.contains("PieceTintColors")) {
                    val tintColors: ListTag<CompoundTag> = skinTag.getList("PieceTintColors", CompoundTag::class.java)
                    for (tintColor: CompoundTag in tintColors.getAll()) {
                        newSkin.getTintColors().add(
                            PersonaPieceTint(
                                tintColor.getString("PieceType"),
                                tintColor.getList("Colors", StringTag::class.java).getAll().stream()
                                    .map(Function { stringTag: StringTag -> stringTag.data })
                                    .collect(Collectors.toList())
                            )
                        )
                    }
                }
                if (skinTag.contains("IsTrustedSkin")) {
                    newSkin.setTrusted(skinTag.getBoolean("IsTrustedSkin"))
                }
                this.setSkin(newSkin)
            }

            if (this.getSkin() == null) {
                this.setSkin(Skin())
            }
            this.setUniqueId(
                Utils.dataToUUID(
                    java.lang.String.valueOf(human.getId()).getBytes(StandardCharsets.UTF_8),
                    getSkin()!!.getSkinData().data, human.getNameTag().getBytes(StandardCharsets.UTF_8)
                )
            )
        }

        this.setInventories(
            arrayOf(
                HumanInventory(this),
                HumanOffHandInventory(this),
                HumanEnderChestInventory(this)
            )
        )

        if (human.namedTag!!.containsNumber("SelectedInventorySlot")) {
            getInventory().setHeldItemSlot(ChorusMath.clamp(human.namedTag!!.getInt("SelectedInventorySlot"), 0, 8))
        }

        if (human.namedTag!!.contains("Inventory") && human.namedTag!!.get("Inventory") is ListTag<*>) {
            val inventory: HumanInventory = this.getInventory()
            val inventoryList: ListTag<CompoundTag> = human.namedTag!!.getList("Inventory", CompoundTag::class.java)
            for (item: CompoundTag in inventoryList.getAll()) {
                val slot: Int = item.getByte("Slot").toInt()
                inventory.setItem(slot, NBTIO.getItemHelper(item)) //inventory 0-39
            }
        }
        if (human.namedTag!!.containsCompound("OffInventory")) {
            val offhandInventory: HumanOffHandInventory? = getOffhandInventory()
            val offHand: CompoundTag = human.namedTag!!.getCompound("OffInventory")
            offhandInventory!!.setItem(0, NBTIO.getItemHelper(offHand)) //offinventory index 0
        }
        if (human.namedTag!!.contains("EnderItems") && human.namedTag!!.get("EnderItems") is ListTag<*>) {
            val inventoryList: ListTag<CompoundTag> = human.namedTag!!.getList("EnderItems", CompoundTag::class.java)
            for (item: CompoundTag in inventoryList.getAll()) { //enderItems index 0-26
                (human as EntityHumanType).getEnderChestInventory()!!
                    .setItem(item.getByte("Slot").toInt(), NBTIO.getItemHelper(item))
            }
        }
    }

    fun saveHumanEntity(human: Entity) {
        //EntityHumanType
        var inventoryTag: ListTag<CompoundTag?>? = null
        if (this.getInventory() != null) {
            inventoryTag = ListTag()
            human.namedTag!!.putList("Inventory", inventoryTag)

            for (entry: Map.Entry<Int?, Item?> in getInventory().getContents().entrySet()) {
                inventoryTag.add(NBTIO.putItemHelper(entry.getValue(), entry.getKey()))
            }

            human.namedTag!!.putInt("SelectedInventorySlot", getInventory().getHeldItemIndex())
        }

        if (this.getOffhandInventory() != null) {
            val item: Item = getOffhandInventory()!!.getItem(0)
            human.namedTag!!.putCompound("OffInventory", NBTIO.putItemHelper(item, 0))
        }

        human.namedTag!!.putList("EnderItems", ListTag<CompoundTag>())
        if (this.getEnderChestInventory() != null) {
            val enderItems: ListTag<CompoundTag> = human.namedTag!!.getList("EnderItems", CompoundTag::class.java)
            for (slot in 0..<getEnderChestInventory()!!.size) {
                val item: Item = getEnderChestInventory()!!.getItem(slot)
                if (!item.isNull()) {
                    enderItems.add(NBTIO.putItemHelper(item, slot))
                }
            }
            human.namedTag!!.putList("EnderItems", enderItems)
        }

        //EntityHuman
        val skin: Skin? = getSkin()
        if (skin != null) {
            val skinTag: CompoundTag = CompoundTag()
                .putByteArray("Data", skin.getSkinData().data)
                .putInt("SkinImageWidth", skin.getSkinData().width)
                .putInt("SkinImageHeight", skin.getSkinData().height)
                .putString("ModelId", skin.getSkinId()!!)
                .putString("CapeId", skin.getCapeId())
                .putByteArray("CapeData", skin.getCapeData().data)
                .putInt("CapeImageWidth", skin.getCapeData().width)
                .putInt("CapeImageHeight", skin.getCapeData().height)
                .putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8))
                .putByteArray("SkinAnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8))
                .putBoolean("PremiumSkin", skin.isPremium())
                .putBoolean("PersonaSkin", skin.isPersona())
                .putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic())
                .putString("ArmSize", skin.getArmSize()!!)
                .putString("SkinColor", skin.getSkinColor()!!)
                .putBoolean("IsTrustedSkin", skin.isTrusted())

            val animations: List<SkinAnimation?> = skin.getAnimations()
            if (!animations.isEmpty()) {
                val animationsTag: ListTag<CompoundTag> = ListTag()
                for (animation: SkinAnimation in animations) {
                    animationsTag.add(
                        CompoundTag()
                            .putFloat("Frames", animation.frames)
                            .putInt("Type", animation.type)
                            .putInt("ImageWidth", animation.image.width)
                            .putInt("ImageHeight", animation.image.height)
                            .putInt("AnimationExpression", animation.expression)
                            .putByteArray("Image", animation.image.data)
                    )
                }
                skinTag.putList("AnimatedImageData", animationsTag)
            }

            val personaPieces: List<PersonaPiece?> = skin.getPersonaPieces()
            if (!personaPieces.isEmpty()) {
                val piecesTag: ListTag<CompoundTag> = ListTag()
                for (piece: PersonaPiece in personaPieces) {
                    piecesTag.add(
                        CompoundTag().putString("PieceId", piece.id)
                            .putString("PieceType", piece.type)
                            .putString("PackId", piece.packId)
                            .putBoolean("IsDefault", piece.isDefault)
                            .putString("ProductId", piece.productId)
                    )
                }
                skinTag.putList("PersonaPieces", piecesTag)
            }
            val tints: List<PersonaPieceTint?> = skin.getTintColors()
            if (!tints.isEmpty()) {
                val tintsTag: ListTag<CompoundTag> = ListTag()
                for (tint: PersonaPieceTint in tints) {
                    val colors: ListTag<StringTag> = ListTag()
                    colors.setAll(tint.colors.stream().map(Function { data: String? ->
                        StringTag(
                            data!!
                        )
                    }).collect(Collectors.toList()))
                    tintsTag.add(
                        CompoundTag()
                            .putString("PieceType", tint.pieceType)
                            .putList("Colors", colors)
                    )
                }
                skinTag.putList("PieceTintColors", tintsTag)
            }

            if (!skin.getPlayFabId()!!.isEmpty()) {
                skinTag.putString("PlayFabId", skin.getPlayFabId()!!)
            }

            human.namedTag!!.putCompound("Skin", skinTag)
        }
    }

    fun setSkin(skin: Skin?)

    fun getSkin(): Skin?

    fun getUniqueId(): UUID?

    fun setUniqueId(uuid: UUID?)

    fun setInventories(inventory: Array<Inventory>)

    override fun getInventory(): HumanInventory

    fun getOffhandInventory(): HumanOffHandInventory?

    fun getEnderChestInventory(): HumanEnderChestInventory?

    override fun getLevel(): Level

    fun getEntity(): Entity {
        return this as Entity
    }

    companion object {
        const val NETWORK_ID: Int = 257
    }
}
