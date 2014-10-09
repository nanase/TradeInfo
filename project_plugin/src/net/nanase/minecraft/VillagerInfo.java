package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.EntityVillager;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * 村人の情報を格納します。
 */
public class VillagerInfo {
    /**
     * 村人が大人であるかの真偽値。
     */
    public boolean a;

    /**
     * 村人の職業を表す数値。
     */
    public int p;

    /**
     * 村人の名前を表す文字列。
     */
    public String n;

    /**
     * 取引内容の配列。
     */
    public RecipeInfo[] r;

    /**
     * オブジェクトから情報を抽出します。
     *
     * @param villager 抽出される EntityVillager オブジェクト。
     * @return 抽出に成功した場合は true、 失敗した場合はfalse。
     */
    public boolean extract(EntityVillager villager) {
        this.a = (villager.getAge() >= 0);
        this.p = villager.getProfession();
        this.n = villager.getCustomName();

        NBTTagCompound villagerTag = TradeInfo.getTag(villager);

        if (villagerTag.hasKey("Offers")) {
            NBTTagCompound offerTag = villagerTag.getCompound("Offers");

            if (!offerTag.hasKey("Recipes"))
                return true;

            NBTTagList recipesTag = offerTag.getList("Recipes", 10);
            int size = recipesTag.size();

            List<RecipeInfo> recipes = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                NBTTagCompound recipeTag = recipesTag.get(i);
                RecipeInfo recipe = new RecipeInfo();

                if (!recipe.extract(recipeTag))
                    return false;

                recipes.add(recipe);
            }

            this.r = recipes.toArray(new RecipeInfo[size]);
        }

        return true;
    }

    /**
     * 村人が村の有効範囲内に存在しているかを検査します。
     *
     * @param village 対象となる村を表す VillageInfo オブジェクト。
     * @param villager 対象となる村人を表す EntityVillager オブジェクト。
     * @return 村人が有効範囲内にいる場合は true、それ以外の時 false。
     */
    public static boolean checkInhabitant(VillageInfo village, EntityVillager villager) {
        return Math.sqrt(Utils.pow2(village.c[0] - villager.locX)
                + Utils.pow2(village.c[1] - villager.locY)
                + Utils.pow2(village.c[2] - villager.locZ)) < (double) village.s;
    }
}
