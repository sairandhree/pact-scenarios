package server

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/Shopify/sarama"
	"github.com/gorilla/mux"
	"github.com/rs/cors"
)

type Order struct {
	OrderID       int32    `json:"orderId,omitempty"`
	Books         []string `json:"books,omitempty"`
	CustomerEmail string   `json:"customerEmail,omitempty"`
}

var producer sarama.SyncProducer

func StartServer() {

	router := mux.NewRouter()
	router.HandleFunc("/order", postOrder).Methods("POST")
	handler := cors.Default().Handler(router)

	log.Fatal(http.ListenAndServe(":8081", handler))

	log.Println("Listening on localhost:8081")

}

func postOrder(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var newOrder Order
	json.NewDecoder(r.Body).Decode(&newOrder)

	log.Printf("Order '%+v'  is received.", newOrder)
	sendMessageToWarehouse(newOrder)

	json.NewEncoder(w).Encode(newOrder)
	return
}

func sendMessageToWarehouse(order Order) {
	producer, err :=
		sarama.NewSyncProducer([]string{"localhost:9092"}, nil)
	if err != nil {
		panic(err)
	}
	orderstr, _ := json.Marshal(order)
	msg := &sarama.ProducerMessage{Topic: "orders", Value: sarama.StringEncoder(string(orderstr))}
	partition, offset, err := producer.SendMessage(msg)
	if err != nil {

		log.Printf("FAILED to send message: %s\n", err)
		return
	}
	log.Printf(" message sent to partition %d at offset %d\n",
		partition, offset)
	return
}
