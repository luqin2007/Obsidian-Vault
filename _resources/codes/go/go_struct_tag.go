package main

import (
	"fmt"
	"reflect"
)

type person struct {
	Name string `json:"name"`
	Age  uint   `json:"age"`
}

func main() {
	tp := reflect.TypeOf(person{})
	field, ok := tp.FieldByName("Name")
	if ok {
		jsonTag := field.Tag.Get("json")
		fmt.Println(jsonTag)
	}
}
