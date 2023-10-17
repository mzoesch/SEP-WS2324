import java.util.Scanner;
import Cards.Card;
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

    public GameMode(Scanner scanner, int playerCount) {
        this.scanner = scanner;
        this.playerCount = playerCount;
        this.deckOnTable = new ArrayList<Card>(0);

        this.players = new PlayerController[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            this.players[i] = new PlayerController(i);
        }
        this.mostRecentPlayerID = 0;

        return;
    }

    public void StartGame() {
        System.out.println("Game Started.");


        while (true) {
            // TODO: Select first player as it is written in the rules. We select the index 0 as the first player.
            this.mostRecentPlayerID = 0;

            this.StartNewRound();
            break;

            // TODO: Check for player win.
            // TODO: End game if won else continue.
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
     * - Princess (1x) <p>
     * - Countess (1x) <p>
     * - King (1x) <p>
     * - Prince (2x) <p>
     * - Handmaid (2x) <p>
     * - Baron (2x) <p>
     * - Priest (2x) <p>
     * - Guard (5x)
     */
    private void SetupTableDeck() {
        this.deckOnTable.clear();
        this.examinedCard = null;

        this.deckOnTable.add(new Card("Princess Annette"));

        this.deckOnTable.add(new Card("Countess Wilhelmina"));

        this.deckOnTable.add(new Card("King Arnaud IV"));

        this.deckOnTable.add(new Card("Prince Arnaud"));
        this.deckOnTable.add(new Card("Prince Arnaud"));

        this.deckOnTable.add(new Card("Handmaid Susannah"));
        this.deckOnTable.add(new Card("Handmaid Susannah"));

        this.deckOnTable.add(new Card("Baron Talus"));
        this.deckOnTable.add(new Card("Baron Talus"));

        this.deckOnTable.add(new Card("Priest Tomas"));
        this.deckOnTable.add(new Card("Priest Tomas"));

        this.deckOnTable.add(new Card("Guard Odette"));
        this.deckOnTable.add(new Card("Guard Odette"));
        this.deckOnTable.add(new Card("Guard Odette"));
        this.deckOnTable.add(new Card("Guard Odette"));
        this.deckOnTable.add(new Card("Guard Odette"));

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
}
