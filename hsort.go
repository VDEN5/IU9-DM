package main

import "fmt"

func hsort(n int, less func(i, j int) bool, swap func(i, j int)) {
	heapify := func(start, end int) {
		root := start
		for {
			child := 2*root + 1
			if child > end {
				break
			}
			if child+1 <= end && less(child, child+1) {
				child++
			}
			if !less(root, child) {
				return
			}
			swap(root, child)
			root = child
		}
	}

	for start := n/2 - 1; start >= 0; start-- {
		heapify(start, n-1)
	}

	for end := n - 1; end >= 0; end-- {
		swap(0, end)
		heapify(0, end-1)
	}
}

func main() {
	data := []int{12, 3, 45, 7, 23, 9, 17}

	less := func(i, j int) bool { return data[i] < data[j] }
	swap := func(i, j int) { data[i], data[j] = data[j], data[i] }

	fmt.Println("Unsorted data:", data)

	hsort(len(data), less, swap)

	fmt.Println("Sorted data:", data)
}
