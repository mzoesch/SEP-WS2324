package app.cards;

import app.App;
import app.PlayerController;

import java.util.Objects;
import java.util.Scanner;


/**
 * <p>Prince Arnaud card.</p>
 * @see ACard
 */
public class PrinceArnaud extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Prince Arnaud";
    private static final int CARD_AFFECTION = 5;

    /**
     * <p>Constructor.</p>
     */
    public PrinceArnaud() {
        super(
            PrinceArnaud.NAME,
            "As a social gady, Prince Arnaud was not as distressed over his mother’s arrest as one "
                + "would suppose. Since many women clamor for his attention, he hopes to help his sister "
                +" find the same banal happiness by playing matchmaker.",
            "When you discard Prince Arnaud, choose one player still in the round (including yourself). "
                + "That player discards his or her hand (but doesn't apply its effect, unless it is the "
                + "Princess, see page 8) and draws a new one. If the deck is empty and the player cannot "
                + "draw a card, that player draws the card that was removed at the start of the round. "
                + "If all other players are protected by the Handmaid, you must choose yourself.",
                PrinceArnaud.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * As written in the rules. If the owner has the Countess in their hand,
     * they must discard the Countess and take the Prince.<br />
     * When discarded the player must choose another player to discard their
     * hand and draw a new one (self included).</p>
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
                            "Choose a player to discard their hand and draw a new one"
                        );

                if (Objects.equals(PC.getPlayerName(), choice)) {
                    PC.addCardToDiscardedCards(this);
                    if (bIsHandCard)
                        PC.setCardInHand(PC.getPickedCardFromDeck());
                    PC.setPickedCardFromDeck(null);
                    PC.addCardToDiscardedCards(PC.getCardInHand());
                    PC.overwriteCurrentHandCardWithNewDeckCard();
                    System.out.printf("You have discarded your hand and drawn %s.\n",
                            PC.getCardInHand().getAsString());

                    return ACard.RC_OK_HANDS_UPDATED;
                }

                PlayerController pcToDiscard = PC.getActiveGameMode().getPlayerControllerByName(choice);
                if (pcToDiscard == null)
                   throw new RuntimeException("pcToDiscard is null");

                int RC = pcToDiscard.getCardInHand().playEffect(
                        scanner,
                        pcToDiscard,
                        false,
                        null,
                        "You card has changed!\nA Prince Arnaud was played on you.\n"
                );

                if (RC == ACard.RC_OK) {
                    pcToDiscard.addCardToDiscardedCards(pcToDiscard.getCardInHand());
                    pcToDiscard.overwriteCurrentHandCardWithNewDeckCard();
                    System.out.printf("%s has discarded their hand and drawn a new one.\n", choice);
                    return ACard.RC_OK;
                }

                if (RC == ACard.RC_ERR)
                    continue;

                if (RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
                    System.out.printf("You have knocked out %s.\n", choice);
                    return ACard.RC_OK;
                }

                break;
            }

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
