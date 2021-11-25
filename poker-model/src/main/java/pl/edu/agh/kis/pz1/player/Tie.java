package pl.edu.agh.kis.pz1.player;

import pl.edu.agh.kis.pz1.cards.Card;

import java.util.List;

public class Tie {
    public static boolean straightFlush(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    public static boolean fourOfAKind(List<Card> a, List<Card> b) {
        return a.get(4).rank().ordinal() > b.get(4).rank().ordinal();
    }

    public static boolean fullHouse(List<Card> a, List<Card> b) {
        int a3Best = 0, b3Best = 0, a2Best = 0, b2Best = 0;
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

    public static boolean flush(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    public static boolean streigh(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }

    public static boolean freeOfAKind(List<Card> a, List<Card> b) {
        int a3Best = 0, b3Best = 0;
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

    public static boolean twoPairs(List<Card> a, List<Card> b) {
        int aBigger = 0;
        int aSmaller = 0;
        int aLast = 0;
        if (a.get(0).rank().ordinal()!=a.get(1).rank().ordinal()) {
            aBigger = Math.max(a.get(1).rank().ordinal(), a.get(3).rank().ordinal());
            aSmaller = Math.min(a.get(1).rank().ordinal(), a.get(3).rank().ordinal());
            aLast = a.get(0).rank().ordinal();
        }
        if (a.get(1).rank().ordinal()!=a.get(2).rank().ordinal()) {
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
        if (b.get(1).rank().ordinal()!=b.get(2).rank().ordinal()) {
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

    public static boolean onePair(List<Card> a, List<Card> b) {
        int aPair = 0, aBest = 0;
        if (a.get(0).rank().ordinal()==a.get(1).rank().ordinal()) aPair = a.get(0).rank().ordinal(); aBest=a.get(2).rank().ordinal();
        if (a.get(1).rank().ordinal()==a.get(2).rank().ordinal()) aPair = a.get(1).rank().ordinal(); aBest=a.get(0).rank().ordinal();
        if (a.get(2).rank().ordinal()==a.get(3).rank().ordinal()) aPair = a.get(2).rank().ordinal(); aBest=a.get(0).rank().ordinal();
        if (a.get(3).rank().ordinal()==a.get(4).rank().ordinal()) aPair = a.get(3).rank().ordinal(); aBest=a.get(0).rank().ordinal();
        int bPair = 0, bBest = 0;
        if (b.get(0).rank().ordinal()==b.get(1).rank().ordinal()) bPair = b.get(0).rank().ordinal(); bBest=a.get(2).rank().ordinal();
        if (b.get(1).rank().ordinal()==b.get(2).rank().ordinal()) bPair = b.get(1).rank().ordinal(); bBest=b.get(0).rank().ordinal();
        if (b.get(2).rank().ordinal()==b.get(3).rank().ordinal()) bPair = b.get(2).rank().ordinal(); bBest=b.get(0).rank().ordinal();
        if (b.get(3).rank().ordinal()==b.get(4).rank().ordinal()) bPair = b.get(3).rank().ordinal(); bBest=b.get(0).rank().ordinal();
        if (aPair==bPair) return aBest > bBest;
        return aPair > bPair;
    }

    public static boolean hightCard(List<Card> a, List<Card> b) {
        return a.get(0).rank().ordinal() > b.get(0).rank().ordinal();
    }
}
