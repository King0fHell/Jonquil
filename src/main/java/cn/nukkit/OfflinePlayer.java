package cn.nukkit;

import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class OfflinePlayer implements IPlayer {
    private final String name;
    private final UUID uuid;
    private final Server server;
    private final CompoundTag namedTag;

    public OfflinePlayer(Server server, UUID uuid) {
        this(server, uuid, null);
    }

    public OfflinePlayer(Server server, String name) {
        this(server, null, name);
    }

    @SuppressWarnings("deprecation")
    public OfflinePlayer(Server server, UUID uuid, String name) {
        this.server = server;


        if (uuid != null) {
            this.namedTag = this.server.getOfflinePlayerData(uuid);
            if (name == null) {
                name = getName();
            }
            this.namedTag.putLong("UUIDMost", uuid.getMostSignificantBits());
            this.namedTag.putLong("UUIDLeast", uuid.getLeastSignificantBits());
        } else if (name != null) {
            this.namedTag = this.server.getOfflinePlayerData(name);
            uuid = getUniqueId();
        } else {
            throw new IllegalArgumentException("Name and UUID cannot both be null");
        }
        this.uuid = uuid;
        this.name = name;

        if (name != null) {
            namedTag.putString("NameTag", name);
        }
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @Override
    public String getName() {
        if (namedTag != null && namedTag.contains("NameTag")) {
            return namedTag.getString("NameTag");
        }
        return null;
    }

    @Override
    public UUID getUniqueId() {
        if (namedTag != null) {
            long least = namedTag.getLong("UUIDLeast");
            long most = namedTag.getLong("UUIDMost");

            if (least != 0 && most != 0) {
                return new UUID(most, least);
            }
        }
        return null;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public boolean isOp() {
        return this.server.isOp(this.getName().toLowerCase());
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) {
            return;
        }

        if (value) {
            this.server.addOp(this.getName().toLowerCase());
        } else {
            this.server.removeOp(this.getName().toLowerCase());
        }
    }

    @Override
    public boolean isBanned() {
        return this.server.getNameBans().isBanned(this.getName());
    }

    @Override
    public void setBanned(boolean value) {
        if (value) {
            this.server.getNameBans().addBan(this.getName(), null, null, null);
        } else {
            this.server.getNameBans().remove(this.getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.isWhitelisted(this.getName().toLowerCase());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.addWhitelist(this.getName().toLowerCase());
        } else {
            this.server.removeWhitelist(this.getName().toLowerCase());
        }
    }

    @Override
    public Player getPlayer() {
        return this.server.getPlayerExact(this.getName());
    }

    @Override
    public Long getFirstPlayed() {
        return this.namedTag != null ? this.namedTag.getLong("firstPlayed") : null;
    }

    @Override
    public Long getLastPlayed() {
        return this.namedTag != null ? this.namedTag.getLong("lastPlayed") : null;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.namedTag != null;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

}
