package org.chorus.blockentity

import org.chorus.block.*
import org.chorus.event.inventory.FurnaceBurnEvent
import org.chorus.event.inventory.FurnaceSmeltEvent
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.network.protocol.ContainerSetDataPacket
import org.chorus.recipe.SmeltingRecipe
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ceil
import kotlin.math.floor

/**
 * @author MagicDroidX
 */
open class BlockEntityFurnace(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt),
    RecipeInventoryHolder,
    BlockEntityInventoryHolder {
    protected var inventory: SmeltingInventory? = null

    var burnTime: Int = 0
    var burnDuration: Int = 0
    var cookTime: Int = 0
    var maxTime: Int = 0
    @JvmField
    var storedXP: Float = 0f
    private var crackledTime = 0

    protected open fun createInventory(): SmeltingInventory {
        return FurnaceTypeInventory(this)
    }

    override fun initBlockEntity() {
        super.initBlockEntity()
        if (burnTime > 0) {
            this.scheduleUpdate()
        }
    }

    override fun loadNBT() {
        super.loadNBT()
        this.inventory = createInventory()

        if (!namedTag.contains("Items") || namedTag["Items"] !is ListTag<*>) {
            namedTag.putList("Items", ListTag<CompoundTag>())
        }

        for (i in 0..<this.size) {
            inventory!!.setItem(i, this.getItem(i))
        }

        burnTime =
            if (!namedTag.contains("BurnTime") || namedTag.getShort("BurnTime") < 0) {
                0
            } else {
                namedTag.getShort("BurnTime").toInt()
            }

        cookTime =
            if (!namedTag.contains("CookTime") || namedTag.getShort("CookTime") < 0 || (namedTag.getShort(
                    "BurnTime"
                ).toInt() == 0 && namedTag.getShort("CookTime") > 0)
            ) {
                0
            } else {
                namedTag.getShort("CookTime").toInt()
            }

        burnDuration =
            if (!namedTag.contains("BurnDuration") || namedTag.getShort("BurnDuration") < 0) {
                0
            } else {
                namedTag.getShort("BurnDuration").toInt()
            }

        if (!namedTag.contains("MaxTime")) {
            maxTime = burnTime
            burnDuration = 0
        } else {
            maxTime = namedTag.getShort("MaxTime").toInt()
        }

        if (namedTag.contains("BurnTicks")) {
            burnDuration = namedTag.getShort("BurnTicks").toInt()
            namedTag.remove("BurnTicks")
        }

        storedXP = if (namedTag.contains("StoredXpInt")) {
            namedTag.getShort("StoredXpInt").toFloat()
        } else {
            0f
        }
    }

    protected open val furnaceName: String
        get() = "Furnace"

    protected open val clientName: String
        get() = BlockEntityID.Companion.FURNACE

    override var name: String
        get() = if (this.hasName()) namedTag.getString("CustomName") else furnaceName
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
                player.removeWindow(this.getInventory())
            }
            super.close()
        }
    }

    override fun onBreak(isSilkTouch: Boolean) {
        for (content in inventory!!.contents.values) {
            level.dropItem(this.position, content)
        }
        inventory!!.clearAll()
        val xp = calculateXpDrop()
        if (xp > 0) {
            storedXP = 0f
            level.dropExpOrb(this.position, xp.toInt())
        }
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putList("Items", ListTag<CompoundTag>())
        for (index in 0..<this.size) {
            this.setItem(index, inventory!!.getItem(index))
        }
        namedTag.putShort("CookTime", cookTime)
        namedTag.putShort("BurnTime", burnTime)
        namedTag.putShort("BurnDuration", burnDuration)
        namedTag.putShort("MaxTime", maxTime)
        namedTag.putShort("StoredXpInt", storedXP.toInt())
    }

    override val isBlockEntityValid: Boolean
        get() {
            val BlockID.= block.id
            return BlockID.=== idleBlockID.|| BlockID.=== burningBlockId
        }

    val size: Int
        get() = 3

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

        if (item.isNull) {
            if (i >= 0) {
                namedTag.getList("Items").all.removeAt(i)
            }
        } else if (i < 0) {
            (namedTag.getList("Items", CompoundTag::class.java)).add(d)
        } else {
            (namedTag.getList("Items", CompoundTag::class.java)).add(i, d)
        }
    }

    override fun getInventory(): SmeltingInventory {
        return inventory!!
    }

    protected open val idleBlockID.String
        get() = Block.FURNACE

    protected open val burningBlockID.String
        get() = Block.LIT_FURNACE

    protected fun setBurning(burning: Boolean) {
        if (burning) {
            if (this.block.id == this.idleBlockID.{
                level.setBlock(
                    this.position, Block.getWithState(
                        burningBlockID.this.block.blockState
                    ), true
                )
            }
        } else if (this.block.id == this.burningBlockID.{
            level.setBlock(
                this.position, Block.getWithState(
                    idleBlockID.this.block.blockState
                ), true
            )
        }
    }

    protected fun checkFuel(fuel: Item) {
        var fuel = fuel
        val ev = FurnaceBurnEvent(this, fuel, if (fuel.fuelTime == null) 0 else fuel.fuelTime)
        server.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return
        }

        maxTime = ceil((ev.burnTime / speedMultiplier.toFloat()).toDouble()).toInt()
        burnTime = ceil((ev.burnTime / speedMultiplier.toFloat()).toDouble()).toInt()
        burnDuration = 0
        setBurning(true)

        if (burnTime > 0 && ev.isBurning) {
            fuel.setCount(fuel.getCount() - 1)
            if (fuel.getCount() == 0) {
                if (fuel is ItemLavaBucket) {
                    fuel.setDamage(0)
                    fuel.setCount(1)
                } else {
                    fuel = Item.AIR
                }
            }
            inventory!!.setFuel(fuel)
        }
    }

    protected open fun matchRecipe(raw: Item): SmeltingRecipe? {
        return server.recipeRegistry.findFurnaceRecipe(raw)
    }

    protected open val speedMultiplier: Int
        get() = 1

    override fun onUpdate(): Boolean {
        if (this.closed) {
            return false
        }

        var ret = false
        val fuel = inventory!!.fuel
        var raw = inventory!!.smelting
        var product = inventory!!.result
        val smelt = matchRecipe(raw)

        var canSmelt = false
        if (smelt != null) {
            canSmelt = (raw.getCount() > 0 && ((smelt.result.equals(
                product,
                true
            ) && product.getCount() < product.maxStackSize) || product.id === BlockID.AIR))
            //检查输入
            if (!smelt.input.toItem().equals(raw, true, false)) {
                canSmelt = false
            }
        }

        if (burnTime <= 0 && canSmelt && fuel.fuelTime != null && fuel.getCount() > 0) {
            this.checkFuel(fuel)
        }

        if (burnTime > 0) {
            burnTime--
            val readyAt = 200 / speedMultiplier
            burnDuration = ceil((burnTime.toFloat() / maxTime * readyAt).toDouble()).toInt()

            if (crackledTime-- <= 0) {
                this.crackledTime = ThreadLocalRandom.current().nextInt(20, 100)
                level.addSound(position.add(0.5, 0.5, 0.5), Sound.BLOCK_FURNACE_LIT)
            }

            if (smelt != null && canSmelt) {
                cookTime++
                if (cookTime >= readyAt) {
                    val count = product.getCount() + 1
                    product = smelt.result.clone()
                    product.setCount(count)

                    val ev = FurnaceSmeltEvent(
                        this, raw, product,
                        server.recipeRegistry.getRecipeXp(smelt).toFloat()
                    )
                    server.pluginManager.callEvent(ev)
                    if (!ev.isCancelled) {
                        inventory!!.setResult(ev.result)
                        raw.setCount(raw.getCount() - 1)
                        if (raw.getCount() == 0) {
                            raw = Item.AIR
                        }
                        this.storedXP += ev.xp
                        inventory!!.setSmelting(raw)
                    }

                    cookTime -= readyAt
                }
            } else if (burnTime <= 0) {
                burnTime = 0
                cookTime = 0
                burnDuration = 0
            } else {
                cookTime = 0
            }
            ret = true
        } else {
            setBurning(false)
            burnTime = 0
            cookTime = 0
            burnDuration = 0
            this.crackledTime = 0
        }

        for (player in getInventory().viewers) {
            val windowId = player.getWindowId(this.getInventory())
            if (windowId > 0) {
                var pk = ContainerSetDataPacket()
                pk.windowId = windowId
                pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_TICK_COUNT
                pk.value = cookTime
                player.dataPacket(pk)

                pk = ContainerSetDataPacket()
                pk.windowId = windowId
                pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_LIT_TIME
                pk.value = burnDuration
                player.dataPacket(pk)
            }
        }

        return ret
    }

    override val spawnCompound: CompoundTag
        get() {
            val c = super.spawnCompound
                .putBoolean("isMovable", this.isMovable)
                .putShort("BurnDuration", burnDuration)
                .putShort("BurnTime", burnTime)
                .putShort("CookTime", cookTime)
                .putShort("StoredXpInt", storedXP.toInt())
            if (this.hasName()) {
                c.put("CustomName", namedTag["CustomName"])
            }
            return c
        }

    fun calculateXpDrop(): Short {
        return (floor(storedXP.toDouble()) + (if (ThreadLocalRandom.current()
                .nextFloat() < (this.storedXP % 1)
        ) 1 else 0)).toInt().toShort()
    }

    override fun getIngredientView(): Inventory {
        return InventorySlice(inventory!!, 0, 1)
    }

    override fun getProductView(): Inventory {
        return InventorySlice(inventory!!, 2, 3)
    }
}
