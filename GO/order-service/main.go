package main

import (
	"fmt"
	"orderservice/server"
)

func main() {

	fmt.Println("Starting Order service ...")
	server.StartServer()

}
