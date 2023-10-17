package App.Cards;

import App.App;
import App.PlayerController;

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

    @Override
    public int PlayEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Card pickedCardFromDeck,
            String MessageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            if (Objects.equals(PC.GetCardInHand().GetName(), "Countess Wilhelmina")
                    || Objects.equals(pickedCardFromDeck.GetName(), "Countess Wilhelmina")) {
                System.out.print("You must discard the Countess Wilhelmina.\n");
                return 1;
            }

            System.out.printf("%s has been played by you.\n", this.name);

            String choice = "";
            PlayerController pcToDiscard = null;
            while (true) {
                System.out.print("\nChoose a player to discard their hand:\n");
                choice = App.WaitForInput(scanner, PC.GetActiveGameMode().GetRemainingPlayersAsStringArray());

                if (Objects.equals(choice, PC.GetPlayerName())) {
                    PC.AddToDiscardedCards(this);
                    PC.AddToDiscardedCards(pickedCardFromDeck);
                    PC.SetNewPlayerHandWithDeckOrHiddenCard();

                    System.out.printf("You have discarded your hand and drawn %s.\n",
                            PC.GetCardInHand().GetAsString());
                    return 2;
                }

                pcToDiscard = PC.GetActiveGameMode().GetPlayerControllerFromName(choice);
                if (pcToDiscard.GetProtectedByHandmaid()) {
                    System.out.printf("%s is protected by the Handmaid.\n", choice);
                    System.out.print("If all other players are protected by the Handmaid, you must choose yourself.\n");
                    continue;
                }

                break;
            }

            pcToDiscard.GetCardInHand().PlayEffect(
                    null,
                    pcToDiscard,
                    false,
                    null,
                    "You card has changed!\nA Prince Arnaud was played on you.\n"
            );
            pcToDiscard.AddToDiscardedCards(pcToDiscard.GetCardInHand());
            pcToDiscard.SetNewPlayerHandWithDeckOrHiddenCard();

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
