package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.NBTTagCompound;

public class EnchantInfo {

  public short i;
  public short l;

  public boolean extract(NBTTagCompound enchant) {

    this.i = enchant.getShort("id");
    this.l = enchant.getShort("lvl");

    return true;
  }
}
