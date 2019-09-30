package coup.actions;

import coup.TestingActions;
import coup.cards.TheAmbassador;
import coup.cards.TheCaptain;
import coup.cards.TheDuke;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActionStealShould extends TestingActions {

    // Action: Take 2 coins from another player
    // Action can be challenged

    // Block: Can be blocked by a player claiming Captain
    // Block by Captain can be challenged

    // Block: Can be blocked by a player claiming Ambassador
    // Block by Ambassador can be challenged

    @BeforeEach
    public void before()  {
        super.before();
        action = new Steal(gameEngine);
    }

    // Action try to steal more than other player has
    @Test
    void player_does_action_to_a_poor_player()  {
        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        gameEngine.playerReturnCoinsToTreasury(gameEngine.getTargetPlayer(), 2);

        assertThrowsWithMessage(() -> action.doAction(), "Player is broke and we can't take more coins from it");
    }

    // Action cant be done to himself (Engine integrity test)
    @Test
    void player_does_action_to_himself() {
        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(1));

        assertThrowsWithMessage(() -> action.doAction(), "Action can't be done to himself");
    }

    // Action
    @Test
    void player_does_action()  {
        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(4, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(2).coins());
    }

    // Player cant challenge himself (Engine integrity test)
    @Test
    void player_does_action_and_challenge_himself()  {
        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(1));

        assertThrowsWithMessage(() -> action.doCallTheBluffOnAction(), "Action bluff can't be called over himself");
    }

    // Action can be challenged
    // Challenger (wins)
    @Test
    void player_does_action_and_other_player_calls_the_bluff_and_wins_the_call()  {
        // given
        gameEngine.player(1).influenceDeck().clear();
        gameEngine.player(1).influenceDeck().add(0, new TheAmbassador());
        gameEngine.player(1).influenceDeck().add(0, new TheAmbassador());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(2));
        action.doCallTheBluffOnAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(1, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

    // Action can be challenged
    // Challenger (lose)
    @Test
    void player_does_action_and_other_calls_the_bluff_and_lose_the_call()  {
        // given
        gameEngine.player(1).influenceDeck().clear();
        gameEngine.player(1).influenceDeck().add(0, new TheCaptain());
        gameEngine.player(1).influenceDeck().add(0, new TheAmbassador());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(2));
        action.doCallTheBluffOnAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(4, gameEngine.player(1).coins());

        Assertions.assertEquals(1, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(2).coins());
    }

    // Player cant block himself (Engine integrity test)
    @Test
    void player_does_action_and_blocks_himself()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheCaptain());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(1));

        assertThrowsWithMessage(() -> action.doBlockAction(), "Player cant block himself");
    }

    // Action can be blocked (by Captain)
    @Test
    void player_does_action_and_gets_block_by_captain()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheCaptain());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(2));
        action.doBlockAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

    // BlockAction bluff can't be called over the player doing the BlockAction (Engine integrity test)
    @Test
    void player_does_action_and_gets_block_then_the_player_blocking_challenge_himself()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheDuke());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(2));
        action.doBlockAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(2));

        assertThrowsWithMessage(() -> action.doCallTheBluffOnBlockAction(), "BlockAction bluff can't be called over the player doing the BlockAction");
    }

    // Action can be blocked (by Captain)
    // Block can be challenged
    // Challenger wins
    @Test
    void player_does_action_and_gets_block_by_captain_but_a_player_calls_the_bluff_on_the_block_and_wins_the_call()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheDuke());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(2));
        action.doBlockAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(1));
        action.doCallTheBluffOnBlockAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(4, gameEngine.player(1).coins());

        Assertions.assertEquals(1, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(0, gameEngine.player(2).coins());
    }

    // Action can be blocked (by Captain)
    // Block can be challenged
    // Challenger lose
    @Test
    void player_does_action_and_gets_block_by_captain_but_a_player_calls_the_bluff_on_the_block_and_lose_the_call()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheCaptain());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(2));
        action.doBlockAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(1));
        action.doCallTheBluffOnBlockAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(1, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());

    }

    // Action can be blocked (by Ambassador)
    @Test
    void player_does_action_and_gets_block_by_ambassador()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheAmbassador());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(2));
        action.doBlockAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(2, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

    // Action can be blocked (by Ambassador)
    // Block can be challenged
    // Challenger wins
    @Test
    void player_does_action_and_gets_block_by_ambassador_but_a_player_calls_the_bluff_on_the_block_and_wins_the_call()  {
        // given
        gameEngine.player(2).influenceDeck().clear();
        gameEngine.player(2).influenceDeck().add(0, new TheAmbassador());
        gameEngine.player(2).influenceDeck().add(1, new TheDuke());

        // when
        gameEngine.setPlayerDoingTheAction(gameEngine.player(1));
        gameEngine.setTargetPlayer(gameEngine.player(2));
        action.doAction();

        gameEngine.setPlayerBlockingTheAction(gameEngine.player(2));
        action.doBlockAction();

        gameEngine.setPlayerCallingTheBluff(gameEngine.player(1));
        action.doCallTheBluffOnBlockAction();

        // then
        Assertions.assertEquals(46, gameEngine.treasury());

        Assertions.assertEquals(1, gameEngine.player(1).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(1).coins());

        Assertions.assertEquals(2, gameEngine.player(2).cardsInGame());
        Assertions.assertEquals(2, gameEngine.player(2).coins());
    }

}
