package coup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private int coins = 0;
    private List<Card> cards = new ArrayList<>();

    public Player() {

    }

    public void addCoin() {
        ++coins;
    }

    public void looseCoin() {
        --coins;
    }

    public void looseCard() {
        assassinate();
    }

    public int coins() {
        return coins;
    }

    public List<Card> cards() {
        return cards;
    }

    public void assassinate() { // What if there are no cards visible left?
        int card = new Random().nextInt(2);
        cards().get(card).setVisible(true);
    }

    public void dies() {
        cards().get(0).setVisible(true);
        cards().get(1).setVisible(true);
    }

    public boolean isDead() {
        return cards().get(0).isVisible() && cards().get(1).isVisible();
    }

    public int cardsInGame() {

        int leftCards = 0;
        if (!cards().get(0).isVisible()) {
            ++leftCards;
        }
        if (!cards().get(1).isVisible()) {
            ++leftCards;
        }

        return leftCards;
    }

    public boolean canHeBlockAction(Action action) {

        for (Card card : cards) {

            Class classz = card.blocksAction().getClass();
            if (classz == null) return false;

            if (classz == action.getClass()) {
                return true;
            }

        }

        return false;

    }

}
