package pl.edu.agh.kis.pz1.cards;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {
    private Deck d1;
    List<Card> cards = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        d1 = new Deck();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    @Test
    public void getCards() {
        assertNotNull(d1.getCards());
    }

    @Test
    public void fabryki() {
        assertEquals(cards, d1.getCards());
        assertEquals(52, d1.getCards().size());
    }

    @Test
    public void shuffle() {
        d1.shuffle();
        assertNotEquals(cards, d1.getCards());
    }

    @Test
    public void topCard() {
        assertEquals("DEUCE of CLUBS" ,d1.topCard().toString());
    }

    @Test
    public void addCard() {
        d1.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        assertEquals(53, d1.getCards().size());
    }
}