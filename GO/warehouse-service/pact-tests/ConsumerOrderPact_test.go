package test

import (
	"fmt"
	"os"
	"testing"
	"warehouse/server"

	"github.com/pact-foundation/pact-go/dsl"
)

var like = dsl.Like
var eachLike = dsl.EachLike

var term = dsl.Term

type request = dsl.Request

var pact = CreatePact()

func TestMessageConsumer_Order(t *testing.T) {

	message := pact.AddMessage()
	message.
		Given("Order received").
		ExpectsToReceive("matching Order format").
		//WithMetadata(commonHeaders).
		WithContent(map[string]interface{}{
			"orderId":       like(1),
			"books":         eachLike("Title of a book", 2),
			"customerEmail": term("user1@gmail.com", `^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$`),
		}).
		AsType(&server.Order{})

	err := pact.VerifyMessageConsumer(t, message, orderHandlerWrapper)
	if err != nil {
		//t.Fatal(err)
	}
}

var orderHandlerWrapper = func(m dsl.Message) error {
	return orderHandler(*m.Content.(*server.Order))
}

var orderHandler = func(order server.Order) error {

	server.ValidateOrder(&order)

	return nil
}

// Configuration / Test Data
var dir, _ = os.Getwd()
var pactDir = fmt.Sprintf("%s/../../pacts", dir)
var logDir = fmt.Sprintf("%s/../../logs", dir)

// Setup the Pact client.
func CreatePact() dsl.Pact {
	return dsl.Pact{
		Consumer: "WarehouseOrderConsumerGo",
		Provider: "OrderService",
		LogDir:   logDir,
		PactDir:  pactDir,
		LogLevel: "INFO",
	}
}
