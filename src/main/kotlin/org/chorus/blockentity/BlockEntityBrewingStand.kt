package org.chorus.blockentity

import cn.nukkit.block.BlockBrewingStand
import cn.nukkit.block.BlockID
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.event.inventory.BrewEvent
import cn.nukkit.event.inventory.StartBrewEvent
import cn.nukkit.inventory.BrewingInventory
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.InventorySlice
import cn.nukkit.inventory.RecipeInventoryHolder
import cn.nukkit.item.Item
import cn.nukkit.item.ItemID
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.ContainerSetDataPacket
import cn.nukkit.recipe.ContainerRecipe
import cn.nukkit.recipe.MixRecipe
import cn.nukkit.registry.Registries

class BlockEntityBrewingStand(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt),
    RecipeInventoryHolder,
    BlockEntityInventoryHolder {
    protected var inventory: BrewingInventory? = null

    var brewTime: Int = 0
    var fuelTotal: Int = 0
    var fuel: Int = 0

    override fun initBlockEntity() {
        super.initBlockEntity()
        if (brewTime < MAX_BREW_TIME) {
            this.scheduleUpdate()
        }
    }

    override fun loadNBT() {
        super.loadNBT()
        inventory = BrewingInventory(this)
        if (!namedTag.contains("Items") || namedTag["Items"] !is ListTag<*>) {
            namedTag.putList("Items", ListTag())
        }

        for (i in 0..<size) {
            inventory!!.setItem(i, this.getItem(i))
        }

        if (!namedTag.contains("CookTime") || namedTag.getShort("CookTime") > MAX_BREW_TIME) {
            this.brewTime = MAX_BREW_TIME
        } else {
            this.brewTime = namedTag.getShort("CookTime").toInt()
        }

        this.fuel = namedTag.getShort("FuelAmount").toInt()
        this.fuelTotal = namedTag.getShort("FuelTotal").toInt()
    }

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else "Brewing Stand"
        set(name) {
            if (name == null || name == "") {
                namedTag.remove("CustomName")
                return
            }

            namedTag.putString("CustomName", name)
        }

    override fun hasName(): Boolean {
        return namedTag.contains("CustomName")
    }

    override fun close() {
        if (!closed) {
            for (player in HashSet(getInventory().viewers)) {
                player.removeWindow(getInventory())
            }
            super.close()
        }
    }

    override fun onBreak(isSilkTouch: Boolean) {
        for (content in inventory!!.contents.values) {
            level.dropItem(this.position, content)
        }
        inventory!!.clearAll()
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putList("Items", ListTag())
        for (index in 0..<size) {
            this.setItem(index, inventory!!.getItem(index))
        }

        namedTag.putShort("CookTime", brewTime)
        namedTag.putShort("FuelAmount", this.fuel)
        namedTag.putShort("FuelTotal", this.fuelTotal)
    }

    override val isBlockEntityValid: Boolean
        get() = block.id === BlockID.BREWING_STAND

    val size: Int
        get() = 5

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

        if (item.id === BlockID.AIR || item.getCount() <= 0) {
            if (i >= 0) {
                namedTag.getList("Items").all.removeAt(i)
            }
        } else if (i < 0) {
            (namedTag.getList("Items", CompoundTag::class.java)).add(d)
        } else {
            (namedTag.getList("Items", CompoundTag::class.java)).add(i, d)
        }
    }

    override fun getInventory(): BrewingInventory {
        return inventory!!
    }

    override fun onUpdate(): Boolean {
        if (closed) {
            return false
        }

        restockFuel()

        if (this.fuel <= 0 || matchRecipes(true)[0] == null) {
            stopBrewing()
            return false
        }

        if (brewTime == MAX_BREW_TIME) {
            val e = StartBrewEvent(this)
            server.pluginManager.callEvent(e)

            if (e.isCancelled) {
                return false
            }

            this.sendBrewTime()
        }

        if (--brewTime > 0) {
            if (brewTime % 40 == 0) {
                sendBrewTime()
            }

            return true
        }

        //20 seconds
        val e = BrewEvent(this)
        server.pluginManager.callEvent(e)

        if (e.isCancelled) {
            stopBrewing()
            return true
        }

        var mixed = false
        val recipes = matchRecipes(false)
        for (i in 0..2) {
            val recipe = recipes[i] ?: continue

            val previous = inventory!!.getItem(i + 1)
            if (!previous.isNull) {
                val result = recipe.result
                result.setCount(previous.getCount())
                if (recipe is ContainerRecipe) {
                    result.damage = previous.damage
                }
                inventory!!.setItem(i + 1, result)
                mixed = true
            }
        }

        if (mixed) {
            val ingredient = inventory!!.ingredient
            ingredient.count--
            inventory!!.ingredient = ingredient

            fuel--
            this.sendFuel()

            level.addSound(this.position, Sound.RANDOM_POTION_BREWED)
        }

        stopBrewing()
        return true
    }

    private fun restockFuel() {
        val fuel = getInventory().fuel
        if (this.fuel > 0 || fuel.id !== ItemID.BLAZE_POWDER || fuel.getCount() <= 0) {
            return
        }

        fuel.count--
        this.fuel = 20
        this.fuelTotal = 20

        inventory!!.fuel = fuel
        this.sendFuel()
    }

    private fun stopBrewing() {
        this.brewTime = 0
        this.sendBrewTime()
        this.brewTime = MAX_BREW_TIME
    }

    private fun matchRecipes(quickTest: Boolean): Array<MixRecipe?> {
        val recipes = arrayOfNulls<MixRecipe>(if (quickTest) 1 else 3)
        val ingredient = inventory!!.ingredient
        for (i in 0..2) {
            val potion = inventory!!.getItem(i + 1)
            if (potion.isNull) {
                continue
            }

            var recipe: MixRecipe? = Registries.RECIPE.findBrewingRecipe(ingredient, potion)
            if (recipe == null) {
                recipe = Registries.RECIPE.findContainerRecipe(ingredient, potion)
            }
            if (recipe == null) {
                continue
            }

            if (quickTest) {
                recipes[0] = recipe
                return recipes
            }

            recipes[i] = recipe
        }

        return recipes
    }

    protected fun sendFuel() {
        for (p in inventory!!.viewers) {
            val windowId = p.getWindowId(inventory!!)
            if (windowId > 0) {
                val pk1 = ContainerSetDataPacket()
                pk1.windowId = windowId
                pk1.property = ContainerSetDataPacket.PROPERTY_BREWING_STAND_FUEL_AMOUNT
                pk1.value = this.fuel
                p.dataPacket(pk1)

                val pk2 = ContainerSetDataPacket()
                pk2.windowId = windowId
                pk2.property = ContainerSetDataPacket.PROPERTY_BREWING_STAND_FUEL_TOTAL
                pk2.value = this.fuelTotal
                p.dataPacket(pk2)
            }
        }
    }

    protected fun sendBrewTime() {
        val pk = ContainerSetDataPacket()
        pk.property = ContainerSetDataPacket.PROPERTY_BREWING_STAND_BREW_TIME
        pk.value = brewTime

        for (p in inventory!!.viewers) {
            val windowId = p.getWindowId(inventory!!)
            if (windowId > 0) {
                pk.windowId = windowId

                p.dataPacket(pk)
            }
        }
    }

    fun updateBlock() {
        val block = this.levelBlock as? BlockBrewingStand ?: return

        for (i in 1..3) {
            val potion = inventory!!.getItem(i)

            val id = potion.id
            if ((id === ItemID.POTION || id === ItemID.SPLASH_POTION || id === ItemID.LINGERING_POTION) && potion.getCount() > 0) {
                when (i) {
                    1 -> block.setPropertyValue(CommonBlockProperties.BREWING_STAND_SLOT_A_BIT, true)
                    2 -> block.setPropertyValue(CommonBlockProperties.BREWING_STAND_SLOT_B_BIT, true)
                    3 -> block.setPropertyValue(CommonBlockProperties.BREWING_STAND_SLOT_C_BIT, true)
                }
            } else {
                when (i) {
                    1 -> block.setPropertyValue(CommonBlockProperties.BREWING_STAND_SLOT_A_BIT, false)
                    2 -> block.setPropertyValue(CommonBlockProperties.BREWING_STAND_SLOT_B_BIT, false)
                    3 -> block.setPropertyValue(CommonBlockProperties.BREWING_STAND_SLOT_C_BIT, false)
                }
            }
        }
        level.setBlock(block.position, block, false, false)

        if (brewTime != MAX_BREW_TIME && matchRecipes(true)[0] == null) {
            stopBrewing()
        }
    }

    override val spawnCompound: CompoundTag
        get() {
            val nbt = super.getSpawnCompound()
                .putBoolean("isMovable", this.isMovable)
                .putShort("FuelTotal", this.fuelTotal)
                .putShort("FuelAmount", this.fuel)

            if (this.brewTime < MAX_BREW_TIME) {
                nbt.putShort("CookTime", this.brewTime)
            }

            if (this.hasName()) {
                nbt.put("CustomName", namedTag["CustomName"])
            }

            return nbt
        }

    override fun getIngredientView(): Inventory {
        return InventorySlice(inventory!!, 0, 1)
    }

    override fun getProductView(): Inventory {
        return InventorySlice(inventory!!, 1, 4)
    }

    companion object {
        const val MAX_BREW_TIME: Int = 400
    }
}
