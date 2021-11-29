package pl.edu.agh.kis.pz1.game;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.cards.Deck;
import pl.edu.agh.kis.pz1.player.Player;

import java.util.ArrayList;
import java.util.List;

/** Game is a class of representation 5 Cards Daw Poker.
 *  A game object encapsulates all the most important features of game like:
 *  <ul>
 *      <li>Ante</li>
 *      <li>All funds</li>
 *      <li>Current max bet</li>
 *      <li>Currently playing player</li>
 *      <li>Current round of the game</li>
 *      <li>Currently drawing player if it is a draw time.</li>
 *      <li>Players participating in the game</li>
 *      <li>Deck of 52 cards</li>
 *  </ul>
 *  Adds players to game and updates information about play.
 *
 *  @author Michał Strzeżoń
 */
public class Game {
    /**
     * Id of the game.
     */
    private final int id;
    /**
     * Ante in the game.
     */
    private final int ante;
    /**
     * All funds in the game.
     */
    private int allFunds;
    /**
     * Current max bet.
     */
    private int bet;
    /**
     * Index of currently playing player.
     */
    private int currentPlayer;
    /**
     * Current round.
     */
    private int currentRound;
    /**
     * Index of currently drawing player.
     */
    private int currentDrawPlayer;
    /**
     * Players participating in the game.
     */
    private List<Player> players;
    /**
     * Deck of 52 cards.
     */
    private Deck deck;

    /**
     * Creates game.
     * @param gameId unique game id.
     * @param x fixed ante.
     */
    public Game(int gameId, int x) {
        this.id = gameId;
        this.ante = x;
        this.allFunds = 0;
        this.bet = x;
        this.currentPlayer = 0;
        this.currentRound = 1;
        currentDrawPlayer = 0;
        this.players = new ArrayList<>();
        this.deck = new Deck();
    }

    /**
     * Gets gameId
     * @return game ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets ante.
     * @return ante in this game.
     */
    public int getAnte() { return ante; }

    /**
     * Gets funds in the game.
     * @return all funds on the table.
     */
    public int getAllFunds() { return allFunds; }

    /**
     * Gets bet.
     * @return currently the largest bet.
     */
    public int getBet() { return bet; }

    /**
     * Gets current player.
     * @return index of currently playing player.
     */
    public int getCurrentPlayer() { return currentPlayer; }

    /**
     * Gets current round.
     * @return number of the round.
     */
    public int getCurrentRound() { return currentRound; }

    /**
     * Gets currently drawing player.
     * @return currently drawing player.
     */
    public int getCurrentDrawPlayer() { return currentDrawPlayer; }

    /**
     * Gets all players.
     * @return <code>List&lt;Player&gt;</code> includes all player in this game.
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Gets player of <code>player.id=id</code>
     * @param id player id
     * @return   <code>Player</code> if a player with the given id exists;
     *           <code>null</code> otherwise.
     */
    public Player getPlayer(int id) {
        for (Player p: getPlayers()) {
            if (p.getId() == id ) { return p; }
        }
        return null;
    }

    /**
     * Gets deck.
     * @return <code>Deck</code> - cards used by the game.
     */
    public Deck getDeck() { return deck; }

    /**
     * Sets bet.
     * @param x new bet.
     */
    public void setBet(int x) { bet = x; }

    /**
     * Adds funds to money on the table.
     * @param x money to add.
     */
    public void addFunds(int x) { allFunds += x; }

    /**
     * Adds player to the game if number of player in this game is smaller than 4.
     * @param playerId id of the player to be added to this game.
     * @return         <code>true</code> if managed to add;
     *                 <code>false</code> otherwise.
     */
    public boolean addPlayer(int playerId) {
        if (players.size() == 4) { return false; }
        else {
            players.add(new Player(playerId));
            addFunds(ante);
            return true;
        }
    }

    /**
     * Deals the cards to all the players in this game.
     */
    public void deal() {
        deck.shuffle();
        for(int i = 0; i < 5; i++) {
            for(Player p: players) {
                p.getHand().addCard(deck.topCard());
            }
        }
    }

    /**
     * Goes to the next player in the round.
     * @return <code>Player</code> who is to play now.
     */
    public Player nextPlayer() {
        Player nextPlayer;
        while(true) {
            if (currentPlayer == players.size()-1) {
                currentPlayer = -1;
                currentRound += 1;
            }
            nextPlayer = players.get(++currentPlayer);
            if (nextPlayer.getIsInPlay()) {
                return nextPlayer;
            }
        }
    }

    /**
     * Goes to the next player in the draw.
     * @return <code>Player</code> who is to draw now.
     */
    public boolean nextDrawPlayer() {
        ++currentDrawPlayer;
        return currentDrawPlayer < players.size();
    }

    /**
     * Makes a move of a player such as:
     * <ul>
     *     <li>call</li>
     *     <li>raise</li>
     *     <li>fold</li>
     * </ul>
     * @param type    Type of move: 1->call, 2->raise, 3->fold
     * @param wage    new wage - is needed to raise. In case of call or raise can be anything.
     * @param player  The player to make the move.
     * @return        <code>true</code> if the move was successful;
     *                <code>false</code> otherwise.
     */
    public boolean makeAMove(int type, int wage, Player player) {
        switch (type) {
            case(1) -> {
                if (player.call(bet)){
                    addFunds(bet);
                    nextPlayer();
                    return true;
                } else return false;

            }
            case(2) -> {
                if (wage <= bet) return false;
                if (player.raise(wage)){
                    nextPlayer();
                    bet = wage;
                    addFunds(bet);
                    return true;
                } else return false;
            }
            case(3) -> {
                player.fold();
                nextPlayer();
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    /**
     * Informs if the player is still playing the game or she/he folded.
     * @param id  id of player who is to be checked.
     * @return    <code>true</code> if the player is still playing.
     *            <code>false</code> otherwise.
     */
    public boolean isInGame(int id) {
        for (Player player : getPlayers()) {
            if (player.getId()==id) return true;
        }
        return false;
    }

    /**
     * Makes the move of the player such as draw.
     * @param cardsToDraw cards to be replaced.
     * @param player      player who makes the move.
     */
    public void draw(List<Card> cardsToDraw, Player player) {
        int numbersOfCards = cardsToDraw.size();
        for (Card c : cardsToDraw) {
            player.getHand().removeCard(c);
            deck.addCard(c);
        }
        deck.shuffle();
        for (int i = 0; i < numbersOfCards; i++) {
            player.getHand().addCard(deck.topCard());
        }
    }

    /**
     * Inform if the game is over.
     * @return <code>true</code> if game is over;
     *         <code>false</code> otherwise.
     */
    public boolean isFinish() {
        return currentPlayer == 0 && currentRound == 3;
    }

    /**
     * Informs about the results of the game.
     * @return  <code>List&lt;Player&gt;</code> from the best to the worst.
     */
    public List<Player> result() {
        List<Player> result = new ArrayList<>(players);
        result.removeIf(p -> !p.getIsInPlay());
        result.sort((h1, h2) -> {
            if (h1.getHand().getValue().ordinal()==h2.getHand().getValue().ordinal()) {
                return h2.getHand().compareTo(h1.getHand().getCards());
            } else {
                return Integer.compare(h2.getHand().getValue().ordinal(), h1.getHand().getValue().ordinal());
            }
        });
        return result;
    }

}
