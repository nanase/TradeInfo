package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.Item;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * アイテムの情報を格納します。
 */
public class ItemInfo {
    /**
     * アイテムの数量。
     */
    public byte c;

    /**
     * アイテムのID。
     */
    public short i;

    /**
     * アイテムの名前。
     */
    public String n;

    /**
     * アイテムに適用されたエンチャントのリスト。
     */
    public EnchantInfo[] e;

    /**
     * オブジェクトから情報を抽出します。
     *
     * @param item 抽出される NBTTagCompound オブジェクト。
     * @return 抽出に成功した場合は true、 失敗した場合はfalse。
     */
    public boolean extract(NBTTagCompound item) {
        this.c = item.getByte("Count");
        this.i = item.getShort("id");

        this.n = Item.d(this.i).getName().replace("item.", "").replace("tile.", "");

        if (item.hasKey("tag")) {
            NBTTagCompound tag = item.getCompound("tag");
            String targetKey;

            if (tag.hasKey("ench"))
                targetKey = "ench";
            else if (tag.hasKey("StoredEnchantments"))
                targetKey = "StoredEnchantments";
            else
                return false;

            NBTTagList enchantmentsTag = tag.getList(targetKey, 10);
            int size = enchantmentsTag.size();
            List<EnchantInfo> enchants = new ArrayList<>(size);

            for (int j = 0; j < size; j++) {
                NBTTagCompound enchantment = enchantmentsTag.get(j);
                EnchantInfo enchant = new EnchantInfo();

                if (!enchant.extract(enchantment))
                    return false;

                enchants.add(enchant);
            }

            this.e = enchants.toArray(new EnchantInfo[size]);
        }

        return true;
    }
}
