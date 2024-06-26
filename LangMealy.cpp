#include <iostream>
#include <vector>
#include <map>
#include <algorithm>

int M;
std::map<std::string, std::vector<int>> hashTable;
std::vector<std::vector<std::string>> O;
std::vector<std::vector<int>> T;

void f(int q0, std::string res, int n) {
    if (O[q0][n] != "-") {
        res = res + O[q0][n];
    }
    if (res.length() > M) {
        return;
    }
    int q = T[q0][n];
    bool t = false;
    auto A = hashTable[res];
    for (int v : A) {
        if (v == q) {
            t = true;
        }
    }
    if (!t) {
        hashTable[res].push_back(q);
        f(q, res, 0);
        f(q, res, 1);
    }
}

int main() {
    int q, q0;
    std::cin >> q;

    T.resize(q, std::vector<int>(2));
    O.resize(q, std::vector<std::string>(2));

    for (int i = 0; i < q; i++) {
        for (int j = 0; j < 2; j++) {
            std::cin >> T[i][j];
        }
    }

    for (int i = 0; i < q; i++) {
        for (int j = 0; j < 2; j++) {
            std::cin >> O[i][j];
        }
    }

    std::cin >> q0 >> M;

    f(q0, "", 0);
    f(q0, "", 1);

    std::vector<std::string> keys;
    for (const auto& pair : hashTable) {
        if (!pair.first.empty()) {
            keys.push_back(pair.first);
        }
    }

    std::sort(keys.begin(), keys.end());

    for (const std::string& k : keys) {
        std::cout << k << " ";
    }
    std::cout << std::endl;

    return 0;
}
