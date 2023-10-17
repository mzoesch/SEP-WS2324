package App.Cards;

import App.PlayerController;
import App.App;

import java.util.Objects;
import java.util.Scanner;


public class KingArnaud extends Card{

    public KingArnaud() {
        super(
            "King Arnaud IV",
            "The undisputed ruler of Tempest... for the moment. Because of his role in the arrest "
                + "of Queen Marianna, he does not rate as highly with Princess Annette as a father "
                + "should. He hopes to work himself back into her graces.",
            "When you discard King Arnaud IV, trade the card in your hand with the card held by another "
                + "player of your choice. You cannot trade with a player who is out of the round.",
                6
        );

        return;
    }

    /**
     * As written in the rules. <p>
     * If the owner has the Countess Wilhelmina in their hand, they must discard the Countess and take the King. <p>
     */
    @Override
    public int PlayEffect(Scanner scanner, PlayerController PC, boolean bPlayedManually, Card pickedCardFromDeck, String MessageForPlayerWhenForced) {
        if (bPlayedManually) {
            if (Objects.equals(PC.GetCardInHand().GetName(), "Countess Wilhelmina")
                    || Objects.equals(pickedCardFromDeck.GetName(), "Countess Wilhelmina")) {
                System.out.print("You must discard the Countess Wilhelmina.\n");
                return 1;
            }

            System.out.printf("%s has been played by you.\n", this.name);

            String choice = "";
            while (true) {
                System.out.print("\nChoose another player to swap hands with:\n");
                if (PC.GetActiveGameMode().GetRemainingPlayers().size() < 2) {
                    System.out.print("There are no other players to swap hands with.\n");
                    System.out.print("The effect is cancelled.\n");
                    return 0;
                }

                choice = App.WaitForInput(scanner, PC.GetActiveGameMode().GetRemainingPlayersAsStringArray());

                if (Objects.equals(PC.GetPlayerName(), choice)) {
                    // We swap the same hand. Nothing happens.
                    System.out.print("You have chosen to swap hands with yourself.\n");
                    return 0;
                }

                if (PC.GetActiveGameMode().GetPlayerControllerFromName(choice).GetProtectedByHandmaid()) {
                    System.out.printf("%s is protected by the Handmaid.\n", choice);
                    System.out.print("If all other players are protected by the Handmaid, you must choose yourself.\n");

                    continue;
                }

                break;
            }

            PC.AddToDiscardedCards(this);
            boolean bPlayedCardWasHandCard = Objects.equals(PC.GetCardInHand().GetName(), this.GetName());
            if (bPlayedCardWasHandCard)
                PC.SetCardInHand(pickedCardFromDeck);

            PC.GetActiveGameMode().SwapHands(PC, PC.GetActiveGameMode().GetPlayerControllerFromName(choice));
            System.out.printf("You have swapped hands with %s.\n", choice);
            return 2;
        }

        if (PC.GetProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.GetPlayerName());
            return 1;
        }

        PC.SetMessageForPlayerWhenPlayEffectWasForced(MessageForPlayerWhenForced);
        return 0;
    }
}
