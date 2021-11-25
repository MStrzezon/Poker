package pl.edu.agh.kis.pz1.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.pz1.cards.Card;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TieTest {

    @Test
    public void straightFlush() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.NINE, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        assertFalse(Tie.straightFlush(a, b));
        assertTrue(Tie.straightFlush(b, a));
    }

    @Test
    public void fourOfAKind() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        a.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        b.add(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        b.add(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        assertFalse(Tie.fourOfAKind(a, b));
        assertTrue(Tie.fourOfAKind(b, a));
    }

    @Test
    public void fullHouse() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        a.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.KING, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        b.add(new Card(Card.Rank.QUEEN, Card.Suit.DIAMONDS));
        b.add(new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        assertTrue(Tie.fullHouse(a, b));
        assertFalse(Tie.fullHouse(b, a));
    }

    @Test
    public void flush() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        assertTrue(Tie.flush(a, b));
        assertFalse(Tie.flush(b, a));
    }

    @Test
    public void streigh() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS));
        a.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        b.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        b.add(new Card(Card.Rank.NINE, Card.Suit.DIAMONDS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.SEVEN, Card.Suit.CLUBS));
        assertFalse(Tie.streigh(a, b));
        assertTrue(Tie.streigh(b, a));
    }

    @Test
    public void freeOfAKind() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.SIX, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        b.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        assertTrue(Tie.freeOfAKind(a, b));
        assertFalse(Tie.freeOfAKind(b, a));
    }

    @Test
    public void twoPairs() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.KING, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        assertFalse(Tie.twoPairs(a, b));
        assertTrue(Tie.twoPairs(b, a));
        a.clear();
        b.clear();
        a.add(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.KING, Card.Suit.DIAMONDS));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.KING, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        assertTrue(Tie.twoPairs(a, b));
        assertFalse(Tie.twoPairs(b, a));
        a.clear();
        b.clear();
        a.add(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.KING, Card.Suit.DIAMONDS));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.KING, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        assertTrue(Tie.twoPairs(a, b));
        assertFalse(Tie.twoPairs(b, a));
    }

    @Test
    public void onePair() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.KING, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FIVE, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        assertFalse(Tie.twoPairs(a, b));
        assertTrue(Tie.twoPairs(b, a));
        a.clear();
        b.clear();
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.DEUCE, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FIVE, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        assertFalse(Tie.twoPairs(a, b));
        assertTrue(Tie.twoPairs(b, a));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.DEUCE, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        b.add(new Card(Card.Rank.FIVE, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        assertFalse(Tie.onePair(a, b));
        assertTrue(Tie.onePair(b, a));
    }

    @Test
    public void hightCard() {
        List<Card> a = new ArrayList<>();
        List<Card> b = new ArrayList<>();
        a.add(new Card(Card.Rank.KING, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        a.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        a.add(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        a.add(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        b.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.FIVE, Card.Suit.CLUBS));
        b.add(new Card(Card.Rank.DEUCE, Card.Suit.SPADES));
        assertTrue(Tie.hightCard(a, b));
        assertFalse(Tie.hightCard(b, a));
    }
}