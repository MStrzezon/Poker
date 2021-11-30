package pl.edu.agh.kis.pz1.player;

import pl.edu.agh.kis.pz1.cards.Card;

import java.util.List;

/**
 * Tie is the static class that breaks the tie.
 * It may settle when both players have:
 *  <ul>
 *      <li>straight flush</li>
 *      <li>four of a kind</li>
 *      <li>full house</li>
 *      <li>flush</li>
 *      <li>straight</li>
 *      <li>free of a kind</li>
 *      <li>two pair</li>
 *      <li>one pair</li>
 *      <li>high card</li>
 *  </ul>
 */
public class Tie {
    /**
     * Throws exception if someone wants to create Tie object.
     */
    private Tie() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Resolves when both players have straight flush.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean straightFlush(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    /**
     * Resolves when both players have four of a kind.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean fourOfAKind(List<Card> a, List<Card> b) {
        return a.get(4).rank().ordinal() > b.get(4).rank().ordinal();
    }

    /**
     * Resolves when both players have full house.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean fullHouse(List<Card> a, List<Card> b) {
        int a3Best = 0;
        int b3Best = 0;
        int a2Best = 0;
        int b2Best = 0;
        if (a.get(0).rank().ordinal()==a.get(2).rank().ordinal()) {
            a3Best=a.get(0).rank().ordinal();
            a2Best=a.get(3).rank().ordinal();
        }
        if (a.get(2).rank().ordinal()==a.get(4).rank().ordinal()) {
            a3Best=a.get(0).rank().ordinal();
            a2Best=a.get(3).rank().ordinal();
        }
        if (b.get(0).rank().ordinal()==b.get(2).rank().ordinal()) {
            b3Best=b.get(0).rank().ordinal();
            b2Best=b.get(3).rank().ordinal();
        }
        if (b.get(2).rank().ordinal()==b.get(4).rank().ordinal()) {
            b3Best=b.get(0).rank().ordinal();
            b2Best=b.get(3).rank().ordinal();
        }
        if (a3Best>b3Best) return true;
        else if (a3Best<b3Best) return false;
        else return a2Best > b2Best;
    }

    /**
     * Resolves when both players have flush.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean flush(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    /**
     * Resolves when both players have straight.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean straight(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    /**
     * Resolves when both players have straight free of a kind.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean freeOfAKind(List<Card> a, List<Card> b) {
        int a3Best = 0;
        int b3Best = 0;
        if (a.get(0).rank().ordinal()==a.get(2).rank().ordinal()) {
            a3Best=a.get(0).rank().ordinal();
        }
        if (a.get(2).rank().ordinal()==a.get(4).rank().ordinal()) {
            a3Best=a.get(0).rank().ordinal();
        }
        if (b.get(0).rank().ordinal()==b.get(2).rank().ordinal()) {
            b3Best=b.get(0).rank().ordinal();
        }
        if (b.get(2).rank().ordinal()==b.get(4).rank().ordinal()) {
            b3Best=b.get(0).rank().ordinal();
        }
        return a3Best > b3Best;
    }

    /**
     * Resolves when both players have two pairs.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean twoPairs(List<Card> a, List<Card> b) {
        int aBigger = 0;
        int aSmaller = 0;
        int aLast = 0;
        if (a.get(0).rank().ordinal()!=a.get(1).rank().ordinal()) {
            aBigger = Math.max(a.get(1).rank().ordinal(), a.get(3).rank().ordinal());
            aSmaller = Math.min(a.get(1).rank().ordinal(), a.get(3).rank().ordinal());
            aLast = a.get(0).rank().ordinal();
        }
        else if (a.get(1).rank().ordinal()!=a.get(2).rank().ordinal()) {
            aBigger = Math.max(a.get(0).rank().ordinal(), a.get(4).rank().ordinal());
            aSmaller = Math.min(a.get(0).rank().ordinal(), a.get(4).rank().ordinal());
            aLast = a.get(2).rank().ordinal();
        } else {
            aBigger = Math.max(a.get(0).rank().ordinal(), a.get(2).rank().ordinal());
            aSmaller = Math.min(a.get(0).rank().ordinal(), a.get(2).rank().ordinal());
            aLast = a.get(4).rank().ordinal();
        }
        int bBigger = 0;
        int bSmaller = 0;
        int bLast = 0;
        if (b.get(0).rank().ordinal()!=b.get(1).rank().ordinal()) {
            bBigger = Math.max(b.get(1).rank().ordinal(), b.get(3).rank().ordinal());
            bSmaller = Math.min(b.get(1).rank().ordinal(), b.get(3).rank().ordinal());
            bLast = b.get(0).rank().ordinal();
        }
        else if (b.get(1).rank().ordinal()!=b.get(2).rank().ordinal()) {
            bBigger = Math.max(b.get(0).rank().ordinal(), b.get(4).rank().ordinal());
            bSmaller = Math.min(b.get(0).rank().ordinal(), b.get(4).rank().ordinal());
            bLast = b.get(2).rank().ordinal();
        } else {
            bBigger = Math.max(b.get(0).rank().ordinal(), a.get(2).rank().ordinal());
            bSmaller = Math.min(b.get(0).rank().ordinal(), a.get(2).rank().ordinal());
            bLast = b.get(4).rank().ordinal();
        }
        if (aBigger==bBigger) {
            if (aSmaller==bSmaller) return aLast > bLast;
            else return aSmaller > bSmaller;
        } else return aBigger > bBigger;
    }

    /**
     * Resolves when both players have one pair.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean onePair(List<Card> a, List<Card> b) {
        int aPair = 0;
        int aBest = 0;
        if (a.get(0).rank().ordinal()==a.get(1).rank().ordinal()) {
            aPair = a.get(0).rank().ordinal();
            aBest = a.get(2).rank().ordinal();
        }
        else if (a.get(1).rank().ordinal()==a.get(2).rank().ordinal()) {
            aPair = a.get(1).rank().ordinal();
            aBest = a.get(0).rank().ordinal();
        }
        else if (a.get(2).rank().ordinal()==a.get(3).rank().ordinal()) {
            aPair = a.get(2).rank().ordinal();
            aBest = a.get(0).rank().ordinal();
        }
        else if (a.get(3).rank().ordinal()==a.get(4).rank().ordinal()) {
            aPair = a.get(3).rank().ordinal();
            aBest = a.get(0).rank().ordinal();
        }
        int bPair = 0;
        int bBest = 0;
        if (b.get(0).rank().ordinal()==b.get(1).rank().ordinal()) {
            bPair = b.get(0).rank().ordinal();
            bBest = a.get(2).rank().ordinal();
        }
        if (b.get(1).rank().ordinal()==b.get(2).rank().ordinal()) {
            bPair = b.get(1).rank().ordinal();
            bBest = b.get(0).rank().ordinal();
        }
        if (b.get(2).rank().ordinal()==b.get(3).rank().ordinal()) {
            bPair = b.get(2).rank().ordinal();
            bBest = b.get(0).rank().ordinal();
        }
        if (b.get(3).rank().ordinal()==b.get(4).rank().ordinal()) {
            bPair = b.get(3).rank().ordinal();
            bBest = b.get(0).rank().ordinal();
        }
        if (aPair==bPair) return aBest > bBest;
        return aPair > bPair;
    }

    /**
     * Resolves when both players have high card.
     * @param a <code>List&lt;Card&gt;</code> of player a.
     * @param b <code>List&lt;Card&gt;</code> of player b.
     * @return  <code>true</code> if player a has better hand than player b.
     *          <code>false</code> otherwise.
     */
    public static boolean highCard(List<Card> a, List<Card> b) {
        if (a.get(0).rank().ordinal() > b.get(0).rank().ordinal()) return true;
        else if (a.get(0).rank().ordinal() == b.get(0).rank().ordinal()) return a.get(1).rank().ordinal() > b.get(1).rank().ordinal();
        else return false;
    }
}
