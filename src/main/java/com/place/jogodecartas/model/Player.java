package com.place.jogodecartas.model;

import java.util.*;

public class Player {

    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private Card lastCard;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card c) {
        if (c != null) {
            hand.add(c);
        }
    }

    public void setLastCard(Card c) {
        this.lastCard = c;
    }

    public Card getLastCard() {
        return lastCard;
    }

    // ESSENCIAL: equals/hashCode pelo nome para uso em HashMap!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
