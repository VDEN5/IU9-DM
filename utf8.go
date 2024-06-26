package main

import (
	"fmt"
	"math"
)

func encode(a []rune) []byte {
	var s []byte
	for i := 0; i < len(a); i++ {
		if a[i] < rune(math.Pow(2, 7)) {
			s = append(s, byte(a[i]))
		} else if a[i] < rune(math.Pow(2, 11)) {
			s = append(s, 192+byte(a[i]/64), 128+byte(a[i]%64))
		} else if a[i] < rune(math.Pow(2, 16)) {
			s = append(s, 224+byte(a[i]/4096), 128+byte(a[i]/64%64), 128+byte(a[i]%64))
		} else {
			a1, a2, a3 := byte(a[i]/262144), byte(a[i]/4096%64), byte(a[i]/64%64)
			s = append(s, 240+a1, 128+a2, 128+a3, 128+byte(a[i]%64))
		}
	}
	return s
}
func decode(a []byte) []rune {
	var (
		r []rune
		i int = 0
	)
	for {
		if i == len(a) {
			return r
		}
		if a[i] < byte(math.Pow(2, 7)) {
			r = append(r, rune(a[i]))
			i++
		} else if a[i] < 224 {
			r = append(r, rune(a[i]-192)*64+rune(a[i+1]-128))
			i += 2
		} else if a[i] < 240 {
			r = append(r, rune(a[i]-224)*4096+rune(a[i+1]-128)*64+rune(a[i+2]-128))
			i += 3
		} else {
			a1, a2, a3 := rune(a[i]-240), rune(a[i+1]-128), rune(a[i+2]-128)
			r = append(r, a1*262144+a2*4096+a3*64+rune(a[i+3]-128))
			i += 4
		}
	}
}
func main() {
	s := "привет"
	r := ([]byte)(s)
	b := decode(r)
	for _, x := range b {
		fmt.Printf("%c %d\n", x, x)
	}
	r1 := ([]rune)(s)
	b1 := encode(r1)
	for _, x := range b1 {
		fmt.Printf("%c %d\n", x, x)
	}
}
