package appv2.cards;


import appv2.core.PlayerController;

/**
 * <p>Priest Tomas card.</p>
 * @see ACard
 */
public class PriestTomas extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Priest Tomas";
    private static final int CARD_AFFECTION = 2;

    /**
     * <p>Constructor.</p>
     */
    public PriestTomas() {
        super(
                PriestTomas.NAME,
                "Open, honest, and uplifting, Father Tomas always seeks out the opportunity to do good. "
                        + "With the arrest of the Queen, he is often seen about the palace, acting as confessor, "
                        + "counselor, and friend.",
                "When you discard the Priest, you can look at another player’s hand. Do not reveal the "
                        + "hand to any other players",
                PriestTomas.CARD_AFFECTION
        );

        return;
    }

    @Override
    public int playCard(PlayerController PC, boolean bPlayedManually, String messageForPlayerWhenForced, StringBuilder stdoutPipeline, StringBuilder stderrPipeline) {
        return 0;
    }

    @Override
    public int callback(PlayerController PC, PlayerController targetPC, StringBuilder stdoutPipeline, StringBuilder stderrPipeline, String messageForPlayerWhenForced) {
        return 0;
    }

}
