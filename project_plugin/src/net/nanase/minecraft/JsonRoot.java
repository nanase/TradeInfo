package net.nanase.minecraft;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_6_R3.EntityVillager;
import net.minecraft.server.v1_6_R3.Village;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class JsonRoot {

  public long t;
  public int[] s;
  public VillageInfo[] v;
  public VillagerInfo[] r;

  public boolean extract(World world) {
    Location spawn = world.getSpawnLocation();

    this.t = System.currentTimeMillis();
    this.s = new int[]{spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()};

    List<VillageInfo> info_villages = new ArrayList<>();
    List<VillagerInfo> info_villagers = new ArrayList<>();
    List<Village> world_villages = getVillages(world);
    List<EntityVillager> world_villagers = getVillagers(world);

    for (Village village : world_villages) {
      VillageInfo villageInfo = new VillageInfo();

      if (!villageInfo.extract(village)) {
        return false;
      }

      info_villages.add(villageInfo);
    }

    for (EntityVillager villager : world_villagers) {
      VillagerInfo villagerInfo = new VillagerInfo();

      if (!villagerInfo.extract(villager, world_villages)) {
        return false;
      }

      info_villagers.add(villagerInfo);
    }

    this.v = info_villages.toArray(new VillageInfo[info_villages.size()]);
    this.r = info_villagers.toArray(new VillagerInfo[info_villagers.size()]);

    return true;
  }

  public static List<Village> getVillages(World world) {
    return (((CraftWorld) world).getHandle()).villages.getVillages();
  }

  public static List<EntityVillager> getVillagers(World world) {
    List<EntityVillager> result = new ArrayList<>();

    for (LivingEntity le : world.getLivingEntities()) {
      if (le.getType() != EntityType.VILLAGER) {
        continue;
      }

      result.add(((CraftVillager) le).getHandle());
    }
    return result;
  }
}
