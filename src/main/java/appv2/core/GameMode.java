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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * <p>The rules of the game are implemented here. It is a high-level manager object for
 * a game and controls the overall flow of the game.</p>
 * <p>Is spawned at the beginning of a game and not destroyed until the
 * application is killed or a completely new game is started.</p>
 * <br />
 * <p>On the most basic level, these rules include:</p>
 * <ul>
 *  <li>The number of players present in the game.
 *  <li>The order of all valid players and calling their turns.
 *  <li>The amount of cards and the order them in the deck.
 *  <li>Investigating the game winner.
 * </ul>
 * @see appv2.core.GameState#initializeNewGame(int, String[])
 */
public class GameMode {

    private static final int CARD_AMOUNT_IN_DECK = 16;

    private static final int PLAYER_COUNT_TWO_TOKENS_TO_WIN = 7;
    private static final int PLAYER_COUNT_THREE_TOKENS_TO_WIN = 5;
    private static final int PLAYER_COUNT_FOUR_TOKENS_TO_WIN = 4;

    public static final int AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS = 2;

    /**
     * <p>Contains the player controllers who won the most recent round.</p>
     * <p><b>Warning:</b>This may be empty.</p>
     */
    private ArrayList<PlayerController> mostRecentRoundWinners;
    /**
     * <p>Contains the player controllers who won the game.</p>
     * <p><b>Warning:</b>This may be empty.</p>
     */
    private ArrayList<PlayerController> gameWinners;
    /**
     * <p>The ID of the player who played the last turn.</p>
     */
    private int mostRecentPlayerID;
    /**
     * <p>Array of all player controllers in the game.</p>
     */
    private final PlayerController[] playerControllers;

    /**
     * <p>As written in the rules. These cards are not in the deck and are removed at the beginning of each round
     * and placed faced-up on the table to be examined by everyone (applies only to 2-player games).</p>
     * <p><b>Warning:</b> This may be empty.</p>
     */
    private final ArrayList<ACard> examiningCards;
    /**
     * <p>As written in the rules. This card is removed from the top of the deck and
     * placed faced-down on the table at the beginning of each round.</p>
     * <p><b>Warning:</b> This may be null.</p>
     */
    private ACard hiddenCard;
    /**
     * <p>As written in the rules. This is the deck of all cards remaining to
     * be drawn (does not include hidden card).</p>
     * <p><b>Warning:</b> This may be empty.</p>
     */
    private final ArrayList<ACard> tableCardsPile;

    /**
     * <p>Counts up as a round in a game progresses.</p>
     */
    private int currentRoundNumber;

    /**
     * <p>Constructor. Initializes default values and
     * creates all necessary player controllers.</p>
     *
     * @param playerCount The number of players in this game.
     * @param playerNames The names of all players in the game.
     */
    public GameMode(int playerCount, String[] playerNames) {
        super();

        this.mostRecentRoundWinners = null;
        this.gameWinners = null;
        this.playerControllers = new PlayerController[playerCount];
        for (int i = 0; i < playerCount; i++)
            this.playerControllers[i] = new PlayerController(i, playerNames[i]);

        // TODO: Select the starting player as stated in the rules.
        this.mostRecentPlayerID = 0;

        this.tableCardsPile = new ArrayList<ACard>();
        this.examiningCards = new ArrayList<ACard>();
        this.hiddenCard = null;

        this.currentRoundNumber = 0;

        return;
    }

    // region Game Core methods

    /**
     * <p>Updates the list of winners based on the current highest affection.</p>
     *
     * @param winners Current players with the highest affection.
     * @param highestAffection Current highest affection.
     * @param PC Player to check and compare with the current highest affection.
     * @param currentAffection Current affection of the provided player to check.
     * @return The updated highest affection.
     */
    private static int updateRoundWinnerListBasedOnAffection(
            ArrayList<PlayerController> winners, int highestAffection, PlayerController PC, int currentAffection
    ) {
        if (currentAffection > highestAffection) {
            highestAffection = currentAffection;
            winners.clear();
            winners.add(PC);
            return highestAffection;
        }

        if (currentAffection == highestAffection) {
            winners.add(PC);
            return highestAffection;
        }

        return highestAffection;
    }

