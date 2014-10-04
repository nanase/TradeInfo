package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.NBTTagCompound;

public class RecipeInfo {
    public int m;
    public int u;
    public ItemInfo a;
    public ItemInfo b;
    public ItemInfo s;

    public boolean extract(NBTTagCompound recipe) {
        this.m = recipe.getInt("maxUses");
        this.u = recipe.getInt("uses");

        this.a = new ItemInfo();
        if (!this.a.extract(recipe.getCompound("buy"))) {
            return false;
        }

        if (recipe.hasKey("buyB")) {
            this.b = new ItemInfo();
            if (!this.b.extract(recipe.getCompound("buyB"))) {
                return false;
            }
        }

        this.s = new ItemInfo();

        return this.s.extract(recipe.getCompound("sell"));
    }
}
