package org.chorus.blockentity

import cn.nukkit.block.BlockAir
import cn.nukkit.block.BlockCampfire
import cn.nukkit.block.BlockID
import cn.nukkit.event.inventory.CampfireSmeltEvent
import cn.nukkit.inventory.CampfireInventory
import cn.nukkit.item.Item
import cn.nukkit.item.ItemBlock
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.recipe.CampfireRecipe
import java.util.concurrent.ThreadLocalRandom

class BlockEntityCampfire(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt),
    BlockEntityInventoryHolder {
    private var inventory: CampfireInventory? = null
    private var burnTime: IntArray
    private var recipes: Array<CampfireRecipe?>
    private var keepItem: BooleanArray


    override fun initBlockEntity() {
        super.initBlockEntity()
        scheduleUpdate()
    }

    override fun loadNBT() {
        super.loadNBT()
        this.inventory = CampfireInventory(this)
        this.burnTime = IntArray(4)
        this.recipes = arrayOfNulls(4)
        this.keepItem = BooleanArray(4)
        for (i in 1..burnTime.size) {
            burnTime[i - 1] = namedTag.getInt("ItemTime$i")
            keepItem[i - 1] = namedTag.getBoolean("KeepItem" + 1)

            if (namedTag.contains("Item$i") && namedTag["Item$i"] is CompoundTag) {
                inventory!!.setItem(i - 1, NBTIO.getItemHelper(namedTag.getCompound("Item$i")))
            }
        }
    }

    override fun onUpdate(): Boolean {
        var needsUpdate = false
        val block = block
        val isLit = block is BlockCampfire && !block.isExtinguished
        for (slot in 0..<inventory!!.size) {
            val item = inventory!!.getItem(slot)
            if (item.isNull) {
                burnTime[slot] = 0
                recipes[slot] = null
            } else if (!keepItem[slot]) {
                var recipe = recipes[slot]
                if (recipe == null) {
                    recipe = server.recipeRegistry.findCampfireRecipe(item)
                    if (recipe == null) {
                        inventory!!.setItem(slot, Item.AIR)
                        val random = ThreadLocalRandom.current()
                        level.dropItem(
                            position.add(random.nextFloat().toDouble(), 0.5, random.nextFloat().toDouble()),
                            item
                        )
                        burnTime[slot] = 0
                        recipes[slot] = null
                        continue
                    } else {
                        burnTime[slot] = 600
                        recipes[slot] = recipe
                    }
                }

                val burnTimeLeft = burnTime[slot]
                if (burnTimeLeft <= 0) {
                    val product = Item.get(recipe.result.id, recipe.result.damage, item.getCount())
                    val event = CampfireSmeltEvent(this, item, product)
                    if (!event.isCancelled) {
                        inventory!!.setItem(slot, Item.AIR)
                        val random = ThreadLocalRandom.current()
                        level.dropItem(
                            position.add(random.nextFloat().toDouble(), 0.5, random.nextFloat().toDouble()),
                            event.result
                        )
                        burnTime[slot] = 0
                        recipes[slot] = null
                    } else if (event.keepItem) {
                        keepItem[slot] = true
                        burnTime[slot] = 0
                        recipes[slot] = null
                    }
                } else if (isLit) {
                    burnTime[slot]--
                    needsUpdate = true
                } else {
                    burnTime[slot] = 600
                }
            }
        }

        return needsUpdate
    }

    fun getKeepItem(slot: Int): Boolean {
        if (slot < 0 || slot >= keepItem.size) {
            return false
        }
        return keepItem[slot]
    }

    fun setKeepItem(slot: Int, keep: Boolean) {
        if (slot < 0 || slot >= keepItem.size) {
            return
        }
        keepItem[slot] = keep
    }

    override fun saveNBT() {
        super.saveNBT()
        for (i in 1..burnTime.size) {
            val item = inventory!!.getItem(i - 1)
            if (item == null || item.id === BlockID.AIR || item.getCount() <= 0) {
                namedTag.remove("Item$i")
                namedTag.putInt("ItemTime$i", 0)
                namedTag.remove("KeepItem$i")
            } else {
                namedTag.putCompound("Item$i", NBTIO.putItemHelper(item))
                namedTag.putInt("ItemTime$i", burnTime[i - 1])
                namedTag.putBoolean("KeepItem$i", keepItem[i - 1])
            }
        }
    }

    fun setRecipe(index: Int, recipe: CampfireRecipe?) {
        recipes[index] = recipe
    }

    override fun close() {
        if (!closed) {
            for (player in HashSet(getInventory().viewers)) {
                player.removeWindow(this.getInventory())
            }
            super.close()
        }
    }

    override fun onBreak(isSilkTouch: Boolean) {
        for (content in inventory!!.contents.values) {
            level.dropItem(this.position, content)
        }
    }

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Campfire"
        set(name) {
            if (name == null || name.isBlank()) {
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
            val c = super.getSpawnCompound()

            for (i in 1..burnTime.size) {
                val item = inventory!!.getItem(i - 1)
                if (item.isNull) {
                    c!!.remove("Item$i")
                } else {
                    c!!.putCompound("Item$i", NBTIO.putItemHelper(item))
                }
            }

            return c!!
        }

    override val isBlockEntityValid: Boolean
        get() = block.id === BlockID.CAMPFIRE

    val size: Int
        get() = 4

    fun getItem(index: Int): Item {
        if (index < 0 || index >= size) {
            return ItemBlock(BlockAir(), 0, 0)
        } else {
            val data = namedTag.getCompound("Item" + (index + 1))
            return NBTIO.getItemHelper(data)
        }
    }

    fun setItem(index: Int, item: Item?) {
        if (index < 0 || index >= size) {
            return
        }

        val nbt = NBTIO.putItemHelper(item)
        namedTag.putCompound("Item" + (index + 1), nbt)
    }

    override fun getInventory(): CampfireInventory {
        return inventory!!
    }
}
