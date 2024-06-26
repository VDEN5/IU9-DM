#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <string>
#include <functional>

struct Vertex {
    std::string Name;
    int i;
    std::vector<std::string> Adj;
    int Ts;
    int DsuTs;
    bool Rebuild;
};

int main() {
    int n;
    std::cin >> n;
    std::vector<Vertex> arr(n);
    std::unordered_map<std::string, Vertex*> mapping;
    int main_index = -1;
    for (int i = 0; i < n; i++) {
        Vertex& v = arr[i];
        v.i = i;
        std::cin >> v.Name;
        if (v.Name == "main") {
            main_index = i;
        }
        mapping[v.Name] = &v;
        int k;
        std::cin >> k;
        v.Adj.resize(k);
        v.DsuTs = -1;
        for (int j = 0; j < k; j++) {
            std::cin >> v.Adj[j];
            if (v.Adj[j] == v.Name) {
                std::cout << "!CYCLE" << std::endl;
                exit(0);
                return 0;
            }
        }
    }
    int m;
    std::cin >> m;
    for (int i = 0; i < m; i++) {
        std::string s;
        int ts;
        std::cin >> s >> ts;
        std::string base = s.substr(0, s.length() - 4);
        std::string extension = s.substr(s.length() - 4);
        Vertex* v = mapping[base];
        if (extension == ".pas") {
            v->Ts = ts;
        } else if (extension == ".dcu") {
            v->DsuTs = ts;
        }
    }

    std::vector<int> used(n, 0);

    std::function<bool(int)> dfs = [&](int v) {
        used[v] = 1;
        if (arr[v].DsuTs == -1) {
            arr[v].Rebuild = true;
        }
        if (arr[v].DsuTs < arr[v].Ts) {
            arr[v].Rebuild = true;
        }
        for (const auto& el : arr[v].Adj) {
            int i = mapping[el]->i;
            Vertex& next = arr[i];
            if (next.DsuTs > arr[v].DsuTs) {
                arr[v].Rebuild = true;
            }
            if (used[i] == 0) {
                arr[v].Rebuild = dfs(i) || arr[v].Rebuild;
            }
            if (used[i] == 1) {
                std::cout << "!CYCLE" << std::endl;
                exit(0);
                return false;
            }
        }
        used[v] = 2;
        return arr[v].Rebuild;
    };

    dfs(main_index);
    arr[main_index].Rebuild = true;

    std::vector<std::string> todo;
    for (const auto& el : arr) {
        if (el.Rebuild) {
            todo.push_back(el.Name + ".pas");
        }
    }
    std::sort(todo.begin(), todo.end());

    for (const auto& el : todo) {
        std::cout << el << std::endl;
    }

    return 0;
}
