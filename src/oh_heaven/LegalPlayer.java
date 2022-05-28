package oh_heaven;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class LegalPlayer implements PlayerT{
    private int score = 0;
    private int tricks = 0;
    private int bid = 0;
    private Hand hand;

    public LegalPlayer(Deck deck) {
        this.hand = new Hand(deck);
    }

    @Override
    public Card PlayCard(Deck deck, Game.Suit lead, Game.Suit trump, Card winningCard) {
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
