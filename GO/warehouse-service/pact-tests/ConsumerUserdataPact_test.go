package test

import (
	"fmt"
	"testing"

	"net/url"
	"warehouse/server"

	"github.com/pact-foundation/pact-go/dsl"
)

var commonHeaders = dsl.MapMatcher{
	"Content-Type": term("application/json; charset=utf-8", `application\/json`),
}

var u *url.URL

func TestClientPact_GetUser(t *testing.T) {
	setup()

	t.Run("the user exists", func(t *testing.T) {

		pactRest.
			AddInteraction().
			Given("User with email exists").
			UponReceiving("Send user address").
			WithRequest(request{
				Method: "GET",
				Path:   term("/users/user1@gmail.com", "/users/user1@gmail.com"),
				//Headers: commonHeaders,
			}).
			WillRespondWith(dsl.Response{
				Status: 200,
				Body: map[string]interface{}{
					"name":  like("user1"),
					"email": term("user1@gmail.com", `^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$`),
					"address": map[string]interface{}{
						"addressstr1": like("addr 1"),
						"addressstr2": like("addr 2"),
						"city":        like("pune"),
						"pin":         like(411045),
					},
				},
				Headers: commonHeaders,
			})

		err := pactRest.Verify(func() error {
			fmt.Println("success")
			_, err := server.GetAddress(pactRest.Server.Port, "user1@gmail.com")

			return err
		})

		if err != nil {
			t.Fatalf("Error on Verify: %v", err)
		}
		pactRest.WritePact()
	})

}

// Common test data
var pactRest dsl.Pact

// Aliases
//var term = dsl.Term

//type request = dsl.Request

func setup() {
	pactRest = createPact()

	// Proactively start service to get access to the port
	pactRest.Setup(true)

	u, _ = url.Parse(fmt.Sprintf("http://localhost:%d", 8080))

}

func createPact() dsl.Pact {
	return dsl.Pact{
		Consumer:                 "UserDataConsumerGo",
		Provider:                 "UserDataProvider",
		LogDir:                   "./logs",
		PactDir:                  "../../pacts",
		LogLevel:                 "INFO",
		DisableToolValidityCheck: true,
	}
}
