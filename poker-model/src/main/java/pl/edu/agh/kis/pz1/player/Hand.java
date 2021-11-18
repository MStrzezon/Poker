package pl.edu.agh.kis.pz1.player;

import pl.edu.agh.kis.pz1.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hand {
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

    private List<Card> cards;
    
    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card c) {
        cards.add(c);
        sortCards();}

    public List<Card> getCards() { return cards; }

    public void deleteCard(Card c) { cards.remove(c); }

    public void sortCards() {
        Collections.sort(cards, (c1, c2) -> Integer.compare(c2.rank().ordinal(), c1.rank().ordinal()));
    }

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

    public boolean isThreeOfAKind() {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);
        Card card4 = cards.get(3);
        Card card5 = cards.get(4);

        return (card1.rank() == card3.rank() || card2.rank() == card4.rank() || card3.rank() == card5.rank());
    }

    public boolean isStreigh() {
        return cards.get(0).rank().ordinal() - cards.get(4).rank().ordinal() == 4;
    }

    public boolean isFlush() {
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).suit() != cards.get(i+1).suit()) return false;
        }
        return true;
    }

    public boolean isFullHouse() {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);
        Card card4 = cards.get(3);
        Card card5 = cards.get(4);

        return ((card1.rank() == card2.rank() && card2.rank() != card3.rank() && card3.rank() == card5.rank()) ||
                (card1.rank() == card3.rank() && card3.rank() != card4.rank() && card4.rank() == card5.rank()));
    }

    public boolean isFourOfAKind() {
        return cards.get(0).rank() == cards.get(3).rank() || cards.get(1).rank() == cards.get(4).rank();
    }

    public boolean isStraightFlush() {
        return isFlush() && isStreigh();
    }

    public boolean isRoyalFlush() {
        return isStraightFlush() && cards.get(0).rank().ordinal() == 12;
    }

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

    public Card hightCard() {
        return cards.get(0);
    }
}
