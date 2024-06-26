#include <iostream>
#include <vector>
#include <algorithm>

struct Vertex {
    int id;
    std::vector<int> edges;
    bool visited;
    bool isRed;
};

struct Component {
    std::vector<int> vertexes;
    int edgesCount;
};

struct Edges {
    int indA, indB;
};

void dfs(Vertex* vertex, std::vector<Vertex>& graph, std::vector<Component>& comps, int& countOfComps) {
    vertex->visited = true;
    comps[countOfComps - 1].vertexes.push_back(vertex->id);
    comps[countOfComps - 1].edgesCount += vertex->edges.size();

    for (int i = 0; i < vertex->edges.size(); i++) {
        int to = vertex->edges[i];
        if (!graph[to].visited) {
            dfs(&graph[to], graph, comps, countOfComps);
        }
    }
}

bool compComponents(const Component& a, const Component& b) {
    if (a.vertexes.size() != b.vertexes.size()) {
        return a.vertexes.size() < b.vertexes.size();
    }
    if (a.edgesCount != b.edgesCount) {
        return a.edgesCount < b.edgesCount;
    }
    return a.vertexes[0] > b.vertexes[0];
}

int main() {
    int N, M;
    std::cin >> N >> M;

    std::vector<Vertex> graph(N);
    std::vector<Edges> edges(M);

    for (int i = 0; i < N; i++) {
        graph[i].id = i;
    }

    for (int i = 0; i < M; i++) {
        int a, b;
        std::cin >> a >> b;
        edges[i] = {a, b};
        graph[a].edges.push_back(b);
        graph[b].edges.push_back(a);
    }

    std::vector<Component> comps;
    int countOfComps = 0;

    for (int i = 0; i < N; i++) {
        if (!graph[i].visited) {
            countOfComps++;
            comps.push_back(Component());
            dfs(&graph[i], graph, comps, countOfComps);
        }
    }

    Component maxcomp = comps[0];
    for (int i = 1; i < comps.size(); i++) {
        if (compComponents(maxcomp, comps[i])) {
            maxcomp = comps[i];
        }
    }

    for (const auto& vertexInd : maxcomp.vertexes) {
        graph[vertexInd].isRed = true;
    }

    std::cout << "graph {" << std::endl;

    for (const auto& vertex : graph) {
        std::cout << "\t";
        if (vertex.isRed) {
            std::cout << vertex.id << " [color = red]" << std::endl;
        } else {
            std::cout << vertex.id << std::endl;
        }
    }

    for (int i = 0; i < edges.size(); i++) {
        std::cout << "\t";
        if (graph[edges[i].indA].isRed) {
            std::cout << edges[i].indA << " -- " << edges[i].indB << " [color = red]" << std::endl;
        } else {
            std::cout << edges[i].indA << " -- " << edges[i].indB << std::endl;
        }
    }

    std::cout << "}" << std::endl;

    return 0;
}
