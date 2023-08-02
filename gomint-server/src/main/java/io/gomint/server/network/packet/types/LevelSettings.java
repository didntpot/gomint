package io.gomint.server.network.packet.types;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.math.BlockPosition;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.Packet;
import io.gomint.server.network.packet.types.gamerule.GameRule;
import java.util.Map;
import javax.annotation.Nullable;

public class LevelSettings {
    private long seed;
    private SpawnSettings spawnSettings;
    private int generator = GeneratorType.OVERWORLD;
    private int worldGameMode;
    private int difficulty;
    private BlockPosition spawnPosition;
    private boolean hasAchievementsDisabled = true;
    private boolean isEditorMode = false;
    private boolean createdInEditorMode = false;
    private boolean exportedFromEditorMode = false;
    private int time = -1;
    private int eduEditionOffer = EducationEditionOffer.NONE;
    private boolean hasEduFeaturesEnabled = false;
    private String eduProductUUID = "";
    private float rainLevel;
    private float lightningLevel;
    private boolean hasConfirmedPlatformLockedContent = false;
    private boolean isMultiplayerGame = true;
    private boolean hasLANBroadcast = true;
    private int xboxLiveBroadcastMode = MultiplayerGameVisibility.PUBLIC;
    private int platformBroadcastMode = MultiplayerGameVisibility.PUBLIC;
    private boolean commandsEnabled;
    private boolean isTexturePacksRequired = true;
    private Map<String, GameRule> gameRules;
    private Experiments experiments;
    private boolean hasBonusChestEnabled = false;
    private boolean hasStartWithMapEnabled = false;
    private int defaultPlayerPermission = PlayerPermission.MEMBER;
    private int serverChunkTickRange = 4; // TODO
    private boolean hasLockedBehaviorPack = false;
    private boolean hasLockedResourcePack = false;
    private boolean isFromLockedWorldTemplate = false;
    private boolean useMsaGamertagsOnly = false;
    private boolean isFromWorldTemplate = false;
    private boolean isWorldTemplateOptionLocked = false;
    private boolean onlySpawnV1Villagers = false;
    private boolean disablePersona = false;
    private boolean disableCustomSkins = false;
    private boolean muteEmoteAnnouncements = false;
    private String vanillaVersion = Protocol.MINECRAFT_PE_NETWORK_VERSION;
    private int limitedWorldWidth = 0;
    private int limitedWorldHeight = 0;
    private boolean isNewNether = false;
    private @Nullable EducationUriResource eduSharedUriResource = null;
    private @Nullable Boolean experimentalGameplayOverride = null;
    private int chatRestrictionLevel = ChatRestrictionLevel.NONE;
    private boolean disablePlayerInteractions = false;

    private void internalRead(PacketBuffer buffer) {
        this.seed = buffer.readLong();
        this.spawnSettings = SpawnSettings.read(buffer);
        this.generator = buffer.readSignedVarInt();
        this.worldGameMode = buffer.readSignedVarInt();
        this.difficulty = buffer.readSignedVarInt();
        this.spawnPosition = Packet.readBlockPosition(buffer);
        this.hasAchievementsDisabled = buffer.readBoolean();
        this.isEditorMode = buffer.readBoolean();
        this.createdInEditorMode = buffer.readBoolean();
        this.exportedFromEditorMode = buffer.readBoolean();
        this.time = buffer.readSignedVarInt();
        this.eduEditionOffer = buffer.readSignedVarInt();
        this.hasEduFeaturesEnabled = buffer.readBoolean();
        this.eduProductUUID = buffer.readString();
        this.rainLevel = buffer.readLFloat();
        this.lightningLevel = buffer.readLFloat();
        this.hasConfirmedPlatformLockedContent = buffer.readBoolean();
        this.isMultiplayerGame = buffer.readBoolean();
        this.hasLANBroadcast = buffer.readBoolean();
        this.xboxLiveBroadcastMode = buffer.readSignedVarInt();
        this.platformBroadcastMode = buffer.readSignedVarInt();
        this.commandsEnabled = buffer.readBoolean();
        this.isTexturePacksRequired = buffer.readBoolean();
        this.gameRules = Packet.getGameRules(buffer);
        this.experiments = Experiments.read(buffer);
        this.hasBonusChestEnabled = buffer.readBoolean();
        this.hasStartWithMapEnabled = buffer.readBoolean();
        this.defaultPlayerPermission = buffer.readSignedVarInt();
        this.serverChunkTickRange = buffer.readLInt();
        this.hasLockedBehaviorPack = buffer.readBoolean();
        this.hasLockedResourcePack = buffer.readBoolean();
        this.isFromLockedWorldTemplate = buffer.readBoolean();
        this.useMsaGamertagsOnly = buffer.readBoolean();
        this.isFromWorldTemplate = buffer.readBoolean();
        this.isWorldTemplateOptionLocked = buffer.readBoolean();
        this.onlySpawnV1Villagers = buffer.readBoolean();
        this.disablePersona = buffer.readBoolean();
        this.disableCustomSkins = buffer.readBoolean();
        this.muteEmoteAnnouncements = buffer.readBoolean();
        this.vanillaVersion = buffer.readString();
        this.limitedWorldWidth = buffer.readLInt();
        this.limitedWorldHeight = buffer.readLInt();
        this.isNewNether = buffer.readBoolean();
        this.eduSharedUriResource = EducationUriResource.read(buffer);
        if (buffer.readBoolean()) {
            this.experimentalGameplayOverride = buffer.readBoolean();
        }
        this.chatRestrictionLevel = buffer.readByte();
        this.disablePlayerInteractions = buffer.readBoolean();
    }

