package agents.behaviours.board;

import agents.BoardAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerAction;
import agents.messages.player.ProposePlayerSetup;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;
import logic.Game;
import logic.Player;
import logic.Territory;

import java.io.IOException;

public class BoardSetupBehaviour extends Behaviour {

    Game game;

    public BoardSetupBehaviour(Agent a) {
        super(a);
        game = ((BoardAgent) myAgent).getGame();
    }

    @Override
    public void onStart() {
        System.out.println("Board SETUP BEHAVIOUR STARTED");
        game.setup(((BoardAgent) myAgent).getPlayers());
    }

    @Override
    public void action() {
        //Get current AID
        AID currentPlayer = this.game.getCurrentAID();

        PlayerAction request;

        // Send message to aid
        request = new RequestPlayerAction(Actions.Setup, this.game);

        ACLMessage response;
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

        try {
            message.setContentObject(request);
            message.addReceiver(currentPlayer);
            myAgent.send(message);

            //Get answer from aid
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchSender(currentPlayer));
            response = this.getAgent().blockingReceive(mt);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }// TODO make more robust


        //Check valid response and make changes
        try {
            ProposePlayerSetup action = (ProposePlayerSetup) response.getContentObject();

            if (action.getAction() == Actions.Setup) {

                Territory t = game.getTerritory(action.getTerritories().get(0));

                t.setPlayerID(game.getCurrentPlayer().getID());
                t.increaseUnits(1);

                game.getCurrentPlayer().decreaseUnits(1);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
            // TODO: handle exception
        }

        game.nextTurn();
    }

    @Override
    public int onEnd() {
        ACLMessage endMessage = new ACLMessage(ACLMessage.INFORM);
        PlayerAction playerAction = new PlayerAction(Actions.EndSetup);
        try {
            endMessage.setContentObject(playerAction);
            for (Player player:
                 game.getPlayers()) {
                endMessage.addReceiver(player.getAid());
            }
            myAgent.send(endMessage);
            String playerList = "[ \n";

            Iterator iterator = endMessage.getAllReceiver();

            while (iterator.hasNext())
                playerList = playerList + iterator.next() + "\n";

            playerList = playerList + " ]";

            System.out.println(myAgent.getLocalName() + " Sent ENDSETUP to Players " + playerList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean done() {
        if (game.setupFinished())
            System.out.println("Board SETUP BEHAVIOUR ENDED");
        return game.setupFinished();
    }

}
