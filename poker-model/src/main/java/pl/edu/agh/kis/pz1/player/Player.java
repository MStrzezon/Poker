package pl.edu.agh.kis.pz1.player;

public class Player {
    private final int id;
    private boolean isInPlay;
    private int funds;
    private Hand hand;

    public Player(int id) {
        this.id = id;
        funds=50;
        this.isInPlay = true;
        hand = new Hand();
    }

    public int getId() {
        return id;
    }

    public int getFunds() { return funds; }

    public boolean getIsInPlay() { return isInPlay;}

    public void setIsInPlay(boolean state) { isInPlay = state; }

    public Hand getHand() { return hand; }

    /** it will be done */
    public boolean call(int wage) {
        if (funds - wage > 0) {
            funds -= wage;
            return true;
        }
        else return false;
    }

    /** it will be done */
    public boolean raise(int newWage) {
        if (funds - newWage > 0) {
            funds -= newWage;
            return true;
        }
        else return false;
    }

    public void fold() {
        isInPlay = false;
    }



}
