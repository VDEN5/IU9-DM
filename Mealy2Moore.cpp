#include <iostream>
#include <vector>
#include <map>
#include <algorithm>

struct MealyMachine {
    int q0;
    std::vector<int> Q;
    std::vector<std::string> X;
    std::vector<std::string> Y;
    std::vector<std::vector<int>> s;
    std::vector<std::vector<std::string>> r;

    std::vector<std::vector<std::string>> ArrayOfFrequency() {
        std::map<std::pair<int, std::string>, bool> MyMap;
        std::vector<std::vector<std::string>> newQ(Q.size(), std::vector<std::string>());

        for (size_t i = 0; i < Q.size(); i++) {
            for (size_t j = 0; j < X.size(); j++) {
                if (!MyMap[{s[i][j], r[i][j]}]) {
                    newQ[s[i][j]].push_back(r[i][j]);
                    MyMap[{s[i][j], r[i][j]}] = true;
                }
            }
        }

        for (size_t i = 0; i < Q.size(); i++) {
            std::sort(newQ[i].begin(), newQ[i].end());
        }

        return newQ;
    }

    void PrintMur() {
        auto AOF = ArrayOfFrequency();
        std::map<std::string, int> renamer;

        auto createMap = [&]() {
            int counter = 0;
            for (size_t i = 0; i < AOF.size(); i++) {
                for (const auto& str : AOF[i]) {
                    renamer[std::to_string(i) + "," + str] = counter;
                    counter++;
                }
            }
            return renamer;
        };

        renamer = createMap();

        std::cout << "digraph {" << std::endl;
        std::cout << "\trankdir = LR" << std::endl;

        for (size_t i = 0; i < AOF.size(); i++) {
            for (const auto& str : AOF[i]) {
                int num = std::stoi(str);
                int nameOfNode = renamer[std::to_string(i) + "," + str];
                std::cout << "\t" << nameOfNode <<
                 " [label = \"(" << i << "," << Y[atoi(str.c_str())] << ")\"]" << std::endl;
            }
        }

        for (size_t i = 0; i < AOF.size(); i++) {
            for (const auto& str : AOF[i]) {
                for (size_t j = 0; j < s[i].size(); j++) {
                    int nameOfNode1 = renamer[std::to_string(i) + "," + str];
                    int nameOfNode2 = renamer[std::to_string(s[i][j]) + "," + r[i][j]];
                    std::cout << "\t" << nameOfNode1 <<
                     " -> " << nameOfNode2 << " [label = \"" << X[j] << "\"]" << std::endl;
                }
            }
        }

        std::cout << "}" << std::endl;
    }
};

int main() {
    int kX, kY, n;
    std::cin >> kX;
    std::vector<std::string> X(kX);
    for (int i = 0; i < kX; i++) {
        std::cin >> X[i];
    }

    std::cin >> kY;
    std::vector<std::string> Y(kY);
    for (int i = 0; i < kY; i++) {
        std::cin >> Y[i];
    }

    std::cin >> n;
    std::vector<int> Q(n);
    for (int i = 0; i < n; i++) {
        Q[i] = i;
    }

    std::vector<std::vector<int>> s(n, std::vector<int>(kX));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < kX; j++) {
            std::cin >> s[i][j];
        }
    }

    std::vector<std::vector<std::string>> r(n, std::vector<std::string>(kX));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < kX; j++) {
            std::cin >> r[i][j];
        }
    }

    MealyMachine mealy {0, Q, X, Y, s, r};
    mealy.PrintMur();

    return 0;
}
