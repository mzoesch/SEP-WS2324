package App;

import App.Cards.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;


public class PlayerController {
    private final int playerID;
    private final String playerName;

    private int affection;

    Card cardInHand;
    private final ArrayList<Card> discardedCards;

    private boolean bSignalPlayerNextTurnAboutKnockout;
    private boolean bIsKnockedOut;
    private String MessageForPlayerWhenPlayEffectWasForced;
    private boolean bProtectedByHandmaid;

    static final HashMap<String, String> validCommands = new HashMap<String, String>() {
        {
            // TODO: Show Discarded
            put("help", "Shows this help message.");
            put("playCard", "Plays the card just picked from the deck. ");
            put("playHand", "Discard the card in your hand.");
            put("showHand", "Shows both cards in your hand.");
            put("showScore", "Shows the current score of all players.");
            put("showExaminationCards", "Shows all cards that are currently open for examination.");
            put("showAllDiscarded", "Shows all discarded cards of all players.");
            put("showCardDetails", "Shows the effects of your cards.");
            put("showAllCardDetails", "Shows the effects of all cards.");
            put("endTurn", "Ends your turn.");
        }
    };

    public PlayerController(int playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;

        this.cardInHand = null;
        this.discardedCards = new ArrayList<Card>(0);
        this.bSignalPlayerNextTurnAboutKnockout = false;
        this.bIsKnockedOut = false;
        this.MessageForPlayerWhenPlayEffectWasForced = "";
        this.bProtectedByHandmaid = false;

        return;
    }

    // region Turn helper functions

    private void PrepareTerminalForTurn(Scanner scanner, Card pickedCardFromDeck) {
        App.ClearScreen(scanner);

        System.out.printf("%d) %s's turn.\n", this.playerID, this.playerName);
        System.out.print("Press enter to play your turn: ");
        scanner.nextLine();

        if (this.bIsKnockedOut && this.bSignalPlayerNextTurnAboutKnockout) {
            this.bSignalPlayerNextTurnAboutKnockout = false;

            if (this.MessageForPlayerWhenPlayEffectWasForced != null
                    && !this.MessageForPlayerWhenPlayEffectWasForced.isEmpty()) {
                System.out.print(this.MessageForPlayerWhenPlayEffectWasForced);
                this.MessageForPlayerWhenPlayEffectWasForced = "";
            }
            System.out.print("You were knocked out last turn.\n");
            System.out.print("You are not allowed to play this round anymore.\n");
            System.out.print("You may still type \"\\showAllDiscarded\" to see who knocked you out.\n");
            System.out.print("Press enter to continue: ");
            scanner.nextLine();

            return;
        }

        if (this.MessageForPlayerWhenPlayEffectWasForced != null
                && !this.MessageForPlayerWhenPlayEffectWasForced.isEmpty()) {
            System.out.print(this.MessageForPlayerWhenPlayEffectWasForced);
            System.out.print("Press enter to continue: ");
            scanner.nextLine();
            this.MessageForPlayerWhenPlayEffectWasForced = "";
        }

        System.out.printf("\n%s players are remaining:\n",
                GameInstance.GetActiveGameMode().GetRemainingPlayerCount());
        App.PrintArrayListPC(GameInstance.GetActiveGameMode().GetRemainingPlayers(), ", ", "");

        System.out.printf("\nYour new card is %s.\n",
                pickedCardFromDeck.GetAsString());
        System.out.printf("Your card from last round is %s. You have an affection of %d.\n",
                this.cardInHand.GetAsString(),
                this.affection
        );
        System.out.printf("There are %d cards left in the deck.\n",
                GameInstance.GetActiveGameMode().GetRemainingDeckSize());

        System.out.println();
        if (!GameInstance.GetActiveGameMode().GetExaminingCards().isEmpty()) {
            System.out.printf("There are %d cards open for examination:\n%s\n",
                    GameInstance.GetActiveGameMode().GetExaminingCards().size(),
                    GameInstance.GetActiveGameMode().GetExaminingCardsAsString()
            );
        }
        if (GameInstance.GetActiveGameMode().GetHiddenCard() == null)
            System.out.print("There is no hidden card.\n");
        else
            System.out.print("A hidden card is still available.\n");

        System.out.printf("\nYou currently have %d discarded cards.\n", this.discardedCards.size());
        App.PrintArrayListCard(this.discardedCards, " -> ");
        System.out.println();

        return;
    }

