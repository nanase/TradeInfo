package net.nanase.minecraft;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_7_R3.Item;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

public class ItemInfo {

		public byte c;
		public short i;
		public String n;
		public EnchantInfo[] e;

		public boolean extract(NBTTagCompound item) {
				this.c = item.getByte("Count");
				this.i = item.getShort("id");

                this.n = Item.d(this.i).getName().replace("item.", "").replace("tile.", "");

				if (item.hasKey("tag")) {
						NBTTagCompound tag = item.getCompound("tag");
						if (tag.hasKey("ench")) {

								NBTTagList enchsTag = tag.getList("ench", 10);
								int size = enchsTag.size();
								List<EnchantInfo> enchants = new ArrayList<>(size);

								for (int j = 0; j < size; j++) {
										NBTTagCompound ench = (NBTTagCompound) enchsTag.get(j);
										EnchantInfo enchant = new EnchantInfo();

										if (!enchant.extract(ench)) {
												return false;
										}

										enchants.add(enchant);
								}

								this.e = enchants.toArray(new EnchantInfo[size]);
						}else if (tag.hasKey("StoredEnchantments")) {

                            NBTTagList enchsTag = tag.getList("StoredEnchantments", 10);
                            int size = enchsTag.size();
                            List<EnchantInfo> enchants = new ArrayList<>(size);

                            for (int j = 0; j < size; j++) {
                                NBTTagCompound ench = (NBTTagCompound) enchsTag.get(j);
                                EnchantInfo enchant = new EnchantInfo();

                                if (!enchant.extract(ench)) {
                                    return false;
                                }

                                enchants.add(enchant);
                            }

                            this.e = enchants.toArray(new EnchantInfo[size]);
                        }
				}

				return true;
		}
}
