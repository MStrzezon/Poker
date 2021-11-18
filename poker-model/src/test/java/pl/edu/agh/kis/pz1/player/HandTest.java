package pl.edu.agh.kis.pz1.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.pz1.cards.Card;

import static org.junit.Assert.*;

public class HandTest {
    private Hand hand;

    @Before
    public void setUp() throws Exception {
        hand = new Hand();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addCard() {
        Card c = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
        hand.addCard(c);
        assertEquals(c.toString(), hand.getCards().get(0).toString());
    }

    @Test
    public void getCards() {
//        Card c = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
//        hand.addCard(c);
        assertNotNull(hand.getCards());
    }

    @Test
    public void deleteCard() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.DEUCE, Card.Suit.CLUBS));
        hand.deleteCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        assertEquals(1, hand.getCards().size());
    }

    @Test
    public void isPair() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.THREE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        Card c = new Card(Card.Rank.QUEEN, Card.Suit.DIAMONDS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isOnePair());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        hand.sortCards();
        assertTrue(hand.isOnePair());
    }

    @Test
    public void isTwoPair() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.THREE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isTwoPairs());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        hand.sortCards();
        assertTrue(hand.isTwoPairs());
    }

    @Test
    public void isThreeOfAKind() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.THREE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isThreeOfAKind());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        hand.sortCards();
        assertTrue(hand.isThreeOfAKind());
    }

    @Test
    public void isStraight() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isStreigh());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        hand.sortCards();
        assertTrue(hand.isStreigh());
    }

    @Test
    public void isFlush() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.QUEEN, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isFlush());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        hand.sortCards();
        assertTrue(hand.isFlush());
    }

    @Test
    public void isFullHouse() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.DIAMONDS));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isFullHouse());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        hand.sortCards();
        assertTrue(hand.isFullHouse());
    }

    @Test
    public void isFourOfAKind() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isFourOfAKind());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.sortCards();
        assertTrue(hand.isFourOfAKind());
    }

    @Test
    public void isStraightFlush() {
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.NINE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        Card c = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isStraightFlush());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        hand.sortCards();
        assertTrue(hand.isStraightFlush());
    }

    @Test
    public void isRoyalFlush() {
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.QUEEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        Card c = new Card(Card.Rank.DEUCE, Card.Suit.CLUBS);
        hand.addCard(c);
        hand.sortCards();
        assertFalse(hand.isRoyalFlush());
        hand.deleteCard(c);
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        hand.sortCards();
        assertTrue(hand.isRoyalFlush());
    }

    @Test
    public void getHandValue() {
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.NINE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        hand.sortCards();
        assertEquals(Hand.Value.STRAIGHT_FLUSH, hand.getValue());
        hand.getCards().clear();
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.QUEEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        assertEquals(Hand.Value.ROYAL_FLUSH, hand.getValue());
        hand.getCards().clear();
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(Hand.Value.FULL_HOUSE, hand.getValue());
        hand.getCards().clear();
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(Hand.Value.TWO_PAIRS, hand.getValue());
        hand.getCards().clear();
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        hand.addCard(new Card(Card.Rank.DEUCE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(Hand.Value.HIGHT_CARD, hand.getValue());
        hand.getCards().clear();
        hand.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        hand.addCard(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        hand.addCard(new Card(Card.Rank.DEUCE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(Hand.Value.ONE_PAIR, hand.getValue());
    }

    @Test
    public void HightCard() {
        hand.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.NINE, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        hand.addCard(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        hand.sortCards();
        assertEquals(new Card(Card.Rank.TEN, Card.Suit.SPADES), hand.hightCard());
    }
}