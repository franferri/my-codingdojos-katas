package coup.actions;

import coup.Action;
import coup.GameEngine;

public class Coup7 extends Action {

    // Action: Pay 7 cons, choose the player to lose Influence
    // Action cannot be challenged

    // Block: Cannot be blocked
    // -

    public Coup7(GameEngine gameEngine) throws Exception {
        super(gameEngine);
    }

    // Setup
    public boolean canThisActionBeChallenged() {
        return false;
    }

    public boolean canThisBlockActionBeChallenged() {
        return false;
    }

    // Action
    public void doActionInternal() throws Exception {
        gameEngine.playerReturnCoinsToTreasury(gameEngine.playerDoingTheAction, 7);
        gameEngine.targetPlayer.looseCard();
    }

    // Block Action
    public void doBlockAction() throws Exception {
        throw new Exception("This action can't be blocked");
    }

}
