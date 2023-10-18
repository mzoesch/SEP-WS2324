package App.Cards;

import App.PlayerController;

import java.util.Scanner;


public class HandmaidSusannah extends Card {

    public HandmaidSusannah() {
        super(
            "Handmaid Susannah",
            "Few would trust a mere Handmaid with a letter of importance. Fewer still understand "
                + "Susannah's cleverness, or her skilled ability at playing the foolish Handmaid. That "
                + "the Queen’s confidante and loyal servant escaped any attention after the Queen's "
                + "arrest is a testament to her clever mind.",
            "When you discard the Handmaid, you are immune to the effects of other players' cards until "
                + "the start of your next turn. If all players other than the player whose turn it is are "
                + "protected by the Handmaid, the player must choose him- or herself for a card's effects, "
                + "if possible.",
                4
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
            PC.setProtectedByHandmaid(true);

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
