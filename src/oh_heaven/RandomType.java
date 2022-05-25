package oh_heaven;

import ch.aplu.jcardgame.*;

public class RandomType implements Type{
    @Override
    public Card play(Hand hand, Game.Suit lead, Game.Suit trump) {
        int x = Game.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
