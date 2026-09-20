package com.place.jogodecartas.model;

public class Card {
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() { return suit; }
    public String getRank() { return rank; }

    @Override
    public String toString() {
        return rank + suit;
    }
    public int getValue() {
    switch(rank) {
        case "A": return 11;
        case "K":
        case "Q":
        case "J": return 10;
        default: return Integer.parseInt(rank);
    }
}


}
