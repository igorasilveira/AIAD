package agents.behaviours.board;

import agents.BoardAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.Game;

import java.io.IOException;

public class BoardSetupBehaviour extends Behaviour {

    Game game;

    public BoardSetupBehaviour(Agent a) {
        super(a);
        game = ((BoardAgent) myAgent).getGame();
    }

    @Override
    public void onStart() {
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
            System.out.println("Board: Setup message sent to " + currentPlayer);

            //Get answer from aid
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchSender(currentPlayer));
            response = this.getAgent().blockingReceive(mt);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }// TODO make more robust


        //Check valid response and make changes
        try {
            PlayerAction action = (PlayerAction) response.getContentObject();

            if (action.getAction() == Actions.Setup) {
                System.out.println("Received Setup from " + response.getSender());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
            // TODO: handle exception
        }

        game.nextTurn();
    }

    @Override
    public boolean done() {
        return game.setupFinished();
    }

}
