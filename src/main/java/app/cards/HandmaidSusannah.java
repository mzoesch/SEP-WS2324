package app.cards;

import app.PlayerController;

import java.util.Scanner;


/**
 * <p>Handmaid Susannah card.</p>
 * @see ACard
 */
public class HandmaidSusannah extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Handmaid Susannah";
    private static final int CARD_AFFECTION = 4;

    /**
     * <p>Constructor.</p>
     */
    public HandmaidSusannah() {
        super(
            HandmaidSusannah.NAME,
            "Few would trust a mere Handmaid with a letter of importance. Fewer still understand "
                + "Susannah's cleverness, or her skilled ability at playing the foolish Handmaid. That "
                + "the Queen’s confidante and loyal servant escaped any attention after the Queen's "
                + "arrest is a testament to her clever mind.",
            "When you discard the Handmaid, you are immune to the effects of other players' cards until "
                + "the start of your next turn. If all players other than the player whose turn it is are "
                + "protected by the Handmaid, the player must choose him- or herself for a card's effects, "
                + "if possible.",
                HandmaidSusannah.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * When discarded the player is protected until his next turn by any card
     * effects from other players.</p>
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
            System.out.print("You are now protected until the start of your next turn.\n");
            PC.setProtectedByHandmaid(true);

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
