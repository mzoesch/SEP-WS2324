package App.Cards;

import App.PlayerController;
import App.App;

import java.util.ArrayList;
import java.util.Objects;
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
    public int playEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Boolean bIsHandCard,
            String messageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);

            ArrayList<PlayerController> targetablePCs = new ArrayList<PlayerController>();
            for (PlayerController tPC : PC.getActiveGameMode().getRemainingPlayers()) {
                if (Objects.equals(PC.getPlayerName(), tPC.getPlayerName()))
                    continue;
                if (tPC.getProtectedByHandmaid())
                    continue;

                targetablePCs.add(tPC);
                continue;
            }

            if (targetablePCs.isEmpty()) {
                System.out.print("All players are unavailable for comparison.\n");
                System.out.print("The effect is cancelled.\n");
                return Card.RC_OK;
            }

            String choice = App.waitForInputStringWithValidation_V2(
                    scanner,
                    targetablePCs.stream().map(PlayerController::getPlayerName).toArray(String[]::new),
                    "Choose a player you want to see the hand of"
                );

            PlayerController targetPC = PC.getActiveGameMode().getPlayerControllerByName(choice);
            System.out.printf("The hand of %s is: %s\n",
                    targetPC.getPlayerName(), targetPC.getCardInHand().getAsString());

            return Card.RC_OK;
        }

        if (PC.getProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return Card.RC_ERR;
        }

        PC.setMessageForPlayerWhenPlayEffectWasForced(messageForPlayerWhenForced);
        return Card.RC_OK;
    }
}
