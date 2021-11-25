package pl.edu.agh.kis.pz1.player;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.player.Hand;
import pl.edu.agh.kis.pz1.player.Player;

import java.util.List;

public class Tie {
    public boolean straightFlush(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    public boolean fourOfAKind(List<Card> a, List<Card> b) {
        return a.get(4).rank().ordinal() > b.get(4).rank().ordinal();
    }

    public boolean fullHouse(List<Card> a, List<Card> b) {
        // TODO
        return true;
    }

    public boolean flush(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    public boolean streigh(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    public boolean freeOfAKind(List<Card> a, List<Card> b) {
        // TODO
        return true;
    }

    public boolean twoPairs(List<Card> a, List<Card> b) {
        // TODO
        return true;
    }

    public boolean onePair(List<Card> a, List<Card> b) {
        // TODO
        return true;
    }

    public boolean hightCard(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }
}
