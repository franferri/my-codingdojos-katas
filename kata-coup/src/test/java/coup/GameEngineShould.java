package coup;

import coup.cards.Card;
import coup.cards.TheAmbassador;
import coup.cards.TheAssassin;
import coup.cards.TheCaptain;
import coup.cards.TheContessa;
import coup.cards.TheDuke;
import coup.decks.CourtDeck;
import coup.decks.Deck;
import coup.players.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameEngineShould {

    // TODO: If the gameEngine only have 2 onlinePlayers the rules are different
    // Mode 1 Normal (as with many onlinePlayers)
    // Mode 2 The selected starting player (player 1) gets only 1 coin at the beginning of the gameEngine
    // Mode 3 divide the cards in 3 sets of 5 (each set has 1 of each characters), Each player (player 1 and player 2) pick one of the sets and selects secretly a card and discard the rest.
    // Shuffle the third set and deal 1 card to each player and then put the remaining 3 cards face down ad court deck

    // TODO: If the gameEngine only have 2 onlinePlayers the rules are different
    // The selected starting player (player 1) gets only 1 coin at the beginning of the gameEngine

    // TODO: To cover 3 onlinePlayers or more, to choose who is doing the actions in the tests, we can just use random(), or the test repeats itself for all possible combinations in a loop (changing who does the action, who calls the bluff, who blocks the action, and who calls the bluff on the block action)

    private TheTable gameEngine;

    @BeforeEach
    void before() {

        // given
        gameEngine = new TheTable();

        gameEngine.addPlayer();
        gameEngine.addPlayer();

    }

    // CONTENTS

    // COUP gameEngine is a 2-6 onlinePlayers gameEngine

    @Test
    void a_game_needs_2_players_at_least() {

        gameEngine = new TheTable();
        gameEngine.addPlayer();

        // then
        Assertions.assertThrows(Exception.class, () -> gameEngine.startGame());

    }

    @Test
    void a_game_can_have_until_6_players() {

        gameEngine = new TheTable();
        gameEngine.addPlayer();
        gameEngine.addPlayer();
        gameEngine.addPlayer();
        gameEngine.addPlayer();
        gameEngine.addPlayer();
        gameEngine.addPlayer();
        gameEngine.addPlayer();

        // then
        Assertions.assertThrows(Exception.class, () -> gameEngine.startGame());

    }

    // The Treasury starts with 50 coins
    @Test
    void a_new_game_has_a_treasury_of_50_coins() {

        // when
        int treasury = gameEngine.treasury().coins();

        // then
        Assertions.assertEquals(50, treasury);

    }

    // The deck has 15 character cards (3 each of Duke, Assassin, Captain, Ambassador, Contessa)

    @Test
    void a_new_game_has_a_deck() {

        // when
        Deck deck = gameEngine.courtDeck();

        // then
        Assertions.assertNotNull(deck);

    }

    @Test
    void a_new_game_has_a_deck_of_15_cards() {

        // when
        Deck deck = gameEngine.courtDeck();

        // then
        Assertions.assertEquals(15, deck.cards().size());

    }

    // TODO this test is wrong, we need to do start game to evaluate the deck present in the game
    @Test
    void a_new_game_has_a_deck_that_consist_in_three_copies_of_each_characters_card() {

        // given
        CourtDeck deck = new CourtDeck();

        // when
        List<Card> cards = deck.cards();

        int ambassators = 0;
        int assasins = 0;
        int captains = 0;
        int conptesas = 0;
        int dukes = 0;

        for (Card card : cards) {

            if (card instanceof TheAmbassador) {
                ++ambassators;
            }
            if (card instanceof TheAssassin) {
                ++assasins;
            }
            if (card instanceof TheCaptain) {
                ++captains;
            }
            if (card instanceof TheContessa) {
                ++conptesas;
            }
            if (card instanceof TheDuke) {
                ++dukes;
            }

        }

        // then
        Assertions.assertEquals(3, ambassators);
        Assertions.assertEquals(3, assasins);
        Assertions.assertEquals(3, captains);
        Assertions.assertEquals(3, conptesas);
        Assertions.assertEquals(3, dukes);

    }

    // SET-UP

    // Shuffle all the characters cards and deal 2 to each player

    @Test
    void a_game_starts_with_a_shuffled_deck() {

        // when
        Deck deck = gameEngine.courtDeck();
        List<Card> originalOrderCards = new ArrayList<>(deck.cards());

        gameEngine.startGame();

        // then
        Assertions.assertNotEquals(originalOrderCards, deck.cards());

    }

    @Test
    void a_new_game_starts_with_two_character_cards_per_player() {

        // given
        int amountOfCardsAvailableInTheCourtDeck = gameEngine.courtDeck().cards().size();
        int cardsTakenFromTheDeck = gameEngine.livePlayers().size() * 2;

        // when
        gameEngine.startGame();

        // then
        Assertions.assertEquals(2, gameEngine.player(1).influenceDeck().cards().size());
        Assertions.assertEquals(2, gameEngine.player(2).influenceDeck().cards().size());

        Assertions.assertEquals(amountOfCardsAvailableInTheCourtDeck - cardsTakenFromTheDeck, gameEngine.courtDeck().cards().size());

    }

    // Give each player 2 coins

    @Test
    void a_new_game_starts_with_two_coins_per_player() {

        // when
        gameEngine.startGame();

        // then
        Assertions.assertEquals(2, gameEngine.player(1).wallet().coins());
        Assertions.assertEquals(2, gameEngine.player(2).wallet().coins());

    }

    // GOAL

    // To eliminate the influence of all other onlinePlayers and be the last survivor

    @Test
    void a_player_wins_when_no_more_players_left_alive() {

        // when
        gameEngine.startGame();

        gameEngine.player(1).dies();

        // then
        Assertions.assertEquals(2, gameEngine.whoIsTheWinner());

    }

    // INFLUENCE

    // Face down cards in front of a player represent who they influence at court
    @Test
    void a_new_game_starts_with_two_non_visible_character_cards_per_player() {

        // when
        gameEngine.startGame();

        // then
        for (Player player : gameEngine.livePlayers()) {
            Assertions.assertEquals(2, player.influenceDeck().cards().size());
            Assertions.assertEquals(0, player.lostInfluenceDeck().cards().size());
        }

    }

    // Every time a player loses an influence they have to turn over and reveal one of their face down cards
    @Test
    void when_a_player_loses_influence_must_reveal_one_of_its_cards() {

        // when
        gameEngine.startGame();
        gameEngine.player(1).looseCard();

        // then
        Assertions.assertEquals(1, gameEngine.player(1).influenceDeck().cards().size());

    }
    // When a player has lost all their influence they are exiled and out of the gameEngine

    @Test
    void a_player_dies_when_he_runs_out_of_cards() {

        // when
        gameEngine.startGame();
        gameEngine.player(1).dies();

        // then
        Assertions.assertTrue(gameEngine.player(1).isDead());

    }

}