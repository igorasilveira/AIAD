package agents.behaviours.board;

import agents.BoardAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerAction;
import agents.messages.player.ProposePlayerSetup;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.Game;
import logic.Player;
import logic.Territory;
import jade.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

import java.io.IOException;
import java.util.Iterator;

public class BoardSetupBehaviour extends Behaviour {

    private Game game;
    private boolean test = true;

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
            if (test) {
//                System.out.println("Message Sent to : " + currentPlayer);
                test = false;
            }

            //Get answer from aid
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchSender(currentPlayer));
            response = this.getAgent().receive(mt);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }// TODO make more robust


        if (response != null) {

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
        //Check valid response and make changes

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
