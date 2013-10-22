package net.nanase.minecraft;

import java.util.Random;
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

  public long getId() {
      int a = new Random(this.c[0]).nextInt(), b = new Random(this.c[1]).nextInt(), d = new Random(this.c[2]).nextInt();
      return (long)a << (d % 32) ^ (long)b << (a % 32) ^ (long)d << (b % 32);
  }
}
