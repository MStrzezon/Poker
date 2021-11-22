package pl.edu.agh.kis.pz1.game;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.cards.Deck;
import pl.edu.agh.kis.pz1.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final int id;
    private final int ante;
    private int allFunds;
    private int bet;
    private int currentPlayer;
    private int currentRound;
    private List<Player> players;
    private Deck deck;


    public Game(int idGry, int x) {
        this.id = idGry;
        this.ante = x;
        this.allFunds = 0;
        this.bet = x;
        this.currentPlayer = 0;
        this.currentRound = 1;
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

    public void makeAMove(int type, int newWage, Player player) {
        switch (type) {
            case(1) -> {
                player.call();
                addFunds(bet);
            }
            case(2) -> {
                player.raise(newWage);
                bet = newWage;
                addFunds(bet);
            }
            case(3) -> {
                player.fold();
            }
        }
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
        return currentPlayer == 0 && currentRound == 4;
    }

    public Player winner() {
        Player winner;
        List<Player> idRename = new ArrayList<>();
        int value = -1;
        for (Player p : players) {
            if (p.getHand().getValue().ordinal() > value) {
                idRename.clear();
                value = p.getHand().getValue().ordinal();
                idRename.add(p);
            }
            else if (p.getHand().getValue().ordinal() == value) {
                idRename.add(p);
            }
        }
        return idRename.get(0);
    }

}
