package main

import "fmt"

func merge(ch1 chan int, ch2 chan int, compare func(i, j int) int, res chan int) {
	val1, ok1 := <-ch1
	val2, ok2 := <-ch2
	for ok1 || ok2 {
		if !ok1 {
			res <- val2
			val2, ok2 = <-ch2
		} else {
			if !ok2 {
				res <- val1
				val1, ok1 = <-ch1
			} else {
				if compare(val1, val2) > 0 {
					res <- val2
					val2, ok2 = <-ch2
				} else {
					res <- val1
					val1, ok1 = <-ch1
				}
			}
		}
	}
	close(res)
}

func MergeSeqSort(nitems int, compare func(i, j int) int, indices chan int) {
	n, allchan := nitems, make([]chan int, nitems)
	for i := 0; i < nitems; i++ {
		ch := make(chan int)
		go func(i int) {
			ch <- i
			close(ch)
		}(i)
		allchan[i] = ch
	}
	for nitems > 1 {
		arrchan := make([]chan int, nitems/2+nitems%2)
		for i := 0; i < nitems-1; i += 2 {
			ch := make(chan int)
			go merge(allchan[i], allchan[i+1], compare, ch)
			arrchan[i/2] = ch
		}
		if nitems%2 == 1 {
			arrchan[nitems/2] = allchan[nitems-1]
		}
		allchan = arrchan
		nitems = nitems/2 + nitems%2
	}
	for i := 0; i < n; i++ {
		v, _ := <-allchan[0]
		indices <- v
	}
	close(indices)
}

func compareFunc(i, j int) int {
	values := []int{5, 6, 3, 4, 2, 1}
	if values[i] < values[j] {
		return -1
	} else if values[i] > values[j] {
		return 1
	}
	return 0
}

func main() {
	nitems := 6
	indices := make(chan int)
	go MergeSeqSort(nitems, compareFunc, indices)
	for index := range indices {
		fmt.Printf("%d ", index)
	}
}
