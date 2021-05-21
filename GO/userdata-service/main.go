package main

import (
	"fmt"

	"userdata/server"
)

func main() {
	fmt.Println("starting userdata service ...")
	server.StartServer()

}
