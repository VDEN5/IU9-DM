import java.util.*;

class Stack {
    private List<TopOfTheGraph> buf = new ArrayList<>();
    private int top = 0;

    public void push(TopOfTheGraph x) {
        buf.add(x);
        top++;
    }

    public TopOfTheGraph pop() {
        int index = buf.size() - 1;
        TopOfTheGraph topElement = buf.get(index);
        buf.remove(index);
        top--;
        return topElement;
    }

    public int getTop() {
        return top;
    }
}

class TopOfTheGraph {
    boolean visited;
    String command;
    int index;
    int move;
    TopOfTheGraph dom;
    TopOfTheGraph sdom;
    TopOfTheGraph label;
    TopOfTheGraph parent;
    TopOfTheGraph ancestor;
    List<TopOfTheGraph> bucket = new ArrayList<>();
    List<TopOfTheGraph> out = new ArrayList<>();
    List<TopOfTheGraph> in = new ArrayList<>();
}

public class Loops {
    private static Stack stack = new Stack();
    private static int topOfTheGraphNel = 0;
    private static Map<Integer, Integer> topOfTheGraphMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        List<TopOfTheGraph> graph = setGraph(N);
        actionScan(graph, scanner);
        actionRecognition(graph, N);
        topOfTheGraphNel++;
        dfs(graph.get(0));
        graph = in(graph);

        graph.sort((a, b) -> b.index - a.index);

        N = graph.size();
        graph = dominators(graph, N);
        int result = 0;

        for (TopOfTheGraph a : graph) {
            for (TopOfTheGraph b : a.in) {
                while (b != null && b != a) {
                    b = b.dom;
                }
                if (a == b) {
                    result++;
                    break;
                }
            }
        }
        System.out.println(result);
    }

    private static List<TopOfTheGraph> in(List<TopOfTheGraph> graph) {
        for (int i = 0; i < graph.size(); i++) {
            if (!graph.get(i).visited) {
                int l = graph.size() - 1;
                graph.set(i, graph.get(l));
                graph.remove(l);
                i--;
            } else {
                for (int j = 0; j < graph.get(i).in.size(); j++) {
                    if (!graph.get(i).in.get(j).visited) {
                        int li = graph.get(i).in.size() - 1;
                        graph.get(i).in.set(j, graph.get(i).in.get(li));
                        graph.get(i).in.remove(li);
                        j--;
                    }
                }
            }
        }
        return graph;
    }

    private static void actionRecognition(List<TopOfTheGraph> graph, int N) {
        for (int index = 0; index < graph.size(); index++) {
            switch (graph.get(index).command) {
                case "ACTION":
                    if (index != N - 1) {
                        graph.get(index).out.add(graph.get(index + 1));
                        graph.get(index + 1).in.add(graph.get(index));
                    }
                    break;
                case "JUMP":
                    int t = topOfTheGraphMap.get(graph.get(index).move);
                    graph.get(index).out.add(graph.get(t));
                    graph.get(t).in.add(graph.get(index));
                    break;
                case "BRANCH":
                    t = topOfTheGraphMap.get(graph.get(index).move);
                    graph.get(index).out.add(graph.get(t));
                    graph.get(t).in.add(graph.get(index));
                    if (index != N - 1) {
                        graph.get(index).out.add(graph.get(index + 1));
                        graph.get(index + 1).in.add(graph.get(index));
                    }
                    break;
                default:
                    System.out.println("??????");
            }
        }
    }

    private static void actionScan(List<TopOfTheGraph> graph, Scanner scanner) {
        for (int i = 0; i < graph.size(); i++) {
            int v = scanner.nextInt();
            String currentAct = scanner.next();
            graph.get(i).command = currentAct;
            if (!currentAct.equals("ACTION")) {
                int w = scanner.nextInt();
                graph.get(i).move = w;
            }
            topOfTheGraphMap.put(v, i);
        }
    }

    private static List<TopOfTheGraph> setGraph(int N) {
        List<TopOfTheGraph> result = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            TopOfTheGraph t = new TopOfTheGraph();
            t.sdom = t;
            t.label = t;
            result.add(t);
        }
        return result;
    }

    private static TopOfTheGraph findMin(TopOfTheGraph v) {
        TopOfTheGraph min;
        if (v.ancestor == null) {
            min = v;
        } else {
            TopOfTheGraph u = v;
            while (u.ancestor.ancestor != null) {
                stack.push(u);
                u = u.ancestor;
            }
            while (stack.getTop() != 0) {
                v = stack.pop();
                if (v.ancestor.label.sdom.index < v.label.sdom.index) {
                    v.label = v.ancestor.label;
                }
                v.ancestor = u.ancestor;
            }
            min = v.label;
        }
        return min;
    }

    private static List<TopOfTheGraph> dominators(List<TopOfTheGraph> graph, int N) {
        stack = new Stack();

        for (TopOfTheGraph w : graph) {
            if (w.index != 1) {
                for (TopOfTheGraph v : w.in) {
                    TopOfTheGraph u = findMin(v);
                    if (w.sdom.index > u.sdom.index) {
                        w.sdom = u.sdom;
                    }
                }
                w.ancestor = w.parent;
                w.sdom.bucket.add(w);
                for (TopOfTheGraph v : w.parent.bucket) {
                    TopOfTheGraph u = findMin(v);
                    if (u.sdom != v.sdom) {
                        v.dom = u;
                    } else {
                        v.dom = v.sdom;
                    }
                }
                w.parent.bucket.clear();
            }
        }

        for (TopOfTheGraph w : graph) {
            if (w.index != 1 && w.dom != w.sdom) {
                w.dom = w.dom.dom;
            }
        }
        graph.get(graph.size() - 1).dom = null;
        return graph;
    }

    private static void dfs(TopOfTheGraph r) {
        r.visited = true;
        topOfTheGraphNel++;
        r.index = topOfTheGraphNel - 1;
        for (TopOfTheGraph e : r.out) {
            if (!e.visited) {
                e.parent = r;
                dfs(e);
            }
        }
    }
}