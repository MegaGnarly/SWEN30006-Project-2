package oh_heaven;

import ch.aplu.jcardgame.*;


public interface Type {
    public Card play(Hand hand, Game.Suit lead, Game.Suit trump);
}
