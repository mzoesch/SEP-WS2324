package app.cards;

import app.PlayerController;

import java.util.Scanner;


/**
 * <p>Princess Annette Card.</p>
 * @see ACard
 */
public class PrincessAnnette extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Princess Annette";
    private static final int CARD_AFFECTION = 8;

    /**
     * <p>Constructor.</p>
     */
    public PrincessAnnette() {
        super(
            PrincessAnnette.NAME,
            "Hampered only by the naïveté of youth, Princess Annette is elegant, charming, and beautiful. "
                + "Obviously, you want the princess to carry your letter. However, she is self-conscious "
                + "about matters of the heart, and if confronted, will toss your letter "
                + "in the fire and deny looking at any correspondence.",
            "If you discard the Princess—no matter how or why—she has tossed your letter into the fire. "
                + "You are immediately knocked out of the round. If the Princess was discarded by a card "
                + "effect, any remaining effects of that card do not apply (you do not draw a card from the "
                + "Prince, for example). Effects tied to being knocked out the round still apply (eg. "
                + "Constable, Jester), however.",
                PrincessAnnette.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * As written in the rules. If the player discards this card, they are immediately knocked out
     * of the round regardless if the play was forced or not.</p>
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
            return ACard.RC_OK_PLAYER_KNOCKED_OUT;
        }

        if (PC.isProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return ACard.RC_ERR;
        }

        PC.setIsKnockedOut(true, true, "While you were away, someone discarded your Princess Annette Card.");
        return ACard.RC_OK_PLAYER_KNOCKED_OUT;
    }
}
