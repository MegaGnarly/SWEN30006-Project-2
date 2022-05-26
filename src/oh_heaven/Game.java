package oh_heaven;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class Game extends CardGame{
    public enum Suit
    {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank
    {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
    }

    final String[] trumpImage = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

    static public int seed;
    static Random random = new Random(seed);

    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from Hand
    public static Card randomCard(Hand hand){
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list){
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    private void dealingOut(int nbPlayers, int nbCardsPerPlayer) {
        Hand pack3 = deck.toHand(false);
        // pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j=0; j < nbPlayers; j++) {
                if (pack3.isEmpty()) return;
                Card dealt3 = randomCard(pack3);
                // System.out.println("Cards = " + dealt);
                dealt3.removeFromHand(false);
                players.get(j).getHand().insert(dealt3, false);
                // dealt.transfer(hands[j], true);
            }
        }
    }

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    private final String version = "1.0";
    public final int nbPlayers = 4;
    public int nbStartCards;
    public int nbRounds;
    public final int madeBidBonus = 10;
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private final Deck deck = new Deck(Game.Suit.values(), Game.Rank.values(), "cover");
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            // new Location(650, 575)
            new Location(575, 575)
    };
    private final Actor[] scoreActors = {null, null, null, null };
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private final int thinkingTime = 2000;
    private final Location hideLocation = new Location(-500, - 500);
    private final Location trumpsActorLocation = new Location(50, 50);
    private boolean enforceRules;

    public void setStatus(String string) { setStatusText(string); }

    private final ArrayList<PlayerT> players = new ArrayList<PlayerT>();

    Font bigFont = new Font("Serif", Font.BOLD, 36);

    private void initPlayers(Properties properties) {
        for (int i = 0; i < nbPlayers; i++) {
            String type = properties.getProperty("players."+i);
            if (type.equals("random")){
                players.add(new RandomPlayer(deck));
            }
            else if (type.equals("legal")){
                players.add(new LegalPlayer(deck));
            }
            else if (type.equals("smart")){
                players.add(new SmartPlayer(deck));
            }
            else if (type.equals("human")){
                players.add(new HumanPlayer(deck));
            }
            else{
                players.add(new RandomPlayer(deck));
            }
        }
//        dealingOut(hands, nbPlayers, nbStartCards);
    }

    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + players.get(i).getScore() + "]" + players.get(i).getTricks() + "/" + players.get(i).getBid();
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        String text = "[" + players.get(player).getScore() + "]" + players.get(player).getTricks() + "/" + players.get(player).getBid();
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void updateScores() {
        for (PlayerT player: players){
            player.setScore(player.getScore()+player.getTricks());
            if (player.getTricks() == player.getBid()){
                player.setScore(player.getScore()+madeBidBonus);
            }
        }
    }

    private void initBids(Game.Suit trumps, int nextPlayer) {
        int total = 0;
        for (PlayerT player: players){
            player.setBid(nbStartCards / 4 + random.nextInt(2));
            total += player.getBid();
        }
        if (total == nbStartCards) {
            PlayerT last = players.get(players.size()-1);
            if (last.getBid() == 0){
                last.setBid(1);
            }
            else{
                last.setBid(last.getBid()+(random.nextBoolean() ? -1 : 1));
            }
        }
        // for (int i = 0; i < nbPlayers; i++) {
        // 	 bids[i] = nbStartCards / 4 + 1;
        //  }
    }

    private Card selected;

    private void initRound() {
        dealingOut(nbPlayers, nbStartCards);
        for (int i = 0; i < nbPlayers; i++) {
            players.get(i).getHand().sort(Hand.SortType.SUITPRIORITY, true);
            players.get(i).setTricks(0);
        }
        // graphics
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
//            hands[i].setView(this, layouts[i]);
            players.get(i).getHand().setView(this, layouts[i]);
//            hands[i].setTargetArea(new TargetArea(trickLocation));
            players.get(i).getHand().setTargetArea(new TargetArea(trickLocation));
//            hands[i].draw();
            players.get(i).getHand().draw();
        }
