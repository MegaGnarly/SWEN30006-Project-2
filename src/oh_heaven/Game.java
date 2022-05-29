package oh_heaven;

import ch.aplu.jcardgame.*;
import java.util.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class Game{
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

    // deals out cards from beck into player hands
    private void dealingOut(int nbPlayers, int nbCardsPerPlayer) {
        Hand pack = deck.toHand(false);
        // pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j=0; j < nbPlayers; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack);
                // System.out.println("Cards = " + dealt);
                dealt.removeFromHand(false);
                players.get(j).getHand().insert(dealt, false);
                // dealt.transfer(hands[j], true);
            }
        }
    }

    public static boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    static final String version = "1.0";
    public final int nbPlayers = 4;
    public int nbStartCards;
    public int nbRounds;
    public final int madeBidBonus = 10;
    private final int trickWidth = 40;
    private final Deck deck = new Deck(Game.Suit.values(), Game.Rank.values(), "cover");
    private Properties properties;
    private final int thinkingTime = 2000;
    private boolean enforceRules;
    Graphics graphics = new Graphics();


    private final ArrayList<PlayerT> players = new ArrayList<PlayerT>();

    // initiallises players using the player factory
    private void initPlayers() {
        for (int i = 0; i < nbPlayers; i++) {
            String type = properties.getProperty("players."+i);
            players.add(PlayerFactory.getPlayer(type, deck));
        }
    }

    // initiallises the score Actor via Graphics
    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + players.get(i).getScore() + "]" + players.get(i).getTricks() + "/" + players.get(i).getBid();
            graphics.addScore(text, i);
        }
    }

    // updates the score Actor via Graphics
    private void updateScore(int player) {
        String text = "[" + players.get(player).getScore() + "]" + players.get(player).getTricks() + "/" +
                players.get(player).getBid();
        graphics.changeScore(text, player);
    }

    // updates the score in individual players
    private void updateScores() {
        for (PlayerT player: players){
            player.setScore(player.getScore()+player.getTricks());
            if (player.getTricks() == player.getBid()){
                player.setScore(player.getScore()+madeBidBonus);
            }
        }
    }

    // initiallises each players bids through random, can be changed to implement properties
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

    // deals out the cards and sorts them
    private void initRound() {
        dealingOut(nbPlayers, nbStartCards);
        for (int i = 0; i < nbPlayers; i++) {
            players.get(i).getHand().sort(Hand.SortType.SUITPRIORITY, true);
            players.get(i).setTricks(0);
        }
        // graphics
        graphics.startRound(players);

//	    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);			// You do not need to use or change this code.
        // End graphics
    }

    private void playRound() {
        // Select and display trump suit
        final Game.Suit trumps = randomEnum(Game.Suit.class);
        graphics.initTrump(trumps);
        // End trump suit
        Hand trick;
        int winner;
        Card winningCard;
        Game.Suit lead;
        winningCard = null;
        int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
        initBids(trumps, nextPlayer); // initiallises bids for each player
        // initScore();
        for (int i = 0; i < nbPlayers; i++) updateScore(i);
        for (int i = 0; i < nbStartCards; i++) {
            lead = null;
            trick = new Hand(deck);
            selected = null;
            // gets selected card based on Type of player, with only difference being status text
            if (players.get(nextPlayer) instanceof HumanPlayer){
                graphics.setStatusText("Player "+nextPlayer+" double-click on card to lead");
                selected = players.get(nextPlayer).PlayCard(deck, lead, trumps, winningCard);
            }
            else {
                graphics.setStatusText("Player " + nextPlayer + " thinking...");
                graphics.delay(thinkingTime);
                selected = players.get(nextPlayer).PlayCard(deck, lead, trumps, winningCard);
            }

            // Lead with selected card
            trick.setView(graphics, new RowLayout(graphics.getTrickLocation(), (trick.getNumberOfCards()+2)*trickWidth));
            trick.draw();
            selected.setVerso(false);
            // No restrictions on the card being lead
            lead = (Game.Suit) selected.getSuit();
            selected.transfer(trick, true); // transfer to trick (includes graphic effect)
            winner = nextPlayer;
            winningCard = selected;
            // End Lead
            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
                selected = null;
                // gets selected card based on Type of player, with only difference being status text
                if (players.get(nextPlayer) instanceof HumanPlayer){
                    graphics.setStatusText("Player "+nextPlayer+" double-click on card to lead");
                    selected = players.get(nextPlayer).PlayCard(deck, lead, trumps, winningCard);
                }
                else {
                    graphics.setStatusText("Player " + nextPlayer + " thinking...");
                    graphics.delay(thinkingTime);
                    selected = players.get(nextPlayer).PlayCard(deck, lead, trumps, winningCard);
                }

                // Follow with selected card
                trick.setView(graphics, new RowLayout(graphics.getTrickLocation(), (trick.getNumberOfCards()+2)*trickWidth));
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
            graphics.delay(600);
            trick.setView(graphics, new RowLayout(graphics.getHideLocation(), 0));
            trick.draw();
            nextPlayer = winner;
            graphics.setStatusText("Player " + nextPlayer + " wins trick.");
            players.get(nextPlayer).setTricks(players.get(nextPlayer).getTricks()+1);
            updateScore(nextPlayer);
        }
        graphics.removeTrumpsActor();
    }

    public Game(Properties properties)
    {
        seed = Integer.parseInt(properties.getProperty("seed"));
        random = new Random(seed);
        // sets attributes based on properties
        this.properties = properties;
        this.nbStartCards = Integer.parseInt(properties.getProperty("nbStartCards"));
        this.nbRounds = Integer.parseInt(properties.getProperty("rounds"));
        this.enforceRules = Boolean.parseBoolean(properties.getProperty("enforceRules"));
        initPlayers();
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
        graphics.resetGame(winText);
    }
}
