package pl.edu.agh.kis.pz1.game;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.cards.Deck;
import pl.edu.agh.kis.pz1.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final int id;
    private final int ante;
    private int allFunds;
    private int bet;
    private int currentPlayer;
    private int currentRound;
    private int currentDrawPlayer;
    private List<Player> players;
    private Deck deck;


    public Game(int idGry, int x) {
        this.id = idGry;
        this.ante = x;
        this.allFunds = 0;
        this.bet = x;
        this.currentPlayer = 0;
        this.currentRound = 1;
        currentDrawPlayer = 0;
        this.players = new ArrayList<>();
        this.deck = new Deck();
    }

    public int getId() {
        return id;
    }

    public int getAnte() { return ante; }

    public int getAllFunds() { return allFunds; }

    public int getBet() { return bet; }

    public int getCurrentPlayer() { return currentPlayer; }

    public int getCurrentRound() { return currentRound; }

    public int getCurrentDrawPlayer() { return currentDrawPlayer; }

    public List<Player> getPlayers() { return players; }

    public Player getPlayer(int id) {
        for (Player p: getPlayers()) {
            if (p.getId() == id ) { return p; }
        }
        return null;
    }

    public Deck getDeck() { return deck; }

    public void setBet(int x) { bet = x; }

    public void addFunds(int x) { allFunds += x; }

    public boolean addPlayer(int playerId) {
        if (players.size() == 4) { return false; }
        else {
            players.add(new Player(playerId));
            addFunds(ante);
            return true;
        }
    }

    public void deal() {
        deck.shuffle();
        for(int i = 0; i < 5; i++) {
            for(Player p: players) {
                p.getHand().addCard(deck.topCard());
            }
        }
    }

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

    public boolean nextDrawPlayer() {
        ++currentDrawPlayer;
        return currentDrawPlayer < players.size();
    }

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

    public boolean isInGame(int id) {
        for (Player player : getPlayers()) {
            if (player.getId()==id) return true;
        }
        return false;
    }

    public void draw(List<Card> cardsToDraw, Player player) {
        int numbersOfCards = cardsToDraw.size();
        for (Card c : cardsToDraw) {
            player.getHand().deleteCard(c);
            deck.addCard(c);
        }
        deck.shuffle();
        for (int i = 0; i < numbersOfCards; i++) {
            player.getHand().addCard(deck.topCard());
        }
    }

    public boolean isFinish() {
        return currentPlayer == 0 && currentRound == 3;
    }

    public List<Player> result() {
        List<Player> result = new ArrayList<>(players);
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
