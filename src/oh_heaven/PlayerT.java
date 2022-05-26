package oh_heaven;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

public interface PlayerT{
    public Card PlayCard(Deck deck, Game.Suit lead, Game.Suit trump);

    public int getScore();

    public void setScore(int score);

    public int getTricks();

    public void setTricks(int tricks);

    public int getBid();

    public void setBid(int bid);

    public Hand getHand();

}
