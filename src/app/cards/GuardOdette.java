package app.cards;

import app.App;
import app.PlayerController;

import java.util.Arrays;
import java.util.Scanner;


/**
 * <p>Guard Odette card.</p>
 * @see ACard
 */
public class GuardOdette extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Guard Odette";
    private static final int CARD_AFFECTION = 1;

    /**
     * <p>Minimum value when guessing the affection of a hand.</p>
     */
    private static final int MIN_AFFECTION_WHEN_GUESSING = 2;
    /**
     * <p>Maximum value when guessing the affection of a hand.</p>
     */
    private static final int MAX_AFFECTION_WHEN_GUESSING = 8;

    /**
     * <p>Constructor.</p>
     */
    public GuardOdette() {
        super(
                GuardOdette.NAME,
                "Charged with seeing to the security of the royal family, Odette follows her orders with "
                + "persistence and diligence... even though her mentor is said to have drowned while fleeing "
                + "arrest for complicity in the Queen’s treason.",
                "When you discard the Guard, choose a player and name a number (other than 1). If that player "
                + "has that number in their hand, that player is knocked out of the round. If all other players "
                + "still in the round cannot be chosen (eg. due to Handmaid or Sycophant), this card is "
                + "discarded without effect.",
                GuardOdette.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * When discarded the player must guess the affection of another player's card in hand.
     * If they guess correctly, that player will be knocked out. If all players are
     * protected (e.g. by the Handmaid) the effect is being cancelled. </p>
     * <br />
     * {@inheritDoc}
     */
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

            PlayerController[] targetablePCs = ACard.getAllRemainingPlayersTargetableByCardEffects(PC);
            if (targetablePCs.length == 0) {
                System.out.print("All players are protected for comparison.\n");
                System.out.print("The effect is cancelled.\n");
                return ACard.RC_OK;
            }

            String choice = App.waitForInputStringWithValidation_V2(
                    scanner,
                    Arrays.stream(targetablePCs).map(PlayerController::getPlayerName).toArray(String[]::new),
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
                        true, true, "A Guard was played on you.\n");

                return ACard.RC_OK;
            }

            System.out.printf("You guessed wrong. %s does not have a card with affection %d.\n",
                    targetPC.getPlayerName(), guess);
            return ACard.RC_OK;
        }

        if (PC.isProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return ACard.RC_ERR;
        }

        PC.setMessageForPlayerWhenPlayEffectWasForced(messageForPlayerWhenForced);
        return ACard.RC_OK;
    }
}
