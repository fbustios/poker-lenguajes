package poker.gamemodes;

import poker.items.PlayerModel;

public record Action(PlayerModel player, PlayerAction action) {
}
