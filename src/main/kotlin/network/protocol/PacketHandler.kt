package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.experimental.network.MigrationPacket

interface PacketHandler {
    fun handle(pk: MigrationPacket<*>) {}

    fun handle(pk: AnimatePacket) {}

    fun handle(pk: EntityEventPacket) {}

    fun handle(pk: InteractPacket) {}

    fun handle(pk: InventoryTransactionPacket) {}

    fun handle(pk: ItemStackRequestPacket) {}

    fun handle(pk: ItemStackResponsePacket) {}

    fun handle(pk: LevelChunkPacket) {}

    fun handle(pk: LevelEventGenericPacket) {}

    fun handle(pk: LevelEventPacket) {}

    fun handle(pk: LevelSoundEventPacket) {}

    fun handle(pk: LoginPacket) {}

    fun handle(pk: MobArmorEquipmentPacket) {}

    fun handle(pk: MobEffectPacket) {}

    fun handle(pk: MobEquipmentPacket) {}

    fun handle(pk: ModalFormResponsePacket) {}

    fun handle(pk: MoveEntityAbsolutePacket) {}

    fun handle(pk: MoveEntityDeltaPacket) {}

    fun handle(pk: MovePlayerPacket) {}

    fun handle(pk: NPCDialoguePacket) {}

    fun handle(pk: NPCRequestPacket) {}

    fun handle(pk: NetworkChunkPublisherUpdatePacket) {}

    fun handle(pk: NetworkSettingsPacket) {}

    fun handle(pk: PlayerActionPacket) {}

    fun handle(pk: PlayerAuthInputPacket) {}

    fun handle(pk: PlayerEnchantOptionsPacket) {}

    fun handle(pk: PlayerFogPacket) {}

    fun handle(pk: PlayerHotbarPacket) {}

    fun handle(pk: PlayerListPacket) {}

    fun handle(pk: PlayerSkinPacket) {}

    fun handle(pk: PlayerStartItemCoolDownPacket) {}

    fun handle(pk: RemoveObjectivePacket) {}

    fun handle(pk: RequestAbilityPacket) {}

    fun handle(pk: RequestNetworkSettingsPacket) {}

    fun handle(pk: RequestPermissionsPacket) {}

    fun handle(pk: RespawnPacket) {}

    fun handle(pk: ServerSettingsRequestPacket) {}

    fun handle(pk: ServerSettingsResponsePacket) {}

    fun handle(pk: SetDefaultGameTypePacket) {}

    fun handle(pk: SetDifficultyPacket) {}

    fun handle(pk: SetDisplayObjectivePacket) {}

    fun handle(pk: SetLocalPlayerAsInitializedPacket) {}

    fun handle(pk: SetPlayerGameTypePacket) {}

    fun handle(pk: SetScorePacket) {}

    fun handle(pk: SetScoreboardIdentityPacket) {}

    fun handle(pk: SetTitlePacket) {}

    fun handle(pk: ShowProfilePacket) {}

    fun handle(pk: SpawnParticleEffectPacket) {}

    fun handle(pk: StartGamePacket) {}

    fun handle(pk: StructureBlockUpdatePacket) {}

    fun handle(pk: SyncEntityPropertyPacket) {}

    fun handle(pk: TakeItemEntityPacket) {}

    fun handle(pk: TextPacket) {}

    fun handle(pk: UpdateAbilitiesPacket) {}

    fun handle(pk: UpdateAttributesPacket) {}

    fun handle(pk: UpdateBlockPacket) {}

    fun handle(pk: UpdateBlockSyncedPacket) {}

    fun handle(pk: UpdateSoftEnumPacket) {}

    fun handle(pk: UpdateSubChunkBlocksPacket) {}
}
