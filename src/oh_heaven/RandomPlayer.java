package oh_heaven;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class RandomPlayer implements PlayerT{
    int score = 0;
    int tricks = 0;
    int bid = 0;
    Hand hand;

    public RandomPlayer(Deck deck) {
        this.hand = new Hand(deck);
    }

    @Override
    public Card PlayCard(Deck deck, Game.Suit lead, Game.Suit trump) {
        int x = Game.random.nextInt(hand.getNumberOfCards());
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
