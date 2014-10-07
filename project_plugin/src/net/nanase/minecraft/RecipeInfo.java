package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.NBTTagCompound;

/**
 * 取引の内容を格納します。
 */
public class RecipeInfo {
    /**
     * 取引の最大可能上限数。
     */
    public int m;

    /**
     * 取引の回数。
     */
    public int u;

    /**
     * 1つ目の買取アイテム。
     */
    public ItemInfo a;

    /**
     * 2つ目の買取アイテム。
     */
    public ItemInfo b;

    /**
     * 売出アイテム。
     */
    public ItemInfo s;

    /**
     * オブジェクトから情報を抽出します。
     *
     * @param recipe 抽出される NBTTagCompound オブジェクト。
     * @return 抽出に成功した場合は true、 失敗した場合はfalse。
     */
    public boolean extract(NBTTagCompound recipe) {
        this.m = recipe.getInt("maxUses");
        this.u = recipe.getInt("uses");

        this.a = new ItemInfo();

        if (!this.a.extract(recipe.getCompound("buy")))
            return false;

        if (recipe.hasKey("buyB")) {
            this.b = new ItemInfo();

            if (!this.b.extract(recipe.getCompound("buyB")))
                return false;
        }

        this.s = new ItemInfo();

        return this.s.extract(recipe.getCompound("sell"));
    }
}
