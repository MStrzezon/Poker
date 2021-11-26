package pl.edu.agh.kis.pz1.player;

/**
 * Player is the class, which represents poker player.
 * A player object encapsulates all the most important information of player like:
 *  <ul>
 *      <li>id</li>
 *      <li>isInPlay</li>
 *      <li>funds</li>
 *      <li>hand</li>
 *  </ul>
 *
 * @author Michał Strzeżoń
 */
public class Player {
    /**
     * player id.
     */
    private final int id;
    /**
     * information if player is still player.
     */
    private boolean isInPlay;
    /**
     * player funds.
     */
    private int funds;
    /**
     * player hand with cards.
     */
    private final Hand hand;

    /**
     * Creates player with id and 50 funds.
     * @param id player id.
     */
    public Player(int id) {
        this.id = id;
        funds=50;
        this.isInPlay = true;
        hand = new Hand();
    }

    /**
     * Gets player id.
     * @return player id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets player money.
     * @return player funds.
     */
    public int getFunds() { return funds; }

    /**
     * Gets isInPlay
     * @return   <code>true</code> if player is in the game.
     *           <code>false</code> otherwise.
     */
    public boolean getIsInPlay() { return isInPlay;}

    /**
     * Sets isInPlay
     * @param state <code>true</code> if player is in the game.
     *              <code>false</code> otherwise.
     */
    public void setIsInPlay(boolean state) { isInPlay = state; }

    /**
     * Gets player hand.
     * @return player hand.
     */
    public Hand getHand() { return hand; }

    /**
     * Calls in the game.
     * @param wage wage that the player wants to bet.
     * @return     <code>true</code> if call succeeds.
     *             <code>false</code> otherwise.
     */
    public boolean call(int wage) {
        if (funds - wage > 0) {
            funds -= wage;
            return true;
        }
        else return false;
    }

    /**
     * Raises in the game.
     * @param newWage wage that the player wants to bet.
     * @return        <code>true</code> if call succeeds.
     *                <code>false</code> otherwise.
     */
    public boolean raise(int newWage) {
        if (funds - newWage > 0) {
            funds -= newWage;
            return true;
        }
        else return false;
    }

    /**
     * Folds in the game.
     */
    public void fold() {
        isInPlay = false;
    }



}
