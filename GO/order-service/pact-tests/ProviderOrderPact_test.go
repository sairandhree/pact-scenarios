package test

import (
	"fmt"
	"orderservice/server"
	"os"
	"path/filepath"
	"testing"

	"github.com/pact-foundation/pact-go/dsl"
)

var order *server.Order

// The actual Provider test itself
func TestOrderProvider(t *testing.T) {
	pact := createPact()

	// Map test descriptions to message producer (handlers)

	functionMappings := dsl.MessageHandlers{
		"matching Order format": func(m dsl.Message) (interface{}, error) {
			return order, nil
		},
	}

	stateMappings := dsl.StateHandlers{
		"Order received": func(s dsl.State) error {
			order = &server.Order{
				OrderID:       41,
				Books:         []string{"book2 title", "book2 title"},
				CustomerEmail: "foo@bar.baz",
			}

			return nil
		},
	}
	//verfy the local Pact Files
	pact.VerifyMessageProvider(t, dsl.VerifyMessageRequest{
		PactURLs:        []string{filepath.ToSlash(fmt.Sprintf("%s/warehouseorderconsumergo-orderservice.json", pactDir))},
		MessageHandlers: functionMappings,
		StateHandlers:   stateMappings,
	})
}

// Configuration / Test Data
var dir, _ = os.Getwd()
var pactDir = fmt.Sprintf("%s/../../pacts", dir)
var logDir = fmt.Sprintf("%s/../../logs", dir)

// Setup the Pact client.
func createPact() dsl.Pact {
	return dsl.Pact{
		Provider: "OrderService",
		LogDir:   logDir,
	}
}
