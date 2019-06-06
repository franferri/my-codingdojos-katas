package coup.actions;

import coup.Action;
import coup.GameEngine;

public class Steal extends Action {

    // Action: Take 2 coins from another player
    // Action can be challenged

    // Block: Can be blocked by Captain
    // Block by Captain can be challenged

    // Block: Can be blocked by Ambassador
    // Block by Ambassador can be challenged

    public Steal() {
    }

    public Steal(GameEngine gameEngine) {
        super(gameEngine);
    }

    // Action
    public void doActionInternal()  {
        gameEngine.playerTakesCoinsFromOtherPlayer(gameEngine.playerDoingTheAction, gameEngine.targetPlayer, 2);
    }

    // Block Action
    public void doBlockActionInternal()  {
        gameEngine.playerTakesCoinsFromOtherPlayer(gameEngine.targetPlayer, gameEngine.playerDoingTheAction, 2);
    }

}
