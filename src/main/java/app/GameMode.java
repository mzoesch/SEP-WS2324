package app;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

import app.cards.*;


/**
 * <p>The rules of the game are implemented here. It is a high-level manager object for
 * the game and controls the overall flow of the game.</p>
 * <p>Is spawned at the beginning of the application and not destroyed until the
 * application is killed or a completely new game is started.</p>
 * <br />
 * <p>On the most basic level, these rules include:</p>
 * <ul>
 *  <li>The number of players present in the game.
 *  <li>The order of all valid players and calling their turns.
 *  <li>The amount of cards and the order them in the deck.
 *  <li>Investigating the game winner.
 * </ul>
 * @see app.GameInstance#run()
 */
public class GameMode {

    private static final int CARD_AMOUNT_IN_DECK = 16;

    private static final int PLAYER_COUNT_TWO_TOKENS_TO_WIN = 7;
    private static final int PLAYER_COUNT_THREE_TOKENS_TO_WIN = 5;
    private static final int PLAYER_COUNT_FOUR_TOKENS_TO_WIN = 4;

    public static final int AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS = 2;

    private final Scanner scanner;
    private final int playerCount;

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
    private final ArrayList<ACard> deckOnTable;

    /**
     * <p>The ID of the player who played the last turn.</p>
     */
    private int mostRecentPlayerID;
    private final PlayerController[] players;

    /**
     * <p>Constructor.</p>
     *
     * @param scanner Scanner object to inherit to other objects.
     * @param playerCount The amount of players in this game.
     * @param playerNames The names of all players in the game.
     */
    public GameMode(Scanner scanner, int playerCount, String[] playerNames) {
        this.scanner = scanner;
        this.playerCount = playerCount;
        this.deckOnTable = new ArrayList<ACard>(0);
        this.examiningCards = new ArrayList<ACard>(0);

        this.players = new PlayerController[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            this.players[i] = new PlayerController(i, playerNames[i]);
        }

        this.mostRecentPlayerID = 0;

        return;
    }

    // region Game helper functions
    
    /**
     * <p>Prints the affection of all players in descending order.</p>
     *
     * @param bNewLine If true, a new line will be printed after each player's affection.
     */
    public void printAffectionOfPlayersDSC(boolean bNewLine) {
        ArrayList<PlayerController> playersSortedByAffection = new ArrayList<PlayerController>(0);
        for (PlayerController PC : this.players) {
            int i = 0;
            for (; i < playersSortedByAffection.size(); i++) {
                if (PC.getAffection() > playersSortedByAffection.get(i).getAffection())
                    break;
                continue;
            }

            playersSortedByAffection.add(i, PC);
            continue;
        }

        System.out.println("The current affection of all players is: ");
        for (int i = 0; i < playersSortedByAffection.size(); i++) {
            System.out.printf("%s: %d",
                    playersSortedByAffection.get(i).getPlayerName(), playersSortedByAffection.get(i).getAffection());
            if (i != playersSortedByAffection.size() - 1) {
                if (bNewLine) {
                    System.out.print("\n");
                    continue;
                }

                System.out.print(", ");
                continue;
            }

            continue;
        }
        System.out.print(".\n");

        return;
    }

    /**
     * <p>Updates the list of winners based on the current highest affection.</p>
     *
     * @param winners Current players with the highest affection.
     * @param highestAffection Current highest affection.
     * @param PC Player to check and compare with the current highest affection.
     * @param currentAffection Current affection of the provided player to check.
     * @return The updated highest affection.
     */
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

