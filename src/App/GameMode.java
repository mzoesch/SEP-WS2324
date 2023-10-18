package App;

import App.Cards.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;


public class GameMode {

    private static final int CARD_AMOUNT_IN_DECK = 16;

    private static final int PLAYER_COUNT_TWO_TOKENS_TO_WIN = 7;
    private static final int PLAYER_COUNT_THREE_TOKENS_TO_WIN = 5;
    private static final int PLAYER_COUNT_FOUR_TOKENS_TO_WIN = 4;

    public static final int AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS = 2;

    private final Scanner scanner;
    private final int playerCount;

    /**
     * As written in the rules. <p>
     * These cards are not in the deck and are removed at the beginning of each round
     * and placed faced-up on the table to be examined by everyone (Applies only to 2-player games). <p>
     * <b>Warning:</b> This may be empty.
     */
    private final ArrayList<Card> examiningCards;
    /**
     * As written in the rules. <p>
     * This card is removed from the top of the deck and placed faced-down on the table at the
     * beginning of each round. <p>
     * <b>Warning:</b> This may be null.
     */
    private Card hiddenCard;
    private final ArrayList<Card> deckOnTable;

    private int mostRecentPlayerID;
    private final PlayerController[] players;

    public GameMode(Scanner scanner, int playerCount, String[] playerNames) {
        this.scanner = scanner;
        this.playerCount = playerCount;
        this.deckOnTable = new ArrayList<Card>(0);
        this.examiningCards = new ArrayList<Card>(0);

        this.players = new PlayerController[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            this.players[i] = new PlayerController(i, playerNames[i]);
        }

        this.mostRecentPlayerID = 0;

        return;
    }

    // region Game helper functions

    /**
     * Sorts the players by their affection in descending order. <p>
     * Then prints the players in the order they are in the list. <p>
     */
    public void printAffectionOfPlayers(boolean bNewLine) {
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
     * Checks which player has won the round and increases their affection as stated in the rules. <p>
     *
     * The player with the highest affection on their hand wins the round. <p>
     * If there is a tie, the player with the highest sum of affection in their discard pile wins the round. <p>
     * If there is still a tie, all tied players win the round and get their affection increased by one. <p>
     */
    private void applyRoundWinBonusToPlayers() {
        ArrayList<PlayerController> playersWithHighestAffection = new ArrayList<PlayerController>(0);
        int highestAffection = -1;
        for (PlayerController PC : this.players) {
            int currentAffection = PC.getCardInHand().getAffection();
            highestAffection = updateRoundWinnersListBasedOnAffection(
                    playersWithHighestAffection, highestAffection, PC, currentAffection);
            continue;
        }

        if (playersWithHighestAffection.size() == 1) {
            playersWithHighestAffection.get(0).increaseAffection();

            App.clearStdOut();
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

        App.clearStdOut();
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
     * A player wins the game if they have gathered enough affection tokens throughout the game. <p>
     * As written in the rules, the amount of affection tokens needed is based on the amount of players: <p>
     *     - 2 players: 7 affection tokens <p>
     *     - 3 players: 5 affection tokens <p>
     *     - 4 players: 4 affection tokens <p>
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

            System.out.print("But don't worry. All player played pretty well:\n");
            this.printAffectionOfPlayers(false);
            System.out.print("\ngg\n");

            return true;
        }

        if (winners.size() > 1) {
            System.out.print(
                    "\n\n******************** - * - ********************\nThe winners of the game are ");
            App.printArray_V2(
                    winners.stream().map(PlayerController::getPlayerName).toArray(String[]::new), ", ", "!\n");

            System.out.print("But the others also played pretty well:\n");
            this.printAffectionOfPlayers(false);
            System.out.print("\ngg\n");

            return true;
        }

        return false;
    }

    private void resetForNewRound() {
        for (PlayerController PC : this.players)
            PC.resetForNewRound();

        this.deckOnTable.clear();
        this.hiddenCard = null;
        this.examiningCards.clear();

        return;
    }

    // endregion Game helper functions

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
            this.printAffectionOfPlayers(true);

            System.out.println("Press enter to start the next round: ");
            scanner.nextLine();
            continue;
        }

