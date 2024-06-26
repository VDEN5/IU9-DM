package main

import "fmt"

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
func add(a, b []int32, p int) []int32 {
	var q, r int32
	q = 0
	var res []int32 //a[i]+b[i]=qp+r
	for i := 0; i < min(len(a), len(b)); i++ {
		r = (a[i] + b[i] + q) % (int32)(p)
		q = (a[i] + b[i] + q) / (int32)(p)
		res = append(res, r)
	}
	if len(a) > len(b) {
		for i := min(len(a), len(b)); i < len(a)+len(b)-min(len(a), len(b)); i++ {
			r = (a[i] + q) % (int32)(p)
			q = (a[i] + q) / (int32)(p)
			res = append(res, r)
		}
	} else {
		for i := min(len(a), len(b)); i < len(a)+len(b)-min(len(a), len(b)); i++ {
			r = (b[i] + q) % (int32)(p)
			q = (b[i] + q) / (int32)(p)
			res = append(res, r)
		}
	}
	if q == 1 {
		res = append(res, 1)
	}
	return res
}
func main() {
	a := []int32{1, 2, 4, 5, 7}
	b := []int32{4, 1, 7, 4, 5, 3, 6, 2}
	res := add(a, b, 8)
	for _, x := range res {
		fmt.Printf("%d ", x)
	}
}
