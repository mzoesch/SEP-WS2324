package appv2.cards;


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

    @Override
    public int playCard() {
        return 0;
    }

}
