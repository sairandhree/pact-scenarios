package server

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/rs/cors"
)

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

var users = []User{
	{
		Name:  "user1",
		Email: "user1@gmail.com",
		Address: Address{
			AddrStr1: " addr 1",
			AddrStr2: " addr 2",
			City:     "pune",
			Pin:      411045,
		},
	},
	{
		Name:  "Foo",
		Email: "bar@baz.com",
		Address: Address{
			AddrStr1: "addrStr2",
			AddrStr2: "addrStr2",
			City:     "pune",
			Pin:      411045,
		},
	},
}

func StartServer() {
	//w.Header().Set("Content-Type", "application/json")
	router := mux.NewRouter()
	router.HandleFunc("/users", getUsers).Methods("GET")
	router.HandleFunc("/users/{email}", getUser).Methods("GET")
	handler := cors.Default().Handler(router)
	log.Fatal(http.ListenAndServe(":8080", handler))

	log.Println("Listening on localhost:8080")

}

func getUsers(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	log.Println("Fetch all users called")
	json.NewEncoder(w).Encode(users)
}

//Get Single Roll
func getUser(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	params := mux.Vars(r)

	log.Println("User fetched with ID %s", params["email"])

	for _, user := range users {
		if user.Email == params["email"] {
			json.NewEncoder(w).Encode(user)
			return
		}
	}
}