    /**
     * <p>Checks which player has won the round and increases their affection as stated in the rules.</p>
     * <br />
     * <p>The player with the highest affection on their hand wins the round.<br />
     * If there is a tie, the player with the highest sum of affection in their discard pile wins the round.<br />
     * If there is still a tie, all tied players win the round and get their affection increased by one.</p>
     * @link GameMode#applyRoundWinBonusToPlayers()
     */
    private void applyRoundWinBonusToPlayers() {
        ArrayList<PlayerController> playersWithHighestAffection = new ArrayList<PlayerController>(0);
        int highestAffection = -1;
        for (PlayerController PC : this.players) {
            if (PC.isKnockedOut())
                continue;

            int currentAffection;
            if (PC.getCardInHand() == null)
                currentAffection = PC.getAffectionOfLatestDiscardedCard();
            else
                currentAffection = PC.getCardInHand().getAffection();

            highestAffection = updateRoundWinnersListBasedOnAffection(
                    playersWithHighestAffection, highestAffection, PC, currentAffection);
            continue;
        }

        if (playersWithHighestAffection.size() == 1) {
            playersWithHighestAffection.get(0).increaseAffection();

            App.flushStdOut();
            if (playersWithHighestAffection.get(0).getCardInHand() == null)
                System.out.printf("The following player has won the round with %s!\n",
                        playersWithHighestAffection.get(0).getLatestCardOfDiscardedPile().getAsString());
            else
                System.out.printf("The following player has won the round with %s!\n",
                        playersWithHighestAffection.get(0).getCardInHand().getAsString());

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

        App.flushStdOut();
        if (playerWithHighestAffectionInDiscardPile.size() == 1) {
            System.out.printf("The following player has won the round with their discard pile of %d: %s\n",
                    playerWithHighestAffectionInDiscardPile.get(0).getSumOfAffectionInDiscardPile(),
                    playerWithHighestAffectionInDiscardPile.get(0).getPlayerName());
            return;
        }
        System.out.printf("The following players have won the round with their discard pile of %d:\n",
                playerWithHighestAffectionInDiscardPile.get(0).getSumOfAffectionInDiscardPile());
        App.printArray_V2(
                playerWithHighestAffectionInDiscardPile.stream()
                        .map(PlayerController::getPlayerName).toArray(String[]::new),
                ", ",
                "!\n"
        );

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
    private boolean checkIfAPlayerHasWonTheGame() {
        ArrayList<PlayerController> winners = new ArrayList<PlayerController>(0);

        for (PlayerController PC : this.players) {
            if (this.playerCount == 2) {
                if (PC.getAffection() >= GameMode.PLAYER_COUNT_TWO_TOKENS_TO_WIN)
                    winners.add(PC);

                continue;
            }

            if (this.playerCount == 3) {
                if (PC.getAffection() >= GameMode.PLAYER_COUNT_THREE_TOKENS_TO_WIN)
                    winners.add(PC);

                continue;
            }

            if (this.playerCount == 4) {
                if (PC.getAffection() >= GameMode.PLAYER_COUNT_FOUR_TOKENS_TO_WIN)
                    winners.add(PC);

                continue;
            }

            throw new IllegalArgumentException("Invalid player count.");
        }

        if (winners.size() == 1) {
            System.out.printf(
                    "\n\n******************** - * - ********************\nThe winner of the game is %s!\n",
                    winners.get(0).getPlayerName()
            );

            System.out.print("But don't worry. Everyone played pretty well!\n");
            this.printAffectionOfPlayersDSC(false);
            System.out.print("\ngg\n");

            return true;
        }

        if (winners.size() > 1) {
            System.out.print(
                    "\n\n******************** - * - ********************\nThe winners of the game are ");
            App.printArray_V2(
                    winners.stream().map(PlayerController::getPlayerName).toArray(String[]::new), ", ", "!\n");

            System.out.print("But the others also played pretty well!\n");
            this.printAffectionOfPlayersDSC(false);
            System.out.print("\ngg\n");

            return true;
        }

        return false;
    }

    /**
     * <p>Resets the game for a new round.</p>
     */
    private void resetForNewRound() {
        for (PlayerController PC : this.players)
            PC.resetForNewRound();

        this.deckOnTable.clear();
        this.hiddenCard = null;
        this.examiningCards.clear();

        return;
    }

    // endregion Game helper functions

    /**
     * <p>Game loop for one single game.</p>
     */
    public void startGame() {
        this.resetForNewRound();

        while (true) {
            // TODO: Select first player as it is written in the rules. We select the index 0 as the first player.
            this.mostRecentPlayerID = 0;

            this.startNewRound();

            this.applyRoundWinBonusToPlayers();
            boolean bAPlayerWonTheGame = this.checkIfAPlayerHasWonTheGame();
            if (bAPlayerWonTheGame)
                break;

            this.resetForNewRound();
            this.printAffectionOfPlayersDSC(true);

            System.out.println("Press enter to start the next round: ");
            scanner.nextLine();
            continue;
        }

        return;
    }

    // region Round helper functions

    /**
     * <p>Adds initial cards to table deck (16 cards):</p>
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
    private void setupTableDeck() {
        this.deckOnTable.add(new PrincessAnnette());

        this.deckOnTable.add(new CountessWilhelmina());

        this.deckOnTable.add(new KingArnaud());

        this.deckOnTable.add(new PrinceArnaud());
        this.deckOnTable.add(new PrinceArnaud());

        this.deckOnTable.add(new HandmaidSusannah());
        this.deckOnTable.add(new HandmaidSusannah());

        this.deckOnTable.add(new BaronTalus());
        this.deckOnTable.add(new BaronTalus());

        this.deckOnTable.add(new PriestTomas());
        this.deckOnTable.add(new PriestTomas());

        this.deckOnTable.add(new GuardOdette());
        this.deckOnTable.add(new GuardOdette());
        this.deckOnTable.add(new GuardOdette());
        this.deckOnTable.add(new GuardOdette());
        this.deckOnTable.add(new GuardOdette());

        Collections.shuffle(this.deckOnTable);
        if (this.deckOnTable.size() != GameMode.CARD_AMOUNT_IN_DECK)
            throw new RuntimeException("The deck does not contain the correct amount of cards.");

        this.hiddenCard = this.deckOnTable.remove(0);
        if (this.playerCount == GameMode.AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS) {
            this.examiningCards.add(this.deckOnTable.remove(0));
            this.examiningCards.add(this.deckOnTable.remove(0));
            this.examiningCards.add(this.deckOnTable.remove(0));
        }

        return;
    }

    /**
     * <p>Selects the next valid player in the order of the player array.</p>
     */
    private void selectNextValidPlayer() {
        this.mostRecentPlayerID = (this.mostRecentPlayerID + 1) % this.playerCount;

        if (this.players[this.mostRecentPlayerID].isKnockedOut()) {
            if (this.players[this.mostRecentPlayerID].isSignalPlayerNextTurnAboutKnockout())
                return;

            this.selectNextValidPlayer();
        }

        return;
    }

    /**
     * <p>Prepares a new round by setting up the table deck and giving each player a card in hand.</p>
     */
    private void prepareNewRound() {
        this.setupTableDeck();

        // As written in the rules. Each player will have one card in hand at the beginning of each round.
        for (int i = 0; i < this.playerCount; i++) {
            this.players[this.mostRecentPlayerID].setCardInHand(this.drawCard());
            this.selectNextValidPlayer();

            continue;
        }

        return;
    }

    // endregion Round helper functions

    /**
     * <p>Round loop for one single round until the deck is empty or not more than one player is remaining.</p>
     */
    private void startNewRound() {
        this.prepareNewRound();

        while (!this.deckOnTable.isEmpty()) {
            this.startNewTurn();
            if (this.getRemainingPlayerCount() < 2)
                break;

            this.selectNextValidPlayer();
            continue;
        }

        return;
    }

    // region Turn helper functions

    /**
     * <p>Draws a card from the deck.<br />
     * If the deck is empty, the hidden card will be returned.</p>
     *
     * @return The drawn card.
     */
    public ACard drawCard() {
        if (this.deckOnTable.isEmpty()) {
            if (this.hiddenCard == null)
                throw new RuntimeException("There are no cards left in the deck.");

            ACard card = this.hiddenCard;
            this.hiddenCard = null;

            return card;
        }

        return this.deckOnTable.remove(0);
    }

    // endregion Turn helper functions

    /**
     * <p>Starts a new turn for the next valid player and draws a card for them.</p>
     */
    private void startNewTurn() {
        if (this.players[this.mostRecentPlayerID].isKnockedOut()) {
            this.players[this.mostRecentPlayerID].playTurn(this.scanner, null);

            return;
        }

        this.players[this.mostRecentPlayerID].playTurn(this.scanner, this.drawCard());
        return;
    }

    // region Utility functions

    /**
     * @return The amount of players in this game.
     */
    public int getPlayerCount() {
        return this.playerCount;
    }

    /**
     * @return The amount of all remaining players in a round.
     */
    public int getRemainingPlayerCount() {
        int remainingPlayerCount = 0;
        for (PlayerController PC : this.players)
            if (!PC.isKnockedOut())
                remainingPlayerCount++;

        return remainingPlayerCount;
    }

    /**
     * @return The names of all remaining players in a round.
     */
    public String[] getRemainingPlayerNames() {
        String[] remainingPlayerNames = new String[this.getRemainingPlayerCount()];
        int i = 0;
        for (PlayerController PC : this.players)
            if (!PC.isKnockedOut())
                remainingPlayerNames[i++] = PC.getPlayerName();

        return remainingPlayerNames;
    }

    /**
     * @return The PlayerControllers of all remaining players in a round.
     */
    public PlayerController[] getRemainingPlayers() {
        PlayerController[] remainingPlayers = new PlayerController[this.getRemainingPlayerCount()];
        int i = 0;
        for (PlayerController PC : this.players)
            if (!PC.isKnockedOut())
                remainingPlayers[i++] = PC;

        return remainingPlayers;
    }

    /**
     * @return The current size of the deck (does not include the hidden card).
     */
    public int getCurrentDeckSize() {
        return this.deckOnTable.size();
    }

    /**
     * @return The current hidden card (maybe null).
     */
    public ACard getHiddenCard() {
        return this.hiddenCard;
    }

    /**
     * @return The current 3 faced-up lying cards on the
     * table (only relevant in 2-player games - maybe empty).
     */
    public ACard[] getExaminingCards() {
        return this.examiningCards.toArray(new ACard[0]);
    }

    /**
     * @return The current 3 faced-up lying cards on the table as
     * Strings (only relevant in 2-player games - maybe empty).
     */
    public String[] getExaminingCardsAsString() {
        String[] examiningCards = new String[this.examiningCards.size()];
        for (int i = 0; i < this.examiningCards.size(); i++)
            examiningCards[i] = this.examiningCards.get(i).getAsString();

        return examiningCards;
    }

    /**
     * @return The current 3 faced-up lying cards on the table as one
     * String (only relevant in 2-player games - maybe empty).
     */
    public String getExaminingCardsAsOneString() {
        StringBuilder examiningCardsAsString = new StringBuilder();
        for (ACard card : this.examiningCards)
            examiningCardsAsString.append(card.getAsString()).append(" | ");

        return examiningCardsAsString.toString();
    }

    /**
     * @return The affection of all players in this game as Strings
     */
    public String[] getAffectionOfAllPlayerAsString() {
        String[] affectionOfAllPlayer = new String[this.playerCount];
        for (int i = 0; i < this.playerCount; i++)
            affectionOfAllPlayer[i] = String.format("%s: %d",
                    this.players[i].getPlayerName(), this.players[i].getAffection());

        return affectionOfAllPlayer;
    }

    /**
     * @param playerID The ID of the player to get the PlayerController of.
     * @return The PlayerController of the player with the provided ID.
     */
    public PlayerController getPlayerControllerByID(int playerID) {
        return this.players[playerID];
    }

    /**
     * @param playerName The name of the player to get the PlayerController of.
     * @return The PlayerController of the player with the provided name.
     */
    public PlayerController getPlayerControllerByName(String playerName) {
        for (PlayerController PC : this.players)
            if (PC.getPlayerName().equals(playerName))
                return PC;

        throw new IllegalArgumentException("Invalid player name.");
    }

    /**
     * <p>Swaps the cards in hand of two players.</p>
     *
     * @param PC1 Player one.
     * @param PC2 Player two.
     */
    public void swapHands(PlayerController PC1, PlayerController PC2) {
        ACard cardInHandPC1 = PC1.getCardInHand();
        ACard cardInHandPC2 = PC2.getCardInHand();

        PC1.setCardInHand(cardInHandPC2);
        PC2.setCardInHand(cardInHandPC1);

        return;
    }

    // endregion Utility functions

}