    // endregion Turn helper functions

    public void PlayTurn(Scanner scanner, Card pickedCardFromDeck) {
        this.bProtectedByHandmaid = false;
        this.PrepareTerminalForTurn(scanner, pickedCardFromDeck);

        boolean bPlayedTurn = false;
        while (true) {
            int RC = -1;
            String commandEntered = App.WaitForCommand(
                    scanner, PlayerController.validCommands.keySet().toArray(new String[0]));
            switch (commandEntered) {
                case "help":
                    // TODO: Make this better.
                    for (String command : PlayerController.validCommands.keySet()) {
                        System.out.printf("%s: %s\n", command, PlayerController.validCommands.get(command));
                    }
                    break;

                case "playCard":
                    if (bPlayedTurn) {
                        System.out.print("You have already played a card this turn.\n");
                        System.out.print("Type \\endTurn to end your turn.\n");

                        continue;
                    }
                    if (this.bIsKnockedOut) {
                        System.out.print("You have been knocked out of this round.\n");
                        System.out.print("You can only end your turn now.\n");

                        continue;
                    }

                    RC = pickedCardFromDeck.PlayEffect(scanner, this, true, pickedCardFromDeck, null);
                    if (RC == -1)
                        throw new RuntimeException("Something bad happened.");
                    if (RC == 1)
                        continue;
                    if (RC == 2) {
                        bPlayedTurn = true;
                        pickedCardFromDeck = null;
                        continue;
                    }
                    if (RC == 3) {
                        bPlayedTurn = true;
                        System.out.println("You have been knocked out of this round.");
                        System.out.println("You can only end your turn now.");
                        continue;
                    }

                    this.discardedCards.add(pickedCardFromDeck);
                    pickedCardFromDeck = null;
                    bPlayedTurn = true;

                    continue;

                case "playHand":
                    if (bPlayedTurn) {
                        System.out.print("You have already played a card this turn.\n");
                        System.out.print("Type \\endTurn to end your turn.\n");

                        continue;
                    }
                    if (this.bIsKnockedOut) {
                        System.out.print("You have been knocked out of this round.\n");
                        System.out.print("You can only end your turn now.\n");

                        continue;
                    }

                    RC = this.cardInHand.PlayEffect(scanner, this, true, pickedCardFromDeck, null);
                    if (RC == -1)
                        throw new RuntimeException("Something bad happened.");
                    if (RC == 1)
                        continue;
                    if (RC == 2) {
                        bPlayedTurn = true;
                        pickedCardFromDeck = null;
                        continue;
                    }
                    if (RC == 3) {
                        bPlayedTurn = true;
                        System.out.println("You have been knocked out of this round.");
                        System.out.println("You can only end your turn now.");
                        continue;
                    }
                    this.discardedCards.add(this.cardInHand);
                    this.cardInHand = pickedCardFromDeck;
                    pickedCardFromDeck = null;
                    bPlayedTurn = true;

                    continue;

                case "showHand":
                    System.out.printf("Card in hand   : %s.\n",
                            this.cardInHand.GetAsString());
                    if (pickedCardFromDeck != null)
                        System.out.printf("Card from deck : %s.\n",
                                pickedCardFromDeck.GetAsString());
                    else
                        System.out.print("Card from deck : NONE.\n");
                    break;

                case "showScore":
                    GameInstance.GetActiveGameMode().PrintAffectionOfPlayers(false);
                    break;

                case "showExaminationCards":
                    if (GameInstance.GetActiveGameMode().GetPlayerCount() != 2) {
                        System.out.println("This command is only available in a 2 player game.");
                        break;
                    }

                    System.out.printf("There are %d cards open for examination:\n%s\n",
                            GameInstance.GetActiveGameMode().GetExaminingCards().size(),
                            GameInstance.GetActiveGameMode().GetExaminingCardsAsString()
                    );
                    break;

                case "showAllDiscarded":
                    for (int i = 0; i < GameInstance.GetActiveGameMode().GetPlayerCount(); i++) {
                        System.out.printf("%s's discarded cards: ",
                                GameInstance.GetActiveGameMode().GetPlayerByID(i).GetPlayerName());
                        for (
                                int j = 0;
                                j < GameInstance.GetActiveGameMode().GetPlayerByID(i).GetDiscardedCards().length;
                                j++
                        ) {
                            System.out.printf("%s",
                                    GameInstance.GetActiveGameMode().GetPlayerByID(i).GetDiscardedCards()[j]);
                            if (j != GameInstance.GetActiveGameMode().GetPlayerByID(i).GetDiscardedCards().length - 1)
                                System.out.print(" -> ");
                            continue;
                        }

                        System.out.println();
                        continue;
                    }
                    break;

                case "showCardDetails":
                    System.out.printf("%s", this.cardInHand.GetAsDetailedString());
                    System.out.printf("\n%s", pickedCardFromDeck.GetAsDetailedString());
                    break;

                case "showAllCardDetails":
                    System.out.print("Card details:\n\n");
                    System.out.printf("%s", new PrincessAnnette().GetAsDetailedString());
                    System.out.printf("\n%s", new CountessWilhelmina().GetAsDetailedString());
                    System.out.printf("\n%s", new KingArnaud().GetAsDetailedString());
                    System.out.printf("\n%s", new PrinceArnaud().GetAsDetailedString());
                    System.out.printf("\n%s", new HandmaidSusannah().GetAsDetailedString());
                    System.out.printf("\n%s", new BaronTalus().GetAsDetailedString());
                    System.out.printf("\n%s", new PriestTomas().GetAsDetailedString());
                    System.out.printf("\n%s", new GuardOdette().GetAsDetailedString());

                    break;

                case "endTurn":
                    if (this.bIsKnockedOut)
                        return;

                    if (!bPlayedTurn) {
                        System.out.print("You have to play a card before you can end your turn.\n");
                        continue;
                    }

                    return;

                default:
                    System.out.printf("Something went wrong with your command %s.\n", commandEntered);
                    break;

            }

            continue;
        }
    }

