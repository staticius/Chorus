package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.AdventureSettings
import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.blockentity.BlockEntitySpawnable
import org.chorus_oss.chorus.entity.EntityLiving
import org.chorus_oss.chorus.entity.data.EntityFlag
import org.chorus_oss.chorus.entity.mob.EntityArmorStand
import org.chorus_oss.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus_oss.chorus.event.entity.EntityDamageEvent
import org.chorus_oss.chorus.event.entity.EntityDamageEvent.DamageModifier
import org.chorus_oss.chorus.event.player.*
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.enchantment.Enchantment
import org.chorus_oss.chorus.level.GameRule
import org.chorus_oss.chorus.level.Sound
import org.chorus_oss.chorus.level.vibration.VibrationEvent
import org.chorus_oss.chorus.level.vibration.VibrationType
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.InventoryTransactionPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.UpdateBlockPacket
import org.chorus_oss.chorus.network.protocol.types.inventory.transaction.InventorySource
import org.chorus_oss.chorus.network.protocol.types.inventory.transaction.ReleaseItemData
import org.chorus_oss.chorus.network.protocol.types.inventory.transaction.UseItemData
import org.chorus_oss.chorus.network.protocol.types.inventory.transaction.UseItemOnEntityData
import org.chorus_oss.chorus.utils.Loggable
import java.util.*

class InventoryTransactionProcessor : DataPacketProcessor<InventoryTransactionPacket>() {
    private var lastUsedItem: Item? = null

