package App.Cards;

import App.PlayerController;
import App.App;

import java.util.Scanner;


public class PriestTomas extends Card {

    public PriestTomas() {
        super(
            "Priest Tomas",
            "Open, honest, and uplifting, Father Tomas always seeks out the opportunity to do good. "
                + "With the arrest of the Queen, he is often seen about the palace, acting as confessor, "
                + "counselor, and friend.",
            "When you discard the Priest, you can look at another player’s hand. Do not reveal the "
                + "hand to any other players",
                2
        );

        return;
    }

    @Override
    public int PlayEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Card pickedCardFromDeck,
            String MessageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            // TODO: Maybe protect with Handmaid.

            System.out.printf("%s has been played by you.\n", this.name);

            System.out.print("\nChoose a player to look at their hand:\n");
            String choice = App.WaitForInput(scanner, PC.GetActiveGameMode().GetRemainingPlayersAsStringArray());

            if (PC.GetPlayerName().equals(choice)) {
                System.out.print("You have chosen to look at your own hand.\n");
                System.out.printf("Your hand is: %s\n", PC.GetCardInHand().GetName());
                return 0;
            }

            PlayerController target = PC.GetActiveGameMode().GetPlayerControllerFromName(choice);
            System.out.printf("The hand of %s is: %s\n", target.GetPlayerName(), target.GetCardInHand().GetAsString());

            return 0;
        }

        if (PC.GetProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.GetPlayerName());
            return 1;
        }

        PC.SetMessageForPlayerWhenPlayEffectWasForced(MessageForPlayerWhenForced);
        return 0;
    }
}
