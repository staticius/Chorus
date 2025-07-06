package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.experimental.network.MigrationPacket

interface PacketHandler {
    fun handle(pk: MigrationPacket<*>) {}

    fun handle(pk: AnimatePacket) {}

    fun handle(pk: ServerboundDiagnosticsPacket) {}

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

    fun handle(pk: OnScreenTextureAnimationPacket) {}

    fun handle(pk: OpenSignPacket) {}

    fun handle(pk: PacketViolationWarningPacket) {}

    fun handle(pk: PlayerActionPacket) {}

    fun handle(pk: PlayerArmorDamagePacket) {}

    fun handle(pk: PlayerAuthInputPacket) {}

    fun handle(pk: PlayerEnchantOptionsPacket) {}

    fun handle(pk: PlayerFogPacket) {}

    fun handle(pk: PlayerHotbarPacket) {}

    fun handle(pk: PlayerListPacket) {}

    fun handle(pk: PlayerLocationPacket) {}

    fun handle(pk: PlayerSkinPacket) {}

    fun handle(pk: PlayerStartItemCoolDownPacket) {}

    fun handle(pk: PositionTrackingDBClientRequestPacket) {}

    fun handle(pk: PositionTrackingDBServerBroadcastPacket) {}

    fun handle(pk: RemoveObjectivePacket) {}

    fun handle(pk: RemoveVolumeEntityPacket) {}

    fun handle(pk: RequestAbilityPacket) {}

    fun handle(pk: RequestNetworkSettingsPacket) {}

    fun handle(pk: RequestPermissionsPacket) {}

    fun handle(pk: RespawnPacket) {}

    fun handle(pk: ScriptMessagePacket) {}

    fun handle(pk: ServerSettingsRequestPacket) {}

    fun handle(pk: ServerSettingsResponsePacket) {}

    fun handle(pk: SetDefaultGameTypePacket) {}

    fun handle(pk: SetDifficultyPacket) {}

    fun handle(pk: SetDisplayObjectivePacket) {}

    fun handle(pk: SetHudPacket) {}

    fun handle(pk: SetLocalPlayerAsInitializedPacket) {}

    fun handle(pk: SetPlayerGameTypePacket) {}

    fun handle(pk: SetPlayerInventoryOptionsPacket) {}

    fun handle(pk: SetScorePacket) {}

    fun handle(pk: SetScoreboardIdentityPacket) {}

    fun handle(pk: SetTitlePacket) {}

    fun handle(pk: ShowProfilePacket) {}

    fun handle(pk: SimulationTypePacket) {}

    fun handle(pk: SpawnParticleEffectPacket) {}

    fun handle(pk: StartGamePacket) {}

    fun handle(pk: StructureBlockUpdatePacket) {}

    fun handle(pk: SyncEntityPropertyPacket) {}

    fun handle(pk: TakeItemEntityPacket) {}

    fun handle(pk: TextPacket) {}

    fun handle(pk: TickingAreasLoadStatusPacket) {}

    fun handle(pk: ToastRequestPacket) {}

    fun handle(pk: ToggleCrafterSlotRequestPacket) {}

    fun handle(pk: TrimDataPacket) {}

    fun handle(pk: UnlockedRecipesPacket) {}

    fun handle(pk: UpdateAbilitiesPacket) {}

    fun handle(pk: UpdateAdventureSettingsPacket) {}

    fun handle(pk: UpdateAttributesPacket) {}

    fun handle(pk: UpdateBlockPacket) {}

    fun handle(pk: UpdateBlockSyncedPacket) {}

    fun handle(pk: UpdateClientInputLocksPacket) {}

    fun handle(pk: UpdatePlayerGameTypePacket) {}

    fun handle(pk: UpdateSoftEnumPacket) {}

    fun handle(pk: UpdateSubChunkBlocksPacket) {}

    fun handle(pk: SettingsCommandPacket) {}

    fun handle(pk: ServerboundLoadingScreenPacket) {}
}