    public Card GetCardInHand() {
        return this.cardInHand;
    }

    public void SetCardInHand(Card cardInHand) {
        this.cardInHand = cardInHand;
        return;
    }

    public int GetPlayerID() {
        return this.playerID;
    }

    public String GetPlayerName() {
        return this.playerName;
    }

    public void IncreaseAffection() {
        this.affection++;
        return;
    }

    public int GetAffection() {
        return this.affection;
    }

    public String[] GetDiscardedCards() {
        return this.discardedCards.stream().map(Card::GetName).toArray(String[]::new);
    }

    public void AddToDiscardedCards(Card card) {
        this.discardedCards.add(card);
        return;
    }

    public boolean GetSignalPlayerNextTurnAboutKnockout() {
        return this.bSignalPlayerNextTurnAboutKnockout;
    }

    public boolean GetIsKnockedOut() {
        return this.bIsKnockedOut;
    }

    public void SetIsKnockedOut(boolean bIsKnockedOut, boolean bSignalPlayerNextTurn) {
        this.bIsKnockedOut = bIsKnockedOut;
        this.bSignalPlayerNextTurnAboutKnockout = bSignalPlayerNextTurn;

        return;
    }

    public int GetSumOfAffectionInDiscardPile() {
        int sum = 0;
        for (Card card : this.discardedCards)
            sum += card.GetAffection();

        return sum;
    }

    public void ResetForNewRound() {
        this.cardInHand = null;
        this.discardedCards.clear();
        this.bSignalPlayerNextTurnAboutKnockout = false;
        this.bIsKnockedOut = false;
        this.MessageForPlayerWhenPlayEffectWasForced = "";
        this.bProtectedByHandmaid = false;

        return;
    }

    public GameMode GetActiveGameMode() {
        return GameInstance.GetActiveGameMode();
    }

    public String GetMessageForPlayerWhenPlayEffectWasForced() {
        return this.MessageForPlayerWhenPlayEffectWasForced;
    }

    public void SetMessageForPlayerWhenPlayEffectWasForced(String MessageForPlayerWhenPlayEffectWasForced) {
        this.MessageForPlayerWhenPlayEffectWasForced = MessageForPlayerWhenPlayEffectWasForced;
        return;
    }

    public void SetNewPlayerHandWithDeckOrHiddenCard() {
        this.cardInHand = GameInstance.GetActiveGameMode().DrawCard();

        return;
    }

    public boolean GetProtectedByHandmaid() {
        return this.bProtectedByHandmaid;
    }

    public void SetProtectedByHandmaid(boolean bProtectedByHandmaid) {
        this.bProtectedByHandmaid = bProtectedByHandmaid;
        return;
    }
}
