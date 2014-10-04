package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.EntityVillager;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class VillagerInfo {
    public boolean a;
    public int p;
    public RecipeInfo[] r;

    public boolean extract(EntityVillager villager) {
        this.a = (villager.getAge() >= 0);
        this.p = villager.getProfession();

        NBTTagCompound villagerTag = TradeInfo.getTag(villager);

        if (villagerTag.hasKey("Offers")) {
            NBTTagCompound offerTag = villagerTag.getCompound("Offers");

            if (!offerTag.hasKey("Recipes")) {
                return true;
            }

            NBTTagList recipesTag = offerTag.getList("Recipes", 10);
            int size = recipesTag.size();

            List<RecipeInfo> recipes = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                NBTTagCompound recipeTag = recipesTag.get(i);
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

    public static boolean checkInhabitant(VillageInfo village, EntityVillager villager) {
        return Math.sqrt(pow2(village.c[0] - villager.locX)
                + pow2(village.c[1] - villager.locY)
                + pow2(village.c[2] - villager.locZ)) < (double) village.s;
    }

    private static double pow2(double x) {
        return x * x;
    }
}
