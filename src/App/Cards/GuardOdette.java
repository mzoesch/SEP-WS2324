package App.Cards;

import App.PlayerController;
import App.App;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class GuardOdette extends Card {

    private static final int MIN_AFFECTION_WHEN_GUESSING = 2;
    private static final int MAX_AFFECTION_WHEN_GUESSING = 8;

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
                    "Choose a player you want to guess the affection of their hand "
                            + "(they will be knocked out if you guess correctly)"
            );

            PlayerController targetPC = PC.getActiveGameMode().getPlayerControllerByName(choice);
            int guess = App.waitForInputInteger_V2(
                    scanner,
                    GuardOdette.MIN_AFFECTION_WHEN_GUESSING,
                    GuardOdette.MAX_AFFECTION_WHEN_GUESSING,
                    "Take your educated guess"
            );

            if (targetPC.getCardInHand().getAffection() == guess) {
                System.out.printf("You guessed correctly. %s has been knocked out of the round.\n",
                        targetPC.getPlayerName());
                targetPC.setIsKnockedOut(
                        true, true, "A Guard was played on you and you were knocked out of the round.\n");

                return Card.RC_OK;
            }

            System.out.printf("You guessed wrong. %s does not have a card with affection %d.\n",
                    targetPC.getPlayerName(), guess);
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
