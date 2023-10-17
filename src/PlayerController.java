import Cards.Card;
import java.util.Scanner;
import java.util.ArrayList;


public class PlayerController {
    private final int playerID;
    private int affection;

    Card cardInHand;
    private final ArrayList<Card> carrdsDiscarded;

    public PlayerController(int playerID) {
        this.playerID = playerID;
        this.cardInHand = null;
        this.carrdsDiscarded = new ArrayList<Card>(0);

        return;
    }

    public void PlayTurn(Scanner scanner, Card pickedCardFromDeck) {
        App.ClearScreen(scanner);

        System.out.printf("You are player %d and have an affection of %d.\n", this.playerID, this.affection);
        System.out.printf("Card in hand   : %s.\n", this.cardInHand.getName());
        System.out.printf("Card from deck : %s.\n", pickedCardFromDeck.getName());
        App.WaitForCommand(scanner);
        scanner.nextLine();

        return;
    }

    public Card SetCardInHand(Card card) {
        Card oldCard = this.cardInHand;
        this.cardInHand = card;

        return oldCard;
    }

    public int GetPlayerID() {
        return this.playerID;
    }

    public int IncreaseAffection() {
        this.affection++;
        // TODO: Trigger win here or in GameMode?
        return this.affection;
    }

    public int GetAffection() {
        return this.affection;
    }
}
