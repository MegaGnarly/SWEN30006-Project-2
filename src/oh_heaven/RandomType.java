package oh_heaven;

import ch.aplu.jcardgame.*;

public class RandomType implements Type{
    @Override
    public Card play(Hand hand) {
        int x = Game.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
