package net.nanase.minecraft;

import net.minecraft.server.v1_7_R3.ChunkCoordinates;
import net.minecraft.server.v1_7_R3.EntityVillager;
import net.minecraft.server.v1_7_R3.Village;

import java.util.List;
import java.util.Random;

/**
 * 村の情報を格納します。
 */
public class VillageInfo {
    /**
     * 村を識別するIDを表す数値。
     */
    public long i;

    /**
     * 村のサイズ（半径）を表す数値。
     */
    public int s;

    /**
     * 村のドアの数。
     */
    public int d;

    /**
     * 村が認識する村人の数。
     */
    public int p;

    /**
     * 村の中心座標。
     */
    public int[] c;

    /**
     * 村に所属する村人。
     */
    public VillagerInfo[] r;

    /**
     * オブジェクトから情報を抽出します。
     *
     * @param village   抽出される Village オブジェクト。
     * @param villagers 抽出される EntityVillager オブジェクトのリスト。
     * @return 抽出に成功した場合は true、 失敗した場合はfalse。
     */
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

    /**
     * この村を識別するための ID を生成します。
     *
     * @return 村のID。
     */
    private long createId() {
        int a = new Random(this.c[0]).nextInt(), b = new Random(this.c[1]).nextInt(), d = new Random(this.c[2]).nextInt();
        return (long) a << (d % 32) ^ (long) b << (a % 32) ^ (long) d << (b % 32);
    }

    /**
     * 指定された 2 つの村が同じ村であるかの真偽値を返します。
     *
     * @param a 1 つ目の村。
     * @param b 2 つ目の村。
     * @return 2 つの村が同じであるとき true、それ以外のとき false。
     */
    public static boolean isSameVillage(VillageInfo a, VillageInfo b) {
        return (Math.sqrt(Utils.pow2(a.c[0] - b.c[0])
                + Utils.pow2(a.c[1] - b.c[1])
                + Utils.pow2(a.c[2] - b.c[2])) < 64.0);
    }
}
