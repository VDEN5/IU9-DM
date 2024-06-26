#include <iostream>
#include <vector>
#include <queue>

using namespace std;

struct Edge {
    int to;
    char out;
};

struct State {
    bool isFinal;
    std::vector<int> adj;
};

struct BfsState {
    int q;
    int len;
    int q_dfa;
};

int main() {
    int n, m, q_mealy;
    cin >> n >> m >> q_mealy;
    std::vector<std::vector<Edge>> mealy(n, std::vector<Edge>(m));
    for (auto& row : mealy) {
        for (auto& [to, _] : row) {
            cin >> to;
        }
    }
    for (auto& row : mealy) {
        for (auto& [_, out] : row) {
            cin >> out;
        }
    }

    int q_dfa;
    cin >> n >> m >> q_dfa;
    std::vector<State> dfa(n);
    for (auto& [isFinal, adj] : dfa) {
        adj.resize(m);
        char tmp;
        cin >> tmp;
        if (tmp == '+') {
            isFinal = true;
        } else if (tmp == '-') {
            isFinal = false;
        }
        for (auto& to : adj) {
            cin >> to;
        }
    }

    if (dfa[q_dfa].isFinal) {
        cout << 0 << endl;
        return 0;
    }

    queue<BfsState> q;
    q.push({q_mealy, 0, q_dfa});
    for (int i = 0; i < 100000; i++) {
        auto [i_mealy, len, i_dfa] = q.front();
        q.pop();
        auto& state_mealy = mealy[i_mealy];
        auto& state_dfa = dfa[i_dfa];

        if (state_dfa.isFinal) {
            cout << len << endl;
            return 0;
        }

        for (int i = 0; i < state_mealy.size(); i++) {
            auto [next, next_out] = state_mealy[i];

            auto next_dfa = state_dfa.adj[next_out-'a'];
            q.push({next, len+1, next_dfa});
        }
    }

    cout << "none" << endl;
    return 0;
}
