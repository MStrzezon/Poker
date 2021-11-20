package pl.edu.agh.kis.pz1.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {
    private Card c1;
    private Card c2;

    @Before
    public void setUp() throws Exception {
        c1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
        c2 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void rank() {
        assertEquals(Card.Rank.ACE, c1.rank());
    }

    @Test
    public void suit() {
        assertEquals(Card.Suit.CLUBS, c1.suit());
    }

    @Test
    public void testToString() {
        assertEquals("ACE of CLUBS", c1.toString());
    }

    @Test
    public void testEquals() {
        assertEquals(c1, c1);
    }

    /**
     * Hello
     */
    @Test
    public void testHashCode() {
        assertEquals(c1.hashCode(),c2.hashCode());
    }
}