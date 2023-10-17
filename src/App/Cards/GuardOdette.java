package App.Cards;

import App.PlayerController;
import App.App;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class GuardOdette extends Card {

    public GuardOdette() {
        super(
                "Guard Odette",
                "Charged with seeing to the security of the royal family, Odette follows her orders with "
                + "persistence and diligence... even though her mentor is said to have drowned while fleeing "
                + "arrest for complicity in the Queen’s treason.",
                "When you discard the Guard, choose a player and name a number (other than 1). If that player "
                + "has that number in their hand, that player is knocked out of the round. If all other players "
                + "still in the round cannot be chosen (eg. due to Handmaid or Sycophant), this card is "
                + "discarded without effect.",
                1
        );

        return;
    }

    @Override
    public int PlayEffect(Scanner scanner, PlayerController PC, boolean bPlayedManually, Card pickedCardFromDeck, String MessageForPlayerWhenForced) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);

            ArrayList<PlayerController> remainingPCs = PC.GetActiveGameMode().GetRemainingPlayers();
            ArrayList<PlayerController> targetablePCs = new ArrayList<PlayerController>();
            for (PlayerController remainingPC : remainingPCs) {
                if (Objects.equals(PC.GetPlayerName(), remainingPC.GetPlayerName()))
                    continue;
                if (remainingPC.GetProtectedByHandmaid())
                    continue;

                targetablePCs.add(remainingPC);
                continue;
            }

            if (targetablePCs.isEmpty()) {
                System.out.print(
                        "There are no other players to compare hands with or they are protected by Handmaids.\n");
                System.out.print("The effect is cancelled.\n");
                return 0;
            }

            String choice =
                    App.WaitForInput(
                            scanner,
                            targetablePCs.stream().map(PlayerController::GetPlayerName).toArray(String[]::new)
                    );

            PlayerController targetPC = PC.GetActiveGameMode().GetPlayerControllerFromName(choice);
            int guess = App.WaitForInputInteger(scanner, 2, 8);
            if (targetPC.GetCardInHand().GetAffection() == guess) {
                System.out.printf("%s has the card with affection %d. %s is knocked out of the round.\n",
                        targetPC.GetPlayerName(), guess, targetPC.GetPlayerName());
                targetPC.SetIsKnockedOut(true, true);
                targetPC.SetMessageForPlayerWhenPlayEffectWasForced(
                        "A Guard was played on you and you were knocked out of the round.\n");

                return 0;
            }

            System.out.printf("%s does not have the card with affection %d.\n", targetPC.GetPlayerName(), guess);
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
