package oh_heaven;

import ch.aplu.jcardgame.*;

public interface PlayerT{
    Card PlayCard(Deck deck, Game.Suit lead, Game.Suit trump, Card winningCard);

    int getScore();

    void setScore(int score);

    int getTricks();

    void setTricks(int tricks);

    int getBid();

    void setBid(int bid);

    Hand getHand();

}
