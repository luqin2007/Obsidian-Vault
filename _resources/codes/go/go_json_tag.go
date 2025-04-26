package main

import (
	"encoding/json"
	"fmt"
)

type person struct {
	Name string
	Age  int
}

func main() {
	p := person{"Alice", 20}
	fmt.Println(p)
	// struct -> json
	bs, err := json.Marshal(p)
	str := string(bs)
	if err == nil {
		fmt.Println(str)
	} else {
		fmt.Println(err)
	}
	// json -> struct
	var p2 person
	err = json.Unmarshal([]byte(str), &p2)
	if err == nil {
		fmt.Println(p2)
	} else {
		fmt.Println(err)
	}
}
