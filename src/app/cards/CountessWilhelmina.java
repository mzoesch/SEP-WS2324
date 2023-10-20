package app.cards;

import app.PlayerController;

import java.util.Scanner;


/**
 * Countess Wilhelmina card. <p>
 * @see ACard <p>
 */
public class CountessWilhelmina extends ACard {

    /**
     * Name of the card. <p>
     */
    public static final String NAME = "Countess Wilhelmina";
    private static final int CARD_AFFECTION = 7;

    /**
     * Constructor. <p>
     */
    public CountessWilhelmina() {
        super(
            CountessWilhelmina.NAME,
            "Always on the prowl for a handsome man or juicy gossip, Wilhelmina’s age and noble blood make her "
                + "one of Princess Annette's friends. While she has great influence over the Princess, she makes "
                + "herself scarce whenever the King or Prince are around.",
            "Unlike other cards, which take effect when discarded, the text on the Countess applies while she "
                + "is in your hand. In fact, the only time it doesn't apply is when you discard her.\nIf you ever "
                + "have the Countess and either the King or Prince in your hand, you must discard the Countess. "
                + "You do not have to reveal the other card in your hand. Of course, you can also discard the "
                + "Countess even if you do not have a royal family member in your hand. The Countess likes to play "
                + "mind games...",
                CountessWilhelmina.CARD_AFFECTION
        );

        return;
    }

    /**
     * <b>Special Effect:</b> <p>
     * As written in the rules. If the owner has a King or Prince in their hand, they must discard this card. <p>
     * The card does not have any effect when discarded. <p>
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
