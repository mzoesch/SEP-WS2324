package app.cards;

import app.App;
import app.PlayerController;

import java.util.Objects;
import java.util.Scanner;


public class PrinceArnaud extends Card {

    public PrinceArnaud() {
        super(
            "Prince Arnaud",
            "As a social gady, Prince Arnaud was not as distressed over his mother’s arrest as one "
                + "would suppose. Since many women clamor for his attention, he hopes to help his sister "
                +" find the same banal happiness by playing matchmaker.",
            "When you discard Prince Arnaud, choose one player still in the round (including yourself). "
                + "That player discards his or her hand (but doesn't apply its effect, unless it is the "
                + "Princess, see page 8) and draws a new one. If the deck is empty and the player cannot "
                + "draw a card, that player draws the card that was removed at the start of the round. "
                + "If all other players are protected by the Handmaid, you must choose yourself.",
                5
        );

        return;
    }

    /**
     * As written in the rules. <p>
     * If the owner has the Countess Wilhelmina in their hand, they must discard the Countess and take the Prince. <p>
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
            if (Objects.equals(PC.getCardInHand().getName(), "Countess Wilhelmina")
                    || Objects.equals(PC.getPickedCardFromDeck().getName(), "Countess Wilhelmina")) {
                System.out.print("You must discard the Countess Wilhelmina.\n");
                return Card.RC_ERR;
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
                    System.out.print("You have chosen to discard your hand and draw a new one.\n");
                    PC.addCardToDiscardedCards(this);
                    if (bIsHandCard)
                        PC.setCardInHand(PC.getPickedCardFromDeck());
                    PC.setPickedCardFromDeck(null);
                    PC.addCardToDiscardedCards(PC.getCardInHand());
                    PC.overwriteCurrentHandCardWithNewDeckCard();
                    System.out.printf("You have discarded your hand and drawn %s.\n",
                            PC.getCardInHand().getAsString());

                    return Card.RC_OK_HANDS_UPDATED;
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

                if (RC == Card.RC_OK) {
                    pcToDiscard.addCardToDiscardedCards(pcToDiscard.getCardInHand());
                    pcToDiscard.overwriteCurrentHandCardWithNewDeckCard();
                    System.out.printf("%s has discarded their hand and drawn a new one.\n", choice);
                    return Card.RC_OK;
                }

                if (RC == Card.RC_ERR)
                    continue;

                if (RC == Card.RC_OK_PLAYER_KNOCKED_OUT) {
                    System.out.printf("You have knocked out %s.\n", choice);
                    return Card.RC_OK;
                }

                break;
            }

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
