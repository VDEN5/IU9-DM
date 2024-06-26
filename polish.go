package main

import (
	"bufio"
	"fmt"
	"os"
)

func opp(a byte) bool {
	s := (string)(a)
	return (s == "+") || (s == "-") || (s == "*")
}
func digp(a byte) bool {
	return (a >= "0"[0]) && (a <= "9"[0])
}
func myscan() string {
	in := bufio.NewScanner(os.Stdin)
	in.Scan()
	return in.Text()
}
func ops(s byte, a int, b int) int {
	if s == "+"[0] {
		return a + b
	}
	if s == "*"[0] {
		return a * b
	}
	return b - a
}
func digs(a byte) int {
	return (int)(a) - 48
}
func main() {
	var s string
	j := 0
	s = myscan()
	var stack [1000]int
	for i := len(s) - 1; i >= 0; i-- {
		if digp(s[i]) {
			stack[j] = digs(s[i])
			j++
		} else if opp(s[i]) {
			b := stack[j-1]
			a := stack[j-2]
			stack[j-2] = ops(s[i], a, b)
			j--
		}
	}
	res := stack[0]
	fmt.Printf("%d", res)
}
