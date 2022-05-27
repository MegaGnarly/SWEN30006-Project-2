package oh_heaven;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class SmartPlayer implements PlayerT{
    int score = 0;
    int tricks = 0;
    int bid = 0;
    Hand hand;

    public SmartPlayer(Deck deck) {
        this.hand = new Hand(deck);
    }

    @Override
    public Card PlayCard(Deck deck, Game.Suit lead, Game.Suit trump, Card winningCard) {
        int x;
        int i = 0;
        int smallest = 0;
        // If leading, pick smallest non trump card, else pick smallest trump.
        if(lead == null){
            // If there is a non trump card
            if(hand.getNumberOfCards() > hand.getNumberOfCardsWithSuit(trump)){
                while(i<hand.getNumberOfCards()) {
                    // Looking for first card that isn't a trump
                    if(hand.get(smallest).getSuit() == trump && hand.get(i).getSuit() != trump){
                        smallest = i;
                        continue;
                    }
                    // Comparing non trump cards
                    if (Game.rankGreater(hand.get(smallest), hand.get(i)) &&
                            hand.get(i).getSuit() != trump) {
                        smallest = i;
                    }
                    i += 1;
                }
            }
            // If all cards are trump, get smallest trump card.
            else{
                while(i<hand.getNumberOfCards()){
                    if(Game.rankGreater(hand.get(smallest), hand.get(i))){
                        smallest = i;
                    }
                    i += 1;
                }
            }
            return hand.get(smallest);
        }

        // Looking for smallest card that beats current winning
        // If there is a card in hand that follows the lead
        // Get smallest card that wins
        if(hand.getNumberOfCardsWithSuit(lead) > 0) {
            while (i < hand.getNumberOfCardsWithSuit(lead)) {
                if(hand.getNumberOfCardsWithSuit(lead) == 1){
                    return hand.getCardsWithSuit(lead).get(0);
                }
                if (Game.rankGreater(hand.getCardsWithSuit(lead).get(smallest), hand.getCardsWithSuit(lead).get(i)) &&
                    Game.rankGreater(hand.getCardsWithSuit(lead).get(i), winningCard)) {
                    smallest = i;
                }
                i += 1;
            }
            i = 0;
            // If no card of lead suit beats the winning card, get smallest card of leading suit
            if(Game.rankGreater(winningCard, hand.getCardsWithSuit(lead).get(smallest))){
                while (i < hand.getNumberOfCardsWithSuit(lead)){
                    if(Game.rankGreater(hand.getCardsWithSuit(lead).get(smallest), hand.getCardsWithSuit(lead).get(i))){
                        smallest = i;
                    }
                    i += 1;
                }
            }
            return hand.getCardsWithSuit(lead).get(smallest);
        }
        // If no cards of leading suit
        else{
            // If current winning card is trump, Get smallest trump card that beats the trump
            if(winningCard.getSuit() == trump){
                if(hand.getNumberOfCardsWithSuit(trump) > 0){
                    while(i < hand.getNumberOfCardsWithSuit(trump)){
                        if(Game.rankGreater(hand.getCardsWithSuit(trump).get(i), winningCard) &&
                            Game.rankGreater(hand.getCardsWithSuit(trump).get(smallest),
                                    hand.getCardsWithSuit(trump).get(i))){
                            smallest = i;
                        }
                        i += 1;
                    }
                    // Else get smallest trump card
                    if(Game.rankGreater(winningCard, hand.getCardsWithSuit(trump).get(smallest))){
                        while (i < hand.getNumberOfCardsWithSuit(trump)){
                            if(Game.rankGreater(hand.getCardsWithSuit(trump).get(smallest), hand.getCardsWithSuit(trump).get(i))){
                                smallest = i;
                            }
                            i += 1;
                        }
                    }
                    return hand.getCardsWithSuit(trump).get(smallest);
                }
                // If no trump cards in hand, Find smallest card.
                else{
                    while(i < hand.getNumberOfCards()){
                        if(Game.rankGreater(hand.get(smallest), hand.get(i))){
                            smallest = i;
                        }
                        i += 1;
                    }
                }
            }
        }
        return hand.get(smallest);
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