//	    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);			// You do not need to use or change this code.
        // End graphics
    }

    private void playRound() {
        // Select and display trump suit
        final Game.Suit trumps = randomEnum(Game.Suit.class);
        final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
        addActor(trumpsActor, trumpsActorLocation);
        // End trump suit
        Hand trick;
        int winner;
        Card winningCard;
        Game.Suit lead = null;
        int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
        initBids(trumps, nextPlayer);
        // initScore();
        for (int i = 0; i < nbPlayers; i++) updateScore(i);
        for (int i = 0; i < nbStartCards; i++) {
            lead = null;
            trick = new Hand(deck);
            selected = null;
            if (players.get(nextPlayer) instanceof HumanPlayer){
                setStatus("Player "+nextPlayer+" double-click on card to lead");
                selected = players.get(nextPlayer).PlayCard(deck, lead, trumps);
            }
            else {
                setStatusText("Player " + nextPlayer + " thinking...");
                delay(thinkingTime);
                selected = players.get(nextPlayer).PlayCard(deck, lead, trumps);
            }

            System.out.println(selected);
            // Lead with selected card
            trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
            trick.draw();
            selected.setVerso(false);
            // No restrictions on the card being lead
            System.out.println(lead);
            lead = (Game.Suit) selected.getSuit();
            System.out.println(lead);
            selected.transfer(trick, true); // transfer to trick (includes graphic effect)
            winner = nextPlayer;
            winningCard = selected;
            // End Lead
            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
                selected = null;
                if (players.get(nextPlayer) instanceof HumanPlayer){
                    setStatus("Player "+nextPlayer+" double-click on card to lead");
                    selected = players.get(nextPlayer).PlayCard(deck, lead, trumps);
                }
                else {
                    setStatusText("Player " + nextPlayer + " thinking...");
                    delay(thinkingTime);
                    selected = players.get(nextPlayer).PlayCard(deck, lead, trumps);
                }

                // Follow with selected card
                trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
                trick.draw();
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible
                if (selected.getSuit() != lead && players.get(nextPlayer).getHand().getNumberOfCardsWithSuit(lead) > 0) {
                    // Rule violation
                    String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
                    System.out.println(violation);
                    if (enforceRules)
                        try {
                            throw(new BrokeRuleException(violation));
                        } catch (BrokeRuleException e) {
                            e.printStackTrace();
                            System.out.println("A cheating player spoiled the game!");
                            System.exit(0);
                        }
                }
                // End Check
                selected.transfer(trick, true); // transfer to trick (includes graphic effect)
                System.out.println("winning: " + winningCard);
                System.out.println(" played: " + selected);
                // System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
                // System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = nextPlayer;
                    winningCard = selected;
                }
                // End Follow
            }
            delay(600);
            trick.setView(this, new RowLayout(hideLocation, 0));
            trick.draw();
            nextPlayer = winner;
            setStatusText("Player " + nextPlayer + " wins trick.");
            players.get(nextPlayer).setTricks(players.get(nextPlayer).getTricks()+1);
            updateScore(nextPlayer);
        }
        removeActor(trumpsActor);
    }

    public Game(Properties properties)
    {
        super(700, 700, 30);
        setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        seed = Integer.parseInt(properties.getProperty("seed"));
        random = new Random(seed);
        this.nbStartCards = Integer.parseInt(properties.getProperty("nbStartCards"));
        this.nbRounds = Integer.parseInt(properties.getProperty("rounds"));
        this.enforceRules = Boolean.parseBoolean(properties.getProperty("enforceRules"));
        initPlayers(properties);
        initScore();
        for (int i=0; i <nbRounds; i++) {
            initRound();
            playRound();
            updateScores();
        }
        for (int i=0; i <nbPlayers; i++) updateScore(i);
        int maxScore = 0;
        for (int i = 0; i <nbPlayers; i++) if (players.get(i).getScore() > maxScore) maxScore = players.get(i).getScore();
        Set <Integer> winners = new HashSet<Integer>();
        for (int i = 0; i <nbPlayers; i++) if (players.get(i).getScore() == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        }
        else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
    }
}
