import Cards.Card;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;


public class PlayerController {
    private final int playerID;
    private final String playerName;

    private int affection;

    Card cardInHand;
    private final ArrayList<Card> discardedCards;

    static final HashMap<String, String> validCommands = new HashMap<String, String>() {
        {
            put("help", "Shows this help message.");
            put("playCard", "Plays the card just picked from the deck. ");
            put("playHand", "Discard the card in your hand.");
            put("showHand", "Shows both cards in your hand.");
            put("showScore", "Shows the current score of all players.");
            put("showAllDiscarded", "Shows all discarded cards of all players.");
        }
    };

    public PlayerController(int playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;

        this.cardInHand = null;
        this.discardedCards = new ArrayList<Card>(0);

        return;
    }

    public void PlayTurn(Scanner scanner, Card pickedCardFromDeck) {
        App.ClearScreen(scanner);

        System.out.printf("%d) %s's turn.\n", this.playerID, this.playerName);
        System.out.print("Press enter to play your turn: ");
        scanner.nextLine();
        System.out.println();

        System.out.printf("Your new card is %s.\n",
                pickedCardFromDeck.GetAsString());
        System.out.printf("Your card from last round is %s. You have an affection of %d.\n",
                this.cardInHand.GetAsString(),
                this.affection
        );

        if (GameInstance.GetActiveGameMode().GetExaminedCard() == null)
            System.out.printf("There are %d cards left in the deck. There is no examine card.\n",
                    GameInstance.GetActiveGameMode().GetRemainingDeckSize());
        else
            System.out.printf("The current examine card is %s with %d remaining cards in the deck.\n\n",
                    GameInstance.GetActiveGameMode().GetExaminedCard().GetAsString(),
                    GameInstance.GetActiveGameMode().GetRemainingDeckSize()
            );

        System.out.printf("You currently have %d discarded cards.\n", this.discardedCards.size());
        for (int i = 0; i < this.discardedCards.size(); i++) {
            System.out.printf("(%d) %s", i + 1, this.discardedCards.get(i).GetName());
            if (i != this.discardedCards.size() - 1)
                System.out.print(" -> ");
            continue;
        }

        while (true) {
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
                    this.discardedCards.add(pickedCardFromDeck);
                    this.discardedCards.get(this.discardedCards.size() - 1).PlayEffect();

                    return;

                case "playHand":
                    this.discardedCards.add(this.cardInHand);
                    this.cardInHand = pickedCardFromDeck;
                    this.discardedCards.get(this.discardedCards.size() - 1).PlayEffect();

                    return;

                case "showHand":
                    System.out.printf("Card in hand   : %s.\n",
                            this.cardInHand.GetAsString());
                    System.out.printf("Card from deck : %s.\n",
                            pickedCardFromDeck.GetAsString());
                    break;

                case "showScore":
                    GameInstance.GetActiveGameMode().PrintAffectionOfPlayers(false);
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
        // TODO: Trigger win here or in GameMode?
        return;
    }

    public int GetAffection() {
        return this.affection;
    }

    public String[] GetDiscardedCards() {
        return this.discardedCards.stream().map(Card::GetName).toArray(String[]::new);
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
        return;
    }
}
