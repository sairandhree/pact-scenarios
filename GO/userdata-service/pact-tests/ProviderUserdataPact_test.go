package test

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"testing"
	"userdata/server"

	"github.com/pact-foundation/pact-go/dsl"
	"github.com/pact-foundation/pact-go/types"
)

var dir, _ = os.Getwd()
var pactDir = fmt.Sprintf("%s/../../pacts", dir)

var user *server.User

func TestProvider(t *testing.T) {
	// Create Pact connecting to local Daemon
	pact := &dsl.Pact{
		Provider:                 "UserDataProvider",
		DisableToolValidityCheck: true,
		LogDir:                   ".",
		LogLevel:                 "DEBUG",
	}

	// Start provider API in the background
	go startServer()

	// Verify the Provider with local Pact Files
	_, err := pact.VerifyProvider(t, types.VerifyRequest{
		BrokerURL:                  "https://pact-broker.cloudapps.pt.ibm.com", //fmt.Sprintf("%s://%s", os.Getenv("PACT_BROKER_PROTO"), os.Getenv("PACT_BROKER_URL")),
		Tags:                       []string{"master"},
		PublishVerificationResults: true,
		FailIfNoPactsFound:         true, // yet to be implemented
		ProviderVersion:            "1.3",

		//PactURLs:        []string{filepath.ToSlash(fmt.Sprintf("%s/userdataconsumergo-userdataprovider.json", pactDir))},

		StateHandlers:   stateHandlers,
		ProviderBaseURL: "http://localhost:8000",
	})
	if err != nil {
		//t.Fatal(err)
	}
}

var stateHandlers = types.StateHandlers{
	"User with email exists": func() error {
		user = &server.User{
			Name:  "user1",
			Email: "user1@gmail.com",
			Address: server.Address{
				AddrStr1: "addr 1",
				AddrStr2: "addr 2",
				City:     "pune",
				Pin:      411045,
			},
		}
		return nil
	},
}

func startServer() {
	mux := http.NewServeMux()

	mux.HandleFunc("/users/user1@gmail.com", func(w http.ResponseWriter, req *http.Request) {
		w.Header().Add("Content-Type", "application/json")
		userStr, _ := json.Marshal(user)
		fmt.Fprintf(w, string(userStr))
	})

	log.Fatal(http.ListenAndServe("localhost:8000", mux))
}
