package cn.nukkit.blockentity

import cn.nukkit.block.BlockUndyedShulkerBox
import cn.nukkit.inventory.BaseInventory
import cn.nukkit.inventory.ShulkerBoxInventory
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag

/**
 * @author PetteriM1
 */
class BlockEntityShulkerBox(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt),
    BlockEntityInventoryHolder {
    var realInventory: ShulkerBoxInventory? = null
        protected set

    init {
        movable = true
    }

    override fun loadNBT() {
        super.loadNBT()
        this.realInventory = ShulkerBoxInventory(this)

        if (!namedTag.contains("Items") || namedTag["Items"] !is ListTag<*>) {
            namedTag.putList("Items", ListTag<CompoundTag>())
        }

        val list = namedTag.getList("Items") as ListTag<CompoundTag>
        for (compound in list.all) {
            val item = NBTIO.getItemHelper(compound)
            realInventory!!.setItemInternal(compound.getByte("Slot").toInt(), item)
        }

        if (!namedTag.contains("facing")) {
            namedTag.putByte("facing", 0)
        }
    }

    override fun close() {
        if (!closed) {
            for (player in HashSet(this.inventory.viewers)) {
                player.removeWindow(this.inventory)
            }
            super.close()
        }
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putList("Items", ListTag<CompoundTag>())
        for (index in 0..<this.size) {
            this.setItem(index, realInventory!!.getItem(index))
        }
    }

    override val isBlockEntityValid: Boolean
        get() {
            val block = this.block
            return block is BlockUndyedShulkerBox
        }

    val size: Int
        get() = 27

    protected fun getSlotIndex(index: Int): Int {
        val list = namedTag.getList("Items", CompoundTag::class.java)
        for (i in 0..<list.size()) {
            if (list[i].getByte("Slot").toInt() == index) {
                return i
            }
        }

        return -1
    }

    fun getItem(index: Int): Item {
        val i = this.getSlotIndex(index)
        if (i < 0) {
            return Item.AIR
        } else {
            val data = namedTag.getList("Items")[i] as CompoundTag
            return NBTIO.getItemHelper(data)
        }
    }

    fun setItem(index: Int, item: Item) {
        val i = this.getSlotIndex(index)

        val d = NBTIO.putItemHelper(item, index)

        if (item.isNull || item.getCount() <= 0) {
            if (i >= 0) {
                namedTag.getList("Items").remove(i)
            }
        } else if (i < 0) {
            (namedTag.getList("Items", CompoundTag::class.java)).add(d)
        } else {
            (namedTag.getList("Items", CompoundTag::class.java)).add(i, d)
        }
    }

    override fun getInventory(): BaseInventory {
        return realInventory!!
    }

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Shulker Box"
        set(name) {
            if (name == null || name.isEmpty()) {
                namedTag.remove("CustomName")
                return
            }

            namedTag.putString("CustomName", name)
        }

    override fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }

    override val spawnCompound: CompoundTag
        get() {
            val c: CompoundTag =
                BlockEntity.Companion.getDefaultCompound(
                    this.position,
                    BlockEntityID.Companion.SHULKER_BOX
                )
                    .putBoolean("isMovable", this.isMovable)
                    .putBoolean("Findable", false)
                    .putList(
                        "Items",
                        namedTag.getList<CompoundTag>("Items", CompoundTag::class.java)
                    )
                    .putByte("facing", namedTag.getByte("facing").toInt())

            if (this.hasName()) {
                c.put("CustomName", namedTag["CustomName"])
            }

            return c
        }
}
