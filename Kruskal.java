import java.util.*;
import java.util.Arrays;
import static java.lang.Math.pow;
class edge implements Comparable<edge> {
    public int x1, x2, y1, y2, x, y;
    public double dist;
    public edge(int x1, int y1, int x2, int y2, int x, int y) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x = x;
        this.y = y;
        this.dist = pow((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2), 0.5);
    }
    public int compareTo(edge obj) {
        if (this.dist > obj.dist)
            return 1;
        if (this.dist == obj.dist)
            return 0;
        return -1;
    }
}
public class Kruskal {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            b[i] = in.nextInt();
        }
        edge[] arr = new edge[n * (n - 1) / 2];
        int m = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                edge rty = new edge(a[i], b[i], a[j], b[j], i, j);
                arr[m] = rty;
                m++;
            }
        }
        Arrays.sort(arr);
        int[] tree_id = new int[n];
        double cost = 0;
        for (int i = 0; i < n; i++) {
            tree_id[i] = i;
        }
        for (int i = 0; i < m; i++) {
            int a1 = arr[i].x;
            int b1 = arr[i].y;
            double l = arr[i].dist;
            if (tree_id[a1] != tree_id[b1]) {
                cost += l;
                int old_id = tree_id[b1];
                int new_id = tree_id[a1];
                for (int j = 0; j < n; j++) {
                    if (tree_id[j] == old_id) {
                        tree_id[j] = new_id;
                    }
                }
            }
        }
        System.out.printf("%.2f", cost);
    }
}