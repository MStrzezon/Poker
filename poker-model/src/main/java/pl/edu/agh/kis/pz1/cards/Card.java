package pl.edu.agh.kis.pz1.cards;

import java.util.Objects;


public class Card {
    /**
     * typ wyliczeniowy - rank
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
     * typ wyliczeniowy - suit
     */
    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES;
    }


    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank rank() { return rank; }
    public Suit suit() { return suit; }
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
