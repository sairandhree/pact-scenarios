package main

import (
	"log"
	"warehouse/server"
)

func main() {

	//defer producer.Close()
	log.Println("Starting warehouse service...")
	server.StartConsumer()

}
