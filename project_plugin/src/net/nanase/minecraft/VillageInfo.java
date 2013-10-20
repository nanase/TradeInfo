package net.nanase.minecraft;

import net.minecraft.server.v1_6_R3.ChunkCoordinates;
import net.minecraft.server.v1_6_R3.Village;

public class VillageInfo {

  public int i;
  public int[] c;

  public boolean extract(Village village) {

    ChunkCoordinates center = village.getCenter();
    this.i = center.hashCode();
    this.c = new int[]{center.x, center.y, center.z};

    return true;
  }
}
