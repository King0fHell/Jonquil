package cn.nukkit.resourcepacks;

import cn.nukkit.Server;
import com.google.common.io.Files;

import java.io.File;
import java.util.*;

public class ResourcePackManager {
    private final Map<UUID, ResourcePack> resourcePacksById = new HashMap<>();
    private ResourcePack[] resourcePacks;

    public ResourcePackManager(File path) {
        if (!path.exists()) {
            path.mkdirs();
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException("Resource packs path \"" + path.getName() + "\" exists and is not a directory");
        }

        List<ResourcePack> loadedResourcePacks = new ArrayList<>();
        for (File pack : path.listFiles()) {
            try {
                ResourcePack resourcePack = null;

                if (!pack.isDirectory()) { //directory resource packs temporarily unsupported
                    switch (Files.getFileExtension(pack.getName())) {
                        case "zip":
                        case "mcpack":
                            resourcePack = new ZippedResourcePack(pack);
                            break;
                        default:
                            Server.getInstance().getLogger().warning("Could not load \"" + pack.getName() + "\" due to format not recognized");
                            break;
                    }
                }

                if (resourcePack != null) {
                    loadedResourcePacks.add(resourcePack);
                    this.resourcePacksById.put(resourcePack.getPackId(), resourcePack);
                }
            } catch (IllegalArgumentException e) {
                Server.getInstance().getLogger().warning("Could not load \"" + pack.getName() + "\": " + e.getMessage());
            }
        }

        this.resourcePacks = loadedResourcePacks.toArray(new ResourcePack[0]);
    }

    public ResourcePack[] getResourceStack() {
        return this.resourcePacks;
    }

    public ResourcePack getPackById(UUID id) {
        return this.resourcePacksById.get(id);
    }
}