    /**
     * <p>Checks which player has won the round and increases their affection as stated in the rules.</p>
     * <br />
     * <p>The player with the highest affection on their hand wins the round.<br />
     * If there is a tie, the player with the highest sum of affection in their discard pile wins the round.<br />
     * If there is still a tie, all tied players win the round and get their affection increased by one.</p>
     */
    private void applyRoundWinBonusToPlayers() {
        ArrayList<PlayerController> playersWithHighestAffection = new ArrayList<PlayerController>(0);
        int highestAffection = -1;
        for (PlayerController PC : this.playerControllers) {
            if (PC.isKnockedOut())
                continue;

            int currentAffection;
            if (PC.handCard == null)
                currentAffection = PC.getAffectionOfLatestDiscardedCard();
            else
                currentAffection = PC.handCard.getAffection();

            highestAffection = GameMode.updateRoundWinnerListBasedOnAffection(
                    playersWithHighestAffection, highestAffection, PC, currentAffection);
            continue;
        }

        if (playersWithHighestAffection.isEmpty())
            throw new RuntimeException("Beyond the impossible.");

        if (playersWithHighestAffection.size() == 1) {
            playersWithHighestAffection.get(0).increaseAffection();
            this.mostRecentRoundWinners = playersWithHighestAffection;
            return;
        }

        ArrayList<PlayerController> playerWithHighestAffectionInDiscardPile = new ArrayList<PlayerController>(0);
        int highestDiscardPileAffection = -1;
        for (PlayerController PC : playersWithHighestAffection) {
            int currentAffection = PC.getSumOfAffectionInDiscardPile();
            highestDiscardPileAffection =
                    updateRoundWinnerListBasedOnAffection(
                            playerWithHighestAffectionInDiscardPile, highestDiscardPileAffection, PC, currentAffection
                    );
            continue;
        }

        for (PlayerController PC : playerWithHighestAffectionInDiscardPile)
            PC.increaseAffection();

        this.mostRecentRoundWinners = playerWithHighestAffectionInDiscardPile;

        return;
    }

    /**
     * <p>A player wins the game if they have gathered enough affection tokens throughout the game.</p>
     * <br />
     * <p>As written in the rules, the amount of affection tokens needed is based on the amount of players:</p>
     * <ul>
     *  <li>2 players: 7 affection tokens
     *  <li>3 players: 5 affection tokens
     *  <li>4 players: 4 affection tokens
     * </ul>
     *
     * @return True if a player has won the game, false otherwise.
     */
    private boolean hasAPlayerWonGame() {
        this.gameWinners = new ArrayList<PlayerController>(0);
        for (PlayerController PC : this.playerControllers) {

            if (this.getPlayerCount() == 2) {
                if (PC.getAffectionTokens() >= GameMode.PLAYER_COUNT_TWO_TOKENS_TO_WIN)
                    this.gameWinners.add(PC);

                continue;
            }

            if (this.getPlayerCount() == 3) {
                if (PC.getAffectionTokens() >= GameMode.PLAYER_COUNT_THREE_TOKENS_TO_WIN)
                    this.gameWinners.add(PC);

                continue;
            }

            if (this.getPlayerCount() == 4) {
                if (PC.getAffectionTokens() >= GameMode.PLAYER_COUNT_FOUR_TOKENS_TO_WIN)
                    this.gameWinners.add(PC);

                continue;
            }

            throw new IllegalArgumentException("Invalid player count.");
        }

        if (this.gameWinners.isEmpty()) {
            this.gameWinners = null;
            return false;
        }

        return true;
    }

    /**
     * <p>Selects the next valid player in the order of the player array.<br />
     * Valid meaning that the player is not knocked out nor has a message for the next turn.</p>
     *
     * @return The state of the game after selecting the next player.
     * @see appv2.core.EGameModeState
     */
    public EGameModeState selectNextValidPlayer() {
        if (this.tableCardsPile.isEmpty() || this.getRemainingPlayerCount() < 2) {
            this.applyRoundWinBonusToPlayers();
            if (this.hasAPlayerWonGame())
                return EGameModeState.GAME_ENDED;
            return EGameModeState.ROUND_ENDED;
        }

        int nextPlayerID = this.mostRecentPlayerID + 1;
        if (nextPlayerID >= this.getPlayerCount())
            nextPlayerID = 0;

        this.mostRecentPlayerID = nextPlayerID;

        if (
            this.getPlayerControllerByID(this.mostRecentPlayerID).isKnockedOut()
            && (
                this.getPlayerControllerByID(this.mostRecentPlayerID).getMessageForPlayerNextTurn() == null
                || this.getPlayerControllerByID(this.mostRecentPlayerID).getMessageForPlayerNextTurn().isEmpty()
            )
        )
            return this.selectNextValidPlayer();

        return EGameModeState.GAME_RUNNING;
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

        // TODO: Select the starting player as stated in the rules.
        this.mostRecentPlayerID = 0;
        this.mostRecentRoundWinners = null;

        this.currentRoundNumber++;

        return;
    }