    override fun handle(playerHandle: PlayerHandle, pk: InventoryTransactionPacket) {
        val player = playerHandle.player
        if (player.isSpectator) {
            player.sendAllInventories()
            return
        }
        if (pk.transactionType == InventoryTransactionPacket.TransactionType.USE_ITEM) {
            handleUseItem(playerHandle, pk)
        } else if (pk.transactionType == InventoryTransactionPacket.TransactionType.USE_ITEM_ON_ENTITY) {
            handleUseItemOnEntity(playerHandle, pk)
        } else if (pk.transactionType == InventoryTransactionPacket.TransactionType.RELEASE_ITEM) {
            val releaseItemData = pk.transactionData as ReleaseItemData
            try {
                val type = releaseItemData.actionType
                when (type) {
                    InventoryTransactionPacket.RELEASE_ITEM_ACTION_RELEASE -> {
                        val lastUseTick = player.getLastUseTick(releaseItemData.itemInHand.id)
                        if (lastUseTick != -1) {
                            val item = player.inventory.itemInHand

                            val ticksUsed = player.level!!.tick - lastUseTick
                            if (!item.onRelease(player, ticksUsed)) {
                                player.inventory.sendContents(player)
                            }

                            player.removeLastUseTick(releaseItemData.itemInHand.id)
                        } else {
                            player.inventory.sendContents(player)
                        }
                    }

                    InventoryTransactionPacket.RELEASE_ITEM_ACTION_CONSUME -> {
                        InventoryTransactionProcessor.log.debug(
                            "Unexpected release item action consume from {}",
                            player.getEntityName()
                        )
                    }
                }
            } finally {
                player.removeLastUseTick(releaseItemData.itemInHand.id)
            }
        } else if (pk.transactionType == InventoryTransactionPacket.TransactionType.NORMAL) {
            if (pk.actions.size == 2 && pk.actions[0].inventorySource.type == InventorySource.Type.WORLD_INTERACTION &&
                pk.actions[0].inventorySource.flag == InventorySource.Flag.DROP_ITEM &&
                pk.actions[1].inventorySource.type == InventorySource.Type.CONTAINER
                && pk.actions[1].inventorySource.flag == InventorySource.Flag.NONE
            ) { //handle throw hotbar item for player
                dropHotBarItemForPlayer(pk.actions[1].inventorySlot, pk.actions[0].newItem.count, player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.INVENTORY_TRANSACTION_PACKET

    private fun handleUseItemOnEntity(playerHandle: PlayerHandle, pk: InventoryTransactionPacket) {
        val player = playerHandle.player
        val useItemOnEntityData = pk.transactionData as UseItemOnEntityData
        val target = player.level!!.getEntity(useItemOnEntityData.entityRuntimeId) ?: return
        val type = useItemOnEntityData.actionType
        if (!useItemOnEntityData.itemInHand.equalsExact(player.inventory.itemInHand)) {
            player.inventory.sendHeldItem(player)
        }
        var item = player.inventory.itemInHand
        when (type) {
            InventoryTransactionPacket.USE_ITEM_ON_ENTITY_ACTION_INTERACT -> {
                val playerInteractEntityEvent =
                    PlayerInteractEntityEvent(player, target, item, useItemOnEntityData.clickPos)
                if (player.isSpectator) playerInteractEntityEvent.setCancelled()
                Server.instance.pluginManager.callEvent(playerInteractEntityEvent)
                if (playerInteractEntityEvent.isCancelled) {
                    return
                }
                if (target !is EntityArmorStand) {
                    player.level!!.vibrationManager.callVibrationEvent(
                        VibrationEvent(
                            target,
                            target.position.clone(),
                            VibrationType.ENTITY_INTERACT
                        )
                    )
                } else {
                    player.level!!.vibrationManager.callVibrationEvent(
                        VibrationEvent(
                            target,
                            target.position.clone(),
                            VibrationType.EQUIP
                        )
                    )
                }
                if (target.onInteract(
                        player,
                        item,
                        useItemOnEntityData.clickPos
                    ) && (player.isSurvival || player.isAdventure)
                ) {
                    if (item.isTool) {
                        if (item.useOn(target) && item.damage >= item.maxDurability) {
                            player.level!!.addSound(player.position, Sound.RANDOM_BREAK)
                            item = Item.AIR
                        }
                    } else {
                        if (item.count > 1) {
                            item.count--
                        } else {
                            item = Item.AIR
                        }
                    }

                    if (item.isNothing || player.inventory.itemInHand.id == item.id) {
                        player.inventory.setItemInHand(item)
                    } else {
                        logTriedToSetButHadInHand(playerHandle, item, player.inventory.itemInHand)
                    }
                } else {
                    //Otherwise nametag still gets consumed on client side
                    player.inventory.sendContents(player)
                }
            }

            InventoryTransactionPacket.USE_ITEM_ON_ENTITY_ACTION_ATTACK -> {
                if (target is Player && !player.adventureSettings[AdventureSettings.Type.ATTACK_PLAYERS]
                    || target !is Player && !player.adventureSettings[AdventureSettings.Type.ATTACK_MOBS]
                ) return
                if (target.getRuntimeID() == player.getRuntimeID()) {
                    val event = PlayerHackDetectedEvent(player, PlayerHackDetectedEvent.HackType.INVALID_PVP)
                    Server.instance.pluginManager.callEvent(event)

                    if (event.isKick) player.kick(PlayerKickEvent.Reason.INVALID_PVP, "Attempting to attack yourself")

                    InventoryTransactionProcessor.log.warn(player.getEntityName() + " tried to attack oneself")
                    return
                }
                if (!player.canInteract(target.position, (if (player.isCreative) 8 else 5).toDouble())) {
                    return
                } else if (target is Player) {
                    if ((target.gamemode and 0x01) > 0) {
                        return
                    } else if (!Server.instance.settings.levelSettings.default.pvp) {
                        return
                    }
                }
                var itemDamage = item.getAttackDamage(player).toFloat()
                val enchantments = item.enchantments
                if (item.applyEnchantments()) {
                    for (enchantment in enchantments) {
                        itemDamage += enchantment.getDamageBonus(target, player).toFloat()
                    }
                }
                val damage: MutableMap<DamageModifier, Float> = EnumMap(
                    DamageModifier::class.java
                )
                damage[DamageModifier.BASE] = itemDamage
                var knockBack = 0.3f
                if (item.applyEnchantments()) {
                    val knockBackEnchantment = item.getEnchantment(Enchantment.ID_KNOCKBACK)
                    if (knockBackEnchantment != null) {
                        knockBack += knockBackEnchantment.level * 0.1f
                    }
                }
                val entityDamageByEntityEvent = EntityDamageByEntityEvent(
                    player,
                    target,
                    EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                    damage,
                    knockBack,
                    if (item.applyEnchantments()) enchantments else null
                )
                entityDamageByEntityEvent.isBreakShield = item.canBreakShield()
                if (player.isSpectator) entityDamageByEntityEvent.setCancelled()
                if ((target is Player) && !player.level!!.gameRules.getBoolean(GameRule.PVP)) {
                    entityDamageByEntityEvent.setCancelled()
                }

                //保存攻击的目标在lastAttackEntity
                if (!entityDamageByEntityEvent.isCancelled) {
                    playerHandle.setLastAttackEntity(entityDamageByEntityEvent.entity)
                }
                if (target is EntityLiving) {
                    target.preAttack(player)
                }
                try {
                    if (!target.attack(entityDamageByEntityEvent)) {
                        if (item.isTool && player.isSurvival) {
                            player.inventory.sendContents(player)
                        }
                        return
                    }
                } finally {
                    if (target is EntityLiving) {
                        target.postAttack(player)
                    }
                }
                if (item.isTool && (player.isSurvival || player.isAdventure)) {
                    if (item.useOn(target) && item.damage >= item.maxDurability) {
                        player.level!!.addSound(player.position, Sound.RANDOM_BREAK)
                        player.inventory.setItemInHand(Item.AIR)
                    } else {
                        if (item.isNothing || player.inventory.itemInHand.id === item.id) {
                            player.inventory.setItemInHand(item)
                        } else {
                            logTriedToSetButHadInHand(playerHandle, item, player.inventory.itemInHand)
                        }
                    }
                }
            }
        }
    }

    private fun handleUseItem(playerHandle: PlayerHandle, pk: InventoryTransactionPacket) {
        val player = playerHandle.player
        val useItemData = pk.transactionData as UseItemData
        val blockVector = useItemData.blockPos
        val face = useItemData.face

        val type = useItemData.actionType
        when (type) {
            InventoryTransactionPacket.USE_ITEM_ACTION_CLICK_BLOCK -> {
                // Remove if client bug is ever fixed
                val spamBug =
                    (playerHandle.lastRightClickPos != null && System.currentTimeMillis() - playerHandle.lastRightClickTime < 100.0 && blockVector.distanceSquared(
                        playerHandle.lastRightClickPos!!
                    ) < 0.00001)
                playerHandle.lastRightClickPos = blockVector.asVector3()
                playerHandle.lastRightClickTime = System.currentTimeMillis().toDouble()
                if (spamBug) {
                    return
                }
                if (!useItemData.itemInHand.canBeActivated()) player.setDataFlag(EntityFlag.USING_ITEM, false)
                if (player.canInteract(blockVector.add(0.5, 0.5, 0.5), (if (player.isCreative) 13 else 7).toDouble())) {
                    if (player.isCreative) {
                        val i = player.inventory.itemInHand
                        if (player.level!!.useItemOn(
                                blockVector.asVector3(),
                                i,
                                face,
                                useItemData.clickPos.x,
                                useItemData.clickPos.y,
                                useItemData.clickPos.z,
                                player
                            ) != null
                        ) {
                            return
                        }
                    } else if (player.inventory.itemInHand.equals(useItemData.itemInHand, true, false)) {
                        var i: Item? = player.inventory.itemInHand
                        val oldItem: Item = i!!.clone()
                        //TODO: Implement adventure mode checks
                        if ((player.level!!.useItemOn(
                                blockVector.asVector3(),
                                i,
                                face,
                                useItemData.clickPos.x,
                                useItemData.clickPos.y,
                                useItemData.clickPos.z,
                                player
                            ).also { i = it }) != null
                        ) {
                            if (!i!!.equals(oldItem) || i!!.getCount() != oldItem.getCount()) {
                                if (oldItem.id == i!!.id || i!!.isNothing) {
                                    player.inventory.setItemInHand(i!!)
                                } else {
                                    logTriedToSetButHadInHand(playerHandle, i!!, oldItem)
                                }
                                player.inventory.sendHeldItem(player.viewers.values)
                            }
                            return
                        }
                    }
                }
                player.inventory.sendHeldItem(player)
                if (blockVector.distanceSquared(player.position) > 10000) {
                    return
                }
                val target = player.level!!.getBlock(blockVector.asVector3())
                val block = target.getSide(face)
                player.level!!.sendBlocks(
                    arrayOf(player),
                    arrayOf<Block?>(target, block),
                    UpdateBlockPacket.FLAG_NOGRAPHIC
                )
                player.level!!.sendBlocks(
                    arrayOf(player), arrayOf(
                        target.getLevelBlockAtLayer(1), block.getLevelBlockAtLayer(1)
                    ), UpdateBlockPacket.FLAG_NOGRAPHIC, 1
                )
            }

            InventoryTransactionPacket.USE_ITEM_ACTION_BREAK_BLOCK -> {
                //Creative mode use PlayerActionPacket.ACTION_CREATIVE_PLAYER_DESTROY_BLOCK
                if (!player.spawned || !player.isAlive() || player.isCreative) {
                    return
                }
                player.resetInventory()
                var i: Item? = player.inventory.itemInHand
                val oldItem: Item = i!!.clone()
                if (player.isSurvival || player.isAdventure) {
                    if (player.canInteract(blockVector.add(0.5, 0.5, 0.5), 7.0) && (player.level!!.useBreakOn(
                            blockVector.asVector3(),
                            face,
                            i,
                            player,
                            true
                        ).also { i = it }) != null
                    ) {
                        player.foodData.exhaust(0.005)
                        if (!i!!.equals(oldItem) || i!!.getCount() != oldItem.getCount()) {
                            if (oldItem.id == i!!.id || i!!.isNothing) {
                                player.inventory.setItemInHand(i!!)
                            } else {
                                logTriedToSetButHadInHand(playerHandle, i!!, oldItem)
                            }
                            player.inventory.sendHeldItem(player.viewers.values)
                        }
                        return
                    }
                }
                player.inventory.sendContents(player)
                player.inventory.sendHeldItem(player)
                if (blockVector.distanceSquared(player.position) < 10000) {
                    val target = player.level!!.getBlock(blockVector.asVector3())
                    player.level!!.sendBlocks(
                        arrayOf(player), arrayOf(
                            target.position
                        ), UpdateBlockPacket.FLAG_ALL_PRIORITY, 0
                    )

                    val blockEntity = player.level!!.getBlockEntity(blockVector.asVector3())
                    if (blockEntity is BlockEntitySpawnable) {
                        blockEntity.spawnTo(player)
                    }
                }
            }

            InventoryTransactionPacket.USE_ITEM_ACTION_CLICK_AIR -> {
                val item: Item
                val useItemDataItem = useItemData.itemInHand
                val serverItemInHand = player.inventory.itemInHand
                val directionVector = player.getDirectionVector()
                // Removes Damage Tag that the client adds, but we do not store.
                if (useItemDataItem.hasCompoundTag() && (!serverItemInHand.hasCompoundTag() || !serverItemInHand.namedTag!!.containsInt(
                        "Damage"
                    ))
                ) {
                    if (useItemDataItem.namedTag!!.containsInt("Damage")) {
                        useItemDataItem.namedTag!!.remove("Damage")
                    }
                }

                item = if (player.isCreative) {
                    serverItemInHand
                } else if (player.inventory.itemInHand != useItemDataItem) {
                    Server.instance.logger.warning("Item received did not match item in hand.")
                    player.inventory.sendHeldItem(player)
                    return
                } else {
                    serverItemInHand
                }
                val interactEvent =
                    PlayerInteractEvent(player, item, directionVector, face, PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
                Server.instance.pluginManager.callEvent(interactEvent)
                if (interactEvent.isCancelled) {
                    if (interactEvent.item != null && interactEvent.item.isArmor) {
                        player.inventory.sendArmorContents(player)
                    }
                    player.inventory.sendHeldItem(player)
                    return
                }
                if (item.onClickAir(player, directionVector)) {
                    if (!player.isCreative) {
                        if (item.isNothing || player.inventory.itemInHand.id == item.id) {
                            player.inventory.setItemInHand(item)
                        } else {
                            logTriedToSetButHadInHand(playerHandle, item, player.inventory.itemInHand)
                        }
                    }
                    if (!player.isUsingItem(item.id)) {
                        lastUsedItem = item
                        player.setLastUseTick(item.id, player.level!!.tick) //set lastUsed tick
                        return
                    }

                    val ticksUsed = player.level!!.tick - player.getLastUseTick(lastUsedItem!!.id)
                    if (lastUsedItem!!.onUse(player, ticksUsed)) {
                        lastUsedItem!!.afterUse(player)
                        player.removeLastUseTick(item.id)
                        lastUsedItem = null
                    }
                }
            }

            else -> Unit
        }
    }

    private fun logTriedToSetButHadInHand(playerHandle: PlayerHandle, tried: Item, had: Item) {
        InventoryTransactionProcessor.log.debug(
            "Tried to set item {} but {} had item {} in their hand slot",
            tried.id,
            playerHandle.username,
            had.id
        )
    }

    companion object : Loggable {
        private fun dropHotBarItemForPlayer(hotbarSlot: Int, dropCount: Int, player: Player) {
            val inventory = player.inventory
            val item = inventory.getItem(hotbarSlot)
            if (item.isNothing) return

            val ev: PlayerDropItemEvent
            Server.instance.pluginManager.callEvent(PlayerDropItemEvent(player, item).also { ev = it })
            if (ev.isCancelled) {
                player.inventory.sendContents(player)
                return
            }

            val c = item.getCount() - dropCount
            if (c <= 0) {
                inventory.clear(hotbarSlot)
            } else {
                item.setCount(c)
                inventory.setItem(hotbarSlot, item)
            }
            item.setCount(dropCount)
            player.dropItem(item)
        }
    }
}
