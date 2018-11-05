package agents.messages.board;

import agents.messages.PlayerAction;
import logic.Game;

public class RequestPlayerAction extends PlayerAction{
	
	private Game game;
	
	public RequestPlayerAction() {
		// board requesting player to make a move
	}
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

}