        return;
    }

    // region Round helper functions

    /**
     * Adds initial cards to table deck (16 cards).<p>
     * - Princess Annette (1x) <p>
     * - Countess Wilhelmina (1x) <p>
     * - King Arnaud IV (1x) <p>
     * - Prince Arnaud (2x) <p>
     * - Handmaid Susannah (2x) <p>
     * - Baron Talus (2x) <p>
     * - Priest Tomas (2x) <p>
     * - Guard Odette (5x)
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
        this.hiddenCard = this.deckOnTable.remove(0);
        if (this.playerCount == GameMode.AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS) {
            this.examiningCards.add(this.deckOnTable.remove(0));
            this.examiningCards.add(this.deckOnTable.remove(0));
            this.examiningCards.add(this.deckOnTable.remove(0));
        }

        return;
    }

    private void selectNextValidPlayer() {
        this.mostRecentPlayerID = (this.mostRecentPlayerID + 1) % this.playerCount;

        if (this.players[this.mostRecentPlayerID].getKnockedOut()) {
            if (this.players[this.mostRecentPlayerID].getSignalPlayerNextTurnAboutKnockout())
                return;

            this.selectNextValidPlayer();
        }

        return;
    }

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

    public Card drawCard() {
        if (this.deckOnTable.isEmpty()) {
            if (this.hiddenCard == null)
                throw new RuntimeException("There are no cards left in the deck.");

            Card card = this.hiddenCard;
            this.hiddenCard = null;

            return card;
        }

        return this.deckOnTable.remove(0);
    }

    // endregion Turn helper functions

    private void startNewTurn() {
        if (this.players[this.mostRecentPlayerID].getKnockedOut()) {
            this.players[this.mostRecentPlayerID].playTurn(this.scanner, null);

            return;
        }

        this.players[this.mostRecentPlayerID].playTurn(this.scanner, this.drawCard());
        return;
    }

    // region Utility functions

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int getRemainingPlayerCount() {
        int remainingPlayerCount = 0;
        for (PlayerController PC : this.players)
            if (!PC.getKnockedOut())
                remainingPlayerCount++;

        return remainingPlayerCount;
    }

    public String[] getRemainingPlayerNames() {
        String[] remainingPlayerNames = new String[this.getRemainingPlayerCount()];
        int i = 0;
        for (PlayerController PC : this.players)
            if (!PC.getKnockedOut())
                remainingPlayerNames[i++] = PC.getPlayerName();

        return remainingPlayerNames;
    }

    public PlayerController[] getRemainingPlayers() {
        PlayerController[] remainingPlayers = new PlayerController[this.getRemainingPlayerCount()];
        int i = 0;
        for (PlayerController PC : this.players)
            if (!PC.getKnockedOut())
                remainingPlayers[i++] = PC;

        return remainingPlayers;
    }

    public int getCurrentDeckSize() {
        return this.deckOnTable.size();
    }

    public Card getHiddenCard() {
        return this.hiddenCard;
    }

    public Card[] getExaminingCards() {
        return this.examiningCards.toArray(new Card[0]);
    }

    public String[] getExaminingCardsAsString() {
        String[] examiningCards = new String[this.examiningCards.size()];
        for (int i = 0; i < this.examiningCards.size(); i++)
            examiningCards[i] = this.examiningCards.get(i).getAsString();

        return examiningCards;
    }

    public String getExaminingCardsAsOneString() {
        StringBuilder examiningCardsAsString = new StringBuilder();
        for (Card card : this.examiningCards)
            examiningCardsAsString.append(card.getAsString()).append(" | ");

        return examiningCardsAsString.toString();
    }

    public String[] getAffectionOfAllPlayerAsString() {
        String[] affectionOfAllPlayer = new String[this.playerCount];
        for (int i = 0; i < this.playerCount; i++)
            affectionOfAllPlayer[i] = String.format("%s: %d",
                    this.players[i].getPlayerName(), this.players[i].getAffection());

        return affectionOfAllPlayer;
    }

    public PlayerController getPlayerControllerByID(int playerID) {
        return this.players[playerID];
    }

    public PlayerController getPlayerControllerByName(String playerName) {
        for (PlayerController PC : this.players)
            if (PC.getPlayerName().equals(playerName))
                return PC;

        throw new IllegalArgumentException("Invalid player name.");
    }

    public void swapHands(PlayerController PC1, PlayerController PC2) {
        Card cardInHandPC1 = PC1.getCardInHand();
        Card cardInHandPC2 = PC2.getCardInHand();

        PC1.setCardInHand(cardInHandPC2);
        PC2.setCardInHand(cardInHandPC1);

        return;
    }

    // endregion Utility functions

}
