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

import java.util.*;


public class GameMode {

    private static final int CARD_AMOUNT_IN_DECK = 16;

    private static final int PLAYER_COUNT_TWO_TOKENS_TO_WIN = 2; // 7
    private static final int PLAYER_COUNT_THREE_TOKENS_TO_WIN = 5;
    private static final int PLAYER_COUNT_FOUR_TOKENS_TO_WIN = 4; // 4

    public static final int AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS = 2;

    private ArrayList<PlayerController> mostRecentRoundWinners;
    private ArrayList<PlayerController> gameWinners;
    private final PlayerController[] playerControllers;
    private int mostRecentPlayerID;

    private final ArrayList<ACard> tableCardsPile;
    private final ArrayList<ACard> examiningCards;
    private ACard hiddenCard;

    private int currentRoundNumber;

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

    // TODO: Static?
    private int updateRoundWinnersListBasedOnAffection(
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

            highestAffection = this.updateRoundWinnersListBasedOnAffection(
                    playersWithHighestAffection, highestAffection, PC, currentAffection);
            continue;
        }

        if (playersWithHighestAffection.isEmpty())
            throw new RuntimeException("No players with highest affection found.");

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
                    updateRoundWinnersListBasedOnAffection(
                            playerWithHighestAffectionInDiscardPile, highestDiscardPileAffection, PC, currentAffection
                    );
            continue;
        }

        for (PlayerController PC : playerWithHighestAffectionInDiscardPile)
            PC.increaseAffection();

        this.mostRecentRoundWinners = playerWithHighestAffectionInDiscardPile;

        return;
    }

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

        if (this.getPlayerControllerByID(this.mostRecentPlayerID).isKnockedOut()
                && (
                        this.getPlayerControllerByID(this.mostRecentPlayerID).getMessageForPlayerNextTurn() == null
                || this.getPlayerControllerByID(this.mostRecentPlayerID).getMessageForPlayerNextTurn().isEmpty()
        ))
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

        this.mostRecentPlayerID = 0;
        this.mostRecentRoundWinners = null;

        this.currentRoundNumber++;

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

        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new KingArnaud());

        this.tableCardsPile.add(new PrinceArnaud());
        this.tableCardsPile.add(new PrinceArnaud());

        this.tableCardsPile.add(new HandmaidSusannah());
        this.tableCardsPile.add(new HandmaidSusannah());

        this.tableCardsPile.add(new BaronTalus());
        this.tableCardsPile.add(new BaronTalus());

        this.tableCardsPile.add(new PriestTomas());
        this.tableCardsPile.add(new PriestTomas());

//        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());
        this.tableCardsPile.add(new GuardOdette());

//        Collections.shuffle(this.tableCardsPile);
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

    public PlayerController[] getRemainingPlayerControllers() {
        return Arrays.stream(this.playerControllers)
                .filter(PC -> !PC.isKnockedOut())
                .toArray(PlayerController[]::new);
    }

    public int getRemainingPlayerCount() {
        return this.getRemainingPlayerControllers().length;
    }

    public int getRemainingTableCardsCount() {
        return this.tableCardsPile.size();
    }

    public boolean isHiddenCardAvailable() {
        return this.hiddenCard != null;
    }

    public PlayerController[] getPlayerControllers() {
        return this.playerControllers;
    }

    public ArrayList<PlayerController> getMostRecentRoundWinners() {
        return this.mostRecentRoundWinners;
    }

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

    public PlayerController[] getPlayerControllerByDSCAffection() {
        PlayerController[] pcs = this.playerControllers.clone();
        Arrays.sort(pcs, (a, b) -> b.getAffectionTokens() - a.getAffectionTokens());

        return pcs;

    }

    public int getAmountOfTokensOfAffectionToWin() {
        if (this.getPlayerCount() == 2)
            return GameMode.PLAYER_COUNT_TWO_TOKENS_TO_WIN;

        if (this.getPlayerCount() == 3)
            return GameMode.PLAYER_COUNT_THREE_TOKENS_TO_WIN;

        if (this.getPlayerCount() == 4)
            return GameMode.PLAYER_COUNT_FOUR_TOKENS_TO_WIN;

        throw new IllegalArgumentException("Invalid player count.");
    }

    public int getCurrentRoundNumber() {
        return this.currentRoundNumber;
    }

    // endregion Getters and setters

}
