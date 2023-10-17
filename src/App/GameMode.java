package App;

import App.Cards.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;


public class GameMode {

    private static final int CARDS_AMOUNT_IN_DECK = 16;

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
     * This card is removed from the top of the deck and placed faced-down on the table. <p>
     * <b>Warning:</b> This may be null.
     */
    private Card hiddenCard;
    private final ArrayList<Card> deckOnTable;

    private final PlayerController[] players;
    private int mostRecentPlayerID;

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

    private int UpdateRoundWinnersListBasedOnAffection(
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
     * Check which player has won the round and increases his affection. <p>
     * The player with the highest affection on their hand wins the round. <p>
     * If there is a tie, the player with the highest sum of affection in their discard pile wins the round. <p>
     * If there is still a tie, all tied players win the round and get their affection increased by one. <p>
     */
    private void CheckForPlayerRoundWin() {
        ArrayList<PlayerController> winners = new ArrayList<PlayerController>(0);
        ArrayList<PlayerController> winnersForSumOfAffectionInDiscardPile = new ArrayList<PlayerController>(0);

        int highestAffection = -1;
        for (PlayerController PC : this.players) {
            int currentAffection = PC.GetCardInHand().GetAffection();
            highestAffection = UpdateRoundWinnersListBasedOnAffection(winners, highestAffection, PC, currentAffection);
        }

        highestAffection = -1;
        if (winners.size() > 1) {
            for (PlayerController PC : winners) {
                int currentAffection = PC.GetSumOfAffectionInDiscardPile();
                highestAffection =
                        UpdateRoundWinnersListBasedOnAffection(
                                winnersForSumOfAffectionInDiscardPile, highestAffection, PC, currentAffection
                        );
            }
        } else
            winnersForSumOfAffectionInDiscardPile = winners;

        for (PlayerController PC : winnersForSumOfAffectionInDiscardPile)
            PC.IncreaseAffection();

        App.ClearScreen(this.scanner);
        if (winnersForSumOfAffectionInDiscardPile.size() == 1)
            System.out.print("The following player has won the round with ");
        else
            System.out.print("The following players have won the round with ");
        System.out.printf("%s!\n",
                winnersForSumOfAffectionInDiscardPile.get(0).GetCardInHand().GetAsString());
        App.PrintArrayListPC(winnersForSumOfAffectionInDiscardPile, ", ", "!\n");

        return;
    }

    /**
     * A player wins the game if they have gathered enough affection tokens throughout the game. <p>
     * As written in the rules, the amount of affection tokens needed is based on the amount of players: <p>
     *     - 2 players: 7 affection tokens <p>
     *     - 3 players: 5 affection tokens <p>
     *     - 4 players: 4 affection tokens <p>
     */
    private boolean CheckIfPlayerHasWonTheGame() {
        ArrayList<PlayerController> winners = new ArrayList<PlayerController>(0);

        for (PlayerController PC : this.players) {
            if (this.playerCount == 2) {
                if (PC.GetAffection() >= 7)
                    winners.add(PC);

                continue;
            }

            if (this.playerCount == 3) {
                if (PC.GetAffection() >= 5)
                    winners.add(PC);

                continue;
            }

            if (this.playerCount == 4) {
                if (PC.GetAffection() >= 4)
                    winners.add(PC);

                continue;
            }

            throw new IllegalArgumentException("Invalid player count.");
        }

        if (winners.size() == 1) {
            System.out.printf("\n\n**** - ****\nThe winner of the game is %s!\n", winners.get(0).GetPlayerName());
            System.out.print("But the others also played pretty well:\n");
            this.PrintAffectionOfPlayers(false);
            System.out.print("\ngg\n");

            return true;
        }

        if (winners.size() > 1) {
            System.out.print("The winners are ");
            App.PrintArrayListPC(winners, ", ", "!\n");
            System.out.print("But the others also played pretty well:\n");
            this.PrintAffectionOfPlayers(false);
            System.out.print("\ngg\n");

            return true;
        }

        return false;
    }

    /**
     * Sorts the players by their affection in descending order. <p>
     * Then prints the players in the order they are in the list. <p>
     */
    public void PrintAffectionOfPlayers(boolean bNewLine) {
        ArrayList<PlayerController> playersSortedByAffection = new ArrayList<PlayerController>(0);
        for (PlayerController PC : this.players) {
            int i = 0;
            for (; i < playersSortedByAffection.size(); i++) {
                if (PC.GetAffection() > playersSortedByAffection.get(i).GetAffection())
                    break;
                continue;
            }

            playersSortedByAffection.add(i, PC);
            continue;
        }

        System.out.println("The current affection of all players is: ");
        for (int i = 0; i < playersSortedByAffection.size(); i++) {
            System.out.printf("%s: %d",
                    playersSortedByAffection.get(i).GetPlayerName(), playersSortedByAffection.get(i).GetAffection());
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

    // endregion Game helper functions

    public void StartGame() {
        System.out.println("Game Started.");

        while (true) {
            // TODO: Select first player as it is written in the rules. We select the index 0 as the first player.
            this.mostRecentPlayerID = 0;

            this.StartNewRound();

            this.CheckForPlayerRoundWin();
            boolean bAPlayerWonTheGame = this.CheckIfPlayerHasWonTheGame();
            if (bAPlayerWonTheGame)
                break;

            this.ResetForNewRound();
            this.PrintAffectionOfPlayers(true);

            System.out.println("Press enter to start the next round: ");
            scanner.nextLine();
            continue;
        }

        return;
    }

    private void SelectNextPlayer() {
        this.mostRecentPlayerID = (this.mostRecentPlayerID + 1) % this.playerCount;
        if (this.players[this.mostRecentPlayerID].GetIsKnockedOut()) {
            if (this.players[this.mostRecentPlayerID].GetSignalPlayerNextTurnAboutKnockout())
                return;

            this.SelectNextPlayer();
        }

        return;
    }

    /**
     * Adds initial cards to deck (16 cards).
     * <p>
     * - Princess Annette (1x) <p>
     * - Countess Wilhelmina (1x) <p>
     * - King Arnaud IV (1x) <p>
     * - Prince Arnaud (2x) <p>
     * - Handmaid Susannah (2x) <p>
     * - Baron Talus (2x) <p>
     * - Priest Tomas (2x) <p>
     * - Guard Odette (5x)
     */
    private void SetupTableDeck() {
        this.deckOnTable.clear();
        this.hiddenCard = null;
        this.examiningCards.clear();

        this.deckOnTable.add(new PrincessAnnette());

        this.deckOnTable.add(new CountessWilhelmina());

        this.deckOnTable.add(new KingArnaud());

        this.deckOnTable.add(new PrinceArnaud());
        this.deckOnTable.add(new PrinceArnaud());

        this.deckOnTable.add(new HandmaidSusannah());
        this.deckOnTable.add(new HandmaidSusannah());

        // this.deckOnTable.add(new BaronTalus());
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
        if (this.playerCount == 2) {
            this.examiningCards.add(this.deckOnTable.remove(0));
            this.examiningCards.add(this.deckOnTable.remove(0));
            this.examiningCards.add(this.deckOnTable.remove(0));
        }

        return;
    }

    private void PrepareNewRound() {
        this.SetupTableDeck();

        // As written in the rules. Each player will have one card in hand at the beginning of each round.
        for (int i = 0; i < this.playerCount; i++) {
            this.players[this.mostRecentPlayerID].SetCardInHand(this.deckOnTable.remove(0));
            this.SelectNextPlayer();

            continue;
        }

        return;
    }

    private void StartNewRound() {
        this.PrepareNewRound();

        while (!this.deckOnTable.isEmpty()) {
            this.StartNewTurn();
            if (this.GetRemainingPlayerCount() == 1)
                break;

            this.SelectNextPlayer();
            continue;
        }

        return;
    }

    private void StartNewTurn() {
        if (this.players[this.mostRecentPlayerID].GetIsKnockedOut()) {
            this.players[this.mostRecentPlayerID].PlayTurn(this.scanner, null);
            return;
        }

        Card cardFromDeck = this.deckOnTable.remove(0);
        this.players[this.mostRecentPlayerID].PlayTurn(this.scanner, cardFromDeck);
        return;
    }

    public int GetRemainingDeckSize() {
        return this.deckOnTable.size();
    }

    public Card GetHiddenCard() {
        return this.hiddenCard;
    }

    public ArrayList<Card> GetExaminingCards() {
        return this.examiningCards;
    }

    public String GetExaminingCardsAsString() {
        StringBuilder examiningCardsAsString = new StringBuilder();
        for (Card card : this.examiningCards)
            examiningCardsAsString.append(card.GetAsString()).append(" | ");

        return examiningCardsAsString.toString();
    }

    public String[][] GetAllDiscardedCards() {
        String[][] discardedCards = new String[this.playerCount][];
        for (int i = 0; i < this.playerCount; i++) {
            discardedCards[i] = this.players[i].GetDiscardedCards();
        }
        return discardedCards;
    }

    public PlayerController GetPlayerByID(int playerID) {
        return this.players[playerID];
    }

    public int GetPlayerCount() {
        return this.playerCount;
    }

    public ArrayList<PlayerController> GetRemainingPlayers() {
        ArrayList<PlayerController> remainingPlayers = new ArrayList<PlayerController>(0);
        for (PlayerController PC : this.players)
            if (!PC.GetIsKnockedOut())
                remainingPlayers.add(PC);

        return remainingPlayers;
    }

    public String[] GetRemainingPlayersAsStringArray() {
        String[] remainingPlayersAsStringArray = new String[this.GetRemainingPlayerCount()];
        int i = 0;
        for (PlayerController PC : this.players)
            if (!PC.GetIsKnockedOut())
                remainingPlayersAsStringArray[i++] = PC.GetPlayerName();

        return remainingPlayersAsStringArray;
    }

    public String GetRemainingPlayersAsString() {
        StringBuilder remainingPlayersAsString = new StringBuilder();
        for (PlayerController PC : this.players)
            if (!PC.GetIsKnockedOut())
                remainingPlayersAsString.append(PC.GetPlayerName()).append(" | ");

        return remainingPlayersAsString.toString();
    }

    public int GetRemainingPlayerCount() {
        int remainingPlayerCount = 0;
        for (PlayerController PC : this.players)
            if (!PC.GetIsKnockedOut())
                remainingPlayerCount++;

        return remainingPlayerCount;
    }

    public PlayerController GetPlayerControllerFromName(String playerName) {
        for (PlayerController PC : this.players)
            if (PC.GetPlayerName().equals(playerName))
                return PC;

        throw new IllegalArgumentException("Invalid player name.");
    }

    public void SwapHands(PlayerController PC1, PlayerController PC2) {
        Card cardInHandPC1 = PC1.GetCardInHand();
        Card cardInHandPC2 = PC2.GetCardInHand();

        PC1.SetCardInHand(cardInHandPC2);
        PC2.SetCardInHand(cardInHandPC1);

        return;
    }

    public Card DrawCard() {
        if (this.deckOnTable.isEmpty()) {
            Card card = this.hiddenCard;
            this.hiddenCard = null;
            return card;
        }

        return this.deckOnTable.remove(0);
    }

    private void ResetForNewRound() {
        for (PlayerController PC : this.players)
            PC.ResetForNewRound();

        this.deckOnTable.clear();
        this.hiddenCard = null;
        this.examiningCards.clear();

        return;
    }
}
