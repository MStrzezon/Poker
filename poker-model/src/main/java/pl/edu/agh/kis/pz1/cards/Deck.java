package pl.edu.agh.kis.pz1.cards;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa rezprezentująca talię kart
 * @version 1.0
 * @author Michał Strzeżoń
 *
 */
public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        fabryki();
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card topCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        else {
            return null;
        }
    }

    public void addCard(Card c) { cards.add(c); }

    /**
     * Metoda produkująca posortowaną talię kart
     * @return posortowana talia kart
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
     * Metoda dostająca talię kart i zwracająca ją w posortowanej kolejności
     * @return posortowana talia kart
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

}
