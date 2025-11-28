## client -> server

```json
{
	"event": "JOIN_GAME",
	"game_mode": "Holdem",
	"player_name": "pepe"
}
{
	"event": "PLACE_BET",
	"game_mode": "Omaha",
	"current_player": 0,
	"player_action": "RAISE"
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
	"event": "PLAYER_ACTION",
	"game_mode": "Omaha",
	"player": "aaron",
	"next_player": "franco",
	"pokerAction": "raise",
	"dealingMethod": "p1",
	"poker.pot": 800
}
{
	"event": "ROUND_OVER",
	"game_mode": "Holdem",
	"next_round": "show",
	"poker.pot": 500
}
{
	"event": "GAME_STARTED",
	"playerModels": [
		{ "player": "aaron", "index": 0 },
		{ "player": "franco", "index": 1 },
		{ "player": "daniel", "index": 2 },
		{ "player": "sergio", "index": 3 }
	],
	"game_mode": "Holdem",
	"next_player": "aaron",
	"dealingMethod": "Daniel",
	"small_blind": "sergio",
	"big_blind": "aaron"
}
{
	"event": "GAME_ENDED",
	"game_mode": "Holdem",
	"winners": [
		{
			"player": "sergio",
			"poker.pot": 300
		}
	]
}
```
