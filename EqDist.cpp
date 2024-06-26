#include <iostream>
#include <vector>
#include <queue>

struct Vertex {
    int id;
    std::vector<int> edges;
    std::vector<int> distanceToRoot;
};

struct Queue {
    std::vector<Vertex> data;
    int size, head, tail;
};

Queue initQueue(int n) {
    return Queue{std::vector<Vertex>(n), 0, 0, 0};
}

bool queueIsEmpty(Queue& queue) {
    return queue.size == 0;
}

void enqueue(Queue& queue, Vertex x) {
    queue.data[queue.tail] = x;
    queue.tail++;
    queue.size++;
}

Vertex dequeue(Queue& queue) {
    Vertex vert = queue.data[queue.head];
    queue.head++;
    queue.size--;
    return vert;
}

void bfs(std::vector<Vertex>& graph, int rootInd, int rootRelate) {
    Queue queue = initQueue(graph.size());
    std::vector<bool> visited(graph.size(), false);
    enqueue(queue, graph[rootInd]);

    while (!queueIsEmpty(queue)) {
        Vertex v = dequeue(queue);
        visited[v.id] = true;

        for (int edge : v.edges) {
            if (!visited[edge]) {
                visited[edge] = true;
                graph[edge].distanceToRoot[rootRelate] = graph[v.id].distanceToRoot[rootRelate] + 1;
                enqueue(queue, graph[edge]);
            }
        }
    }
}

bool eqDistFilter(std::vector<Vertex>& graph, int k) {
    if (k == 1) {
        for (int i = 0; i < graph.size(); i++) {
            std::cout << i << " ";
        }
        return true;
    }

    int f = 0;
    for (Vertex& vert : graph) {
        int c = 0;
        int x = vert.distanceToRoot[0];
        for (int i = 1; i < vert.distanceToRoot.size(); i++) {
            if (x != vert.distanceToRoot[i] || vert.distanceToRoot[i] == 0) {
                break;
            }
            c++;
        }

        if (c == vert.distanceToRoot.size() - 1) {
            f = 1;
            std::cout << vert.id << " ";
        }
    }

    if (f == 0) {
        return false;
    }

    return true;
}

int main() {
    int N, M;
    std::cin >> N >> M;
    std::vector<Vertex> graph(N);

    for (int i = 0; i < N; i++) {
        graph[i].id = i;
    }

    for (int i = 0; i < M; i++) {
        int u, v;
        std::cin >> u >> v;
        graph[u].edges.push_back(v);
        graph[v].edges.push_back(u);
    }

    int K;
    std::cin >> K;

    for (int i = 0; i < N; i++) {
        graph[i].distanceToRoot.resize(K);
    }

    for (int i = 0; i < K; i++) {
        int rootVert;
        std::cin >> rootVert;
        bfs(graph, rootVert, i);
    }

    if (!eqDistFilter(graph, K)) {
        std::cout << "-";
    }

    return 0;
}
