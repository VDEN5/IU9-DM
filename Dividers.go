package main

import "fmt"

func getDividers(x int) []int {
	res := make([]int, 1)
	res[0] = x
	for i := x / 2; i >= 1; i-- {
		if x%i == 0 {
			res = append(res, i)
		}
	}
	return res
}
func pr(p int) bool {
	if p == 4 {
		return false
	}
	for i := 2; i < p/2; i++ {
		if (p)%i == 0 {
			return false
		}
	}
	return true
}
func main() {
	var x int
	fmt.Scan(&x)
	arr := getDividers(x)
	fmt.Println("graph {")
	for i := 0; i < len(arr); i++ {
		fmt.Println("    ", arr[i])
	}
	for i := 0; i < len(arr); i++ {
		for j := i + 1; j < len(arr); j++ {
			if arr[i]%arr[j] == 0 {
				if pr((int)(arr[i] / arr[j])) {
					fmt.Println("    ", arr[i], "--", arr[j])
				}
			}
		}
	}
	fmt.Println("}")
}
