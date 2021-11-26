package pl.edu.agh.kis.pz1.cards;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck is class, which represents deck of cards.
 * A Deck object encapsulates the cards, which can:
 * <ul>
 *     <li>create</li>
 *     <li>shuffle</li>
 * </ul>
 *
 * @author Michał Strzeżoń
 */
public class Deck {
    /**
     * List of cards, which contain 52 cards.
     */
    private List<Card> cards = new ArrayList<>();

    /**
     * Created sorted deck.
     */
    public Deck() {
        fabryki();
    }

    /**
     * Gets the cards.
     * @return     <code>List&lt;Card&gt;</code> which contain cards in deck.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Gets the top card.
     * @return <code>Card</code> from the deck top.
     */
    public Card topCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        else {
            return null;
        }
    }

    /**
     * Adds card to the deck.
     * @param c card to be added to the deck of cards.
     */
    public void addCard(Card c) { cards.add(c); }

    /**
     * Creates new sorted deck of 52 cards.
     */
    public void fabryki() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Shuffles the cards in deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

}
