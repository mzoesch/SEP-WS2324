package appv2.cards;


/**
 * <p>King Arnaud IV card.</p>
 * @see ACard
 */
public class KingArnaud extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "King Arnaud IV";
    private static final int CARD_AFFECTION = 6;

    /**
     * <p>Constructor.</p>
     */
    public KingArnaud() {
        super(
                KingArnaud.NAME,
                "The undisputed ruler of Tempest... for the moment. Because of his role in the arrest "
                        + "of Queen Marianna, he does not rate as highly with Princess Annette as a father "
                        + "should. He hopes to work himself back into her graces.",
                "When you discard King Arnaud IV, trade the card in your hand with the card held by another "
                        + "player of your choice. You cannot trade with a player who is out of the round.",
                KingArnaud.CARD_AFFECTION
        );

        return;
    }

    @Override
    public int playCard() {
        return 0;
    }

}
