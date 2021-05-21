package server

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"os/signal"

	"github.com/Shopify/sarama"
)

const userServiceUrl = "http://localhost:%d/users/"

type User struct {
	Name    string  `json:"name,omitempty"`
	Email   string  `json:"email,omitempty"`
	Address Address `json:"address,omitempty"`
}

type Address struct {
	AddrStr1 string `json:"addressstr1,omitempty"`
	AddrStr2 string `json:"addressstr2,omitempty"`
	City     string `json:"city,omitempty"`
	Pin      int32  `json:"pin,omitempty"`
}
type Order struct {
	OrderID       int32    `json:"orderId,omitempty"`
	Books         []string `json:"books,omitempty"`
	CustomerEmail string   `json:"customerEmail,omitempty"`
}

func StartConsumer() {
	config := sarama.NewConfig()
	config.Consumer.Return.Errors = true

	brokers := []string{"localhost:9092"}

	// Create new consumer
	master, err := sarama.NewConsumer(brokers, config)
	if err != nil {
		panic(err)
	}

	defer func() {
		if err := master.Close(); err != nil {
			panic(err)
		}
	}()

	topic := "orders"
	// How to decide partition, is it fixed value...?
	consumer, err := master.ConsumePartition(topic, 0, sarama.OffsetNewest)
	if err != nil {
		panic(err)
	}

	signals := make(chan os.Signal, 1)
	signal.Notify(signals, os.Interrupt)

	// Count how many message processed
	msgCount := 0

	// Get signnal for finish
	doneCh := make(chan struct{})
	go func() {
		for {
			select {
			case err := <-consumer.Errors():
				fmt.Println(err)
			case msg := <-consumer.Messages():
				msgCount++
				fmt.Println("Received messages", string(msg.Key), string(msg.Value))
				shipOrder(string(msg.Value))
			case <-signals:
				fmt.Println("Interrupt is detected")
				doneCh <- struct{}{}
			}
		}
	}()

	<-doneCh
	fmt.Println("Processed", msgCount, "messages")
}

func ValidateOrder(order *Order) error {

	fmt.Println(order.CustomerEmail)
	if order.CustomerEmail == "" {
		return errors.New("invalid object supplied, missing fields (customerEmail)")
	}
	return nil
}

func shipOrder(msg string) {

	order := &Order{}
	err := json.Unmarshal([]byte(msg), order)
	if err != nil {
		fmt.Printf("err unmarshalling order %s\n", err)
		return
	}
	err = ValidateOrder(order)
	if err != nil {
		fmt.Printf("err validating order %s\n", err)
		return
	}

	address, err := GetAddress(8080, order.CustomerEmail)
	if err != nil {
		fmt.Printf("err validating order %s\n", err)
		return
	}
	shipToAddress(order, address)
}

func shipToAddress(order *Order, address Address) {

	fmt.Printf("order from %s is shipped to address %v", order.CustomerEmail, address)
}

func GetAddress(port int, email string) (Address, error) {

	url := fmt.Sprintf(userServiceUrl, port)
	response, err := http.Get(url + email)
	if err != nil {
		fmt.Printf("The HTTP request failed with error %s\n", err)
	}
	data, _ := ioutil.ReadAll(response.Body)
	fmt.Println(string(data))

	user := &User{
		Address: Address{},
	}
	err = json.Unmarshal([]byte(string(data)), user)
	if err != nil {
		fmt.Printf("err unmarshalling user %s\n", err)

	}
	fmt.Printf("unmarshlled user address is  %v\n", &user.Address)

	if user.Address.Pin == 0 {
		return user.Address, fmt.Errorf("Something's wrong with address...")
	}
	return user.Address, nil
}
