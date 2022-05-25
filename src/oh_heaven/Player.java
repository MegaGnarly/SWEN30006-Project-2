package oh_heaven;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

public class Player extends Actor{
    private int score = 0;
    private int tricks = 0;
    private int bid = 0;
    private Hand hand;
    private Type type;

    public Player(String type, Deck deck){
        this.hand = new Hand(deck);

        if (type.equals("random")){
            this.type = new RandomType();
        }
        else if (type.equals("legal")){
            this.type = new LegalType();
        }
        else if (type.equals("smart")){
            this.type = new SmartType();
        }
        else if (type.equals("human")){
            this.type = new HumanType(this.hand);
        }
        else{
            this.type = new RandomType();
        }
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

    public Type getType() {
        return type;
    }
}
