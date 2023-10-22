package appv2.core;

import appv2.cards.ACard;
import appv2.cards.PrincessAnnette;
import appv2.cards.CountessWilhelmina;
import appv2.cards.KingArnaud;
import appv2.cards.PrinceArnaud;
import appv2.cards.HandmaidSusannah;
import appv2.cards.BaronTalus;
import appv2.cards.PriestTomas;
import appv2.cards.GuardOdette;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;


public class GameMode {

    private static final int CARD_AMOUNT_IN_DECK = 16;

    private static final int PLAYER_COUNT_TWO_TOKENS_TO_WIN = 7;
    private static final int PLAYER_COUNT_THREE_TOKENS_TO_WIN = 5;
    private static final int PLAYER_COUNT_FOUR_TOKENS_TO_WIN = 4;

    public static final int AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS = 2;

    private final PlayerController[] playerControllers;
    private int mostRecentPlayerID;

    private final ArrayList<ACard> tableCardsPile;
    private final ArrayList<ACard> examiningCards;
    private ACard hiddenCard;

    public GameMode(int playerCount, String[] playerNames) {
        super();

        this.tableCardsPile = new ArrayList<ACard>();
        this.examiningCards = new ArrayList<ACard>();
        this.hiddenCard = null;

        this.playerControllers = new PlayerController[playerCount];
        for (int i = 0; i < playerCount; i++) {
            this.playerControllers[i] = new PlayerController(i, playerNames[i]);
        }

        // TODO: Select the starting player as stated in the rules.
        this.mostRecentPlayerID = 0;

        return;
    }

    // region Game Core methods


    public void selectNextValidPlayer() {
        int nextPlayerID = this.mostRecentPlayerID + 1;
        if (nextPlayerID >= this.getPlayerCount())
            nextPlayerID = 0;

        this.mostRecentPlayerID = nextPlayerID;
        return;
    }

    /**
     * Prepares the game for the next round. This includes:
     * - Resetting the deck on the table.
     * - Resetting the discard pile.
     * - Resetting the hands of all players.
     * - Selecting the next player to start.
     */
    public void prepareForNextRound() {
        this.prepareTableCardsPile();
        for (PlayerController PC : this.playerControllers)
            PC.prepareForNextRound();

        return;
    }

    public ACard drawCard() {
        if (this.tableCardsPile.isEmpty()) {
            if (this.hiddenCard == null)
                throw new RuntimeException("No cards left to draw.");

            ACard card = this.hiddenCard;
            this.hiddenCard = null;

            return card;
        }

        return this.tableCardsPile.remove(0);
    }

    // endregion Game Core methods

    // region Utility methods

    /**
     * <p>Adds initial cards to table deck (16 cards) and shuffles them:</p>
     * <ul>
     *  <li>Princess Annette (1x)
     *  <li>Countess Wilhelmina (1x)
     *  <li>King Arnaud IV (1x)
     *  <li>Prince Arnaud (2x)
     *  <li>Handmaid Susannah (2x)
     *  <li>Baron Talus (2x)
     *  <li>Priest Tomas (2x)
     *  <li>Guard Odette (5x)
     * </ul>
     */
    private void prepareTableCardsPile() {
        this.tableCardsPile.clear();
        this.examiningCards.clear();
        this.hiddenCard = null;

        this.tableCardsPile.add(new PrincessAnnette());

        this.tableCardsPile.add(new CountessWilhelmina());

        this.tableCardsPile.add(new KingArnaud());

        this.tableCardsPile.add(new PrinceArnaud());
        this.tableCardsPile.add(new PrinceArnaud());

        this.tableCardsPile.add(new HandmaidSusannah());
        this.tableCardsPile.add(new HandmaidSusannah());

        this.tableCardsPile.add(new BaronTalus());
        this.tableCardsPile.add(new BaronTalus());

        this.tableCardsPile.add(new PriestTomas());
        this.tableCardsPile.add(new PriestTomas());

        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());

        Collections.shuffle(this.tableCardsPile);
        if (this.tableCardsPile.size() != GameMode.CARD_AMOUNT_IN_DECK)
            throw new RuntimeException("Table cards pile size is not 16.");

        this.hiddenCard = this.tableCardsPile.remove(0);

        if (this.getPlayerCount() == GameMode.AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS) {
            this.examiningCards.add(this.tableCardsPile.remove(0));
            this.examiningCards.add(this.tableCardsPile.remove(0));
            this.examiningCards.add(this.tableCardsPile.remove(0));
        }

        return;
    }

    // endregion Utility methods

    // region Getters and setters

    public int getPlayerCount() {
        return this.playerControllers.length;
    }

    public String[] getPlayerNames() {
        return Arrays.stream(this.playerControllers)
                .map(PlayerController::getPlayerName)
                .toArray(String[]::new);
    }

    public PlayerController getPlayerControllerByID(int playerID) {
        return this.playerControllers[playerID];
    }

    public PlayerController getPlayerControllerByName(String playerName) {
        for (PlayerController PC : this.playerControllers) {
            if (PC.getPlayerName().equals(playerName))
                return PC;
        }

        throw new RuntimeException("Player name not found.");
    }

    public PlayerController getMostRecentPlayerController() {
        return this.getPlayerControllerByID(this.mostRecentPlayerID);
    }

    public ArrayList<ACard> getTableCardsPile() {
        return this.tableCardsPile;
    }

    public ArrayList<ACard> getExaminingCards() {
        return this.examiningCards;
    }

    public ACard getHiddenCard() {
        return this.hiddenCard;
    }

    // endregion Getters and setters

}
