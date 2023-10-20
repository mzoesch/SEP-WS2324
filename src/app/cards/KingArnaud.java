package app.cards;

import app.App;
import app.PlayerController;

import java.util.Objects;
import java.util.Scanner;


/**
 * <p>King Arnaud IV card.</p>
 * @see ACard
 */
public class KingArnaud extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "King Arnaud IV";
    private static final int CARD_AFFECTION = 6;

    /**
     * <p>Constructor.</p>
     */
    public KingArnaud() {
        super(
            KingArnaud.NAME,
            "The undisputed ruler of Tempest... for the moment. Because of his role in the arrest "
                + "of Queen Marianna, he does not rate as highly with Princess Annette as a father "
                + "should. He hopes to work himself back into her graces.",
            "When you discard King Arnaud IV, trade the card in your hand with the card held by another "
                + "player of your choice. You cannot trade with a player who is out of the round.",
                KingArnaud.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p>Special one time use to update the hand of the players after the King was played.</p>
     *
     * @param PC The player controller who invoked the effect.
     * @param TargetPC The player controller who was chosen to swap hands with.
     * @param bIsHandCard Whether the card was in the hand of the player or just picked up from the deck.
     */
    private void updateHands(PlayerController PC, PlayerController TargetPC, Boolean bIsHandCard) {
        PC.addCardToDiscardedCards(this);

        if (bIsHandCard)
            PC.setCardInHand(PC.getPickedCardFromDeck());
        PC.setPickedCardFromDeck(null);

        PC.getActiveGameMode().swapHands(PC, TargetPC);
        System.out.printf("You have swapped hands with %s.\n", TargetPC.getPlayerName());

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * As written in the rules. If the owner has the Countess in their hand,
     * they must discard the Countess and take the King.<br />
     * When discarded the player must choose another player to swap hands with (self included).</p>
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
            if (PC.hasCountessWilhelminaInHand()) {
                System.out.print("You must discard the Countess Wilhelmina.\n");
                return ACard.RC_ERR;
            }

            System.out.printf("%s has been played by you.\n", this.name);
            String choice = null;
            while (true) {
                choice =
                    App.waitForInputStringWithValidation_V2(
                        scanner,
                        PC.getActiveGameMode().getRemainingPlayerNames(),
                        "Choose a player to swap hands with"
                    );

                if (Objects.equals(PC.getPlayerName(), choice)) {
                    System.out.print("You have chosen to swap hands with yourself.\n");
                    System.out.print("Nothing happened.\n");
                    return ACard.RC_OK;
                }

                if (PC.getActiveGameMode().getPlayerControllerByName(choice).isProtectedByHandmaid()) {
                    System.out.printf("%s is protected by the Handmaid.\n", choice);
                    System.out.print("If all other players are protected by the Handmaid, you must choose yourself.\n");

                    continue;
                }

                break;
            }

            this.updateHands(PC, PC.getActiveGameMode().getPlayerControllerByName(choice), bIsHandCard);
            return ACard.RC_OK_HANDS_UPDATED;
        }

        if (PC.isProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return ACard.RC_ERR;
        }

        PC.setMessageForPlayerWhenPlayEffectWasForced(messageForPlayerWhenForced);
        return ACard.RC_OK;
    }
}
