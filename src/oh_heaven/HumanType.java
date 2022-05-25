package oh_heaven;

import ch.aplu.jcardgame.*;
import static ch.aplu.jgamegrid.GameGrid.delay;

public class HumanType implements Type {
    private Card selected;

    public HumanType(Hand hand) {
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card; hand.setTouchEnabled(false); }
        };
        hand.addCardListener(cardListener);
    }

    @Override
    public Card play(Hand hand, Game.Suit lead, Game.Suit trump) {
        selected = null;
        hand.setTouchEnabled(true);
        while (null == selected) delay(100);
        return selected;
    }
}
