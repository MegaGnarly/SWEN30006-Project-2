package oh_heaven;

import ch.aplu.jcardgame.Deck;

public class PlayerFactory {
    public static PlayerT getPlayer(String type, Deck deck) {
        PlayerT player;
        // creates players based on type
        if (type.equals("random")){
            player = new RandomPlayer(deck);
        }
        else if (type.equals("legal")){
            player = new LegalPlayer(deck);
        }
        else if (type.equals("smart")){
            player = new SmartPlayer(deck);
        }
        else if (type.equals("human")){
            player = new HumanPlayer(deck);
        }
        else{
            player = new RandomPlayer(deck);
        }
//        dealingOut(hands, nbPlayers, nbStartCards);
        return player;
    }
}
