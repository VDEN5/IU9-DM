#include <iostream>
#include <queue>
#include <vector>

struct Vertex {
  int num;
  int color;
  Vertex* next;
  bool used;
};

int main() {
  int n, m;
  std::cin >> n >> m;

  std::vector<int> length(n, 300000);
  std::vector<Vertex> list(n);

  for (int i = 0; i < n; ++i) {
    length[i] = 300000;
  }

  for (int i = 0; i < m; ++i) {
    int x, y, c;
    std::cin >> x >> y >> c;
    x--;
    y--;

    if (list[x].next == nullptr) {
      list[x].next = new Vertex{y, c, nullptr, false};
    } else {
      Vertex* tek = list[x].next;
      while (tek->next != nullptr) {
        tek = tek->next;
      }
      tek->next = new Vertex{y, c, nullptr, false};
    }

    if (list[y].next == nullptr) {
      list[y].next = new Vertex{x, c, nullptr, false};
    } else {
      Vertex* tek = list[y].next;
      while (tek->next != nullptr) {
        tek = tek->next;
      }
      tek->next = new Vertex{x, c, nullptr, false};
    }
  }

  int start = n - 1;
  int finish = 0;
  length[start] = 0;

  std::queue<int> q1;
  std::queue<int> q2;
  std::queue<int> q3;
  q1.push(start);

  while (!q1.empty()) {
    int tek = q1.front();
    q1.pop();
    for (Vertex* k = list[tek].next; k != nullptr; k = k->next) {
      if (length[k->num] > length[tek] + 1) {
        length[k->num] = length[tek] + 1;
        q1.push(k->num);
      }
    }
  }

  std::cout << length[finish] << std::endl;

  q1.push(finish);
  q2.push(finish);

  for (int i = 0; i < length[finish]; ++i) {
    int min = 300000;
    while (!q1.empty()) {
      int tek = q1.front();
      q1.pop();
      for (Vertex* k = list[tek].next; k != nullptr; k = k->next) {
        if (length[k->num] == length[tek] - 1 && k->color < min) {
          min = k->color;
        }
      }
    }

    while (!q2.empty()) {
      int tek = q2.front();
      q2.pop();
      for (Vertex* k = list[tek].next; k != nullptr; k = k->next) {
        if (length[k->num] == length[tek] - 1 && k->color == min) {
          q3.push(k->num);
        }
      }
    }

    std::cout << min << " ";

    while (!q3.empty()) {
      int tek = q3.front();
      q3.pop();
      q1.push(tek);
      q2.push(tek);
    }
  }

  return 0;
}
