package oh_heaven;

import ch.aplu.jcardgame.*;

import static ch.aplu.jgamegrid.GameGrid.delay;

public class HumanPlayer implements PlayerT{
    private int score = 0;
    private int tricks = 0;
    private int bid = 0;
    private Hand hand;
    private Card selected;

    public HumanPlayer(Deck deck) {
        this.hand = new Hand(deck);
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card; hand.setTouchEnabled(false); }
        };
        hand.addCardListener(cardListener);
    }

    @Override
    public Card PlayCard(Deck deck, Game.Suit lead, Game.Suit trump, Card winningCard) {
        selected = null;
        hand.setTouchEnabled(true);
        while (null == selected) delay(100);
        return selected;
    }
    public int getScore(){
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTricks() {
        return tricks;
    }

    public void setTricks(int tricks) {
        this.tricks = tricks;
    }

    public int getBid(){
        return this.bid;
    }

    public void setBid(int bid){
        this.bid =bid;
    }

    public Hand getHand() {
        return hand;
    }


}
