package app.cards;

import app.App;
import app.PlayerController;

import java.util.Arrays;
import java.util.Scanner;


public class PriestTomas extends Card {

    public static final String NAME = "Priest Tomas";
    private static final int CARD_AFFECTION = 2;

    public PriestTomas() {
        super(
            PriestTomas.NAME,
            "Open, honest, and uplifting, Father Tomas always seeks out the opportunity to do good. "
                + "With the arrest of the Queen, he is often seen about the palace, acting as confessor, "
                + "counselor, and friend.",
            "When you discard the Priest, you can look at another player’s hand. Do not reveal the "
                + "hand to any other players",
                PriestTomas.CARD_AFFECTION
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

            PlayerController[] targetablePCs = Card.getAllRemainingPlayersTargetableByCardEffects(PC);
            if (targetablePCs.length == 0) {
                System.out.print("All players are Protected. You are not able to see any other card.\n");
                System.out.print("The effect is cancelled.\n");
                return Card.RC_OK;
            }

            String choice = App.waitForInputStringWithValidation_V2(
                    scanner,
                    Arrays.stream(targetablePCs).map(PlayerController::getPlayerName).toArray(String[]::new),
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
