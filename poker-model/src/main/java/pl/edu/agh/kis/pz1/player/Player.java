package pl.edu.agh.kis.pz1.player;

public class Player {
    private final int id;
    private boolean isInPlay;
    private Hand hand;

    public Player(int id) {
        this.id = id;
        this.isInPlay = true;
        hand = new Hand();
    }

    public int getId() {
        return id;
    }

    public boolean getIsInPlay() { return isInPlay;}

    public void setIsInPlay(boolean state) { isInPlay = state; }

    public Hand getHand() { return hand; }

    /** it will be done */
    public void call() {
        //do nothing
    }

    /** it will be done */
    public void raise() {
        //do nothing
    }

    public void fold() {
        isInPlay = false;
    }



}
