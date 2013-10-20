package net.nanase.minecraft;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_6_R3.ChunkCoordinates;
import net.minecraft.server.v1_6_R3.EntityVillager;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import net.minecraft.server.v1_6_R3.Village;

public class VillagerInfo {

  public int v;
  public boolean a;
  public int p;
  public RecipeInfo[] r;

  public boolean extract(EntityVillager villager, List<Village> villages) {
    this.v = this.detectVillageFor(villages, villager);
    this.a = (villager.getAge() >= 0);
    this.p = villager.getProfession();

    NBTTagCompound villagerTag = TradeInfo.getTag(villager);

    if (villagerTag.hasKey("Offers")) {
      NBTTagCompound offerTag = villagerTag.getCompound("Offers");

      if (!offerTag.hasKey("Recipes")) {
        return true;
      }

      NBTTagList recipesTag = offerTag.getList("Recipes");
      int size = recipesTag.size();
      List<RecipeInfo> recipes = new ArrayList<>(size);

      for (int i = 0; i < size; i++) {
        NBTTagCompound recipeTag = (NBTTagCompound) recipesTag.get(i);
        RecipeInfo recipe = new RecipeInfo();

        if (!recipe.extract(recipeTag)) {
          return false;
        }

        recipes.add(recipe);
      }

      this.r = recipes.toArray(new RecipeInfo[size]);
    }

    return true;
  }

  private int detectVillageFor(List<Village> villages, EntityVillager villager) {
    int i = 0;
    for (Village village : villages) {
      ChunkCoordinates coord = village.getCenter();

      if (Math.sqrt(pow2(coord.x - villager.locX)
              + pow2(coord.y - villager.locY)
              + pow2(coord.z - villager.locZ)) < (double) village.getSize()) {
        return i;
      }
      i++;
    }
    return -1;
  }

  private static double pow2(double x) {
    return x * x;
  }
}
