package logic;

import java.util.ArrayList;

public class CardSet {
	public ArrayList<Card> cards;
	
	public CardSet(ArrayList<Card> cards) {
		this.cards = new ArrayList<Card>();
		this.cards.addAll(cards);
	}
}
