package App.Cards;

import App.PlayerController;

import java.util.Scanner;


public class CountessWilhelmina extends Card {

    public CountessWilhelmina() {
        super(
            "Countess Wilhelmina",
            "Always on the prowl for a handsome man or juicy gossip, Wilhelmina’s age and noble blood make her "
                + "one of Princess Annette's friends. While she has great influence over the Princess, she makes "
                + "herself scarce whenever the King or Prince are around.",
            "Unlike other cards, which take effect when discarded, the text on the Countess applies while she "
                + "is in your hand. In fact, the only time it doesn't apply is when you discard her.\nIf you ever "
                + "have the Countess and either the King or Prince in your hand, you must discard the Countess. "
                + "You do not have to reveal the other card in your hand. Of course, you can also discard the "
                + "Countess even if you do not have a royal family member in your hand. The Countess likes to play "
                + "mind games...",
                7
        );

        return;
    }

    /**
     * As written in the rules. <p>
     * If the owner has a King or Prince in their hand, they must discard the Countess. <p>
     */
    @Override
    public int PlayEffect(Scanner scanner, PlayerController PC, boolean bPlayedManually, Card pickedCardFromDeck, String MessageForPlayerWhenForced) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);
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
