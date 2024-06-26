import java.util.ArrayList;
import java.util.Scanner;

class Vertex {
    ArrayList<Integer> edges;
    int timeIn, time;
    boolean visited;

    Vertex() {
        edges = new ArrayList<>();
        timeIn = 0;
        time = 0;
        visited = false;
    }
}

public class BridgeNum {
    public static int min(int a, int b) {
        return a > b ? b : a;
    }

    public static void dfs(int v, int p, int timer, int[] bridgeCounter, Vertex[] graph) {
        graph[v].visited = true;
        graph[v].time = timer;
        graph[v].timeIn = timer;
        timer++;
        for (int i = 0; i < graph[v].edges.size(); i++) {
            int to = graph[v].edges.get(i);
            if (to != p) {
                if (graph[to].visited) {
                    graph[v].time = min(graph[v].time, graph[to].timeIn);
                } else {
                    dfs(to, v, timer, bridgeCounter, graph);
                    graph[v].time = min(graph[v].time, graph[to].time);
                    if (graph[to].time > graph[v].timeIn) {
                        bridgeCounter[0]++;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        Vertex[] graph = new Vertex[N];
        for (int i = 0; i < N; i++) {
            graph[i] = new Vertex();
        }
        for (int i = 0; i < M; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph[u].edges.add(v);
            graph[v].edges.add(u);
        }
        int[] bridgeCounter = {0};
        for (int i = 0; i < graph.length; i++) {
            if (!graph[i].visited) {
                dfs(i, -1, 0, bridgeCounter, graph);
            }
        }
        System.out.println(bridgeCounter[0]);
    }
}