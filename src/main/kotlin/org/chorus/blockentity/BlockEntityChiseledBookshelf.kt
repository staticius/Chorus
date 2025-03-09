package org.chorus.blockentity

import cn.nukkit.api.DoNotModify
import cn.nukkit.block.BlockChiseledBookshelf
import cn.nukkit.item.Item
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import com.google.common.base.Preconditions
import kotlin.math.pow

class BlockEntityChiseledBookshelf(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    private var lastInteractedSlot: Int? = null

    @get:DoNotModify
    var items: Array<Item?>
        private set

    override val isBlockEntityValid: Boolean
        get() = block is BlockChiseledBookshelf

    override fun saveNBT() {
        super.saveNBT()
        addBookshelfNbt(namedTag)
    }

    fun removeBook(index: Int): Item? {
        Preconditions.checkArgument(index >= 0 && index <= 5)
        val remove = items[index]
        lastInteractedSlot = index
        setBook(null, index)
        return remove
    }

    fun hasBook(index: Int): Boolean {
        Preconditions.checkArgument(index >= 0 && index <= 5)
        return items[index] != null && !items[index]!!.isNull
    }

    val booksStoredBit: Int
        get() {
            var sum = 0
            for (i in 0..5) {
                if (items[i] != null && !items[i]!!.isNull) {
                    sum = (sum + 2.0.pow(i.toDouble())).toInt()
                }
            }
            return sum
        }

    fun setBook(item: Item?, index: Int) {
        Preconditions.checkArgument(index >= 0 && index <= 5)
        items[index] = item
        setDirty()
    }

    override val spawnCompound: CompoundTag
        get() {
            val compoundTag =
                super.getSpawnCompound().putBoolean("isMovable", this.isMovable)
            addBookshelfNbt(compoundTag)
            return compoundTag
        }

    override fun setDirty() {
        this.spawnToAll()
        super.setDirty()
    }

    override fun loadNBT() {
        super.loadNBT()
        items = arrayOf(Item.AIR, Item.AIR, Item.AIR, Item.AIR, Item.AIR, Item.AIR)
        if (namedTag.containsInt(LAST_INTERACTED_SLOT)) {
            this.lastInteractedSlot = namedTag.getInt(LAST_INTERACTED_SLOT)
        }
        if (namedTag.containsList("Items")) {
            val items = namedTag.getList("Items", CompoundTag::class.java)
            if (items.size() > 6) return
            val all = items.all
            for (i in all.indices) {
                val compoundTag = all[i]
                val name = compoundTag.getString("Name")
                if (name == "") {
                    this.items[i] = null
                    continue
                }
                val item = Item.get(name)
                item.damage = compoundTag.getByte("Damage").toInt()
                item.setCount(compoundTag.getByte("Count").toInt())
                if (compoundTag.containsCompound("tag")) {
                    item.setNamedTag(compoundTag.getCompound("tag"))
                }
                this.items[i] = item
            }
        }
    }

    private fun addBookshelfNbt(namedTag: CompoundTag) {
        if (lastInteractedSlot != null) {
            namedTag.putInt(LAST_INTERACTED_SLOT, lastInteractedSlot!!)
        }
        val compoundTagListTag = ListTag<CompoundTag>()
        for (item in items) {
            if (item == null || item.isNull) {
                compoundTagListTag.add(
                    CompoundTag()
                        .putByte("Count", 0)
                        .putString("Name", "")
                        .putByte("Damage", 0)
                        .putBoolean("WasPickedUp", false)
                )
            } else {
                val compoundTag = CompoundTag()
                    .putByte("Count", item.getCount())
                    .putString("Name", item.id)
                    .putByte("Damage", item.damage)
                    .putBoolean("WasPickedUp", false)
                if (item.hasCompoundTag()) {
                    compoundTag.putCompound("tag", item.namedTag)
                }
                compoundTagListTag.add(compoundTag)
            }
        }
        namedTag.putList("Items", compoundTagListTag)
    }

    companion object {
        const val LAST_INTERACTED_SLOT: String = "LastInteractedSlot"
    }
}
