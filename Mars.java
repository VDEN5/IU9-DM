import java.util.*;

public class Mars {
    static List<List<Integer>> g;
    static List<Integer> color;

    static Pair<Integer, Integer> dfs(int v, int c) {
        color.set(v, c);
        Pair<Integer, Integer> sz = new Pair<>(1, 0);
        for (int to : g.get(v)) {
            if (color.get(to) != -1) {
                if (c == color.get(to)) {
                    return new Pair<>(-1, -1);
                }
            } else {
                Pair<Integer, Integer> add = dfs(to, c ^ 1);
                if (add.getKey() == -1) {
                    return add;
                }
                sz = new Pair<>(sz.getKey() + add.getValue(), sz.getValue() + add.getKey());
            }
        }
        return sz;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        g = new ArrayList<>(n);
        color = new ArrayList<>(Collections.nCopies(n, -1));
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = scanner.next().charAt(0);
                if (c == '+') {
                    g.get(i).add(j);
                    g.get(j).add(i);
                }
            }
        }
        int cnt = 0;
        List<Pair<Integer, Integer>> comp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (color.get(i) == -1) {
                comp.add(dfs(i, cnt));
                if (comp.get(comp.size() - 1).getValue() == -1) {
                    System.out.println("No solution");
                    return;
                }
                cnt += 2;
            }
        }
        int s = comp.size();
        int bestDif = n + 1;
        List<Integer> best = new ArrayList<>();
        for (int mask = 0; mask < (1 << s); mask++) {
            int fi = 0, se = 0;
            for (int i = 0; i < s; i++) {
                if ((mask & (1 << i)) != 0) {
                    fi += comp.get(i).getValue();
                    se += comp.get(i).getKey();
                } else {
                    fi += comp.get(i).getKey();
                    se += comp.get(i).getValue();
                }
            }
            if (fi > se) continue;
            int dif = se - fi;
            if (dif <= bestDif) {
                List<Integer> fiComp = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    int ind = color.get(i) >> 1;
                    if (((mask & (1 << ind)) >> ind) == (color.get(i) & 1)) {
                        fiComp.add(i);
                    }
                }
                if (dif < bestDif) {
                    bestDif = dif;
                    best = fiComp;
                } else {
                    int ind = 0;
                    while (ind < best.size() && ind < fiComp.size()) {
                        if (!best.get(ind).equals(fiComp.get(ind))) break;
                        ind++;
                    }
                    if (ind == best.size()) continue;
                    if (ind == fiComp.size() || best.get(ind) > fiComp.get(ind)) {
                        best = fiComp;
                    }
                }
            }
        }
        for (int v : best) {
            System.out.print((v + 1) + " ");
        }
        System.out.println();
    }

    static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}