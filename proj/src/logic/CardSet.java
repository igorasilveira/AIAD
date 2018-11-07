package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class CardSet implements Serializable {
	private ArrayList<Card> cards;
	
	public CardSet(ArrayList<Card> cards) {
		this.cards = new ArrayList<Card>();
		this.cards.addAll(cards);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
}
