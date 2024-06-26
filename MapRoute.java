import java.util.Arrays;
import java.util.PriorityQueue;
public class MapRoute {
    static int[][] cost, d;
    static int n;
    static final int inf = Integer.MAX_VALUE;
    static class Pair {
        int x, y;
        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static class Item implements Comparable<Item> {
        Pair value;
        int priority, index;
        Item(Pair value, int priority) {
            this.value = value;
            this.priority = priority;
        }
        public int compareTo(Item other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
    static void dij(int x, int y) {
        for (int i = 0; i < n; i++) {
            Arrays.fill(d[i], -1);
        }
        PriorityQueue<Item> pq = new PriorityQueue<>();
        pq.add(new Item(new Pair(x, y), cost[x][y]));

        while (!pq.isEmpty()) {
            Item minItem = pq.poll();
            x = minItem.value.x;
            y = minItem.value.y;
            d[x][y] = minItem.priority;
            int[] dx = {1, 0, -1, 0};
            int[] dy = {0, 1, 0, -1};
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];
                if (nx >= 0 && nx < n && ny >= 0 && ny < n
                        && (d[nx][ny] == -1 || d[nx][ny] > d[x][y] + cost[nx][ny])) {
                    d[nx][ny] = d[x][y] + cost[nx][ny];
                    pq.add(new Item(new Pair(nx, ny), d[nx][ny]));
                }
            }
        }
    }
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        n = scanner.nextInt();
        cost = new int[n][n];
        d = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = scanner.nextInt();
            }
        }
        dij(0, 0);
        System.out.println(d[n - 1][n - 1]);
    }
}