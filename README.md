## client -> server

```json
{
	"event": "JOIN_GAME",
	"game_mode": "Holdem",
	"player_name": "pepe"
}
{
	"event": "PLACE_BET",
	"current_player": 0,
	"game_mode": "Omaha",
	"bet": 5000
},
{
	"event": "PLAY_CARD",
	"game_mode": "Holdem",
	"current_player": 0,
	"card": {
		"value": 2,
		"suit": "hearts"
	}
},
{
	"event": "CHANGE_CARTS",
	"current_player": 0,
	"cards": [
		{ "value": 3, "suit": "hearts" },
		{ "value": 4, "suit": "hearts" },
		{ "value": 5, "suit": "hearts" }
	]
}
```

## servidor -> cliente  
  
```json
{  
	event: "PLAYER_ACTION",
	game_mode: "Omaha",  
	player: "aaron",  
	next_player: "franco",  
	action: "raise",  
	dealer: "p1",  
	pot: 800,  
}  
	  
{  
	event: "ROUND_OVER",  
	gamemode: "holdem",  
	next_round: "show",  
	pot: 500  
}

{ 
	event: "GAME_STARTED",
	players: [
		{
			player: "aaron",
			index: 0
		},
		{
			player: "franco",
			index: 1
		},
		{
			player: "daniel",
			index: 2
		},
		{
			player: "sergio",
			index: 3
		}
	 ],  
	game_mode: "holdem",  
	next_player: "aaron",  
	dealer: "Daniel",  
	small_blind : "sergio",  
	big_blind: "aaron"  
}
  
  
{  
	event: "GAME_ENDED",  
	game_mode: "holdem",  
	winners: [
		{ 
			player: "sergio",
			pot: 300
		}
	]
}
```