    public void write(PacketBuffer buffer) {
        buffer.writeLLong(this.seed);
        this.spawnSettings.write(buffer);
        buffer.writeSignedVarInt(this.generator);
        buffer.writeSignedVarInt(this.worldGameMode);
        buffer.writeSignedVarInt(this.difficulty);
        Packet.writeBlockPosition(this.spawnPosition, buffer);
        buffer.writeBoolean(this.hasAchievementsDisabled);
        buffer.writeBoolean(this.isEditorMode);
        buffer.writeBoolean(this.createdInEditorMode);
        buffer.writeBoolean(this.exportedFromEditorMode);
        buffer.writeSignedVarInt(this.time);
        buffer.writeSignedVarInt(this.eduEditionOffer);
        buffer.writeBoolean(this.hasEduFeaturesEnabled);
        buffer.writeString(this.eduProductUUID);
        buffer.writeLFloat(this.rainLevel);
        buffer.writeLFloat(this.lightningLevel);
        buffer.writeBoolean(this.hasConfirmedPlatformLockedContent);
        buffer.writeBoolean(this.isMultiplayerGame);
        buffer.writeBoolean(this.hasLANBroadcast);
        buffer.writeSignedVarInt(this.xboxLiveBroadcastMode);
        buffer.writeSignedVarInt(this.platformBroadcastMode);
        buffer.writeBoolean(this.commandsEnabled);
        buffer.writeBoolean(this.isTexturePacksRequired);
        Packet.writeGameRules(buffer, this.gameRules);
        this.experiments.write(buffer);
        buffer.writeBoolean(this.hasBonusChestEnabled);
        buffer.writeBoolean(this.hasStartWithMapEnabled);
        buffer.writeSignedVarInt(this.defaultPlayerPermission);
        buffer.writeLInt(this.serverChunkTickRange);
        buffer.writeBoolean(this.hasLockedBehaviorPack);
        buffer.writeBoolean(this.hasLockedResourcePack);
        buffer.writeBoolean(this.isFromLockedWorldTemplate);
        buffer.writeBoolean(this.useMsaGamertagsOnly);
        buffer.writeBoolean(this.isFromWorldTemplate);
        buffer.writeBoolean(this.isWorldTemplateOptionLocked);
        buffer.writeBoolean(this.onlySpawnV1Villagers);
        buffer.writeBoolean(this.disablePersona);
        buffer.writeBoolean(this.disableCustomSkins);
        buffer.writeBoolean(this.muteEmoteAnnouncements);
        buffer.writeString(this.vanillaVersion);
        buffer.writeLInt(this.limitedWorldWidth);
        buffer.writeLInt(this.limitedWorldHeight);
        buffer.writeBoolean(this.isNewNether);
        if (this.eduSharedUriResource == null) {
            this.eduSharedUriResource = new EducationUriResource("", "");
        }
        this.eduSharedUriResource.write(buffer);
        if (this.experimentalGameplayOverride != null) {
            buffer.writeBoolean(true);
            buffer.writeBoolean(this.experimentalGameplayOverride);
        } else {
            buffer.writeBoolean(false);
        }
        buffer.writeByte((byte) this.chatRestrictionLevel);
        buffer.writeBoolean(this.disablePlayerInteractions);
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setSpawnSettings(SpawnSettings spawnSettings) {
        this.spawnSettings = spawnSettings;
    }

    public void setGenerator(int generator) {
        this.generator = generator;
    }

    public void setWorldGameMode(int worldGameMode) {
        this.worldGameMode = worldGameMode;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setSpawnPosition(BlockPosition spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public void setHasAchievementsDisabled(boolean hasAchievementsDisabled) {
        this.hasAchievementsDisabled = hasAchievementsDisabled;
    }

    public void setEditorMode(boolean editorMode) {
        isEditorMode = editorMode;
    }

    public void setCreatedInEditorMode(boolean createdInEditorMode) {
        this.createdInEditorMode = createdInEditorMode;
    }

    public void setExportedFromEditorMode(boolean exportedFromEditorMode) {
        this.exportedFromEditorMode = exportedFromEditorMode;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setEduEditionOffer(int eduEditionOffer) {
        this.eduEditionOffer = eduEditionOffer;
    }

    public void setHasEduFeaturesEnabled(boolean hasEduFeaturesEnabled) {
        this.hasEduFeaturesEnabled = hasEduFeaturesEnabled;
    }

    public void setEduProductUUID(String eduProductUUID) {
        this.eduProductUUID = eduProductUUID;
    }

    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    public void setLightningLevel(float lightningLevel) {
        this.lightningLevel = lightningLevel;
    }

    public void setHasConfirmedPlatformLockedContent(boolean hasConfirmedPlatformLockedContent) {
        this.hasConfirmedPlatformLockedContent = hasConfirmedPlatformLockedContent;
    }

    public void setMultiplayerGame(boolean multiplayerGame) {
        isMultiplayerGame = multiplayerGame;
    }

    public void setHasLANBroadcast(boolean hasLANBroadcast) {
        this.hasLANBroadcast = hasLANBroadcast;
    }

    public void setXboxLiveBroadcastMode(int xboxLiveBroadcastMode) {
        this.xboxLiveBroadcastMode = xboxLiveBroadcastMode;
    }

    public void setPlatformBroadcastMode(int platformBroadcastMode) {
        this.platformBroadcastMode = platformBroadcastMode;
    }

    public void setCommandsEnabled(boolean commandsEnabled) {
        this.commandsEnabled = commandsEnabled;
    }

    public void setTexturePacksRequired(boolean texturePacksRequired) {
        isTexturePacksRequired = texturePacksRequired;
    }

    public void setGameRules(Map<String, GameRule> gameRules) {
        this.gameRules = gameRules;
    }

    public void setExperiments(Experiments experiments) {
        this.experiments = experiments;
    }

    public void setHasBonusChestEnabled(boolean hasBonusChestEnabled) {
        this.hasBonusChestEnabled = hasBonusChestEnabled;
    }

    public void setHasStartWithMapEnabled(boolean hasStartWithMapEnabled) {
        this.hasStartWithMapEnabled = hasStartWithMapEnabled;
    }

    public void setDefaultPlayerPermission(int defaultPlayerPermission) {
        this.defaultPlayerPermission = defaultPlayerPermission;
    }

    public void setServerChunkTickRange(int serverChunkTickRange) {
        this.serverChunkTickRange = serverChunkTickRange;
    }

    public void setHasLockedBehaviorPack(boolean hasLockedBehaviorPack) {
        this.hasLockedBehaviorPack = hasLockedBehaviorPack;
    }

    public void setHasLockedResourcePack(boolean hasLockedResourcePack) {
        this.hasLockedResourcePack = hasLockedResourcePack;
    }

    public void setFromLockedWorldTemplate(boolean fromLockedWorldTemplate) {
        isFromLockedWorldTemplate = fromLockedWorldTemplate;
    }

    public void setUseMsaGamertagsOnly(boolean useMsaGamertagsOnly) {
        this.useMsaGamertagsOnly = useMsaGamertagsOnly;
    }

    public void setFromWorldTemplate(boolean fromWorldTemplate) {
        isFromWorldTemplate = fromWorldTemplate;
    }

    public void setWorldTemplateOptionLocked(boolean worldTemplateOptionLocked) {
        isWorldTemplateOptionLocked = worldTemplateOptionLocked;
    }

    public void setOnlySpawnV1Villagers(boolean onlySpawnV1Villagers) {
        this.onlySpawnV1Villagers = onlySpawnV1Villagers;
    }

    public void setDisablePersona(boolean disablePersona) {
        this.disablePersona = disablePersona;
    }

    public void setDisableCustomSkins(boolean disableCustomSkins) {
        this.disableCustomSkins = disableCustomSkins;
    }

    public void setMuteEmoteAnnouncements(boolean muteEmoteAnnouncements) {
        this.muteEmoteAnnouncements = muteEmoteAnnouncements;
    }

    public void setVanillaVersion(String vanillaVersion) {
        this.vanillaVersion = vanillaVersion;
    }

    public void setLimitedWorldWidth(int limitedWorldWidth) {
        this.limitedWorldWidth = limitedWorldWidth;
    }

    public void setLimitedWorldHeight(int limitedWorldHeight) {
        this.limitedWorldHeight = limitedWorldHeight;
    }

    public void setNewNether(boolean newNether) {
        isNewNether = newNether;
    }

    public void setEduSharedUriResource(@Nullable EducationUriResource eduSharedUriResource) {
        this.eduSharedUriResource = eduSharedUriResource;
    }

    public void setExperimentalGameplayOverride(@Nullable Boolean experimentalGameplayOverride) {
        this.experimentalGameplayOverride = experimentalGameplayOverride;
    }

    public void setChatRestrictionLevel(int chatRestrictionLevel) {
        this.chatRestrictionLevel = chatRestrictionLevel;
    }

    public void setDisablePlayerInteractions(boolean disablePlayerInteractions) {
        this.disablePlayerInteractions = disablePlayerInteractions;
    }
}
