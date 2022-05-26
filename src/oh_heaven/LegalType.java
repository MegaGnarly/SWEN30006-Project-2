package oh_heaven;

import ch.aplu.jcardgame.*;

public class LegalType implements Type{
    @Override
    public Card play(Hand hand, Game.Suit lead, Game.Suit trump) {
        int x;
        if(lead == null){
            x = Game.random.nextInt(hand.getNumberOfCards());
        }
        else if(hand.getNumberOfCardsWithSuit(lead) > 0){
            return Game.randomCard(hand.getCardsWithSuit(lead));
        }
        else{
            x = Game.random.nextInt(hand.getNumberOfCards());
        }
        return hand.get(x);
    }
}
