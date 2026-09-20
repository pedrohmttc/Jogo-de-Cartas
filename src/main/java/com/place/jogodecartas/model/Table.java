package com.place.jogodecartas.model;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final List<Card> cards = new ArrayList<>();

    /** Adiciona carta sobre a mesa */
    public void play(Card c) {
        cards.add(c);
    }

    /** Limpa a mesa para próxima rodada */
    public void clear() {
        cards.clear();
    }

    /** Soma das cartas, com Ás valendo 1 ou 11 */
    public int getSum() {
        int sum = 0;
        int numAces = 0;

        for (Card c : cards) {
            if (c.getRank().equals("A")) {
                sum += 11;
                numAces++;
            } else {
                sum += c.getValue();
            }
        }

        // Ajusta Ás de 11 para 1 se ultrapassar 21
        while (sum > 21 && numAces > 0) {
            sum -= 10;
            numAces--;
        }
        return sum;
    }

    public List<Card> getCards() {
        return cards;
    }
}
