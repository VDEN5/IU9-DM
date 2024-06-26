package main

import "fmt"

type Vertex struct {
	time, comp, low, id int
	edges               []int
}
type Stack struct {
	data []*Vertex
	head int
}

func tarjan(graph []*Vertex, time *int, count *int) {
	for i := 0; i < len(graph); i++ {
		graph[i].time, graph[i].comp = 0, 0
	}
	stack := Stack{make([]*Vertex, len(graph)), 0}
	var visit_vertex func(vertex *Vertex)
	visit_vertex = func(vertex *Vertex) {
		vertex.time = *time
		vertex.low = *time
		*time++
		stack.data[stack.head] = vertex
		stack.head++
		for i := 0; i < len(vertex.edges); i++ {
			u := graph[vertex.edges[i]]
			if u.time == 0 {
				visit_vertex(u)
			}
			if u.comp == 0 && vertex.low > u.low {
				vertex.low = u.low
			}
		}
		if vertex.time == vertex.low {
			for {
				stack.head--
				v := stack.data[stack.head]
				v.comp = *count
				if v == vertex {
					break
				}
			}
			*count++
		}
	}
	for i := 0; i < len(graph); i++ {
		if graph[i].time == 0 {
			visit_vertex(graph[i])
		}
	}
}
func main() {
	var N, M int
	fmt.Scan(&N, &M)
	graph := make([]*Vertex, N)
	for i := 0; i < N; i++ {
		graph[i] = &Vertex{}
		graph[i].id = i
	}
	for i := 0; i < M; i++ {
		var u, v int
		fmt.Scan(&u, &v)
		graph[u].edges = append(graph[u].edges, v)
	}
	time, count := 1, 1
	tarjan(graph, &time, &count)

	condensMinimal := make([]int, count)
	сondensInBase := make([]bool, count)
	for i := 0; i < count; i++ {
		сondensInBase[i], condensMinimal[i] = true, -1
	}

	for i := 0; i < len(graph); i++ {
		if condensMinimal[graph[i].comp] == -1 {
			condensMinimal[graph[i].comp] = i
		}
		for j := 0; j < len(graph[i].edges); j++ {
			u := graph[graph[i].edges[j]]
			if graph[i].comp != u.comp {
				сondensInBase[u.comp] = false
			}
		}
	}

	for i := 1; i < count; i++ {
		if сondensInBase[i] {
			fmt.Print(condensMinimal[i], " ")
		}
	}
}
