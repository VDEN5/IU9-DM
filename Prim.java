import java.util.*;
class pair {
    private int num;
    private int len;
    public pair(int a, int b) {
        this.num = a;
        this.len = b;
    }
    public int getnum() {
        return this.num;
    }
    public int getlen() {
        return this.len;
    }
}
public class Prim {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), m = in.nextInt(), u, v, len, ans = 0;
        int gc[] = new int[n];
        for (int i = 0; i < n; i++) gc[i] = 0;
        pair g[][] = new pair[n][m];
        for (int i = 0; i < m; i++) {
            u = in.nextInt();
            v = in.nextInt();
            len = in.nextInt();
            g[u][gc[u]] = new pair(v, len);
            gc[u]++;
            g[v][gc[v]] = new pair(u, len);
            gc[v]++;
        }
        boolean used[] = new boolean[n];
        used[0] = true;
        for (int i = 0; i < n - 1; i++) {
            int mun = 1000000, vmun = 0;
            for (int j = 0; j < n; j++) {
                if (used[j]) {
                    for (int k = 0; k < gc[j]; k++) {
                        if (used[g[j][k].getnum()] == false) {
                            if (g[j][k].getlen() < mun) {
                                mun = g[j][k].getlen();
                                vmun = g[j][k].getnum();
                            }
                        }
                    }
                }
            }
            used[vmun] = true;
            ans += mun;
        }
        System.out.println(ans);
    }
}