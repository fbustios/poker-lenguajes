package poker.dealing;

import poker.items.Deck;
import poker.table.PokerTable;
import poker.items.PlayerModel;


public class StudRazzDealer implements Dealer {
    private enum Stage { THIRD, FOURTH, FIFTH, SIXTH, SEVENTH };

    private Stage currentStage = Stage.THIRD;

    @Override
    public void deal(PokerTable table, Deck deck) {
        switch (currentStage){
            case THIRD -> {
                dealThirdStreet(table, deck);
                dealOneUpCard(table, deck);
                currentStage = Stage.FOURTH;
            }
            case FOURTH -> {
                dealOneUpCard(table, deck);
                currentStage = Stage.FIFTH;
            }
            case FIFTH -> {
                dealOneUpCard(table, deck);
                currentStage = Stage.SIXTH;
            }

            case SIXTH -> {
                dealOneUpCard(table, deck);
                currentStage = Stage.SEVENTH;
            }

            case SEVENTH -> {
                dealSeventhStreet(table, deck);
            }


        }

    }

    private void dealThirdStreet(PokerTable table, Deck deck){
        int playerCount = table.getActivePlayers().size();
        for(int i = 0; i < playerCount; i++){
            PlayerModel player = table.next();
            player.receiveCard(deck.draw());
            player.receiveCard(deck.draw());
        }
    }

    private void dealOneUpCard(PokerTable table, Deck deck) {
        int playerCount = table.getActivePlayers().size();
        for (int i = 0; i < playerCount; i++) {
            PlayerModel player = table.next();
            if(!player.isFolded()) {
                player.receiveCard(deck.draw());
            }
        }
    }

    private void dealSeventhStreet(PokerTable table, Deck deck) {
        int playerCount = table.getActivePlayers().size();
        for (int i = 0; i < playerCount; i++) {
            PlayerModel player = table.next();
            player.receiveCard(deck.draw());

        }
    }
}
