import java.util.Scanner;
import Cards.Card;
import Cards.*;
import java.util.ArrayList;
import java.util.Collections;


public class GameMode {

    private static final int CARDS_AMOUNT_IN_DECK = 16;

    private final Scanner scanner;
    private final int playerCount;

    /**
     * As written in the rules. <p>
     * This card is not in the deck and is removed at the beginning of each round (Applies only to 2-player games). <p>
     * <b>Warning:</b> This may be null.
     * */
    private Card examinedCard;
    private final ArrayList<Card> deckOnTable;

    private final PlayerController[] players;
    private int mostRecentPlayerID;

    public GameMode(Scanner scanner, int playerCount, String[] playerNames) {
        this.scanner = scanner;
        this.playerCount = playerCount;
        this.deckOnTable = new ArrayList<Card>(0);

        this.players = new PlayerController[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            this.players[i] = new PlayerController(i, playerNames[i]);
        }
        this.mostRecentPlayerID = 0;

        return;
    }

    // region Game helper functions

    private void PrintArrayList(ArrayList<PlayerController> list, String sep) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%s", list.get(i).GetPlayerName());
            if (i != list.size() - 1)
                System.out.printf("%s", sep);
            continue;
        }
        System.out.print("!\n");
    }

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

        PrintArrayList(winnersForSumOfAffectionInDiscardPile, ", ");

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
            System.out.print("But the other players also played pretty well:\n");
            this.PrintAffectionOfPlayers(false);
            System.out.print("\ngg\n");

            return true;
        }

        if (winners.size() > 1) {
            System.out.print("The winners are ");
            PrintArrayList(winners, ", ");
            System.out.print("But the other players also played pretty well:\n");
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
        this.examinedCard = null;

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
        if (this.playerCount == 2)
            this.examinedCard = this.deckOnTable.remove(0);

        return;
    }

    private void PrepareNewRound() {
        this.SetupTableDeck();


        // As written in the rules. Each player will have one card in hand at the beginning of each round.
        for (int i = 0; i < this.playerCount; i++) {
            this.players[this.mostRecentPlayerID].SetCardInHand(this.deckOnTable.remove(0));
            this.SelectNextPlayer();
        }

        return;
    }


    private void StartNewRound() {
        this.PrepareNewRound();

        while (!this.deckOnTable.isEmpty()) {
            this.StartNewTurn();
            this.SelectNextPlayer();
            continue;
        }

        return;
    }

    private void StartNewTurn() {
        Card cardFromDeck = this.deckOnTable.remove(0);
        this.players[this.mostRecentPlayerID].PlayTurn(this.scanner, cardFromDeck);

        return;
    }

    public int GetRemainingDeckSize() {
        return this.deckOnTable.size();
    }

    public Card GetExaminedCard() {
        return this.examinedCard;
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

    private void ResetForNewRound() {
        for (PlayerController PC : this.players)
            PC.ResetForNewRound();

        this.deckOnTable.clear();
        this.examinedCard = null;

        return;
    }
}
