package poker.gamemodes;

import poker.items.PlayerModel;

public record PokerAction(PlayerModel player, PlayerAction action, int bet) {
}
