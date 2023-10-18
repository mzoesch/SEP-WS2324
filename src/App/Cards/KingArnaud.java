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
     * As written in the rules. <p>
     * If the owner has the Countess Wilhelmina in their hand, they must discard the Countess and take the King. <p>
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
                        "Choose a player to swap hands with"
                    );

                if (Objects.equals(PC.getPlayerName(), choice)) {
                    System.out.print("You have chosen to swap hands with yourself.\n");
                    return Card.RC_OK;
                }

                if (PC.getActiveGameMode().getPlayerControllerByName(choice).getProtectedByHandmaid()) {
                    System.out.printf("%s is protected by the Handmaid.\n", choice);
                    System.out.print("If all other players are protected by the Handmaid, you must choose yourself.\n");

                    continue;
                }

                break;
            }

            this.updateHands(PC, PC.getActiveGameMode().getPlayerControllerByName(choice), bIsHandCard);
            return Card.RC_OK_HANDS_UPDATED;
        }

        if (PC.getProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return Card.RC_ERR;
        }

        PC.setMessageForPlayerWhenPlayEffectWasForced(messageForPlayerWhenForced);
        return Card.RC_OK;
    }
}