    /**
     * <p>Draws a card from the deck.<br />
     * If the deck is empty, the hidden card will be returned.</p>
     *
     * @return The drawn card.
     */
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
     * <p>Also prepares the hidden card and the examining cards.</p>
     *
     * @throws RuntimeException If the table cards pile size is not 16.
     */
    private void prepareTableCardsPile() throws RuntimeException {
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

    /**
     * @return The number of players in this game.
     */
    public int getPlayerCount() {
        return this.playerControllers.length;
    }

    /**
     * @return The names of all players in the game.
     */
    public String[] getPlayerNames() {
        return Arrays.stream(this.playerControllers)
                .map(PlayerController::getPlayerName)
                .toArray(String[]::new);
    }

    /**
     * <p>Gets the PlayerController of the player with the provided index in the player array.</p>
     *
     * @param playerID The identifier of the player to get the PlayerController of.
     * @return The PlayerController of the player with the provided ID.
     */
    public PlayerController getPlayerControllerByID(int playerID) {
        return this.playerControllers[playerID];
    }

    /**
     * <p>Gets the PlayerController of the player with the provided name.</p>
     *
     * @param playerName The name of the player to get the PlayerController of.
     * @return The PlayerController of the player with the provided name.
     * @throws RuntimeException If the player name is not found.
     */
    public PlayerController getPlayerControllerByName(String playerName) throws RuntimeException {
        for (PlayerController PC : this.playerControllers) {
            if (PC.getPlayerName().equals(playerName))
                return PC;
        }

        throw new RuntimeException("Player name not found.");
    }

    /**
     * @return The PlayerController of the player who played the last turn.
     */
    public PlayerController getMostRecentPlayerController() {
        return this.getPlayerControllerByID(this.mostRecentPlayerID);
    }

    /**
     * @return The current table cards pile (does not include hidden card).
     */
    public ArrayList<ACard> getTableCardsPile() {
        return this.tableCardsPile;
    }

    /**
     * @return The current examining cards if available (applies only to 2-player games).
     */
    public ArrayList<ACard> getExaminingCards() {
        return this.examiningCards;
    }

    /**
     * @return The current hidden card if available, null otherwise.
     */
    public ACard getHiddenCard() {
        return this.hiddenCard;
    }

    /**
     * <p>Return the current remaining Player Controllers in a round. Does not
     * include Player Controllers that are knocked out but waiting for a message.</p>
     *
     * @return The current remaining player controllers.
     * @see appv2.core.PlayerController#getMessageForPlayerNextTurn()
     */
    public PlayerController[] getRemainingPlayerControllers() {
        return Arrays.stream(this.playerControllers)
                .filter(PC -> !PC.isKnockedOut())
                .toArray(PlayerController[]::new);
    }

    /**
     * <p>Return the current remaining player count in a round. Does not
     * include players that are knocked out but waiting for a message.</p>
     *
     * @return The current remaining player count.
     * @see #getRemainingPlayerControllers()
     * @see appv2.core.PlayerController#getMessageForPlayerNextTurn()
     */
    public int getRemainingPlayerCount() {
        return this.getRemainingPlayerControllers().length;
    }

    /**
     * @return The current remaining table cards count.
     */
    public int getRemainingTableCardsCount() {
        return this.tableCardsPile.size();
    }

    /**
     * @return True if the hidden card is available, false otherwise.
     */
    public boolean isHiddenCardAvailable() {
        return this.hiddenCard != null;
    }

    /**
     * @return The current Player Controllers participating in this game.
     */
    public PlayerController[] getPlayerControllers() {
        return this.playerControllers;
    }

    /**
     * <p>Includes the round winners of last round.</p>
     * <p><b>Warning:</b> This maybe empty.</p>
     *
     * @return The current winners of the game.
     */
    public ArrayList<PlayerController> getMostRecentRoundWinners() {
        return this.mostRecentRoundWinners;
    }

    /**
     * @return The current winners of the game as a string.
     *         "NONE" if no winners are available.
     */
    public String getMostRecentRoundWinnersAsString() {
        if (this.mostRecentRoundWinners == null)
            return "NONE";

        if (this.mostRecentRoundWinners.size() == 1)
            return String.format("%s",
                    this.mostRecentRoundWinners.get(0).getPlayerName());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mostRecentRoundWinners.size(); i++) {
            if (i == this.mostRecentRoundWinners.size() - 1)
                sb.append("and ");
            sb.append(this.mostRecentRoundWinners.get(i).getPlayerName());
            if (i != this.mostRecentRoundWinners.size() - 1)
                sb.append(", ");
        }

        return sb.toString();
    }

    /**
     * @return All Player Controllers participating in this game
     *         sorted by their affection in descending order.
     */
    public PlayerController[] getPlayerControllerByDSCAffection() {
        PlayerController[] pcs = this.playerControllers.clone();
        Arrays.sort(pcs, (a, b) -> b.getAffectionTokens() - a.getAffectionTokens());

        return pcs;
    }

    /**
     * @return The tokens of affection needed to win the game.
     * @throws IllegalStateException If the player count is invalid.
     */
    public int getAmountOfTokensOfAffectionToWin() throws IllegalStateException {
        if (this.getPlayerCount() == 2)
            return GameMode.PLAYER_COUNT_TWO_TOKENS_TO_WIN;

        if (this.getPlayerCount() == 3)
            return GameMode.PLAYER_COUNT_THREE_TOKENS_TO_WIN;

        if (this.getPlayerCount() == 4)
            return GameMode.PLAYER_COUNT_FOUR_TOKENS_TO_WIN;

        throw new IllegalStateException("Invalid player count.");
    }

    /**
     * @return The current round number.
     */
    public int getCurrentRoundNumber() {
        return this.currentRoundNumber;
    }

    // endregion Getters and setters

}
