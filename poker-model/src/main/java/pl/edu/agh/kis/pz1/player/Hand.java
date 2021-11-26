package pl.edu.agh.kis.pz1.player;

import pl.edu.agh.kis.pz1.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that reflects the hand of a poker player.
 *
 * @author Michał Strzeżoń.
 */
public class Hand {
    /**
     * Poker Hand Rankings.
     */
    public enum Value {
        HIGHT_CARD,
        ONE_PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        STREIGH,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }

    /**
     * Cards in the hand.
     */
    private List<Card> cards;

    /**
     * Creates empty hand of 5 cards.
     */
    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds card to the hand and sorts cards in the hand.
     * @param c card is to be added to the hand.
     */
    public void addCard(Card c) {
        cards.add(c);
        sortCards();}

    /**
     * Gets player cards.
     * @return <code>List&lt;Card&gt;</code> which player has in the hand.
     */
    public List<Card> getCards() { return cards; }

    /**
     * Removes card from the hand.
     * @param c cards is to be removed.
     */
    public void removeCard(Card c) { cards.remove(c); }

    /**
     * Sorts cards by rank.
     */
    public void sortCards() {
        Collections.sort(cards, (c1, c2) -> Integer.compare(c2.rank().ordinal(), c1.rank().ordinal()));
    }

    /**
     * Check if player has one pair among his cards.
     * @return <code>true</code> if player has one pair;
     *         <code>false</code> otherwise.
     */
    public boolean isOnePair() {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);
        Card card4 = cards.get(3);
        Card card5 = cards.get(4);

        return ((card1.rank() == card2.rank() && card2.rank() != card3.rank()) ||
                (card2.rank() == card3.rank() && card3.rank() != card4.rank() && card2.rank() != card1.rank()) ||
                (card3.rank() == card4.rank() && card4.rank() != card5.rank() && card3.rank() != card2.rank()) ||
                (card4.rank() == card5.rank() && card4.rank() != card3.rank()));
    }

    /**
     * Check if player has two pairs among his cards.
     * @return <code>true</code> if player has two pairs;
     *         <code>false</code> otherwise.
     */
    public boolean isTwoPairs() {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);
        Card card4 = cards.get(3);
        Card card5 = cards.get(4);

        return ((card1.rank() == card2.rank() && card3.rank() == card4.rank() && card2.rank()!=card3.rank() && card4.rank() != card5.rank()) ||
                (card1.rank() != card2.rank() && card2.rank() == card3.rank() && card3.rank() != card4.rank() && card4.rank() == card5.rank())
        );
    }

    /**
     * Check if player has three of a kind among his cards.
     * @return <code>true</code> if player has three of a kind;
     *         <code>false</code> otherwise.
     */
    public boolean isThreeOfAKind() {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);
        Card card4 = cards.get(3);
        Card card5 = cards.get(4);

        return (card1.rank() == card3.rank() || card2.rank() == card4.rank() || card3.rank() == card5.rank());
    }

    /**
     * Check if player has straight among his cards.
     * @return <code>true</code> if player has straight;
     *         <code>false</code> otherwise.
     */
    public boolean isStreigh() {
        return cards.get(0).rank().ordinal() - cards.get(4).rank().ordinal() == 4;
    }

    /**
     * Check if player has flush among his cards.
     * @return <code>true</code> if player has flush;
     *         <code>false</code> otherwise.
     */
    public boolean isFlush() {
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).suit() != cards.get(i+1).suit()) return false;
        }
        return true;
    }

    /**
     * Check if player has full house among his cards.
     * @return <code>true</code> if player has full house;
     *         <code>false</code> otherwise.
     */
    public boolean isFullHouse() {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);
        Card card4 = cards.get(3);
        Card card5 = cards.get(4);

        return ((card1.rank() == card2.rank() && card2.rank() != card3.rank() && card3.rank() == card5.rank()) ||
                (card1.rank() == card3.rank() && card3.rank() != card4.rank() && card4.rank() == card5.rank()));
    }

    /**
     * Check if player has four of a kind among his cards.
     * @return <code>true</code> if player has four of a kind;
     *         <code>false</code> otherwise.
     */
    public boolean isFourOfAKind() {
        return cards.get(0).rank() == cards.get(3).rank() || cards.get(1).rank() == cards.get(4).rank();
    }

    /**
     * Check if player has straight flush among his cards.
     * @return <code>true</code> if player has straight flush;
     *         <code>false</code> otherwise.
     */
    public boolean isStraightFlush() {
        return isFlush() && isStreigh();
    }

    /**
     * Check if player has royal flush among his cards.
     * @return <code>true</code> if player has royal flush;
     *         <code>false</code> otherwise.
     */
    public boolean isRoyalFlush() {
        return isStraightFlush() && cards.get(0).rank().ordinal() == 12;
    }

    /**
     * Gets poker hand ranking.
     * @return poker hand ranking.
     */
    public Value getValue() {
        if (isRoyalFlush()) return Value.ROYAL_FLUSH;
        if (isStraightFlush()) return Value.STRAIGHT_FLUSH;
        if (isFourOfAKind()) return Value.FOUR_OF_KIND;
        if (isFullHouse()) return Value.FULL_HOUSE;
        if (isFlush()) return Value.FLUSH;
        if (isStreigh()) return Value.STREIGH;
        if (isThreeOfAKind()) return Value.THREE_OF_A_KIND;
        if (isTwoPairs()) return Value.TWO_PAIRS;
        if (isOnePair()) return Value.ONE_PAIR;
        else return Value.HIGHT_CARD;
    }

    /**
     * Casts boolean to int
     * @param a boolean
     * @return int
     */
    private int boolToInt(boolean a) {
        return a? 1:0;
    }

    /**
     * Compares player hand to another hand if the players has the same hand.
     * @param anotherHand <code>List&lt;Card&gt;</code> player with the same hand.
     * @return            <code>1</code> if this is better than anotherHand.
     *                    <code>0</code> otherwise.
     */
    public int compareTo(List<Card> anotherHand) {
        switch (getValue()) {
            case STRAIGHT_FLUSH -> {
                return boolToInt(Tie.straightFlush(cards, anotherHand));
            }
            case FOUR_OF_KIND -> {
                return boolToInt(Tie.fourOfAKind(cards, anotherHand));
            }
            case FULL_HOUSE -> {
                return boolToInt(Tie.fullHouse(cards, anotherHand));
            }
            case FLUSH -> {
                return boolToInt(Tie.flush(cards, anotherHand));
            }
            case STREIGH -> {
                return boolToInt(Tie.streigh(cards, anotherHand));
            }
            case THREE_OF_A_KIND -> {
                return boolToInt(Tie.freeOfAKind(cards, anotherHand));
            }
            case TWO_PAIRS -> {
                return boolToInt(Tie.twoPairs(cards, anotherHand));
            }
            case ONE_PAIR -> {
                return boolToInt(Tie.onePair(cards, anotherHand));
            }
            case HIGHT_CARD -> {
                return boolToInt(Tie.hightCard(cards, anotherHand));
            }
            default -> {
                return 1;
            }
        }
    }
}
