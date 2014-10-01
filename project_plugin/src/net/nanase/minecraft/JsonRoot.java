package net.nanase.minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import net.arnx.jsonic.TypeReference;
import net.minecraft.server.v1_7_R3.EntityVillager;
import net.minecraft.server.v1_7_R3.Village;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class JsonRoot {

  public long t;
  public int[] s;
  public VillageInfo[] v;

  public boolean extract(World world) {
    Location spawn = world.getSpawnLocation();

    this.t = System.currentTimeMillis();
    this.s = new int[]{spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()};

    List<VillageInfo> info_villages = JsonRoot.loadVillageList();

    List<Village> world_villages = getVillages(world);
    List<EntityVillager> world_villagers = getVillagers(world);

    for (Village village : world_villages) {
      VillageInfo villageInfo = new VillageInfo();

      if (!villageInfo.extract(village, world_villagers)) {
        return false;
      }

      JsonRoot.integrateVillageJson(info_villages, villageInfo);
    }

    this.v = info_villages.toArray(new VillageInfo[info_villages.size()]);

    JsonRoot.saveVillageList(info_villages);

    return true;
  }

  private static List<VillageInfo> loadVillageList() {
    final String path = TradeInfo.PluginDir + "/villages.json";
    if (!new File(path).exists()) {
      return new ArrayList<>();
    }

    try {
      List<VillageInfo> villages = new ArrayList<>();
      try (FileReader reader = new FileReader(path)) {
        villages.addAll((Collection<? extends VillageInfo>) JSON.decode(reader,new TypeReference<List<VillageInfo>>() {} ));
      }
      return villages;
    } catch (FileNotFoundException ex) {
      return new ArrayList<>();
    } catch (IOException | JSONException ex) {
      Bukkit.getLogger().log(Level.SEVERE, "Jsonファイルの読み込みに失敗しました.", ex);
      return new ArrayList<>();
    }
  }

  private static void saveVillageList(List<VillageInfo> villages) {
    final String path = TradeInfo.PluginDir + "/villages.json";

    try {
      try (FileWriter writer = new FileWriter(path)) {
        JSON.encode(villages, writer);
      }
    } catch (IOException | JSONException ex) {
      Bukkit.getLogger().log(Level.SEVERE, "Jsonファイルの書き込みに失敗しました.", ex);
    }
  }

  private static void integrateVillageJson(List<VillageInfo> villages, VillageInfo village) {
    VillageInfo delTarget = null;
    for (VillageInfo v : villages) {
      if (VillageInfo.isSameVillage(v, village)) {
        delTarget = v;
        break;
      }
    }

    if (delTarget == null) {
      villages.add(village);
    } else {
      villages.remove(delTarget);
      villages.add(village);
    }
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
