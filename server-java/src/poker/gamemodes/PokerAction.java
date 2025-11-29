package poker.gamemodes;

import poker.items.Player;

public record PokerAction(Player player, PlayerAction action, int bet) {
}
