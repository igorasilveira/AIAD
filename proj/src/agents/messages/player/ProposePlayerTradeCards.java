package agents.messages.player;

import agents.messages.Actions;
import logic.CardSet;

public class ProposePlayerTradeCards extends ProposePlayerAction {

	private CardSet cardSet;
	
	public ProposePlayerTradeCards(CardSet cards) {
		super(Actions.TradeCards);
		this.cardSet = cards;
	}

	public CardSet getCardSet() {
		return cardSet;
	}
}
