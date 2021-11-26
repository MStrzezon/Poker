package pl.edu.agh.kis.pz1.cards;

import java.util.Objects;

/**
 * Card is the class, which represents the playing card.
 * A Card object encapsulates information about card:
 * <ul>
 *     <li>Rank</li>
 *     <li>Suit</li>
 * </ul>
 */
public class Card {
    /**
     * Rank
     */
    public enum Rank {
        DEUCE,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE;
    }

    /**
     * Suit
     */
    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES;
    }

    /**
     * Rank of card.
     */
    private final Rank rank;
    /**
     * Suit of card.
     */
    private final Suit suit;

    /**
     * Creates card with the given rank and suit.
     * @param rank the rank of card
     * @param suit the suit of card
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Gets the rank.
     * @return rank of card.
     */
    public Rank rank() { return rank; }
    /**
     * Gets the suit.
     * @return suit of card.
     */
    public Suit suit() { return suit; }

    /**
     * Creates string of card.
     * @return this card string.
     */
    public String toString() { return rank+ " of " + suit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
