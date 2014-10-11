package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.ChunkCoordinates;
import net.minecraft.server.v1_7_R3.EntityVillager;
import net.minecraft.server.v1_7_R3.Village;

import java.util.List;
import java.util.Random;

public class VillageInfo {
    public long i;
    public int s;
    public int d;
    public int p;
    public int[] c;
    public VillagerInfo[] r;

    public boolean extract(Village village, List<EntityVillager> villagers) {
        ChunkCoordinates center = village.getCenter();
        int size = villagers.size();
        this.r = new VillagerInfo[size];

        this.c = new int[]{center.x, center.y, center.z};
        this.i = this.createId();
        this.s = village.getSize();
        this.d = village.getDoorCount();
        this.p = village.getPopulationCount();

        for (int i = 0; i < size; i++) {
            this.r[i] = new VillagerInfo();

            if (!VillagerInfo.checkInhabitant(this, villagers.get(i)))
                continue;

            if (!this.r[i].extract(villagers.get(i)))
                return false;
        }

        return true;
    }

    private long createId() {
        int a = new Random(this.c[0]).nextInt(), b = new Random(this.c[1]).nextInt(), d = new Random(this.c[2]).nextInt();
        return (long) a << (d % 32) ^ (long) b << (a % 32) ^ (long) d << (b % 32);
    }

    public static boolean isSameVillage(VillageInfo a, VillageInfo b) {
        return (Math.sqrt(Utils.pow2(a.c[0] - b.c[0])
                + Utils.pow2(a.c[1] - b.c[1])
                + Utils.pow2(a.c[2] - b.c[2])) < 64.0);
    }
}
