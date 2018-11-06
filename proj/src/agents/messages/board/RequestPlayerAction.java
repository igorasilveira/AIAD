package agents.messages.board;

import agents.messages.Actions;
import agents.messages.PlayerAction;
import logic.Game;

public class RequestPlayerAction extends PlayerAction{

	protected Game game;

	public RequestPlayerAction(Actions action, Game game) {
		super(action);
		this.game = game;
		// board requesting player to make a move
	}

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

}
