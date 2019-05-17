package coup.actions;

import coup.TestingActions;
import coup.cards.TheAmbassator;
import coup.cards.TheAssassin;
import coup.cards.TheContessa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActionAssassinateShould extends TestingActions {

    // Action: Pay 3 coins, choose the player to lose influence
    // Action can be challenged

    // Block: Can be blocked by Contessa
    // Block can be challenged

    @BeforeEach
    public void before() throws Exception {
        super.before();
        action = new Assassinate(gameEngine);
    }

    // Action costs money
    @Test
    public void player_needs_money_to_do_the_action() {
        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);

        Assertions.assertThrows(Exception.class, () -> action.doAction());
    }

    // Action
    @Test
    public void player_does_action() throws Exception {
        // given
        gameEngine.player(1).gainCoin();

        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);

        action.doAction();

        // then
        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(1).coins());

        Assertions.assertEquals(1, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

    // Action can be challenged
    // Challenger (wins)
    @Test
    public void player_does_action_and_other_player_calls_the_bluff_and_wins_the_call() throws Exception {
        // given
        gameEngine.player(1).cards().clear();
        gameEngine.player(1).cards().add(0, new TheAmbassator());
        gameEngine.player(1).cards().add(0, new TheAmbassator());
        gameEngine.player(1).gainCoin();

        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);
        action.doAction();

        gameEngine.playerCallingTheBluff = gameEngine.player(2);
        action.doCallTheBluffOnAction();

        // then
        Assertions.assertEquals(49, gameEngine.treasury());

        Assertions.assertEquals(1, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

    // Action can be challenged
    // Challenger (lose)
    @Test
    public void player_does_action_and_other_calls_the_bluff_and_lose_the_call() throws Exception {
        // given
        gameEngine.player(1).cards().clear();
        gameEngine.player(1).cards().add(0, new TheAssassin());
        gameEngine.player(1).cards().add(0, new TheAmbassator());
        gameEngine.player(1).gainCoin();

        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);
        action.doAction();

        gameEngine.playerCallingTheBluff = gameEngine.player(2);
        action.doCallTheBluffOnAction();

        // then
        Assertions.assertEquals(49, gameEngine.treasury());

        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(1).coins());

        Assertions.assertEquals(0, gameEngine.player(2).cardsInGame());
        Assertions.assertTrue(gameEngine.player(2).isDead());
    }

    // Action can be blocked
    @Test
    public void player_does_action_and_gets_block() throws Exception {
        // given
        gameEngine.player(1).gainCoin();

        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);

        action.doAction();

        gameEngine.playerBlockingTheAction = gameEngine.player(2);
        action.doBlockAction();

        // then
        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

    // Action can be blocked
    // Block can be challenged
    // Challenger wins
    @Test
    public void player_does_action_and_gets_block_but_a_player_calls_the_bluff_on_the_block_and_wins_the_call() throws Exception {
        // given
        gameEngine.player(1).gainCoin();

        // given
        gameEngine.player(2).cards().clear();
        gameEngine.player(2).cards().add(0, new TheAmbassator());
        gameEngine.player(2).cards().add(1, new TheAmbassator());

        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);

        action.doAction();

        gameEngine.playerBlockingTheAction = gameEngine.player(2);
        action.doBlockAction();

        gameEngine.playerCallingTheBluff = gameEngine.player(1);
        action.doCallTheBluffOnBlockAction();

        // then
        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(1).coins());

        Assertions.assertEquals(0, gameEngine.player(2).cardsInGame());
        Assertions.assertTrue(gameEngine.player(2).isDead());
    }

    // Action can be blocked
    // Block can be challenged
    // Challenger lose
    @Test
    public void player_does_action_and_gets_block_but_a_player_calls_the_bluff_on_the_block_and_lose_the_call() throws Exception {
        // given
        gameEngine.player(1).gainCoin();

        // given
        gameEngine.player(2).cards().clear();
        gameEngine.player(2).cards().add(0, new TheContessa());
        gameEngine.player(2).cards().add(1, new TheAmbassator());

        // when
        gameEngine.playerDoingTheAction = gameEngine.player(1);
        gameEngine.targetPlayerForAssassination = gameEngine.player(2);

        action.doAction();

        gameEngine.playerBlockingTheAction = gameEngine.player(2);
        action.doBlockAction();

        gameEngine.playerCallingTheBluff = gameEngine.player(1);
        action.doCallTheBluffOnBlockAction();

        // then
        Assertions.assertEquals(1, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());

    }

}
