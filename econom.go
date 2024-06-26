package main

import (
	"fmt"
	"strings"
)

func main() {
	str := ""
	fmt.Scan(&str)
	str = strings.ReplaceAll(strings.ReplaceAll(strings.ReplaceAll(str, "(", ""), ")", ""), " ", "")
	str = strings.ReplaceAll(str, "\n", "")
	stack, used, k := []string{}, map[string]bool{}, 0
	for i := len(str) - 1; i >= 0; i-- {
		if isabc(string(str[i])) {
			stack = append(stack, string(str[i]))
			continue
		}
		stack[len(stack)-2] = string(str[i]) + stack[len(stack)-1] + stack[len(stack)-2]
		stack = stack[:len(stack)-1]
		if _, ok := used[stack[len(stack)-1]]; ok {
			continue
		}
		used[stack[len(stack)-1]], k = true, k+1
	}
	fmt.Println(k)
}
func isabc(s string) bool {
	return "a" <= s && s <= "z"
}
