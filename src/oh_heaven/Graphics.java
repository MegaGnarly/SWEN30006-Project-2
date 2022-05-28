package oh_heaven;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.awt.*;
import java.util.ArrayList;

public class Graphics extends CardGame{
    private final String version = "1.0";
    final String[] trumpImage = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};
    Font bigFont = new Font("Serif", Font.BOLD, 36);
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
    private final int handWidth = 400;
    private final Actor[] scoreActors = {null, null, null, null };
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);



    private final Location hideLocation = new Location(-500, - 500);
    private final Location trumpsActorLocation = new Location(50, 50);

    private Actor trumpsActor;

    public Graphics() {
        super(700, 700, 30);
        setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
    }


    public void initTrump(Game.Suit trumps){
        trumpsActor = new Actor("sprites/"+this.trumpImage[trumps.ordinal()]);
        addActor(trumpsActor, trumpsActorLocation);
    }


    public void resetGame(String winText){
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
    }
    public void addScore(String text, int actor){
        scoreActors[actor] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[actor], scoreLocations[actor]);
    }
    public void changeScore(String text, int actor){
        removeActor(scoreActors[actor]);
        scoreActors[actor] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[actor], scoreLocations[actor]);
    }

    public void startRound(ArrayList<PlayerT> players){
        RowLayout[] layouts = new RowLayout[players.size()];
        for (int i = 0; i < players.size(); i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
            players.get(i).getHand().setView(this, layouts[i]);
            players.get(i).getHand().setTargetArea(new TargetArea(trickLocation));
            players.get(i).getHand().draw();
        }
    }

    public Location getTrickLocation(){
        return trickLocation;
    }

    public Location getHideLocation() {
        return hideLocation;
    }

    public void removeTrumpsActor(){
        removeActor(trumpsActor);
    }
}
