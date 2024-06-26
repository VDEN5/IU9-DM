import java.util.*;

class DSU {
    int[] parent, depth;

    public DSU(int n) {
        parent = new int[n];
        depth = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if (parent[x] == x) {
            return x;
        } else {
            parent[x] = find(parent[x]);
            return parent[x];
        }
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (depth[rootX] < depth[rootY]) {
            parent[rootX] = rootY;
        } else {
            parent[rootY] = rootX;
            if (depth[rootX] == depth[rootY] && rootX != rootY) {
                depth[rootX]++;
            }
        }
    }
}

public class EqMealy {
    int states, alphabetSize, startState;
    int[][] transMatrix;
    String[][] inputMatrix;

    public EqMealy(int states, int alphabetSize, int startState) {
        this.states = states;
        this.alphabetSize = alphabetSize;
        this.startState = startState;
        transMatrix = new int[states][alphabetSize];
        inputMatrix = new String[states][alphabetSize];
    }

    public int[] split1() {
        int[] roots = new int[states];
        int count = states;
        DSU dsu = new DSU(count);
        for (int i = 0; i < states; i++) {
            for (int j = 0; j < states; j++) {
                if (dsu.find(i) != dsu.find(j)) {
                    boolean eq = true;
                    for (int k = 0; k < alphabetSize; k++) {
                        if (!inputMatrix[i][k].equals(inputMatrix[j][k])) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        dsu.union(i, j);
                        count--;
                    }
                }
            }
        }
        for (int i = 0; i < states; i++) {
            roots[i] = dsu.find(i);
        }
        return roots;
    }

    public int[] split(int[] roots) {
        int m = states;
        DSU dsu = new DSU(m);
        for (int i = 0; i < states; i++) {
            for (int j = 0; j < states; j++) {
                if (roots[i] == roots[j] && dsu.find(i) != dsu.find(j)) {
                    boolean eq = true;
                    for (int k = 0; k < alphabetSize; k++) {
                        int w1 = transMatrix[i][k];
                        int w2 = transMatrix[j][k];
                        if (roots[w1] != roots[w2]) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        dsu.union(i, j);
                        m--;
                    }
                }
            }
        }
        for (int i = 0; i < states; i++) {
            roots[i] = dsu.find(i);
        }
        return roots;
    }

    public EqMealy aufenkampHohn() {
        int[] roots = split1();
        int m1 = states;
        int m2;
        while (true) {
            roots = split(roots);
            m2 = states;
            if (m1 == m2) {
                break;
            }
            m1 = m2;
        }
        int[] a = new int[states];
        int[] b = new int[states];
        int counter = 0;
        for (int i = 0; i < states; i++) {
            if (roots[i] == i) {
                a[counter] = i;
                b[i] = counter;
                counter++;
            }
        }
        EqMealy minimized = new EqMealy(m1, alphabetSize, b[roots[startState]]);
        for (int i = 0; i < minimized.states; i++) {
            for (int j = 0; j < alphabetSize; j++) {
                minimized.transMatrix[i][j] = b[roots[transMatrix[a[i]][j]]];
                minimized.inputMatrix[i][j] = inputMatrix[a[i]][j];
            }
        }
        return minimized;
    }

    public int[] canonizedNumbering() {
        boolean[] visited = new boolean[states];
        int[] canonizeNumbering = new int[states];
        Arrays.fill(canonizeNumbering, -1);
        int count = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(startState);
        while (!stack.isEmpty()) {
            int ind = stack.pop();
            if (visited[ind]) continue;
            canonizeNumbering[ind] = count++;
            visited[ind] = true;
            for (int i = 0; i < alphabetSize; i++) {
                if (!visited[transMatrix[ind][i]]) {
                    stack.push(transMatrix[ind][i]);
                }
            }
        }
        return canonizeNumbering;
    }

    public EqMealy getCanonized() {
        int[] canonNumbers = canonizedNumbering();
        int newStateCount = (int) Arrays.stream(canonNumbers).filter(x -> x != -1).count();
        EqMealy canonized = new EqMealy(newStateCount, alphabetSize, 0);
        for (int i = 0; i < states; i++) {
            if (canonNumbers[i] == -1) continue;
            for (int j = 0; j < alphabetSize; j++) {
                canonized.transMatrix[canonNumbers[i]][j] = canonNumbers[transMatrix[i][j]];
                canonized.inputMatrix[canonNumbers[i]][j] = inputMatrix[i][j];
            }
        }
        return canonized;
    }

    public static boolean isEq(EqMealy mealy1, EqMealy mealy2) {
        if (mealy1.states != mealy2.states ||
                mealy1.alphabetSize != mealy2.alphabetSize || mealy1.startState != mealy2.startState) {
            return false;
        }
        for (int i = 0; i < mealy1.states; i++) {
            for (int j = 0; j < mealy1.alphabetSize; j++) {
                if (mealy1.transMatrix[i][j] != mealy2.transMatrix[i][j] ||
                        !mealy1.inputMatrix[i][j].equals(mealy2.inputMatrix[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int statesCount = scanner.nextInt();
        int alphabetSize = scanner.nextInt();
        int startState = scanner.nextInt();
        EqMealy mealy1 = new EqMealy(statesCount, alphabetSize, startState);
        for (int i = 0; i < statesCount; i++) {
            for (int j = 0; j < alphabetSize; j++) {
                mealy1.transMatrix[i][j] = scanner.nextInt();
            }
        }
        for (int i = 0; i < statesCount; i++) {
            for (int j = 0; j < alphabetSize; j++) {
                mealy1.inputMatrix[i][j] = scanner.next();
            }
        }
        statesCount = scanner.nextInt();
        alphabetSize = scanner.nextInt();
        startState = scanner.nextInt();
        EqMealy mealy2 = new EqMealy(statesCount, alphabetSize, startState);
        for (int i = 0; i < statesCount; i++) {
            for (int j = 0; j < alphabetSize; j++) {
                mealy2.transMatrix[i][j] = scanner.nextInt();
            }
        }
        for (int i = 0; i < statesCount; i++) {
            for (int j = 0; j < alphabetSize; j++) {
                mealy2.inputMatrix[i][j] = scanner.next();
            }
        }
        EqMealy minimizedMealy1 = mealy1.aufenkampHohn().getCanonized();
        EqMealy minimizedMealy2 = mealy2.aufenkampHohn().getCanonized();
        if (isEq(minimizedMealy1, minimizedMealy2)) {
            System.out.println("EQUAL");
        } else {
            System.out.println("NOT EQUAL");
        }
    }
}