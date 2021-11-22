package pl.edu.agh.kis.pz1.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.exceptions.NoPlacesException;
import pl.edu.agh.kis.pz1.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(1, 5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getId() {
        assertEquals(1, game.getId());
    }

    @Test
    public void getAnte() {
        assertEquals(5, game.getAnte());
    }

    @Test
    public void getBet() {
        assertEquals(5, game.getBet());
    }

    @Test
    public void getAllFunds() {
        game.addPlayer(0);
        game.addPlayer(1);
        assertEquals(10, game.getAllFunds());
    }

    @Test
    public void getCurrentPlayer() {
        game.addPlayer(0);
        game.addPlayer(1);
        assertEquals(0, game.getCurrentPlayer());
        game.nextPlayer();
        assertEquals(1, game.getCurrentPlayer());
        game.nextPlayer();
        assertEquals(0, game.getCurrentPlayer());
    }

    @Test
    public void getCurrentRound() {
        game.addPlayer(0);
        assertEquals(1, game.getCurrentRound());
        game.nextPlayer();
        assertEquals(2, game.getCurrentRound());
    }

    @Test
    public void getPlayers() {
        game.addPlayer(0);
        game.addPlayer(1);
        assertNotNull(game.getPlayers());
        assertEquals(0, game.getPlayers().get(0).getId());
    }

    @Test
    public void getDeck() {
        assertNotNull(game.getDeck());
    }

    @Test
    public void addFunds() {
        game.addPlayer(0);
        game.addPlayer(1);
        game.addFunds(10);
        assertEquals(20, game.getAllFunds());
    }

    @Test
    public void setBet() {
        game.setBet(11);
        assertEquals(11, game.getBet());
    }

    @Test
    public void addPlayer() throws NoPlacesException {
        assertTrue(game.addPlayer(0));
        assertEquals(5, game.getAllFunds());
        assertTrue(game.addPlayer(1));
        assertEquals(10, game.getAllFunds());
        assertTrue(game.addPlayer(2));
        assertEquals(15, game.getAllFunds());
        assertTrue(game.addPlayer(3));
        assertEquals(20, game.getAllFunds());
        assertFalse(game.addPlayer(4));
    }

    @Test
    public void deal2Players() {
        for(int i = 0; i <2; i++) {
            game.addPlayer(0);
        }
        game.deal();
        assertEquals(5, game.getPlayers().get(0).getHand().getCards().size());
        assertEquals(5, game.getPlayers().get(1).getHand().getCards().size());
        assertTrue(Collections.disjoint(game.getPlayers().get(0).getHand().getCards(), game.getPlayers().get(1).getHand().getCards()));
    }

    @Test
    public void deal3Players() {
        for(int i = 0; i <3; i++) {
            game.addPlayer(0);
        }
        game.deal();
        assertEquals(5, game.getPlayers().get(0).getHand().getCards().size());
        assertEquals(5, game.getPlayers().get(1).getHand().getCards().size());
        assertEquals(5, game.getPlayers().get(2).getHand().getCards().size());
        assertTrue(Collections.disjoint(game.getPlayers().get(0).getHand().getCards(), game.getPlayers().get(1).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(0).getHand().getCards(), game.getPlayers().get(2).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(2).getHand().getCards(), game.getPlayers().get(1).getHand().getCards()));

    }

    @Test
    public void deal4Players() {
        for(int i = 0; i <4; i++) {
            game.addPlayer(0);
        }
        game.deal();
        assertEquals(5, game.getPlayers().get(0).getHand().getCards().size());
        assertEquals(5, game.getPlayers().get(1).getHand().getCards().size());
        assertEquals(5, game.getPlayers().get(2).getHand().getCards().size());
        assertEquals(5, game.getPlayers().get(3).getHand().getCards().size());
        assertTrue(Collections.disjoint(game.getPlayers().get(0).getHand().getCards(), game.getPlayers().get(1).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(0).getHand().getCards(), game.getPlayers().get(2).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(1).getHand().getCards(), game.getPlayers().get(2).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(0).getHand().getCards(), game.getPlayers().get(3).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(1).getHand().getCards(), game.getPlayers().get(3).getHand().getCards()));
        assertTrue(Collections.disjoint(game.getPlayers().get(3).getHand().getCards(), game.getPlayers().get(2).getHand().getCards()));
    }

    @Test
    public void nextPlayer() {
        for (int i = 0; i < 4; i++) game.addPlayer(0);
        assertEquals(game.getPlayers().get(1), game.nextPlayer());
        assertEquals(game.getPlayers().get(2), game.nextPlayer());
        assertEquals(game.getPlayers().get(3), game.nextPlayer());
        assertEquals(game.getPlayers().get(0), game.nextPlayer());
        assertEquals(2, game.getCurrentRound());
        game.getPlayers().get(1).fold();
        assertEquals(game.getPlayers().get(2), game.nextPlayer());
    }

    @Test
    public void draw() {
        game.addPlayer(0);
        game.addPlayer(1);
        game.addPlayer(2);
        game.deal();
        List<Card> c1 = new ArrayList<>(game.getPlayers().get(0).getHand().getCards());
        List<Card> c2 = new ArrayList<>();
        c2.add(c1.get(1));
        c2.add(c1.get(3));
        game.draw(c2, game.getPlayers().get(0));
        assertEquals(5, game.getPlayers().get(0).getHand().getCards().size());
        assertNotEquals(c1, game.getPlayers().get(0).getHand().getCards());
    }

    @Test
    public void isFinish() {
        game.addPlayer(0);
        game.addPlayer(1);
        game.addPlayer(2);
        game.nextPlayer();
        game.nextPlayer();
        assertFalse(game.isFinish());
        game.nextPlayer();
        assertFalse(game.isFinish());
        game.nextPlayer();
        game.nextPlayer();
        game.nextPlayer();
        assertTrue(game.isFinish());
    }

    @Test
    public void winner() {
        Player winner = new Player(1);
        Player looser = new Player(2);
        winner.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        winner.getHand().addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        winner.getHand().addCard(new Card(Card.Rank.QUEEN, Card.Suit.SPADES));
        winner.getHand().addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        winner.getHand().addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        looser.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        looser.getHand().addCard(new Card(Card.Rank.KING, Card.Suit.DIAMONDS));
        looser.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        looser.getHand().addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        looser.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        game.getPlayers().add(winner);
        game.getPlayers().add(looser);
        assertEquals(winner, game.winner());
    }


    @Test
    public void makeAMove() {
        game.addPlayer(0);
        game.addPlayer(1);
        game.addPlayer(2);
        assertTrue(game.makeAMove(1, 1, game.getPlayers().get(0)));
        assertEquals(1, game.getCurrentPlayer());
        assertEquals(1, game.getCurrentRound());
        assertTrue(game.makeAMove(1, 1, game.getPlayers().get(1)));
        assertEquals(2, game.getCurrentPlayer());
        assertEquals(1, game.getCurrentRound());
        assertFalse(game.makeAMove(2, 1, game.getPlayers().get(2)));
        assertEquals(2, game.getCurrentPlayer());
        assertEquals(1, game.getCurrentRound());
        assertTrue(game.makeAMove(1, 1, game.getPlayers().get(2)));
        assertEquals(0, game.getCurrentPlayer());
        assertEquals(2, game.getCurrentRound());
    }

    @Test
    public void isInGame() {
        game.addPlayer(0);
        assertFalse(game.isInGame(1));
        game.addPlayer(1);
        assertTrue(game.isInGame(1));
    }
}