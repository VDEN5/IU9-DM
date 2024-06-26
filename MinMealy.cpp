#include <iostream>
#include <vector>
#include <string>

struct Status
{
	int i;
	Status * parent;
	int depth;
	Status * pi;
	bool was;
	std::vector<Status*> delt;
};

Status* find(Status *x)
{
	Status * root;
	if (x->parent == x)
	{
		root = x;
	}
	else
	{
		x->parent = find(x->parent);
		root = x->parent;
	}

	return root;
}

void unionFunc(Status *u, Status *v)
{
	Status *rootU = find(u);
	Status *rootV = find(v);
	if (rootU->depth < rootV->depth)
	{
		rootU->parent = rootV;
	}
	else
	{
		rootV->parent = rootU;
		if (rootU->depth == rootV->depth && rootU != rootV)
		{
			rootU->depth++;
		}
	}
}

int split1(int n, int m, std::vector<Status> &state, std::vector<std::vector< std::string>> &phi)
{
	int mm = n;
	for (int i = 0; i < n; i++)
	{
		state[i].parent = &state[i];
		state[i].depth = 0;
	}

	for (int q1 = 0; q1 < n; q1++)
	{
		for (int q2 = q1 + 1; q2 < n; q2++)
		{
			if (find(&state[q1]) != find(&state[q2]))
			{
				bool eq = true;
				for (int x = 0; x < m; x++)
				{
					if (phi[q1][x] != phi[q2][x])
					{
						eq = false;
						break;
					}
				}

				if (eq)
				{
					unionFunc(&state[q1], &state[q2]);
					mm -= 1;
				}
			}
		}
	}

	for (int q = 0; q < n; q++)
	{
		state[q].pi = find(&state[q]);
	}

	return m;
}

int split(int n, int m, std::vector<Status> &state, std::vector<std::vector< std::string>> &phi)
{
	int mm = n;
	for (int i = 0; i < n; i++)
	{
		state[i].parent = &state[i];
		state[i].depth = 0;
	}

	for (int q1 = 0; q1 < n; q1++)
	{
		for (int q2 = q1 + 1; q2 < n; q2++)
		{
			if (state[q1].pi == state[q2].pi &&
				find(&state[q1]) != find(&state[q2]))
			{
				bool eq = true;
				for (int x = 0; x < m; x++)
				{
					Status *w1 = state[q1].delt[x];
					Status *w2 = state[q2].delt[x];
					if (w1->pi != w2->pi)
					{
						eq = false;
						break;
					}
				}

				if (eq)
				{
					unionFunc(&state[q1], &state[q2]);
					mm -= 1;
				}
			}
		}
	}

	for (int q = 0; q < n; q++)
	{
		state[q].pi = find(&state[q]);
	}

	return m;
}

void DFS(int q0, std::vector<std::vector < int>> &D, Status *s, int &count, int m, std::vector< Status > &stat)
{
	s->i = count;
	count++;
	s->was = true;
	for (int i = 0; i < m; i++)
	{
		int qq = D[q0][i];
		if (!(s->delt[i]->pi->was))
		{
			DFS(qq, D, s->delt[i]->pi, count, m, stat);
		}
	}
}

void printautomata(std::vector<Status> &state, std::vector<std::vector< std::string>> &phi, int q0, int n, int m)
{
	std::cout << "digraph {\n\trankdir = LR\n";
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < m; j++)
		{
			if (state[i].was)
			{
				std::cout << "\t" << state[i].i << " -> " << state[i].delt[j]->pi->i << "[label = \"" << char(97 + j) << "(" << phi[i][j] << ")\"]\n";
			}
		}
	}

	std::cout << "}";
}

void AufenkampHohn(int n, int m, int q0, std::vector<std::vector < int>> &delta, std::vector< Status > &state, std::vector< std::vector< std::string>> &phi, int &count)
{
	int m1 = split1(n, m, state, phi);
	while (true)
	{
		int m2 = split(n, m, state, phi);
		if (m1 == m2)
		{
			break;
		}

		m1 = m2;
	}

	DFS(q0, delta, state[q0].pi, count, m, state);
}

int main()
{
	int n, m, q0;
	int count = 0;
	std::cin >> n;
	std::cin >> m;
	std::cin >> q0;

	std::vector<std::vector < int>> delta(n, std::vector<int> (m));
	std::vector<std::vector<std::string>> phi(n, std::vector<std::string > (m));
	std::vector<Status> state(n);

	for (int i = 0; i < n; i++)
	{
		delta[i].resize(m);
		state[i].delt.resize(m);
		for (int j = 0; j < m; j++)
		{
			std::cin >> delta[i][j];
			state[i].delt[j] = &state[delta[i][j]];
		}
	}

	for (int i = 0; i < n; i++)
	{
		phi[i].resize(m);
		for (int j = 0; j < m; j++)
		{
			std::cin >> phi[i][j];
		}
	}

	AufenkampHohn(n, m, q0, delta, state, phi, count);
	printautomata(state, phi, q0, n, m);
	return 0;
}
