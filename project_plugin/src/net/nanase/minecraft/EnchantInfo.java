package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.NBTTagCompound;

/**
 * エンチャント情報を格納します。
 */
public class EnchantInfo {
    /**
     * エンチャントID。
     */
    public short i;

    /**
     * エンチャントレベル。
     */
    public short l;

    /**
     * オブジェクトから情報を抽出します。
     *
     * @param enchant 抽出される NBTTagCompound オブジェクト。
     * @return 抽出に成功した場合は true、 失敗した場合はfalse。
     */
    public boolean extract(NBTTagCompound enchant) {
        this.i = enchant.getShort("id");
        this.l = enchant.getShort("lvl");

        return true;
    }
}
